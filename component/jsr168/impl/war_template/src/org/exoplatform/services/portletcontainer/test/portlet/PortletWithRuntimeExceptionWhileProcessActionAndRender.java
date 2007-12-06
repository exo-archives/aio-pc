package org.exoplatform.services.portletcontainer.test.portlet;

import javax.portlet.*;
import java.io.IOException;

/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL        .
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 14 oct. 2003
 * Time: 21:24:13
 */
public class PortletWithRuntimeExceptionWhileProcessActionAndRender implements Portlet{

  public void init(PortletConfig portletConfig) throws PortletException {
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
		throw new RuntimeException("runtime exception in processAction");
  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
		throw new RuntimeException("runtime exception in render");
  }

  public void destroy() {
		throw new RuntimeException("runtime exception in destroy");
  }

}

