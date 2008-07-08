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
package org.exoplatform.services.portletcontainer.test.servlets;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.frameworks.portletcontainer.portalframework.PortalFramework;
import org.exoplatform.frameworks.portletcontainer.portalframework.PortletInfo;
import org.exoplatform.frameworks.portletcontainer.portalframework.Template;
import org.exoplatform.frameworks.portletcontainer.portalframework.filters.DummyResponse;
import org.exoplatform.frameworks.portletcontainer.portalframework.filters.SessionReplicator;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutColumn;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutHtmlTag;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutHtmlText;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutItem;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutNode;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutPlt;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutZone;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;

//http://extjs.com/deploy/dev/examples/window/hello.html
//http://extjs.com/deploy/dev/examples/portal/portal.html

/**
 * Created by The eXo Platform SAS  .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */
public class PortalServlet extends HttpServlet {

  /**
   * Log.
   */
  private Log log;

  /**
   * Frameworks. One per http session.
   */
  public static final HashMap<String, PortalFramework> FRAMEWORKS = new HashMap<String, PortalFramework>();

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

  public static final String REPLICATOR_IDENTIFIER = "RID";

  private SessionReplicator sr;

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
    }
    return original;
  }

  /**
   * Portlet infos.
   */
  private List<PortletInfo> portletInfos;

  /**
   * Serves http request. Renders portal page.
   *
   * @param request http request
   * @param response http response
   * @throws IOException declared by superclass
   * @throws ServletException declared by superclass
   */
  public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    HttpSession session = request.getSession();
    ServletContext ctx = session.getServletContext();

    try {
      this.log = ExoLogger.getLogger(getClass());

      StandaloneContainer portalContainer = StandaloneContainer.getInstance();
      // create WindowInfosContainer instance if there's no one
      WindowInfosContainer.createInstance(portalContainer, session.getId(), request.getRemoteUser());

      // create/get PortalFramework instance
      framework = FRAMEWORKS.get(session.getId());
      if (framework == null) {
        framework = new PortalFramework(portalContainer);
        FRAMEWORKS.put(session.getId(), framework);
      }
      framework.init(session);

      if (request.getParameter("portal:page") != null)
        framework.setCurrentPage(request.getParameter("portal:page"));
      else if (request.getParameter("portal:newpage") != null)
        framework.addPortalPage(request.getParameter("portal:newpage"));
      else if (request.getParameter("portal:delpage") != null)
        framework.delPortalPage(request.getParameter("portal:delpage"));
      session.setAttribute("portalPage", framework.getCurrentPage());

      ArrayList<String> pages = new ArrayList<String>();
      for (Iterator<String> i = framework.getPortalPages(); i.hasNext(); )
        pages.add(i.next());
      session.setAttribute("portalPages", pages);

      Map<String, String[]> servletParams = request.getParameterMap();

      LayoutPlt newPlt = null;
      String pltIdToDel = null;
      if (servletParams.containsKey("pAction")) {
        if (servletParams.get("pAction")[0].equals("add")) {
          String id = framework.addPortlet(servletParams.get("pApp")[0], servletParams.get("pName")[0]);
          if (id != null) {
            newPlt = new LayoutPlt(servletParams.get("pApp")[0], servletParams.get("pName")[0], id);
            framework.addPortletToPage(id);
          }
        } else if (servletParams.get("pAction")[0].equals("del")) {
          pltIdToDel = servletParams.get("pId")[0];
          framework.removePortlet(pltIdToDel);
        }
      }

      InputStream tmpl = null;
      String tmplPath = "/pages/" + framework.getCurrentPage() + "_" + request.getRemoteUser().hashCode() + ".tmpl";
      try {
        tmpl = new FileInputStream(ctx.getRealPath(tmplPath));
      } catch(Exception e) {
        tmpl = ctx.getResourceAsStream("/default.tmpl");
      }
      List<LayoutItem> layout = Template.getPortletLayout(tmpl);
      List<LayoutPlt> plts = Template.getPortletList(layout);
      for (Iterator<LayoutPlt> i = plts.iterator(); i.hasNext();) {
        LayoutPlt plt = i.next();
        String id = null;
        if (plt.getId() != null) {
          if (framework.getPortletWindowById(plt.getId()) == null)
            id = framework.addPortletWithId(plt.getApp(), plt.getName(), plt.getId());
        } else {
          id = framework.addPortlet(plt.getApp(), plt.getName());
          plt.setId(id);
        }
        if (!framework.getPagePortlets().contains(id))
          framework.addPortletToPage(id);
      }
      if (newPlt != null)
        Template.addPortletToLayout(layout, newPlt);
      if (pltIdToDel != null)
        Template.delLayoutPltById(layout, pltIdToDel);
      if (request.getParameter("portal:layout") != null)
        Template.changeLayoutWith(layout, request.getParameter("portal:layout"));
      Template.saveLayoutAs(layout, new FileOutputStream(ctx.getRealPath(tmplPath)));
      if (request.getParameter("portal:layout") != null)
        return;

      // --- this dummy http response is intended to avoid premature response commit on some AS-es
      HttpServletResponse dummyHttpResponse = createDummyResponse(response);

      // call PortalFramework to process current request to portlet container
      portletInfos = framework.processRequestForCurrentPage(ctx, request, dummyHttpResponse, "text/html");

      if (framework.getRedirect() != null) {
        response.sendRedirect(framework.getRedirect());
        return;
      }

      if (framework.getAction() == PCConstants.RESOURCE_INT) {
        if (framework.getResourceContentType() != null)
          response.setContentType(framework.getResourceContentType());
        else
          response.setContentType("text/html");
        Map<String, String> headers = framework.getResourceHeaders();
        if (headers != null) {
          for (Iterator<String> i = headers.keySet().iterator(); i.hasNext();) {
            String name = i.next();
            response.setHeader(name, headers.get(name));
          }
        }
        response.setStatus(framework.getResourceStatus());
        OutputStream w = response.getOutputStream();
        w.write(framework.getResourceContent());
        w.close();
        return;
      }

      Iterator<PortletInfo> plts1 = portletInfos.iterator();
      while (plts1.hasNext()) {
        PortletInfo portletinfo = plts1.next();
        portletinfo.setTitle(portletinfo.getTitle());
        portletinfo.setToRender(true);
        //Collecting session info
        HashMap<String, Object> hm = portletinfo.getSessionMap();
        sessionInfo.put(portletinfo.getPortlet().split("/")[0], hm);
      }

      generatePortletNamesJSarray(session);

      response.setContentType("text/html;charset=UTF-8");
      PrintWriter w = response.getWriter();

      includeJsp(request, response, "/layouts/" + framework.getDesktopLayout() + "/portal_header.jsp");

      layout = Template.getPortletLayout(new FileInputStream(ctx.getRealPath(tmplPath)));

      renderLayout(layout, request, response, w);

      includeJsp(request, response, "/layouts/" + framework.getDesktopLayout() + "/portal_footer.jsp");
      System.out.println("-------------------------------------------------------------");

      // Session Replication
      sessionInfo.put(SESSION_IDENTIFIER, session.getId());
      sessionInfo.put(PORTAL_IDENTIFIER, framework.getPortalName());
      try {
        if (sr == null)
          sr = new SessionReplicator();
        sessionInfo.put(REPLICATOR_IDENTIFIER, sr.toString());
        sr.send(sessionInfo);
      } catch (Exception e){
        e.printStackTrace();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param session
   */
  private void generatePortletNamesJSarray(HttpSession session) {
    Map<String, List<String>> pList = new HashMap<String, List<String>>();
    for (Iterator<String> i = framework.getPortletNames().iterator(); i.hasNext(); ) {
      String pn = i.next();
      String[] ss = pn.split("/");
      List<String> pl = pList.get(ss[0]);
      if (pl == null)
        pList.put(ss[0], pl = new ArrayList<String>());
      pl.add(ss[1]);
    }
    String pNames = "";
    boolean b = false;
    List<String> pal = new ArrayList<String>(pList.keySet());
    Collections.sort(pal);
    for (Iterator<String> i = pal.iterator(); i.hasNext(); ) {
      String pan = i.next();
      if (b)
        pNames += ", ";
      pNames += "\"" + pan + "\": [";
      List<String> pl = pList.get(pan);
      boolean b1 = false;
      for (Iterator<String> i1 = pl.iterator(); i1.hasNext(); ) {
        if (b1)
          pNames += ", ";
        pNames += "\"" + i1.next() + "\"";
        b1 = true;
      }
      pNames += "]";
      b = true;
    }
    session.setAttribute("portletNames", pNames);
  }

  private void renderLayout(List<LayoutItem> layout, HttpServletRequest request, HttpServletResponse response,
      PrintWriter w) throws ServletException, IOException {
    HttpSession session = request.getSession();
    for (Iterator<LayoutItem> i = layout.iterator(); i.hasNext();) {
      renderItem(i.next(), request, response, session, w);
    }
  }

  private void renderItem(LayoutItem item, HttpServletRequest request, HttpServletResponse response, HttpSession session,
      PrintWriter w) throws ServletException, IOException {
    if (item == null)
      return;
    if (item instanceof LayoutColumn) {
      LayoutColumn col = (LayoutColumn) item;
      request.setAttribute("col_id", col.getId());
      request.setAttribute("col_width", col.getWidth());
      request.setAttribute("col_empty", col.getChildren().size() <= 0);
      includeJsp(request, response, "/layouts/" + framework.getDesktopLayout() + "/col_header.jsp");
      renderChildren(item, request, response, session, w);
      includeJsp(request, response, "/layouts/" + framework.getDesktopLayout() + "/col_footer.jsp");
    } else if (item instanceof LayoutZone) {
      includeJsp(request, response, "/layouts/" + framework.getDesktopLayout() + "/zone_header.jsp");
      renderChildren(item, request, response, session, w);
      includeJsp(request, response, "/layouts/" + framework.getDesktopLayout() + "/zone_footer.jsp");
    } else if (item instanceof LayoutPlt)
      renderPortlet((LayoutPlt) item, request, response, session);
    else if (item instanceof LayoutHtmlText)
      w.print(item.getName());
    else if (item instanceof LayoutHtmlTag) {
      w.print("<" + item.getName() + " ");
      for (Map.Entry<String, String> attr : ((LayoutHtmlTag) item).getAttrs().entrySet())
        w.print(attr.getKey() + "=\"" + attr.getValue() + "\"");
      w.print(">");
      renderChildren(item, request, response, session, w);
      w.print("</" + item.getName() + ">");
    } else if (item.getName().equals(LayoutItem.HEADER))
      includeJsp(request, response, "/layouts/" + framework.getDesktopLayout() + "/header.jsp");
    else if (item.getName().equals(LayoutItem.FOOTER))
      includeJsp(request, response, "/layouts/" + framework.getDesktopLayout() + "/footer.jsp");
    else if (item.getName().equals(LayoutItem.PAGES))
      includeJsp(request, response, "/layouts/" + framework.getDesktopLayout() + "/tabs.jsp");
  }

  private void renderChildren(LayoutItem item, HttpServletRequest request, HttpServletResponse response,
      HttpSession session, PrintWriter w) throws ServletException, IOException {
    if (item instanceof LayoutNode) {
      for (Iterator<LayoutItem> i = ((LayoutNode) item).getChildren().iterator(); i.hasNext();)
        renderItem(i.next(), request, response, session, w);
    }
  }

  private void renderPortlet(LayoutPlt lp, HttpServletRequest request, HttpServletResponse response,
      HttpSession session) throws ServletException, IOException {
    for (Iterator<PortletInfo> i = portletInfos.iterator(); i.hasNext();) {
      PortletInfo pinf = i.next();
      if (pinf.getWid().equals(lp.getId())) {
        session.setAttribute("pinf", pinf);
        includeJsp(request, response, "/layouts/" + framework.getDesktopLayout() + "/plt.jsp");
      }
    }
  }

  private void includeJsp(HttpServletRequest request, HttpServletResponse response, String page) throws ServletException,
      IOException {
    getServletContext().getRequestDispatcher(page).include(request, response);
  }

}
