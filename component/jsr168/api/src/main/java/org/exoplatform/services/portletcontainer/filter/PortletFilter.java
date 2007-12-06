package org.exoplatform.services.portletcontainer.filter;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 17 nov. 2003
 * Time: 17:41:18
 */
public interface PortletFilter {

	public void init(PortletFilterConfig filterConfig) throws PortletException;

	public void doFilter(PortletRequest request,
											 PortletResponse response,
											 PortletFilterChain chain)
					throws java.io.IOException,
					PortletException;

    public void destroy();

}
