/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp2.urls1;

import java.net.MalformedURLException;
import java.net.URL;

import org.exoplatform.Constants;
import org.exoplatform.services.wsrp2.consumer.URLTemplateComposer;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 6 févr. 2004
 * Time: 13:05:01
 */

public class URLTemplateComposerImpl implements URLTemplateComposer {

  public static final String SECURE_PROTOCOL     = "https://";

  public static final String NON_SECURE_PROTOCOL = "http://";

  private String             host                = "localhost";

  private int                port                = 8080;

  public void setHost(String host) {
    this.host = host;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String createBlockingActionTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, false);
    appendParameters(sB);
    return sB.toString();
  }

  public String createSecureBlockingActionTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, true);
    appendParameters(sB);
    return sB.toString();
  }

  public String createRenderTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, false);
    appendParameters(sB);
    return sB.toString();
  }

  public String createSecureRenderTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, true);
    appendParameters(sB);
    return sB.toString();
  }

  public String createResourceTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, false);
    appendParameters(sB);
    return sB.toString();
  }

  public String createSecureResourceTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, true);
    appendParameters(sB);
    return sB.toString();
  }

  public String createDefaultTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, false);
    appendParameters(sB);
    return sB.toString();
  }

  public String createSecureDefaultTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, true);
    appendParameters(sB);
    return sB.toString();
  }

  public String getNamespacePrefix() {
    return "";
  }

  private void manageServerPath(StringBuffer sB, String path, Boolean isSecure) {
//    manageServerProtocol(sB,isSecure);
    sB.append(path);
  }

  private void appendParameters(StringBuffer stringBuffer) {
    // JSR-168 parameters
    stringBuffer.append("&" + Constants.TYPE_PARAMETER + "={" + WSRPConstants.WSRP_URL_TYPE + "}");
    stringBuffer.append("&" + Constants.PORTLET_MODE_PARAMETER + "={" + WSRPConstants.WSRP_MODE
        + "}");
    stringBuffer.append("&" + Constants.WINDOW_STATE_PARAMETER + "={"
        + WSRPConstants.WSRP_WINDOW_STATE + "}");
    stringBuffer.append("&" + Constants.SECURE_PARAMETER + "={" + WSRPConstants.WSRP_SECURE_URL
        + "}");

    // WSRP1 parameters
    stringBuffer.append("&" + WSRPConstants.WSRP_PORTLET_HANDLE + "={"
        + WSRPConstants.WSRP_PORTLET_HANDLE + "}");
    stringBuffer.append("&" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "={"
        + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "}");
    stringBuffer.append("&" + WSRPConstants.WSRP_NAVIGATIONAL_STATE + "={"
        + WSRPConstants.WSRP_NAVIGATIONAL_STATE + "}");
    stringBuffer.append("&" + WSRPConstants.WSRP_SESSION_ID + "={" + WSRPConstants.WSRP_SESSION_ID
        + "}");
    stringBuffer.append("&" + WSRPConstants.WSRP_USER_CONTEXT_KEY + "={"
        + WSRPConstants.WSRP_USER_CONTEXT_KEY + "}");
    stringBuffer.append("&" + WSRPConstants.WSRP_URL + "={" + WSRPConstants.WSRP_URL + "}");
    stringBuffer.append("&" + WSRPConstants.WSRP_REQUIRES_REWRITE + "={"
        + WSRPConstants.WSRP_REQUIRES_REWRITE + "}");
    stringBuffer.append("&" + WSRPConstants.WSRP_INTERACTION_STATE + "={"
        + WSRPConstants.WSRP_INTERACTION_STATE + "}");
    stringBuffer.append("&" + WSRPConstants.WSRP_FRAGMENT_ID + "={"
        + WSRPConstants.WSRP_FRAGMENT_ID + "}");
  }

  private void manageServerProtocol(StringBuffer sB, Boolean isSecure){
    if (isSecure != null && isSecure) {
      sB.append(SECURE_PROTOCOL);
    } else {
      sB.append(NON_SECURE_PROTOCOL);
    }
  }
  
  @Deprecated
  private void manageServerPath(StringBuffer sB, String path) {
    String newPath = null;
    try {
      URL url = new URL(path);
      host = url.getHost();
      port = url.getPort();
      newPath = url.getFile();
    } catch (MalformedURLException e) {
      newPath = path;
    }
    sB.append(host);
    if (port > 0)
      sB.append(":").append(port);
    sB.append(newPath);
  }

}
