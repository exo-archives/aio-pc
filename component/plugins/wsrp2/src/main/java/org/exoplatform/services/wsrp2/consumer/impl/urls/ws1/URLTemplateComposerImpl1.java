/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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

package org.exoplatform.services.wsrp2.consumer.impl.urls.ws1;

import org.exoplatform.Constants;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.consumer.URLTemplateComposer;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 21, 2008
 */

public class URLTemplateComposerImpl1 implements URLTemplateComposer {

  public String createBlockingActionTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, false);
    appendCommonParameters(sB);
    appendBlockingActionParameters(sB);
    return sB.toString();
  }

  public String createSecureBlockingActionTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, true);
    appendCommonParameters(sB);
    appendBlockingActionParameters(sB);
    return sB.toString();
  }

  public String createRenderTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, false);
    appendCommonParameters(sB);
    appendRenderParameters(sB);
    return sB.toString();
  }

  public String createSecureRenderTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, true);
    appendCommonParameters(sB);
    appendRenderParameters(sB);
    return sB.toString();
  }

  public String createResourceTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, false);
    appendCommonParameters(sB);
    appendResourceParameters(sB);
    return sB.toString();
  }

  public String createSecureResourceTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, true);
    appendCommonParameters(sB);
    appendResourceParameters(sB);
    return sB.toString();
  }

  public String createDefaultTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, false);
    appendCommonParameters(sB);
    return sB.toString();
  }

  public String createSecureDefaultTemplate(String path) {
    StringBuffer sB = new StringBuffer();
    manageServerPath(sB, path, true);
    appendCommonParameters(sB);
    return sB.toString();
  }

  public String getNamespacePrefix() {
    return "prefix_";
  }

  private void manageServerPath(StringBuffer sB, String path, Boolean isSecure) {
    sB.append(path);
  }

  private void appendCommonParameters(StringBuffer stringBuffer) {

    stringBuffer.append("&" + Constants.TYPE_PARAMETER + "={" + WSRPConstants.WSRP_URL_TYPE + "}");
    stringBuffer.append("&" + Constants.SECURE_PARAMETER + "={" + WSRPConstants.WSRP_SECURE_URL
        + "}");
    stringBuffer.append("&" + WSRPConstants.WSRP_PORTLET_HANDLE + "={"
        + WSRPConstants.WSRP_PORTLET_HANDLE + "}");
    stringBuffer.append("&" + WSRPConstants.WSRP_USER_CONTEXT_KEY + "={"
        + WSRPConstants.WSRP_USER_CONTEXT_KEY + "}");
    stringBuffer.append("&" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "={"
        + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "}");
    stringBuffer.append("&" + WSRPConstants.WSRP_SESSION_ID + "={" + WSRPConstants.WSRP_SESSION_ID
        + "}");

    stringBuffer.append("&" + WSRPConstants.WSRP_FRAGMENT_ID + "={"
        + WSRPConstants.WSRP_FRAGMENT_ID + "}");

  }

  private void appendBlockingActionParameters(StringBuffer stringBuffer) {

    stringBuffer.append("&" + WSRPConstants.WSRP_INTERACTION_STATE + "={"
        + WSRPConstants.WSRP_INTERACTION_STATE + "}");
    appendRenderParameters(stringBuffer);
  }

  private void appendRenderParameters(StringBuffer stringBuffer) {

    stringBuffer.append("&" + Constants.PORTLET_MODE_PARAMETER + "={" + WSRPConstants.WSRP_MODE
        + "}");
    stringBuffer.append("&" + Constants.WINDOW_STATE_PARAMETER + "={"
        + WSRPConstants.WSRP_WINDOW_STATE + "}");
    stringBuffer.append("&" + WSRPConstants.WSRP_NAVIGATIONAL_STATE + "={"
        + WSRPConstants.WSRP_NAVIGATIONAL_STATE + "}");

  }

  private void appendResourceParameters(StringBuffer stringBuffer) {

    stringBuffer.append("&" + WSRPConstants.WSRP_URL + "={" + WSRPConstants.WSRP_URL + "}");

    stringBuffer.append("&" + WSRPConstants.WSRP_REQUIRES_REWRITE + "={"
        + WSRPConstants.WSRP_REQUIRES_REWRITE + "}");
  }

}
