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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.portlet.PortalContext;
import javax.portlet.PortletConfig;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.SecurityConstraint;
import org.exoplatform.services.portletcontainer.pci.model.SecurityRoleRef;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletApplicationProxy;

/**
 * Created by The eXo Platform SAS.
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * Date: Jul 27, 2003
 * Time: 8:44:32 PM
 *
 * This implementation acts like a wrapper to the global ServletRequest This object
 * should be pooled, therefore two fill and empty methods are provided.
 */
public abstract class PortletRequestImp extends HttpServletRequestWrapper implements
    PortletRequest, Map {

  /**
   * Logger.
   */
  private final Log log;

  /**
   * Exo container.
   */
  protected ExoContainer cont;

  /**
   * Request context.
   */
  protected RequestContext reqCtx;

  /**
   * Param names.
   */
  private Vector<String> paramNames;

  /**
   * Filtered map.
   */
  private Map<String, String[]> filteredMap;

  /**
   * Was encoding modified.
   */
  protected boolean encodingModified;

  /**
   * Encoding.
   */
  protected String enc;

  /**
   * Render parameters.
   */
  private final Map<String, String[]> renderParameters;

  /**
   * @param reqCtx request context
   */
  public PortletRequestImp(final RequestContext reqCtx) {
    super(reqCtx.getHttpServletRequest());
    this.log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
    this.cont = ((PortletContextImpl) reqCtx.getPortletContext()).getContainer();
    this.reqCtx = reqCtx;
    this.enc = reqCtx.getHttpServletRequest().getCharacterEncoding();
    this.renderParameters = reqCtx.getInput().getRenderParameters();
  }

  /**
   * Overridden method.
   *
   * @param windowState window state
   * @return is window state allowed
   * @see javax.portlet.PortletRequest#isWindowStateAllowed(javax.portlet.WindowState)
   */
  public final boolean isWindowStateAllowed(final WindowState windowState) {
    Enumeration<WindowState> e = reqCtx.getPortalContext().getSupportedWindowStates();
    while (e.hasMoreElements()) {
      WindowState supportedWindowState = e.nextElement();
      if (supportedWindowState.equals(windowState))
        return true;
    }
    return false;
  }

  /**
   * Overridden method.
   *
   * @param portletMode portlet mode
   * @return is portlet mode allowed
   * @see javax.portlet.PortletRequest#isPortletModeAllowed(javax.portlet.PortletMode)
   */
  public final boolean isPortletModeAllowed(final PortletMode portletMode) {
    Enumeration<PortletMode> e = reqCtx.getPortalContext().getSupportedPortletModes();
    while (e.hasMoreElements()) {
      PortletMode supportedPortletMode = e.nextElement();
      if (supportedPortletMode.toString().toLowerCase()
          .equals(portletMode.toString().toLowerCase()))
        return true;
    }
    return false;
  }

  /**
   * @return portlet config
   */
  public final PortletConfig getPortletConfig() {
    ExoContainer manager = cont;
    String portletAppName = reqCtx.getPortletWindowInternal().getWindowID()
        .getPortletApplicationName();
    PortletApplicationProxy proxy = (PortletApplicationProxy) manager
        .getComponentInstance(portletAppName);
    return proxy.getPortletConfig(reqCtx.getPortletWindowInternal().getWindowID().getPortletName());
  }

  /**
   * @return input
   */
  public final Input getInput() {
    return reqCtx.getInput();
  }

  /**
   * Overridden method.
   *
   * @return portlet mode
   * @see javax.portlet.PortletRequest#getPortletMode()
   */
  public final PortletMode getPortletMode() {
    return reqCtx.getInput().getPortletMode();
  }

  /**
   * Overridden method.
   *
   * @return window state
   * @see javax.portlet.PortletRequest#getWindowState()
   */
  public final WindowState getWindowState() {
    return reqCtx.getInput().getWindowState();
  }

  /**
   * Overridden method.
   *
   * @return preferences
   * @see javax.portlet.PortletRequest#getPreferences()
   */
  public final PortletPreferences getPreferences() {
    return reqCtx.getPortletWindowInternal().getPreferences();
  }

  /**
   * Overridden method.
   *
   * @return portlet session
   * @see javax.portlet.PortletRequest#getPortletSession()
   */
  public final PortletSession getPortletSession() {
    return getPortletSession(true);
  }

  /**
   * Overridden method.
   *
   * @param create if to create
   * @return portlet session
   * @see javax.portlet.PortletRequest#getPortletSession(boolean)
   */
  public final PortletSession getPortletSession(final boolean create) {
    if (create) {
      if (reqCtx.getSession().isSessionValid())
        return reqCtx.getSession();
      reqCtx.getSession().setSession(((HttpServletRequest) super.getRequest()).getSession(),
          reqCtx.getPortletWindowInternal().getWindowID().getUniqueID());
      return reqCtx.getSession();
    }
    // HttpSession tmpSession = ((HttpServletRequest)
    // super.getRequest()).getSession(false);
    HttpSession tmpSession = reqCtx.getSession().getSession();
    if (tmpSession == null)
      return null;
    // to check either session was expired
    try {
      tmpSession.getLastAccessedTime();
    } catch (IllegalStateException e) {
      return null;
    }
    return reqCtx.getSession();
  }

  /**
   * Overridden method.
   *
   * @return is requested session id valid
   * @see javax.servlet.http.HttpServletRequestWrapper#isRequestedSessionIdValid()
   */
  public final boolean isRequestedSessionIdValid() {
    try {
      // ((HttpServletRequest) super.getRequest()).getSession()
      // .getLastAccessedTime();
      return ((HttpServletRequest) super.getRequest()).isRequestedSessionIdValid();
    } catch (IllegalStateException e) {
      log.error("IllegalStateExcetion sent in PortletRequestImp isRequestedSessionIdValid()", e);
      return false;
    }
  }

  /**
   * Overridden method.
   *
   * @param s name
   * @return value
   * @see javax.portlet.PortletRequest#getProperty(java.lang.String)
   */
  public final String getProperty(final String s) {
    String header = ((HttpServletRequest) super.getRequest()).getHeader(s);
    if (header != null)
      return header;
    return reqCtx.getPortalContext().getProperty(s);
  }

  /**
   * Overridden method.
   *
   * @param s name
   * @return values
   * @see javax.portlet.PortletRequest#getProperties(java.lang.String)
   */
  public final Enumeration<String> getProperties(final String s) {
    Enumeration<String> header = ((HttpServletRequest) super.getRequest()).getHeaders(s);
    return header;
  }

  /**
   * Overridden method.
   *
   * @return property names
   * @see javax.portlet.PortletRequest#getPropertyNames()
   */
  public final Enumeration<String> getPropertyNames() {
    Enumeration<String> headerNames = ((HttpServletRequest) super.getRequest()).getHeaderNames();
    Enumeration<String> portalPropertyNames = reqCtx.getPortalContext().getPropertyNames();
    Collection<String> global = new ArrayList<String>();
    while (portalPropertyNames.hasMoreElements()) {
      String s = portalPropertyNames.nextElement();
      global.add(s);
    }
    while (headerNames.hasMoreElements()) {
      String s = headerNames.nextElement();
      global.add(s);
    }
    return Collections.enumeration(global);
  }

  /**
   * Overridden method.
   *
   * @return portal context
   * @see javax.portlet.PortletRequest#getPortalContext()
   */
  public final PortalContext getPortalContext() {
    return reqCtx.getPortalContext();
  }

  /**
   * Overridden method.
   *
   * @return response content types
   * @see javax.portlet.PortletRequest#getResponseContentType()
   */
  public final String getResponseContentType() {
    List<Supports> l = reqCtx.getPortletDatas().getSupports();
    String markup = reqCtx.getInput().getMarkup();
    String inputPortletMode = reqCtx.getInput().getPortletMode().toString();
    for (int i = 0; i < l.size(); i++) {
      Supports supportsType = l.get(i);
      String mimeType = supportsType.getMimeType();
      if (mimeType.equals(markup)) {
        List<String> portletModes = supportsType.getPortletMode();
        for (int modeIdx = 0; modeIdx < portletModes.size(); modeIdx++) {
          String portletMode = portletModes.get(modeIdx);
          if (portletMode.equals(inputPortletMode))
            return mimeType;
        }
      }
    }
    return PCConstants.XHTML_MIME_TYPE;
  }

  /**
   * Overridden method.
   *
   * @return response content type
   * @see javax.portlet.PortletRequest#getResponseContentTypes()
   */
  public final Enumeration<String> getResponseContentTypes() {
    List<String> result = new ArrayList<String>();
    result.add(getResponseContentType());
    String markup = reqCtx.getInput().getMarkup();
    String inputPortletMode = reqCtx.getInput().getPortletMode().toString();
    for (Object element : reqCtx.getSupportedContents()) {
      String supportedContent = (String) element;
      List l = reqCtx.getPortletDatas().getSupports();
      for (int i = 0; i < l.size(); i++) {
        Supports supportsType = (Supports) l.get(i);
        String mimeType = supportsType.getMimeType();
        if (supportedContent.equals(mimeType) && !supportedContent.equals(markup)) {
          List portletModes = supportsType.getPortletMode();
          for (Iterator iter2 = portletModes.iterator(); iter2.hasNext();) {
            String portletMode = (String) iter2.next();
            if (portletMode.equals(inputPortletMode)) {
              result.add(mimeType);
              break;
            }
          }
        }
      }
    }
    return Collections.enumeration(result);
  }

  /**
   * Overridden method.
   *
   * @param role role
   * @return is user in role
   * @see javax.servlet.http.HttpServletRequestWrapper#isUserInRole(java.lang.String)
   */
  public final boolean isUserInRole(final String role) {
    List l = reqCtx.getPortletDatas().getSecurityRoleRef();
    for (Iterator iterator = l.iterator(); iterator.hasNext();) {
      SecurityRoleRef securityRoleRef = (SecurityRoleRef) iterator.next();
      if (securityRoleRef.getRoleName().equals(role)) {
        String roleLink = securityRoleRef.getRoleLink();
        if ((roleLink == null) || "".equals(roleLink)) {
          if (isRoleDefinedInWebXML(role))
            return super.isUserInRole(role);
          return false;
        }
        if (isRoleDefinedInWebXML(roleLink))
          return super.isUserInRole(roleLink);
        return false;
      }
    }
    return false;
  }

  /**
   * @param role role
   * @return is role defined in WEB.XML
   */
  private boolean isRoleDefinedInWebXML(final String role) {
    for (Object element : reqCtx.getRoles()) {
      String roleDefined = (String) element;
      if (roleDefined.equals(role))
        return true;
    }
    return false;
  }

  /**
   * @return portlet datas
   */
  public final Portlet getPortletDatas() {
    return reqCtx.getPortletDatas();
  }

  /**
   * @return portlet window internal object
   */
  public final PortletWindowInternal getPortletWindowInternal() {
    return reqCtx.getPortletWindowInternal();
  }

  /**
   * @param portletName portlet name
   * @return does it need security constraint
   */
  public final boolean needsSecurityContraints(final String portletName) {
    for (Object element : reqCtx.getSecurityContraints()) {
      SecurityConstraint securityConstraint = (SecurityConstraint) element;
      List l = securityConstraint.getPortletCollection().getPortletName();
      for (Iterator iterator2 = l.iterator(); iterator2.hasNext();) {
        String portletN = (String) iterator2.next();
        if (portletN.equals(portletName))
          return true;
      }
    }
    return false;
  }

  /**
   * Overridden method.
   *
   * @return auth type
   * @see javax.servlet.http.HttpServletRequestWrapper#getAuthType()
   */
  public final String getAuthType() {
    String type = super.getAuthType();
    if (HttpServletRequest.BASIC_AUTH.equals(type))
      return PortletRequest.BASIC_AUTH;
    else if (HttpServletRequest.DIGEST_AUTH.equals(type))
      return PortletRequest.DIGEST_AUTH;
    else if (HttpServletRequest.CLIENT_CERT_AUTH.equals(type))
      return PortletRequest.CLIENT_CERT_AUTH;
    else if (HttpServletRequest.FORM_AUTH.equals(type))
      return PortletRequest.FORM_AUTH;
    else
      return type;
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @return value
   * @see javax.servlet.ServletRequestWrapper#getAttribute(java.lang.String)
   */
  public final Object getAttribute(final String name) {
    if (name == null)
      throw new IllegalArgumentException("The attribute name cannot be null");
    return super.getAttribute(name);
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @param value value
   * @see javax.servlet.ServletRequestWrapper#setAttribute(java.lang.String, java.lang.Object)
   */
  public final void setAttribute(final String name, final Object value) {
    if (name == null)
      throw new IllegalArgumentException("The attribute name cannot be null");
    // when the value is null, should have the same effect as removeAttribute
    // (Spec)
    if (value == null)
      super.removeAttribute(name);
    else
      super.setAttribute(name, value);
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @see javax.servlet.ServletRequestWrapper#removeAttribute(java.lang.String)
   */
  public final void removeAttribute(final String name) {
    if (name == null)
      throw new IllegalArgumentException("The attribute name cannot be null");
    super.removeAttribute(name);
  }

  /**
   * Overridden method.
   *
   * @return context path
   * @see javax.servlet.http.HttpServletRequestWrapper#getContextPath()
   */
  public final String getContextPath() {
    return "/" + this.reqCtx.getPortletContext().getPortletContextName();
  }

  // Bridge methods

  /**
   * Overridden method.
   *
   * @return size
   * @see java.util.Map#size()
   */
  public final int size() {
    int n = 0;
    Enumeration keys = getAttributeNames();
    while (keys.hasMoreElements())
      // String key = (String) keys.nextElement();
      n++;
    return n;
  }

  /**
   * Overridden method.
   *
   * @return is empty
   * @see java.util.Map#isEmpty()
   */
  public final boolean isEmpty() {
    return !getAttributeNames().hasMoreElements();
  }

  /**
   * Overridden method.
   *
   * @param key key
   * @return if contains key
   * @see java.util.Map#containsKey(java.lang.Object)
   */
  public final boolean containsKey(final Object key) {
    return (getAttribute((String) key) != null);
  }

  /**
   * Overridden method.
   *
   * @param value value
   * @return if contains value
   * @see java.util.Map#containsValue(java.lang.Object)
   */
  public final boolean containsValue(final Object value) {
    boolean match = false;
    Enumeration<String> keys = getAttributeNames();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      Object val = getAttribute(key);
      if (value.equals(val)) {
        match = true;
        break;
      }
    }
    return match;
  }

  /**
   * Overridden method.
   *
   * @param key key
   * @return value
   * @see java.util.Map#get(java.lang.Object)
   */
  public final Object get(final Object key) {
    return getAttribute((String) key);
  }

  /**
   * Overridden method.
   *
   * @param key key
   * @param value value
   * @return value
   * @see java.util.Map#put(java.lang.Object, java.lang.Object)
   */
  public final Object put(final Object key, final Object value) {
    Object result = null;
    if (containsKey(key))
      result = getAttribute((String) key);
    setAttribute((String) key, value);
    return result;
  }

  /**
   * Overridden method.
   *
   * @param key key
   * @return remove object
   * @see java.util.Map#remove(java.lang.Object)
   */
  public final Object remove(final Object key) {
    Object result = null;
    if (containsKey(key))
      result = getAttribute((String) key);
    removeAttribute((String) key);
    return result;
  }

  /**
   * Overridden method.
   *
   * @param t source map
   * @see java.util.Map#putAll(java.util.Map)
   */
  public final void putAll(final Map t) {
    for (Iterator i = t.keySet().iterator(); i.hasNext();) {
      String key = (String) i.next();
      Object value = t.get(key);
      put(key, value);
    }
  }

  /**
   * Overridden method.
   *
   * @see java.util.Map#clear()
   */
  public final void clear() {
    throw new UnsupportedOperationException();
  }

  /**
   * Overridden method.
   *
   * @return key set
   * @see java.util.Map#keySet()
   */
  public final Set keySet() {
    throw new UnsupportedOperationException();
  }

  /**
   * Overridden method.
   *
   * @return values
   * @see java.util.Map#values()
   */
  public final Collection values() {
    throw new UnsupportedOperationException();
  }

  /**
   * Overridden method.
   *
   * @return entry set
   * @see java.util.Map#entrySet()
   */
  public final Set entrySet() {
    throw new UnsupportedOperationException();
  }

  /**
   * Overridden method.
   *
   * @return locale
   * @see javax.servlet.ServletRequestWrapper#getLocale()
   */
  public final Locale getLocale() {
    List<Locale> locales = reqCtx.getInput().getLocales();
    if ((locales != null) && !locales.isEmpty())
      return locales.iterator().next();
    return super.getLocale();
  }

  /**
   * Overridden method.
   *
   * @return locales
   * @see javax.servlet.ServletRequestWrapper#getLocales()
   */
  public final Enumeration<Locale> getLocales() {
    List<Locale> locales = reqCtx.getInput().getLocales();
    if (locales != null)
      return Collections.enumeration(locales);
    return super.getLocales();
  }

  /**
   * Overridden method.
   *
   * added for jsr-286
   *
   * @return window id
   * @see javax.portlet.PortletRequest#getWindowID()
   */
  public final String getWindowID() {
    return reqCtx.getInput().getInternalWindowID().getUniqueID();
  }

  /**
   * Overridden method.
   *
   * @param param name
   * @return value
   * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
   */
  public String getParameter(final String param) {
    if ((param == null) || param.startsWith(Constants.PARAMETER_ENCODER))
      throw new IllegalArgumentException("parameter must not be null");
    Object obj = renderParameters.get(param);
    if (obj instanceof String[]) {
      String[] tmp = (String[]) obj;
      return tmp[0];
    }
    return (String) obj;
  }

  /**
   * Overridden method.
   *
   * @return parameter names
   * @see javax.servlet.ServletRequestWrapper#getParameterNames()
   */
  public Enumeration<String> getParameterNames() {
    if (paramNames == null) {
      Set<String> set = renderParameters.keySet();
      paramNames = new Vector<String>();
      for (String key : set)
        if (!key.startsWith(Constants.PARAMETER_ENCODER))
          paramNames.add(key);
    }
    return paramNames.elements();
  }

  /**
   * Overridden method.
   *
   * @param s name
   * @return values
   * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
   */
  public String[] getParameterValues(final String s) {
    if ((s == null) || s.startsWith(Constants.PARAMETER_ENCODER))
      throw new IllegalArgumentException("parameter must not be null");
    Object o = renderParameters.get(s);
    if (o == null)
      return null;
    if (o instanceof String) {
      String[] a = { (String) o };
      return a;
    }
    return (String[]) o;
  }

  /**
   * Overridden method.
   *
   * @return parameter map
   * @see javax.servlet.ServletRequestWrapper#getParameterMap()
   */
  public Map<String, String[]> getParameterMap() {
    if (filteredMap == null) {
      Set<String> set = renderParameters.keySet();
      filteredMap = new HashMap<String, String[]>();
      for (String key : set) {
        String[] values = renderParameters.get(key);
        // if (values instanceof String) {
        // String[] a = { (String) values };
        // values = a;
        // }
        if (!key.startsWith(Constants.PARAMETER_ENCODER))
          filteredMap.put(key, values);
      }
    }
    return Collections.unmodifiableMap(filteredMap);
  }

  /**
   * Overridden method.
   *
   * @return private parameter map
   * @see javax.portlet.PortletRequest#getPrivateParameterMap()
   */
  public final Map<String, String[]> getPrivateParameterMap() {
    // only RenderInput has public params
    if (!(getInput() instanceof RenderInput))
      return getParameterMap();
    List<String> pubNames = ((RenderInput) getInput()).getPublicParamNames();
    if (pubNames == null)
      return getParameterMap();
    HashMap<String, String[]> privateMap = new HashMap<String, String[]>();
    Map<String, String[]> allMap = getParameterMap();
    Iterator<String> names = allMap.keySet().iterator();
    while (names.hasNext()) {
      String name = names.next();
      if (!pubNames.contains(name))
        privateMap.put(name, allMap.get(name));
    }
    return Collections.unmodifiableMap(privateMap);
  }

  /**
   * Overridden method.
   *
   * @return public parameter map
   * @see javax.portlet.PortletRequest#getPublicParameterMap()
   */
  public final Map<String, String[]> getPublicParameterMap() {
    // only RenderInput has public params
    if (!(getInput() instanceof RenderInput))
      return Collections.unmodifiableMap(new HashMap<String, String[]>());
    List<String> pubNames = ((RenderInput) getInput()).getPublicParamNames();
    if (pubNames == null)
      return Collections.unmodifiableMap(new HashMap<String, String[]>());
    HashMap<String, String[]> publicMap = new HashMap<String, String[]>();
    Map<String, String[]> allMap = getParameterMap();
    Iterator<String> names = allMap.keySet().iterator();
    while (names.hasNext()) {
      String name = names.next();
      if (pubNames.contains(name))
        publicMap.put(name, allMap.get(name));
    }
    return Collections.unmodifiableMap(publicMap);
  }

}
