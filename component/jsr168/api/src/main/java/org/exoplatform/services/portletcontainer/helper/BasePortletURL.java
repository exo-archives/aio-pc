/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.  
 * Created on 12 janv. 2004
 */
package org.exoplatform.services.portletcontainer.helper;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import org.exoplatform.services.portletcontainer.pci.model.*;


/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public abstract class BasePortletURL implements PortletURL {
    
  private List supports;
  private String markup;
  protected Enumeration supportedWindowState;
  protected List customWindowStates;
  protected WindowState requiredWindowState;
  protected PortletMode requiredPortletMode;
  protected Map parameters = new HashMap();
  protected boolean isSecure;
  protected boolean setSecureCalled;
  protected String type;

  protected boolean isCurrentlySecured;  

  public BasePortletURL(String type, String markup, 
                        List supports, 
                        boolean isCurrentlySecured, 
                        List customWindowStates,
                        Enumeration supportedWindowState) {
    this.type = type;
    this.markup = markup;    
    this.supports = supports;
    this.isCurrentlySecured = isCurrentlySecured;
    this.customWindowStates = customWindowStates;
    this.supportedWindowState = supportedWindowState;
  }

  public void setWindowState(WindowState windowState)
          throws WindowStateException {
    if(windowState == null){
      throw new WindowStateException("The portlet mode is null", windowState); 
    }            
    if (windowState == WindowState.NORMAL || windowState == WindowState.MINIMIZED ||
        windowState == WindowState.MAXIMIZED) {
      requiredWindowState = windowState;
      return;
    }            
        
    while (supportedWindowState.hasMoreElements()) {
      WindowState state = (WindowState) supportedWindowState.nextElement();      
      if (state.toString().equals(windowState.toString())) {
        for (Iterator iter = customWindowStates.iterator(); iter.hasNext();) {
          CustomWindowState customState = (CustomWindowState) iter.next();
          if(customState.getWindowState().equals(windowState.toString())){
            requiredWindowState = windowState;
            return;
          }
        }
      }
    }
    throw new WindowStateException("The window state " + windowState.toString() + " is not supported by the portlet container",
            windowState);
  }

  public void setPortletMode(PortletMode portletMode) throws PortletModeException {

    if(portletMode == null)
      throw new PortletModeException("The portlet mode is null", portletMode);

    if (portletMode == PortletMode.VIEW) {
      requiredPortletMode = portletMode;
      return;
    }
    
    boolean supported = false;
    for (Iterator iterator = supports.iterator(); iterator.hasNext();) {
      Supports sp = (Supports) iterator.next();
      if (markup.equals(sp.getMimeType())) {
        List modeList = sp.getPortletMode();
        for (Iterator iterator1 = modeList.iterator(); iterator1.hasNext();) {
          String modeString = (String)iterator1.next();
          if (modeString != null && modeString.equalsIgnoreCase(portletMode.toString())) {
            supported = true;
            break;
          }
        }
        break;
      }
    }
    if (!supported)
      throw new PortletModeException("The mode " + portletMode.toString() + " is not supported by that portlet",
              portletMode);

    requiredPortletMode = portletMode;
  }

  public void setParameter(String s, String s1) {
    if(s == null)
      throw new IllegalArgumentException("the key given is null");
    if(s1 == null)
      throw new IllegalArgumentException("the value given is null");        
    parameters.put(s, s1);
  }

  public void setParameter(String s, String[] strings) {
    if(s == null)  throw new IllegalArgumentException("the key given is null");
    if(strings == null) throw new IllegalArgumentException("the value given is null");    
    parameters.put(s, strings);
  }

  public void setParameters(Map map) {
    if(map == null)
      throw new IllegalArgumentException("the map given is null");
    if(map.containsKey(null))
      throw new IllegalArgumentException("the map given contains a null key");
    Set keys = map.keySet();
    for (Iterator iter = keys.iterator(); iter.hasNext();) {
      if(! (iter.next() instanceof String ))
        throw new IllegalArgumentException("the map contains a non String key");
    }
    Collection values = map.values();
    for (Iterator iter = values.iterator(); iter.hasNext();) {
      if(! (iter.next() instanceof String[] ))
        throw new IllegalArgumentException("the map contains a non String[] value");
    }           
    parameters = map;
  }

  public Object getParameter(String key) {
    return parameters.get(key);
  }

  public void setSecure(boolean b)
          throws PortletSecurityException {
    isSecure = b;
    setSecureCalled = true;
  }

  public abstract String toString();
}