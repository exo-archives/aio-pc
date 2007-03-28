/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.portletcontainer.test.filters;

import java.net.URLDecoder;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.exoplatform.Constants;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;

/**
 * Created by The eXo Platform SARL .
 *
 * @author <a href="mailto:lautarul@gmail.com">Roman Pedchenko</a>
 * @version $Id$
 */

public class Helper {

  //decode string
  private static String[] _decode(String s) {
    try {
      return new String[]{URLDecoder.decode(s, "UTF-8")};
    } catch (java.io.UnsupportedEncodingException e) {
      return new String[]{s};
    }
  }

  static public void putMultiply(Map target, String n, String[] param) {
    if (param != null && param.length == 1)
      target.put(n, param[0]);
    else
      target.put(n, param);
  }

  static public void parseParams(HttpServletRequest httpRequest, HashMap portalParams, HashMap portletParams) {
System.out.println(" ---------- query string: " + httpRequest.getQueryString());
    Enumeration pNames = httpRequest.getParameterNames();
    while (pNames.hasMoreElements()) {
      String n = (String) pNames.nextElement();
System.out.println(" ---------- parameter: " + n + ": [" + httpRequest.getParameterValues(n)[0] + "]");
      if (n.startsWith(Constants.PARAMETER_ENCODER))
//        putMultiply(portalParams, n, _decode(httpRequest.getParameterValues(n)[0]));
        putMultiply(portalParams, n, httpRequest.getParameterValues(n));
      else
//        putMultiply(portletParams, n, _decode(httpRequest.getParameterValues(n)[0]));
        putMultiply(portletParams, n, httpRequest.getParameterValues(n));
    }
  }

  static public boolean getActionType(String v) {
    return (v != null) && v.toLowerCase().equals("action");
  }

  static public PortletMode getPortletMode(String v, Collection pms) {
    if (v == null)
      return PortletMode.VIEW;
    if (v.toLowerCase().equals(PortletMode.EDIT.toString()))
      return PortletMode.EDIT;
    if (v.toLowerCase().equals(PortletMode.HELP.toString()))
      return PortletMode.HELP;
    Iterator i = pms.iterator();
    while (i.hasNext()) {
      PortletMode pm = (PortletMode) i.next();
      if (pm.toString().equalsIgnoreCase(v))
        return pm;
    }
    return PortletMode.VIEW;
  }

  static public WindowState getWindowState(String v, Collection wss) {
    if (v == null)
      return WindowState.NORMAL;
    if (v.toLowerCase().equals(WindowState.MAXIMIZED.toString()))
      return WindowState.MAXIMIZED;
    if (v.toLowerCase().equals(WindowState.MINIMIZED.toString()))
      return WindowState.MINIMIZED;
    Iterator i = wss.iterator();
    while (i.hasNext()) {
      WindowState ws = (WindowState) i.next();
      if (ws.toString().equalsIgnoreCase(v))
        return ws;
    }
    return WindowState.NORMAL;
  }

}
