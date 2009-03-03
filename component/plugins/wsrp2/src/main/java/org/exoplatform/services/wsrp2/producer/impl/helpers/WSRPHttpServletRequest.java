/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp2.producer.impl.helpers;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRPHttpServletRequest extends HttpServletRequestWrapper {

  private HttpSession           wsrpSession;

  private Map<String, String[]> parameters;

  public WSRPHttpServletRequest(HttpServletRequest request) {
    super(request);
    this.wsrpSession = new WSRPHttpSession(request.getSession());
    this.parameters = new HashMap<String, String[]>();
  }

  //SESSIONS

  public HttpSession getSession(boolean create) {
    if (wsrpSession == null && create) {
      HttpServletRequest httpServletRequest = (HttpServletRequest) super.getRequest();
      wsrpSession = new WSRPHttpSession(httpServletRequest.getSession());
    }
    return wsrpSession;
  }

  public HttpSession getSession() {
    return getSession(true);
  }

  public void setWsrpSession(WSRPHttpSession wsrpSession) {
    this.wsrpSession = wsrpSession;
  }

  // PARAMETERS

  public String getParameter(String name) {
    if (parameters.get(name) == null)
      return super.getParameter(name);
    return ((String[]) parameters.get(name))[0];
  }

  public Enumeration<String> getParameterNames() {
    Enumeration<String> supernames = super.getParameterNames();
    Set<String> thisnames = parameters.keySet();
    Set<String> result = new HashSet<String>(thisnames);
    while (supernames.hasMoreElements()) {
      String elem = (String) supernames.nextElement();
      result.add(elem);
    }
    return Collections.enumeration(result);
  }

  public String[] getParameterValues(String name) {
    Set<String> result = null;
    String[] supervaluesarr = super.getParameterValues(name);
    if (supervaluesarr != null) {
      Collection<String> supervaluescoll = Arrays.asList(supervaluesarr);
      result = new HashSet<String>(supervaluescoll);
    } else {
      result = new HashSet<String>();
    }

    String[] thisvalues = parameters.get(name);
    if (thisvalues != null) {
      for (String value : thisvalues) {
        if (value != null) {
          result.add(value);
        }
      }
    }
    return (String[]) result.toArray(new String[] {});
  }

  public Map<String, String[]> getParameterMap() {
    return Collections.unmodifiableMap(parameters);
  }

  public void setParameter(String key, String value) {
    String[] valueArray = new String[1];
    valueArray[0] = value;
    parameters.put(key, valueArray);
  }

  public void setParameters(Map<String, String[]> parameters) {
    this.parameters = parameters;
  }

}
