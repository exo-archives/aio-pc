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
package org.exoplatform.services.portletcontainer.pci.model;

import java.util.List;

import org.exoplatform.services.portletcontainer.PCConstants;

/**
 * Jul 11, 2004 .
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: Util.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class Util {

  /**
   * Simple constructor.
   */
  protected Util() {
  }

  /**
   * @param lang language
   * @param descriptions description list
   * @return description
   */
  public static String getDescription(final String lang, final List<Description> descriptions) {
    if (lang == null)
      return null;
    String lang1 = lang.toLowerCase();
    for (int i = 0; i < descriptions.size(); i++) {
      Description desc = descriptions.get(i);
      String listLang = desc.getLang();
      if (listLang != null)
        if (lang1.equals(desc.getLang().toLowerCase()))
          return desc.getDescription();
    }
    return null;
  }

  /**
   * @param action action int
   * @return action string
   */
  public static String actionToString(final int action) {
    switch (action) {
      case PCConstants.ACTION_INT:
        return PCConstants.ACTION_STRING;
      case PCConstants.EVENT_INT:
        return PCConstants.EVENT_STRING;
      case PCConstants.RESOURCE_INT:
        return PCConstants.RESOURCE_STRING;
      default:
        return PCConstants.RENDER_STRING;
    }
  }

  /**
   * @param action action string
   * @return action int
   */
  public static int actionToInt(final String action) {
    if (action == null)
      return PCConstants.RENDER_INT;
    if (action.equals(PCConstants.ACTION_STRING))
      return PCConstants.ACTION_INT;
    if (action.equals(PCConstants.EVENT_STRING))
      return PCConstants.EVENT_INT;
    if (action.equals(PCConstants.RESOURCE_STRING))
      return PCConstants.RESOURCE_INT;
    return PCConstants.RENDER_INT;
  }

}
