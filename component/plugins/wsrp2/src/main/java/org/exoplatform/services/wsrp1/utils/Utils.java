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

package org.exoplatform.services.wsrp1.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.wsrp1.WSRPConstants;
import org.exoplatform.services.wsrp1.type.LocalizedString;
import org.exoplatform.services.wsrp1.type.NamedString;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class Utils {

  public static String getStringFromLocalizedString(LocalizedString ls) {
    if (ls != null)
      return ls.getValue();
    return null;
  }

  public static LocalizedString getLocalizedString(String value, String lang, String rn) {
    LocalizedString tmp = new LocalizedString();
    tmp.setValue(value);
    tmp.setLang(lang);
    tmp.setResourceName(rn);
    return tmp;
  }

  public static LocalizedString getLocalizedString(String value, String lang) {
    LocalizedString tmp = new LocalizedString();
    tmp.setValue(value);
    tmp.setLang(lang);
    return tmp;
  }

  public static NamedString getNamesString(String name, String value) {
    NamedString tmp = new NamedString();
    tmp.setName(name);
    tmp.setValue(value);
    return tmp;
  }

  public static String changeUrlTypeFromJSRPortletToWSRP(String type) {
    if (type.equalsIgnoreCase(PCConstants.ACTION_STRING))
      return WSRPConstants.URL_TYPE_BLOCKINGACTION;
    if (type.equalsIgnoreCase(PCConstants.RESOURCE_STRING))
      return WSRPConstants.URL_TYPE_RESOURCE;
    if (type.equalsIgnoreCase(PCConstants.RENDER_STRING))
      return WSRPConstants.URL_TYPE_RENDER;
    return type;
  }

  public static String changeUrlTypeFromWSRPToJSRPortlet(String type) {
    if (type.equalsIgnoreCase(WSRPConstants.URL_TYPE_BLOCKINGACTION))
      return PCConstants.ACTION_STRING;
    if (type.equalsIgnoreCase(WSRPConstants.URL_TYPE_RESOURCE))
      return PCConstants.RESOURCE_STRING;
    if (type.equalsIgnoreCase(WSRPConstants.URL_TYPE_RENDER))
      return PCConstants.RENDER_STRING;
    return type;
  }

  /**
   * Convert from input.getRenderParameters() to
   * interactionRequest.setFormParameters(NamedString[]).
   * 
   * @param parameters Map<String, String[]>
   * @param boolean value to store only those parameters which starting with
   *          "wsrp-" prefix
   * @return
   */
  public static NamedString[] getNamedStringArrayParametersFromMap(Map<String, String[]> params,
                                                                   boolean selectOnlyNonWSRP) {
    if (params == null)
      return null;
    if (params.isEmpty())
      return new NamedString[] {};
    Set<String> keys = params.keySet();
    List<NamedString> listNamedStringParams = new ArrayList<NamedString>();
    Iterator<String> iteratorKeys = keys.iterator();
    while (iteratorKeys.hasNext()) {
      String name = iteratorKeys.next();
      if ((selectOnlyNonWSRP && !name.startsWith(WSRPConstants.WSRP_PARAMETER_PREFIX))
          || !selectOnlyNonWSRP) {
        String[] values = params.get(name);
        for (String value : values) {
          listNamedStringParams.add(getNamesString(name, value));
        }
      }
    }
    return (NamedString[]) listNamedStringParams.toArray(new NamedString[listNamedStringParams.size()]);
  }

  /**
   * Convert from NamedString[] to Map<String, String[]>.
   * 
   * @param NamedString[]
   * @return Map<String, String[]>
   */
  public static Map<String, String[]> getMapParametersFromNamedStringArray(NamedString[] array) {
    if (array == null)
      return null;
    Map<String, String[]> result = new HashMap<String, String[]>();
    if (array != null) {
      for (NamedString namedString : array) {
        String name = namedString.getName();
        String value = namedString.getValue();
        if (value != null) {
          if (result.get(name) == null) {
            // new added parameter
            result.put(name, new String[] { value });
          } else {
            // next added parameter
            Arrays.asList(result.get(name)).add(value);
//            String[] oldArray = result.get(name);
//            String[] newArray = new String[oldArray.length + 1];
//            int i = 0;
//            if (oldArray != null) {
//              for (String v : oldArray) {
//                newArray[i++] = v;
//              }
//            }
//            newArray[i] = value;
//            result.put(name, newArray);
          }
        }
      }
    }
    return result;
  }

}
