 /***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.portletcontainer.test.portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public class PortletToTestGetTitle extends GenericPortlet {

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    String title  = getTitle(renderRequest);
    
    System.out.println("TITLE "+title);
    
    renderResponse.setContentType("text/html");
    PrintWriter w = renderResponse.getWriter();
    w.println("Everything is ok");    
  }

}
