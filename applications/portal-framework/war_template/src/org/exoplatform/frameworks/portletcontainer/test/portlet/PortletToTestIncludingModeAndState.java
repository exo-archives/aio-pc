package org.exoplatform.frameworks.portletcontainer.test.portlet;

import javax.portlet.*;

import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.BaseURLImp;

import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by The eXo Platform SAS  2007 .
 *
 * @author Max Shaposhnik
 * @version $Id$
 */


public class PortletToTestIncludingModeAndState implements Portlet {
  
  public void init(PortletConfig portletConfig) throws PortletException {
    //To change body of implemented methods use Options | File Templates.
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
    
    renderResponse.setContentType("text/html");
    PrintWriter w = renderResponse.getWriter();
    try {
      w.print(renderRequest.getPortletMode().toString());
      w.print(renderRequest.getWindowState().toString());
//      System.out.println("123" + renderRequest.getPortletMode().toString() + renderRequest.getWindowState().toString());
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  public void destroy() {
    //To change body of implemented methods use Options | File Templates.
  }

}
