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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.exoplatform.frameworks.portletcontainer.portalframework.PortletInfo;
import org.exoplatform.frameworks.portletcontainer.portalframework.WindowID2;
import org.exoplatform.services.portletcontainer.PCConstants;

/**
 * Created by The eXo Platform SAS  .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: PortletFilter.java 8554 2006-09-04 15:28:35Z sunman $
 */

/**
 * PortletFilter class does portal's work using portal-framework it processes user http requests
 * and invokes portlets.
 */
public class TckFilter implements Filter {

  /**
   * Portlet map for TCK tests.
   */
  private Map<String, String> tckPltMap = new HashMap<String, String>();

  /**
   * Some ASs commit ServletResponse's that they get with include() method so we have to
   * construct dummy responses to be committed :).
   *
   * @param original original http servlet response
   * @return dummy response if it's need, otherwise original one
   */
  public final HttpServletResponse createDummyResponse(final HttpServletResponse original) {
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

  private boolean foundInPortlets(PortalFramework framework, String app, String plt) {
    for (Iterator<String> i = framework.getPagePortlets().iterator(); i.hasNext(); ) {
      WindowID2 win = framework.getPortletWindowById(i.next());
      if (win.getPortletApplicationName().equals(app) && win.getPortletName().equals(plt))
        return true;
    }
    return false;
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
      ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
    HttpSession httpSession = httpRequest.getSession();
    ServletContext ctx = httpRequest.getSession().getServletContext();

    try {
      PortalFramework framework = PortalFramework.getInstance();

      List<String> portlets2render = null;

      // collecting portlets to render -- especially for TCK tests
      String[] ps = servletRequest.getParameterValues("portletName");
      if (ps != null) {
        portlets2render = new ArrayList<String>();
        for (String s : ps) {
          String nn = tckPltMap.get(s);
          if (nn == null) {
            String[] ss = s.split("/");
            nn = framework.addPortlet(ss[0], ss[1]);
            tckPltMap.put(s, nn);
          }
          portlets2render.add(nn);
        }
        httpSession.setAttribute("portletName", portlets2render);
      } else
        portlets2render = (ArrayList<String>) httpSession.getAttribute("portletName");

      // --- this dummy http response is intended to avoid premature response commit on some AS-es
      HttpServletResponse dummyHttpResponse = createDummyResponse(httpResponse);

      // call PortalFramework to process current request to portlet container
      framework.preRenderRequest(ctx, httpRequest, dummyHttpResponse, "text/html");

      if (framework.getRedirect() != null) {
        httpResponse.sendRedirect(framework.getRedirect());
        filterChain.doFilter(servletRequest, servletResponse);
        return;
      }

      if (framework.getAction() == PCConstants.RESOURCE_INT) {
        httpSession.setAttribute("resourceType", framework.getResourceContentType());
        httpSession.setAttribute("resource", framework.getResourceContent());
        httpSession.setAttribute("resourceHeaders", framework.getResourceHeaders());
        httpSession.setAttribute("resourceStatus", new Integer(framework.getResourceStatus()));
      } else {
        ArrayList<PortletInfo> portletInfos = new ArrayList<PortletInfo>();
        for (Iterator<String> i = portlets2render.iterator(); i.hasNext(); ) {
          String plt = i.next();
          portletInfos.add(framework.renderPortlet(plt));
        }
        httpSession.removeAttribute("portletinfos");
        httpSession.setAttribute("portletinfos", portletInfos);
      }

    } catch (Exception e) {
      e.printStackTrace();
      httpSession.setAttribute("portletinfos", null);
      return;
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }


  /**
   * Does nothing.
   */
  public void destroy() {
  }

}
