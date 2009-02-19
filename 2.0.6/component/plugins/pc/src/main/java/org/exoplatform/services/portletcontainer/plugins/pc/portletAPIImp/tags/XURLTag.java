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
 * Created by The eXo Platform SAS. Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: Aug 20, 2003 Time: 2:00:32 PM
 */
public abstract class XURLTag extends BodyTagSupport {

  /**
   * Window state.
   */
  protected WindowState             windowState;

  /**
   * Portlet mode.
   */
  protected PortletMode             portletMode;

  /**
   * Var name.
   */
  protected String                  var;

  /**
   * Secure.
   */
  protected boolean                 secure;

  /**
   * Parameters.
   */
  private HashMap<String, String[]> parameters                  = null;

  /**
   * Render parameters.
   */
  private Map<String, String[]>     renderParameters            = null;

  /**
   * Properties.
   */
  private HashMap<String, String[]> properties                  = new HashMap<String, String[]>();

  /**
   * Copy current render parameters.
   */
  protected boolean                 copyCurrentRenderParameters = false;

  /**
   * Escape xml value.
   */
  protected boolean                 escapeXml                   = true;

  /**
   * Resource id.
   */
  protected String                  resourceID;

  /**
   * Resource cacheability.
   */
  protected String                  resourceCacheability;

  /**
   * @param key key
   * @param value value
   */
  public final void addParameter(final String key, final String value) {
    if (parameters == null)
      parameters = new HashMap<String, String[]>();
    if (((value == null) || value.equals("")) && (renderParameters.get(key) != null))
      renderParameters.remove(key);
    if (parameters.get(key) == null) {
      if ((value != null) && !value.equals(""))
        parameters.put(key, new String[] { value });
    } else if ((value == null) || value.equals(""))
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

  /**
   * @param key key
   * @param values values
   */
  protected void addParameters(final String key, final String[] values) {
    if (parameters.get(key) == null) {
      if (values != null)
        parameters.put(key, values);
    } else {
      String[] oldValue = parameters.get(key);
      String[] newValue = new String[oldValue.length + values.length];
      int i = 0;
      for (String v : oldValue)
        newValue[i++] = v;
      for (String v : values)
        newValue[i++] = v;
      parameters.put(key, newValue);
    }
  }

  /**
   * @param key key
   * @param value value
   */
  public final void addProperty(final String key, final String value) {
    properties.put(key, new String[] { value });
  }

  /**
   * @param url url
   * @param map url properties
   */
  public final void setProperties(final BaseURL url, final Map<String, String[]> map) {
    if (map == null)
      return;
    if (map.containsKey(null))
      return;
    Set<String> keys = map.keySet();
    for (String name : keys) {
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

  /**
   * @param windowState window state
   */
  public final void setWindowState(final String windowState) {
    if (WindowState.MAXIMIZED.toString().equalsIgnoreCase(windowState))
      this.windowState = WindowState.MAXIMIZED;
    else if (WindowState.MINIMIZED.toString().equalsIgnoreCase(windowState))
      this.windowState = WindowState.MINIMIZED;
    else if (WindowState.NORMAL.toString().equalsIgnoreCase(windowState))
      this.windowState = WindowState.NORMAL;
    else
      this.windowState = new WindowState(windowState);
  }

  /**
   * @param portletMode portlet mode
   */
  public final void setPortletMode(final String portletMode) {
    if (PortletMode.EDIT.toString().equalsIgnoreCase(portletMode))
      this.portletMode = PortletMode.EDIT;
    else if (PortletMode.HELP.toString().equalsIgnoreCase(portletMode))
      this.portletMode = PortletMode.HELP;
    else if (PortletMode.VIEW.toString().equalsIgnoreCase(portletMode))
      this.portletMode = PortletMode.VIEW;
    else
      this.portletMode = new PortletMode(portletMode);
  }

  /**
   * @param var jsp var name
   */
  public final void setVar(final String var) {
    this.var = var;
  }

  /**
   * @param secure secure
   */
  public final void setSecure(final String secure) {
    if (secure.equals("true"))
      this.secure = true;
    else
      this.secure = false;
  }

  /**
   * @param copyCurrentRenderParameters copy current render parameters
   */
  public final void setCopyCurrentRenderParameters(final boolean copyCurrentRenderParameters) {
    this.copyCurrentRenderParameters = copyCurrentRenderParameters;
  }

  /**
   * @param escapeXml escape xml
   */
  public final void setEscapeXml(final boolean escapeXml) {
    this.escapeXml = escapeXml;
  }

  /**
   * Overridden method.
   * 
   * @param resourceID resource id
   * @see javax.servlet.jsp.tagext.TagSupport#setId(java.lang.String)
   */
  public final void setId(final String resourceID) {
    this.resourceID = resourceID;
  }

  /**
   * @param resourceCacheability cacheability
   */
  public final void setCacheability(final String resourceCacheability) {
    if (resourceCacheability != null)
      if (ResourceURLImp.isSupportedCacheLevel(resourceCacheability))
        this.resourceCacheability = resourceCacheability;
      else
        this.resourceCacheability = ResourceURL.PAGE;
  }

  /**
   * Overridden method.
   * 
   * @return tag result
   * @throws JspException
   * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
   */
  public final int doStartTag() throws JspException {
    if (parameters == null)
      parameters = new HashMap<String, String[]>();
    ServletRequest request = pageContext.getRequest();
    PortletRequest portletRequest = (PortletRequest) request.getAttribute("javax.portlet.request");
    renderParameters = new HashMap<String, String[]>(portletRequest.getParameterMap());
    return EVAL_BODY_BUFFERED;
  }

  /**
   * Overridden method.
   * 
   * @return tag evaluation resul
   * @throws JspException
   * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
   */
  public int doEndTag() throws JspException {
    BaseURL baseURL = getPortletURL();

    if (copyCurrentRenderParameters) {
      if (parameters == null)
        parameters = new HashMap<String, String[]>();
      for (String name : renderParameters.keySet()) {
        addParameters(name, renderParameters.get(name));
      }
    }

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
      } else if (baseURL instanceof ResourceURL) {
        ResourceURL resourceURL = (ResourceURL) baseURL;
        resourceURL.setResourceID(resourceID);
        resourceURL.setCacheability(resourceCacheability);
      }
      if ((var == null) || "".equals(var))
        try {
          pageContext.getOut().print(URLToString(baseURL));
        } catch (IOException e1) {
          throw new JspException(e1);
        }
      else
        pageContext.setAttribute(var, URLToString(baseURL));
    } catch (PortletModeException e) {
      throw new JspException(e);
    } catch (WindowStateException e) {
      throw new JspException(e);
    } catch (PortletSecurityException e) {
      throw new JspException(e);
    } finally {
      cleanUp();
    }

    return EVAL_PAGE;
  }

  /**
   * Cleans up class fields.
   */
  private void cleanUp() {
    parameters = null;
    properties = null;
    windowState = null;
    portletMode = null;
    var = null;
    secure = false;
    copyCurrentRenderParameters = false;
    escapeXml = true;
    resourceID = null;
    resourceCacheability = null;
  }

  /**
   * Returns the portlet URL string representation to be embedded in the markup.
   * The container-runtime-option element may re-define default portlet
   * container behavior, like the javax.portlet.escapeXml setting that disables
   * XML encoding of URLs produced by the portlet tag library as default.
   * 
   * @param baseURL base url
   * @return url string
   */
  public final String URLToString(final BaseURL baseURL) {
    return ((BaseURLImp) baseURL).toString(escapeXml);
  }

  /**
   * @return portlet url
   */
  public abstract BaseURL getPortletURL();

}
