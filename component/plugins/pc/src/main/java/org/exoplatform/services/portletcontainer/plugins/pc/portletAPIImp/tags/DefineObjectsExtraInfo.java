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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.tags;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;
import javax.servlet.jsp.tagext.TagData;

/**
 * Created by The eXo Platform SAS.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 20, 2003
 * Time: 12:51:20 AM
 */
public class DefineObjectsExtraInfo extends TagExtraInfo {

  /**
   * Overridden method.
   *
   * @param tagData tag data
   * @return variable info
   * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(javax.servlet.jsp.tagext.TagData)
   */
  public final VariableInfo[] getVariableInfo(final TagData tagData) {
    return new VariableInfo[] {
        new VariableInfo("portletConfig", "javax.portlet.PortletConfig", true, VariableInfo.AT_END),

        new VariableInfo("renderRequest", "javax.portlet.RenderRequest", true, VariableInfo.AT_END),
        new VariableInfo("renderResponse",
            "javax.portlet.RenderResponse",
            true,
            VariableInfo.AT_END),

        new VariableInfo("resourceRequest",
            "javax.portlet.ResourceRequest",
            true,
            VariableInfo.AT_END),
        new VariableInfo("resourceResponse",
            "javax.portlet.ResourceResponse",
            true,
            VariableInfo.AT_END),
        new VariableInfo("actionRequest", "javax.portlet.ActionRequest", true, VariableInfo.AT_END),
        new VariableInfo("actionResponse",
            "javax.portlet.ActionResponse",
            true,
            VariableInfo.AT_END),
        new VariableInfo("eventRequest", "javax.portlet.EventRequest", true, VariableInfo.AT_END),
        new VariableInfo("eventResponse", "javax.portlet.EventResponse", true, VariableInfo.AT_END),

        new VariableInfo("portletSession",
            "javax.portlet.PortletSession",
            true,
            VariableInfo.AT_END),
        new VariableInfo("portletSessionScope", "java.util.Map", true, VariableInfo.AT_END),
        new VariableInfo("portletPreferences",
            "javax.portlet.PortletPreferences",
            true,
            VariableInfo.AT_END),
        new VariableInfo("portletPreferencesValues", "java.util.Map", true, VariableInfo.AT_END)

    };
  }

}
