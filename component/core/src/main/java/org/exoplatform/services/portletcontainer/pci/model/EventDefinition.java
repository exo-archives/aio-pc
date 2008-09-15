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
public class EventDefinition {

  /**
   * Descriptions.
   */
  private List<Description> description;

  /**
   * Event name.
   */
  private QName             name;

  /**
   * Aliases.
   */
  private List<QName>       aliases;

  /**
   * Class fully qualified name of an event.
   */
  private String            javaClass;

  /**
   * Id.
   */
  private String            id;

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
  public final QName getPrefferedName() {
    return this.name;
  }

  /**
   * @param value preffered name
   */
  public final void setPrefferedName(final QName value) {
    this.name = value;
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
  public final List<QName> getAliases() {
    if (aliases == null)
      return Constants.EMPTY_LIST;
    return aliases;
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
   * @param value id
   */
  public final void setId(final String value) {
    this.id = value;
  }

  /**
   * @param newAliases aliases
   */
  public final void setAliases(final List<QName> newAliases) {
    aliases = newAliases;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (this == obj)
      return true;
    if (!(obj instanceof EventDefinition))
      return false;
    EventDefinition other = (EventDefinition) obj;

    if (this.id != other.id)
      return false;
    if (this.description != other.description)
      return false;
    if (this.name != other.name)
      return false;
    if (this.aliases != other.aliases)
      return false;
    if (this.javaClass != other.javaClass)
      return false;
    return true;
  }

}
