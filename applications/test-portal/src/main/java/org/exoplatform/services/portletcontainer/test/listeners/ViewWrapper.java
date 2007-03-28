/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.portletcontainer.test.listeners;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.HashMap;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.impl.PortletApplicationHandler;
import org.exoplatform.services.portletcontainer.impl.PortletContainerDispatcher;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.Output;
import org.exoplatform.services.portletcontainer.pci.WindowID;

/**
 * Created by The eXo Platform SARL .
 * 
 * @author <a href="mailto:lautarul@gmail.com">Roman Pedchenko</a>
 * @version $Id$
 */

public class ViewWrapper extends HttpServlet {

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
  }

  protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
                  throws ServletException, java.io.IOException {
    ServletContext ctx = getServletContext();
    PortalContainer manager = PortalContainer.getInstance();
    
    PortletWindowInternal windowInfo = (PortletWindowInternal) ctx.getAttribute("WINDOW_INFO");
    Input input = (Input) ctx.getAttribute("INPUT");
    Output output = (Output) ctx.getAttribute("OUTPUT");

//    windowID.setPortletName(portletName);

    PortletApplicationHandler handler = (PortletApplicationHandler) manager.getComponentInstanceOfType(PortletApplicationHandler.class);

		try {			      
			handler.process(ctx, servletRequest,
							        servletResponse, input, output, windowInfo, false);
		} catch (PortletContainerException e) {
			throw new ServletException("An error occured while processing the portlet request", e);
		}
	}
}
