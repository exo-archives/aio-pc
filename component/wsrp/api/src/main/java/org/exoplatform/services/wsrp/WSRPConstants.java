/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 12 janv. 2004
 */
package org.exoplatform.services.wsrp;


/**
 * @author Mestrallet Benjamin
 *         benjmestrallet@users.sourceforge.net
 */
public class WSRPConstants {

  public static final String WSRP_REWITE_PREFIX = "wsrp_rewrite?";
  public static final String WSRP_REWITE_SUFFFIX = "/wsrp_rewrite";
  public static final String NEXT_PARAM = "&";
  public static final String NEXT_PARAM_AMP = "&amp;";

  public static final String WSRP_URL_TYPE = "wsrp-urlType";
  public static final String WSRP_URL = "wsrp-url";
  public static final String WSRP_REQUIRES_REWRITE = "wsrp-requiresRewrite";
  public static final String WSRP_NAVIGATIONAL_STATE = "wsrp-navigationalState";
  public static final String WSRP_INTERACTION_STATE = "wsrp-interactionState";
  public static final String WSRP_MODE = "wsrp-mode";
  public static final String WSRP_WINDOW_STATE = "wsrp-windowState";
  public static final String WSRP_FRAGMENT_ID = "wsrp-fragmentID";
  public static final String WSRP_SECURE_URL = "wsrp-secureURL";
  public static final String WSRP_PORTLET_HANDLE = "wsrp-portletHandle";
  public static final String WSRP_USER_CONTEXT_KEY = "wsrp-userContextKey";
  public static final String WSRP_PORTLET_INSTANCE_KEY = "wsrp-portletInstanceKey";
  public static final String WSRP_SESSION_ID = "wsrp-sessionID";

  public static final String URL_TYPE_BLOCKINGACTION = "blockingAction";
  public static final String URL_TYPE_RENDER = "render";
  public static final String URL_TYPE_RESOURCE = "resource";

  public static final String WSRP_PREFIX = "wsrp:";

  public static final String WSRP_CACHE_REGION = "wsrp";
  public static final String WSRP_USER_SCOPE_CACHE = "wsrp:perUser";
  public static final String WSRP_GLOBAL_SCOPE_CACHE = "wsrp:forAll";


  public static final String WSRP_PRODUCER_ID = "wsrp-producerID";
  public static final String WSRP_PARENT_HANDLE = "wsrp-parentHandle";

  public static final String NO_USER_AUTHENTIFICATION = WSRP_PREFIX + "none";
  public static final String PASSWORD_USER_AUTHENTIFICATION = WSRP_PREFIX + "password";
  public static final String CERTIFICATE_USER_AUTHENTIFICATION = WSRP_PREFIX + "certificate";
}
