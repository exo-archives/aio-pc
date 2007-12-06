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
package org.exoplatform.services.wsrp.consumer.util;
public interface ConsumerConstants {
  
  /**
   * Key in the property file which is used to set the implementation
   * of the consumer environment.
   */
  public final String CONSUMER_ENV_CLASS = "consumer.enviroment.class";
  // some constants used within the proxy portlet
  public static final String WSRP_INSTANCE_KEY = "wsrp_portlet_instance_key";
  public static final String WSRP_SESSION_ID = "wsrp_session_id";
  public static final String WSRP_MARKUP_CACHE = "wsrp_markup_cache";
  public static final String WSRP_GROUPSESSIONS = "wsrp_group_sessions";
  public static final String WSRP_PORTLET_MAN_PORT = "wsrp_portlet_manag_port";
  public static final String WSRP_MARKUP_PORT = "wsrp_markup_port";
  public static final String WSRP_INIT_COOKIE_DONE = "wsrp_init_cookie_done";
  public static final String WSRP_INIT_COOKIE_REQ = "wsrp_init_cookie_required";
  // consumer to end-user authentication methods
  public static final String NONE = "wsrp:none";
  public static final String PASSWORD = "wsrp:password";
  public static final String CERTIFICATE = "wsrp:certificate";
  // keys for proxy portlet preferences
  public static final String WSRP_PORTLET_HANDLE = "wsrp_portlet_handle";
  public static final String WSRP_PARENT_HANDLE = "wsrp_parent_handle";
  public static final String WSRP_PRODUCER_ID = "wsrp_producer_id";
  public static final String WSRP_REGISTRATION_URL = "wsrp_reg_url";
  public static final String WSRP_SERVICE_DESC_URL = "wsrp_service_desc_url";
  public static final String WSRP_PORTLET_SERV_URL = "wsrp_portlet_serv_url";
  public static final String WSRP_PORTLET_MGMT_URL = "wsrp_portlet_mgmt_url";
  public static final String WSRP_MARKUP_URL = "wsrp_markup_url";
}