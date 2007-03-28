/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.consumer.impl;

import org.exoplatform.Constants;
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.consumer.URLTemplateComposer;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 6 févr. 2004
 * Time: 13:05:01
 */

public class URLTemplateComposerImpl implements URLTemplateComposer{

  private static final String SECURE_PROTOCOL = "https://";
  private static final String NON_SECURE_PROTOCOL = "http://";
  private String host = "localhost";
  private int port = 8080;

  public void setHost(String host) {
    this.host = host;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String createBlockingActionTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    sB.append(NON_SECURE_PROTOCOL);
    manageServerPath(sB, path);
    appendParameters(sB);
    return sB.toString();
  }

  public String createSecureBlockingActionTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    sB.append(SECURE_PROTOCOL);
    manageServerPath(sB, path);
    appendParameters(sB);
    return sB.toString();
  }

  public String createRenderTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    sB.append(NON_SECURE_PROTOCOL);
    manageServerPath(sB, path);
    appendParameters(sB);
    return sB.toString();
  }

  public String createSecureRenderTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    sB.append(SECURE_PROTOCOL);
    manageServerPath(sB, path);
    appendParameters(sB);
    return sB.toString();
  }

  public String createResourceTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    sB.append(NON_SECURE_PROTOCOL);
    manageServerPath(sB, path);
    appendParameters(sB);
    return sB.toString();
  }

  public String createSecureResourceTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    sB.append(SECURE_PROTOCOL);
    manageServerPath(sB, path);
    appendParameters(sB);
    return sB.toString();
  }

  public String createDefaultTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    sB.append(NON_SECURE_PROTOCOL);
    manageServerPath(sB, path);
    appendParameters(sB);
    return sB.toString();
  }

  public String createSecureDefaultTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    sB.append(SECURE_PROTOCOL);
    manageServerPath(sB, path);
    appendParameters(sB);
    return sB.toString();
  }

  public String getNamespacePrefix() {
    return "";
  }

  private void manageServerPath(StringBuffer stringBuffer, String path) {
    stringBuffer.append(host);
    if(port > 0)
      stringBuffer.append(":").append(port);
    stringBuffer.append(path);
  }

  private void appendParameters(StringBuffer stringBuffer){
    stringBuffer.append("&" + Constants.PARAMETER_ENCODER + "type={" + WSRPConstants.WSRP_URL_TYPE + "}" );
    stringBuffer.append("&" + Constants.PARAMETER_ENCODER + "mode={" + WSRPConstants.WSRP_MODE + "}" );
    stringBuffer.append("&" + Constants.PARAMETER_ENCODER + "windowState={" + WSRPConstants.WSRP_WINDOW_STATE + "}" );
    stringBuffer.append("&" + Constants.PARAMETER_ENCODER + "isSecure={" + WSRPConstants.WSRP_SECURE_URL + "}" );

    stringBuffer.append("&" + WSRPConstants.WSRP_PORTLET_HANDLE + "={"
        + WSRPConstants.WSRP_PORTLET_HANDLE  + "}" );
    stringBuffer.append("&" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "={"
        + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY  + "}" );
    stringBuffer.append("&" + WSRPConstants.WSRP_NAVIGATIONAL_STATE + "={"
        + WSRPConstants.WSRP_NAVIGATIONAL_STATE  + "}" );
    stringBuffer.append("&" + WSRPConstants.WSRP_SESSION_ID + "={"
        + WSRPConstants.WSRP_SESSION_ID  + "}" );
    stringBuffer.append("&" + WSRPConstants.WSRP_USER_CONTEXT_KEY + "={"
        + WSRPConstants.WSRP_USER_CONTEXT_KEY  + "}" );
    stringBuffer.append("&" + WSRPConstants.WSRP_URL + "={"
        + WSRPConstants.WSRP_URL  + "}" );
    stringBuffer.append("&" + WSRPConstants.WSRP_REQUIRES_REWRITE + "={"
        + WSRPConstants.WSRP_REQUIRES_REWRITE  + "}" );
    stringBuffer.append("&" + WSRPConstants.WSRP_INTERACTION_STATE + "={"
        + WSRPConstants.WSRP_INTERACTION_STATE  + "}" );
    stringBuffer.append("&" + WSRPConstants.WSRP_FRAGMENT_ID + "={"
        + WSRPConstants.WSRP_FRAGMENT_ID  + "}" );
  }

}