/**
 * Copyright 2007 IBM Corporation.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletFilter;

import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class WrapperTestPortlet extends GenericPortlet {
	public static final String TEST_NAME = "WrapperTest";

	public void render(RenderRequest request, RenderResponse response) 
        throws PortletException, IOException {
    }
}
