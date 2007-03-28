/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.pci.model;

import java.util.*;

/**
 * Jul 11, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortletApp.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class PortletApp {
	private List portlet ;
  private String version ;
  private List customWindowState ;
  private List customPortletMode ;
  private List securityConstraint ;
  private List userAttribute ;
  private String id ;
  
  public PortletApp() {
  	portlet = new ArrayList() ;
    customWindowState = new ArrayList() ;
    customPortletMode = new ArrayList() ;
    securityConstraint = new ArrayList() ;
    userAttribute = new ArrayList() ;
  }
  
  public List getPortlet() { return this.portlet ; }
  public void addPortlet(Portlet p) { this.portlet.add(p) ; }
  
  public String getVersion() { return this.version ; }
  public void setVersion(String value) { this.version = value ; }


  public List getCustomWindowState() { return this.customWindowState ; }
  public void addCustomWindowState(CustomWindowState value) { 
    this.customWindowState.add(value) ; 
  }

  public List getCustomPortletMode() { return this.customPortletMode ;}
  public void addCustomPortletMode(CustomPortletMode mode) {
    this.customPortletMode.add(mode) ;
  }

  public List getSecurityConstraint() { return this.securityConstraint ; }
  public void addSecurityConstraint(SecurityConstraint sc) {
  	this.securityConstraint.add(sc) ;
  }
  
  public List getUserAttribute() { return userAttribute ; }
  public void addUserAttribute(UserAttribute att) {
  	this.userAttribute.add(att) ;
  }

  public String getId() { return this.id ; }

  public void setId(String value) { this.id = value ; } 
}