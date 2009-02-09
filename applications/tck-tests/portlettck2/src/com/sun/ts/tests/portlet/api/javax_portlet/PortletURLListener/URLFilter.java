/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletURLListener;

import javax.portlet.PortletURL;
import javax.portlet.PortletURLGenerationListener;
import javax.portlet.ResourceURL;

public class URLFilter implements PortletURLGenerationListener {
	private static String URL_FILTER_PARAMETER = "URLFilterParameterURLFilter";

	public void filterActionURL(PortletURL url) {
		url.setParameter(URL_FILTER_PARAMETER, "action");
	}

	public void filterRenderURL(PortletURL url) {
		url.setParameter(URL_FILTER_PARAMETER, "render");
	}

	public void filterResourceURL(ResourceURL url) {
		url.setParameter(URL_FILTER_PARAMETER, "resource");
	}
}
