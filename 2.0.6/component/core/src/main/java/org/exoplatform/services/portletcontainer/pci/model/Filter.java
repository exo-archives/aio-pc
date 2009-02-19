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
 * Jul 11, 2004 .
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: Filter.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class Filter {

  /**
   * All lifecycles.
   */
  public static final int ALL = 0;

  /**
   * Action lifecycle.
   */
  public static final int ACTION = 1;

  /**
   * Event lifecycle.
   */
  public static final int EVENT = 2;

  /**
   * Render lifecycle.
   */
  public static final int RENDER = 3;

  /**
   * Resource lifecycle.
   */
  public static final int RESOURCE = 4;

  /**
   * Filter name.
   */
  private String filterName;

  /**
   * Filter class.
   */
  private String filterClass;

  /**
   * Init params.
   */
  private List<InitParam> initParam;

  /**
   * Lifecycles.
   */
  private List<Integer> lifecycle;

  /**
   * Descriptions.
   */
  private List<Description> description;

  /**
   * Display names.
   */
  private List<DisplayName> displayName;

  /**
   * @param desc description
   */
  public final void addDescription(final Description desc) {
    if (description == null)
      description = new ArrayList<Description>();
    this.description.add(desc);
  }

  /**
   * @param name display name
   */
  public final void addDisplayName(final DisplayName name) {
    if (this.displayName == null)
      displayName = new ArrayList<DisplayName>();
    this.displayName.add(name);
  }

  /**
   * @return filter class
   */
  public final String getFilterClass() {
    return filterClass;
  }

  /**
   * @param filterClass filter class
   */
  public final void setFilterClass(final String filterClass) {
    this.filterClass = filterClass;
  }

  /**
   * @return filter name
   */
  public final String getFilterName() {
    return filterName;
  }

  /**
   * @param filterName filter name
   */
  public final void setFilterName(final String filterName) {
    this.filterName = filterName;
  }

  /**
   * @return init params
   */
  public final List<InitParam> getInitParam() {
    if (initParam == null)
      return Constants.EMPTY_LIST;
    return initParam;
  }

  /**
   * @param initParam init params
   */
  public final void setInitParam(final List<InitParam> initParam) {
    this.initParam = initParam;
  }

  /**
   * @param param init param
   */
  public final void addInitParam(final InitParam param) {
    if (initParam == null)
      initParam = new ArrayList<InitParam>();
    this.initParam.add(param);
  }

  /**
   * @return lifecycle list
   */
  public final List<Integer> getLifecycle() {
    if (lifecycle == null)
      return Constants.EMPTY_LIST;
    return lifecycle;
  }

  /**
   * @param lifecycle lifecycle list
   */
  public final void setLifecycle(final List<Integer> lifecycle) {
    this.lifecycle = lifecycle;
  }

  /**
   * @param num lifecycle mark
   */
  public final void addLifecycle(final Integer num) {
    if (lifecycle == null)
      lifecycle = new ArrayList<Integer>();
    this.lifecycle.add(num);
  }

}
