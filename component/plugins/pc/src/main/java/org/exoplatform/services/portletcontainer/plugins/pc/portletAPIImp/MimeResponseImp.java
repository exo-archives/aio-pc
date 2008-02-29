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
 * Author : Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua 25.05.2007
 */
public class MimeResponseImp extends PortletResponseImp implements MimeResponse {

  private final Collection<String> supportedContents_;

  private String contentType_;

  private boolean writerAlreadyCalled_;

  private boolean outputStreamAlreadyCalled_;

  private final CacheControlImp cacheCtl;

  private boolean committed;

  public MimeResponseImp(final ResponseContext resCtx) {
    super(resCtx);
    this.supportedContents_ = resCtx.getSupportedContents();
    this.writerAlreadyCalled_ = false;
    this.outputStreamAlreadyCalled_ = false;
    this.cacheCtl = new CacheControlImp(this);
    this.committed = false;
  }

  public String getContentType() {
    if ((contentType_ == null) || "".equals(contentType_))
      return null;
    return contentType_;
  }

  public void setContentType(String contentType) {
    if (committed)
      throw new IllegalStateException("the response has already been committed");
    if (contentType != null)
      contentType = StringUtils.split(contentType, ';')[0];

    if (!isContentTypeSupported(contentType))
      throw new IllegalArgumentException("the content type : " + contentType + " is not supported.");
    this.contentType_ = contentType;
    if (getOutput() instanceof RenderOutput)
      ((RenderOutput) getOutput()).setContentType(this.contentType_);
    if (getOutput() instanceof ResourceOutput)
      ((ResourceOutput) getOutput()).setContentType(this.contentType_);

  }

  public OutputStream getPortletOutputStream() throws IOException {
    if (isAlreadyForwarded())
      throw new IllegalStateException("response has already been forwarded");
    if (writerAlreadyCalled_)
      throw new IllegalStateException("getWriter() has already been called");
    if ((contentType_ == null) || "".equals(contentType_))
      throw new IllegalStateException("the content type has not been set before calling the"
          + "getPortletOutputStream() method.");
    outputStreamAlreadyCalled_ = true;
    return super.getOutputStream();
  }

  public PrintWriter getWriter() throws IOException {
    if (isAlreadyForwarded())
      throw new IllegalStateException("response has already been forwarded");
    if (outputStreamAlreadyCalled_)
      throw new IllegalStateException("the getPortletOutputStream object has already been called");
    if ((contentType_ == null) || "".equals(contentType_))
      throw new IllegalStateException("the content type has not been set before calling the"
          + "getWriter() method.");
    writerAlreadyCalled_ = true;
    return super.getWriter();
  }

  private boolean isContentTypeSupported(final String contentTypeToTest) {
    Collection<String> c = getResponseContentTypes();
    for (String element : c) {
      if (element.equals(contentTypeToTest))
        return true;
    }
    return false;
  }

  // TODO could be shared with PortletRequest.getResponseContentType()
  private Collection<String> getResponseContentTypes() {
    Collection<String> result = new ArrayList<String>();
    result.add(getResponseContentType());
    for (String element : supportedContents_) {
      List<Supports> l = getPortletDatas().getSupports();
      for (int i = 0; i < l.size(); i++) {
        Supports supportsType = l.get(i);
        String mimeType = supportsType.getMimeType();
        if (element.equals(mimeType) && !element.equals(getInput().getMarkup())) {
          List<String> portletModes = supportsType.getPortletMode();
          for (String portletMode : portletModes) {
            if (portletMode.equals(getInput().getPortletMode().toString())) {
              result.add(mimeType);
              break;
            }
          }
        }
      }
    }
    return result;
  }

  private String getResponseContentType() {
    List<Supports> l = getPortletDatas().getSupports();
    for (int i = 0; i < l.size(); i++) {
      Supports supportsType = l.get(i);
      String mimeType = supportsType.getMimeType();
      if (mimeType.equals(getInput().getMarkup())) {
        List<String> portletModes = supportsType.getPortletMode();
        for (String portletMode : portletModes) {
          if (portletMode.equals(getInput().getPortletMode().toString()))
            return mimeType;
        }
      }
    }
    return PCConstants.XHTML_MIME_TYPE;
  }

  public void flushBuffer() throws IOException {
    committed = true;
    return;
  }

  public int getBufferSize() {
    return 0;
  }

  public boolean isCommitted() {
    // made for TCK
    // com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsMiscMiscTestPortlet
    // on weblogic 9.2
    // was: return true;
    return committed;
  }

  public void reset() {
    return;
  }

  public void resetBuffer() {
    return;
  }

  public void setBufferSize(final int arg0) {
    return;
  }

  public Locale getLocale() {
    Locale l = super.getLocale();
    if (l == null)
      return new Locale("en");
    return l;
  }

  public CacheControl getCacheControl() {
    return cacheCtl;
  }

  public PortletURL createActionURL() throws java.lang.IllegalStateException {

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

    return new PortletURLImp(PCConstants.ACTION_STRING,
        getInput().getBaseURL(),
        getInput().getMarkup(),
        getPortletDatas().getSupports(),
        isCurrentlySecured(),
        getInput().getEscapeXml(),
        getPortletDatas());
  }

  public ResourceURL createResourceURL() throws java.lang.IllegalStateException {

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
