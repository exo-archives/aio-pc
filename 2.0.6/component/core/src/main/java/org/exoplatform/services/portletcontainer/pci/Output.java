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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.MimeResponse;
import javax.portlet.ResourceResponse;

import org.exoplatform.services.portletcontainer.PCConstants;

/**
 * Created by The eXo Platform SAS Author : Mestrallet Benjamin .
 * benjmestrallet@users.sourceforge.net Date: Jul 30, 2003 Time: 9:09:25 PM
 */
public class Output {

  /**
   * HTTP OK status (code 200).
   */
  private static final int HTTP_OK_STATUS = 200;

  /**
   * Invalidate session constant.
   */
  public static final String INVALIDATE_SESSION = "_invalidate_session_";

  /**
   * Redirect constant.
   */
  public static final String SEND_REDIRECT = "_send_redirect_";

  /**
   * Login constant.
   */
  public static final String LOGIN = "_login_";

  /**
   * Password constant.
   */
  public static final String PASSWORD = "_password_";

  /**
   * Logout constant.
   */
  public static final String LOGOUT = "_logout_";

  /**
   * Properties.
   */
  private Map<String, Object> properties = new HashMap<String, Object>();

  /**
   * Session map.
   */
  private HashMap<String, Object> sessionMap = new HashMap<String, Object>();

  /**
   * Params to remove.
   */
  private final Set<String> publicRenderParamsToRemove = new HashSet<String>();

  /**
   * @return props
   */
  public final Map<String, Object> getProperties() {
    return properties;
  }

  /**
   * @param key name
   * @param o value
   */
  public final void addProperty(final String key, final Object o) {
    properties.put(key, o);
  }

  /**
   * @param properties props
   */
  public final void setProperties(final Map<String, Object> properties) {
    this.properties = properties;
  }

  /**
   * Special properties names.
   */
  private final List<String> specialProperties = java.util.Arrays.asList(MimeResponse.CACHE_SCOPE,
      MimeResponse.EXPIRATION_CACHE, MimeResponse.ETAG, MimeResponse.USE_CACHED_CONTENT,
      MimeResponse.MARKUP_HEAD_ELEMENT, MimeResponse.NAMESPACED_RESPONSE,
      ResourceResponse.HTTP_STATUS_CODE, Output.SEND_REDIRECT, Output.INVALIDATE_SESSION,
      PCConstants.EXCEPTION, PCConstants.DESTROYED);

  /**
   * @param s name
   * @return either specified property is special
   */
  private boolean specialProperty(final String s) {
    return specialProperties.contains(s);
  }

  /**
   * @return properties map
   */
  public final Map<String, String> getHeaderProperties() {
    HashMap<String, String> newMap = new HashMap<String, String>();
    for (String name : properties.keySet()) {
      if (specialProperty(name))
        continue;
      if (!(properties.get(name) instanceof String))
        continue;
      newMap.put(name, (String) properties.get(name));
    }
    return newMap;
  }

  /**
   * @return http status
   */
  public final int getStatus() {
    try {
      return Integer.parseInt((String) properties.get(ResourceResponse.HTTP_STATUS_CODE));
    } catch (Exception e) {
      return HTTP_OK_STATUS;
    }
  }

  /**
   * @return either output has error
   */
  public final boolean hasError() {
    if ((properties.get(PCConstants.DESTROYED) != null)
        || (properties.get(PCConstants.EXCEPTION) != null))
      return true;
    return false;
  }

  /**
   * @return map
   */
  public final HashMap<String, Object> getSessionMap() {
    return sessionMap;
  }

  /**
   * @param map map
   */
  public final void setSessionMap(final HashMap<String, Object> map) {
    this.sessionMap = map;
  }

  /**
   * @param name param name to remove
   */
  public final void removePublicRenderParameter(final String name) {
    publicRenderParamsToRemove.add(name);
  }

  /**
   * @return removed params
   */
  public final Set<String> getRemovedPublicRenderParameters() {
    return publicRenderParamsToRemove;
  }

}
