/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.pci.model;

/**
 * Jul 11, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: SecurityConstraint.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class SecurityConstraint {
	private String							displayName;
	private PortletCollection		portletCollection;
	private UserDataConstraint	userDataConstraint;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public PortletCollection getPortletCollection() {
		return portletCollection;
	}

	public void setPortletCollection(PortletCollection portletCollection) {
		this.portletCollection = portletCollection;
	}

	public UserDataConstraint getUserDataConstraint() {
		return userDataConstraint;
	}

	public void setUserDataConstraint(UserDataConstraint userDataConstraint) {
		this.userDataConstraint = userDataConstraint;
	}
}