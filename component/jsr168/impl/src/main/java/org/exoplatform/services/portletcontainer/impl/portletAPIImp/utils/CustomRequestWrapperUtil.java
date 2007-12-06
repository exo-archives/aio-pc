/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 11:49:38 AM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.utils;

public class CustomRequestWrapperUtil {

  private static final char SEPARATOR = '$';

  public static String decodeRequestAttribute(String windowId, String attributeName) {    
    if (attributeName.startsWith(windowId + SEPARATOR)) {
      int index = attributeName.indexOf(SEPARATOR);
      if (index > -1) {
        attributeName = attributeName.substring(index + 1);
      }
    }
    return attributeName;
  }

  public static String encodeAttribute(String windowId, String attributeName) {
    if(attributeName.startsWith("javax."))
      return attributeName;
    StringBuffer sB = new StringBuffer();
    sB.append(windowId);
    sB.append(SEPARATOR);
    sB.append(attributeName);
    return sB.toString();
  }

}
