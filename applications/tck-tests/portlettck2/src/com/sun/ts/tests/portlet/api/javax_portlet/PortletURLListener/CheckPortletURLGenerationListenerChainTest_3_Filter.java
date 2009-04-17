/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletURLListener;

import java.util.Map;

import javax.portlet.PortletURL;
import javax.portlet.PortletURLGenerationListener;
import javax.portlet.ResourceURL;

public class CheckPortletURLGenerationListenerChainTest_3_Filter implements PortletURLGenerationListener {

	public void filterActionURL(PortletURL url) {
		
	}

	public void filterRenderURL(PortletURL url) {
		
	}

	public void filterResourceURL(ResourceURL url) {
		Map<String,String[]> paramMap =url.getParameterMap();
		if (paramMap.get("PortletURLGenerationFilter1") != null &&
			paramMap.get("PortletURLGenerationFilter2") != null &&
			paramMap.get("PortletURLGenerationFilter3") == null){
			
			url.setParameter("PortletURLGenerationFilter3", "true");
		}
	}
}
