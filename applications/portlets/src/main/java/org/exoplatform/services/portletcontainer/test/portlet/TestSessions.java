package org.exoplatform.services.portletcontainer.test.portlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

//import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletSessionImp;


public class TestSessions  extends GenericPortlet {
  
  protected void doView(RenderRequest renderRequest,
      RenderResponse renderResponse) throws PortletException, IOException {
    
    
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    
    String a  = (String)renderRequest.getPortletSession().getAttribute("a");
    w.println(a); 

    a +="++";
    renderRequest.getPortletSession().setAttribute("a", a);

    
    
    String c  = (String)renderRequest.getPortletSession().getAttribute("c");
    w.println(c); 
    
    c +="||";
    renderRequest.getPortletSession().setAttribute("c", c);
    
    
   }
  
  public void processAction(ActionRequest actionRequest,
      ActionResponse actionResponse) throws PortletException, IOException {

  }

}
