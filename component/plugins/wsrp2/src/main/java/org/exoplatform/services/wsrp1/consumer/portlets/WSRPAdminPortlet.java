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

package org.exoplatform.services.wsrp1.consumer.portlets;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.portlet.PortletMode;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletURLImp;
import org.exoplatform.services.wsrp.ConsumerEnvironment;
import org.exoplatform.services.wsrp.Producer;
import org.exoplatform.services.wsrp.ProducerRegistry;
import org.exoplatform.services.wsrp.WSRPConfiguration;
import org.exoplatform.services.wsrp.WSRPException;
import org.exoplatform.services.wsrp1.WSRPConstants;
import org.exoplatform.services.wsrp1.type.CookieProtocol;
import org.exoplatform.services.wsrp1.type.LocalizedString;
import org.exoplatform.services.wsrp1.type.MarkupType;
import org.exoplatform.services.wsrp1.type.PortletDescription;
import org.exoplatform.services.wsrp1.type.RegistrationContext;
import org.exoplatform.services.wsrp1.type.RegistrationData;
import org.exoplatform.services.wsrp1.type.ServiceDescription;
import org.exoplatform.services.wsrp1.type.UserContext;

public class WSRPAdminPortlet {

  private String                  portletTitle;

  private String                  producerName;

  private String                  producerURL;

  private String                  markupIntfEndpoint;

  private String                  portletManagementIntfEndpoint;

  private String                  registrationIntfEndpoint;

  private String                  serviceDescriptionIntfEndpoint;

  private String                  description   = "";

  private String[]                consumerModes;

  private String[]                consumerStates;

  private String                  consumerName  = "www.exoplatform.org";

  private String                  consumerAgent = "exoplatform.1.0";

  private ConsumerEnvironment     consumer;

  private WSRPConfiguration       conf;

  private PortletContainerConf    pcConf;

  private PortletContainerService pcService;
  
  private final Log log = ExoLogger.getLogger(getClass().getName());

  public void init(ExoContainer cont) {

    this.consumer = (ConsumerEnvironment) cont.getComponentInstanceOfType(ConsumerEnvironment.class);
    this.conf = (WSRPConfiguration) cont.getComponentInstanceOfType(WSRPConfiguration.class);
    this.pcService = (PortletContainerService) cont.getComponentInstanceOfType(PortletContainerService.class);
    this.pcConf = (PortletContainerConf) cont.getComponentInstanceOfType(PortletContainerConf.class);

    // Collection<PortletMode> modes =
    // Collections.list(pcConf.getSupportedPortletModes());
    Collection<PortletMode> modes = pcService.getSupportedPortletModes();
    // Collection<WindowState> states =
    // Collections.list(pcConf.getSupportedWindowStates());
    Collection<WindowState> states = pcService.getSupportedWindowStates();

    consumerModes = new String[modes.size()];
    Iterator<PortletMode> mIterator = modes.iterator();
    int j = 0;
    while (mIterator.hasNext()) {
      PortletMode mode = (PortletMode) mIterator.next();
      consumerModes[j] = "wsrp:" + mode.toString();
      j++;
    }

    consumerStates = new String[states.size()];
    Iterator<WindowState> sIterator = states.iterator();
    j = 0;
    while (sIterator.hasNext()) {
      WindowState state = (WindowState) sIterator.next();
      consumerStates[j] = "wsrp:" + state.toString();
      j++;
    }

    reset();
  }

  public void render(RenderInput input, RenderOutput output) throws PortletContainerException {
    try {

      output.setTitle(this.portletTitle);

      CharArrayWriter charArrayWriter = new CharArrayWriter();
      PrintWriter w = new PrintWriter(charArrayWriter);

      // ActionURL
      PortletURL actionURL = null;

      if (input.getPortletURLFactory() != null) {
        actionURL = input.getPortletURLFactory().createPortletURL(PCConstants.ACTION_STRING);
      }
      actionURL = new PortletURLImp(PCConstants.ACTION_STRING,
                                    input.getBaseURL(),
                                    input.getMarkup(),
                                    null,
                                    true,
                                    false,//input.getEscapeXml(),
                                    null);

      // actionURL.setSecure(true);
      actionURL.setWindowState(WindowState.NORMAL);
      actionURL.setPortletMode(PortletMode.VIEW);

      w.println("<center><b>Register remote producer for " + WSRPConstants.WSRP_ID
          + " plugin.</b></center><br>");
      w.println("<form name=\"producer_wsrp1_form\" method=\"post\" action=\""
          + actionURL.toString() + "\">");
      w.println("  <input type=\"hidden\" name=\"op\" value=\"\"/>");
      w.println("    <table>");
      w.println("    <tr><td><label>Producer Name</label></td>");
      w.println("      <td><input name=\"" + WSRPConstants.WAP_producerName + "\" value=\""
          + producerName + "\" type=\"text\" size=\"45\"></td></tr>");
      w.println("    <tr><td><label>Producer URL</label></td>");
      w.println("      <td><input name=\"" + WSRPConstants.WAP_producerURL + "\" value=\""
          + producerURL + "\" type=\"text\" size=\"45\"></td></tr>");
      w.println("    <tr><td><label>Markup Interface End Point</label></td>");
      w.println("      <td><input name=\"" + WSRPConstants.WAP_markupIntfEndpoint + "\" value=\""
          + markupIntfEndpoint + "\" type=\"text\" size=\"45\"></td></tr>");
      w.println("    <tr><td><label>Portlet Management Interface End Point</label></td>");
      w.println("      <td><input name=\"" + WSRPConstants.WAP_portletManagementIntfEndpoint
          + "\" value=\"" + portletManagementIntfEndpoint
          + "\" type=\"text\" size=\"45\"></td></tr>");
      w.println("    <tr><td><label>Registration Interface End Point</label></td>");
      w.println("      <td><input name=\"" + WSRPConstants.WAP_registrationIntfEndpoint
          + "\" value=\"" + registrationIntfEndpoint + "\" type=\"text\" size=\"45\"></td></tr>");
      w.println("    <tr><td><label>Service Description Interface End Point</label></td>");
      w.println("      <td><input name=\"" + WSRPConstants.WAP_serviceDescriptionIntfEndpoint
          + "\" value=\"" + serviceDescriptionIntfEndpoint
          + "\" type=\"text\" size=\"45\"></td></tr>");
      w.println("    <tr><td><label>Description</label></td>");
      w.println("      <td><textarea id=\"" + WSRPConstants.WAP_description + "\" name=\""
          + WSRPConstants.WAP_description + "\" cols=\"35\" rows=\"5\">" + description
          + "</textarea></td></tr>");
      w.println("    <tr><td colspan='2' align='center'>");
      w.println("      <a href=\"javascript:submit_producer_wsrp1_form('save');\">Save</a>");
      w.println("      <a href=\"javascript:submit_producer_wsrp1_form('reset');\">Reset</a>");
      w.println("    </td></tr>");
      w.println("  </table>");
      w.println("<script type=\"text/javascript\">");
      w.println("  function submit_producer_wsrp1_form(action) {");
      w.println("    document.producer_wsrp1_form.elements['op'].value = action;");
      w.println("    document.producer_wsrp1_form.submit();");
      w.println("  }");
      w.println("</script>");
      w.println("</form>");

      w.println("<hr>");

      w.println("<table>");
      ProducerRegistry pregistry = consumer.getProducerRegistry();
      
      // a form for unregister producer
      Iterator<Producer> i = pregistry.getAllProducers();
      ServiceDescription serviceDescr = null;
      while (i.hasNext()) {
        Producer producer = (Producer) i.next();
        try {
          serviceDescr = producer.getServiceDescription();
        } catch (WSRPException e) {
          e.printStackTrace(w);
        }
        w.println("<tr>");
        w.println("<td>");
        w.println("Name (ID)");
        w.println("</td>");
        w.println("<td>");
        w.println("Action");
        w.println("</td>");
        w.println("</tr>");
        w.println("<td>");
        w.println(producer.getName() + " ( " + producer.getID() + " )");
        w.println("</td>");
        w.println("<td>");
        w.println("<a href=\"" + actionURL.toString() + "&op=deregister&producerid="
            + producer.getID() + "\">Deregister</a><br>");
        w.println("</td>");
        w.println("</tr>");
      }
      w.println("<tr><td colspan='2'>&nbsp;<br></td></tr>");
      
      i = pregistry.getAllProducers();
      serviceDescr = null;
      while (i.hasNext()) {
        Producer producer = (Producer) i.next();
        try {
          serviceDescr = producer.getServiceDescription();
        } catch (WSRPException e) {
          e.printStackTrace(w);
        }
        w.println("<tr>");
        w.println("<td colspan='2'>");
        w.println("<b>Name - " + producer.getName() + ", ID - " + producer.getID() + "</b><br><br>");
        w.println("RegistrationInterfaceEndpoint - " + producer.getRegistrationInterfaceEndpoint()
            + "<br>");
        //        w.println("Description - " + producer.getDescription() + "<br>");
        //        RegistrationContext regCtx = producer.getRegistrationContext();
        //        if (regCtx != null) {
        //          w.println("RegistrationHandle - " + regCtx.getRegistrationHandle() + "<br>");
        //        }
        w.println("</td>");
        w.println("</tr>");
        w.println("<tr>");
        w.println("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>");
        w.println("<td>");
        w.println("<table>");
        w.println("<tr>");
        w.println("<td>Requires Registration </td>");
        String answer = "N/A";
        if (serviceDescr != null)
          answer = Boolean.toString(serviceDescr.isRequiresRegistration());
        w.println("<td>" + answer + "</td>");
        w.println("</tr>");
        w.println("<tr>");
        w.println("<td>Requires Init Cookie </td>");
        answer = "none";
        if (serviceDescr != null) {
          CookieProtocol cookie = serviceDescr.getRequiresInitCookie();
          if (cookie != null)
            answer = cookie.getValue();
        }
        w.println("<td>" + answer + "</td>");
        w.println("</tr>");
        if (serviceDescr != null) {
          PortletDescription[] portletDescriptions = serviceDescr.getOfferedPortlets();
          if (portletDescriptions != null) {
            for (int k = 0; k < portletDescriptions.length; k++) {
              PortletDescription portletDescription = portletDescriptions[k];
              w.println("<tr><td colspan='2'><b><br>"
                  + getValue(portletDescription.getDisplayName()) + "</b></td></tr>");
              w.println("<tr><td>" + "portletHandle" + "</td><td>"
                  + portletDescription.getPortletHandle().toString() + "</td></tr>");
              w.println("<tr><td>" + "groupId" + "</td><td>"
                  + portletDescription.getGroupID().toString() + "</td></tr>");
              w.println("<tr><td>" + "title" + "</td><td>"
                  + getValue(portletDescription.getTitle()) + "</td></tr>");
              w.println("<tr><td>" + "shortTitle" + "</td><td>"
                  + getValue(portletDescription.getShortTitle()) + "</td></tr>");
              w.println("<tr><td>" + "displayName" + "</td><td>"
                  + getValue(portletDescription.getDisplayName()) + "</td></tr>");
              StringBuffer value = new StringBuffer();
              LocalizedString[] keywords = portletDescription.getKeywords();
              if (keywords != null) {
                for (int j = 0; j < keywords.length; j++) {
                  value.append(getValue(keywords[j])).append(" ");
                }
              }
              w.println("<tr><td>" + "keywords" + "</td><td>" + value.toString() + "</td></tr>");
              MarkupType[] types = portletDescription.getMarkupTypes();
              value.setLength(0);
              for (int j = 0; j < types.length; j++) {
                value.append(types[j].getMimeType()).append(" ");
              }
              w.println("<tr><td>" + "markupType" + "</td><td>" + value.toString() + "</td></tr>");
              /*
              String[] userCategories = portletDescription.getUserCategories();
              String valueCategories = "";
              if (userCategories != null) {
                value.setLength(0);
                for (int j = 0; j < userCategories.length; j++) {
                  value.append(userCategories[j]).append(" ");
                }
                valueCategories = value.toString();
              }
              w.println("<tr><td>" + "userCategory" + "</td><td>" + valueCategories + "</td></tr>");
              String[] userProfileItems = portletDescription.getUserProfileItems();
              String valueProfileItem = "";
              if (userProfileItems != null) {
                value.setLength(0);
                for (int j = 0; j < userProfileItems.length; j++) {
                  value.append(userProfileItems[j]).append(" ");
                }
                valueProfileItem = value.toString();
              }
              w.println("<tr><td>" + "userProfileItem" + "</td><td>" + valueProfileItem
                  + "</td></tr>");
              w.println("<tr><td>" + "usesMethodGet" + "</td><td>"
                  + portletDescription.getUsesMethodGet().toString() + "</td></tr>");
              if (portletDescription.getDefaultMarkupSecure() != null)
                w.println("<tr><td>" + "defaultMarkupSecure" + "</td><td>"
                    + portletDescription.getDefaultMarkupSecure().toString() + "</td></tr>");
              if (portletDescription.getOnlySecure() != null)
                w.println("<tr><td>" + "onlySecure" + "</td><td>"
                    + portletDescription.getOnlySecure().toString() + "</td></tr>");
              if (portletDescription.getUserContextStoredInSession() != null)
                w.println("<tr><td>" + "userContextStoredInSession" + "</td><td>"
                    + portletDescription.getUserContextStoredInSession().toString() + "</td></tr>");
              if (portletDescription.getTemplatesStoredInSession() != null)
                w.println("<tr><td>" + "templatesStoredInSession" + "</td><td>"
                    + portletDescription.getTemplatesStoredInSession().toString() + "</td></tr>");
              if (portletDescription.getHasUserSpecificState() != null)
                w.println("<tr><td>" + "hasUserSpecificState" + "</td><td>"
                    + portletDescription.getHasUserSpecificState().toString() + "</td></tr>");
              if (portletDescription.getDoesUrlTemplateProcessing() != null)
                w.println("<tr><td>" + "doesUrlTemplateProcessing" + "</td><td>"
                    + portletDescription.getDoesUrlTemplateProcessing().toString() + "</td></tr>");
              w.println("<tr><td>" + "extensions" + "</td><td>" + "N/A" + "</td></tr>");
              */
            }
          }
        }
        w.println("</table>");
        w.println("</td>");
        w.println("</tr>");
      }
      w.println("</table>");
      // store content to output
      try {
        output.setContent((new String(charArrayWriter.toCharArray())).getBytes("utf-8"));
      } catch (java.io.UnsupportedEncodingException e) {
        output.setContent((new String(charArrayWriter.toCharArray())).getBytes());
      }
    } catch (Exception e) {
      throw new PortletContainerException("Exception in WSRPAdminPortlet");
    }

  }

  public void processAction(ActionInput input, ActionOutput output, HttpServletRequest request) {
    try {
      String action = request.getParameter("op");
      if (action.equals("reset")) {
        reset();
      }
      if (action.equals("deregister")) {
        String producerid = request.getParameter("producerid");
        if (log.isDebugEnabled())
          log.debug("WSRPAdminPortlet.processAction() producerid = " + producerid);
        ProducerRegistry pregistry = consumer.getProducerRegistry();
        Producer producer = pregistry.getProducer(producerid);
        RegistrationContext registrationContext = producer.getRegistrationContext();
        
        UserContext userContext = new UserContext();
        userContext.setUserCategories(null);
        userContext.setProfile(null);
        userContext.setUserContextKey("");
        
        producer.deregister();
        
        pregistry.removeProducer(producerid);
      }
      if (action.equals("save")) {
        producerName = request.getParameter(WSRPConstants.WAP_producerName);
        producerURL = request.getParameter(WSRPConstants.WAP_producerURL);
        markupIntfEndpoint = request.getParameter(WSRPConstants.WAP_markupIntfEndpoint);
        portletManagementIntfEndpoint = request.getParameter(WSRPConstants.WAP_portletManagementIntfEndpoint);
        registrationIntfEndpoint = request.getParameter(WSRPConstants.WAP_registrationIntfEndpoint);
        serviceDescriptionIntfEndpoint = request.getParameter(WSRPConstants.WAP_serviceDescriptionIntfEndpoint);
        description = request.getParameter("description");

        String pURL = producerURL;
        String producerId = "producer1" + Integer.toString(pURL.hashCode());
        ProducerRegistry pregistry = consumer.getProducerRegistry();
        Producer producer = pregistry.createProducerInstance();
        producer.setID(producerId);
        producer.setName(producerName);
        producer.setMarkupInterfaceEndpoint(pURL + markupIntfEndpoint);
        producer.setPortletManagementInterfaceEndpoint(pURL + portletManagementIntfEndpoint);
        producer.setRegistrationInterfaceEndpoint(pURL + registrationIntfEndpoint);
        producer.setServiceDescriptionInterfaceEndpoint(pURL + serviceDescriptionIntfEndpoint);
        producer.setDescription(description);
        producer.setDesiredLocales(new String[] { "en" });

        if (producer.isRegistrationRequired()) {
          String[] CONSUMER_SCOPES = { "chunk_data" };
          String[] CONSUMER_CUSTOM_PROFILES = { "what_more" };
          RegistrationData registrationData = new RegistrationData();
          registrationData.setConsumerName(consumerName);
          registrationData.setConsumerAgent(consumerAgent);
          registrationData.setMethodGetSupported(false);
          registrationData.setConsumerModes(consumerModes);
          registrationData.setConsumerWindowStates(consumerStates);
          registrationData.setConsumerUserScopes(CONSUMER_SCOPES);
          registrationData.setCustomUserProfileData(CONSUMER_CUSTOM_PROFILES);
          registrationData.setRegistrationProperties(null);
          registrationData.setExtensions(null);
          RegistrationContext registrationContext = producer.register(registrationData);
          if (log.isDebugEnabled())
            log.debug("WSRPAdminPortlet.processAction() registrationContext = "
                + registrationContext);
          if (log.isDebugEnabled())
            log.debug("WSRPAdminPortlet.processAction() registrationContext.getRegistrationHandle() = "
                + registrationContext.getRegistrationHandle());
        }
        pregistry.addProducer(producer);
      }
    } catch (Exception e) {
    }
  }

  protected void reset() {
    if (conf != null) {
      if (conf.getAdminPortletParams() != null) {
        portletTitle = (String) conf.getAdminPortletParams().get(WSRPConstants.WAP_portletTitle);
        consumerName = (String) conf.getAdminPortletParams().get(WSRPConstants.WAP_consumerName);
        consumerAgent = (String) conf.getAdminPortletParams().get(WSRPConstants.WAP_consumerAgent);
        producerName = (String) conf.getAdminPortletParams().get(WSRPConstants.WAP_producerName);
        producerURL = (String) conf.getAdminPortletParams().get(WSRPConstants.WAP_producerURL);
        markupIntfEndpoint = (String) conf.getAdminPortletParams()
                                          .get(WSRPConstants.WAP_markupIntfEndpoint);
        portletManagementIntfEndpoint = (String) conf.getAdminPortletParams()
                                                     .get(WSRPConstants.WAP_portletManagementIntfEndpoint);
        registrationIntfEndpoint = (String) conf.getAdminPortletParams()
                                                .get(WSRPConstants.WAP_registrationIntfEndpoint);
        serviceDescriptionIntfEndpoint = (String) conf.getAdminPortletParams()
                                                      .get(WSRPConstants.WAP_serviceDescriptionIntfEndpoint);
        description = (String) conf.getAdminPortletParams().get(WSRPConstants.WAP_description);
        return;
      }
    }
    portletTitle = "WSRPAdminPortlet";
    consumerName = "www.exoplatform.org";
    consumerAgent = "exoplatform.1.0";
    producerName = "exo producer";
    producerURL = "http://localhost:8080/wsrp/services/";
    markupIntfEndpoint = "WSRPMarkupService";
    portletManagementIntfEndpoint = "WSRPPortletManagementService";
    registrationIntfEndpoint = "WSRPRegistrationService";
    serviceDescriptionIntfEndpoint = "WSRPServiceDescriptionService";
    description = "";
  }

  protected String getValue(LocalizedString s) {
    if (s == null)
      return "";
    else
      return s.getValue();
  }

}
