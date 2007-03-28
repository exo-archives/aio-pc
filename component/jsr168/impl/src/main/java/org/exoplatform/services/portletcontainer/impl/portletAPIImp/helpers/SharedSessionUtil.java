/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 1 d�c. 2003
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers;


/**
 * To simulate independant PortletSession objects for every portlet application,
 * we partitionnate a single Http Session object wit the following convention :
 *           portletApplicationId.data?attributeName
 *           portletApplicationId.metaData?attributeName
 * 
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class SharedSessionUtil {    

  private static final char SEPARATOR = '$';

  public static String encodePortletSessionAttribute(String portletApplicationId,
                                                     String attributeName) {
    StringBuffer sB = new StringBuffer();
    sB.append(portletApplicationId);
    sB.append(".data");
    sB.append(SEPARATOR);
    sB.append(attributeName);
    return sB.toString();
  }

  public static String encodePortletSessionMetaDataAttribute(String portletApplicationId,
                                                             String attributeName) {
    StringBuffer sB = new StringBuffer();
    sB.append(portletApplicationId);
    sB.append(".metaData");
    sB.append(SEPARATOR);
    sB.append(attributeName);
    return sB.toString();
  }  

  public static String decodePortletSessionMetaDataAttribute(String portletApplicationId,
                                                             String attributeName) {
    if (attributeName.startsWith(portletApplicationId + ".metaData" + SEPARATOR)) {
      int index = attributeName.indexOf(SEPARATOR);
      if (index > -1) {
        attributeName = attributeName.substring(index + 1);
      }
    }
    return attributeName;
  }

  public static boolean isMetaDataAttribute(String portletApplicationId,
                                            String attributeName) {
    if (attributeName.startsWith(portletApplicationId + ".metaData" + SEPARATOR))
      return true;
    else
      return false;
  }

  public static String decodePortletSessionAttribute(String portletApplicationId,
                                                     String attributeName) {
    if (attributeName.startsWith(portletApplicationId + ".data" + SEPARATOR)) {
      int index = attributeName.indexOf(SEPARATOR);
      if (index > -1) {
        attributeName = attributeName.substring(index + 1);
      }
    }
    return attributeName;
  }

  public static boolean isAttribute(String portletApplicationId, String s) {
    if (s.startsWith(portletApplicationId + ".data" + SEPARATOR))
      return true;
    else
      return false;
  }
  

}
