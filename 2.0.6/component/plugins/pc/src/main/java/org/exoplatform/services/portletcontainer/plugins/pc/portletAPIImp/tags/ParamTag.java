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

/**
 * @author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * @date: Aug 20, 2003
 * @time: 1:54:58 PM
 */
public class ParamTag extends TagSupport {

  /**
   * Serial ID.
   */
  private static final long serialVersionUID = 278357243620991146L;

  /**
   * Name.
   */
  private String name;

  /**
   * Value.
   */
  private String value;

  /**
   * @return value
   */
  public final String getValue() {
    return value;
  }

  /**
   * @param value value
   */
  public final void setValue(final String value) {
    this.value = value;
  }

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
   * @return state
   * @throws JspException exception
   * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
   */
  public int doEndTag() throws JspException {
    XURLTag father = (XURLTag) getParent();
    father.addParameter(name, value);
    return super.doEndTag();
  }

}
