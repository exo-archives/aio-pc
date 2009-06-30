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

import org.exoplatform.services.log.Log;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.InvalidSession;
import org.exoplatform.services.wsrp2.producer.TransientStateManager;
import org.exoplatform.services.wsrp2.producer.impl.helpers.CacheControlProxy;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpSession;
import org.exoplatform.services.wsrp2.type.CacheControl;
import org.exoplatform.services.wsrp2.type.Templates;
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 25
 *         janv. 2004 Time: 17:53:18
 */

public class TransientStateManagerImpl implements TransientStateManager {

  private static final String TEMPLATE_KEY     = "org.exoplatform.services.wsrp2.templates.key";

  private static final String USER_CONTEXT_KEY = "org.exoplatform.services.wsrp2.user.context.key";

  private Log                 log              = ExoLogger.getLogger(this.getClass().getName());

  private ExoCache   <String, Object>    cache;
  
  private WSRPConfiguration   conf;

  protected ExoContainer      cont;

  public TransientStateManagerImpl(ExoContainerContext ctx,
                                   CacheService cacheService,
                                   WSRPConfiguration conf) {
    this.conf = conf;
    try {
      cache = cacheService.getCacheInstance(WSRPConstants.WSRP_CACHE_REGION);
    } catch (Exception e) {
      if (log.isDebugEnabled())
        log.debug("Can not lookup cache : " + WSRPConstants.WSRP_CACHE_REGION, e);
    }
    cont = ctx.getContainer();
  }

  public WSRPHttpSession resolveSession(String sessionID, Integer sessiontimeperiod) throws InvalidSession {
    if (sessiontimeperiod == null)
      sessiontimeperiod = SESSION_TIME_PERIOD;

    if (log.isDebugEnabled())
      log.debug("Try to lookup session with ID : " + sessionID);

    WSRPHttpSession session = null;
    
    try {
      session = (WSRPHttpSession) cache.get(sessionID);
    } catch (Exception e) {
      throw new InvalidSession(e.getMessage(), e);
    }
    if (session != null) {
      // get from cache
      // session != null && sessionID != null
      if (session.isInvalidated()) {
        //create a new fresh session
        session = new WSRPHttpSession(sessionID, sessiontimeperiod);
        try {
          cache.remove(sessionID);
          cache.put(sessionID, session);
        } catch (Exception e) {
          throw new InvalidSession(e.getMessage(), e);
        }
      } else {
        session.setLastAccessTime(System.currentTimeMillis());
      }
      if (log.isDebugEnabled())
        log.debug("Lookup session success");
    } else if (sessionID != null) {
      // session == null && sessionID != null
      throw new InvalidSession("Session doesn't exist anymore with sessionID = '" + sessionID + "'");
    } else {
      // session == null && sessionID == null
      sessionID = IdentifierUtil.generateUUID(this);
      session = new WSRPHttpSession(sessionID, sessiontimeperiod);
      try {
        cache.put(sessionID, session);
      } catch (Exception e) {
        throw new InvalidSession(e.getMessage(), e);
      }

      WSRPHttpSession sessionNewFromCache = null;
      try {
        sessionNewFromCache = (WSRPHttpSession) cache.get(sessionID);
      } catch (Exception e) {
        throw new InvalidSession(e.getMessage(), e);
      }
      if (sessionNewFromCache == null) {
        throw new InvalidSession("Session cannot be stored in the cache (sessionID = '" + sessionID
            + "')");
      }

      if (log.isDebugEnabled())
        log.debug("Create new session with ID : " + sessionID);
    }
    return session;

  }

  public void releaseSession(String sessionID) {
    try {
      WSRPHttpSession session = (WSRPHttpSession) cache.get(sessionID);
      cache.remove(sessionID);
      WindowInfosContainer.removeInstance(cont, sessionID);
    } catch (Exception e) {
      if (log.isDebugEnabled())
        log.debug("Can not release session : " + sessionID, e);
    }
  }

  public CacheControl getCacheControl(PortletData portletDatas) throws WSRPException {
    if (log.isDebugEnabled())
      log.debug("Fill a CacheControl object for the portlet");
    CacheControl cacheControl = null;
    try {
      cacheControl = new CacheControl();
      String key = IdentifierUtil.generateUUID(cacheControl);
      cacheControl.setExpires(Integer.parseInt(portletDatas.getExpirationCache()));
      if (portletDatas.isCacheGlobal()) {
        cacheControl.setUserScope(WSRPConstants.WSRP_GLOBAL_SCOPE_CACHE);
      } else {
        cacheControl.setUserScope(WSRPConstants.WSRP_USER_SCOPE_CACHE);
      }
      if (log.isDebugEnabled())
        log.debug("Use Cache key : " + key);
      cacheControl.setValidateTag(key);
      CacheControlProxy cacheControlProxy = new CacheControlProxy(cacheControl);
      cache.put(key, cacheControlProxy);
    } catch (Exception e) {
      if (log.isDebugEnabled())
        log.debug("Unable to cache CacheControlProxy", e);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
    return cacheControl;
  }

  public boolean validateCache(String validateTag) throws WSRPException {
    if (log.isDebugEnabled())
      log.debug("Validate a CacheControl object : " + validateTag);
    // TODO find a better way to validate a cache
    try {
      CacheControlProxy cacheControlProxy = (CacheControlProxy) cache.get(validateTag);
      if (cacheControlProxy != null && cacheControlProxy.isValid()) {
        if (log.isDebugEnabled()) {
          log.debug("Consumer cache validated");
        }
        return true;
      }
    } catch (Exception e) {
      if (log.isDebugEnabled())
        log.debug("Unable to lookup CacheControlProxy", e);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
    return false;
  }

  public Templates getTemplates(WSRPHttpSession session) {
    return (Templates) session.getAttribute(TEMPLATE_KEY);
  }

  public void storeTemplates(Templates templates, WSRPHttpSession session) {
    session.setAttribute(TEMPLATE_KEY, templates);
  }

  public UserContext resolveUserContext(UserContext userContext, WSRPHttpSession session) {
    if (conf.isUserContextStoredInSession()) {
      if (log.isDebugEnabled())
        log.debug("Optimized mode : user context store in session");
      if (userContext == null) {
        if (log.isDebugEnabled())
          log.debug("Optimized mode : retrieve the user context from session");
        return (UserContext) session.getAttribute(USER_CONTEXT_KEY);
      } else {
        if (log.isDebugEnabled())
          log.debug("Optimized mode : store the user context in session");
        session.setAttribute(USER_CONTEXT_KEY, userContext);
      }
    }
    return userContext;
  }
}
