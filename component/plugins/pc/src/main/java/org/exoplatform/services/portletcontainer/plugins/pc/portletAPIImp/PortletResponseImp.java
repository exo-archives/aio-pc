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

import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponseWrapper;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.Output;
import org.exoplatform.services.portletcontainer.pci.ResourceInput;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/**
 * @author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * @date: Jul 28, 2003
 * @time: 10:18:25 PM
 */
public class PortletResponseImp extends HttpServletResponseWrapper implements PortletResponse {

  /**
   * Response context.
   */
  private ResponseContext resCtx;

  /**
   * Exo container.
   */
  private final ExoContainer cont;

  /**
   * Input.
   */
  private Input input;

  /**
   * Output.
   */
  private Output output;

  /**
   * Is redirect possible.
   */
  private boolean redirectionPossible;

  /**
   * Is redirected.
   */
  private boolean sendRedirectAlreadyOccured;

  /**
   * Portlet datas.
   */
  private Portlet portletDatas;

  /**
   * Is currently secured.
   */
  private final boolean isCurrentlySecured;

  /**
   * Window id.
   */
  private final String windowId;

  /**
   * Is already forwarded.
   */
  private boolean alreadyForwarded;

  /**
   * @param resCtx response context
   */
  public PortletResponseImp(final ResponseContext resCtx) {
    super(resCtx.getHttpServletResponse());
    this.cont = resCtx.getCont();
    this.input = resCtx.getInput();
    this.output = resCtx.getOutput();
    this.setRedirectionPossible(true);
    this.sendRedirectAlreadyOccured = false;
    this.portletDatas = resCtx.getPortletDatas();
    this.windowId = resCtx.getWindowID();
    this.isCurrentlySecured = resCtx.isSecure();
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @param value value
   * @see javax.portlet.PortletResponse#addProperty(java.lang.String,
   *      java.lang.String)
   */
  public final void addProperty(final String name, final String value) {
    getOutput().addProperty(name, value);
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @param value value
   * @see javax.portlet.PortletResponse#setProperty(java.lang.String,
   *      java.lang.String)
   */
  public final void setProperty(final String name, final String value) {
    getOutput().addProperty(name, value);
  }

  /**
   * @param name name
   * @return value
   */
  protected final String getProperty(final String name) {
    return (String) getOutput().getProperties().get(name);
  }

  /**
   * @param name name
   */
  protected final void removeProperty(final String name) {
    getOutput().getProperties().remove(name);
  }

  /**
   * Overridden method.
   *
   * @return namespace
   * @see javax.portlet.PortletResponse#getNamespace()
   */
  public final String getNamespace() {
    return "I" + (getWindowId().replace('-', 'I')).replace('/', 'I') + "I";
  }

  /**
   * Overridden method.
   *
   * @param path url
   * @return encoded url
   * @see javax.servlet.http.HttpServletResponseWrapper#encodeURL(java.lang.String)
   */
  public final String encodeURL(final String path) {
    // http://jira.exoplatform.org/browse/PC-335
    if (path.startsWith("#"))
      return path;
    if (!path.startsWith("/") && !path.startsWith("http://"))
      throw new IllegalArgumentException("Path must be started with / or http://");

    // made for TCK
    // com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsMiscMiscTestPortlet
    // on weblogic 9.2
    // was: return path;
    if (super.encodeURL(path) == null)
      return path;
    return super.encodeURL(path);
  }

  /**
   * @return output
   */
  public final Output getOutput() {
    return output;
  }

  /**
   * @param o output
   */
  public final void setOutput(final Output o) {
    this.output = o;
  }

  /**
   * @return is redirected
   */
  public final boolean isSendRedirectAlreadyOccured() {
    return sendRedirectAlreadyOccured;
  }

  /**
   * @param sendRedirectAlreadyOccured is redirected
   */
  public final void setSendRedirectAlreadyOccured(final boolean sendRedirectAlreadyOccured) {
    this.sendRedirectAlreadyOccured = sendRedirectAlreadyOccured;
  }

  /**
   * Overridden method.
   *
   * @param cookie cookie
   * @see javax.portlet.PortletResponse#addProperty(javax.servlet.http.Cookie)
   */
  public final void addProperty(final Cookie cookie) {
    super.addCookie(cookie);
  }

  /**
   * Overridden method.
   *
   * @param key key
   * @param element element
   * @see javax.portlet.PortletResponse#addProperty(java.lang.String,
   *      org.w3c.dom.Element)
   */
  public final void addProperty(final String key, final org.w3c.dom.Element element) {
    // TODO does it work correctly??
    addProperty(new Cookie(key, element.toString()));
  }

  /**
   * @return render url
   * @throws java.lang.IllegalStateException exception
   */
  public final PortletURL createRenderURL() throws java.lang.IllegalStateException {

    if (getInput() instanceof ResourceInput)
      // throws java.lang.IllegalStateException:
      // "If the cacheability level of the resource URL triggering this
      // serveResource call is not PAGE and thus does not allow for creating
      // render URLs."
      // if Cacheability == PAGE, then create URL
      if (!ResourceURL.PAGE.equalsIgnoreCase(((ResourceInput) getInput()).getCacheability()))
        throw new IllegalStateException("Cannot create render URL from within serveResource() without PAGE Cacheability");

    if (getInput().getPortletURLFactory() != null)
      return getInput().getPortletURLFactory().createPortletURL(PCConstants.RENDER_STRING);

    return new PortletURLImp(PCConstants.RENDER_STRING, getInput().getBaseURL(), getInput()
        .getMarkup(), getPortletDatas().getSupports(), isCurrentlySecured(), getInput()
        .getEscapeXml(), getPortletDatas());
  }

  /**
   * Overridden method.
   *
   * @param name element name
   * @return element
   * @throws DOMException exception
   * @see javax.portlet.PortletResponse#createElement(java.lang.String)
   */
  public final Element createElement(final String name) throws DOMException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Already forwarded.
   */
  public final void setAlreadyForwarded() {
    alreadyForwarded = true;
    sendRedirectAlreadyOccured = true;
  }

  /**
   * @return is already forwarded
   */
  public final boolean isAlreadyForwarded() {
    return alreadyForwarded;
  }

  /**
   * @return window id
   */
  protected final String getWindowId() {
    return windowId;
  }

  /**
   * @return is currently secured
   */
  protected final boolean isCurrentlySecured() {
    return isCurrentlySecured;
  }

  /**
   * @param portletDatas the portletDatas to set
   */
  protected final void setPortletDatas(final Portlet portletDatas) {
    this.portletDatas = portletDatas;
  }

  /**
   * @return the portletDatas
   */
  protected final Portlet getPortletDatas() {
    return portletDatas;
  }

  /**
   * @param redirectionPossible the redirectionPossible to set
   */
  protected final void setRedirectionPossible(final boolean redirectionPossible) {
    this.redirectionPossible = redirectionPossible;
  }

  /**
   * @return the redirectionPossible
   */
  protected final boolean isRedirectionPossible() {
    return redirectionPossible;
  }

  /**
   * @param input the input to set
   */
  protected final void setInput(final Input input) {
    this.input = input;
  }

  /**
   * @return the input
   */
  protected final Input getInput() {
    return input;
  }

  /**
   * @return the cont
   */
  protected final ExoContainer getCont() {
    return cont;
  }

  /**
   * @param resCtx the resCtx to set
   */
  protected final void setResCtx(final ResponseContext resCtx) {
    this.resCtx = resCtx;
  }

  /**
   * @return the resCtx
   */
  protected final ResponseContext getResCtx() {
    return resCtx;
  }

}
