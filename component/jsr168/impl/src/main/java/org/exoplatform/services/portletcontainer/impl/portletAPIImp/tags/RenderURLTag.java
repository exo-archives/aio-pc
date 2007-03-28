/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 20, 2003
 * Time: 2:53:04 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.tags;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.servlet.ServletRequest;

public class RenderURLTag extends XURLTag{
  public PortletURL getPortletURL() {
    ServletRequest request =  pageContext.getRequest();
    RenderResponse renderResponse = (RenderResponse) request.getAttribute("javax.portlet.response") ;
    return renderResponse.createRenderURL();
  }
}
