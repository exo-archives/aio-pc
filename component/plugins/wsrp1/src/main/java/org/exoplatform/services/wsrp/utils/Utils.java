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

package org.exoplatform.services.wsrp.utils;

import java.util.HashMap;
import java.util.Map;

import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.type.LocalizedString;
import org.exoplatform.services.wsrp.type.NamedString;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class Utils {

  public static String getStringFromLocalizedString(LocalizedString ls) {
    if (ls != null)
      return ls.getValue();
    return null;
  }

  public static LocalizedString getLocalizedString(String value,
                                                   String lang,
                                                   String rn) {
    LocalizedString tmp = new LocalizedString();
    tmp.setValue(value);
    tmp.setLang(lang);
    tmp.setResourceName(rn);
    return tmp;
  }

  public static LocalizedString getLocalizedString(String value,
                                                   String lang) {
    LocalizedString tmp = new LocalizedString();
    tmp.setValue(value);
    tmp.setLang(lang);
    return tmp;
  }

  public static NamedString getNamesString(String name,
                                           String value) {
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

  public static Map<String, String[]> getRenderParametersFromFormParameters(NamedString[] params) {
    if (params == null)
      return null;
    Map<String, String[]> result = new HashMap<String, String[]>();
    for (NamedString namedString : params) {
      result.put(namedString.getName(), new String[] { namedString.getValue() });
    }
    return result;
  }

}
