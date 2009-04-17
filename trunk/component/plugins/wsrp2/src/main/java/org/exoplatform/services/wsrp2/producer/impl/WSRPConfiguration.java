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
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.ExoProperties;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.PropertiesParam;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.type.StateChange;

/**
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 23
 * janv. 2004 Time: 12:16:47
 */
public class WSRPConfiguration {

  /**
   * WSRP Configuration constants
   */

  public static final String      HAS_USER_SPECIFIC_STATE             = "wsrp.has.user.specific.state";

  public static final String      DOES_URL_TEMPLATE_PROCESSING        = "wsrp.does.url.template.processing";

  public static final String      TEMPLATES_STORED_IN_SESSION         = "wsrp.templates.stored.in.session";

  public static final String      USER_CONTEXT_STORED_IN_SESSION      = "wsrp.user.context.stored.in.session";

  public static final String      USES_METHOD_GET                     = "wsrp.uses.method.get";

  public static final String      REQUIRES_REGISTRATION               = "wsrp.requires.registration";

  public static final String      BLOCKING_INTERACTION_OPTIMIZED      = "wsrp.perform.blocking.interaction.optimized";

  public static final String      HANDLE_EVENTS_OPTIMIZED             = "wsrp.handle.events.optimized";

  public static final String      SAVE_REGISTRATION_STATE_ON_CONSUMER = "wsrp.save.registration.state.on.consumer";

  public static final String      SAVE_PORTLET_STATE_ON_CONSUMER      = "wsrp.save.portlet.state.on.consumer";

  public static final String      COOKIE_PROTOCOL                     = "wsrp.cookie.protocol";

  public static final String      PORTLET_STATE_CHANGE                = "wsrp.portlet.state.change";

  private HashMap<String, String> properties;

  private List<String>            excludeList;

  private HashMap<String, String> adminPortletParams;

  private final Log               log;

  public WSRPConfiguration(InitParams params) {
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
    PropertiesParam param = params.getPropertiesParam("wsrp-conf");
    init(param.getProperties());
    PropertiesParam paramAdminPortlet = params.getPropertiesParam("wsrp-admin-portlet-conf");
    initParamsAdminPortlet(paramAdminPortlet.getProperties());
    if (params.getValuesParam("exclude-list") != null)
      excludeList = (List<String>) params.getValuesParam("exclude-list").getValues();
    else
      excludeList = new ArrayList<String>();
    excludeList.add(WSRPConstants.WSRP_ADMIN_PORTLET_KEY.substring(0, 5) + "*");
    log.info(" excludeList = " + excludeList);
  }

  private void init(ExoProperties props) {

    if (properties == null)
      properties = new HashMap<String, String>();
    properties.putAll(props);

  }

  private void initParamsAdminPortlet(ExoProperties props) {
    if (adminPortletParams == null)
      adminPortletParams = new HashMap<String, String>();
    adminPortletParams.put(WSRPConstants.WAP_portletTitle,
                           props.getProperty(WSRPConstants.WAP_portletTitle));
    adminPortletParams.put(WSRPConstants.WAP_consumerName,
                           props.getProperty(WSRPConstants.WAP_consumerName));
    adminPortletParams.put(WSRPConstants.WAP_consumerAgent,
                           props.getProperty(WSRPConstants.WAP_consumerAgent));
    adminPortletParams.put(WSRPConstants.WAP_producerName,
                           props.getProperty(WSRPConstants.WAP_producerName));
    adminPortletParams.put(WSRPConstants.WAP_producerURL,
                           props.getProperty(WSRPConstants.WAP_producerURL));
    adminPortletParams.put(WSRPConstants.WAP_markupIntfEndpoint,
                           props.getProperty(WSRPConstants.WAP_markupIntfEndpoint));
    adminPortletParams.put(WSRPConstants.WAP_portletManagementIntfEndpoint,
                           props.getProperty(WSRPConstants.WAP_portletManagementIntfEndpoint));
    adminPortletParams.put(WSRPConstants.WAP_registrationIntfEndpoint,
                           props.getProperty(WSRPConstants.WAP_registrationIntfEndpoint));
    adminPortletParams.put(WSRPConstants.WAP_serviceDescriptionIntfEndpoint,
                           props.getProperty(WSRPConstants.WAP_serviceDescriptionIntfEndpoint));
    adminPortletParams.put(WSRPConstants.WAP_description,
                           props.getProperty(WSRPConstants.WAP_description));
    adminPortletParams.put(WSRPConstants.WAP_version, props.getProperty(WSRPConstants.WAP_version));
    adminPortletParams.put(WSRPConstants.WAP_userAttributes,
                           props.getProperty(WSRPConstants.WAP_userAttributes));
    adminPortletParams.put(WSRPConstants.WAP_userDataConstraint,
                           props.getProperty(WSRPConstants.WAP_userDataConstraint));
  }

  public boolean isHasUserSpecificState() {
    return Boolean.parseBoolean(properties.get(WSRPConfiguration.HAS_USER_SPECIFIC_STATE));
  }

  public boolean isDoesUrlTemplateProcessing() {
    return Boolean.parseBoolean(properties.get(WSRPConfiguration.DOES_URL_TEMPLATE_PROCESSING));
  }

  public boolean isTemplatesStoredInSession() {
    return Boolean.parseBoolean(properties.get(WSRPConfiguration.TEMPLATES_STORED_IN_SESSION));
  }

  public boolean isUserContextStoredInSession() {
    return Boolean.parseBoolean(properties.get(WSRPConfiguration.USER_CONTEXT_STORED_IN_SESSION));
  }

  public boolean isUsesMethodGet() {
    return Boolean.parseBoolean(properties.get(WSRPConfiguration.USES_METHOD_GET));
  }

  public boolean isRegistrationRequired() {
    return Boolean.parseBoolean(properties.get(WSRPConfiguration.REQUIRES_REGISTRATION));
  }

  public boolean isBlockingInteractionOptimized() {
    return Boolean.parseBoolean(properties.get(WSRPConfiguration.BLOCKING_INTERACTION_OPTIMIZED));
  }

  public boolean isHandleEventsOptimized() {
    return Boolean.parseBoolean(properties.get(WSRPConfiguration.HANDLE_EVENTS_OPTIMIZED));
  }

  public boolean isSaveRegistrationStateOnConsumer() {
    return Boolean.parseBoolean(properties.get(WSRPConfiguration.SAVE_REGISTRATION_STATE_ON_CONSUMER));
  }

  public boolean isSavePortletStateOnConsumer() {
    return Boolean.parseBoolean(properties.get(WSRPConfiguration.SAVE_PORTLET_STATE_ON_CONSUMER));
  }

  public String getCookieProtocol() {
    return properties.get(WSRPConfiguration.COOKIE_PROTOCOL);
  }

  public StateChange getPortletStateChange() {
    return StateChange.fromValue(properties.get(WSRPConfiguration.PORTLET_STATE_CHANGE));
  }

  public List<String> getExcludeList() {
    if (excludeList == null)
      excludeList = new ArrayList<String>();
    return excludeList;
  }

  public HashMap<String, String> getAdminPortletParams() {
    if (adminPortletParams == null)
      adminPortletParams = new HashMap<String, String>();
    return adminPortletParams;
  }

  public HashMap<String, String> getProperties() {
    if (properties == null)
      properties = new HashMap<String, String>();
    return properties;
  }

}
