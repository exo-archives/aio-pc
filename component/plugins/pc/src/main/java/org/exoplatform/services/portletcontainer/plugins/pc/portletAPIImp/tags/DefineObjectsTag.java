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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by The eXo Platform SAS.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 20, 2003
 * Time: 12:18:00 AM
 */
public class DefineObjectsTag extends TagSupport {

  /**
   * Serial ID.
   */
  private static final long serialVersionUID = 6747920745801687486L;

  /**
   * Overridden method.
   *
   * @return tag evaluation result
   * @throws JspException
   * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
   */
  public final int doStartTag() throws JspException {
    ServletRequest request = pageContext.getRequest();

    PortletConfig portletConfig = (PortletConfig) request.getAttribute("javax.portlet.config");
    PortletRequest portletRequest = (PortletRequest) request.getAttribute("javax.portlet.request");
    PortletResponse portletResponse = (PortletResponse) request
        .getAttribute("javax.portlet.response");
    PortletSession portletSession = portletRequest.getPortletSession(); // (PortletSession) request.getAttribute("javax.portlet.session");
    PortletPreferences portletPreferences = portletRequest.getPreferences(); // (PortletPreferences) request.getAttribute("javax.portlet.preferences");
    java.util.Map<String, String[]> portletPreferencesValues = portletPreferences.getMap();

    pageContext.setAttribute("portletConfig", portletConfig);

    if (portletRequest instanceof RenderRequest) {
      RenderRequest renderRequest = (RenderRequest) portletRequest;
      RenderResponse renderResponse = (RenderResponse) portletResponse;
      pageContext.setAttribute("renderRequest", renderRequest);
      pageContext.setAttribute("renderResponse", renderResponse);
    } else if (portletRequest instanceof ResourceRequest) {
      ResourceRequest resourceRequest = (ResourceRequest) portletRequest;
      ResourceResponse resourceResponse = (ResourceResponse) portletResponse;
      pageContext.setAttribute("resourceRequest", resourceRequest);
      pageContext.setAttribute("resourceResponse", resourceResponse);
    } else if (portletRequest instanceof ActionRequest) {
      ActionRequest actionRequest = (ActionRequest) portletRequest;
      ActionResponse actionResponse = (ActionResponse) portletResponse;
      pageContext.setAttribute("actionRequest", actionRequest);
      pageContext.setAttribute("actionResponse", actionResponse);
    } else if (portletRequest instanceof EventRequest) {
      EventRequest eventRequest = (EventRequest) portletRequest;
      EventResponse eventResponse = (EventResponse) portletResponse;
      pageContext.setAttribute("eventRequest", eventRequest);
      pageContext.setAttribute("eventResponse", eventResponse);
    }

    pageContext.setAttribute("portletSession", portletSession);
    pageContext.setAttribute("portletSessionScope", portletSession.getAttributeMap());
    pageContext.setAttribute("portletPreferences", portletPreferences);
    pageContext.setAttribute("portletPreferencesValues", portletPreferencesValues);

    return EVAL_PAGE;
  }

}
