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
package org.exoplatform.frameworks.portletcontainer.portalframework;

import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;

/**
 * Created by The eXo Platform SAS  .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */

/**
 * Internal class that is used to store info about single portlet window
 * it extends functionality of ExoWindowID and adds methods to store
 * current portlet mode and window state of a portlet.
 */
public class WindowID2 extends ExoWindowID {

  /**
   * Stored portlet mode for a portlet window.
   */
  private PortletMode portletMode;

  /**
   * Stored window state for a portlet window.
   */
  private WindowState windowState;

  /**
   * Stored render parameters for a portlet window.
   */
  private Map<String, String[]> renderParams;

  /**
   * Default constructor.
   */
  public WindowID2() {
    super();
  }
  
  /**
   * Extended constructor.
   * 
   * @param persistenceId
   */
  public WindowID2(final String persistenceId) {
    super(persistenceId);
  }
  
  /**
   * Returns stored portlet mode for a portlet window.
   *
   * @return stored portlet mode
   */
  public final PortletMode getPortletMode() {
    return portletMode;
  }

  /**
   * Stores portlet mode for a portlet window.
   *
   * @param portletMode portlet mode to store
   */
  public final void setPortletMode(final PortletMode portletMode) {
    this.portletMode = portletMode;
  }

  /**
   * Returns stored window state for a portlet window.
   *
   * @return stored window state
   */
  public final WindowState getWindowState() {
    return windowState;
  }

  /**
   * Stores window state for a portlet window.
   *
   * @param windowState window state to store
   */
  public final void setWindowState(final WindowState windowState) {
    this.windowState = windowState;
  }

  /**
   * Returns render parameters stored among user requests.
   *
   * @return render parameters map
   */
  public final Map<String, String[]> getRenderParams() {
    return renderParams;
  }

  /**
   * Stores render parameters among user requests.
   *
   * @param renderParams render parameters map to store
   */
  public final void setRenderParams(final Map<String, String[]> renderParams) {
    this.renderParams = renderParams;
  }
}
