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

package org.exoplatform.services.wsrp2;

import org.exoplatform.Constants;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRPConstants {

  // 9.2.3 Extended BNF Description of URL formats
  public static final String WSRP_REWRITE_PREFIX                = "wsrp_rewrite?";

  public static final String WSRP_REWRITE_SUFFFIX               = "/wsrp_rewrite";

  public static final String NEXT_PARAM                         = "&";

  public static final String NEXT_PARAM_AMP                     = "&amp;";

  // WSRPURLTypes
  public static final String URL_TYPE_RENDER                    = "render";

  public static final String URL_TYPE_BLOCKINGACTION            = "blockingAction";

  public static final String URL_TYPE_RESOURCE                  = "resource";

  // CommonTextName
  public static final String WSRP_FRAGMENT_ID                   = "wsrp-fragmentID";

  public static final String WSRP_EXTENSIONS                    = "wsrp-extensions";               //

  // CommonBooleanName
  public static final String WSRP_SECURE_URL                    = "wsrp-secureURL";

  // RenderTextName
  public static final String WSRP_MODE                          = "wsrp-mode";

  public static final String WSRP_WINDOW_STATE                  = "wsrp-windowState";

  // opaque
  public static final String WSRP_NAVIGATIONAL_STATE            = "wsrp-navigationalState";

  // it is parameter which defines updates to the public values
  public static final String WSRP_NAVIGATIONAL_VALUES           = "wsrp-navigationalValues";

  // ActionTextName (+RenderTextName above)
  public static final String WSRP_INTERACTION_STATE             = "wsrp-interactionState";

  // ResourceTextName
  public static final String WSRP_URL                           = "wsrp-url";

  public static final String WSRP_RESOURCE_ID                   = "wsrp-resourceID";

  public static final String WSRP_RESOURCE_STATE                = "wsrp-resourceState";

  // was: WSRP_CACHELEVEL = "wsrp-cacheLevel"
  public static final String WSRP_RESOURCE_CACHEABILITY         = "wsrp-resourceCacheability";

  // ResourceBooleanName
  public static final String WSRP_REQUIRES_REWRITE              = "wsrp-requiresRewrite";

  public static final String WSRP_PREFER_OPERATION              = "wsrp-preferOperation";

  // ParameterName
  public static final String WSRP_URL_TYPE                      = "wsrp-urlType";

  public static final String WSRP_PORTLET_HANDLE                = "wsrp-portletHandle";

  public static final String WSRP_USER_CONTEXT_KEY              = "wsrp-userContextKey";

  public static final String WSRP_PORTLET_INSTANCE_KEY          = "wsrp-portletInstanceKey";

  public static final String WSRP_SESSION_ID                    = "wsrp-sessionID";

  public static final String WSRP_PAGE_STATE                    = "wsrp-pageState";

  public static final String WSRP_PORTLET_STATES                = "wsrp-portletStates";

  // ??
  // public static final String WSRP_CACHELEVEL = "wsrp-cacheLevel";
  public static final String WSRP_PRODUCER_ID                   = "wsrp-producerID";

  public static final String WSRP_PARENT_HANDLE                 = "wsrp-parentHandle";

  public static final String WSRP_PARAMETER_PREFIX              = "wsrp-";

  public static final String WSRP_PREFIX                        = "wsrp:";

  public static final String WSRP_CACHE_REGION                  = "wsrp";

  public static final String WSRP_USER_SCOPE_CACHE              = "wsrp:perUser";

  public static final String WSRP_GLOBAL_SCOPE_CACHE            = "wsrp:forAll";

  public static final String NO_USER_AUTHENTIFICATION           = WSRP_PREFIX + "none";

  public static final String PASSWORD_USER_AUTHENTIFICATION     = WSRP_PREFIX + "password";

  public static final String CERTIFICATE_USER_AUTHENTIFICATION  = WSRP_PREFIX + "certificate";

  // FOR WSRP STARTER
  public static final String WSRP_ID                            = "WSRP2";

  // WSRP ADMIN
  public static final String WSRP_PRODUCER_APP_ENCODER          = "@";

  public static final String WSRP_ADMIN_PORTLET_APP             = WSRP_PRODUCER_APP_ENCODER
                                                                    + "WSRP2App";

  public static final String WSRP_ADMIN_PORTLET_NAME            = "WSRPAdminPortlet";

  public static final String WSRP_ADMIN_PORTLET_CLASS           = "org.exoplatform.services.wsrp2.consumer.portlets."
                                                                    + WSRP_ADMIN_PORTLET_NAME;

  public static final String WSRP_ADMIN_PORTLET_KEY             = WSRP_ADMIN_PORTLET_APP
                                                                    + Constants.PORTLET_META_DATA_ENCODER
                                                                    + WSRP_ADMIN_PORTLET_NAME;

  // WSRP REGISTRATION PARAMS
  public static final String WAP_portletTitle                   = "portletTitle";

  public static final String WAP_consumerName                   = "consumerName";

  public static final String WAP_consumerAgent                  = "consumerAgent";

  public static final String WAP_producerName                   = "producerName";

  public static final String WAP_producerURL                    = "producerURL";

  public static final String WAP_markupIntfEndpoint             = "markupIntfEndpoint";

  public static final String WAP_portletManagementIntfEndpoint  = "portletManagementIntfEndpoint";

  public static final String WAP_registrationIntfEndpoint       = "registrationIntfEndpoint";

  public static final String WAP_serviceDescriptionIntfEndpoint = "serviceDescriptionIntfEndpoint";

  public static final String WAP_description                    = "description";

}
