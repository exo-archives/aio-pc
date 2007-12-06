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
 * @author: Roman Pedchenko
 * @email:  roman.pedchenko@exoplatform.com.ua
 * @version: $Id$
 */
public class SharedSessionAttribute {
  private List description;
	private String name;
  private List names;
  private String xmlSchema;
  private String jaxbMapping;
  private String javaClass;
  private String id;

	public List getDescription() {
    if(description == null) return Constants.EMPTY_LIST;
		return description;
	}

	public void setDescription(List description) {
		this.description = description;
	}

  public void addDescription(Description desc) {
  	if(this.description == null)  description = new ArrayList();
    this.description.add(desc);
  }

  public String getPrefferedName() { return this.name; }

  public void setPrefferedName(String value) {
    this.name = value;
    addName(value);
  }

  public void addName(String value) {
    if (names == null)
      names = new ArrayList();
    names.add(value);
  }

  public List getNames() {
    if (names == null)
      return Constants.EMPTY_LIST;
    return names;
  }

  public String getXmlSchema() { return this.xmlSchema; }

  public void setXmlSchema(String value) { this.xmlSchema = value; }

  public String getJaxbMapping() { return this.jaxbMapping; }

  public void setJaxbMapping(String value) { this.jaxbMapping = value; }

  public String getJavaClass() { return this.javaClass; }

  public void setJavaClass(String value) { this.javaClass = value; }

  public String getId() { return this.id ; }

  public void setId(String value) { this.id = value ; }
}
