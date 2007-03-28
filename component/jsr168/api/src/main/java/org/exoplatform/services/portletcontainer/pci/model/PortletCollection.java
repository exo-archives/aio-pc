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
 * @version: $Id: PortletCollection.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class PortletCollection {
	private List	portletName	= new ArrayList();

	public List getPortletName() {
		return portletName;
	}

	public void setPortletName(List portletName) {
		this.portletName = portletName;
	}

	public void addPortletName(String name) {
		this.portletName.add(name);
	}
}