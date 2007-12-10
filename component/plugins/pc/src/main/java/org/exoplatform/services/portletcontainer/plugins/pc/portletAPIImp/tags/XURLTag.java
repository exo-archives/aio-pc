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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.tags;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.portlet.BaseURL;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.BaseURLImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceURLImp;

/**
 * Created by The eXo Platform SAS
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 20, 2003
 * Time: 2:00:32 PM
 */
public abstract class XURLTag extends BodyTagSupport {

  protected WindowState             windowState;

  protected PortletMode             portletMode;

  protected String                  var;

  protected boolean                 secure;

  private HashMap<String, String[]> parameters                  = null;

  private HashMap<String, String[]> properties                  = new HashMap<String, String[]>();

  protected boolean                 copyCurrentRenderParameters = false;

  protected boolean                 escapeXml                   = true;

  protected boolean                 changedEscapeXml            = false;

  protected String                  resourceID;

  protected String                  resourceCacheability;

  public void addParameter(String key,
                           String value) {
    if (parameters.get(key) == null) {
      if (value != null && !value.equals(""))
        parameters.put(key, new String[] { value });
    } else {
      if (value == null || value.equals(""))
        parameters.remove(key);
      else {
        String[] oldValue = parameters.get(key);
        String[] newValue = new String[oldValue.length + 1];
        int i = 0;
        for (String v : oldValue)
          newValue[i++] = v;
        newValue[i] = value;
        parameters.put(key, newValue);
      }
    }
  }

  public void addProperty(String key,
                          String value) {
    properties.put(key, new String[] { value });
  }

  public void setProperties(BaseURL url,
                            Map<String, String[]> map) {
    if (map == null)
      return;
    if (map.containsKey(null))
      return;
    Set<String> keys = map.keySet();
    for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
      String name = iter.next();
      Object value = map.get(name);
      if (value instanceof String)
        url.addProperty(name, (String) value);
      else if (value instanceof String[]) {
        String[] saVal = (String[]) value;
        for (String sVal : saVal)
          url.addProperty(name, sVal);
      }
    }
  }

  public void setWindowState(String windowState) {
    if (WindowState.MAXIMIZED.toString().equals(windowState))
      this.windowState = WindowState.MAXIMIZED;
    else if (WindowState.MINIMIZED.toString().equals(windowState))
      this.windowState = WindowState.MINIMIZED;
    else if (WindowState.NORMAL.toString().equals(windowState))
      this.windowState = WindowState.NORMAL;
    else
      this.windowState = new WindowState(windowState);
  }

  public void setPortletMode(String portletMode) {
    if (PortletMode.EDIT.toString().equals(portletMode))
      this.portletMode = PortletMode.EDIT;
    else if (PortletMode.HELP.toString().equals(portletMode))
      this.portletMode = PortletMode.HELP;
    else if (PortletMode.VIEW.toString().equals(portletMode))
      this.portletMode = PortletMode.VIEW;
    else
      this.portletMode = new PortletMode(portletMode);
  }

  public void setVar(String var) {
    this.var = var;
  }

  public void setSecure(String secure) {
    if (secure.equals("true"))
      this.secure = true;
    else
      this.secure = false;
  }

  public void setCopyCurrentRenderParameters(boolean copyCurrentRenderParameters) {
    this.copyCurrentRenderParameters = copyCurrentRenderParameters;
    if (copyCurrentRenderParameters) {
      ServletRequest request = pageContext.getRequest();
      PortletRequest portletRequest = (PortletRequest) request.getAttribute("javax.portlet.request");
      if (parameters == null)
        parameters = new HashMap<String, String[]>();
      parameters.putAll(portletRequest.getParameterMap());
    }
  }

  public void setEscapeXml(boolean escapeXml) {
    this.escapeXml = escapeXml;
    this.changedEscapeXml = true;
  }

  public void setId(String resourceID) {
    this.resourceID = resourceID;
  }

  public void setCacheability(String resourceCacheability) {
    if (resourceCacheability != null) {
      if (ResourceURLImp.isSupportedCacheLevel(resourceCacheability)) {
        this.resourceCacheability = resourceCacheability;
      } else {
        this.resourceCacheability = ResourceURL.PAGE;
      }
    }
  }

  public int doStartTag() throws JspException {
    if (parameters == null)
      parameters = new HashMap<String, String[]>();
    return EVAL_BODY_BUFFERED;
  }

  public int doEndTag() throws JspException {
    BaseURL baseURL = getPortletURL();
    baseURL.setParameters(parameters);
    setProperties(baseURL, properties);
    try {
      baseURL.setSecure(secure);
      if (baseURL instanceof PortletURL) {
        PortletURL portletURL = (PortletURL) baseURL;
        if (portletMode != null)
          portletURL.setPortletMode(portletMode);
        if (windowState != null)
          portletURL.setWindowState(windowState);
      } else {
        if (baseURL instanceof ResourceURL) {
          ResourceURL resourceURL = (ResourceURL) baseURL;
          resourceURL.setResourceID(resourceID);
          resourceURL.setCacheability(resourceCacheability);
        }
      }
    } catch (PortletModeException e) {
      throw new JspException(e);
    } catch (WindowStateException e) {
      throw new JspException(e);
    } catch (PortletSecurityException e) {
      throw new JspException(e);
    } finally {
      cleanUp();
    }

    if (var == null || "".equals(var)) {
      try {
        pageContext.getOut().print(URLToString(baseURL));
      } catch (IOException e1) {
        throw new JspException(e1);
      }
    } else {
      pageContext.setAttribute(var, URLToString(baseURL));
    }
    return EVAL_PAGE;
  }

  private void cleanUp() {
    parameters = null;
    properties = null;
    windowState = null;
    portletMode = null;
    var = null;
    secure = false;
    copyCurrentRenderParameters = false;
    escapeXml = true;
    changedEscapeXml = false;
    resourceID = null;
    resourceCacheability = null;
  }

  public String URLToString(BaseURL baseURL) {
    if (changedEscapeXml)
      return ((BaseURLImp) baseURL).toString(escapeXml);
    return ((BaseURLImp) baseURL).toString();
  }

  public abstract BaseURL getPortletURL();

}
