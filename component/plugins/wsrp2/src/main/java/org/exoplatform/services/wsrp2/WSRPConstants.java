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

package org.exoplatform.services.wsrp2;

import org.exoplatform.Constants;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRPConstants {

  /**
   *  9.2.3 Extended BNF Description of URL formats
   */
  public static final String WSRP_REWRITE_PREFIX                    = "wsrp_rewrite?";

  /**
   *  Rewrite suffix.
   */
  public static final String WSRP_REWRITE_SUFFFIX               = "/wsrp_rewrite";

  /**
   *  Param separator.
   */
  public static final String NEXT_PARAM                         = "&";

  /**
   *  Param separator &amp;.
   */
  public static final String NEXT_PARAM_AMP                     = "&amp;";

  /**
   *  Render URL type.
   */
  public static final String URL_TYPE_RENDER                        = "render";

  /**
   *  Action URL type.
   */
  public static final String URL_TYPE_BLOCKINGACTION            = "blockingAction";

  /**
   *  Resource URL type.
   */
  public static final String URL_TYPE_RESOURCE                  = "resource";

  /**
   *  Common text name.
   */
  public static final String WSRP_FRAGMENT_ID                       = "wsrp-fragmentID";

  /**
   *  Extensions name.
   */
  public static final String WSRP_EXTENSIONS                    = "wsrp-extensions";               //

  /**
   *  Secure URL.
   */
  public static final String WSRP_SECURE_URL                        = "wsrp-secureURL";

  /**
   * WSRP mode. 
   */
  public static final String WSRP_MODE                              = "wsrp-mode";

  /**
   * WSRP state. 
   */
  public static final String WSRP_WINDOW_STATE                  = "wsrp-windowState";

  /**
   * WSRP navigation state.
   */
  public static final String WSRP_NAVIGATIONAL_STATE                = "wsrp-navigationalState";

  /**
   * WSRP navigation values.
   */
  public static final String WSRP_NAVIGATIONAL_VALUES               = "wsrp-navigationalValues";

  /**
   * WSRP interaction state.  
   */
  public static final String WSRP_INTERACTION_STATE                 = "wsrp-interactionState";

  /**
   * WSRP URL.  
   */
  public static final String WSRP_URL                               = "wsrp-url";

  /**
   * WSRP resource-id. 
   */
  public static final String WSRP_RESOURCE_ID                   = "wsrp-resourceID";

  /**
   * WSRP resource-state.
   */
  public static final String WSRP_RESOURCE_STATE                = "wsrp-resourceState";

  /**
   * WSRP cache level.
   */
  public static final String WSRP_RESOURCE_CACHEABILITY             = "wsrp-resourceCacheability";

  /**
   * WSRP1 cache level.
   */
  public static final String WSRP1_CACHELEVEL                   = "wsrp-cacheLevel";

  /**
   * WSRP requires rewrite. 
   */
  public static final String WSRP_REQUIRES_REWRITE                  = "wsrp-requiresRewrite";

  /**
   * WSRP prefer operation.
   */
  public static final String WSRP_PREFER_OPERATION              = "wsrp-preferOperation";

  /**
   * WSRP URL type.
   */
  public static final String WSRP_URL_TYPE                          = "wsrp-urlType";

  /**
   * WSRP portlet handle.
   */
  public static final String WSRP_PORTLET_HANDLE                = "wsrp-portletHandle";

  /**
   * WSRP user context key.
   */
  public static final String WSRP_USER_CONTEXT_KEY              = "wsrp-userContextKey";

  /**
   * WSRP portlet instance key.
   */
  public static final String WSRP_PORTLET_INSTANCE_KEY          = "wsrp-portletInstanceKey";

  /**
   * WSRP session ID.
   */
  public static final String WSRP_SESSION_ID                    = "wsrp-sessionID";

  /**
   * WSRP page state.
   */
  public static final String WSRP_PAGE_STATE                    = "wsrp-pageState";

  /**
   * WSRP portlet states.
   */
  public static final String WSRP_PORTLET_STATES                = "wsrp-portletStates";

  // ??
  // public static final String WSRP_CACHELEVEL = "wsrp-cacheLevel";
  /**
   * WSRP producer ID.
   */
  public static final String WSRP_PRODUCER_ID                   = "wsrp-producerID";

  /**
   * WSRP parent handle.
   */
  public static final String WSRP_PARENT_HANDLE                 = "wsrp-parentHandle";

  /**
   * WSRP paramether-prefix.
   */
  public static final String WSRP_PARAMETER_PREFIX              = "wsrp-";

  /**
   * WSRP prefix.
   */
  public static final String WSRP_PREFIX                        = "wsrp:";

  /**
   * WSRP chache region.
   */
  public static final String WSRP_CACHE_REGION                  = "wsrp";

  /**
   * WSRP user scope chache.
   */
  public static final String WSRP_USER_SCOPE_CACHE              = "wsrp:perUser";

  /**
   * WSRP global  scope cache.
   */
  public static final String WSRP_GLOBAL_SCOPE_CACHE            = "wsrp:forAll";

  /**
   * WSRP no user authentification. 
   */
  public static final String AUTH_NO_USER_AUTHENTIFICATION          = "wsrp:none";

  /**
   * WSRP password user authentification.
   */
  public static final String AUTH_PASSWORD_USER_AUTHENTIFICATION    = "wsrp:password";

  /**
   * WSRP certificate user authentification.
   */
  public static final String AUTH_CERTIFICATE_USER_AUTHENTIFICATION = "wsrp:certificate";

  /**
   * WSRP admin.
   */
  public static final String WSRP_PRODUCER_APP_ENCODER              = "@";

  /**
   * WSRP admin portlet app.
   */
  public static final String WSRP_ADMIN_PORTLET_APP             = WSRP_PRODUCER_APP_ENCODER
                                                                    + "WSRP2App";

  /**
   * WSRP admin portlet name.
   */
  public static final String WSRP_ADMIN_PORTLET_NAME            = "WSRPAdminPortlet";

  /**
   * WSRP admin portlet class.
   */
  public static final String WSRP_ADMIN_PORTLET_CLASS           = "org.exoplatform.services.wsrp2.consumer.portlets."
                                                                    + WSRP_ADMIN_PORTLET_NAME;

  /**
   * WSRP admin portlet key.
   */
  public static final String WSRP_ADMIN_PORTLET_KEY             = WSRP_ADMIN_PORTLET_APP
                                                                    + Constants.PORTLET_META_DATA_ENCODER
                                                                    + WSRP_ADMIN_PORTLET_NAME;

  /**
   * WSRP registration WAP portlet title.
   */
  public static final String WAP_portletTitle                       = "portletTitle";

  /**
   * WSRP registration WAP consumer name.
   */
  public static final String WAP_consumerName                   = "consumerName";

  /**
   * WSRP registration WAP consumer agent.
   */
  public static final String WAP_consumerAgent                  = "consumerAgent";

  /**
   * WSRP registration WAP producer name.
   */
  public static final String WAP_producerName                   = "producerName";

  /**
   * WSRP registration WAP producer URL.
   */
  public static final String WAP_producerURL                    = "producerURL";

  /**
   * WSRP registration WAP markup interface endpoint.
   */
  public static final String WAP_markupIntfEndpoint             = "markupIntfEndpoint";

  /**
   * WSRP registration WAP portlet management interface endpoint.
   */
  public static final String WAP_portletManagementIntfEndpoint  = "portletManagementIntfEndpoint";

  /**
   * WSRP registration WAP registration interface endpoint.
   */
  public static final String WAP_registrationIntfEndpoint       = "registrationIntfEndpoint";

  /**
   * WSRP registration WAP service description interface endpoint.
   */
  public static final String WAP_serviceDescriptionIntfEndpoint = "serviceDescriptionIntfEndpoint";

  /**
   * WSRP registration WAP description.
   */
  public static final String WAP_description                    = "description";

  /**
   * WSRP registration WAP version.
   */
  public static final String WAP_version                        = "version";
  
  /**
   * WSRP registration WAP lifetime.
   */
  public static final String WAP_lifetime                        = "lifetime";

  /**
   * WSRP registration WAP user attributes.
   */
  public static final String WAP_userAttributes                 = "userAttributes";

  /**
   * WSRP registration WAP user data constraint.
   */
  public static final String WAP_userDataConstraint             = "userDataConstraint";

  /**
   * WSRP default consumer agent name.
   */
  public static final String DEFAULT_consumerAgentName          = "exoplatform.2.0";

}
