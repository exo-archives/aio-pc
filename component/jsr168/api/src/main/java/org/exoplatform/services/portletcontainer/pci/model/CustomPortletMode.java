/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.pci.model;

import java.util.*;
import org.exoplatform.Constants;

/**
 * Jul 11, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: CustomPortletMode.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class CustomPortletMode {
	private List description ;
  private String portletMode ;
  
	public List getDescription() {
    if(description == null) return Constants.EMPTY_LIST ;  
		return description;
	}
	public void setDescription(List description) {
		this.description = description;
	}
  
  public void addDescription(Description desc) {
    if(description == null) description = new ArrayList() ;
    this.description.add(desc) ; 
  }
  
	public String getPortletMode() {  
		return portletMode;
	}
  
	public void setPortletMode(String portletMode) {
		this.portletMode = portletMode;
	}
}