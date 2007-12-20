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
 * Jul 11, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Util.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class Util {

	static public String getDescription(String lang, List descriptions) {
		if (lang == null) return null;
		lang = lang.toLowerCase();
		for (int i = 0; i < descriptions.size(); i++) {
			Description desc = (Description) descriptions.get(i);
			String listLang = desc.getLang();
			if (listLang != null) {
				if (lang.equals(desc.getLang().toLowerCase())) {
					return desc.getDescription() ;
				}
			}
		}
		return null;
	}

  public static String actionToString(int action) {
    switch (action) {
    case PCConstants.actionInt:
      return PCConstants.actionString;
    case PCConstants.eventInt:
      return PCConstants.eventString;
    case PCConstants.resourceInt:
      return PCConstants.resourceString;
    default:
      return PCConstants.renderString;
    }
  }

  public static int actionToInt(String action) {
    if (action == null)
      return PCConstants.renderInt;
    if (action.equals(PCConstants.actionString))
      return PCConstants.actionInt;
    if (action.equals(PCConstants.eventString))
      return PCConstants.eventInt;
    if (action.equals(PCConstants.resourceString))
      return PCConstants.resourceInt;
    return PCConstants.renderInt;
  }
	
	
	
	
}
