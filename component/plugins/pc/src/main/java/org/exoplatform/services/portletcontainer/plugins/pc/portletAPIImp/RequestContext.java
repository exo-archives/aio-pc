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
import java.util.List;

import javax.portlet.PortalContext;
import javax.portlet.PortletContext;
import javax.servlet.http.HttpServletRequest;

import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.model.CustomPortletMode;
import org.exoplatform.services.portletcontainer.pci.model.CustomWindowState;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.SecurityConstraint;
import org.exoplatform.services.portletcontainer.pci.model.UserAttribute;

/**
 * Created by The eXo Platform SAS
 * Author : Roman Pedchenko <roman.pedchenko@exoplatform.com.ua>
 */
public class RequestContext {

  private HttpServletRequest       httpServletRequest_;

  private PortalContext            portalContext_;

  private PortletContext           portletContext_;

  private PortletSessionImp        session_;

  private Portlet                  portletDatas_;

  private Input                    input_;

  private PortletWindowInternal    portletWindowInternal_;

  private List<SecurityConstraint> securityContraints_;

  private List<UserAttribute>      userAttributes_;

  private List<CustomPortletMode>  customPortletModes_;

  private List<CustomWindowState>  customWindowStates_;

  private Collection<String>       roles_;

  private Collection               supportedContents_;

  public RequestContext(HttpServletRequest httpServletRequest,
                        PortalContext portalContext,
                        PortletContext portletContext,
                        PortletSessionImp session,
                        // Portlet portletDatas,
                        Input input,
                        PortletWindowInternal portletWindowInternal,
                        List<SecurityConstraint> securityContraints,
                        List<UserAttribute> userAttributes,
                        List<CustomPortletMode> customPortletModes,
                        List<CustomWindowState> customWindowStates,
                        Collection<String> roles,
                        Collection supportedContents) {
    this.httpServletRequest_ = httpServletRequest;
    this.portalContext_ = portalContext;
    this.portletContext_ = portletContext;
    this.session_ = session;
    this.portletDatas_ = ((PortletContextImpl) portletContext).getPortlet();
    this.input_ = input;
    this.portletWindowInternal_ = portletWindowInternal;
    this.securityContraints_ = securityContraints;
    this.userAttributes_ = userAttributes;
    this.customPortletModes_ = customPortletModes;
    this.customWindowStates_ = customWindowStates;
    this.roles_ = roles;
    this.supportedContents_ = supportedContents;
  }

  public HttpServletRequest getHttpServletRequest() {
    return httpServletRequest_;
  }

  public PortalContext getPortalContext() {
    return portalContext_;
  }

  public PortletContext getPortletContext() {
    return portletContext_;
  }

  public PortletSessionImp getSession() {
    return session_;
  }

  public Portlet getPortletDatas() {
    return portletDatas_;
  }

  public Input getInput() {
    return input_;
  }

  public PortletWindowInternal getPortletWindowInternal() {
    return portletWindowInternal_;
  }

  public List<SecurityConstraint> getSecurityContraints() {
    return securityContraints_;
  }

  public List<UserAttribute> getUserAttributes() {
    return userAttributes_;
  }

  public List<CustomPortletMode> getCustomPortletModes() {
    return customPortletModes_;
  }

  public List<CustomWindowState> getCustomWindowStates() {
    return customWindowStates_;
  }

  public Collection<String> getRoles() {
    return roles_;
  }

  public Collection getSupportedContents() {
    return supportedContents_;
  }

}
