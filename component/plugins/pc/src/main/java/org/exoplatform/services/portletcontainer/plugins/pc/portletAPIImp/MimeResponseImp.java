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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.portlet.CacheControl;
import javax.portlet.MimeResponse;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.ResourceInput;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.exoplatform.services.portletcontainer.pci.model.Supports;

/**
 * Author : Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua 25.05.2007.
 */
public class MimeResponseImp extends PortletResponseImp implements MimeResponse {

  /**
   * Supported contents.
   */
  private final Collection<String> supportedContents;

  /**
   * Content type.
   */
  private String contentType;

  /**
   * Has writer already been gotten.
   */
  private boolean writerAlreadyCalled;

  /**
   * Has output stream already been gotten.
   */
  private boolean outputStreamAlreadyCalled;

  /**
   * Cache control object.
   */
  private final CacheControlImp cacheCtl;

  /**
   * Is committed.
   */
  private boolean committed;

  /**
   * @param resCtx response context
   */
  public MimeResponseImp(final ResponseContext resCtx) {
    super(resCtx);
    this.supportedContents = resCtx.getSupportedContents();
    this.writerAlreadyCalled = false;
    this.outputStreamAlreadyCalled = false;
    this.cacheCtl = new CacheControlImp(this);
    this.committed = false;
  }

  /**
   * Overridden method.
   *
   * @return content type
   * @see javax.servlet.ServletResponseWrapper#getContentType()
   */
  public final String getContentType() {
    if ((contentType == null) || "".equals(contentType))
      return null;
    return contentType;
  }

  /**
   * Overridden method.
   *
   * @param contentType content type
   * @see javax.servlet.ServletResponseWrapper#setContentType(java.lang.String)
   */
  public void setContentType(String contentType) {
    if (committed)
      throw new IllegalStateException("the response has already been committed");
    if (contentType != null)
      contentType = StringUtils.split(contentType, ';')[0];

    if (!isContentTypeSupported(contentType))
      throw new IllegalArgumentException("the content type : " + contentType + " is not supported.");
    this.contentType = contentType;
    if (getOutput() instanceof RenderOutput)
      ((RenderOutput) getOutput()).setContentType(this.contentType);
    if (getOutput() instanceof ResourceOutput)
      ((ResourceOutput) getOutput()).setContentType(this.contentType);

  }

  /**
   * Overridden method.
   *
   * @return output stream
   * @throws IOException exception
   * @see javax.portlet.MimeResponse#getPortletOutputStream()
   */
  public final OutputStream getPortletOutputStream() throws IOException {
    if (isAlreadyForwarded())
      throw new IllegalStateException("response has already been forwarded");
    if (writerAlreadyCalled)
      throw new IllegalStateException("getWriter() has already been called");
    if ((contentType == null) || "".equals(contentType))
      throw new IllegalStateException("the content type has not been set before calling the"
          + "getPortletOutputStream() method.");
    outputStreamAlreadyCalled = true;
    return super.getOutputStream();
  }

  /**
   * Overridden method.
   *
   * @return writer
   * @throws IOException exception
   * @see javax.servlet.ServletResponseWrapper#getWriter()
   */
  public final PrintWriter getWriter() throws IOException {
    if (isAlreadyForwarded())
      throw new IllegalStateException("response has already been forwarded");
    if (outputStreamAlreadyCalled)
      throw new IllegalStateException("the getPortletOutputStream object has already been called");
    if ((contentType == null) || "".equals(contentType))
      throw new IllegalStateException("the content type has not been set before calling the"
          + "getWriter() method.");
    writerAlreadyCalled = true;
    return super.getWriter();
  }

  /**
   * @param contentTypeToTest content type to test
   * @return is content type supported
   */
  private boolean isContentTypeSupported(final String contentTypeToTest) {
    Collection<String> c = getResponseContentTypes();
    for (String element : c)
      if (element.equals(contentTypeToTest))
        return true;
    return false;
  }

  /**
   * @return response content types
   */
  private Collection<String> getResponseContentTypes() {
    // TODO could be shared with PortletRequest.getResponseContentType()
    Collection<String> result = new ArrayList<String>();
    result.add(getResponseContentType());
    for (String element : supportedContents) {
      List<Supports> l = getPortletDatas().getSupports();
      for (int i = 0; i < l.size(); i++) {
        Supports supportsType = l.get(i);
        String mimeType = supportsType.getMimeType();
        if (element.equals(mimeType) && !element.equals(getInput().getMarkup())) {
          List<String> portletModes = supportsType.getPortletMode();
          for (String portletMode : portletModes)
            if (portletMode.equals(getInput().getPortletMode().toString())) {
              result.add(mimeType);
              break;
            }
        }
      }
    }
    return result;
  }

  /**
   * @return response content type
   */
  private String getResponseContentType() {
    List<Supports> l = getPortletDatas().getSupports();
    for (int i = 0; i < l.size(); i++) {
      Supports supportsType = l.get(i);
      String mimeType = supportsType.getMimeType();
      if (mimeType.equals(getInput().getMarkup())) {
        List<String> portletModes = supportsType.getPortletMode();
        for (String portletMode : portletModes)
          if (portletMode.equals(getInput().getPortletMode().toString()))
            return mimeType;
      }
    }
    return PCConstants.XHTML_MIME_TYPE;
  }

  /**
   * Overridden method.
   *
   * @throws IOException exception
   * @see javax.servlet.ServletResponseWrapper#flushBuffer()
   */
  public final void flushBuffer() throws IOException {
    committed = true;
    return;
  }

  /**
   * Overridden method.
   *
   * @return buffer size
   * @see javax.servlet.ServletResponseWrapper#getBufferSize()
   */
  public final int getBufferSize() {
    return 0;
  }

  /**
   * Overridden method.
   *
   * @return is committed
   * @see javax.servlet.ServletResponseWrapper#isCommitted()
   */
  public final boolean isCommitted() {
    // made for TCK
    // com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsMiscMiscTestPortlet
    // on weblogic 9.2
    // was: return true;
    return committed;
  }

  /**
   * Overridden method.
   *
   * @see javax.servlet.ServletResponseWrapper#reset()
   */
  public final void reset() {
    return;
  }

  /**
   * Overridden method.
   *
   * @see javax.servlet.ServletResponseWrapper#resetBuffer()
   */
  public final void resetBuffer() {
    return;
  }

  /**
   * Overridden method.
   *
   * @param size buffer size
   * @see javax.servlet.ServletResponseWrapper#setBufferSize(int)
   */
  public final void setBufferSize(final int size) {
    return;
  }

  /**
   * Overridden method.
   *
   * @return locale
   * @see javax.servlet.ServletResponseWrapper#getLocale()
   */
  public final Locale getLocale() {
    Locale l = super.getLocale();
    if (l == null)
      return new Locale("en");
    return l;
  }

  /**
   * Overridden method.
   *
   * @return cache control object
   * @see javax.portlet.MimeResponse#getCacheControl()
   */
  public final CacheControl getCacheControl() {
    return cacheCtl;
  }

  /**
   * Overridden method.
   *
   * @return action url
   * @throws java.lang.IllegalStateException exception
   * @see javax.portlet.MimeResponse#createActionURL()
   */
  public final PortletURL createActionURL() throws java.lang.IllegalStateException {

    if (getInput() instanceof ResourceInput)
      // throws java.lang.IllegalStateException:
      // "If the cacheability level of the resource URL triggering this
      // serveResource call is not PAGE and thus does not allow for creating
      // action URLs."
      // if Cacheability == PAGE, then create URL
      if (!ResourceURL.PAGE.equalsIgnoreCase(((ResourceInput) getInput()).getCacheability()))
        throw new IllegalStateException("Cannot create action URL from within serveResource() without PAGE Cacheability");

    if (getInput().getPortletURLFactory() != null)
      return getInput().getPortletURLFactory().createPortletURL(PCConstants.ACTION_STRING);

    return new PortletURLImp(PCConstants.ACTION_STRING, getInput().getBaseURL(), getInput()
        .getMarkup(), getPortletDatas().getSupports(), isCurrentlySecured(), getInput()
        .getEscapeXml(), getPortletDatas());
  }

  /**
   * Overridden method.
   *
   * @return resource url
   * @throws java.lang.IllegalStateException exception
   * @see javax.portlet.MimeResponse#createResourceURL()
   */
  public final ResourceURL createResourceURL() throws java.lang.IllegalStateException {

    // throws java.lang.IllegalStateException
    // if the cacheability level of the resource URL
    // triggering this serveResource call,
    // or one of the parent calls, have defined a stricter
    // cachability level.
    String originalCacheLevel = null;
    if (getInput() instanceof ResourceInput)
      originalCacheLevel = ((ResourceInput) getInput()).getCacheability();
    // OK, the originalCacheLevel is a parent cache level, what is new?
    if (getInput().getPortletURLFactory() != null)
      return getInput().getPortletURLFactory().createResourceURL(PCConstants.RESOURCE_STRING);

    return new ResourceURLImp(PCConstants.RESOURCE_STRING,
        getInput().getBaseURL(),
        isCurrentlySecured(),
        getInput().getEscapeXml(),
        originalCacheLevel,
        getPortletDatas(),
        getInput().getRenderParameters());
  }

}
