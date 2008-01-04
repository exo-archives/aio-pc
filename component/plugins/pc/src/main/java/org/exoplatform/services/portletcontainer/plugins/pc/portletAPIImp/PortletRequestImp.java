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
 * Created by The eXo Platform SAS
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 27, 2003
 * Time: 8:44:32 PM
 *
 * This implementation acts like a wrapper to the global ServletRequest This
 * object should be pooled, therefore two fill and empty methods are provided.
 */
public abstract class PortletRequestImp extends HttpServletRequestWrapper implements PortletRequest, Map {

  private Log                   log;

  protected ExoContainer        cont;

  protected RequestContext      reqCtx;

  private Vector<String>        paramNames;

  private Map<String, String[]> filteredMap;

  protected boolean             encodingModified;

  protected String              enc;

  private Map<String, String[]> renderParameters;

  public PortletRequestImp(RequestContext reqCtx) {
    super(reqCtx.getHttpServletRequest());
    this.log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
    this.cont = ((PortletContextImpl) reqCtx.getPortletContext()).getContainer();
    this.reqCtx = reqCtx;
    this.enc = reqCtx.getHttpServletRequest().getCharacterEncoding();
    this.renderParameters = reqCtx.getInput().getRenderParameters();
  }

  public boolean isWindowStateAllowed(WindowState windowState) {
    Enumeration<WindowState> e = reqCtx.getPortalContext().getSupportedWindowStates();
    while (e.hasMoreElements()) {
      WindowState supportedWindowState = e.nextElement();
      if (supportedWindowState.equals(windowState))
        return true;
    }
    return false;
  }

  public boolean isPortletModeAllowed(PortletMode portletMode) {
    Enumeration<PortletMode> e = reqCtx.getPortalContext().getSupportedPortletModes();
    while (e.hasMoreElements()) {
      PortletMode supportedPortletMode = e.nextElement();
      if (supportedPortletMode.toString().toLowerCase().equals(portletMode.toString().toLowerCase()))
        return true;
    }
    return false;
  }

  public PortletConfig getPortletConfig() {
    ExoContainer manager = cont;
    String portletAppName = reqCtx.getPortletWindowInternal().getWindowID().getPortletApplicationName();
    PortletApplicationProxy proxy = (PortletApplicationProxy) manager.getComponentInstance(portletAppName);
    return proxy.getPortletConfig(reqCtx.getPortletWindowInternal().getWindowID().getPortletName());
  }

  public Input getInput() {
    return reqCtx.getInput();
  }

  public PortletMode getPortletMode() {
    return reqCtx.getInput().getPortletMode();
  }

  public WindowState getWindowState() {
    return reqCtx.getInput().getWindowState();
  }

  public PortletPreferences getPreferences() {
    return reqCtx.getPortletWindowInternal().getPreferences();
  }

  public PortletSession getPortletSession() {
    return getPortletSession(true);
  }

  public PortletSession getPortletSession(boolean create) {
    if (create) {
      if (reqCtx.getSession().isSessionValid()) {
        return reqCtx.getSession();
      }
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

  public boolean isRequestedSessionIdValid() {
    try {
      // ((HttpServletRequest) super.getRequest()).getSession()
      // .getLastAccessedTime();
      return ((HttpServletRequest) super.getRequest()).isRequestedSessionIdValid();
    } catch (IllegalStateException e) {
      log.error("IllegalStateExcetion sent in PortletRequestImp isRequestedSessionIdValid()", e);
      return false;
    }
  }

  public String getProperty(String s) {
    String header = ((HttpServletRequest) super.getRequest()).getHeader(s);
    if (header != null)
      return header;
    return reqCtx.getPortalContext().getProperty(s);
  }

  public Enumeration<String> getProperties(String s) {
    Enumeration<String> header = ((HttpServletRequest) super.getRequest()).getHeaders(s);
    return header;
  }

  public Enumeration<String> getPropertyNames() {
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

  public PortalContext getPortalContext() {
    return reqCtx.getPortalContext();
  }

  public String getResponseContentType() {
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

  public Enumeration<String> getResponseContentTypes() {
    List<String> result = new ArrayList<String>();
    result.add(getResponseContentType());
    String markup = reqCtx.getInput().getMarkup();
    String inputPortletMode = reqCtx.getInput().getPortletMode().toString();
    for (Iterator iter = reqCtx.getSupportedContents().iterator(); iter.hasNext();) {
      String supportedContent = (String) iter.next();
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

  public boolean isUserInRole(String role) {
    List l = reqCtx.getPortletDatas().getSecurityRoleRef();
    for (Iterator iterator = l.iterator(); iterator.hasNext();) {
      SecurityRoleRef securityRoleRef = (SecurityRoleRef) iterator.next();
      if (securityRoleRef.getRoleName().equals(role)) {
        String roleLink = securityRoleRef.getRoleLink();
        if (roleLink == null || "".equals(roleLink)) {
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

  private boolean isRoleDefinedInWebXML(String role) {
    for (Iterator iter = reqCtx.getRoles().iterator(); iter.hasNext();) {
      String roleDefined = (String) iter.next();
      if (roleDefined.equals(role))
        return true;
    }
    return false;
  }

  public Portlet getPortletDatas() {
    return reqCtx.getPortletDatas();
  }

  public PortletWindowInternal getPortletWindowInternal() {
    return reqCtx.getPortletWindowInternal();
  }

  public boolean needsSecurityContraints(String portletName) {
    for (Iterator iterator = reqCtx.getSecurityContraints().iterator(); iterator.hasNext();) {
      SecurityConstraint securityConstraint = (SecurityConstraint) iterator.next();
      List l = securityConstraint.getPortletCollection().getPortletName();
      for (Iterator iterator2 = l.iterator(); iterator2.hasNext();) {
        String portletN = (String) iterator2.next();
        if (portletN.equals(portletName))
          return true;
      }
    }
    return false;
  }

  public String getAuthType() {
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

  public Object getAttribute(String name) {
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null");
    }
    return super.getAttribute(name);
  }

  public void setAttribute(String name,
                           Object value) {
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null");
    }
    // when the value is null, should have the same effect as removeAttribute
    // (Spec)
    if (value == null) {
      super.removeAttribute(name);
    } else {
      super.setAttribute(name, value);
    }
  }

  public void removeAttribute(String name) {
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null");
    }
    super.removeAttribute(name);
  }

  public String getContextPath() {
    return "/" + this.reqCtx.getPortletContext().getPortletContextName();
  }

  // Bridge methods

  public int size() {
    int n = 0;
    Enumeration keys = getAttributeNames();
    while (keys.hasMoreElements()) {
      // String key = (String) keys.nextElement();
      n++;
    }
    return n;
  }

  public boolean isEmpty() {
    return !getAttributeNames().hasMoreElements();
  }

  public boolean containsKey(Object key) {
    return (getAttribute((String) key) != null);
  }

  public boolean containsValue(Object value) {
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

  public Object get(Object key) {
    return getAttribute((String) key);
  }

  public Object put(Object key,
                    Object value) {
    Object result = null;
    if (containsKey(key))
      result = getAttribute((String) key);
    setAttribute((String) key, value);
    return result;
  }

  public Object remove(Object key) {
    Object result = null;
    if (containsKey(key))
      result = getAttribute((String) key);
    removeAttribute((String) key);
    return result;
  }

  public void putAll(Map t) {
    for (Iterator i = t.keySet().iterator(); i.hasNext();) {
      String key = (String) i.next();
      Object value = t.get(key);
      put(key, value);
    }
  }

  public void clear() {
    throw new UnsupportedOperationException();
  }

  public Set keySet() {
    throw new UnsupportedOperationException();
  }

  public Collection values() {
    throw new UnsupportedOperationException();
  }

  public Set entrySet() {
    throw new UnsupportedOperationException();
  }

  public Locale getLocale() {
    List<Locale> locales = reqCtx.getInput().getLocales();
    if (locales != null && !locales.isEmpty()) {
      return locales.iterator().next();
    }
    return super.getLocale();
  }

  public Enumeration<Locale> getLocales() {
    List<Locale> locales = reqCtx.getInput().getLocales();
    if (locales != null) {
      return Collections.enumeration(locales);
    }
    return super.getLocales();
  }

  // adds for jsr-286
  public String getWindowID() {
    return reqCtx.getInput().getInternalWindowID().getUniqueID();
  }

  public String getParameter(String param) {
    if (param == null || param.startsWith(Constants.PARAMETER_ENCODER))
      throw new IllegalArgumentException("parameter must not be null");
    Object obj = renderParameters.get(param);
    if (obj instanceof String[]) {
      String[] tmp = (String[]) obj;
      return tmp[0];
    }
    return (String) obj;
  }

  public Enumeration<String> getParameterNames() {
    if (paramNames == null) {
      Set<String> set = renderParameters.keySet();
      paramNames = new Vector<String>();
      for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
        String key = iterator.next();
        if (!key.startsWith(Constants.PARAMETER_ENCODER))
          paramNames.add(key);
      }
    }
    return paramNames.elements();
  }

  public String[] getParameterValues(String s) {
    if (s == null || s.startsWith(Constants.PARAMETER_ENCODER))
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

  public Map<String, String[]> getParameterMap() {
    if (filteredMap == null) {
      Set<String> set = renderParameters.keySet();
      filteredMap = new HashMap<String, String[]>();
      for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
        String key = iterator.next();
        String[] values = renderParameters.get(key);
        // if (values instanceof String) {
        // String[] a = { (String) values };
        // values = a;
        // }
        if (!key.startsWith(Constants.PARAMETER_ENCODER)) {
          filteredMap.put(key, values);
        }
      }
    }
    return Collections.unmodifiableMap(filteredMap);
  }

  public Map<String, String[]> getPrivateParameterMap() {
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
      if (!pubNames.contains(name)) {
        privateMap.put(name, allMap.get(name));
      }
    }
    return Collections.unmodifiableMap(privateMap);
  }

  public Map<String, String[]> getPublicParameterMap() {
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
      if (pubNames.contains(name)) {
        publicMap.put(name, allMap.get(name));
      }
    }
    return Collections.unmodifiableMap(publicMap);
  }

}
