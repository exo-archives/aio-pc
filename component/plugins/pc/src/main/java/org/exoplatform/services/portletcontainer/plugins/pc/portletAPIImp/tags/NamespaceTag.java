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

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.ServletRequest;
import javax.portlet.PortletResponse;

import java.io.IOException;

/**
 * Created by The eXo Platform SAS.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 20, 2003
 * Time: 3:15:38 PM
 */
public class NamespaceTag extends TagSupport {

  /**
   * Serial ID.
   */
  private static final long serialVersionUID = 2575978069849495522L;

  /**
   * Overridden method.
   *
   * @return tag processing result
   * @throws JspException
   * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
   */
  public final int doStartTag() throws JspException {
    ServletRequest request = pageContext.getRequest();
    PortletResponse portletResponse = (PortletResponse) request
        .getAttribute("javax.portlet.response");

    try {
      pageContext.getOut().print(portletResponse.getNamespace());
    } catch (IOException e) {
      throw new JspException(e);
    }

    return EVAL_PAGE;
  }

}
