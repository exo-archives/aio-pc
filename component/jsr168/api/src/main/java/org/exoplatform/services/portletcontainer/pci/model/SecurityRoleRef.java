/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.pci.model;

/**
 * Jul 11, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: SecurityRoleRef.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class SecurityRoleRef {
	private String	roleName;
	private String	roleLink;

	public String getRoleLink() {
		return roleLink;
	}

	public void setRoleLink(String roleLink) {
		this.roleLink = roleLink;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}