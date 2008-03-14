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
package org.exoplatform.services.portletcontainer.plugins.pc.utils;

import java.util.List;
import org.exoplatform.services.portletcontainer.pci.model.Description;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;

/**
 * Created by the Exo Development team.
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 */
public class PortletUtil {

  /**
   * @param portlet portlet object
   * @return portlet title
   */
  public static final String getPortletTitle(final Portlet portlet) {
    return portlet.getPortletInfo().getTitle();
  }

  /**
   * @param portlet portlet object
   * @param lang language
   * @return description for specified language
   */
  public static final String getDescription(final Portlet portlet, String lang) {
    lang = lang.toLowerCase();
    List<Description> list = portlet.getDescription();
    for (int i = 0; i < list.size(); i++) {
      Description desc = list.get(i);
      if (lang.equals(desc.getLang().toLowerCase()))
        return desc.getDescription();
    }
    return null;
  }

}
