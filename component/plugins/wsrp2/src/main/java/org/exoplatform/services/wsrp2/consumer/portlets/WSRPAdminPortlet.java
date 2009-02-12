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

package org.exoplatform.services.wsrp2.consumer.portlets;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

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
import org.exoplatform.services.portletcontainer.pci.EventInput;
import org.exoplatform.services.portletcontainer.pci.EventOutput;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.ResourceInput;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletURLImp;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.consumer.ConsumerEnvironment;
import org.exoplatform.services.wsrp2.consumer.Producer;
import org.exoplatform.services.wsrp2.consumer.ProducerRegistry;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.impl.WSRPConfiguration;
import org.exoplatform.services.wsrp2.producer.impl.utils.CalendarUtils;
import org.exoplatform.services.wsrp2.type.CookieProtocol;
import org.exoplatform.services.wsrp2.type.Deregister;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.LocalizedString;
import org.exoplatform.services.wsrp2.type.MarkupType;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.Register;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.UserContext;

public class WSRPAdminPortlet {

  private String                  portletTitle;

  private String                  producerName;

  private String                  producerURL;

  private String                  markupIntfEndpoint;

  private String                  portletManagementIntfEndpoint;

  private String                  registrationIntfEndpoint;

  private String                  serviceDescriptionIntfEndpoint;

  private String                  description        = "";

  private Integer                 version            = 2;

//security
  private String                  userAttributes     = null;

  private String                  userDataConstraint = null;

  private List<String>            consumerModes      = new ArrayList<String>();

  private List<String>            consumerStates     = new ArrayList<String>();

  private String                  consumerName       = "www.exoplatform.org";

  private String                  consumerAgent      = "exoplatform.2.0";

  private ConsumerEnvironment     consumer;

  private WSRPConfiguration       conf;

  private PortletContainerConf    pcConf;

  private PortletContainerService pcService;

  private final Log               log                = ExoLogger.getLogger(getClass().getName());

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

    consumerModes = new ArrayList<String>();
    Iterator<PortletMode> mIterator = modes.iterator();
    while (mIterator.hasNext()) {
      PortletMode mode = (PortletMode) mIterator.next();
      consumerModes.add("wsrp:" + mode.toString());
    }

    consumerStates = new ArrayList<String>();
    Iterator<WindowState> sIterator = states.iterator();
    while (sIterator.hasNext()) {
      WindowState state = (WindowState) sIterator.next();
      consumerStates.add("wsrp:" + state.toString());
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
      
      w.println("<script type=\"text/javascript\">");
      w.println("  function submit_producer_wsrp2_form(action) {");
      w.println("    document.producer_wsrp2_form.elements['op'].value = action;");
      w.println("    document.producer_wsrp2_form.submit();");
      w.println("  }");
      w.println("</script>");


      w.println("<center><b>Register remote producer for " //+ WSRPConstants.WSRP_ID
          + " plugin.</b></center><br>");
      
      
      w.println("<table rows=\"1\" cols=\"2\">");
      w.println("<tr valign=\"top\"><td width=\"40%\">");
      
      
      w.println("<form name=\"producer_wsrp2_form\" method=\"post\" action=\""
          + actionURL.toString() + "\">");
      w.println("  <input type=\"hidden\" name=\"op\" value=\"\"/>");
      w.println("    <table border=\"1\">");
      w.println("    <tr><td><label style=\"font-size:12px; font-weight:bold;\">Producer Name</label></td>");
      w.println("      <td><input name=\"" + WSRPConstants.WAP_producerName + "\" value=\""
          + producerName + "\" type=\"text\" size=\"45\"></td></tr>");
      w.println("    <tr><td><label style=\"font-size:12px; font-weight:bold;\">Producer URL</label></td>");
      w.println("      <td><input name=\"" + WSRPConstants.WAP_producerURL + "\" value=\""
          + producerURL + "\" type=\"text\" size=\"45\"></td></tr>");
//      w.println("    <tr><td><label>Markup Interface End Point</label></td>");
//      w.println("      <td><input name=\"" + WSRPConstants.WAP_markupIntfEndpoint + "\" value=\""
//          + markupIntfEndpoint + "\" type=\"text\" size=\"45\"></td></tr>");
//      w.println("    <tr><td><label>Portlet Management Interface End Point</label></td>");
//      w.println("      <td><input name=\"" + WSRPConstants.WAP_portletManagementIntfEndpoint
//          + "\" value=\"" + portletManagementIntfEndpoint
//          + "\" type=\"text\" size=\"45\"></td></tr>");
//      w.println("    <tr><td><label>Registration Interface End Point</label></td>");
//      w.println("      <td><input name=\"" + WSRPConstants.WAP_registrationIntfEndpoint
//          + "\" value=\"" + registrationIntfEndpoint + "\" type=\"text\" size=\"45\"></td></tr>");
//      w.println("    <tr><td><label>Service Description Interface End Point</label></td>");
//      w.println("      <td><input name=\"" + WSRPConstants.WAP_serviceDescriptionIntfEndpoint
//          + "\" value=\"" + serviceDescriptionIntfEndpoint
//          + "\" type=\"text\" size=\"45\"></td></tr>");
      w.println("    <tr><td><label style=\"font-size:12px; font-weight:bold;\">Description</label></td>");
      w.println("      <td><textarea id=\"" + WSRPConstants.WAP_description + "\" name=\""
          + WSRPConstants.WAP_description + "\" cols=\"35\" rows=\"5\">" + description
          + "</textarea></td></tr>");

      w.println("    <tr><td><label style=\"font-size:12px; font-weight:bold;\">Version</label></td>");
      w.println("      <td><select id=\"" + WSRPConstants.WAP_version + "\" name=\"" + WSRPConstants.WAP_version + "\">"); 
      w.println("<option value=\"1\""); 
         if (version == 1) w.println(" SELECTED");
     w.println( ">WSRP v.1"); 
      w.println("<option value=\"2\"");
        if (version == 2) w.println(" SELECTED");
      w.println( ">WSRP v.2");
      w.println( "</select></td></tr>");
      
      w.println("    <tr><td><label style=\"font-size:12px; font-weight:bold;\">Lifetime</label></td>");
      w.println("      <td><input type=\"text\" id=\"" + WSRPConstants.WAP_lifetime + "\" name=\"" + WSRPConstants.WAP_lifetime + "\">"); 
      w.println( "</input>&nbsp; <span style=\"font-size:12px; \" >e.g.: \"2h\", \"30m\"</span></td></tr>");


      w.println("    <tr><td colspan='2' align='center'>");
      w.println("      <input type=\"button\" onclick=\"submit_producer_wsrp2_form('save');\"  value=\"Save\">");
      w.println("      <input type=\"button\"  onclick=\" submit_producer_wsrp2_form('reset');\" value=\"Reset\">");
      w.println("</form>");
      w.println("    </td></tr>");
      w.println("  </table>");
      
      w.println("</td><td>");
      

      w.println("<table border=\"1\">");
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
        w.println("<tr bgcolor=\"#FFFFF0\">");
        w.println("<td>");
        w.println(" <label style=\"font-size:12px; font-weight:bold;\">Name (ID)</label>");
        w.println("</td>");
        w.println("<td>");
        w.println(" <label style=\"font-size:12px; font-weight:bold;\">Lifetime</label>");
        w.println("</td>");
        w.println("<td>");
        w.println("<label style=\"font-size:12px; font-weight:bold;\">Action</label>");
        w.println("</td>");
        w.println("</tr>");
        w.println("<td>");
        w.println("<label style=\"font-size:12px; \">" + producer.getName() + " ( " + producer.getID() + " ) </label>");
        w.println("</td>");

        RegistrationContext rContext = producer.getRegistrationContext();
        if (rContext != null){
        Lifetime destruction = rContext.getScheduledDestruction();
        if (destruction != null) {
       GregorianCalendar calend = destruction.getTerminationTime().toGregorianCalendar();
       SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        w.println("<td>");
        w.println("<label style=\"font-size:12px; \">&nbsp;" +   format.format(calend.getTime()) + "&nbsp;" + "</label>");
        w.println("</td>");
          }
        }else {
          w.println("<td>");
          w.println("<label style=\"font-size:12px; \">&nbsp;Permanent&nbsp;" + "</label>");
          w.println("</td>");
        }
        
        w.println("<td>");
        w.println("<a href=\"" + actionURL.toString() + "&op=deregister&producerid="
            + producer.getID() + "\"  style=\"font-size:12px; \">Deregister</a><br>");
        w.println("</td>");
        w.println("</tr>");
      }
      w.println("</table>");
      
      w.println("</td></tr></table>");
      
      w.println("<hr>");
     // w.println("<h2>Detailed info</h2>");
      
      
      w.println("<table border=\"1\">");

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
        w.println("<label style=\"font-size:15px; font-weight:bold;\">Name - " + producer.getName() + ", ID - " + producer.getID() + "</label><br><br>");
//        w.println("RegistrationInterfaceEndpoint - " + producer.getRegistrationInterfaceEndpoint()
//            + "<br>");

//        w.println("Description - " + producer.getDescription() + "<br>");
//        RegistrationContext regCtx = producer.getRegistrationContext();
//        if (regCtx != null) {
//          w.println("RegistrationHandle - " + regCtx.getRegistrationHandle() + "<br>");
//          if (regCtx.getScheduledDestruction() != null) {
//            w.println("CurrentTime - " + regCtx.getScheduledDestruction().getCurrentTime() + "<br>");
//            w.println("TerminationTime - " + regCtx.getScheduledDestruction().getTerminationTime() + "<br>");
//          }
//        }
        w.println("</td>");
        w.println("</tr>");
        
        
        w.println("<tr>");
        w.println("<td><span style=\"font-size:12px; font-weight:bold;\" >Requires Registration</span></td>");
        String answer = "N/A";
        if (serviceDescr != null)
          answer = Boolean.toString(serviceDescr.isRequiresRegistration());
        w.println("<td><span style=\"font-size:12px; \" >" + answer + "</span></td>");
        w.println("</tr>");
        w.println("<tr>");
        w.println("<td><span style=\"font-size:12px; font-weight:bold;\" >Requires Init Cookie </span></td>");
        answer = "none";
        if (serviceDescr != null) {
          CookieProtocol cookie = serviceDescr.getRequiresInitCookie();
          if (cookie != null)
            answer = cookie.value();
        }
        w.println("<td><span style=\"font-size:12px; \" >" + answer + "</span></td>");
        w.println("</tr>");
        
        w.println("<tr>");
        w.println("<td colspan='2'>");
        w.println("<table border =\"1\">");
        
        if (serviceDescr != null) {
          List<PortletDescription> portletDescriptions = serviceDescr.getOfferedPortlets();
          if (portletDescriptions != null) {
            w.println("<tr bgcolor=\"#6495ED\" style=\"font-weight:bold;\">");
            w.println("<td>Name</td><td>Portlet handle</td><td>Group ID</td><td>Title</td><td>Short title</td><td>Markup type</td></tr>");
            for (PortletDescription portletDescription : portletDescriptions) {
              w.println("<tr>");
              w.println("<td bgcolor=\"#87CEEB\"><span style=\"font-size:12px; font-weight:bold;\" >"+ getValue(portletDescription.getDisplayName()) + "</span></td>");
              w.println("<td><span style=\"font-size:12px; \" >" + portletDescription.getPortletHandle().toString() + "</span></td>");
              w.println("<td><span style=\"font-size:12px; \" >" + portletDescription.getGroupID().toString() + "</span></td>");
              w.println("<td><span style=\"font-size:12px; \" >" + getValue(portletDescription.getTitle()) + "</span></td>");
              w.println("<td><span style=\"font-size:12px; \" >" + getValue(portletDescription.getShortTitle()) + "</span></td>");
              StringBuffer value = new StringBuffer();
//              List<LocalizedString> keywords = portletDescription.getKeywords();
//              if (keywords != null) {
//                for (LocalizedString localizedString : keywords) {
//                  value.append(getValue(localizedString)).append(" ");
//                }
//              }
//              w.println("<tr><td>" + "keywords" + "</td><td>" + value.toString() + "</td></tr>");
              List<MarkupType> types = portletDescription.getMarkupTypes();
              value.setLength(0);
              for (MarkupType markupType : types) {
                value.append(markupType.getMimeType()).append(" ");
              }
              w.println("<td><span style=\"font-size:12px; \" >" + value.toString() + "</span></td></tr>");
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
      e.printStackTrace();
      // throw new PortletContainerException("Exception in
      // WSRPAdminPortlet.render");
    }
  }

  public void processAction(ActionInput input, ActionOutput output, HttpServletRequest request) throws PortletContainerException {
    try {
      String action = request.getParameter("op");
      if (action.equals("reset")) {
        reset();
      }
      if (action.equals("deregister")) {
        String producerid = request.getParameter("producerid");

        ProducerRegistry pregistry = consumer.getProducerRegistry();
        Producer producer = pregistry.getProducer(producerid);
        RegistrationContext registrationContext = producer.getRegistrationContext();

        UserContext userContext = new UserContext();
//        userContext.getUserCategories().addAll(null);
        userContext.setProfile(null);
        userContext.setUserContextKey("");

        Deregister deregister = new Deregister();
        deregister.setRegistrationContext(registrationContext);
        deregister.setUserContext(userContext);
        if (producer.isRegistrationRequired())
          producer.deregister(deregister);

        pregistry.removeProducer(producerid);
      }
      if (action.equals("save")) {
        producerName = request.getParameter(WSRPConstants.WAP_producerName);
        producerURL = request.getParameter(WSRPConstants.WAP_producerURL);
        markupIntfEndpoint = request.getParameter(WSRPConstants.WAP_markupIntfEndpoint);
        portletManagementIntfEndpoint = request.getParameter(WSRPConstants.WAP_portletManagementIntfEndpoint);
        registrationIntfEndpoint = request.getParameter(WSRPConstants.WAP_registrationIntfEndpoint);
        serviceDescriptionIntfEndpoint = request.getParameter(WSRPConstants.WAP_serviceDescriptionIntfEndpoint);
        description = request.getParameter(WSRPConstants.WAP_description);
        version = Integer.parseInt(request.getParameter(WSRPConstants.WAP_version));
        

        String pURL = producerURL;// + "?wsdl";
        ProducerRegistry pregistry = consumer.getProducerRegistry();
        Producer producer = pregistry.createProducerInstance(pURL, version);
        producer.setName(producerName);
//        producer.setMarkupInterfaceEndpoint(pURL + markupIntfEndpoint);
//        producer.setPortletManagementInterfaceEndpoint(pURL + portletManagementIntfEndpoint);
//        producer.setRegistrationInterfaceEndpoint(pURL + registrationIntfEndpoint);
//        producer.setServiceDescriptionInterfaceEndpoint(pURL + serviceDescriptionIntfEndpoint);
        producer.setDescription(description);
        producer.getDesiredLocales().add("en");

        if (producer.isRegistrationRequired()) {
          List<String> CONSUMER_SCOPES = new ArrayList<String>();
          CONSUMER_SCOPES.add("chunk_data");
          String[] CONSUMER_CUSTOM_PROFILES = { "what_more" };
          RegistrationData registrationData = new RegistrationData();
          // required
          registrationData.setConsumerName(consumerName);
          registrationData.setConsumerAgent(consumerAgent);
          registrationData.setMethodGetSupported(false);
          // optional
          registrationData.getConsumerModes().addAll(consumerModes);
          registrationData.getConsumerWindowStates().addAll(consumerStates);
          registrationData.getConsumerUserScopes().addAll(CONSUMER_SCOPES);
//          registrationData.getExtensionDescriptions().addAll(null);
//          registrationData.getRegistrationProperties().addAll(null);
//          registrationData.getResourceList().getResources().addAll(null);
//          registrationData.getExtensions().addAll(null);

          Register register = new Register();
          register.setRegistrationData(registrationData);
//          register.setUserContext(userContext);
          
          if (request.getParameter(WSRPConstants.WAP_lifetime)!= null && !request.getParameter(WSRPConstants.WAP_lifetime).equals("") ) {
          Lifetime lifetime = new Lifetime();
          lifetime.setCurrentTime(CalendarUtils.getNow());
          Calendar cal = Calendar.getInstance();
          String aaa=request.getParameter(WSRPConstants.WAP_lifetime).trim();
          int i =Integer.parseInt(aaa.substring(0, aaa.length()-1));
          aaa =String.valueOf(aaa.charAt(aaa.length()-1));
          
          if (aaa.equalsIgnoreCase("h"))
           cal.add(Calendar.HOUR_OF_DAY  ,i);
          else if (aaa.equalsIgnoreCase("m"))
            cal.add(Calendar.MINUTE  ,i);
          else if (aaa.equalsIgnoreCase("s"))
            cal.add(Calendar.SECOND  ,i);
//          else if (aaa.equalsIgnoreCase("d"))
//            cal.add(Calendar.DAY_OF_MONTH  ,i);
          lifetime.setTerminationTime(CalendarUtils.convertCalendar(cal));
          register.setLifetime(lifetime);
          }

          RegistrationContext registrationContext = producer.register(register);
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
      e.printStackTrace();
      // throw new PortletContainerException("Exception in
      // WSRPAdminPortlet.processAction");
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
        version = Integer.valueOf(conf.getAdminPortletParams().get(WSRPConstants.WAP_version));
        //security
        userAttributes = (String) conf.getAdminPortletParams()
                                      .get(WSRPConstants.WAP_userAttributes);
        userDataConstraint = (String) conf.getAdminPortletParams()
                                          .get(WSRPConstants.WAP_userDataConstraint);
        return;
      }
    }
    portletTitle = "WSRPAdminPortlet";
    consumerName = "www.exoplatform.org";
    consumerAgent = "exoplatform.2.0";
    producerName = "exo producer";
    producerURL = "http://localhost:8080/wsrp/soap/services/WSRPService2?wsdl";//"http://localhost:8080/wsrp/services2/";
    markupIntfEndpoint = "WSRPMarkupService";
    portletManagementIntfEndpoint = "WSRPPortletManagementService";
    registrationIntfEndpoint = "WSRPRegistrationService";
    serviceDescriptionIntfEndpoint = "WSRPServiceDescriptionService";
    description = "";
    version = 2;
    //security
    userAttributes = null;
    userDataConstraint = null;
  }

  protected String getValue(LocalizedString s) {
    if (s == null)
      return "";
    else
      return s.getValue();
  }

  public void serveResource(ResourceInput input, ResourceOutput output) throws PortletContainerException {
    try {
      output.setTitle(this.portletTitle);
      CharArrayWriter charArrayWriter = new CharArrayWriter();
      PrintWriter w = new PrintWriter(charArrayWriter);
      w.println("<b>In the serveResource method of WSRPAdminPortlet</b><br>");
      // store content to output
      try {
        output.setContent((new String(charArrayWriter.toCharArray())).getBytes("utf-8"));
      } catch (java.io.UnsupportedEncodingException e) {
        output.setContent((new String(charArrayWriter.toCharArray())).getBytes());
      }
    } catch (Exception e) {
      throw new PortletContainerException("Exception in WSRPAdminPortlet.serveResource");
    }
  }

  public void processEvent(EventInput input, EventOutput output) throws PortletContainerException {
    try {
      // output.setEvent(input.getEvent().getQName(),input.getEvent().getValue());
    } catch (Exception e) {
      throw new PortletContainerException("Exception in WSRPAdminPortlet.serveResource");
    }
  }

}
