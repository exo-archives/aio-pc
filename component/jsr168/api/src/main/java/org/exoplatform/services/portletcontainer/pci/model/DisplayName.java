/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.pci.model;

/**
 * Jul 7, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: DisplayName.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class DisplayName {
	private String lang ;
	private String displayName ;
	
	
	public String getDisplayName() {
		return displayName;
	}
    
	public void setDisplayName(String name) {
		this.displayName = name;
	}
    
	public String getLang() {
		return lang;
	}
    
	public void setLang(String lang) {
		this.lang = lang;
	}
}
