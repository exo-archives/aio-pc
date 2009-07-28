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
package org.exoplatform.services.portletcontainer.test.portlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;

import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletContextImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletSessionImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers.CustomRequestWrapper;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers.CustomResponseWrapper;

/**
 * Created by The eXo Platform SAS  .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */
public class BridgePortlet extends GenericPortlet {

  /**
   * Context path.
   */
  private String contextPath;

  /**
   * Default page to include.
   */
  private String defaultPage;

  /**
   * Owner's URI.
   */
  private String ownerUri;

  /**
   * Parameter prefix.
   */
  public static final String PARAM_PREFIX = "portlet:";

  /**
   * Overridden method.
   *
   * @param config portlet config
   * @throws PortletException something may go wrong
   * @see javax.portlet.GenericPortlet#init(javax.portlet.PortletConfig)
   */
  public void init(PortletConfig config) throws PortletException {
    super.init(config);
    contextPath = config.getPortletContext().getPortletContextName();
    defaultPage = config.getInitParameter("default-page");
    ownerUri = contextPath;
  }

  protected void doView(RenderRequest request, RenderResponse response)
      throws PortletException, IOException {
    //wrap request & response
    CustomRequestWrapper requestWrapper = (CustomRequestWrapper)
    ((HttpServletRequestWrapper)request).getRequest();
    CustomResponseWrapper responseWrapper = (CustomResponseWrapper)
    ((HttpServletResponseWrapper)response).getResponse();

    boolean isSharedSessionEnable = false;
    try {
      //read contexts
      PortletContextImpl portletContext = (PortletContextImpl) getPortletContext();
      ServletContext scontext = portletContext.getWrappedServletContext();

      //isSharedSessionEnable = context.isSessionShared();

      //get real request url
      String requestUrl = getRequestURL(requestWrapper);
      //get request dispatcher
      RequestDispatcher dispatcher = null;
      dispatcher = scontext.getRequestDispatcher(requestUrl) ;
      //change request settings
      requestWrapper.setRedirected(true);
      requestWrapper.setRedirectedPath(requestUrl);
      requestWrapper.servletPath = requestUrl;
      requestWrapper.pathInfo = "";
      requestWrapper.contextPath = "/"+contextPath;
      if(isSharedSessionEnable){
        PortletSessionImp pS = (PortletSessionImp)request.getPortletSession();
        requestWrapper.setContextPath(request.getContextPath());
      }
      //!!!!!!!!!!Dispatch request !!!!!!!!!!!!!!!!!!!!!!
      dispatcher.include(requestWrapper, responseWrapper);
      //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

      //read output
      boolean isstream = responseWrapper.isStreamUsed();
      InputStream responseAsInputStream = new ByteArrayInputStream(responseWrapper.getPortletContent());
	    
      ByteArrayOutputStream  byteOutput = new ByteArrayOutputStream();
      //set transform params
      PortletOutputTransformer rewriter  = new PortletOutputTransformer();
      rewriter.setPortalURI(getPortalBaseURI());
      rewriter.setPortletURI(getPorletBaseURI(requestWrapper));
      rewriter.setPortalContextPath(requestWrapper.getContextPath());
      rewriter.setPortalQueryString("");
      rewriter.setParamNamespace(getPortletBasedParamName(""));

      //transform output
      rewriter.rewrite(responseAsInputStream,byteOutput);
      //reset response
      responseWrapper.reset();
      //convert output of transformation to String
      //result of transformation is in "UTF-8"
      Reader reader = new InputStreamReader( new ByteArrayInputStream(byteOutput.toByteArray()),"UTF-8" );
      ByteArrayInputStream bais = new ByteArrayInputStream(byteOutput.toByteArray());

      int readed;

      if (isstream) {
        // getPortletOutputStream
        if (response.getContentType() == null) response.setContentType("text/html; charset=UTF-8");
        OutputStream w = response.getPortletOutputStream();

        // --- simple navigation bar ---------
        w.write("<table width=\"100%\"><tr><td>".getBytes());
        PortletURL renderURL = response.createRenderURL();
        renderURL.setParameter(getPortletBasedParamName("url"), "/test");
        w.write(new String("<p><a href=\"" + renderURL.toString() + "\">Embed servlet</a> | ").getBytes());
        renderURL = response.createRenderURL();
        renderURL.setParameter(getPortletBasedParamName("url"), "/test.jsp");
        w.write(new String("<a href=\"" + renderURL.toString() + "\">Embed JSP</a></p>").getBytes());
        w.write("</td></tr><tr><td>".getBytes());
        // ------------
        //write transformed output to response
        byte[] but = new byte[1024];
        while ((readed = bais.read(but)) > 0 ){
          w.write(but,0,readed);
        }
        w.write("</td></tr></table>".getBytes());
      } else {
        // getWriter
        if (response.getContentType() == null) response.setContentType("text/html; charset=UTF-8");
        PrintWriter w = response.getWriter();

        // --- simple navigation bar ---------
        w.println("<table width=\"100%\"><tr><td>");
        PortletURL renderURL = response.createRenderURL();
        renderURL.setParameter(getPortletBasedParamName("url"), "/test");
        w.println("<p><a href=\"" + renderURL.toString() + "\">Embed servlet</a> | ");
        renderURL = response.createRenderURL();
        renderURL.setParameter(getPortletBasedParamName("url"), "/test.jsp");
        w.println("<a href=\"" + renderURL.toString() + "\">Embed JSP</a></p>");

        w.println("</td></tr><tr><td>");
        // ------------
        //write transformed output to response
        char[] buf = new char[1024];
        while ((readed = reader.read(buf)) > 0 ){
          w.write(buf,0,readed);
        }
        w.println("</td></tr></table>");
      }


    } catch (Exception e) {
      throw new PortletException("Problems occur when using PortletDispatcher", e);
    } finally {
      if (requestWrapper != null)
        requestWrapper.setRedirected(false);
    }
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException,
    IOException { }

  private String getPortletBasedParamName(String shortName) {
    return PARAM_PREFIX + contextPath + ":" + shortName;
  }

  private String getRequestURL(HttpServletRequest request) {
    String paramURL = getPortletBasedParamName("url");
    String result = request.getParameter(paramURL);
    result = result == null ? defaultPage : result;
    return result;
  }

  private String getPorletBaseURI(HttpServletRequest request) {
    String result = getRequestURL(request);

    if (result.indexOf('/') != -1) {
      result = result.substring(0,result.lastIndexOf('/'));
    }
    return result;
  }

  //URI (path + query string) of current string
  private String getPortalBaseURI() {
    String path = ownerUri;
    return path;
  }
}
