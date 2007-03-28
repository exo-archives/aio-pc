/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.pci.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Jul 11, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Supports.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class Supports {
	private String	mimeType;
	private List		portletMode;
	
  public Supports() {
  	portletMode = new ArrayList() ;
  }
  
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public List getPortletMode() {
		return portletMode;
	}

	public void setPortletMode(List portletMode) {
		this.portletMode = portletMode;
	}
  
  public void addPortletMode(String mode) {
  	this.portletMode.add(mode) ;
  }
}