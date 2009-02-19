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

/**
 * Jul 7, 2004 .
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: Global.java,v 1.1 2004/07/08 19:11:45 tuan08 Exp $
 */
public class Global {

  /**
   * Name.
   */
  private String name;

  /**
   * Description.
   */
  private String description;

  /**
   * Major version.
   */
  private Integer majorVersion;

  /**
   * Minor version.
   */
  private Integer minorVersion;

  /**
   * Should the Portlet Container hook portlet exceptions and generate human readable error report
   * or generate PortletProcessingException.
   */
  private String hookPortletExceptions;

  /**
   * @return description
   */
  public final String getDescription() {
    return description;
  }

  /**
   * @param description description
   */
  public final void setDescription(final String description) {
    this.description = description;
  }

  /**
   * @return major version
   */
  public final int getMajorVersion() {
    return majorVersion.intValue();
  }

  /**
   * @param majorVersion major version
   */
  public final void setMajorVersion(final int majorVersion) {
    this.majorVersion = new Integer(majorVersion);
  }

  /**
   * @return minor version
   */
  public final int getMinorVersion() {
    return minorVersion.intValue();
  }

  /**
   * @param minorVersion minor version
   */
  public final void setMinorVersion(final int minorVersion) {
    this.minorVersion = new Integer(minorVersion);
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
   * @return the hookPortletExceptions
   */
  public String getHookPortletExceptions() {
    return hookPortletExceptions;
  }

  /**
   * @param hookPortletExceptions the hookPortletExceptions to set
   */
  public void setHookPortletExceptions(String hookPortletExceptions) {
    this.hookPortletExceptions = hookPortletExceptions;
  }
}
