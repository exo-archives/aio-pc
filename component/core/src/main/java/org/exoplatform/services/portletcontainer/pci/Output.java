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
package org.exoplatform.services.portletcontainer.pci;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.MimeResponse;

import org.exoplatform.services.portletcontainer.PortletContainerConstants;

/**
 * Created by The eXo Platform SAS
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 30, 2003
 * Time: 9:09:25 PM
 */
public class Output {

  final static public String      SEND_REDIRECT = "_send_redirect_";

  final static public String      LOGIN         = "_login_";

  final static public String      PASSWORD      = "_password_";

  final static public String      LOGOUT        = "_logout_";

  private Map<String, Object>     properties    = new HashMap<String, Object>();

  private HashMap<String, Object> sessionMap    = new HashMap<String, Object>();

  public Map<String, Object> getProperties() {
    return properties;
  }

  public void addProperty(String key,
                          Object o) {
    properties.put(key, o);
  }

  public void setProperties(Map<String, Object> properties) {
    this.properties = properties;
  }

  private List<String> specialProperties = java.util.Arrays.asList(
      MimeResponse.CACHE_SCOPE,
      MimeResponse.EXPIRATION_CACHE,
      MimeResponse.ETAG,
      MimeResponse.USE_CACHED_CONTENT,
      MimeResponse.MARKUP_HEAD_ELEMENT,
      MimeResponse.NAMESPACED_RESPONSE,
      Output.SEND_REDIRECT,
      PortletContainerConstants.EXCEPTION,
      PortletContainerConstants.DESTROYED
  );

  private boolean specialProperty(String s) {
    return specialProperties.contains(s);
  }

  public Map<String, String> getHeaderProperties() {
    HashMap<String, String> newMap = new HashMap<String, String>();
    for (Iterator<String> iterator = properties.keySet().iterator(); iterator.hasNext();) {
      String name = iterator.next();
      if (specialProperty(name))
        continue;
      if (!(properties.get(name) instanceof String))
        continue;
      newMap.put(name, (String) properties.get(name));
    }
    return newMap;
  }

  public boolean hasError() {
    if (properties.get(PortletContainerConstants.DESTROYED) != null || properties.get(PortletContainerConstants.EXCEPTION) != null)
      return true;
    return false;
  }

  public HashMap<String, Object> getSessionMap() {
    return sessionMap;
  }

  public void setSessionMap(HashMap<String, Object> map) {
    this.sessionMap = map;
  }

}
