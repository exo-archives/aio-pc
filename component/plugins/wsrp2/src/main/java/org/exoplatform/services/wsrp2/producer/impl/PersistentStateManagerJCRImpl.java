/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp2.producer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.helper.IOUtil;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.peristence.WSRPPersister;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.producer.impl.helpers.ConsumerContext;
import org.exoplatform.services.wsrp2.producer.impl.utils.CalendarUtils;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class PersistentStateManagerJCRImpl implements PersistentStateManager {

  /** The conf. */
  private WSRPConfiguration   conf;

  /** The log. */
  private final Log           log          = ExoLogger.getLogger(getClass().getName());

  /** The cont. */
  private ExoContainer        cont;

  /** The cache. */
  private ExoCache <String, WSRP2StateData>    cache;

  /** The persister. */
  private WSRPPersister       persister;

  /** The path. */
  private String              path;

  /** The registration handles. */
  private List<String>        registrationHandles;

  /**
   * The service name.
   */
  private static final String SERVICE_NAME = "PersistentStateManagerJCRImpl";

  /**
   * Instantiates a new persistent state manager jcr impl.
   * 
   * @param ctx the ctx
   * @param cacheService the cache service
   * @param conf the conf
   * @param persister the persister
   * @param params the params
   * @throws Exception the exception
   */
  public PersistentStateManagerJCRImpl(ExoContainerContext ctx,
                                       CacheService cacheService,
                                       WSRPConfiguration conf,
                                       WSRPPersister persister,
                                       InitParams params) throws Exception {
    this.cont = ctx.getContainer();
    this.conf = conf;
    this.cache = cacheService.getCacheInstance(getClass().getName());
    this.persister = persister;
    this.path = params.getValueParam("path").getValue();
    this.registrationHandles = new ArrayList<String>();
  }

  public RegistrationData getRegistrationData(RegistrationContext registrationContext) throws WSRPException {
    if (conf.isSaveRegistrationStateOnConsumer()) {
      // If registration state on CONSUMER
      log.debug("Lookup registration stored on the consumer");
      return resolveRegistrationData(registrationContext);
    } else {
      // If registration state on PRODUCER
      log.debug("Lookup registration data stored on the producer");
      try {
        WSRP2StateData sD = load(registrationContext.getRegistrationHandle());
        if (sD == null) {
          return null;
        }
        return ((ConsumerContext) sD.getDataObject()).getRegistationData();
      } catch (Exception e) {
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#register(java.lang.String, org.exoplatform.services.wsrp2.type.RegistrationData)
   */
  public byte[] register(String registrationHandle, RegistrationData data) throws WSRPException {
    if (conf.isSaveRegistrationStateOnConsumer()) {
      // If registration state on CONSUMER
      log.debug("Register and send the registration state to the consumer");
      try {
        byte[] bytes = IOUtil.serialize(data);
        try {
          save(registrationHandle, "java.util.Collection", new ArrayList());
        } catch (Exception e) {
          throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
        }
        return bytes;
      } catch (Exception e) {
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
    } else {
      // If registration state on PRODUCER
      log.debug("Register and save the registration state in the producer");
      ConsumerContext cC = new ConsumerContext(registrationHandle, data);
      try {
        save(registrationHandle,
             "org.exoplatform.services.wsrp2.producer.impl.helpers.ConsumerContext",
             cC);
        return null;
      } catch (Exception e) {
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
    }

  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#deregister(org.exoplatform.services.wsrp2.type.RegistrationContext)
   */
  public void deregister(RegistrationContext registrationContext) throws WSRPException {
    try {
      if (!conf.isSaveRegistrationStateOnConsumer()) {
        // If registration state on CONSUMER
        log.debug("Deregister the consumer (state save on producer)");
        remove(registrationContext.getRegistrationHandle());
      } else {
        // If registration state on PRODUCER
        log.debug("Deregister the consumer (state save on consumer)");
        remove(registrationContext.getRegistrationHandle());
      }
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#isRegistered(org.exoplatform.services.wsrp2.type.RegistrationContext)
   */
  public boolean isRegistered(RegistrationContext registrationContext) throws WSRPException {
    if (registrationContext.getRegistrationHandle() == null
        || registrationContext.getRegistrationHandle().length() == 0)
      return false;
    log.debug("Look up from a registration stored");
    try {
      WSRP2StateData sD = load(registrationContext.getRegistrationHandle());
      if (sD == null) {
        return false;
      }
      if (sD.getDataObject() != null) {
        return true;
      }
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
    log.debug("Look up failed");
    return false;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#isConsumerConfiguredPortlet(java.lang.String, org.exoplatform.services.wsrp2.type.RegistrationContext)
   */
  public boolean isConsumerConfiguredPortlet(String portletHandle,
                                             RegistrationContext registrationContext) throws WSRPException {
    if (conf.isSaveRegistrationStateOnConsumer()) {
      // If registration state on CONSUMER
      Collection c = null;
      try {
        WSRP2StateData sD = load(registrationContext.getRegistrationHandle());
        if (sD == null) {
          return false;
        }
        c = (Collection) sD.getDataObject();
      } catch (Exception e) {
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
      if (c.contains(portletHandle)) {
        return true;
      }
      return false;
    } else {
      // If registration state on PRODUCER
      ConsumerContext consumerContext = null;
      try {
        WSRP2StateData sD = load(registrationContext.getRegistrationHandle());
        if (sD == null) {
          return false;
        }
        consumerContext = (ConsumerContext) sD.getDataObject();
      } catch (Exception e) {
//        log.error("Can not extract Registration data from persistent store");
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
      return consumerContext.isPortletHandleRegistered(portletHandle);
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#addConsumerConfiguredPortletHandle(java.lang.String, org.exoplatform.services.wsrp2.type.RegistrationContext)
   */
  public void addConsumerConfiguredPortletHandle(String portletHandle,
                                                 RegistrationContext registrationContext) throws WSRPException {
    if (conf.isSaveRegistrationStateOnConsumer()) {
      // If registration state on CONSUMER
      Collection c = null;
      try {
        WSRP2StateData sD = load(registrationContext.getRegistrationHandle());
        if (sD == null) {
          return;
        }
        c = (Collection) sD.getDataObject();
      } catch (Exception e) {
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
      c.add(portletHandle);
      save(registrationContext.getRegistrationHandle(), "java.util.Collection", c);
    } else {
      // If registration state on PRODUCER
      ConsumerContext consumerContext = null;
      try {
        WSRP2StateData sD = load(registrationContext.getRegistrationHandle());
        if (sD == null) {
          return;
        }
        consumerContext = (ConsumerContext) sD.getDataObject();
      } catch (Exception e) {
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
      consumerContext.addPortletHandle(portletHandle);
      save(registrationContext.getRegistrationHandle(),
           "org.exoplatform.services.wsrp2.producer.impl.helpers.ConsumerContext",
           consumerContext);
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#removeConsumerConfiguredPortletHandle(java.lang.String, org.exoplatform.services.wsrp2.type.RegistrationContext)
   */
  public void removeConsumerConfiguredPortletHandle(String portletHandle,
                                                    RegistrationContext registrationContext) throws WSRPException {
    if (conf.isSaveRegistrationStateOnConsumer()) {
      // If registration state on CONSUMER
      Collection c = null;
      try {
        WSRP2StateData sD = load(registrationContext.getRegistrationHandle());
        if (sD == null) {
          return;
        }
        c = (Collection) sD.getDataObject();
      } catch (Exception e) {
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
      c.remove(portletHandle);
      save(registrationContext.getRegistrationHandle(), "java.util.Collection", c);
    } else {
      // If registration state on PRODUCER
      ConsumerContext consumerContext = null;
      try {
        WSRP2StateData sD = load(registrationContext.getRegistrationHandle());
        if (sD == null) {
          return;
        }
        consumerContext = (ConsumerContext) sD.getDataObject();
      } catch (Exception e) {
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
      consumerContext.removePortletHandle(portletHandle);
      save(registrationContext.getRegistrationHandle(),
           "org.exoplatform.services.wsrp2.producer.impl.helpers.ConsumerContext",
           consumerContext);

    }
  }

  /**
   * Resolve registration data.
   * 
   * @param registrationContext the registration context
   * @return the registration data
   * @throws WSRPException the WSRP exception
   */
  private RegistrationData resolveRegistrationData(RegistrationContext registrationContext) throws WSRPException {
    byte[] registrationState = registrationContext.getRegistrationState();
    if (registrationState == null) {
      throw new WSRPException(Faults.MISSING_PARAMETERS_FAULT);
    }
    Object o = null;
    try {
      o = IOUtil.deserialize(registrationState);
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
    if (o instanceof RegistrationData) {
      return (RegistrationData) o;
    }
    throw new WSRPException(Faults.OPERATION_FAILED_FAULT);
  }

  /**
   * Save.
   * 
   * @param key the key
   * @param type the type
   * @param o the o
   * @throws WSRPException the WSRP exception
   */
  final private void save(String key, String type, Object o) throws WSRPException {

    if (key == null || key.length() == 0)
      throw new WSRPException("A key cannot be null or empty!");

    if (o == null) {
      remove(key);
    } else {
      WSRP2StateData data = new WSRP2StateData();
      data.setId(key);
      data.setDataType(type);
      data.setDataObject(o);
      try {
        String value = new String(data.getData());
        persister.putValue(path, key, value);
      } catch (Exception e) {
        throw new WSRPException(e.getMessage());
      }
      try {
        this.cache.put(key, data);
      } catch (Exception e) {
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }

      this.registrationHandles.add(key);
    }
  }

  // if present in cache return it
  // else look in hservice, if one obj put into cache and return it
  //   if more exception
  //   if none return null 
  /**
   * Load.
   * 
   * @param key the key
   * @return the wSR p2 state data
   * @throws WSRPException the WSRP exception
   */
  final private WSRP2StateData load(String key) throws WSRPException {
    if (key == null || key.length() == 0)
      throw new WSRPException("A key cannot be null or empty!");

    WSRP2StateData data = null;
    try {
      data =  this.cache.get(key);
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }

    if (data == null) {
      data = new WSRP2StateData();
      data.setId(key);
      String value = null;
      try {
        value = persister.getValue(path, key);
      } catch (RepositoryException e) {
        throw new WSRPException(e.getMessage(), e);
      }
      if (value == null) {
        return null;
      } else {
        try {
          this.cache.put(key, data);
        } catch (Exception e) {
          throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
        }
        if ( ! this.registrationHandles.contains(key)) // To prevent duplicate keys;
        this.registrationHandles.add(key);
      }
      try {
        data.setData(value.getBytes());
      } catch (Exception e) {
        throw new WSRPException(e.getMessage());
      }
      
    }

    return data;
  }

  // get and remove from cache
  // if null get from hservice
  //  if more than one result throw exception
  //  if one result: get from DB and put into cache
  // else delete from DB
  /**
   * Removes the.
   * 
   * @param key the key
   * @throws WSRPException the WSRP exception
   */
  final private void remove(String key) throws WSRPException {
    if (key == null || key.length() == 0)
      throw new WSRPException("A key cannot be null or empty!");

    WSRP2StateData data = load(key);
    if (data == null) {
      this.registrationHandles.remove(key);
      return;
    }
    try {
      this.cache.remove(key);
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }

    this.registrationHandles.remove(key);
    try {
      persister.putValue(path, key, null);
    } catch (RepositoryException e) {
      throw new WSRPException(e.getMessage(), e.getCause());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#getNavigationalState(java.lang.String)
   */
  public Map<String, String[]> getNavigationalState(String navigationalState) throws WSRPException {
    return getState(navigationalState);
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#putNavigationalState(java.lang.String, java.util.Map)
   */
  public void putNavigationalState(String navigationalState, Map<String, String[]> renderParameters) throws WSRPException {
    putState(navigationalState, renderParameters);
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#getInteractionSate(java.lang.String)
   */
  public Map<String, String[]> getInteractionSate(String interactionState) throws WSRPException {
    return getState(interactionState);
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#putInteractionState(java.lang.String, java.util.Map)
   */
  public void putInteractionState(String interactionState,
                                  Map<String, String[]> interactionParameters) throws WSRPException {
    putState(interactionState, interactionParameters);
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#getResourceState(java.lang.String)
   */
  public Map<String, String[]> getResourceState(String resourceState) throws WSRPException {
    return getState(resourceState);
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#putResourceState(java.lang.String, java.util.Map)
   */
  public void putResourceState(String resourceState, Map<String, String[]> resourceParameters) throws WSRPException {
    putState(resourceState, resourceParameters);
  }

  /**
   * Gets the state.
   * 
   * @param state the state
   * @return the state
   * @throws WSRPException the WSRP exception
   */
  private Map<String, String[]> getState(String state) throws WSRPException {
    if (state == null || state.length() == 0)
      return null;
    try {
      WSRP2StateData sD = load(state + "_state");
      if (sD == null) {
        return null;
      }
      return (Map<String, String[]>) sD.getDataObject();
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  /**
   * Put state.
   * 
   * @param state the state
   * @param parameters the parameters
   * @throws WSRPException the WSRP exception
   */
  private void putState(String state, Map<String, String[]> parameters) throws WSRPException {
    try {
      save(state + "_state", "java.util.Map", parameters);
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#putRegistrationLifetime(java.lang.String, org.exoplatform.services.wsrp2.type.Lifetime)
   */
  public Lifetime putRegistrationLifetime(String registrationHandle, Lifetime lifetime) throws WSRPException {
    return putLifetime(registrationHandle + "_registration_lifetime", lifetime);
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#getRegistrationLifetime(org.exoplatform.services.wsrp2.type.RegistrationContext)
   */
  public Lifetime getRegistrationLifetime(RegistrationContext registrationContext) throws WSRPException {
    log.debug("Look up getRegistrationLifetime");
    return getLifetime(registrationContext.getRegistrationHandle() + "_registration_lifetime");
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#putPortletLifetime(java.lang.String, org.exoplatform.services.wsrp2.type.Lifetime)
   */
  public Lifetime putPortletLifetime(String portletHandle, Lifetime lifetime) throws WSRPException {
    return putLifetime(portletHandle.hashCode() + "_portlet_lifetime", lifetime);
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#getPortletLifetime(java.lang.String)
   */
  public Lifetime getPortletLifetime(String portletHandle) throws WSRPException {
    return getLifetime(portletHandle.hashCode() + "_portlet_lifetime");
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#getLifetime(java.lang.String)
   */
  public Lifetime getLifetime(String key) throws WSRPException {
    log.debug("Look up getRegistrationLifetime");
    try {
      WSRP2StateData sD = load(key);
      if (sD == null) {
        return null;
      }
      Lifetime lf = (Lifetime) sD.getDataObject();
      if (lf != null)
        lf.setCurrentTime(CalendarUtils.getNow());
      return lf;
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  /**
   * Put lifetime.
   * 
   * @param key the key
   * @param lifetime the lifetime
   * @return the lifetime
   * @throws WSRPException the WSRP exception
   */
  private Lifetime putLifetime(String key, Lifetime lifetime) throws WSRPException {
    if (lifetime == null) {
      removeLifetime(key);
      return null;
    }
    try {
      save(key, "org.exoplatform.services.wsrp2.type.Lifetime", lifetime);
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
    return lifetime;
  }

  /**
   * Removes the lifetime.
   * 
   * @param key the key
   * @throws WSRPException the WSRP exception
   */
  private void removeLifetime(String key) throws WSRPException {
    try {
      remove(key);
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  /**
   * @see org.exoplatform.services.wsrp2.producer.PersistentStateManager#getRegistrationHandles()
   */
  public List<String> getRegistrationHandles() throws WSRPException {

    return this.registrationHandles;
  }

}
