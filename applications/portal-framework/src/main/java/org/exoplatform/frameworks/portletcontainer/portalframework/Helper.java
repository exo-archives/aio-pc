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
package org.exoplatform.frameworks.portletcontainer.portalframework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;

import org.exoplatform.services.portletcontainer.PCConstants;

/**
 * Created by The eXo Platform SAS  .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */

/**
 * Helper class that contains utility functions to use in
 * portal framework.
 */
public final class Helper {

  /**
   * Default private constructor.
   */
  private Helper() {
  }


  /**
   * Distinguishes portal service parameters from portlet parameters.
   *
   * @param httpRequest http servlet request
   * @param portalParams portal service params
   * @param portletParams portlet params
   * @param propertyParams property params
   */
  public static void parseParams(final HttpServletRequest httpRequest, final HashMap<String, String[]> portalParams,
      final HashMap<String, String[]> portletParams, final HashMap<String, String[]> propertyParams) {
    System.out.println(" ---------- query string: " + httpRequest.getQueryString());
    Enumeration<String> pNames = httpRequest.getParameterNames();
    while (pNames.hasMoreElements()) {
      String n = pNames.nextElement();
      System.out.println(" ---------- parameter: " + n + ": [" + httpRequest.getParameterValues(n)[0] + "]");
      if (n.startsWith(org.exoplatform.Constants.PARAMETER_ENCODER))
        portalParams.put(n, httpRequest.getParameterValues(n));
      else if (n.startsWith(org.exoplatform.Constants.PROPERTY_ENCODER))
        propertyParams.put(n, httpRequest.getParameterValues(n));
      else
        portletParams.put(n, httpRequest.getParameterValues(n));
    }
  }

  /**
   * Converts action type from string representation to an integer constant.
   *
   * @param v string representation of an action type
   * @return numeric representation of an action type
   */
  public static int getActionType(final String v) {
    if (v == null)
      return PCConstants.renderInt;
    if (v.toLowerCase().equals("render"))
      return PCConstants.renderInt;
    if (v.toLowerCase().equals("action"))
      return PCConstants.actionInt;
    if (v.toLowerCase().equals("event"))
      return PCConstants.eventInt;
    if (v.toLowerCase().equals("resource"))
      return PCConstants.resourceInt;
    return PCConstants.renderInt;
  }

  /**
   * Converts portlet mode from string representation to an PortletMode class instance.
   *
   * @param v string representation of a portlet mode
   * @param pms collection of portlet modes that are allowed at a moment
   * @return PortletMode object
   */
  public static PortletMode getPortletMode(final String v, final Collection<PortletMode> pms) {
    if (v == null)
      return null;
    if (v == "")
      return null;
    if (v.equalsIgnoreCase(PortletMode.VIEW.toString()))
      return PortletMode.VIEW;
    if (v.equalsIgnoreCase(PortletMode.EDIT.toString()))
      return PortletMode.EDIT;
    if (v.equalsIgnoreCase(PortletMode.HELP.toString()))
      return PortletMode.HELP;
    Iterator<PortletMode> i = pms.iterator();
    while (i.hasNext()) {
      PortletMode pm = i.next();
      if (pm.toString().equalsIgnoreCase(v))
        return pm;
    }
    return PortletMode.VIEW;
  }

  /**
   * Converts window state from string representation to an WindowState class instance.
   *
   * @param v string representation of a window state
   * @param wss collection of window states that are allowed at a moment
   * @return WindowState object
   */
  public static WindowState getWindowState(final String v, final Collection<WindowState> wss) {
    if (v == null)
      return null;
    if (v == "")
      return null;
    if (v.equalsIgnoreCase(WindowState.NORMAL.toString()))
      return WindowState.NORMAL;
    if (v.equalsIgnoreCase(WindowState.MAXIMIZED.toString()))
      return WindowState.MAXIMIZED;
    if (v.equalsIgnoreCase(WindowState.MINIMIZED.toString()))
      return WindowState.MINIMIZED;
    Iterator<WindowState> i = wss.iterator();
    while (i.hasNext()) {
      WindowState ws = i.next();
      if (ws.toString().equalsIgnoreCase(v))
        return ws;
    }
    return WindowState.NORMAL;
  }

  /**
   * Creates PortletInfo object for a given portlet and fills most of necessary fields.
   *
   * @param framework PortalFramework instance
   * @param plt portlet name
   * @return created PortletInfo object
   */
  public static PortletInfo createPortletInfo(final PortalFramework framework, final String plt) {
    PortletInfo portletinfo = new PortletInfo();
    portletinfo.setPortlet(plt);
    String[] pl = org.apache.commons.lang.StringUtils.split(plt, "/");
    WindowID2 win = framework.getWindowID(plt);
    portletinfo.setWid(win.getUniqueID());
    portletinfo.setTitle(framework.getPortletDisplayName(plt));
    if (portletinfo.getTitle() == null)
      portletinfo.setTitle(pl[1]);

    // Create supported modes for each portlet
    portletinfo.setMode(win.getPortletMode().toString());
    portletinfo.setModes(null);
    try {
      Iterator<PortletMode> i = framework.getPortletModes(pl[0], pl[1]).iterator();
      ArrayList<String> pmode = null;
      while (i.hasNext()) {
        PortletMode mode = i.next();
        if (pmode == null)
          pmode = new ArrayList<String>();
        pmode.add(mode.toString());
      }
      portletinfo.setModes(pmode);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Create supported states for each portlet
    portletinfo.setState(win.getWindowState().toString());
    portletinfo.setStates(null);
    try {
      Iterator<WindowState> i = framework.getWindowStates(pl[0], pl[1]).iterator();
      ArrayList<String> pstate = null;
      while (i.hasNext()) {
        WindowState state = i.next();
        if (pstate == null)
          pstate = new ArrayList<String>();
        pstate.add(state.toString());
      }
      portletinfo.setStates(pstate);
    } catch (Exception e) { }
    return portletinfo;
  }

  /**
   * Appends params from source to destination map so that params with the same name
   * weren't replaced by each other but appended to a list.
   *
   * @param dest destination map
   * @param src source map
   */
  public static void appendParams(final Map<String, String[]> dest, final Map<String, String[]> src) {
    for (Iterator<String> i = src.keySet().iterator(); i.hasNext();) {
      String paramName = i.next();
      if (!dest.containsKey(paramName)) {
        dest.put(paramName, src.get(paramName));
      } else {
        ArrayList<String> v = new ArrayList<String>();
        v.addAll(Arrays.asList(dest.get(paramName)));
        v.addAll(Arrays.asList(src.get(paramName)));
        dest.put(paramName, v.toArray(new String[]{}));
      }
    }
  }

  /**
   * Returns from a string array a string with index 0, if array is null, returns null.
   *
   * @param strings string array
   * @return string with index 0 or null, if array if null
   */
  public static String string0(final String[] strings) {
    if (strings == null)
      return null;
    return strings[0];
  }

}
