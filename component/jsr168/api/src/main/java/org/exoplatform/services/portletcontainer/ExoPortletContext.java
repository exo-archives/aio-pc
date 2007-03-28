package org.exoplatform.services.portletcontainer;


import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.servlet.ServletContext;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 19 nov. 2003
 * Time: 00:12:04
 */
public interface ExoPortletContext extends PortletContext{

	public ServletContext getWrappedServletContext();

  public boolean isSessionShared();

}
