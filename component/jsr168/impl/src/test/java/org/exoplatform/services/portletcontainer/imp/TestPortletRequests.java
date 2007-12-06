/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.portletcontainer.imp;

import javax.portlet.PortletMode;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.Constants;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool.EmptyResponse;
import org.exoplatform.services.portletcontainer.pci.*;
import org.exoplatform.test.mocks.servlet.MockHttpSession;
import org.exoplatform.test.mocks.servlet.MockServletRequest;
import org.exoplatform.test.mocks.servlet.MockServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: 16 nov. 2003 Time: 16:39:59
 */
public class TestPortletRequests extends BaseTest {

  public TestPortletRequests(String s) {
    super(s);
  }

  /**
   * test (xlviii) : If a portlet receives a request from a client request
   * targeted to the portlet itself, the parameters must be the string
   * parameters encoded in the URL (added when creating the PortletURL) and the
   * string parameters sent by the client to the portlet as part of the client
   * request
   * 
   * PLT.11.1.1
   */
  public void testIncomingRequestParameter() throws PortletContainerException {
    MockServletRequest request = new MockServletRequest(new MockHttpSession(),
        Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    request.setParameter("param1", "param-value1");
    request.setParameter("param2", "param-value2");
    request.setParameter("param3", "param-value3");
    ((ExoWindowID) actionInput.getWindowID())
        .setPortletName("PortletToTestIncomingRequestParam");
    actionInput.setPortletMode(new PortletMode("config"));
    ActionOutput aO = portletContainer.processAction(request, response,
        actionInput);
    assertEquals("Everything is ok", ((String[]) aO.getRenderParameters().get(
        "status"))[0]);
    Map renderMap = new HashMap();
    renderMap.put("renderParam1", "param-value1");
    renderMap.put("renderParam2", "param-value2");
    renderMap.put("renderParam3", "param-value3");
    ((ExoWindowID) input.getWindowID())
        .setPortletName("PortletToTestIncomingRequestParam");
    input.setPortletMode(PortletMode.HELP);
    input.setRenderParameters(renderMap);
    RenderOutput o = portletContainer.render(request, response, input);
    assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
  }

  /**
   * test (xlix) : The parameters the request object returns must be
   * "x-www-formurlencoded" decoded.
   * 
   * PLT.11.1.1
   */
  public void testParameterDecoding() throws PortletContainerException {
    MockServletRequest request = new MockServletRequest(new MockHttpSession(),
        Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    Map renderMap = new HashMap();
    renderMap.put("renderParam", "param value non url encoded");
    ((ExoWindowID) input.getWindowID())
        .setPortletName("PortletToTestParameterNonEncoding");
    input.setPortletMode(PortletMode.HELP);
    input.setRenderParameters(renderMap);
    RenderOutput o = portletContainer.render(request, response, input);
    assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
  }

  /**
   * test (l) : The portlet-container must not propagate parameters received in
   * an action request to subsequent render requests of the portlet.
   * 
   * PLT.11.1.1
   */
  public void testNonPropagationToRenderOfParametersReceivedInAction()
      throws PortletContainerException {
    MockServletRequest request = new MockServletRequest(new MockHttpSession(),
        Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    request.setParameter("param", "param-value");
    ((ExoWindowID) actionInput.getWindowID())
        .setPortletName("PortletToTestNonPropagationOfActionParamToRender");
    actionInput.setPortletMode(new PortletMode("config"));
    ActionOutput aO = portletContainer.processAction(request, response,
        actionInput);
    assertEquals("Everything is ok", ((String[]) aO.getRenderParameters().get(
        "status"))[0]);
    ((ExoWindowID) input.getWindowID())
        .setPortletName("PortletToTestNonPropagationOfActionParamToRender");
    input.setPortletMode(PortletMode.HELP);
    RenderOutput o = portletContainer.render(request, response, input);
    assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
  }

  /**
   * test (lv) : The value returned from the getParameter method must be the
   * first value in the array of String objects returned by getParameterValues
   * 
   * PLT.11.1.1
   */
  public void testGetParameterXXMethods() throws PortletContainerException {
    MockServletRequest request = new MockServletRequest(new MockHttpSession(),
        Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID) actionInput.getWindowID())
        .setPortletName("PortletToTestGetParameterXXXMethods");
    actionInput.setPortletMode(new PortletMode("config"));
    ActionOutput aO = portletContainer.processAction(request, response,
        actionInput);
    assertEquals("Everything is ok", ((String[]) aO.getRenderParameters().get(
        "status"))[0]);
    Map renderMap = new HashMap();
    renderMap.put("renderParam1", new String[] { "param-value1",
        "param-value1bis" });
    renderMap.put("renderParam2", new String[] { "param-value2" });
    ((ExoWindowID) input.getWindowID())
        .setPortletName("PortletToTestGetParameterXXXMethods");
    input.setPortletMode(PortletMode.HELP);
    input.setRenderParameters(renderMap);
    RenderOutput o = portletContainer.render(request, response, input);
    assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
  }

  /**
   * test (lvii) : Extra parameters used by the portal/portlet-container must be
   * invisible to the portlets receiving the request.
   * 
   * PLT.11.1.2
   */
  public void testInvisibilityOfExtraParameters()
      throws PortletContainerException {
    MockServletRequest request = new MockServletRequest(new MockHttpSession(),
        Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    Map renderMap = new HashMap();
    renderMap.put(Constants.PARAMETER_ENCODER + "renderParam1", "param-value1");
    ((ExoWindowID) input.getWindowID())
        .setPortletName("PortletToTestInvisibilityOfExtraParameters");
    input.setPortletMode(PortletMode.HELP);
    input.setRenderParameters(renderMap);
    RenderOutput o = portletContainer.render(request, response, input);
    assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
  }

  /**
   * test : A portlet can access portal/portlet-container specific properties
   * and, if available, the headers of the HTTP client request through the
   * following methods of the methods of the PortletRequest interface: �
   * getProperty � getProperties � getPropertyNames
   * 
   * PLT.11.1.4
   */
  public void testHeadersFromProperty() throws PortletContainerException {
    MockServletRequest request = new MockServletRequest(new MockHttpSession(),
        Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID) actionInput.getWindowID())
        .setPortletName("PortletToTestAccessToHeaders");
    actionInput.setPortletMode(new PortletMode("config"));
    ActionOutput aO = portletContainer.processAction(request, response,
        actionInput);
    assertEquals("Everything is ok", ((String[]) aO.getRenderParameters().get(
        "status"))[0]);
  }

  /**
   * test : The getAuthType indicates the authentication scheme being used
   * between the user and the portal. It may return one of the defined constants
   * (BASIC_AUTH, DIGEST_AUTH, CERT_AUTH and FORM_AUTH) or another String value
   * that represents a vendor provided authentication type. If the user is not
   * authenticated the getAuthType method must return null. (lx)
   * 
   * The getRemoteUser method returns the login name of the user making this
   * request.
   * 
   * The getUserPrincipal method returns a java.security.Principal object
   * containing the name of the authenticated user.
   * 
   * The isUserInRole method indicates if an authenticated user is included in
   * the specified logical role.
   * 
   * The isSecure method indicates if the request has been transmitted over a
   * secure protocol such as HTTPS.
   * 
   * PLT.11.1.6
   */
  public void testSecurityMethods() throws PortletContainerException {
    MockServletRequest request = new MockServletRequest(new MockHttpSession(),
        Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID) actionInput.getWindowID())
        .setPortletName("PortletToTestSecurityInfoFromRequest");
    actionInput.setPortletMode(new PortletMode("config"));
    ActionOutput aO = portletContainer.processAction(request, response,
        actionInput);
    assertEquals("Everything is ok", ((String[]) aO.getRenderParameters().get(
        "status"))[0]);
  }

  /**
   * test : Portlet developers may code portlets to support multiple content
   * types. A portlet can obtain, using the getResponseContentType method of the
   * request object, a string representing the default content type the portlet
   * container assumes for the output.
   * 
   * The returned Enumeration of strings should contain the content types the
   * portlet container supports in order of preference.
   * 
   * test (lxi) : The first element of the enumeration must be the same content
   * type returned by the getResponseContentType method.
   * 
   * test (lxii) : The getResponseContentTypes method must return only the
   * content types supported by the current portlet mode of the portlet.
   *  
   */
  public void testResponseContentTypesMethods()
      throws PortletContainerException {
    MockServletRequest request = new MockServletRequest(new MockHttpSession(),
        Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID) actionInput.getWindowID())
        .setPortletName("PortletToTestResponseContentTypeMethods");
    actionInput.setPortletMode(new PortletMode("config"));
    actionInput.setMarkup("text/wml");
    ActionOutput aO = portletContainer.processAction(request, response,
        actionInput);
    assertEquals("Everything is ok", ((String[]) aO.getRenderParameters().get(
        "status"))[0]);
    Map renderMap = new HashMap();
    ((ExoWindowID) input.getWindowID())
        .setPortletName("PortletToTestResponseContentTypeMethods");
    input.setPortletMode(PortletMode.HELP);
    input.setRenderParameters(renderMap);
    RenderOutput o = portletContainer.render(request, response, input);
    assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
  }

  public void testGetTitle() throws PortletContainerException {
    MockServletRequest request = new MockServletRequest(new MockHttpSession(),
        Locale.US, true);
    HttpServletResponse response = new MockServletResponse(new EmptyResponse());
    ((ExoWindowID) input.getWindowID())
        .setPortletName("PortletToTestGetTitle");
    RenderOutput o = portletContainer.render(request, response, input);
    assertTrue(new String(o.getContent()).startsWith("Everything is ok"));
  }

}