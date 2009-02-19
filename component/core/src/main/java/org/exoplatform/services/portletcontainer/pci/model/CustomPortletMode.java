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
 * @version: $Id: CustomPortletMode.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class CustomPortletMode {

  /**
   * Description list.
   */
  private List<Description> description;

  /**
   * Portlet mode.
   */
  private String portletMode;

  // portlet api 2.0

  /**
   * Id.
   */
  private String id;

  /**
   * Either portal managed mode.
   */
  private Boolean portalManaged;

  /**
   * Resource id.
   */
  private String resourceID;

  /**
   * @return description list
   */
  public final List<Description> getDescription() {
    if (description == null)
      return Constants.EMPTY_LIST;
    return description;
  }

  /**
   * @param description description list
   */
  public final void setDescription(final List<Description> description) {
    this.description = description;
  }

  /**
   * @param desc description
   */
  public final void addDescription(final Description desc) {
    if (description == null)
      description = new ArrayList<Description>();
    this.description.add(desc);
  }

  /**
   * @return portlet mode
   */
  public final String getPortletMode() {
    return portletMode;
  }

  /**
   * @param portletMode portlet mode
   */
  public final void setPortletMode(final String portletMode) {
    this.portletMode = portletMode;
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

  // portlet api 2.0

  /**
   * @param portalManaged either portal managed mode
   */
  public final void setPortalManaged(final Boolean portalManaged) {
    this.portalManaged = portalManaged;
  }

  /**
   * @return is portal managed mode
   */
  public final Boolean isPortalManaged() {
    return portalManaged;
  }

  /**
   * @param resourceID resource id
   */
  public final void setResourceID(final String resourceID) {
    this.resourceID = resourceID;
  }

  /**
   * @return resource id
   */
  public final String getResourceID() {
    return resourceID;
  }

}
