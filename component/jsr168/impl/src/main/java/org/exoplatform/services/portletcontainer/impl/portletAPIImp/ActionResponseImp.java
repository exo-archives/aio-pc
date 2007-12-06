/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 2:44:18 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp;


import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.impl.PortletContainerConf;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.Supports;


public class ActionResponseImp extends PortletResponseImp
    implements ActionResponse {

  private String location;
  private boolean sendRedirectAlreadyOccured;
  private boolean redirectionPossible;
  private Input input;  
  private Portlet portletDatas;

  public ActionResponseImp(ExoContainer cont, HttpServletResponse httpServletResponse) {
    super(cont, httpServletResponse);
  }
  
  public void fillActionResponse(Input input, Portlet portletDatas){
    this.portletDatas = portletDatas;
    this.input = input;
    this.redirectionPossible = true;
    this.sendRedirectAlreadyOccured = false;
  }
  
  public void emptyActionResponse() {        
    this.redirectionPossible = false;
    this.sendRedirectAlreadyOccured = false;
  }  

  public void setWindowState(WindowState windowState) throws WindowStateException {  
    if(sendRedirectAlreadyOccured)
      throw new IllegalStateException("sendRedirect was already called");                         
    if(windowState == null){
      throw new WindowStateException("The portlet mode is null", windowState); 
    }               
    if (windowState == WindowState.NORMAL || windowState == WindowState.MINIMIZED ||
        windowState == WindowState.MAXIMIZED) {      
      ((ActionOutput) super.getOutput()).setNextState(windowState);
      redirectionPossible = false;
      return;
    }
    ExoContainer manager = cont;
    Enumeration e = 
      ((PortletContainerConf)manager.getComponentInstanceOfType(PortletContainerConf.class)).
                                     getSupportedWindowStates();
    while (e.hasMoreElements()) {
      WindowState state = (WindowState) e.nextElement();
      if (state.toString().equals(windowState.toString())) {
        ((ActionOutput) super.getOutput()).setNextState(windowState);
        redirectionPossible = false;
        return;
      }
    }
    throw new WindowStateException("The window state " + windowState.toString() + " is not supported by the portlet container", windowState);           
  }

  public void setPortletMode(PortletMode portletMode) throws PortletModeException {        
    if(sendRedirectAlreadyOccured)
      throw new IllegalStateException("sendRedirect was already called");                
    if(portletMode == null)
      throw new PortletModeException("The portlet mode is null", portletMode);
    if (portletMode == PortletMode.VIEW) {
      ((ActionOutput) super.getOutput()).setNextMode(portletMode);
      redirectionPossible = false;
      return;
    }
    List l = portletDatas.getSupports();
    for (Iterator iterator = l.iterator(); iterator.hasNext();) {
      Supports supports = (Supports) iterator.next();      
      if (input.getMarkup().equals(supports.getMimeType())) {
        List modeList = supports.getPortletMode();
        for (Iterator iterator1 = modeList.iterator(); iterator1.hasNext();) {
          String modeString = (String) iterator1.next() ;
          modeString = modeString.toLowerCase() ;
          if (modeString != null && modeString.equals(portletMode.toString())) {
            ((ActionOutput) super.getOutput()).setNextMode(portletMode);
            redirectionPossible = false;
            return;
          }
        }        
      }
    }    
    throw new PortletModeException("The mode " + portletMode.toString() + " is not supported by that portlet",
                                   portletMode);                            
  }

  public void setRenderParameters(Map map) {
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
    if(sendRedirectAlreadyOccured)
      throw new IllegalStateException("sendRedirect was already called");    
    redirectionPossible = false;
    ((ActionOutput) super.getOutput()).setRenderParameters(map);
  }

  public void setRenderParameter(String s, String s1) {
    if(s == null) throw new IllegalArgumentException("the key given is null");
    if(s1 == null) throw new IllegalArgumentException("the value given is null");    
    if(sendRedirectAlreadyOccured)
      throw new IllegalStateException("sendRedirect was already called");    
    redirectionPossible = false;
    ((ActionOutput) super.getOutput()).setRenderParameter(s, s1);
  }

  public void setRenderParameter(String s, String[] strings) {
    if(s == null)
      throw new IllegalArgumentException("the key given is null");
    if(strings == null)
      throw new IllegalArgumentException("the value given is null");    
    if(sendRedirectAlreadyOccured)
      throw new IllegalStateException("sendRedirect was already called");    
    redirectionPossible = false;
    ((ActionOutput) super.getOutput()).setRenderParameters(s, strings);
  }
  
  public void sendRedirect(String location)
                    throws IOException,
                           IllegalArgumentException,
                           IllegalStateException{
                             
    if(!redirectionPossible)
      throw new IllegalStateException(" The sendRedirect method can not be invoked " +
      "after any of the following methods of the ActionResponse interface has " +
      "been called: setPortletMode, setWindowState, setRenderParameter, " +
                                      "setRenderParameters");       
    if(location.startsWith("/") || location.startsWith("http://") || location.startsWith("https://")){
      this.sendRedirectAlreadyOccured = true; 
      this.location = location;                                      
    } else {
      throw new IllegalArgumentException ("a relative or incorrect path URL is given");
    }                                                       
  }  
  
  public String getLocation() {
    return location;
  }

  public boolean isSendRedirectAlreadyOccured() {
    return sendRedirectAlreadyOccured;
  }
}
