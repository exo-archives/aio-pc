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

package org.exoplatform.services.wsrp2.consumer.impl;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.exoplatform.Constants;
import org.exoplatform.services.portletcontainer.plugins.pc.PCConstants;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.consumer.URLGenerator;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 6 févr. 2004
 * Time: 13:19:37
 */

public class URLGeneratorImpl implements URLGenerator {

  public String getBlockingActionURL(String baseURL,
                                     Map<String, String> params) {
    return getURL(baseURL, params);
  }

  public String getRenderURL(String baseURL,
                             Map<String, String> params) {
    return getURL(baseURL, params);
  }

  public String getResourceURL(String baseURL,
                               Map<String, String> params) {
    return getURL(baseURL, params);
  }

  private String getURL(String baseURL,
                        Map<String, String> params) {
    StringBuffer sB = new StringBuffer();
    sB.append(baseURL);
    return computeParameters(sB, params);
  }

  public String getNamespacedToken(String token) {
    return token;
  }

  private String computeParameters(StringBuffer sB,
                                   Map parameters) {
    Set<String> names = parameters.keySet();
    for (Iterator<String> iterator = names.iterator(); iterator.hasNext();) {
      String name = (String) iterator.next();
      // need todo below, because the PORTLET_HANDLE doesn't need for our new plugin.wsrp mechanism
      if (name.equalsIgnoreCase(WSRPConstants.WSRP_PORTLET_HANDLE))
        continue;
      Object obj = parameters.get(name);
      if (obj instanceof String) {
        String value = (String) obj;
        sB.append("&");
        sB.append(URLEncoder.encode(replaceName(name)));
        sB.append("=");
        sB.append(URLEncoder.encode(replaceValue(value)));
      } else {
        String[] values = (String[]) obj;
        for (int i = 0; i < values.length; i++) {
          name = replaceName(name);
          sB.append("&");
          sB.append(URLEncoder.encode(name));
          sB.append("=");
          sB.append(URLEncoder.encode(values[i]));
        }
      }
    }
    return sB.toString();
  }

  private String replaceName(String name) {
    if (WSRPConstants.WSRP_MODE.equals(name))
      return Constants.PORTLET_MODE_PARAMETER;
    else if (WSRPConstants.WSRP_WINDOW_STATE.equals(name))
      return Constants.WINDOW_STATE_PARAMETER;
    else if (WSRPConstants.WSRP_PORTLET_HANDLE.equals(name))
      return Constants.COMPONENT_PARAMETER;
    else if (WSRPConstants.WSRP_SECURE_URL.equals(name))
      return Constants.SECURE_PARAMETER;
    else if (WSRPConstants.WSRP_URL_TYPE.equals(name))
      return Constants.TYPE_PARAMETER;
    return name;
  }

  private String replaceValue(String value) {
    if (value.startsWith(WSRPConstants.WSRP_PREFIX))
      value = value.substring(WSRPConstants.WSRP_PREFIX.length());
    if (WSRPConstants.URL_TYPE_BLOCKINGACTION.equals(value))
      value = PCConstants.actionString;
    return value;
  }

}
