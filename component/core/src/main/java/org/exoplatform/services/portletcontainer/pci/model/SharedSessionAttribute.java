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
 * @email: roman.pedchenko@exoplatform.com.ua
 * @version: $Id$
 */
public class SharedSessionAttribute {

  /**
   * Descriptions.
   */
  private List<Description> description;

  /**
   * Preffered name.
   */
  private String name;

  /**
   * Names.
   */
  private List<String> names;

  /**
   * XML schema.
   */
  private String xmlSchema;

  /**
   * JAXB mapping.
   */
  private String jaxbMapping;

  /**
   * Java class.
   */
  private String javaClass;

  /**
   * Id.
   */
  private String id;

  /**
   * @return descriptions
   */
  public final List<Description> getDescription() {
    if (description == null)
      return Constants.EMPTY_LIST;
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
    if (this.description == null)
      description = new ArrayList<Description>();
    this.description.add(desc);
  }

  /**
   * @return preffered name
   */
  public final String getPrefferedName() {
    return this.name;
  }

  /**
   * @param value preffered name
   */
  public final void setPrefferedName(final String value) {
    this.name = value;
    addName(value);
  }

  /**
   * @param value name
   */
  public final void addName(final String value) {
    if (names == null)
      names = new ArrayList<String>();
    names.add(value);
  }

  /**
   * @return names
   */
  public final List<String> getNames() {
    if (names == null)
      return Constants.EMPTY_LIST;
    return names;
  }

  /**
   * @return xml schema
   */
  public final String getXmlSchema() {
    return this.xmlSchema;
  }

  /**
   * @param value xml schema
   */
  public final void setXmlSchema(final String value) {
    this.xmlSchema = value;
  }

  /**
   * @return jaxb mapping
   */
  public final String getJaxbMapping() {
    return this.jaxbMapping;
  }

  /**
   * @param value jaxb mapping
   */
  public final void setJaxbMapping(final String value) {
    this.jaxbMapping = value;
  }

  /**
   * @return java class
   */
  public final String getJavaClass() {
    return this.javaClass;
  }

  /**
   * @param value java class
   */
  public final void setJavaClass(final String value) {
    this.javaClass = value;
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
