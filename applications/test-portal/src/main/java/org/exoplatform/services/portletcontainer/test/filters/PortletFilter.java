/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import org.apache.commons.lang.StringUtils;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.frameworks.portletcontainer.portalframework.PortalFramework;
import org.exoplatform.frameworks.portletcontainer.portalframework.PortletInfo;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;
import org.exoplatform.services.portletcontainer.plugins.pc.PCConstants;

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
public class PortletFilter implements Filter {

  /**
   * Frameworks. One per http session.
   */
  public static final HashMap<String, PortalFramework> frameworks = new HashMap<String, PortalFramework>();

  /**
   * Current framework.
   */
  private PortalFramework framework = null;

  /**
   * Session replication info.
   */
  private HashMap<String, Serializable> sessionInfo = new HashMap<String, Serializable>();

  /**
   * Session identifier.
   */
  public static final String SESSION_IDENTIFIER = "SID";

  /**
   * Portal identifier.
   */
  public static final String PORTAL_IDENTIFIER = "PID";

  /**
   * Portal container name.
   */
  private String portalContainerName = "";

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

  /**
   * Actual request processing.
   *
   * @param servletRequest servlet request
   * @param servletResponse servlet respnse
   * @param filterChain filter chain
   * @throws IOException something may go wrong
   * @throws ServletException something may go wrong
   */
  public void doFilter(ServletRequest servletRequest,
      ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
    HttpSession httpSession = httpRequest.getSession();
    ServletContext ctx = httpRequest.getSession().getServletContext();

    try {
      StandaloneContainer portalContainer = StandaloneContainer.getInstance();
      // create WindowInfosContainer instance if there's no one
      WindowInfosContainer.createInstance(portalContainer, httpSession.getId(), httpRequest.getRemoteUser());

      // create/get PortalFramework instance
      framework = frameworks.get(httpSession.getId());
      if (framework == null) {
        framework = new PortalFramework(portalContainer);
        frameworks.put(httpSession.getId(), framework);
      }
      framework.init(httpSession);

      portalContainerName = framework.getPortalName();

      ArrayList<String> portlets2render = null;

      // collecting portlets to render -- especially for TCK tests
      String[] ps = servletRequest.getParameterValues("portletName");
      if (ps != null) {
        portlets2render = new ArrayList<String>();
        for (String s : ps)
          portlets2render.add(s);
        httpSession.setAttribute("portletName", portlets2render);
      } else
        portlets2render = (ArrayList<String>) httpSession.getAttribute("portletName");

      // collecting portlets to render
      if (portlets2render == null) {
        Map<String, String[]> servletParams = servletRequest.getParameterMap();

        if (servletParams.containsKey("fis")) {
          Iterator<String> plts = framework.getPortletNames().iterator();
          portlets2render = new ArrayList<String>();

          int count = 0;
          while (plts.hasNext()) {
            count++;
            String portlet = plts.next();
            if ((servletParams.containsKey("fis") &&
                (servletParams.containsKey("n" + count + "n") &&
                    servletParams.get("n" + count + "n")[0].equals("on"))))
              portlets2render.add(portlet);
          }
          httpSession.setAttribute("listCollapsed", new Boolean(servletParams.containsKey("listCollapsed") &&
              servletParams.get("listCollapsed")[0].equals("true")));
        }
      }

      if (portlets2render == null)
        portlets2render = (ArrayList<String>) httpSession.getAttribute("portlets2render");
      else
        httpSession.setAttribute("portlets2render", portlets2render);

      // --- this dummy http response is intended to avoid premature response commit on some AS-es
      HttpServletResponse dummyHttpResponse = createDummyResponse(httpResponse);

      // call PortalFramework to process current request to portlet container
      ArrayList<PortletInfo> portletInfos = framework.processRequest(ctx, httpRequest, dummyHttpResponse, "text/html",
          portlets2render);

      // Session Replication
      String count2 = (String) httpSession.getAttribute("count");
      count2 += "+";
      //session_info.clear();

      if (framework.getRedirect() != null) {
        httpResponse.sendRedirect(framework.getRedirect());
        filterChain.doFilter(servletRequest, servletResponse);
        return;
      }

      if (framework.getAction() == PCConstants.resourceInt) {
        httpSession.setAttribute("resourceType", framework.getResourceContentType());
        httpSession.setAttribute("resource", framework.getResourceContent());
        httpSession.setAttribute("resourceHeaders", framework.getResourceHeaders());
      } else {
        Iterator<PortletInfo> plts = portletInfos.iterator();
        if (httpSession.getAttribute("portletName") == null) {
          while (plts.hasNext()) {
            PortletInfo portletinfo = plts.next();
            portletinfo.setTitle(portletinfo.getTitle() + " {mode: " + portletinfo.getMode() + "; state: " +
                portletinfo.getState() + "}");
            portletinfo.setToRender(portlets2render != null && portlets2render.contains(portletinfo.getPortlet()));
            //Collecting session info
            HashMap<String, Object> hm = portletinfo.getSessionMap();
            sessionInfo.put(StringUtils.split(portletinfo.getPortlet(), "/")[0], hm);
          }
        } else {
          ArrayList<PortletInfo> newPortletInfos = new ArrayList<PortletInfo>();
          while (plts.hasNext()) {
            PortletInfo portletinfo = plts.next();
            if (portletinfo.getOut() != null)
              newPortletInfos.add(portletinfo);
          }
          portletInfos = newPortletInfos;
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

    // Session Replication
    sessionInfo.put(SESSION_IDENTIFIER, httpSession.getId());
    sessionInfo.put(PORTAL_IDENTIFIER, portalContainerName);
    try {
//      SessionReplicator sr = new SessionReplicator();
//      sr.send(session_info);
    } catch (Exception e){
    }
  }

  /**
   * Does nothing.
   */
  public void destroy() {
  }

}
