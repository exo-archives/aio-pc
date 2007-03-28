/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.portlets.wsrp;

import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
//import java.io.Writer;
import javax.portlet.*;
//import javax.portlet.PortletSession;
import org.apache.commons.logging.Log;
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.consumer.*;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
//import org.exoplatform.services.wsrp.type.CookieProtocol;
//import org.exoplatform.services.wsrp.type.LocalizedString;
//import org.exoplatform.services.wsrp.type.MarkupType;
import org.exoplatform.services.wsrp.type.PortletDescription;
import org.exoplatform.services.wsrp.type.ServiceDescription;
/*
 * @author  Tuan Nguyne
 *          tuan08@users.sourceforge.net
 * Tue, Feb 24, 2004 @ 14:35
 */
public class WSRPConfigModeHandler {
//  public static final String UI_WSRP_CONFIG = "ui-wsrp-config";
  
  private Log log_;
  private ConsumerEnvironment consumerEnvironment_;
  private PortletConfig portletConfig_;
  
  private static PortletMode wsrpMode_ = new PortletMode("wsrp");  
  
  
  public WSRPConfigModeHandler(PortletConfig config, ConsumerEnvironment env, Log log) {
    log_ = log;
    consumerEnvironment_ = env;
    portletConfig_ = config;
  }
  
  public void render(RenderRequest request, RenderResponse response) {
    response.setContentType("text/html");
    try {
      ResourceBundle res = portletConfig_.getResourceBundle(response.getLocale());
      PrintWriter w = response.getWriter();
      
      w.write("<div align='center' height='35'>");

      PortletURL actionURL = response.createActionURL();
      actionURL.setPortletMode(PortletMode.VIEW);
      
      w.println("<a href='" + actionURL.toString() + "'>");
      w.println("Back to view mode");
      w.println("</a>");
      w.println("<br>");
      
      writeResetLink(w,  response.createActionURL().toString(), res);
      w.write("</div>");

      w.println("<table>");
      ProducerRegistry pregistry = consumerEnvironment_.getProducerRegistry();
      Iterator i = pregistry.getAllProducers();
      ServiceDescription desc = null;
      while (i.hasNext()) {
        Producer producer = (Producer) i.next();
        try {
          desc = producer.getServiceDescription();
        } catch (WSRPException e) {
        }
        w.println("<tr>");
          w.println("<td colspan='2'><b>" + producer.getName() + "</b></td>");
        w.println("</tr>");
        w.println("<td>&nbsp;&nbsp;&nbsp;</td>");
        w.println("<td>");
        w.println("<table>");
          PortletDescription[] portletDescriptions = desc.getOfferedPortlets();
          if (portletDescriptions != null) {
            for (int k = 0; k < portletDescriptions.length; k++) {
              PortletDescription portletDescription = portletDescriptions[k];
              String portletName = portletDescription.getDisplayName().getValue();
              w.println("<tr><td colspan='2'><b><br>");
                writeSelectLink(w,response.createActionURL().toString(),producer.getID(),portletDescription.getPortletHandle(),portletName);
                w.println("</b></td></tr>");
              w.println("<tr><td>" + "portletHandle" + "</td><td>" + portletDescription.getPortletHandle() + "</td></tr>");
              w.println("<tr><td>" + "groupId" + "</td><td>" + portletDescription.getGroupID() + "</td></tr>");
              w.println("<tr><td>" + "title" + "</td><td>" + portletDescription.getTitle().getValue() + "</td></tr>");
              w.println("<tr><td>" + "shortTitle" + "</td><td>" + portletDescription.getShortTitle().getValue() + "</td></tr>");
              w.println("<tr><td>" + "displayName" + "</td><td>" + portletDescription.getDisplayName().getValue() + "</td></tr>");
            }
          }          
        w.println("</table>");
        w.println("</td>");
        w.println("</tr>");
      }
      w.println("</table>");      
    } catch (Exception ex) {
      log_.error("Error: ", ex);
    }
  }

  public void processAction(ActionRequest request, ActionResponse response) throws IOException {
    try {
      String action = request.getParameter("action");
      if ("reset".equals(action)) {
        PortletPreferences prefs = request.getPreferences();
        prefs.reset(WSRPConstants.WSRP_PRODUCER_ID);
        prefs.reset(WSRPConstants.WSRP_PORTLET_HANDLE);
        prefs.reset(WSRPConstants.WSRP_PARENT_HANDLE);
        prefs.store();
      }
      if ("select".equals(action)) {
        String producerId = request.getParameter("producerId");
        String portletHandle = request.getParameter("portletHandle");
        PortletPreferences prefs = request.getPreferences();
        prefs.setValue(WSRPConstants.WSRP_PRODUCER_ID , producerId);
        prefs.setValue(WSRPConstants.WSRP_PORTLET_HANDLE , portletHandle);
        prefs.setValue(WSRPConstants.WSRP_PARENT_HANDLE , portletHandle);
        prefs.store();
      }         
    } catch (Exception ex) {
    }
  }
  
  private void writeResetLink(PrintWriter w,  String baseURL, ResourceBundle res) 
    throws IOException {
    w.println("<a href='" + baseURL + "&portal:portletMode=wsrp" + "&action=reset'>");
    w.println("<b>Reset</b>");
    w.println("</a>");
  }
  
  private void writeSelectLink(Writer w, String baseURL, String producerID, String portletHandle, String portletName) 
    throws IOException {
    w.write("<a href='" + baseURL + "&portal:portletMode=wsrp" + "&producerId=" + producerID +
      "&portletHandle=" + portletHandle + "&action=select'>");
    w.write(portletName);
    w.write("</a>");
  }
  
}