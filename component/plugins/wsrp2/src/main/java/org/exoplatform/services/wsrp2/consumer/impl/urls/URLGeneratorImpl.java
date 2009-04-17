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

package org.exoplatform.services.wsrp2.consumer.impl.urls;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.consumer.URLGenerator;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 21, 2008
 */

public class URLGeneratorImpl implements URLGenerator {

  public String getBlockingActionURL(String baseURL, Map<String, String[]> params) {
    return getURL(baseURL, params);
  }

  public String getRenderURL(String baseURL, Map<String, String[]> params) {
    return getURL(baseURL, params);
  }

  public String getResourceURL(String baseURL, Map<String, String[]> params) {
    return getURL(baseURL, params);
  }

  public String getExtensionURL(String baseURL, Map<String, String[]> params) {
    return getURL(baseURL, params);
  }

  public String getURL(String baseURL, Map<String, String[]> params) {
    StringBuffer sB = new StringBuffer();
    sB.append(baseURL);
    return computeParameters(sB, params);
  }

  public String getNamespacedToken(String token) {
    return token;
  }

  private String computeParameters(StringBuffer sB, Map<String, String[]> parameters) {
    Set<String> names = parameters.keySet();
    for (String name : names) {
      String[] values = parameters.get(name);
      for (String value : values) {
        sB.append(WSRPConstants.NEXT_PARAM);
        sB.append(name);//encode(name, true));
        sB.append("=");
        sB.append(value);//encode(value, true));
      }
    }
    return sB.toString();
  }

  protected String encode(String s, boolean escapeXML) {
    if (escapeXML)
      s = encodeChars(s);
    try {
      return URLEncoder.encode(s, "utf-8");
    } catch (java.io.UnsupportedEncodingException e) {
      return s;
    }
  }

  protected String encodeChars(String s) {
    return s.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&#034;")
            .replace("'", "&#039;");
  }

}
