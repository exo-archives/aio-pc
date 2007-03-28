package org.exoplatform.services.portletcontainer.filter;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletException;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 17 nov. 2003
 * Time: 17:52:53
 */
public interface PortletFilterChain {

	public void doFilter(PortletRequest request,
											 PortletResponse response)
					throws java.io.IOException,
					PortletException;

}
