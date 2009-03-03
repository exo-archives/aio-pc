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
import java.util.Iterator;
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
  
  private WSRPConfiguration   conf;

  private final Log           log            = ExoLogger.getLogger(getClass().getName());

  private ExoContainer        cont;

  private ExoCache            cache;

  private WSRPPersister       persister;

  private String              path;
  
  private List <String>  registrationHandles;

  /**
   * The service name.
   */
  private static final String SERVICE_NAME   = "PersistentStateManagerJCRImpl";

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

  public boolean isRegistered(RegistrationContext registrationContext) throws WSRPException {
    if (registrationContext.getRegistrationHandle()==null || registrationContext.getRegistrationHandle().equalsIgnoreCase(""))
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
      this.cache.put(key, data);
      this.registrationHandles.add(key);
    }
  }

  // if present in cache return it
  // else look in hservice, if one obj put into cache and return it
  //   if more exception
  //   if none return null 
  final private WSRP2StateData load(String key) throws WSRPException {
    if (key == null || key.length() == 0)
      throw new WSRPException("A key cannot be null or empty!");

    WSRP2StateData data = (WSRP2StateData) this.cache.get(key);
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
        this.cache.put(key, data);
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
  final private void remove(String key) throws WSRPException {
    if (key == null || key.length() == 0)
      throw new WSRPException("A key cannot be null or empty!");

    WSRP2StateData data = load(key);
    if (data == null) {
      this.registrationHandles.remove(key);
      return;
    }
    this.cache.remove(key);
    this.registrationHandles.remove(key);
    try {
      persister.putValue(path, key, null);
    } catch (RepositoryException e) {
      throw new WSRPException(e.getMessage(), e.getCause());
    }
  }

  public Map<String, String[]> getNavigationalState(String navigationalState) throws WSRPException {
    return getState(navigationalState);
  }

  public void putNavigationalState(String navigationalState, Map<String, String[]> renderParameters) throws WSRPException {
    putState(navigationalState, renderParameters);
  }

  public Map<String, String[]> getInteractionSate(String interactionState) throws WSRPException {
    return getState(interactionState);
  }

  public void putInteractionState(String interactionState,
                                  Map<String, String[]> interactionParameters) throws WSRPException {
    putState(interactionState, interactionParameters);
  }

  public Map<String, String[]> getResourceState(String resourceState) throws WSRPException {
    return getState(resourceState);
  }

  public void putResourceState(String resourceState, Map<String, String[]> resourceParameters) throws WSRPException {
    putState(resourceState, resourceParameters);
  }

  private Map<String, String[]> getState(String state) throws WSRPException {
    if (state == null || state.length() == 0)
      return null;
    try {
      WSRP2StateData sD = load(state);
      if (sD == null) {
        return null;
      }
      return (Map<String, String[]>) sD.getDataObject();
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  private void putState(String state, Map<String, String[]> parameters) throws WSRPException {
    try {
      save(state, "java.util.Map", parameters);
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  public Lifetime putRegistrationLifetime(String registrationHandle, Lifetime lifetime) throws WSRPException {
    return putLifetime(registrationHandle + "_registration_lifetime", lifetime);
  }

  public Lifetime getRegistrationLifetime(RegistrationContext registrationContext) throws WSRPException {
    log.debug("Look up getRegistrationLifetime");
    return getLifetime(registrationContext.getRegistrationHandle() + "_registration_lifetime");
  }

  public Lifetime putPortletLifetime(String portletHandle, Lifetime lifetime) throws WSRPException {
    return putLifetime(portletHandle + "_portlet_lifetime", lifetime);
  }

  public Lifetime getPortletLifetime(String portletHandle) throws WSRPException {
    return getLifetime(portletHandle + "_portlet_lifetime");
  }

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

  private void removeLifetime(String key) throws WSRPException {
    try {
      remove(key);
    } catch (Exception e) {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }
  
  public List<String>  getRegistrationHandles()  throws WSRPException {
    
  return this.registrationHandles;
  }

}
