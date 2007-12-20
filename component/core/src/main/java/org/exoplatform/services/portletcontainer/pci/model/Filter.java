/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.pci.model;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.Constants;

/**
 * Jul 11, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Filter.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class Filter {

  public static final int ALL = 0;
  public static final int ACTION = 1;
  public static final int EVENT = 2;
  public static final int RENDER = 3;
  public static final int RESOURCE = 4;

	private String	filterName;
	private String	filterClass;
	private List<InitParam>		initParam;
  private List<Integer>    lifecycle;
  private List<Description> description;
  private List<DisplayName>  displayName;

  public void addDescription(Description desc) {
    if (description == null)
      description = new ArrayList<Description>();
    this.description.add(desc);
  }

  public void addDisplayName(DisplayName name) {
    if (this.displayName == null)
      displayName = new ArrayList<DisplayName>();
    this.displayName.add(name);
  }

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

	public List<InitParam> getInitParam() {
    if (initParam == null)
      return Constants.EMPTY_LIST;
		return initParam;
	}

	public void setInitParam(List<InitParam> initParam) {
		this.initParam = initParam;
	}

  public void addInitParam(InitParam param) {
    if (initParam == null)
      initParam = new ArrayList<InitParam>();
  	this.initParam.add(param);
  }

  public List getLifecycle() {
    if (lifecycle == null)
      return Constants.EMPTY_LIST;
    return lifecycle;
  }

  public void setLifecycle(List<Integer> lifecycle) {
    this.lifecycle = lifecycle;
  }

  public void addLifecycle(Integer num) {
    if (lifecycle == null)
      lifecycle = new ArrayList<Integer>();
    this.lifecycle.add(num);
  }

}