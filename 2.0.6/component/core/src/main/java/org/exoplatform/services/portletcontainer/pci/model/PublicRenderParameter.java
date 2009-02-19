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

import javax.xml.namespace.QName;

import org.exoplatform.Constants;

/**
 * @author: Roman Pedchenko
 * @email: roman.pedchenko@exoplatform.com.ua
 * @version: $Id$
 */
public class PublicRenderParameter {

  /**
   * Description.
   */
  private List<String> description;

  /**
   * Indentifier.
   */
  private String identifier;

  /**
   * QName.
   */
  private QName qname;

  /**
   * Name.
   */
  private String name;

  /**
   * Aliases.
   */
  private List<QName> aliases;

  /**
   * Id.
   */
  private String id;

  /**
   * @return descriptions
   */
  public final List<String> getDescription() {
    if (description == null)
      return Constants.EMPTY_LIST;
    return description;
  }

  /**
   * @param description descriptions
   */
  public final void setDescription(final List<String> description) {
    this.description = description;
  }

  /**
   * @param desc description
   */
  public final void addDescription(final String desc) {
    if (this.description == null)
      description = new ArrayList<String>();
    this.description.add(desc);
  }

  /**
   * @return identifier
   */
  public final String getIdentifier() {
    return this.identifier;
  }

  /**
   * @param value identifier
   */
  public final void setIdentifier(final String value) {
    this.identifier = value;
  }

  /**
   * @param value alias
   */
  public final void addAlias(final QName value) {
    if (aliases == null)
      aliases = new ArrayList<QName>();
    aliases.add(value);
  }

  /**
   * @return aliases
   */
  public final List<QName> getAlias() {
    if (aliases == null)
      return Constants.EMPTY_LIST;
    return aliases;
  }

  /**
   * @return qname
   */
  public final QName getQname() {
    return qname;
  }

  /**
   * @param qname qname
   */
  public final void setQname(final QName qname) {
    this.qname = qname;
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
