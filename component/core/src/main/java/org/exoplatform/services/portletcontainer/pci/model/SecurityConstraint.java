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
 * @version: $Id: SecurityConstraint.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class SecurityConstraint {

  /**
   * Display name.
   */
  private List<DisplayName> displayName;

  /**
   * Portlet collection.
   */
  private PortletCollection portletCollection;

  /**
   * User data constraint.
   */
  private UserDataConstraint userDataConstraint;

  // portlet api 2.0

  /**
   * Id.
   */
  private String id;

  /**
   * @return display names
   */
  public final List<DisplayName> getDisplayNames() {
    if (displayName == null)
      return Constants.EMPTY_LIST;
    return displayName;
  }

  /**
   * @param displayName display names
   */
  public final void setDisplayName(final List<DisplayName> displayName) {
    this.displayName = displayName;
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
   * @return portlet collection
   */
  public final PortletCollection getPortletCollection() {
    return portletCollection;
  }

  /**
   * @param portletCollection portlet collection
   */
  public final void setPortletCollection(final PortletCollection portletCollection) {
    this.portletCollection = portletCollection;
  }

  /**
   * @return constraint
   */
  public final UserDataConstraint getUserDataConstraint() {
    return userDataConstraint;
  }

  /**
   * @param userDataConstraint constraint
   */
  public final void setUserDataConstraint(final UserDataConstraint userDataConstraint) {
    this.userDataConstraint = userDataConstraint;
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
