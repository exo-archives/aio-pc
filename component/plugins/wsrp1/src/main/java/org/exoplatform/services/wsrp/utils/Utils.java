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
    if (type.equalsIgnoreCase(PCConstants.actionString))
      return WSRPConstants.URL_TYPE_BLOCKINGACTION;
    if (type.equalsIgnoreCase(PCConstants.resourceString))
      return WSRPConstants.URL_TYPE_RESOURCE;
    if (type.equalsIgnoreCase(PCConstants.renderString))
      return WSRPConstants.URL_TYPE_RENDER;
    return type;
  }

  public static String changeUrlTypeFromWSRPToJSRPortlet(String type) {
    if (type.equalsIgnoreCase(WSRPConstants.URL_TYPE_BLOCKINGACTION))
      return PCConstants.actionString;
    if (type.equalsIgnoreCase(WSRPConstants.URL_TYPE_RESOURCE))
      return PCConstants.resourceString;
    if (type.equalsIgnoreCase(WSRPConstants.URL_TYPE_RENDER))
      return PCConstants.renderString;
    return type;
  }

}
