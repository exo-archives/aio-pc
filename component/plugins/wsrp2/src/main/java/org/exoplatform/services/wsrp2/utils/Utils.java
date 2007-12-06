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
 
package org.exoplatform.services.wsrp2.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.portletcontainer.pci.PortletURLFactory;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.LocalizedString;
import org.exoplatform.services.wsrp2.type.NamedString;

/**
 * @author Mestrallet Benjamin
 *         benjmestrallet@users.sourceforge.net
 */
public class Utils {

  public static LocalizedString getLocalizedString(String value, String lang, String rn) {
    LocalizedString tmp = new LocalizedString();
    tmp.setValue(value);
    //tmp.setLang(lang);
    tmp.setResourceName(rn);
    return tmp;
  }

  public static LocalizedString getLocalizedString(String value, String lang) {
    LocalizedString tmp = new LocalizedString();
    tmp.setValue(value);
    //tmp.setLang(lang);
    return tmp;
  }

  public static NamedString getNamesString(String name, String value){
    NamedString tmp = new NamedString();
    tmp.setName(name);
    tmp.setValue(value);
    return tmp;
  }

  public static QName[] getQNameArray(List<QName> list) {
    if (list == null)
      return null;
    if (list.isEmpty())
      return new QName[]{};
    QName[] result = list.toArray(new QName[]{});
    return result;
  }

  public static List<QName> getQNameList(QName[] array) {
    if (array == null)
      return null;
    if (array.length == 0)
      return new ArrayList<QName>();
    List<QName> result = Arrays.asList(array);
    return result;
  }
  
  public static String changeUrlTypeFromActionToBlockingaction(String type) {
    return type.equalsIgnoreCase(PortletURLFactory.ACTION)?WSRPConstants.URL_TYPE_BLOCKINGACTION:type;
  }
  
  public static void fillExtensions(String temp, Extension[] extensions) {
    if (extensions != null) if (extensions[0] != null) if (extensions[0].get_any() != null) if (extensions[0].get_any()[0] != null) { 
      // TODO EXOMAN: need iterate foreach element of array 
      try {
        temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_EXTENSIONS + "}", extensions[0].get_any()[0].getAsString()); // TODO EXOMAN
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_EXTENSIONS + "}", "");
    }
  }
  
}
