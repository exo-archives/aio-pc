/**
 * Copyright 2001-2003 The eXo Platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 5:52:27 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.pci.*;
import org.exoplatform.services.portletcontainer.pci.model.*;


public class RenderResponseImp extends PortletResponseImp implements RenderResponse {
  private Enumeration supportedWindowState_;
  private Collection supportedContents_;
  private String windowId_;
  private Input input_;
  private Portlet portletDatas_;
  private boolean isCurrentlySecured_;
  private String contentType_;
  private boolean writerAlreadyCalled_;
  private boolean outputStreamAlreadyCalled_;

  public RenderResponseImp(ExoContainer cont, HttpServletResponse httpServletResponse) {
    super(cont, httpServletResponse);
  }

  public void fillRenderResponse(String windowId, Input input,
                                 Portlet portletDatas,
                                 boolean isCurrentlySecured,
                                 Collection supportedContents,
                                 Enumeration supportedWindowState) {
    this.windowId_ = windowId;
    this.input_ = input;
    this.portletDatas_ = portletDatas;
    this.isCurrentlySecured_ = isCurrentlySecured;
    this.supportedContents_ = supportedContents;
    this.supportedWindowState_ = supportedWindowState;
    writerAlreadyCalled_ = false;
    outputStreamAlreadyCalled_ = false;
  }

  public void emptyRenderResponse() {
    this.windowId_ = null;
    contentType_ = null;
    writerAlreadyCalled_ = false;
    outputStreamAlreadyCalled_ = false;
  }

  public String getContentType() {
    if(contentType_ == null || "".equals(contentType_))
      return null;
    return contentType_;
  }

  public void setContentType(String contentType) {
    if(contentType != null)
      contentType = StringUtils.split(contentType, ';')[0];

    if(!isContentTypeSupported(contentType)){
      throw new IllegalArgumentException("the content type : " + contentType
                                         + " is not supported.");
    }
    this.contentType_ = contentType;
  }

  public PortletURL createRenderURL() {
    if(input_.getPortletURLFactory() != null) {
      return input_.getPortletURLFactory().createPortletURL(PortletURLFactory.RENDER);
    }
    return new PortletURLImp(PortletURLFactory.RENDER, input_.getMarkup(),
                             portletDatas_.getSupports(),
                             isCurrentlySecured_, customWindowStates,
                             supportedWindowState_, input_.getBaseURL(),
                             input_.getWindowID());
  }

  public PortletURL createActionURL() {
    if(input_.getPortletURLFactory() != null) {
      return input_.getPortletURLFactory().createPortletURL(PortletURLFactory.ACTION);
    }
    return new PortletURLImp(PortletURLFactory.ACTION, input_.getMarkup(),
                             portletDatas_.getSupports(), isCurrentlySecured_,
                             customWindowStates, supportedWindowState_,
                             input_.getBaseURL(),input_.getWindowID());
  }

  public String getNamespace() {
    return (windowId_.replace('-', 'I') + "I").replace('/', 'I');
  }

  public void setTitle(String s) {
    ((RenderOutput) super.getOutput()).setTitle(s);
  }

  public OutputStream getPortletOutputStream() throws IOException {
    if(writerAlreadyCalled_)
      throw new IllegalStateException("getWriter() has already been called");
    if(contentType_ == null || "".equals(contentType_))
      throw new IllegalStateException("the content type has not been set before calling the" +
        "getPortletOutputStream() method.");
    outputStreamAlreadyCalled_ = true;
    return super.getOutputStream();
  }

  public PrintWriter getWriter() throws IOException {
    if(outputStreamAlreadyCalled_)
      throw new IllegalStateException("the getPortletOutputStream object has already been called");
    if(contentType_ == null || "".equals(contentType_))
      throw new IllegalStateException("the content type has not been set before calling the" +
        "getWriter() method.");
    writerAlreadyCalled_ = true;
    return super.getWriter();
  }

  private boolean isContentTypeSupported(String contentTypeToTest){
    Collection c = getResponseContentTypes();
    for (Iterator iter = c.iterator(); iter.hasNext();) {
      String element = (String) iter.next();
      if(element.equals(contentTypeToTest)){
        return true;
      }
    }
    return false;
  }

  // TODO could be shared with PortletRequest.getResponseContentType()
  private Collection getResponseContentTypes() {
    Collection c = new ArrayList();
    c.add(getResponseContentType());
    for (Iterator iter = supportedContents_.iterator(); iter.hasNext();) {
      String element = (String) iter.next();
      List l = portletDatas_.getSupports();
      for (int i = 0; i < l.size(); i++) {
        Supports supportsType = (Supports) l.get(i);
        String mimeType = supportsType.getMimeType();
        if(element.equals(mimeType) && !element.equals(input_.getMarkup())){
          List portletModes = supportsType.getPortletMode();
          for (Iterator iter2 = portletModes.iterator(); iter2.hasNext();) {
            String portletMode = (String) iter2.next();
            if(portletMode.equals(input_.getPortletMode().toString())){
              c.add(mimeType);
              break;
            }
          }
        }
      }
    }
    return c;
  }

  private String getResponseContentType() {
    List l = portletDatas_.getSupports();
    for (int i = 0; i < l.size(); i++) {
      Supports supportsType = (Supports) l.get(i);
      String mimeType = supportsType.getMimeType();
      if (mimeType.equals(input_.getMarkup())){
        List portletModes = supportsType.getPortletMode();
        for (Iterator iter = portletModes.iterator(); iter.hasNext();) {
          String portletMode = (String) iter.next();
          if(portletMode.equals(input_.getPortletMode().toString())){
            return mimeType;
          }
        }
      }
    }
    return "text/html";
  }


  public void flushBuffer() throws IOException {
    return;
  }

  public int getBufferSize() {
    return 0;
  }

  public boolean isCommitted() {
    // made for TCK com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.ResponseMethodsMiscMiscTestPortlet on weblogic 9.2
    // was: return true;
    return false;
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
    if(l == null)
      return new Locale("en");
    return l;
  }

}
