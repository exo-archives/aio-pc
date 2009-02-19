/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletURLListener;

import javax.portlet.PortletURL;
import javax.portlet.PortletURLGenerationListener;
import javax.portlet.ResourceURL;

public class IsPortletURLInPortletURLGenerationListenerTestFilter implements PortletURLGenerationListener {
	private static String URL_FILTER_PARAMETER = "URLFilterParameterIsPortletURLInPortletURLGenerationListenerTest";
	
	public void filterActionURL(PortletURL url) {
	}

	public void filterRenderURL(PortletURL url) {
		Class[] clazzes = url.getClass().getInterfaces();
		if (clazzes != null){
			for (int i = 0; i<clazzes.length;i++){
				if (clazzes[i].equals(PortletURL.class))
					url.setParameter(URL_FILTER_PARAMETER, "portletURL");
			}
		}
	}

	public void filterResourceURL(ResourceURL url) {
		Class[] clazzes = url.getClass().getInterfaces();
		if (clazzes != null){
			for (int i = 0; i<clazzes.length;i++){
				if (clazzes[i].equals(ResourceURL.class))
					url.setParameter(URL_FILTER_PARAMETER, "resourceURL");
			}
		}
	}
}
