package org.exoplatform.services.portletcontainer.filter;

import javax.portlet.PortletContext;
import java.util.Enumeration;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 17 nov. 2003
 * Time: 17:41:32
 */
public interface PortletFilterConfig {

	 public String getFilterName();

	 public String getInitParameter(String name);

	 public Enumeration getInitParameterNames();

	 public PortletContext getPortletContext();

}
