/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 20, 2003
 * Time: 2:00:32 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.tags;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.JspException;
import javax.portlet.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class XURLTag extends BodyTagSupport{

  protected WindowState windowState;
  protected PortletMode portletMode;
  protected String var;
  protected boolean secure;
  private Map parameters = new HashMap();

  public void addParameter(String key, String value){
    parameters.put(key, new String[]{value});    
  }

  public void setWindowState(String windowState) {
    if(WindowState.MAXIMIZED.toString().equals(windowState))
      this.windowState = WindowState.MAXIMIZED;
    else if (WindowState.MINIMIZED.toString().equals(windowState))
      this.windowState = WindowState.MINIMIZED;
    else if (WindowState.NORMAL.toString().equals(windowState))
      this.windowState = WindowState.NORMAL;
    else
      this.windowState = new WindowState(windowState);  
  }

  public void setPortletMode(String portletMode) {
    if(PortletMode.EDIT.toString().equals(portletMode))
      this.portletMode = PortletMode.EDIT;
    else if(PortletMode.HELP.toString().equals(portletMode))
      this.portletMode = PortletMode.HELP;
    else if(PortletMode.VIEW.toString().equals(portletMode))
      this.portletMode = PortletMode.VIEW;
    else
      this.portletMode =new PortletMode(portletMode);  
  }

  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }

  public void setSecure(String secure) {
    if(secure.equals("true"))
      this.secure = true;
    else
      this.secure = false;
  }

  public int doStartTag() throws JspException {
    parameters = new HashMap();
    return EVAL_BODY_BUFFERED;
  }

  public int doEndTag() throws JspException {
    PortletURL portletURL = getPortletURL();
    portletURL.setParameters(parameters);
    try {
      if(portletMode != null)
        portletURL.setPortletMode(portletMode);        
      portletURL.setSecure(secure);
      if(windowState != null)
        portletURL.setWindowState(windowState);
    } catch (PortletModeException e) {
      throw new JspException(e);
    } catch (WindowStateException e) {
      throw new JspException(e);
    } catch (PortletSecurityException e) {
      throw new JspException(e);
    }

    if(var == null || "".equals(var)){
      try {        
        pageContext.getOut().print(portletURL.toString());
      } catch (IOException e1) {
        throw new JspException(e1);        
      }
    } else {
      pageContext.setAttribute(var, portletURL.toString());
    }
    
    return EVAL_PAGE;
  }

  public abstract PortletURL getPortletURL();
}
