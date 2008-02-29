/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.plugins.pc.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.filter.FilterConfig;

import org.exoplatform.services.portletcontainer.pci.model.InitParam;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 17 nov. 2003
 * Time: 21:11:17
 */
public class PortletFilterConfigImpl implements FilterConfig {

	private String filterName;
	private PortletContext portletContext;
	private Map initParams;

	public PortletFilterConfigImpl(String filterName, List initParamsList, PortletContext portletContext) {
		this.filterName = filterName;
		this.portletContext = portletContext;
		this.initParams = new HashMap();
		for (Iterator iterator = initParamsList.iterator(); iterator.hasNext();) {
			InitParam initParam = (InitParam) iterator.next();
			initParams.put(initParam.getName(), initParam.getValue());
		}
	}

	public String getFilterName() {
		return filterName;
	}

	public String getInitParameter(String string) {
		return (String) initParams.get(string);
	}

	public Enumeration getInitParameterNames() {
		return Collections.enumeration(initParams.keySet());
	}

	public PortletContext getPortletContext() {
		return portletContext;
	}
}
