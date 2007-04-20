/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.producer.impl;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.container.SessionManagerContainer;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.exceptions.Faults;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.producer.TransientStateManager;
import org.exoplatform.services.wsrp.producer.impl.helpers.CacheControlProxy;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpSession;
import org.exoplatform.services.wsrp.type.CacheControl;
import org.exoplatform.services.wsrp.type.Templates;
import org.exoplatform.services.wsrp.type.UserContext;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 25 janv. 2004
 * Time: 17:53:18
 */

public class TransientStateManagerImpl implements TransientStateManager {

  private static final String TEMPLATE_KEY = "org.exoplatform.services.wsrp.templates.key";
  private static final String USER_CONTEXT_KEY = "org.exoplatform.services.wsrp.user.context.key";
  private Log log;
  private ExoCache cache;
  private WSRPConfiguration conf;
  protected ExoContainer cont;

  public TransientStateManagerImpl(ExoContainerContext ctx, LogService logService, CacheService cacheService,
                                   WSRPConfiguration conf) {
    this.log = logService.getLog("org.exoplatform.services.wsrp");
    this.conf = conf;
    try {
      cache = cacheService.getCacheInstance(WSRPConstants.WSRP_CACHE_REGION);
    } catch (Exception e) {
      log.debug("Can not lookup cache : " + WSRPConstants.WSRP_CACHE_REGION, e);      
    }
    cont = ctx.getContainer();
  }

  public WSRPHttpSession resolveSession(String sessionID, String user, Integer sessiontimeperiod) throws WSRPException {
    WSRPHttpSession session = null;
    log.debug("Try to lookup session with ID : " + sessionID);
    try {
      // !!! it's a very dirty hack and it will be removed as soon as possible !!!
      session = (WSRPHttpSession) cache.get(sessionID);
      WindowInfosContainer.createInstance(portalContainer, sessionID, user);
      if (sessionID != null) {
        if (session.isInvalidated()) {
          session = new WSRPHttpSession(sessionID, sessiontimeperiod);
        } else {
          session.setLastAccessTime(System.currentTimeMillis());
        }
        log.debug("Lookup session success");
      } else {
        sessionID = IdentifierUtil.generateUUID(this);
        session = new WSRPHttpSession(sessionID, sessiontimeperiod);
        cache.put(sessionID, session);
        log.debug("Create new session with ID : " + sessionID);
      }      
      return session;
    } catch (Exception e) {
      throw new WSRPException(Faults.INVALID_SESSION_FAULT, e);
    }
  }

  public void releaseSession(String sessionID) {
    try {
      cache.remove(sessionID);
      WindowInfosContainer.removeInstance(portalContainer, sessionID);
    } catch (Exception e) {
      log.debug("Can not release session : " + sessionID, e);
    }
  }

  public CacheControl getCacheControl(PortletData portletDatas) throws WSRPException {
    log.debug("Fill a CacheControl object for the portlet");
    CacheControl cacheControl = null;
    try {
      cacheControl = new CacheControl();
      String key = IdentifierUtil.generateUUID(cacheControl);
      cacheControl.setExpires(Integer.parseInt(portletDatas.getExpirationCache()));
      if(portletDatas.isCacheGlobal()){
        cacheControl.setUserScope(WSRPConstants.WSRP_GLOBAL_SCOPE_CACHE);
      } else {
        cacheControl.setUserScope(WSRPConstants.WSRP_USER_SCOPE_CACHE);
      }
      log.debug("Use Cache key : " + key);
      cacheControl.setValidateTag(key);
      CacheControlProxy proxy = new CacheControlProxy(cacheControl);
      cache.put(key, proxy);
    } catch (Exception e) {
      log.debug("Unable to cache CacheControlProxy", e);
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT, e);
    }
    return cacheControl;
  }

  public boolean validateCache(String validateTag) throws WSRPException {
    log.debug("Validate a CacheControl object : " + validateTag);
    //TODO find a better way to validate a cache
    try {
      CacheControlProxy cacheControlProxy = (CacheControlProxy) cache.get(validateTag);
      if(cacheControlProxy != null && cacheControlProxy.isValid()){
        log.debug("Consumer cache validated");
        return true;
      }
    } catch (Exception e) {
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

  public UserContext reolveUserContext(UserContext userContext, WSRPHttpSession session) {
    if(conf.isUserContextStoredInSession()){
      log.debug("Optimized mode : user context store in session");
      if(userContext == null){
        log.debug("Optimized mode : retrieve the user context from session");
        return (UserContext) session.getAttribute(USER_CONTEXT_KEY);
      } else {
        log.debug("Optimized mode : store the user context in session");
        session.setAttribute(USER_CONTEXT_KEY, userContext);
      }
    }
    return userContext;
  }
}
