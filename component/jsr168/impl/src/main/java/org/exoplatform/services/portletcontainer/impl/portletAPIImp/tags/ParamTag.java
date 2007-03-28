/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 20, 2003
 * Time: 1:54:58 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.tags;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;

public class ParamTag extends TagSupport{

  private String name;
  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int doEndTag() throws JspException {
    XURLTag father = (XURLTag) getParent();
    father.addParameter(name, value);
    return super.doEndTag();
  }

}
