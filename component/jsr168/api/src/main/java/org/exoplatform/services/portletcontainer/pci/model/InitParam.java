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
 * @version: $Id: InitParam.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class InitParam {
	private List		description;
	private String	name;
	private String	value;

	public InitParam() {
		description = new ArrayList();
	}

	public List getDescription() {
		return description;
	}

	public void setDescription(List description) {
		this.description = description;
	}

	public void addDescription(Description desc) {
		this.description.add(desc);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}