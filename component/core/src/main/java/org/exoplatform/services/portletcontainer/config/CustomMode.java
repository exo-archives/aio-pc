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
package org.exoplatform.services.portletcontainer.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Jul 7, 2004
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: CustomMode.java,v 1.1 2004/07/08 19:11:45 tuan08 Exp $
 */
public class CustomMode {
  private String            name;

  private List<Description> description;

  public List<Description> getDescription() {
    return description;
  }

  public void setDescrition(List<Description> descrition) {
    this.description = descrition;
  }

  public void addDescription(Description desc) {
    if (description == null)
      description = new ArrayList<Description>();
    description.add(desc);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
