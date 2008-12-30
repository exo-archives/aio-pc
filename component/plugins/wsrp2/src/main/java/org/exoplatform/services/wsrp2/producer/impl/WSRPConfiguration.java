/**
 * Copyright 2001-2007 The eXo Platform SAS         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
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

  public static final String      SAVE_REGISTRATION_STATE_ON_CONSUMER = "wsrp.save.registration.state.on.consumer";

  public static final String      SAVE_PORTLET_STATE_ON_CONSUMER      = "wsrp.save.portlet.state.on.consumer";

//  private boolean                 hasUserSpecificState;
//
//  private boolean                 doesUrlTemplateProcessing;
//
//  private boolean                 templatesStoredInSession;
//
//  private boolean                 userContextStoredInSession;
//
//  private boolean                 usesMethodGet;
//
//  private boolean                 requiresRegistration;
//
//  private boolean                 blockingInteractionOptimized;
//
//  private boolean                 saveRegistrationStateOnConsumer;
//
//  private boolean                 savePortletStateOnConsumer;

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
    
//    properties.put(WSRPConfiguration.HAS_USER_SPECIFIC_STATE, String.valueOf(hasUserSpecificState));
//    properties.put(WSRPConfiguration.DOES_URL_TEMPLATE_PROCESSING,
//                   String.valueOf(doesUrlTemplateProcessing));
//    properties.put(WSRPConfiguration.TEMPLATES_STORED_IN_SESSION,
//                   String.valueOf(templatesStoredInSession));
//    properties.put(WSRPConfiguration.USER_CONTEXT_STORED_IN_SESSION,
//                   String.valueOf(userContextStoredInSession));
//    properties.put(WSRPConfiguration.USES_METHOD_GET, String.valueOf(usesMethodGet));
//    properties.put(WSRPConfiguration.REQUIRES_REGISTRATION, String.valueOf(requiresRegistration));
//    properties.put(WSRPConfiguration.BLOCKING_INTERACTION_OPTIMIZED,
//                   String.valueOf(blockingInteractionOptimized));
//    properties.put(WSRPConfiguration.SAVE_REGISTRATION_STATE_ON_CONSUMER,
//                   String.valueOf(saveRegistrationStateOnConsumer));
//    properties.put(WSRPConfiguration.SAVE_PORTLET_STATE_ON_CONSUMER,
//                   String.valueOf(savePortletStateOnConsumer));
//    
//    
//    hasUserSpecificState = props.getProperty(WSRPConfiguration.HAS_USER_SPECIFIC_STATE)
//                                .equals("true");
//    doesUrlTemplateProcessing = props.getProperty(WSRPConfiguration.DOES_URL_TEMPLATE_PROCESSING)
//                                     .equals("true");
//    templatesStoredInSession = props.getProperty(WSRPConfiguration.TEMPLATES_STORED_IN_SESSION)
//                                    .equals("true");
//    userContextStoredInSession = props.getProperty(WSRPConfiguration.USER_CONTEXT_STORED_IN_SESSION)
//                                      .equals("true");
//    usesMethodGet = props.getProperty(WSRPConfiguration.USES_METHOD_GET).equals("true");
//    requiresRegistration = props.getProperty(WSRPConfiguration.REQUIRES_REGISTRATION)
//                                .equals("true");
//    blockingInteractionOptimized = props.getProperty(WSRPConfiguration.BLOCKING_INTERACTION_OPTIMIZED)
//                                        .equals("true");
//    saveRegistrationStateOnConsumer = props.getProperty(WSRPConfiguration.SAVE_REGISTRATION_STATE_ON_CONSUMER)
//                                           .equals("true");
//    savePortletStateOnConsumer = props.getProperty(WSRPConfiguration.SAVE_PORTLET_STATE_ON_CONSUMER)
//                                      .equals("true");

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
    adminPortletParams.put(WSRPConstants.WAP_version,
                           props.getProperty(WSRPConstants.WAP_version));
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

  public boolean isSaveRegistrationStateOnConsumer() {
    return Boolean.parseBoolean(properties.get(WSRPConfiguration.SAVE_REGISTRATION_STATE_ON_CONSUMER));
  }

  public boolean isSavePortletStateOnConsumer() {
    return Boolean.parseBoolean(properties.get(WSRPConfiguration.SAVE_PORTLET_STATE_ON_CONSUMER));
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
