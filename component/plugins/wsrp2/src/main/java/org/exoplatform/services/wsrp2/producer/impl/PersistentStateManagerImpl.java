/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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

import org.apache.commons.logging.Log;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.helper.IOUtil;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.producer.impl.helpers.ConsumerContext;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.hibernate.Session;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class PersistentStateManagerImpl implements PersistentStateManager {
  private static final String queryStateData = "from sd in class org.exoplatform.services.wsrp2.producer.impl.WSRP2StateData "
                                                 + "where sd.id = ?";

  //private Map mapToStoreRenderParameters;
  private WSRPConfiguration   conf;

  private final Log           log            = ExoLogger.getLogger(getClass().getName());

  private ExoCache            cache;

  private HibernateService    hservice;

  public PersistentStateManagerImpl(CacheService cacheService,
                                    HibernateService hservice,
                                    WSRPConfiguration conf) throws Exception {
    this.conf = conf;
    this.hservice = hservice;
    this.cache = cacheService.getCacheInstance(getClass().getName());
    //checkDatabase(dbService);
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
        log.error("Can not extract Registration data from persistent store");
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
    }
  }

  public byte[] register(String registrationHandle, RegistrationData data, Lifetime lifetime) throws WSRPException {
    if (conf.isSaveRegistrationStateOnConsumer()) {
      // If registration state on CONSUMER
      log.debug("Register and send the registration state to the consumer");
      try {
        byte[] bytes = IOUtil.serialize(data);
        try {
          save(registrationHandle, "java.util.Collection", new ArrayList());
        } catch (Exception e) {
          log.error("Persistence error");
          throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
        }
        if (lifetime != null) {
          putRegistrationLifetime(registrationHandle, lifetime);
        }
        return bytes;
      } catch (Exception e) {
        log.error("Can not serialize RegistrationData", e);
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
    } else {
      // If registration state on PRODUCER
      log.debug("Register and save the registration state in the producer");
      ConsumerContext cC = new ConsumerContext(registrationHandle, data, lifetime);
      try {
        save(registrationHandle,
             "org.exoplatform.services.wsrp2.producer.impl.helpers.ConsumerContext",
             cC);
        if (lifetime != null) {
          putRegistrationLifetime(registrationHandle, lifetime);
        }
        return null;
      } catch (Exception e) {
        log.error("Persistence error");
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
      log.error("Can not extract Registration data from persistent store");
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
        log.error("Can not extract Registration data from persistent store", e);
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
        log.error("Can not extract Registration data from persistent store");
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
        log.error("Can not extract Registration data from persistent store");
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
      c.add(portletHandle);
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
        log.error("Can not extract Registration data from persistent store");
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
      consumerContext.addPortletHandle(portletHandle);
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
        log.error("Can not extract Registration data from persistent store");
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
      c.remove(portletHandle);
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
        log.error("Can not extract Registration data from persistent store");
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
      consumerContext.removePortletHandle(portletHandle);
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
      log.error("Can not deserialize the RegistrationData object sent by the consumer");
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
    if (o instanceof RegistrationData) {
      return (RegistrationData) o;
    }
    log.error("The registration state is not of type RegistrationData");
    throw new WSRPException(Faults.OPERATION_FAILED_FAULT);
  }

  final public void save(String key, String type, Object o) throws Exception {
    Session session = this.hservice.openSession();
    WSRP2StateData data = load(key);
    if (data == null) {
      data = new WSRP2StateData();
      data.setId(key);
      data.setDataType(type);
      this.cache.put(key, data);
    } else {
      session.delete(data);
      session.flush();
      if (log.isDebugEnabled()) {
        log.debug("Same data id is going to stored with: '" + data.getId() + "'");
      }
    }
    data.setDataObject(o);
    session.save(data);
    session.flush();
  }

  final public WSRP2StateData load(String key) throws Exception {
    WSRP2StateData data = (WSRP2StateData) this.cache.get(key);
    if (data == null) {
      Session session = this.hservice.openSession();
      List<WSRP2StateData> l = session.createQuery(queryStateData).setString(0, key).list();
      if (l.size() > 1) {
        throw new Exception("Expect only one configuration but found" + l.size());
      } else if (l.size() == 1) {
        data = (WSRP2StateData) l.get(0);
        this.cache.put(key, data);
      }
    }
    return data;
  }

  final public void remove(String key) throws Exception {
    Session session = this.hservice.openSession();
    WSRP2StateData data = (WSRP2StateData) this.cache.remove(key);
    if (data == null) {
      //  		List l = session.find(queryStateData, key, Hibernate.STRING);
      List<WSRP2StateData> l = session.createQuery(queryStateData).setString(0, key).list();
      if (l.size() > 1) {
        throw new Exception("Expect only one configuration but found" + l.size());
      } else if (l.size() == 1) {
        data = (WSRP2StateData) l.get(0);
        this.cache.put(key, data);
      }
    } else {
      session.delete(data);
      session.flush();
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
    try {
      WSRP2StateData sD = load(state);
      if (sD == null) {
        return null;
      }
      return (Map<String, String[]>) sD.getDataObject();
    } catch (Exception e) {
      log.error("Can not extract Render Parameters Map from persistent store", e);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  private void putState(String state, Map<String, String[]> parameters) throws WSRPException {
    try {
      save(state, "java.util.Map", parameters);
    } catch (Exception e) {
      log.error("Can not save Render Parameters Map to persistent store", e);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  public Lifetime getRegistrationLifetime(RegistrationContext registrationContext) throws WSRPException {
    log.debug("Look up getRegistrationLifetime");
    try {
      WSRP2StateData sD = load(registrationContext.getRegistrationHandle() + "lifetime");
      if (sD == null) {
        return null;
      }
      return (Lifetime) sD.getDataObject();
    } catch (Exception e) {
      log.error("Can not extract Registration Lifetime from persistent store", e);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  public Lifetime putRegistrationLifetime(String registrationHandle, Lifetime lifetime) throws WSRPException {
    if (lifetime == null) {
      try {
        remove(registrationHandle + "lifetime");
      } catch (Exception e) {
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
      return null;
    }
    try {
      save(registrationHandle + "lifetime",
           "org.exoplatform.services.wsrp2.type.Lifetime",
           lifetime);
    } catch (Exception e) {
      log.error("Can not save Registration Lifetime to persistent store", e);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
    return lifetime;
  }

}
