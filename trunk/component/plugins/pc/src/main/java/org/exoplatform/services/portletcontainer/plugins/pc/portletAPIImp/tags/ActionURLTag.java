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
import javax.portlet.BaseURL;
import javax.portlet.MimeResponse;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

/**
 * Created by The eXo Platform SAS.
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * Date: Aug 20, 2003
 * Time: 2:47:30 PM
 */
public class ActionURLTag extends XURLTag {

  /**
   * Serial ID.
   */
  private static final long serialVersionUID = -887109718512852285L;
  
  /**
   * Name.
   */
  private String name;

  /**
   * @return name
   */
  public final String getName() {
    return name;
  }

  /**
   * @param name name
   */
  public final void setName(final String name) {
    this.name = name;
  }

  /**
   * Overridden method.
   *
   * @return portlet url
   * @see org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.tags.XURLTag#getPortletURL()
   */
  public final BaseURL getPortletURL() {
    ServletRequest request = pageContext.getRequest();
    MimeResponse portletResponse = (MimeResponse) request.getAttribute("javax.portlet.response");
    return portletResponse.createActionURL();
  }

  public int doEndTag() throws JspException {
    if (name != null)
      addParameter(ActionRequest.ACTION_NAME, name);
    return super.doEndTag();
  }

}
