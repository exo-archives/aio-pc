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
 * @version: $Id: Filter.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class Filter {
	private String	filterName;
	private String	filterClass;
	private List		initParam;

	public String getFilterClass() {
		return filterClass;
	}

	public void setFilterClass(String filterClass) {
		this.filterClass = filterClass;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public List getInitParam() {
    if(initParam == null) return Constants.EMPTY_LIST ;  
		return initParam;
	}

	public void setInitParam(List initParam) {
		this.initParam = initParam;
	}
  
  public void addInitParam(InitParam param) {
    if(initParam == null) initParam = new ArrayList() ;
  	this.initParam.add(param) ;
  }
}