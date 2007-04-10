/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.portletcontainer.test.filters;

import java.io.IOException;
import java.util.*;

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
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import org.apache.commons.lang.StringUtils;
import org.exoplatform.Constants;
// import org.exoplatform.container.PortalContainer;
// import org.exoplatform.container.RootContainer;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.impl.PortletApplicationsHolder;
import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.Output;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.model.DisplayName;


/**
 * Created by The eXo Platform SARL .
 *
 * @author <a href="mailto:lautarul@gmail.com">Roman Pedchenko</a>
 * @version $Id: PortletFilter.java 12011 2007-01-17 14:24:21Z sunman $
 */

public class PortletFilter implements Filter {

  HashMap wins = null;

  // Session Replication
  HashMap session_states = null;
  HashMap session_info = new HashMap();
  public static final String session_identifier = "SID";
  public static final String portal_identifier = "PID";
  String portal_container_name = "";


  protected void createWins(Map allPortletMetaData) {
    wins = new HashMap();
    Set keys = allPortletMetaData.keySet();
    Iterator i = keys.iterator();
    while (i.hasNext()) {
      String key = (String) i.next();
      PortletData portlet = (PortletData) allPortletMetaData.get(key);
      String portletApp = key.replace('.', '/');
      String[] ss = StringUtils.split(portletApp, "/");

      WindowID2 windowID = new WindowID2();
      windowID.setOwner(Constants.ANON_USER);
      windowID.setPortletApplicationName(ss[0]);
      windowID.setPortletName(ss[1]);
      windowID.setUniqueID(portletApp);
      windowID.setPersistenceId(ss[0] + "II" + ss[1]);
      windowID.setPortletMode(PortletMode.VIEW);
      windowID.setWindowState(WindowState.NORMAL);
      wins.put(portletApp, windowID);
      // System.out.println("creating: " + portletApp + ": windowID: " +
      // windowID);
    }
  }

  public HttpServletResponse createDummyResponse(HttpServletResponse original) {
    // System.out.println("name: " + original.getClass().getName());
    if (original.getClass().getName().equals("weblogic.servlet.internal.ServletResponseImpl")
        || original.getClass().getName().equals("com.ibm.ws.webcontainer.srt.SRTServletResponse")
//        || original.getClass().getName().equals("com.evermind.server.http.EvermindHttpServletResponse")
        ) {
      // System.out.println(" yes!!!!!!!!!!!!!!!!!!!!!");
      // Class responseClass = original.class.
      return new DummyResponse();
    } else
      return original;
  }

  public void init(FilterConfig filterConfig) {
  }

  public void doFilter(ServletRequest servletRequest,
      ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
    ServletContext ctx = httpRequest.getSession().getServletContext();
    httpResponse.setContentType("text/html");

    httpRequest.getSession().removeAttribute("portletinfos");

    //System.out.println("Session ID : "+httpRequest.getSession().getId());

    try {
      /*
       * PortalContainer portalContainer = PortalContainer.getInstance(); if
       * (portalContainer == null) { RootContainer rootContainer =
       * RootContainer.getInstance(); portalContainer =
       * rootContainer.getPortalContainer(ctx.getServletContextName());
       * PortalContainer.setInstance(portalContainer); }
       */
      StandaloneContainer portalContainer = StandaloneContainer.getInstance();
      portal_container_name = portalContainer.getContext().getName();
      System.out.println("Container1 : "+portalContainer);


      PortletContainerService service =
        (PortletContainerService) portalContainer.getComponentInstanceOfType(PortletContainerService.class);
      PortletApplicationsHolder holder =
        (PortletApplicationsHolder) portalContainer.getComponentInstanceOfType(PortletApplicationsHolder.class);
      SessionContainer sessionContainer =
        (SessionContainer) portalContainer.getComponentInstanceOfType(SessionContainer.class);
      if (sessionContainer == null) // creating session container if there isn't any
        sessionContainer = portalContainer.createSessionContainer("sessioncontainer", "user");


      Map allPortletMetaData = service.getAllPortletMetaData();

System.out.println("portlet metadata count: " + allPortletMetaData.size());
      if (wins == null)
        createWins(allPortletMetaData);

      HashMap portalParams = new HashMap();
      HashMap portletParams = new HashMap();
      Map actionParams = null;

      Helper.parseParams(httpRequest, portalParams, portletParams);

      String component = (String) portalParams
          .get(Constants.COMPONENT_PARAMETER);

      boolean isAction = Helper.getActionType((String) portalParams
          .get(Constants.TYPE_PARAMETER));

      httpResponse.setContentType("text/html");

      // -----------------------------------------------------------------------------------------------------

      HttpServletResponse dummyHttpResponse = createDummyResponse(httpResponse);

      // -----------------------------------------------------------------------------------------------------

      String[] hs = StringUtils.split(component, "/");

      // set MODE and STATE
      PortletMode portletMode;
      WindowState windowState;

      if (component != null) {
        portletMode = Helper.getPortletMode((String) portalParams.get(Constants.PORTLET_MODE_PARAMETER),
          holder.getPortletModes(hs[0], hs[1], "text/html"));
        windowState = Helper.getWindowState((String) portalParams.get(Constants.WINDOW_STATE_PARAMETER),
          holder.getWindowStates(hs[0]));

        WindowID2 winp = (WindowID2) wins.get(component);
        if (portletMode == null)
          portletMode = winp.getPortletMode();
        if (windowState == null)
          windowState = winp.getWindowState();
      } else {
        portletMode = PortletMode.VIEW;
        windowState = WindowState.NORMAL;
      }

      ArrayList portletinfos = new ArrayList();

      if (!portalParams.isEmpty() && portalParams.containsKey(Constants.WINDOW_STATE_PARAMETER)) {
        if (((String)portalParams.get(Constants.WINDOW_STATE_PARAMETER)).equals("maximized")) {
          Set set = wins.keySet();
          Iterator it = set.iterator();
          while (it.hasNext()) {
            String key = (String) it.next();
            WindowID2 window = (WindowID2) wins.get(key);
            window.setWindowState(WindowState.MINIMIZED);
          }
        }
      }

      // Session Replication
      int count = 0;
      String count2 = (String)httpRequest.getSession().getAttribute("count");
      count2 += "+";
      session_info.clear();


      String redirLocation = null;



      // processing action

      if (isAction && component != null) {
//      if (isAction && component != null && component.equals(portletApp)) {
        System.out.println("processing action: " + component + ": windowID: " + (WindowID2) wins.get(component));
        ActionInput actionInput = new ActionInput();
        WindowID2 win = (WindowID2) wins.get(component); // added recently from branch // EXOMAN comments
        actionInput.setWindowID(win);
        ArrayList lo = new ArrayList();
        lo.add(new Locale("en"));
        actionInput.setLocales(lo);
        actionInput.setBaseURL("/" + ctx.getServletContextName() + "/?"
            + Constants.COMPONENT_PARAMETER + "=" + win.getUniqueID());
        actionInput.setUserAttributes(new HashMap());
        actionInput.setPortletMode(portletMode);
        actionInput.setWindowState(windowState);
        actionInput.setMarkup("text/html");
        actionInput.setStateChangeAuthorized(true);

        try {
          ActionOutput o1 = service.processAction(httpRequest, dummyHttpResponse, actionInput);
          actionParams = o1.getRenderParameters();

          //renderInput.getRenderParameters().putAll(o1.getRenderParameters()); // EXOMAN comments
          redirLocation = (String) o1.getProperties().get(Output.SEND_REDIRECT);

          if (o1.getNextMode() != null)
            win.setPortletMode(o1.getNextMode());
          if (o1.getNextState() != null)
            win.setWindowState(o1.getNextState());

        } catch (Exception e2) {
          System.out.println(" !!!!!!!!!!!! error processing action of portlet " + component + ": " + e2);
          e2.printStackTrace();
          System.out.println(" !!!!!!!!!!!! trying to continue...");
        }
      }

      if (redirLocation != null) {
        httpResponse.sendRedirect(redirLocation);
        filterChain.doFilter(servletRequest, servletResponse);
        return;
      }

      // collecting portlets to render

      String[] ps = servletRequest.getParameterValues("portletName");
      ArrayList portletstoshow = null;
      if (ps != null) {
        portletstoshow = new ArrayList();
        for (String s : ps)
          portletstoshow.add(s);
        httpRequest.getSession().setAttribute("portletName", portletstoshow);
      } else {
        portletstoshow = (ArrayList) httpRequest.getSession().getAttribute("portletName");
      }

      Iterator i = null;
      if (portletstoshow == null) {
        Set keys = allPortletMetaData.keySet();
        i = keys.iterator();
      } else {
        i = portletstoshow.iterator();
      }

      // render phase

      while (i.hasNext()) {
        count++;
        PortletInfo portletinfo = new PortletInfo();
        String key = ((String) i.next()).replace('/', '.');
        String portletApp = key.replace('.', '/');
        portletinfos.add(portletinfo);
        portletinfo.portletapp = portletApp;          //portletapps.add(portletApp);
        PortletData portlet = (PortletData) allPortletMetaData.get(key);
        String[] ss = StringUtils.split(portletApp, "/");

        WindowID2 win = (WindowID2) wins.get(portletApp);

        // Create supported modes for each portlet
        try {
          Iterator iterator = service.getPortletModes(ss[0], ss[1], "text/html").iterator();
          String pmode = null;
          while (iterator.hasNext()) {
            PortletMode mode = (PortletMode) iterator.next();
            if (pmode == null) {
              pmode = mode.toString();
            } else {
              pmode = pmode + "." + mode.toString();
            }
          }
          portletinfo.mode = pmode;
        } catch (Exception e) {
          e.printStackTrace();
        }

        // Create supported states for each portlet
        try {
          Iterator iterator = service.getWindowStates(ss[0]).iterator();
          String pstate = null;
          while (iterator.hasNext()) {
            WindowState state = (WindowState) iterator.next();
            if (pstate == null) {
              pstate = state.toString();
            } else {
              pstate = pstate + "." + state.toString();
            }
          }
          portletinfo.state = pstate;
        } catch (Exception e) {
          e.printStackTrace();
        }

        if (component != null && component.equals(portletApp)) {
          win.setPortletMode(portletMode);
          win.setWindowState(windowState);
        }

        portletinfo.fqTitle = portletApp;

        ArrayList myatr0 = (ArrayList) httpRequest.getSession().getAttribute("myatr");
        boolean pltIncl = (portletParams.containsKey("fis") && (portletParams.containsKey("n" + count + "n") &&
          portletParams.get("n" + count + "n").equals("on")));
        if (!pltIncl && !portletParams.containsKey("fis"))
          pltIncl = (myatr0 != null) && !myatr0.get(count - 1).equals("");

        if (!pltIncl) {
          String sdn;
          try {
            sdn = ((DisplayName) portlet.getDisplayName().get(0)).getDisplayName();
          } catch (Exception e) { sdn = ss[1]; }
          portletinfo.title = sdn + " {mode: " + win.getPortletMode().toString() + "; state: " +
            win.getWindowState().toString() + "}";
          continue;
        }

        RenderInput renderInput = new RenderInput();
        renderInput.setWindowID(win);
        ArrayList lo = new ArrayList();
        lo.add(new Locale("en"));
        renderInput.setLocales(lo);
        renderInput.setBaseURL("/" + ctx.getServletContextName() + "/?"
            + Constants.COMPONENT_PARAMETER + "=" + win.getUniqueID());
        renderInput.setUserAttributes(new HashMap());
        renderInput.setMarkup("text/html");
        if (component != null && component.equals(portletApp) && !isAction) {
          renderInput.setRenderParameters(portletParams, true);
        } else
          renderInput.setRenderParameters(new HashMap());

        if (actionParams != null)
          renderInput.getRenderParameters().putAll(actionParams);

        renderInput.setPortletMode(win.getPortletMode());
        renderInput.setWindowState(win.getWindowState());

        System.out.println("rendering: " + portletApp + ": windowID: "
            + renderInput.getWindowID());
        // System.out.println(" -- state: " +
        // renderInput.getWindowState().toString() + "; mode: " +
        // renderInput.getPortletMode().toString());

        try {
          RenderOutput o = service.render(httpRequest, dummyHttpResponse, renderInput);
          // System.out.println(" render output: " + o);
          portletinfo.title = o.getTitle() + " {mode: " + renderInput.getPortletMode().toString() + "; state: " +
            renderInput.getWindowState().toString() + "}";
          String pout = null;

          try {
            pout = new String(o.getContent());
          } catch (Exception oe) {
            pout = "";
          }
          if (pout == null)
            pout = "";
          portletinfo.out = pout;

          // System.out.println(" output data added");

          //Collecting session info
          HashMap  hm = o.getSessionMap();
          session_info.put(ss[0], hm);

        } catch (Exception e1) {
          System.out.println(" !!!!!!!!!!!! error rendering portlet " + portletApp + ": " + e1);
          e1.printStackTrace();
          System.out.println(" !!!!!!!!!!!! trying to continue...");
        }

      }

      httpRequest.getSession().setAttribute("portletinfos", portletinfos);

    } catch (Exception e) {
      e.printStackTrace();
      httpRequest.getSession().setAttribute("portletinfos", null);
      return;
    }

    filterChain.doFilter(servletRequest, servletResponse);

    // Session Replication
    session_info.put(session_identifier, httpRequest.getSession().getId());
    session_info.put(portal_identifier, portal_container_name);
    //System.out.println("Container : "+portalContainer);
    Set set = session_info.keySet();
    try {
      SessionReplicator sr = new SessionReplicator();
      //session_states = sr.send(session_info);
      sr.send(session_info);
      //System.out.println("States :"+ session_states);
    } catch (Exception e){
      //e.printStackTrace();
    }

  }

  public void destroy() {
  }

}
