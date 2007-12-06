package org.exoplatform.services.portletcontainer.impl.filter;


import javax.portlet.PortletContext;
import org.exoplatform.services.portletcontainer.filter.PortletFilterConfig;
import org.exoplatform.services.portletcontainer.pci.model.InitParam;
import java.util.*;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 17 nov. 2003
 * Time: 21:11:17
 */
public class PortletFilterConfigImpl implements PortletFilterConfig{

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