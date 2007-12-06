/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL        .
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Oct 9, 2003
 * Time: 1:57:42 PM
 */
package org.exoplatform.services.portletcontainer.test.portlet;

import javax.portlet.*;
import java.io.IOException;

public class PortletWithNoCacheTag implements Portlet{
  public void init(PortletConfig portletConfig) throws PortletException {
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {    
    renderResponse.setContentType("text/html");
    renderResponse.setProperty(RenderResponse.EXPIRATION_CACHE, ""+8);
  }

  public void destroy() {
  }
}
