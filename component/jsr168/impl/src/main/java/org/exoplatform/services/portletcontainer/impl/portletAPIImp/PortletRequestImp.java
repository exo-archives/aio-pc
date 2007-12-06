/**
 * Copyright 2001-2003 The eXo Platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 27, 2003
 * Time: 8:44:32 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp;


import javax.portlet.PortletRequest ;
import javax.portlet.PortletContext ;
import javax.portlet.PortalContext  ;
import javax.portlet.PortletPreferences ;
import javax.portlet.PortletMode ;
import javax.portlet.WindowState ;
import javax.portlet.PortletSession ;
import javax.portlet.PortletConfig ;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import java.util.*;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.portletcontainer.ExoPortletRequest;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.impl.PortletApplicationProxy;
import org.exoplatform.services.portletcontainer.impl.PortletContainerConf;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.SharedSessionWrapper;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool.EmptyRequest;
import org.exoplatform.services.portletcontainer.pci.*;
import org.exoplatform.services.portletcontainer.pci.model.*;


/**
 * This implementation acts like a wrapper to the global ServletRequest
 *
 * This object should be pooled, therefore two fill and empty methods
 * are provided.
 *
 */
public class PortletRequestImp extends HttpServletRequestWrapper
    implements PortletRequest, ExoPortletRequest, Map {

  private Collection roles_;
  private PortletSessionImp session_;
  private Portlet portletDatas_;
  protected Input input_;
  private PortletWindowInternal portletWindowInternal_;
  private PortalContext portalContext_;
  private PortletContext portletContext_;
  private List securityContraints_;
  private List userAttributes_;
  private List customPortletModes_;
  protected List customWindowStates_;
  private Collection supportedContents_;
  private Log log_;
  protected ExoContainer cont;

  /**
   * This constructor is called by the pool so it gives a null
   * reference of the httpServletRequest to wrap.
   *
   * When the object is retrieve from the pool or released
   * it should call the fillPortletRequest() and emptyPortletRequest()
   * methods
   *
   * @param httpServletRequest
   */
  public PortletRequestImp(ExoContainer cont, HttpServletRequest httpServletRequest) {
    super(httpServletRequest);
    this.log_ = LogUtil.getLog("org.exoplatform.services.portletcontainer");
    this.cont = cont;
  }

  public void fillPortletRequest(HttpServletRequest httpServletRequest,
                                  PortalContext portalContext,
                                  PortletContext portletContext,
                                  PortletSessionImp session,
                                  Portlet portletDatas, Input input,
                                  PortletWindowInternal portletWindowInternal,
                                  List securityContraints, List userAttributes,
                                  List customPortletModes,
                                  List customWindowStates, Collection roles,
                                  Collection supportedContents) {
    super.setRequest(httpServletRequest);
    this.portalContext_ = portalContext;
    this.portletContext_ = portletContext;
    this.session_ = session;
    this.portletDatas_ = portletDatas;
    this.input_ = input;
    this.portletWindowInternal_ = portletWindowInternal;
    this.securityContraints_ = securityContraints;
    this.userAttributes_ = userAttributes;
    this.customPortletModes_ = customPortletModes;
    this.customWindowStates_ = customWindowStates;
    this.roles_ = roles;
    this.supportedContents_ = supportedContents;
    init();
  }

  public void emptyPortletRequest() {
    super.setRequest(new EmptyRequest());
  }

  public void init() {
  }

  public Input getInput() {
    return input_;
  }

  public boolean isWindowStateAllowed(WindowState windowState) {
    Enumeration e = portalContext_.getSupportedWindowStates();
    while (e.hasMoreElements()) {
      WindowState supportedWindowState = (WindowState) e.nextElement();
      if (supportedWindowState.equals(windowState))
        return true;
    }
    return false;
  }

  public boolean isPortletModeAllowed(PortletMode portletMode) {
    Enumeration e = portalContext_.getSupportedPortletModes();
    while (e.hasMoreElements()) {
      PortletMode supportedPortletMode = (PortletMode) e.nextElement();
      if (supportedPortletMode.toString().toLowerCase().equals(portletMode.toString().toLowerCase()))
        return true;
    }

    return false;
  }

  public PortletConfig getPortletConfig(){
    ExoContainer manager = cont;
    String portletAppName = portletWindowInternal_.getWindowID().getPortletApplicationName() ;
    PortletApplicationProxy proxy =
      (PortletApplicationProxy) manager.getComponentInstance(portletAppName);
    return proxy.getPortletConfig(portletWindowInternal_.getWindowID().getPortletName());
  }

  public PortletMode getPortletMode() {
    return input_.getPortletMode();
  }

  public WindowState getWindowState() {
    return input_.getWindowState();
  }

  public PortletPreferences getPreferences() {
    return portletWindowInternal_.getPreferences();
  }

  public PortletSession getPortletSession() {
    return getPortletSession(true);
  }

  public PortletSession getPortletSession(boolean create) {
    PortletContainerConf conf = (PortletContainerConf)cont.
        getComponentInstanceOfType(PortletContainerConf.class);
    boolean isSharedSessionEnable = conf.isSharedSessionEnable();
    if(create){
      if(session_.isSessionValid()){
        return session_;
      }
      if(isSharedSessionEnable){
        ((SharedSessionWrapper)session_.getSession()).init();
      } else{
        session_.setSession(((HttpServletRequest)super.getRequest()).getSession());
      }
      return session_;
    }
    if(isSharedSessionEnable){
      if(session_.isSessionValid()){
        return session_;
      }
    } else{
      HttpSession tmpSession = ((HttpServletRequest)super.getRequest()).getSession(false);
      if(tmpSession == null)
        return null;
      //needed for Tomcat 5.0.16 (first stable version)
      try{
        tmpSession.getLastAccessedTime();
      } catch (IllegalStateException e){
        log_.error("IllegalStateExcetion sent in PortletRequestImp getPortletSession()", e);
        return null;
      }
      return session_;
    }
    return null;
  }

  public boolean isRequestedSessionIdValid() {
    PortletContainerConf conf =
      (PortletContainerConf) cont.
                             getComponentInstanceOfType(PortletContainerConf.class);
    boolean isSharedSessionEnable = conf.isSharedSessionEnable();
    if (isSharedSessionEnable) {
      return this.session_.isSessionValid();
    }
    //needed for Tomcat 5.0.16 (first stable version)
    if (((HttpServletRequest) super.getRequest()).isRequestedSessionIdValid())
    try {
      ((HttpServletRequest) super.getRequest()).getSession()
          .getLastAccessedTime();
    } catch (IllegalStateException e) {
      log_.error("IllegalStateExcetion sent in PortletRequestImp isRequestedSessionIdValid()",  e);
      return false;
    }
    return ((HttpServletRequest) super.getRequest()).isRequestedSessionIdValid();
  }

  public String getProperty(String s) {
    String header = ((HttpServletRequest)super.getRequest()).getHeader(s);
    if(header != null)
      return header;
    return portalContext_.getProperty(s);
  }

  public Enumeration getProperties(String s) {
    Enumeration header = ((HttpServletRequest)super.getRequest()).getHeaders(s);
    return header;
  }

  public Enumeration getPropertyNames() {
    Enumeration headerNames = ((HttpServletRequest)super.getRequest()).getHeaderNames();
    Enumeration portalPropertyNames = portalContext_.getPropertyNames();
    Collection global = new ArrayList();
    while (portalPropertyNames.hasMoreElements()) {
      String s = (String) portalPropertyNames.nextElement();
      global.add(s);
    }
    while (headerNames.hasMoreElements()) {
      String s = (String) headerNames.nextElement();
      global.add(s);
    }
    return Collections.enumeration(global);
  }

  public PortalContext getPortalContext() {
    return portalContext_;
  }

  public String getResponseContentType() {
    List l = portletDatas_.getSupports();
    String markup = input_.getMarkup() ;
    String inputPortletMode = input_.getPortletMode().toString() ;
    for (int i = 0; i < l.size(); i++) {
      Supports supportsType = (Supports) l.get(i);
      String mimeType = supportsType.getMimeType();
      if (mimeType.equals(markup)) {
        List portletModes = supportsType.getPortletMode();
        for (int modeIdx  = 0; modeIdx < portletModes.size(); modeIdx++) {
          String  portletMode = (String) portletModes.get(modeIdx);
          if(portletMode.equals(inputPortletMode))  return mimeType;
        }
      }
    }
    return "text/html";
  }

  public Enumeration getResponseContentTypes() {
    List c = new ArrayList();
    c.add(getResponseContentType());
    String markup = input_.getMarkup() ;
    String inputPortletMode = input_.getPortletMode().toString() ;
    for (Iterator iter = supportedContents_.iterator(); iter.hasNext();) {
      String supportedContent = (String) iter.next();
      List l = portletDatas_.getSupports();
      for (int i = 0; i < l.size(); i++) {
        Supports supportsType = (Supports) l.get(i);
        String mimeType = supportsType.getMimeType();
        if(supportedContent.equals(mimeType) && !supportedContent.equals(markup)){
          List portletModes = supportsType.getPortletMode();
          for (Iterator iter2 = portletModes.iterator(); iter2.hasNext();) {
            String portletMode = (String) iter2.next();
            if(portletMode.equals(inputPortletMode)){
              c.add(mimeType);
              break;
            }
          }
        }
      }
    }
    return Collections.enumeration(c);
  }

  public boolean isUserInRole(String role){
    List l = portletDatas_.getSecurityRoleRef();
    for (Iterator iterator = l.iterator(); iterator.hasNext();) {
      SecurityRoleRef securityRoleRef = (SecurityRoleRef) iterator.next();
      if(securityRoleRef.getRoleName().equals(role)){
        String roleLink = securityRoleRef.getRoleLink();
        if(roleLink == null || "".equals(roleLink)){
          if(isRoleDefinedInWebXML(role)) return super.isUserInRole(role);
          return false;
        }
        if(isRoleDefinedInWebXML(roleLink)) return super.isUserInRole(roleLink);
        return false;
      }
    }
    return false;
  }

  private boolean isRoleDefinedInWebXML(String role){
    for (Iterator iter = roles_.iterator(); iter.hasNext();) {
      String roleDefined = (String) iter.next();
      if(roleDefined.equals(role))
        return true;
    }
    return false;
  }

  public Portlet getPortletDatas() {
    return portletDatas_;
  }

  public PortletWindowInternal getPortletWindowInternal() {
    return portletWindowInternal_;
  }

  public boolean needsSecurityContraints(String portletName) {
    for (Iterator iterator = securityContraints_.iterator(); iterator.hasNext();) {
      SecurityConstraint securityConstraint = (SecurityConstraint) iterator.next();
      List l = securityConstraint.getPortletCollection().getPortletName();
      for (Iterator iterator2 = l.iterator(); iterator2.hasNext();) {
        String portletN = (String) iterator2.next();
        if(portletN.equals(portletName))  return true;
      }
    }
    return false;
  }

  public String getAuthType(){
    String type = super.getAuthType();
    if(HttpServletRequest.BASIC_AUTH.equals(type))
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
      throw new IllegalArgumentException("The attribute name cannot be null") ;
    }
    return super.getAttribute(name) ;
  }

  public void setAttribute(String name, Object value) {
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null") ;
    }
    //when the value is null, should have the same effect as removeAttribute (Spec)
    if(value == null) {
      super.removeAttribute(name) ;
    } else {
      super.setAttribute(name, value) ;
    }
  }

  public void removeAttribute(String name) {
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null") ;
    }
    super.removeAttribute(name) ;
  }

  public String getContextPath() {
    return "/" +  this.portletContext_.getPortletContextName() ;
  }

  // Bridge methods

  public int size() {
    int n = 0;
    Enumeration keys = getAttributeNames();
    while (keys.hasMoreElements()) {
      //String key = (String) keys.nextElement();
      n++;
    }
    return n;
  }

  public boolean isEmpty() {
    return !getAttributeNames().hasMoreElements();
  }

  public boolean containsKey( Object key ) {
    return (getAttribute( (String) key ) != null);
  }

  public boolean containsValue( Object value ) {
    boolean match = false;
    Enumeration keys = getAttributeNames();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      Object val = getAttribute( key );
      if (value.equals( val )) {
        match = true;
        break;
      }
    }
    return match;
  }

  public Object get( Object key ) {
    return getAttribute( (String) key );
  }

  public Object put( Object key, Object value ) {
    Object result = null;
    if (containsKey( key )) result = getAttribute( (String) key );
    setAttribute( (String) key, value );
    return result;
  }

  public Object remove( Object key ) {
    Object result = null;
    if (containsKey( key )) result = getAttribute( (String) key );
    removeAttribute( (String) key );
    return result;
  }

  public void putAll( Map t ) {
    for (Iterator i = t.keySet().iterator(); i.hasNext();) {
      String key = (String) i.next();
      Object value = t.get( key );
      put( key, value );
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

  public Locale getLocale(){
    List locales = input_.getLocales();
    if(locales != null && !locales.isEmpty()){
      return (Locale) locales.iterator().next();
    }
    return super.getLocale();
  }

  public Enumeration getLocales(){
    List locales = input_.getLocales();
    if(locales != null){
      return Collections.enumeration(locales);
    }
    return super.getLocales();
  }

  public WindowID getWindowID() {
    return input_.getWindowID();
  }
}