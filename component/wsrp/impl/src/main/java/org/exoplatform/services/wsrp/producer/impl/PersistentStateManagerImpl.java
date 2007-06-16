/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 14 janv. 2004
 */
package org.exoplatform.services.wsrp.producer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp.exceptions.Faults;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.producer.PersistentStateManager;
import org.exoplatform.services.wsrp.producer.impl.helpers.ConsumerContext;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.RegistrationData;


import org.hibernate.Session;



/**
 * @author Mestrallet Benjamin
 *         benjmestrallet@users.sourceforge.net
 */
public class PersistentStateManagerImpl implements PersistentStateManager {
	private static final String queryStateData =
		"from sd in class org.exoplatform.services.wsrp.producer.impl.StateData " +
		"where sd.id = ?";


  //private Map mapToStoreRenderParameters;
  private WSRPConfiguration conf;
  private Log log;
  private ExoCache cache;
  private HibernateService hservice;

  public PersistentStateManagerImpl(CacheService cacheService,
                                    HibernateService hservice,
                                    WSRPConfiguration conf) throws Exception {
    this.conf = conf;
    this.hservice = hservice ;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp");
    this.cache = cacheService.getCacheInstance(getClass().getName());
    //checkDatabase(dbService);
  }

  public RegistrationData getRegistrationData(RegistrationContext registrationContext)
  throws WSRPException {
  	if (conf.isSaveRegistrationStateOnConsumer()) {
  		log.debug("Lookup registration stored on the consumer");
  		return resolveConsumerContext(registrationContext);
  	}   	
  	log.debug("Lookup registration data stored on the producer");
  	try {
  		StateData sD = load(registrationContext.getRegistrationHandle());
  		if (sD == null) {
  			return null;
  		}
  		return ((ConsumerContext) sD.getDataObject()).
			getRegistationData();
  	} catch (Exception e) {
  		log.error("Can not extract Registration data from persistent store");
  		throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
  	}
  }

  public byte[] register(String registrationHandle, RegistrationData data) throws WSRPException {
    ConsumerContext cC = new ConsumerContext(registrationHandle, data);
    if (conf.isSaveRegistrationStateOnConsumer()) {
      log.debug("Register and send the registration state to the consumer");
      try {
        byte[] bytes = IOUtil.serialize(data);
        try {
          save(registrationHandle, "java.util.Collection", new ArrayList());
        } catch (Exception e) {
          log.error("Persistence error");
          throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
        }
        return bytes;
      } catch (Exception e) {
        log.error("Can not serialize ConsumerContext", e);
        throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
      }
    }
    log.debug("Register and save the registration state in the producer");
    try {
    	save(registrationHandle, "org.exoplatform.services.wsrp.producer.impl.helpers.ConsumerContext", cC);
    } catch (Exception e) {
    	log.error("Persistence error");
    	throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
    return null;
  }

  public void deregister(RegistrationContext registrationContext) throws WSRPException {
    try {
      if (!conf.isSaveRegistrationStateOnConsumer()) {
        log.debug("Deregister the consumer (state save on producer)");
        remove(registrationContext.getRegistrationHandle());
      } else {
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
      StateData sD = load(registrationContext.getRegistrationHandle());
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
                                             RegistrationContext registrationContext)
      throws WSRPException {
    if (conf.isSaveRegistrationStateOnConsumer()) {
      Collection c = null;
      try {
        StateData sD = load(registrationContext.getRegistrationHandle());
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
    } 
    
    ConsumerContext consumerContext = null;
    try {
    	StateData sD = load(registrationContext.getRegistrationHandle());
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

  public void addConsumerConfiguredPortletHandle(String portletHandle,
                                                 RegistrationContext registrationContext)
      throws WSRPException {
    if (conf.isSaveRegistrationStateOnConsumer()) {
      Collection c = null;
      try {
        StateData sD = load(registrationContext.getRegistrationHandle());
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
      ConsumerContext consumerContext = null;
      try {
        StateData sD = load(registrationContext.getRegistrationHandle());
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
      Collection c = null;
      try {
        StateData sD = load(registrationContext.getRegistrationHandle());
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
      ConsumerContext consumerContext = null;
      try {
        StateData sD = load(registrationContext.getRegistrationHandle());
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

  public Map getNavigationalSate(String navigationalState) throws WSRPException {
    try {
      StateData sD = load(navigationalState);
      if (sD == null) {
        return null;
      }
      return (Map) sD.getDataObject();
    } catch (Exception e) {
      log.error("Can not extract Render Parameters Map from persistent store", e);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
  }

  public void putNavigationalState(String ns, Map renderParameters) throws WSRPException {
    try {      
      save(ns, "java.util.Map", renderParameters);
    } catch (Exception e) {
      log.error("Can not save Render Parameters Map from persistent store", e);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }

  }

  private RegistrationData resolveConsumerContext(RegistrationContext registrationContext)
      throws WSRPException {
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
  	StateData data = load(key);
  	if (data == null) {
  		data = new StateData();
  		data.setId(key);
  		data.setDataType(type);
  		this.cache.put(key, data);
  	}
  	data.setDataObject(o);
  	session.save(data);
  	session.flush();
  } 

  final public StateData load(String key) throws Exception {
  	StateData data = (StateData) this.cache.get(key);
  	if (data == null) {
  		Session session = this.hservice.openSession();
  		List l = session.createQuery(queryStateData).setString(0,key).list();
  		if (l.size() > 1) {
  			throw new Exception("Expect only one configuration but found" + l.size());
  		} else if (l.size() == 1) {
  			data = (StateData) l.get(0);
  			this.cache.put(key, data);
  		}
  	}
  	return data;
  }

  final public void remove(String key) throws Exception {
  	Session session = this.hservice.openSession();
  	StateData data = (StateData) this.cache.remove(key);
  	if (data == null) {
//  		List l = session.find(queryStateData, key, Hibernate.STRING);
  		List l = session.createQuery(queryStateData).setString(0,key).list();

  		if (l.size() > 1) {
  			throw new Exception("Expect only one configuration but found" + l.size());
  		} else if (l.size() == 1) {
  			data = (StateData) l.get(0);
  			this.cache.put(key, data);
  		}
  	}
  	if (data != null) {
  		session.delete(data);
  		session.flush();
  	}
  }
}