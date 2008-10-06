/**
 * Copyright 2001-2007 The eXo Platform SAS         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 */

package org.exoplatform.services.wsrp.producer.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.ExoProperties;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.PropertiesParam;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp.WSRPConstants;

/**
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 23
 * janv. 2004 Time: 12:16:47
 */
public class WSRPConfiguration {

  private boolean             hasUserSpecificState;

  private boolean             doesUrlTemplateProcessing;

  private boolean             templatesStoredInSession;

  private boolean             userContextStoredInSession;

  private boolean             usesMethodGet;

  private boolean             requiresRegistration;

  private boolean             blockingInteractionOptimized;

  private boolean             saveRegistrationStateOnConsumer;

  private boolean             savePortletStateOnConsumer;

  private List<String>        excludeList        = null;

  private Map<String, String> adminPortletParams = null;

  private final Log           log;

  public WSRPConfiguration(InitParams params) {
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp1");
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
    hasUserSpecificState = props.getProperty("wsrp.has.user.specific.state").equals("true");
    doesUrlTemplateProcessing = props.getProperty("wsrp.does.url.template.processing")
                                     .equals("true");
    templatesStoredInSession = props.getProperty("wsrp.templates.stored.in.session").equals("true");
    userContextStoredInSession = props.getProperty("wsrp.user.context.stored.in.session")
                                      .equals("true");
    usesMethodGet = props.getProperty("wsrp.uses.method.get").equals("true");
    requiresRegistration = props.getProperty("wsrp.requires.registration").equals("true");
    blockingInteractionOptimized = props.getProperty("wsrp.perform.blocking.interaction.optimized")
                                        .equals("true");
    saveRegistrationStateOnConsumer = props.getProperty("wsrp.save.registration.state.on.consumer")
                                           .equals("true");
    savePortletStateOnConsumer = props.getProperty("wsrp.save.portlet.state.on.consumer")
                                      .equals("true");
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
  }

  public boolean isHasUserSpecificState() {
    return hasUserSpecificState;
  }

  public boolean isDoesUrlTemplateProcessing() {
    return doesUrlTemplateProcessing;
  }

  public boolean isTemplatesStoredInSession() {
    return templatesStoredInSession;
  }

  public boolean isUserContextStoredInSession() {
    return userContextStoredInSession;
  }

  public boolean isUsesMethodGet() {
    return usesMethodGet;
  }

  public boolean isRegistrationRequired() {
    return requiresRegistration;
  }

  public boolean isBlockingInteractionOptimized() {
    return blockingInteractionOptimized;
  }

  public boolean isSaveRegistrationStateOnConsumer() {
    return saveRegistrationStateOnConsumer;
  }

  public boolean isSavePortletStateOnConsumer() {
    return savePortletStateOnConsumer;
  }

  public List<String> getExcludeList() {
    return excludeList;
  }

  public Map<String, String> getAdminPortletParams() {
    return adminPortletParams;
  }

}
