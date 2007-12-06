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
 * Created by The eXo Platform SAS Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: Jul 27, 2003 Time: 9:21:41 PM
 */
public class Preference implements Serializable {
  private String       name;

  private List<String> values   = new ArrayList<String>();

  private boolean      readOnly = false;

  // portlet api 2.0
  private String       id;

  public Preference() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue(String defaultValue) {
    if (values.size() > 0) {
      return values.get(0);
    }
    return defaultValue;
  }

  public String[] getValues(String key,
                            String[] def) {
    int size = values.size();
    if (size == 0)
      return def;
    return values.toArray(new String[size]);
  }

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  public void addValue(String value) {
    values.add(value);
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  public void setReadOnly(String readOnly) {
    this.readOnly = "true".equals(readOnly);
  }

  public void clear() {
    values.clear();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String value) {
    this.id = value;
  }
}
