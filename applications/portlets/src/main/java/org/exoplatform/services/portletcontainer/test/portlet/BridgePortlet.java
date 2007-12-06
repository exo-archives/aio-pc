/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.portletcontainer.test.portlet;

import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.portlet.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.portletcontainer.ExoPortletContext;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletContextImpl;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletSessionImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.CustomRequestWrapper;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.CustomResponseWrapper;

/**
 * Created by The eXo Platform SARL .
 * 
 * @author <a href="mailto:lautarul@gmail.com">Roman Pedchenko</a>
 * @version $Id$
 */

public class BridgePortlet extends GenericPortlet {

  private String contextPath;
  protected String defaultPage;
  private String ownerUri;

  public static String PARAM_PREFIX = "portlet:";

  public void init(PortletConfig config) throws PortletException {
      super.init(config);

      contextPath = config.getPortletContext().getPortletContextName();
      defaultPage = config.getInitParameter("default-page");
//System.out.println(" --- config.getInitParameter('default-page') [" + defaultPage + "]");
for (Enumeration e = config.getInitParameterNames(); e.hasMoreElements();) {
String n = (String) e.nextElement();
//System.out.println(" --- init - " + n + ": " + config.getInitParameter(n));
}
      ownerUri = contextPath;
  }

  protected void doView(RenderRequest request, RenderResponse response)
      throws PortletException, IOException {
    //wrap request & response
    CustomRequestWrapper requestWrapper = (CustomRequestWrapper)
                     ((HttpServletRequestWrapper)request).getRequest();
    CustomResponseWrapper responseWrapper =
            (CustomResponseWrapper) ((HttpServletResponseWrapper)response).getResponse();

    boolean isSharedSessionEnable = false;

    try {
      //read contexts
      PortletContextImpl portletContext = (PortletContextImpl) getPortletContext();
      ServletContext scontext = portletContext.getWrappedServletContext();
      ExoPortletContext context = (ExoPortletContext) request.getPortletSession().
                                  getPortletContext();
      isSharedSessionEnable = context.isSessionShared();

      //get real request url
      String requestUrl = getRequestURL(requestWrapper);

      //get request dispatcher
      RequestDispatcher dispatcher = null;
      dispatcher = scontext.getRequestDispatcher(requestUrl) ;

      //change request settings
      requestWrapper.setRedirected(true);
      requestWrapper.setRedirectedPath(
            requestUrl);
      requestWrapper.servletPath = requestUrl;
      requestWrapper.pathInfo = "";
      requestWrapper.contextPath = "/"+contextPath;
//      requestWrapper.servletPath = "/";
//      requestWrapper.pathInfo = requestUrl;
//            requestUrl);
      if(isSharedSessionEnable){
        PortletSessionImp pS = (PortletSessionImp)request.getPortletSession();
        requestWrapper.setSharedSession(pS.getSession());
        requestWrapper.setContextPath(request.getContextPath());
      }


//System.out.println(" --- before include ---");
      //!!!!!!!!!!Dispatch request !!!!!!!!!!!!!!!!!!!!!!
      dispatcher.include(requestWrapper, responseWrapper);
      //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//System.out.println(" --- after include ---");

      //read output
      InputStream responseAsInputStream;
      try{
        responseWrapper.getWriter();
        responseAsInputStream = new ByteArrayInputStream(
                      String.valueOf(responseWrapper.getPortletContent()).
                      getBytes());

      }
      catch (IllegalStateException ex){
        //if output writed to outputStream


        responseAsInputStream = new ByteArrayInputStream(responseWrapper.toByteArray());
        //reset response wrapper
        responseWrapper.fillResponseWrapper(
            (HttpServletResponse) responseWrapper.getResponse());
      }

      ByteArrayOutputStream  byteOutput = new ByteArrayOutputStream();

      //set transform params
      PortletOutputTransformer rewriter  = new PortletOutputTransformer();
      rewriter.portalURI = getPortalBaseURI();
      rewriter.portletURI = getPorletBaseURI(requestWrapper);
      rewriter.portalContextPath = requestWrapper.getContextPath();
      rewriter.portalQueryString = "";
      rewriter.paramNamespace = getPortletBasedParamName("");

      //transform output
      rewriter.rewrite(responseAsInputStream,byteOutput);

      //reset response
      responseWrapper.reset();

      //convert output of transformation to String
      //result of transformation is in "UTF-8"
      java.io.Reader reader = new java.io.InputStreamReader(
                  new ByteArrayInputStream(byteOutput.toByteArray()),"UTF-8");

      char[] buf = new char[1024];
      int readed;

      PrintWriter w = responseWrapper.getWriter();
      
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
      while ((readed = reader.read(buf)) > 0 ){
          w.write(buf,0,readed);
      }
      w.println("</td></tr></table>");

    }

    catch (Exception e) {
      throw new PortletException("Problems occur when using PortletDispatcher", e);
    } finally {
      if(requestWrapper != null)
        requestWrapper.setRedirected(false);
      if(isSharedSessionEnable){
        requestWrapper.setSharedSession(null);
      }

    }
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
  }

  private String getPortletBasedParamName(String shortName){
      return PARAM_PREFIX + contextPath + ":" + shortName;
  }

  private String getRequestURL(HttpServletRequest request){
      String paramURL = getPortletBasedParamName("url");
      String result = request.getParameter(paramURL);
      result = result == null ? defaultPage : result;

//System.out.println(" --- request.getParameter('"+paramURL+"') ["+result+"]");
      return result;
  }

  private String getPorletBaseURI(HttpServletRequest request) {
      String result = getRequestURL(request);

      if (result.indexOf('/') != -1) {
          result = result.substring(0,result.lastIndexOf('/'));
      }


//System.out.println("------------ begin request info --------------");
//System.out.println("ownerUri "+ ownerUri);
//System.out.println("request.getPathInfo() " + request.getPathInfo());
//System.out.println("request.getServletPath() "+ request.getServletPath());
//System.out.println("request.getQueryString() "+ request.getQueryString());
//System.out.println("request.getContextPath() "+ request.getContextPath());
//System.out.println("------------ end request info --------------");

      return result;
  }

  //URI (path + query string) of current string
  private String getPortalBaseURI(){
      String path = ownerUri;
      return path;
  }
}
