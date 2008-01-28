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
import java.util.Iterator;
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

  private Collection<String> supportedContents_;

  private String             contentType_;

  private boolean            writerAlreadyCalled_;

  private boolean            outputStreamAlreadyCalled_;

  private CacheControlImp    cacheCtl;

  private boolean            committed;

  public MimeResponseImp(ResponseContext resCtx) {
    super(resCtx);
    this.supportedContents_ = resCtx.getSupportedContents();
    this.writerAlreadyCalled_ = false;
    this.outputStreamAlreadyCalled_ = false;
    this.cacheCtl = new CacheControlImp(this);
    this.committed = false;
  }

  public String getContentType() {
    if (contentType_ == null || "".equals(contentType_))
      return null;
    return contentType_;
  }

  public void setContentType(String contentType) {
    if (committed)
      throw new IllegalStateException("the response has already been committed");
    if (contentType != null)
      contentType = StringUtils.split(contentType, ';')[0];

    if (!isContentTypeSupported(contentType)) {
      throw new IllegalArgumentException("the content type : " + contentType + " is not supported.");
    }
    this.contentType_ = contentType;
    if (getOutput() instanceof RenderOutput)
      ((RenderOutput) getOutput()).setContentType(this.contentType_);
    if (getOutput() instanceof ResourceOutput)
      ((ResourceOutput) getOutput()).setContentType(this.contentType_);

  }

  public OutputStream getPortletOutputStream() throws IOException {
    if (alreadyForwarded)
      throw new IllegalStateException("response has already been forwarded");
    if (writerAlreadyCalled_)
      throw new IllegalStateException("getWriter() has already been called");
    if (contentType_ == null || "".equals(contentType_))
      throw new IllegalStateException("the content type has not been set before calling the" + "getPortletOutputStream() method.");
    outputStreamAlreadyCalled_ = true;
    return super.getOutputStream();
  }

  public PrintWriter getWriter() throws IOException {
    if (alreadyForwarded)
      throw new IllegalStateException("response has already been forwarded");
    if (outputStreamAlreadyCalled_)
      throw new IllegalStateException("the getPortletOutputStream object has already been called");
    if (contentType_ == null || "".equals(contentType_))
      throw new IllegalStateException("the content type has not been set before calling the" + "getWriter() method.");
    writerAlreadyCalled_ = true;
    return super.getWriter();
  }

  private boolean isContentTypeSupported(String contentTypeToTest) {
    Collection<String> c = getResponseContentTypes();
    for (Iterator<String> iter = c.iterator(); iter.hasNext();) {
      String element = iter.next();
      if (element.equals(contentTypeToTest)) {
        return true;
      }
    }
    return false;
  }

  // TODO could be shared with PortletRequest.getResponseContentType()
  private Collection<String> getResponseContentTypes() {
    Collection<String> result = new ArrayList<String>();
    result.add(getResponseContentType());
    for (Iterator<String> iter = supportedContents_.iterator(); iter.hasNext();) {
      String element = iter.next();
      List<Supports> l = portletDatas_.getSupports();
      for (int i = 0; i < l.size(); i++) {
        Supports supportsType = l.get(i);
        String mimeType = supportsType.getMimeType();
        if (element.equals(mimeType) && !element.equals(input_.getMarkup())) {
          List<String> portletModes = supportsType.getPortletMode();
          for (Iterator<String> iter2 = portletModes.iterator(); iter2.hasNext();) {
            String portletMode = iter2.next();
            if (portletMode.equals(input_.getPortletMode().toString())) {
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
    List<Supports> l = portletDatas_.getSupports();
    for (int i = 0; i < l.size(); i++) {
      Supports supportsType = l.get(i);
      String mimeType = supportsType.getMimeType();
      if (mimeType.equals(input_.getMarkup())) {
        List<String> portletModes = supportsType.getPortletMode();
        for (Iterator<String> iter = portletModes.iterator(); iter.hasNext();) {
          String portletMode = iter.next();
          if (portletMode.equals(input_.getPortletMode().toString())) {
            return mimeType;
          }
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

  public void setBufferSize(int arg0) {
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

    if (input_ instanceof ResourceInput) {
      // throws java.lang.IllegalStateException:
      // "If the cacheability level of the resource URL triggering this
      // serveResource call is not PAGE and thus does not allow for creating
      // action URLs."
      // if Cacheability == PAGE, then create URL
      if (!ResourceURL.PAGE.equalsIgnoreCase(((ResourceInput) input_).getCacheability())) {
        throw new IllegalStateException("Cannot create action URL from within serveResource() without PAGE Cacheability");
      }
    }

    if (input_.getPortletURLFactory() != null)
      return input_.getPortletURLFactory().createPortletURL(PCConstants.actionString);

    return new PortletURLImp(PCConstants.actionString,
                             input_.getBaseURL(),
                             input_.getMarkup(),
                             portletDatas_.getSupports(),
                             isCurrentlySecured_,
                             input_.getEscapeXml(),
                             portletDatas_);
  }

  public ResourceURL createResourceURL() throws java.lang.IllegalStateException {

    // throws java.lang.IllegalStateException
    // if the cacheability level of the resource URL
    // triggering this serveResource call,
    // or one of the parent calls, have defined a stricter
    // cachability level.
    String originalCacheLevel = null;
    if (input_ instanceof ResourceInput) {
      originalCacheLevel = ((ResourceInput) input_).getCacheability();
      // TODO EXOMAN
      // OK, the originalCacheLevel is a parent cache level, what is new?
    }
    if (input_.getPortletURLFactory() != null) {
      return input_.getPortletURLFactory().createResourceURL(PCConstants.resourceString);
    }

      return new ResourceURLImp(PCConstants.resourceString,
          input_.getBaseURL(),
          isCurrentlySecured_,
          input_.getEscapeXml(),
          originalCacheLevel,
          portletDatas_,
          input_.getRenderParameters());
  }
}
