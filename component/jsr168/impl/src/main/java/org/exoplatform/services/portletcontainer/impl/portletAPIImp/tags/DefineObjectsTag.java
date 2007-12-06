/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 20, 2003
 * Time: 12:18:00 AM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.tags;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class DefineObjectsTag extends TagSupport{

  public int doStartTag() throws JspException {
    ServletRequest request =  pageContext.getRequest();

    PortletConfig config = (PortletConfig)request.getAttribute("javax.portlet.config") ;
    RenderRequest renderRequest = (RenderRequest) request.getAttribute("javax.portlet.request") ;
    RenderResponse renderResponse = (RenderResponse) request.getAttribute("javax.portlet.response") ;

    pageContext.setAttribute("portletConfig", config);
    pageContext.setAttribute("renderRequest", renderRequest);
    pageContext.setAttribute("renderResponse", renderResponse);

    return EVAL_PAGE;
  }

}
