/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.test.filters;

import static org.exoplatform.frameworks.portletcontainer.portalframework.PFConstants.LIST_COLLAPSED;
import static org.exoplatform.frameworks.portletcontainer.portalframework.PFConstants.PORTLETS_2_RENDER;
import static org.exoplatform.frameworks.portletcontainer.portalframework.PFConstants.PORTLET_INFOS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.exoplatform.frameworks.portletcontainer.portalframework.PortalFramework;
import org.exoplatform.services.portletcontainer.PCConstants;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: PortletFilter.java 8554 2006-09-04 15:28:35Z sunman $
 */

/**
 * PortletFilter class does portal's work using portal-framework it processes
 * user http requests and invokes portlets.
 */
public class PortletFilter implements Filter {

  /**
   * Some ASs commit ServletResponse's that they get with include() method so we
   * have to construct dummy responses to be committed :).
   * 
   * @param original original http servlet response
   * @return dummy response if it's need, otherwise original one
   */
  private final HttpServletResponse createDummyResponse(final HttpServletResponse original) {
    if (original.getClass().getName().equals("weblogic.servlet.internal.ServletResponseImpl")
        || original.getClass().getName().equals("com.ibm.ws.webcontainer.srt.SRTServletResponse")
//        || original.getClass().getName().equals("com.evermind.server.http.EvermindHttpServletResponse")
    ) {
      return new DummyResponse(original);
    } else
      return original;
  }

  /**
   * Does nothing.
   * 
   * @param filterConfig filter config
   */
  public void init(final FilterConfig filterConfig) {
  }

  /**
   * Actual request processing.
   * 
   * @param servletRequest servlet request
   * @param servletResponse servlet respnse
   * @param filterChain filter chain
   * @throws IOException something may go wrong
   * @throws ServletException something may go wrong
   */
  public synchronized void doFilter(ServletRequest servletRequest,
                                    ServletResponse servletResponse,
                                    FilterChain filterChain) throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
    HttpSession httpSession = httpRequest.getSession();
    ServletContext ctx = httpSession.getServletContext();
    String markupType = "text/html";

    try {
      PortalFramework framework = PortalFramework.getInstance();

      framework.createOrUpdatePortletWindows();

      // --- this dummy http response is intended to avoid premature response commit on some AS-es
      HttpServletResponse dummyHttpResponse = createDummyResponse(httpResponse);

      // call PortalFramework to process current request to portlet container
      framework.preRenderRequest(ctx, httpRequest, dummyHttpResponse, markupType);

      if (framework.getRedirect() != null) {
        httpResponse.sendRedirect(framework.getRedirect());
        filterChain.doFilter(servletRequest, servletResponse);
        return;
      }

      if (framework.getAction() == PCConstants.RESOURCE_INT) {
        httpSession.setAttribute("resourceType", framework.getResourceContentType());
        httpSession.setAttribute("resource", framework.getResourceContent());
        httpSession.setAttribute("resourceHeaders", framework.getResourceHeaders());
        httpSession.setAttribute("resourceStatus", framework.getResourceStatus());
      } else {
        framework.createOrUpdatePortletWindows();

        List<String> portlets2render = getPortlets2Render(servletRequest.getParameterMap(),
                                                          httpSession,
                                                          framework.getPagePortlets());

        // call PortalFramework to process rendering all portlets
        framework.processRender(portlets2render);

        httpSession.removeAttribute(PORTLET_INFOS);
        httpSession.setAttribute(PORTLET_INFOS, framework.getPortletInfos());
      }

    } catch (Exception e) {
      e.printStackTrace();
      httpSession.setAttribute(PORTLET_INFOS, null);
      return;
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }

  /**
   * Does nothing.
   */
  public void destroy() {
  }

  private List<String> getPortlets2Render(Map<String, String[]> servletParams,
                                          HttpSession httpSession,
                                          List<String> pagePortlets) {
    List<String> portlets2render = null;

    // collecting portlets to render
    if (servletParams.containsKey("fis")) {
      Iterator<String> plts = pagePortlets.iterator();
      portlets2render = new ArrayList<String>();

      int count = 0;
      while (plts.hasNext()) {
        count++;
        String portlet = plts.next();
        if ((servletParams.containsKey("fis") && (servletParams.containsKey("n" + count + "n") && servletParams.get("n"
            + count + "n")[0].equals("on")))) {
          portlets2render.add(portlet);
        }
      }
      Boolean listCollapsedAttr = new Boolean(servletParams.containsKey(LIST_COLLAPSED)
          && servletParams.get(LIST_COLLAPSED)[0].equals("true"));
      httpSession.setAttribute(LIST_COLLAPSED, listCollapsedAttr);
    }

    if (portlets2render == null) {
      portlets2render = (ArrayList<String>) httpSession.getAttribute(PORTLETS_2_RENDER);
    } else {
      httpSession.setAttribute(PORTLETS_2_RENDER, portlets2render);
    }

    return portlets2render;
  }

}
