package org.exoplatform.services.wsrp2.producer.impl.helpers;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WSRPHTTPContainer extends HashMap<Object, Object> {

  private static ThreadLocal  threadLocal = new ThreadLocal();

  private HttpServletRequest  request;

  private HttpServletResponse response;

  public WSRPHttpServletRequest getRequest() {
    return (WSRPHttpServletRequest) request;
  }

  public WSRPHttpServletResponse getResponse() {
    return (WSRPHttpServletResponse) response;
  }

  public WSRPHTTPContainer(HttpServletRequest request, HttpServletResponse response) {
    this.request = new WSRPHttpServletRequest(request);
    this.response = new WSRPHttpServletResponse(request, response);
  }

  public static WSRPHTTPContainer getInstance() {
    return (WSRPHTTPContainer) threadLocal.get();
  }

  public static void setInstance(final WSRPHTTPContainer wsrpHTTPContainer) {
    threadLocal.set(wsrpHTTPContainer);
  }

  public static void createInstance(final HttpServletRequest request,
                                    final HttpServletResponse response) {
    threadLocal.set(new WSRPHTTPContainer(request, response));
  }

}
