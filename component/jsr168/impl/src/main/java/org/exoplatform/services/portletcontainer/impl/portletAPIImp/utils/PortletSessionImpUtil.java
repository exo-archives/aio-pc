/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 26, 2003
 * Time: 4:54:01 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.utils;

import javax.portlet.PortletSession;


/**
 *
 * The attribute name may be encoded according to its scope (application or portlet)
 * using
 *           javax.portlet.p.<ID>?attributeName    (for portlet  scope)
 */
public class PortletSessionImpUtil {

  private static final String PORTLET_SCOPE_NAMESPACE = "javax.portlet.p.";

  public static String encodePortletSessionAttribute(String id,
                                                     String attributeName,
                                                     int scope) {
    StringBuffer sB = new StringBuffer();
    if (PortletSession.APPLICATION_SCOPE == scope) {
      sB.append(attributeName);
      return sB.toString();
    } else if (PortletSession.PORTLET_SCOPE == scope) {
      sB.append(PORTLET_SCOPE_NAMESPACE);
      sB.append(id);
      sB.append("?");
      sB.append(attributeName);
      return sB.toString();
    } else {
      return null;
    }
  }

}
