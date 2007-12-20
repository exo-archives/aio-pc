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

import java.util.Enumeration;
import java.util.List;

import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;
import javax.portlet.WindowState;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponseWrapper;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.Output;
import org.exoplatform.services.portletcontainer.pci.PortletURLFactory;
import org.exoplatform.services.portletcontainer.pci.ResourceInput;
import org.exoplatform.services.portletcontainer.pci.model.CustomWindowState;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;

/**
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 28, 2003
 * Time: 10:18:25 PM
 */
public class PortletResponseImp extends HttpServletResponseWrapper implements PortletResponse {

  protected ResponseContext          resCtx;

  protected ExoContainer             cont_;

  protected List<CustomWindowState>  customWindowStates_;

  protected Input                    input_;

  protected Output                   output_;

  protected boolean                  redirectionPossible_;

  protected boolean                  sendRedirectAlreadyOccured_;

  protected Enumeration<WindowState> supportedWindowState_;

  protected Portlet                  portletDatas_;

  protected boolean                  isCurrentlySecured_;

  protected String                   windowId_;

  public PortletResponseImp(ResponseContext resCtx) {
    super(resCtx.getHttpServletResponse());
    this.cont_ = resCtx.getCont();
    this.input_ = resCtx.getInput();
    this.output_ = resCtx.getOutput();
    this.customWindowStates_ = resCtx.getCustomWindowState();
    this.supportedWindowState_ = resCtx.getPortalContext().getSupportedWindowStates();
    this.redirectionPossible_ = true;
    this.sendRedirectAlreadyOccured_ = false;
    this.portletDatas_ = resCtx.getPortletDatas();
    this.windowId_ = resCtx.getWindowID();
    this.isCurrentlySecured_ = resCtx.isSecure();
  }

  public void addProperty(String s,
                          String s1) {
    output_.addProperty(s, s1);
  }

  public void setProperty(String s,
                          String s1) {
    output_.addProperty(s, s1);
  }

  protected String getProperty(String s) {
    return (String) output_.getProperties().get(s);
  }

  protected void removeProperty(String s) {
    output_.getProperties().remove(s);
  }

  public String getNamespace() {
    return "I" + (windowId_.replace('-', 'I')).replace('/', 'I') + "I";
  }

  public String encodeURL(String path) {
    if (!path.startsWith("/") && !path.startsWith("http://")) {
      throw new IllegalArgumentException("Path must be started with / or http://");
    }
    // made for TCK
    // com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsMiscMiscTestPortlet
    // on weblogic 9.2
    // was: return path;
    if (super.encodeURL(path) == null) {
      return path;
    }
    return super.encodeURL(path);
  }

  // adds for jsr-286
  public Output getOutput() {
    return output_;
  }

  public void setOutput(Output o) {
    this.output_ = o;
  }

  public boolean isSendRedirectAlreadyOccured() {
    return sendRedirectAlreadyOccured_;
  }

  public void addProperty(Cookie cookie) {
    super.addCookie(cookie);
  }

  public void addProperty(String key,
                          org.w3c.dom.Element element) {
    // TODO does it work correctly??
    addProperty(new Cookie(key, element.toString()));
  }

  public PortletURL createRenderURL() throws java.lang.IllegalStateException {

    if (input_ instanceof ResourceInput) {
      // throws java.lang.IllegalStateException:
      // "If the cacheability level of the resource URL triggering this
      // serveResource call is not PAGE and thus does not allow for creating
      // render URLs."
      // if Cacheability == PAGE, then create URL
      if (!ResourceURL.PAGE.equalsIgnoreCase(((ResourceInput) input_).getCacheability())) {
        throw new IllegalStateException("Cannot create render URL from within serveResource() without PAGE Cacheability");
      }
    }

    if (input_.getPortletURLFactory() != null) {
      return input_.getPortletURLFactory().createPortletURL(PCConstants.renderString);
    }

    return new PortletURLImp(PCConstants.renderString,
                             input_.getBaseURL(),
                             input_.getMarkup(),
                             portletDatas_.getSupports(),
                             isCurrentlySecured_,
                             input_.getEscapeXml(),
                             portletDatas_);
  }

}
