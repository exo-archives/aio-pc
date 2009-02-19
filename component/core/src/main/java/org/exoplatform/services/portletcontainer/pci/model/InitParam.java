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

/**
 * Jul 11, 2004 .
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: InitParam.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class InitParam {

  /**
   * Descriptions.
   */
  private List<Description> description;

  /**
   * Name.
   */
  private String name;

  /**
   * Value.
   */
  private String value;

  // portlet api 2.0

  /**
   * Id.
   */
  private String id;

  /**
   * Simple constructor.
   */
  public InitParam() {
    description = new ArrayList<Description>();
  }

  /**
   * @return descriptions
   */
  public final List<Description> getDescription() {
    return description;
  }

  /**
   * @param description descriptions
   */
  public final void setDescription(final List<Description> description) {
    this.description = description;
  }

  /**
   * @param desc description
   */
  public final void addDescription(final Description desc) {
    this.description.add(desc);
  }

  /**
   * @return name
   */
  public final String getName() {
    return name;
  }

  /**
   * @param name name
   */
  public final void setName(final String name) {
    this.name = name;
  }

  /**
   * @return value
   */
  public final String getValue() {
    return value;
  }

  /**
   * @param value value
   */
  public final void setValue(final String value) {
    this.value = value;
  }

  /**
   * @return id
   */
  public final String getId() {
    return this.id;
  }

  /**
   * @param id id
   */
  public final void setId(final String id) {
    this.id = id;
  }
}
