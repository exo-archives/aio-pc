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
 * Created by The eXo Platform SAS
 * Author : Roman Pedchenko <roman.pedchenko@exoplatform.com.ua>
 */
public class ResponseContext {

  private HttpServletResponse      httpServletResponse_;

  private ExoContainer             cont_;

  private String                   windowID_;

  private Input                    input_;

  private Portlet                  portletDatas_;

  private boolean                  isSecure_;

  private Collection<String>       supportedContents_;

  private Enumeration<WindowState> windowStates_;

  private List<CustomWindowState>  customWindowState_;

  private Output                   output_;

  private PortalContext            portalContext_;

  private final PortletRequest request;

  public ResponseContext(HttpServletResponse httpServletResponse,
                         ExoContainer cont,
                         String windowID,
                         Input input,
                         Portlet portletDatas,
                         boolean isSecure,
                         Collection<String> supportedContents,
                         Enumeration<WindowState> windowStates,
                         List<CustomWindowState> customWindowState,
                         Output output,
                         PortalContext portalContext,
                         PortletRequest request) {
    this.httpServletResponse_ = httpServletResponse;
    this.cont_ = cont;
    this.windowID_ = windowID;
    this.input_ = input;
    this.portletDatas_ = portletDatas;
    this.isSecure_ = isSecure;
    this.supportedContents_ = supportedContents;
    this.windowStates_ = windowStates;
    this.customWindowState_ = customWindowState;
    this.output_ = output;
    this.portalContext_ = portalContext;
    this.request = request;
  }

  public HttpServletResponse getHttpServletResponse() {
    return httpServletResponse_;
  }

  public ExoContainer getCont() {
    return cont_;
  }

  public String getWindowID() {
    return windowID_;
  }

  public Input getInput() {
    return input_;
  }

  public Portlet getPortletDatas() {
    return portletDatas_;
  }

  public boolean isSecure() {
    return isSecure_;
  }

  public Collection<String> getSupportedContents() {
    return supportedContents_;
  }

  public Enumeration<WindowState> getWindowStates() {
    return windowStates_;
  }

  public List<CustomWindowState> getCustomWindowState() {
    return customWindowState_;
  }

  public Output getOutput() {
    return output_;
  }

  public PortalContext getPortalContext() {
    return portalContext_;
  }

  public PortletRequest getPortletRequest() {
    return request;
  }

}
