/***************************************************************************
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.test.portlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.*;

/**
 * Created by The eXo Platform SARL
 * Author : Alexey V. Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 * 11.10.2006  
 */

public class TestResources extends GenericPortlet {
  
  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
  throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");    
    renderResponse.setTitle("TestResources");
    PrintWriter w = renderResponse.getWriter();
    w.println("<h2 align=\"center\">Test</h2>");
    PortletConfig portletConfig = getPortletConfig();
    w.println("<br>Locale.getDefault = " + Locale.getDefault());
    System.out.println(">>> EXOMAN TestResources.doView Locale.getDefault() = "
        + Locale.getDefault());
    Locale locales[] = new Locale[] {
                        new Locale("en"),
                        new Locale("ru")
                        };
    if(portletConfig != null) {
      for(int i = 0; i < locales.length; i++) {
        w.println("<br>" + locales[i]);
        System.out.println(">>> EXOMAN TestResources.doView locales[i] = " + locales[i]);
        ResourceBundle resourceBundle = 
          portletConfig.getResourceBundle(locales[i]);
        if (resourceBundle != null) { 
          String resourceTitle = 
            resourceBundle.getString("javax.portlet.title");
          w.println(" = " + resourceTitle);
          System.out.println(">>> EXOMAN TestResources.doView resourceTitle = " + resourceTitle);
        } else {
          System.out.println(">>> EXOMAN TestResources.doView resourceBundle == null = "
              + resourceBundle == null);
        }
        w.println("--------------------");
      }
    } else {
      System.out.println(">>> EXOMAN TestResources.doView portletConfig == null = "
          + portletConfig == null);
    }
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
