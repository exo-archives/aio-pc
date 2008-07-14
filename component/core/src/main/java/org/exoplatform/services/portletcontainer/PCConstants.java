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
package org.exoplatform.services.portletcontainer;

import org.exoplatform.Constants;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * Date: 26 janv. 2004
 * Time: 09:11:34
 */
public class PCConstants {

  /**
   * Cache.
   */
  public static final String CACHE_REGION = "jsr286:";

  /**
   * User scope.
   */
  public static final String USER_SCOPE_CACHE = CACHE_REGION + "user";

  /**
   * Global scope.
   */
  public static final String GLOBAL_SCOPE_CACHE = CACHE_REGION + "forAll";

  /**
   * Exception.
   */
  public static final String EXCEPTION = "org.exoplatform.portal.container.exception";

  /**
   * Destroyed.
   */
  public static final String DESTROYED = "org.exoplatform.portal.container.destroyed";

  /**
   * Portlet scope.
   */
  public static final String PORTLET_SCOPE = "PORTLET_SCOPE";

  /**
   * App scope.
   */
  public static final String APPLICATION_SCOPE = "APPLICATION_SCOPE";

  /**
   * Mime type.
   */
  public static final String XHTML_MIME_TYPE = "text/html";

  /**
   * Render.
   */
  public static final String RENDER_STRING = "render";

  /**
   * Action.
   */
  public static final String ACTION_STRING = "action";

  /**
   * Event.
   */
  public static final String EVENT_STRING = "event";

  /**
   * Resource.
   */
  public static final String RESOURCE_STRING = "resource";

  /**
   * Render.
   */
  public static final int RENDER_INT = 0;

  /**
   * Action.
   */
  public static final int ACTION_INT = 1;

  /**
   * Event.
   */
  public static final int EVENT_INT = 2;

  /**
   * Resource.
   */
  public static final int RESOURCE_INT = 3;

  /**
   * Remove public params.
   */
  public static final String REMOVE_PUBLIC_STRING = Constants.PARAMETER_ENCODER + "removePublicParam";

  /**
   * Ampersand.
   */
  public static final String AMPERSAND = "&";

}
