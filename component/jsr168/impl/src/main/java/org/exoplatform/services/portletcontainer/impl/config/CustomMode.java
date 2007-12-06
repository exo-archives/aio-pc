/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.config;

import java.util.* ;
/**
 * Jul 7, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: CustomMode.java,v 1.1 2004/07/08 19:11:45 tuan08 Exp $
 */
public class CustomMode {
	private String name ;
	private List description ;
	
	public List getDescription() {
		return description;
	}
    
	public void setDescrition(List descrition) {
		this.description = descrition;
	}
   
  public void addDescription(Description desc ) {
    if(description == null) description = new ArrayList();
  	description.add(desc) ;
  }
  
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}