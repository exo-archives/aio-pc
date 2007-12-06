/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 20, 2003
 * Time: 3:15:38 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.tags;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.ServletRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;

public class NamespaceTag extends TagSupport{

  public int doStartTag() throws JspException {
    ServletRequest request =  pageContext.getRequest();
    RenderResponse renderResponse = (RenderResponse) request.getAttribute("javax.portlet.response") ;
    try {
      pageContext.getOut().print(renderResponse.getNamespace());
    } catch (IOException e) {
      throw new JspException(e);
    }

    return EVAL_PAGE;
  }


}
