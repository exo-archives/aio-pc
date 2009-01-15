package org.exoplatform.services.wsrp2.producer.impl.helpers;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WSRPHTTPContainer extends HashMap<Object, Object> {

  private static ThreadLocal<WSRPHTTPContainer> threadLocal = new ThreadLocal<WSRPHTTPContainer>();

  private HttpServletRequest                    request;

  private HttpServletResponse                   response;

  private int                                   version;

  public WSRPHTTPContainer(HttpServletRequest request, HttpServletResponse response) {
    this.request = new WSRPHttpServletRequest(request);
    this.response = new WSRPHttpServletResponse(request, response);
    this.version = 2;
  }

  public static WSRPHTTPContainer getInstance() {
    return (WSRPHTTPContainer) threadLocal.get();
  }

  public static void removeInstance() {
    threadLocal.set(null);
  }

  public static void createInstance(final HttpServletRequest request,
                                    final HttpServletResponse response) {
    threadLocal.set(new WSRPHTTPContainer(request, response));
  }

  public WSRPHttpServletRequest getRequest() {
    return (WSRPHttpServletRequest) request;
  }

  public WSRPHttpServletResponse getResponse() {
    return (WSRPHttpServletResponse) response;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

}
