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
 * Created by The eXo Platform SAS.
 * Author : Roman Pedchenko roman.pedchenko@exoplatform.com.ua
 */
public class RequestContext {

  /**
   * Http servlet request.
   */
  private final HttpServletRequest httpServletRequest;

  /**
   * Portal context.
   */
  private final PortalContext portalContext;

  /**
   * Portlet context.
   */
  private final PortletContext portletContext;

  /**
   * Session.
   */
  private final PortletSessionImp session;

  /**
   * Portlet datas.
   */
  private final Portlet portletDatas;

  /**
   * Input.
   */
  private final Input input;

  /**
   * Portlet window internal object.
   */
  private final PortletWindowInternal portletWindowInternal;

  /**
   * Security contraints.
   */
  private final List<SecurityConstraint> securityContraints;

  /**
   * User attributes.
   */
  private final List<UserAttribute> userAttributes;

  /**
   * Custom portlet modes
   */
  private final List<CustomPortletMode> customPortletModes;

  /**
   * Custom window states.
   */
  private final List<CustomWindowState> customWindowStates;

  /**
   * Roles.
   */
  private final Collection<String> roles;

  /**
   * Supported contents.
   */
  private final Collection<String> supportedContents;

  /**
   * @param httpServletRequest http servlet request
   * @param portalContext portal context
   * @param portletContext portlet context
   * @param session session
   * @param input input
   * @param portletWindowInternal portlet window internal
   * @param securityContraints security contraints
   * @param userAttributes user attributes
   * @param customPortletModes custom portlet modes
   * @param customWindowStates custom window states
   * @param roles roles
   * @param supportedContents supported contents
   */
  public RequestContext(final HttpServletRequest httpServletRequest,
      final PortalContext portalContext,
      final PortletContext portletContext,
      final PortletSessionImp session,
      // Portlet portletDatas,
      final Input input,
      final PortletWindowInternal portletWindowInternal,
      final List<SecurityConstraint> securityContraints,
      final List<UserAttribute> userAttributes,
      final List<CustomPortletMode> customPortletModes,
      final List<CustomWindowState> customWindowStates,
      final Collection<String> roles,
      final Collection<String> supportedContents) {
    this.httpServletRequest = httpServletRequest;
    this.portalContext = portalContext;
    this.portletContext = portletContext;
    this.session = session;
    this.portletDatas = ((PortletContextImpl) portletContext).getPortlet();
    this.input = input;
    this.portletWindowInternal = portletWindowInternal;
    this.securityContraints = securityContraints;
    this.userAttributes = userAttributes;
    this.customPortletModes = customPortletModes;
    this.customWindowStates = customWindowStates;
    this.roles = roles;
    this.supportedContents = supportedContents;
  }

  /**
   * @return http servlet request
   */
  public final HttpServletRequest getHttpServletRequest() {
    return httpServletRequest;
  }

  /**
   * @return portal context
   */
  public final PortalContext getPortalContext() {
    return portalContext;
  }

  /**
   * @return portlet context
   */
  public final PortletContext getPortletContext() {
    return portletContext;
  }

  /**
   * @return session
   */
  public final PortletSessionImp getSession() {
    return session;
  }

  /**
   * @return portlet datas
   */
  public final Portlet getPortletDatas() {
    return portletDatas;
  }

  /**
   * @return input
   */
  public final Input getInput() {
    return input;
  }

  /**
   * @return portlet window internal object
   */
  public final PortletWindowInternal getPortletWindowInternal() {
    return portletWindowInternal;
  }

  /**
   * @return security constraints
   */
  public final List<SecurityConstraint> getSecurityContraints() {
    return securityContraints;
  }

  /**
   * @return user attributes
   */
  public final List<UserAttribute> getUserAttributes() {
    return userAttributes;
  }

  /**
   * @return portlet modes
   */
  public final List<CustomPortletMode> getCustomPortletModes() {
    return customPortletModes;
  }

  /**
   * @return window states
   */
  public final List<CustomWindowState> getCustomWindowStates() {
    return customWindowStates;
  }

  /**
   * @return roles
   */
  public final Collection<String> getRoles() {
    return roles;
  }

  /**
   * @return supported contents
   */
  public final Collection<String> getSupportedContents() {
    return supportedContents;
  }

}
