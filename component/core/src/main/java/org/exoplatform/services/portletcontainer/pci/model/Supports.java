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

/**
 * Jul 11, 2004 .
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: Supports.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class Supports {

  /**
   * Mime type.
   */
  private String mimeType;

  /**
   * Portlet modes.
   */
  private List<String> portletMode;

  // portlet api 2.0

  /**
   * Id.
   */
  private String id;

  /**
   * Window states.
   */
  private List<String> windowState;

  /**
   * Constructor.
   */
  public Supports() {
    portletMode = new ArrayList<String>();
    windowState = new ArrayList<String>();
  }

  /**
   * @return mime type
   */
  public final String getMimeType() {
    return mimeType;
  }

  /**
   * @param mimeType mime type
   */
  public final void setMimeType(final String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * @return portlet modes
   */
  public final List<String> getPortletMode() {
    return portletMode;
  }

  /**
   * @param portletMode portlet modes
   */
  public final void setPortletMode(final List<String> portletMode) {
    this.portletMode = portletMode;
  }

  /**
   * @param mode portlet mode
   */
  public final void addPortletMode(final String mode) {
    this.portletMode.add(mode);
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
   * @return window states
   */
  public final List<String> getWindowState() {
    return windowState;
  }

  /**
   * @param windowState window states
   */
  public final void setWindowState(final List<String> windowState) {
    this.windowState = windowState;
  }

  /**
   * @param state window state
   */
  public final void addWindowState(final String state) {
    this.windowState.add(state);
  }

}
