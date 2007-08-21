/*20.08.2007-16:46:50 Volodymyr*/

package org.exoplatform.services.portletcontainer.test.portlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.*;

public class MvnAdminPortlet extends GenericPortlet {
  
  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
  throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");    
    renderResponse.setTitle("Maven Artifact Managment Tool");
    PortletConfig portletConfig = getPortletConfig();
    
    String templatePath="/template/jsp/importArtifact.jsp";
    
    PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(templatePath);
    dispatcher.include(renderRequest, renderResponse);

  }
  
protected void doEdit(RenderRequest renderRequest, RenderResponse renderResponse)
  throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");    
    PrintWriter w = renderResponse.getWriter();
    w.println("<p>test");
  }

public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
  throws PortletException, IOException {
  
  }  
  
  
}
