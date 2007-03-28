package org.exoplatform.portlets.wsrp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
//import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
//import org.jmock.core.constraint.IsNull;
//import org.apache.commons.logging.Log;
//import org.exoplatform.container.PortalContainer;
//import org.exoplatform.container.ExoContainerContext;
//import org.exoplatform.container.StandaloneContainer;
import org.apache.commons.lang.UnhandledException;
//import org.exoplatform.portlets.wsrp.component.model.ProducerData;
//import org.exoplatform.portlets.wsrp.component.UIOfferedPortlet.PortletItemValue;
import org.exoplatform.services.wsrp.consumer.ConsumerEnvironment;
import org.exoplatform.services.wsrp.consumer.Producer;
import org.exoplatform.services.wsrp.consumer.ProducerRegistry;
//import org.exoplatform.services.log.LogService;
//import org.exoplatform.services.wsrp.*;
import org.exoplatform.services.wsrp.type.CookieProtocol;
import org.exoplatform.services.wsrp.type.LocalizedString;
import org.exoplatform.services.wsrp.type.MarkupType;
import org.exoplatform.services.wsrp.type.PortletDescription;
import org.exoplatform.services.wsrp.type.RegistrationData;
import org.exoplatform.services.wsrp.type.ServiceDescription;
import org.exoplatform.services.portletcontainer.*;
//import org.exoplatform.services.wsrp.consumer.*;
import org.exoplatform.services.wsrp.exceptions.WSRPException;

//import org.exoplatform.services.portletcontainer.test.filters;

import org.exoplatform.container.*; 

import org.exoplatform.services.log.LogService;
import org.apache.commons.logging.Log;

public class WSRPAdminPortlet extends GenericPortlet {

  private String producerName_;
  private String producerURL_;
  private String markupIntfEndPoint_;
  private String pmIntfEndPoint_;
  private String registrationIntfEndPoint_;
  private String serviceDescIntfEndPoint_;
  private String description_;

  private String[] consumerModes_; 
  private String[] consumerStates_; 
  private String   consumerName_; 
  private String   consumerAgent_; 

  private ConsumerEnvironment consumerEnvironment_;   
  protected PortletContext context;
  //private Log log;
  
  public void init(PortletConfig config) throws PortletException, UnhandledException {
    super.init(config);

    try {
      context = config.getPortletContext();
      
      ExoContainer container = null;
      if ( ExoContainerContext.getTopContainer() instanceof RootContainer) {
        //PORTALCONTAINER
        container = PortalContainer.getInstance();
      } else {
        // STANDALONE
        container = ExoContainerContext.getTopContainer();
      }
      
      //this.log = ((LogService)PortalContainer.getInstance().getComponentInstanceOfType(LogService.class)).getLog(getClass());
      this.consumerEnvironment_ = (ConsumerEnvironment) container.getComponentInstanceOfType(ConsumerEnvironment.class);
      PortletContainerService portletContainer = (PortletContainerService) container.getComponentInstanceOfType(PortletContainerService.class);

      Collection modes  = portletContainer.getSupportedPortletModes();
      Collection states = portletContainer.getSupportedWindowStates();
        
      consumerModes_ = new String[modes.size()];
      Iterator iterator= modes.iterator();
      int j = 0;
      while (iterator.hasNext()) {
        PortletMode mode = (PortletMode) iterator.next();
        consumerModes_[j] = "wsrp:" + mode.toString();
        j++;
      }
        
      consumerStates_ = new String[states.size()];
      iterator= states.iterator();
      j = 0;
      while (iterator.hasNext()) {
        WindowState state = (WindowState) iterator.next();
        consumerStates_[j] = "wsrp:" + state.toString();
        j++;
      }
        
      consumerName_ = config.getInitParameter("consumer-name");
      consumerAgent_ = config.getInitParameter("consumer-agent");

      reset();

      
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  
  
  }
  
  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse) 
    throws PortletException, IOException {
    try {
      renderResponse.setContentType("text/html");
      renderResponse.setTitle("WSRPAdminPortlet");
      PrintWriter w = renderResponse.getWriter();      
      // ActionURL
      PortletURL actionURL = renderResponse.createActionURL();
      actionURL.setSecure(true);
      actionURL.setWindowState(WindowState.NORMAL);
      actionURL.setPortletMode(PortletMode.VIEW);
      
      w.println("<b>Create producer:</b><br>");
        w.println("<form name=\"producerForm\" method=\"post\" action=\"" + actionURL.toString() + "\">"); 
        w.println("  <input type=\"hidden\" name=\"op\" value=\"\"/>");
        w.println("    <table>");
        w.println("    <tr><td><label>Producer Name</label></td>");
        w.println("      <td><input name=\"producerName\" value=\"" +  producerName_ + "\" type=\"text\" size=\"45\"></td></tr>");
        w.println("    <tr><td><label>Producer URL</label></td>");
        w.println("      <td><input name=\"producerURL\" value=\"" + producerURL_ + "\" type=\"text\" size=\"45\"></td></tr>");
        w.println("    <tr><td><label>Markup Interface End Point</label></td>");
        w.println("      <td><input name=\"markupIntfEndPoint\" value=\"" + markupIntfEndPoint_ + "\" type=\"text\" size=\"45\"></td></tr>");
        w.println("    <tr><td><label>Portlet Management Interface End Point</label></td>");
        w.println("      <td><input name=\"pmIntfEndPoint\" value=\"" + pmIntfEndPoint_ + "\" type=\"text\" size=\"45\"></td></tr>");
        w.println("    <tr><td><label>Registration Interface End Point</label></td>");
        w.println("      <td><input name=\"registrationIntfEndPoint\" value=\"" + registrationIntfEndPoint_ + "\" type=\"text\" size=\"45\"></td></tr>");
        w.println("    <tr><td><label>Service Description Interface End Point</label></td>");
        w.println("      <td><input name=\"serviceDescIntfEndPoint\" value=\"" + serviceDescIntfEndPoint_ + "\" type=\"text\" size=\"45\"></td></tr>");
        w.println("    <tr><td><label>Description</label></td>");
        w.println("      <td><textarea id=\"description\" name=\"description\" cols=\"35\" rows=\"5\">" + description_ + "</textarea></td></tr>");
        w.println("    <tr><td colspan='2' align='center'>");
        w.println("      <a href=\"javascript:submit_producerForm('save');\">Save</a>");
        w.println("      <a href=\"javascript:submit_producerForm('reset');\">Reset</a>");
        w.println("    </td></tr>");
        w.println("  </table>");
        w.println("<script type=\"text/javascript\">");
        w.println("  function submit_producerForm(action) {");
        w.println("    document.producerForm.elements['op'].value = action;");
        w.println("    document.producerForm.submit();");
        w.println("  }");
        w.println("</script>");
        w.println("</form>");      
        
        w.println("<hr>");

        w.println("<table>");
        ProducerRegistry pregistry = consumerEnvironment_.getProducerRegistry();
        Iterator i = pregistry.getAllProducers();
        ServiceDescription desc = null;
        while (i.hasNext()) {
          Producer producer = (Producer) i.next();
          try {
            desc = producer.getServiceDescription();
          } catch (WSRPException e) {
            e.printStackTrace();
          }
          w.println("<tr>");
            w.println("<td colspan='2'><b>" + producer.getName() + "</b></td>");
          w.println("</tr>");
          w.println("<td>&nbsp;&nbsp;&nbsp;</td>");
          w.println("<td>");
          w.println("<table>");
          w.println("<tr>");
            w.println("<td>Requires Registration </td>");
            String answer = "N/A";
            if(desc != null)
              answer = Boolean.toString(desc.isRequiresRegistration());
            w.println("<td>" + answer + "</td>");
          w.println("</tr>");
          w.println("<tr>");
            w.println("<td>Requires Init Cookie </td>");
            answer = "none";
            if(desc != null) {
              CookieProtocol cookie = desc.getRequiresInitCookie();
              if(cookie != null) answer = cookie.getValue();
            }
            w.println("<td>" + answer + "</td>");
          w.println("</tr>");
            PortletDescription[] portletDescriptions = desc.getOfferedPortlets();
            if (portletDescriptions != null) {
              for (int k = 0; k < portletDescriptions.length; k++) {
                PortletDescription portletDescription = portletDescriptions[k];
                String portletName = portletDescription.getDisplayName().getValue();
                w.println("<tr><td colspan='2'><b><br>" + portletName + "</b></td></tr>");
                StringBuffer value = new StringBuffer();
                w.println("<tr><td>" + "portletHandle" + "</td><td>" + portletDescription.getPortletHandle().toString() + "</td></tr>");
                w.println("<tr><td>" + "groupId" + "</td><td>" + portletDescription.getGroupID().toString() + "</td></tr>");
                w.println("<tr><td>" + "title" + "</td><td>" + portletDescription.getTitle().getValue() + "</td></tr>");
                w.println("<tr><td>" + "shortTitle" + "</td><td>" + portletDescription.getShortTitle().getValue() + "</td></tr>");
                w.println("<tr><td>" + "displayName" + "</td><td>" + portletDescription.getDisplayName().getValue() + "</td></tr>");
                LocalizedString[] keywords = portletDescription.getKeywords();
                if(keywords != null){
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
                String[] userCategories = portletDescription.getUserCategories();
                String valueCategories  = "";
                if( userCategories != null) {
                  value.setLength(0);
                  for (int j = 0; j < userCategories.length; j++) {
                    value.append(userCategories[j]).append(" ");
                  }
                  valueCategories = value.toString();      
                }
                w.println("<tr><td>" + "userCategory" + "</td><td>" + valueCategories + "</td></tr>");
                String[] userProfileItems = portletDescription.getUserProfileItems();
                String valueProfileItem = "";
                if( userProfileItems != null) {
                  value.setLength(0);
                  for (int j = 0; j < userProfileItems.length; j++) {
                    value.append(userProfileItems[j]).append(" ");
                  }
                  valueProfileItem = value.toString();
                } 
                w.println("<tr><td>" + "userProfileItem" + "</td><td>" + valueProfileItem + "</td></tr>");
                  w.println("<tr><td>" + "usesMethodGet" + "</td><td>" + portletDescription.getUsesMethodGet().toString() + "</td></tr>");
                if(portletDescription.getDefaultMarkupSecure() != null)
                  w.println("<tr><td>" + "defaultMarkupSecure" + "</td><td>" + portletDescription.getDefaultMarkupSecure().toString() + "</td></tr>");      
                if(portletDescription.getOnlySecure() != null)
                  w.println("<tr><td>" + "onlySecure" + "</td><td>" + portletDescription.getOnlySecure().toString() + "</td></tr>");            
                if(portletDescription.getUserContextStoredInSession() != null)
                  w.println("<tr><td>" + "userContextStoredInSession" + "</td><td>" + portletDescription.getUserContextStoredInSession().toString() + "</td></tr>");      
                if(portletDescription.getTemplatesStoredInSession() != null)
                  w.println("<tr><td>" + "templatesStoredInSession" + "</td><td>" + portletDescription.getTemplatesStoredInSession().toString() + "</td></tr>");    
                if(portletDescription.getHasUserSpecificState() != null)
                  w.println("<tr><td>" + "hasUserSpecificState" + "</td><td>" + portletDescription.getHasUserSpecificState().toString() + "</td></tr>");      
                if(portletDescription.getDoesUrlTemplateProcessing() != null)
                  w.println("<tr><td>" + "doesUrlTemplateProcessing" + "</td><td>" + portletDescription.getDoesUrlTemplateProcessing().toString() + "</td></tr>");
                  w.println("<tr><td>" + "extensions" + "</td><td>" + "N/A" + "</td></tr>");          
              }
            }          
          w.println("</table>");
          w.println("</td>");
          w.println("</tr>");
        }
        w.println("</table>");
        
    } catch (Exception e) {
      throw new PortletException("Exception in WSRPAdminPortlet");
    }
    
  }
  
  protected void doEdit(RenderRequest renderRequest, RenderResponse renderResponse)
    throws PortletException, IOException {
    doView(renderRequest,renderResponse);
  }
  
  protected void doHelp(RenderRequest renderRequest, RenderResponse renderResponse)
    throws PortletException, IOException {
    doView(renderRequest,renderResponse);
  }
  
  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
    throws PortletException, IOException {
    try {
      String action = actionRequest.getParameter("op");
      if (action.equals("reset")) {
        reset();
      }
      if (action.equals("save")) {
        producerName_ = actionRequest.getParameter("producerName");
        producerURL_ = actionRequest.getParameter("producerURL");
        markupIntfEndPoint_ = actionRequest.getParameter("markupIntfEndPoint");
        pmIntfEndPoint_ = actionRequest.getParameter("pmIntfEndPoint");
        registrationIntfEndPoint_ = actionRequest.getParameter("registrationIntfEndPoint");
        serviceDescIntfEndPoint_ = actionRequest.getParameter("serviceDescIntfEndPoint");
        description_ = actionRequest.getParameter("description");

        String pURL = producerURL_;
        String producerId = "producer" + Integer.toString(pURL.hashCode());
        ProducerRegistry pregistry = consumerEnvironment_.getProducerRegistry();
        Producer producer = pregistry.createProducerInstance();
        producer.setID(producerId);
        producer.setName(producerName_);
        producer.setMarkupInterfaceEndpoint(pURL +markupIntfEndPoint_);
        producer.setPortletManagementInterfaceEndpoint(pURL + pmIntfEndPoint_);
        producer.setRegistrationInterfaceEndpoint(pURL + registrationIntfEndPoint_);
        producer.setServiceDescriptionInterfaceEndpoint(pURL + serviceDescIntfEndPoint_);
        producer.setDescription(description_);
        producer.setDesiredLocales(new String[] {"en"});
        
        if (producer.isRegistrationRequired()) {
          String[] CONSUMER_SCOPES = {"chunk_data"};
          String[] CONSUMER_CUSTOM_PROFILES = {"what_more"};
          RegistrationData registrationData =  new RegistrationData();
          registrationData.setConsumerName(consumerName_);
          registrationData.setConsumerAgent(consumerAgent_);
          registrationData.setMethodGetSupported(false);
          registrationData.setConsumerModes(consumerModes_);
          registrationData.setConsumerWindowStates(consumerStates_);
          registrationData.setConsumerUserScopes(CONSUMER_SCOPES);
          registrationData.setCustomUserProfileData(CONSUMER_CUSTOM_PROFILES);
          registrationData.setRegistrationProperties(null);
          registrationData.setExtensions(null);
          producer.register(registrationData);
        }
        pregistry.addProducer(producer);     
      }
    } catch (Exception e) {
    }
  }

  public void reset()  {
    producerName_ = "exo producer";
    producerURL_ = "http://localhost:8080/wsrp/services/";
    markupIntfEndPoint_ = "WSRPBaseService";
    pmIntfEndPoint_ = "WSRPPortletManagementService";
    registrationIntfEndPoint_ = "WSRPRegistrationService";
    serviceDescIntfEndPoint_ = "WSRPServiceDescriptionService";
    description_ = "";
  }

  private String getValue(LocalizedString s) {
    if (s == null) return "";
    else return s.getValue();
  }

}