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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by The eXo Platform SAS Author : Mestrallet Benjamin .
 * benjmestrallet@users.sourceforge.net Date: Jul 27, 2003 Time: 9:21:41 PM
 */
public class Preference implements Serializable {

  /**
   * Name.
   */
  private String name;

  /**
   * Values.
   */
  private List<String> values = new ArrayList<String>();

  /**
   * Either readonly.
   */
  private boolean readOnly = false;

  // portlet api 2.0

  /**
   * Id.
   */
  private String id;

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
   * @param defaultValue default value
   * @return value
   */
  public final String getValue(final String defaultValue) {
    if (values.size() > 0)
      return values.get(0);
    return defaultValue;
  }

  /**
   * @param key key
   * @param def default values
   * @return values
   */
  public final String[] getValues(final String key, final String[] def) {
    int size = values.size();
    if (size == 0)
      return def;
    return values.toArray(new String[size]);
  }

  /**
   * @return value list
   */
  public final List<String> getValues() {
    return values;
  }

  /**
   * @param values value list
   */
  public final void setValues(final List<String> values) {
    this.values = values;
  }

  /**
   * @param value value
   */
  public final void addValue(final String value) {
    values.add(value);
  }

  /**
   * @return either readonly
   */
  public final boolean isReadOnly() {
    return readOnly;
  }

  /**
   * @param readOnly either readonly
   */
  public final void setReadOnly(final boolean readOnly) {
    this.readOnly = readOnly;
  }

  /**
   * @param readOnly either readonly
   */
  public final void setReadOnly(final String readOnly) {
    this.readOnly = "true".equals(readOnly);
  }

  /**
   * Clear values.
   */
  public final void clear() {
    values.clear();
  }

  /**
   * @return id
   */
  public final String getId() {
    return this.id;
  }

  /**
   * @param value id
   */
  public final void setId(final String value) {
    this.id = value;
  }
}
