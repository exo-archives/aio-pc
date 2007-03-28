package org.exoplatform.services.portletcontainer.test.filter;

import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.filter.PortletFilter;
import org.exoplatform.services.portletcontainer.filter.PortletFilterConfig;
import org.exoplatform.services.portletcontainer.filter.PortletFilterChain;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletException;
import java.io.IOException;

import org.apache.commons.logging.Log;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 17 nov. 2003
 * Time: 19:06:35
 */
public class LoggerFilter2 implements PortletFilter{

	private LogService logService;
	private Log log;

	public LoggerFilter2(LogService logService) {
		this.logService = logService;
		log = logService.getLog("org.exoplatform.portal.container");
	}

	public void init(PortletFilterConfig portletFilterConfig) throws PortletException {
	}

	public void doFilter(PortletRequest portletRequest,
											 PortletResponse portletResponse,
											 PortletFilterChain filterChain)
					throws IOException, PortletException {
		log.debug("------------->LOG FILTER2  PRE");
		filterChain.doFilter(portletRequest, portletResponse) ;
		log.debug("------------->LOG FILTER2  POST");
	}

	public void destroy() {
		log.debug("------------->LOG FILTER2 DESTROY");
	}
}
