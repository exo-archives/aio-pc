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

package org.exoplatform.services.wsrp;

import org.exoplatform.Constants;

/**
 * @author Mestrallet Benjamin
 *         benjmestrallet@users.sourceforge.net
 */
public class WSRPConstants {

  public static final String WSRP_REWRITE_PREFIX = "wsrp_rewrite?";
  public static final String WSRP_REWRITE_SUFFFIX = "/wsrp_rewrite";
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
  public static final String WSRP_RESOURCE_ID = "wsrp-resourceID";
  public static final String WSRP_CACHELEVEL = "wsrp-cacheLevel";

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
  
  public static final String WSRP_PRODUCER_APP_ENCODER = "@";
  public static final String WSRP_ADMIN_PORTLET_APP = WSRP_PRODUCER_APP_ENCODER + "WSRP1App";
  public static final String WSRP_ADMIN_PORTLET_NAME = "WSRPAdminPortlet";
  public static final String WSRP_ADMIN_PORTLET_CLASS = "org.exoplatform.services.wsrp.consumer.portlets." + WSRP_ADMIN_PORTLET_NAME;
  public static final String WSRP_ADMIN_PORTLET_KEY = WSRP_ADMIN_PORTLET_APP + // portletapp name 
                                                      Constants.PORTLET_META_DATA_ENCODER + 
                                                      WSRP_ADMIN_PORTLET_NAME; //portlet name
  
  public static final String WAP_portletTitle = "portletTitle";
  public static final String WAP_consumerName = "consumerName";
  public static final String WAP_consumerAgent = "consumerAgent";
  public static final String WAP_producerName = "producerName";
  public static final String WAP_producerURL = "producerURL";
  public static final String WAP_markupIntfEndpoint = "markupIntfEndpoint";
  public static final String WAP_portletManagementIntfEndpoint = "portletManagementIntfEndpoint";
  public static final String WAP_registrationIntfEndpoint = "registrationIntfEndpoint";
  public static final String WAP_serviceDescriptionIntfEndpoint = "serviceDescriptionIntfEndpoint";
  public static final String WAP_description = "description";
  
  public static final String WSRP_ID = "WSRP1";
  
}
