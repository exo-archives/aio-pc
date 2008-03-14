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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.portlet.PortalContext;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.Output;
import org.exoplatform.services.portletcontainer.pci.model.CustomWindowState;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;

/**
 * Created by The eXo Platform SAS.
 * Author : Roman Pedchenko roman.pedchenko@exoplatform.com.ua
 */
public class ResponseContext {

  /**
   * Http servlet response.
   */
  private final HttpServletResponse httpServletResponse;

  /**
   * Exo container.
   */
  private final ExoContainer cont;

  /**
   * Window id.
   */
  private final String windowID;

  /**
   * Input.
   */
  private final Input input;

  /**
   * Portlet datas.
   */
  private final Portlet portletDatas;

  /**
   * Is secure.
   */
  private final boolean isSecure;

  /**
   * Supported contents.
   */
  private final Collection<String> supportedContents;

  /**
   * Window states.
   */
  private final Enumeration<WindowState> windowStates;

  /**
   * Custom window states.
   */
  private final List<CustomWindowState> customWindowState;

  /**
   * Output.
   */
  private final Output output;

  /**
   * Portal context.
   */
  private final PortalContext portalContext;

  /**
   * Portlet request.
   */
  private final PortletRequest request;

  /**
   * @param httpServletResponse http servlet response
   * @param cont container
   * @param windowID window id
   * @param input input
   * @param portletDatas portlet datas
   * @param isSecure is secure
   * @param supportedContents supported contents
   * @param windowStates window states
   * @param customWindowState custom window state
   * @param output output
   * @param portalContext portal context
   * @param request request
   */
  public ResponseContext(final HttpServletResponse httpServletResponse,
      final ExoContainer cont,
      final String windowID,
      final Input input,
      final Portlet portletDatas,
      final boolean isSecure,
      final Collection<String> supportedContents,
      final Enumeration<WindowState> windowStates,
      final List<CustomWindowState> customWindowState,
      final Output output,
      final PortalContext portalContext,
      final PortletRequest request) {
    this.httpServletResponse = httpServletResponse;
    this.cont = cont;
    this.windowID = windowID;
    this.input = input;
    this.portletDatas = portletDatas;
    this.isSecure = isSecure;
    this.supportedContents = supportedContents;
    this.windowStates = windowStates;
    this.customWindowState = customWindowState;
    this.output = output;
    this.portalContext = portalContext;
    this.request = request;
  }

  /**
   * @return http servlet response
   */
  public final HttpServletResponse getHttpServletResponse() {
    return httpServletResponse;
  }

  /**
   * @return exo container
   */
  public final ExoContainer getCont() {
    return cont;
  }

  /**
   * @return window id
   */
  public final String getWindowID() {
    return windowID;
  }

  /**
   * @return input
   */
  public final Input getInput() {
    return input;
  }

  /**
   * @return portlet datas
   */
  public final Portlet getPortletDatas() {
    return portletDatas;
  }

  /**
   * @return is secure
   */
  public final boolean isSecure() {
    return isSecure;
  }

  /**
   * @return supported contents
   */
  public final Collection<String> getSupportedContents() {
    return supportedContents;
  }

  /**
   * @return window states
   */
  public final Enumeration<WindowState> getWindowStates() {
    return windowStates;
  }

  /**
   * @return custom window states
   */
  public final List<CustomWindowState> getCustomWindowState() {
    return customWindowState;
  }

  /**
   * @return output
   */
  public final Output getOutput() {
    return output;
  }

  /**
   * @return portal context
   */
  public final PortalContext getPortalContext() {
    return portalContext;
  }

  /**
   * @return portlet request
   */
  public final PortletRequest getPortletRequest() {
    return request;
  }

}
