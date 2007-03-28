/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.portletcontainer;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 26 janv. 2004
 * Time: 09:11:34
 */

public class PortletContainerConstants {

  public static final String CACHE_REGION = "jsr168:";
  public static final String USER_SCOPE_CACHE = CACHE_REGION + "user";
  public static final String GLOBAL_SCOPE_CACHE = CACHE_REGION + "forAll";

  static public String EXCEPTION = "org.exoplatform.portal.container.exception";
  static public String DESTROYED = "org.exoplatform.portal.container.destroyed";
}