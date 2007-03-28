/**
 * Copyright 2001-2003 The EXO Development Team All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.portletcontainer.impl.utils;


import java.util.List;
import org.exoplatform.services.portletcontainer.pci.model.Description;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
/**
 * Created by the Exo Development team
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 */
public class PortletUtil {

  static public String getPortletTitle(Portlet portlet) {
    return portlet.getPortletInfo().getTitle() ;
  }

  static public String getDescription(Portlet portlet, String lang) {
    lang = lang.toLowerCase() ;
    List list = portlet.getDescription() ;
    for (int i = 0; i < list.size(); i++) {
      Description desc = (Description) list.get(i) ;
      if (lang.equals(desc.getLang().toLowerCase())) {
        return desc.getDescription() ;
      }
    }
   return null ; 
  }
}
