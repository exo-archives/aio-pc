/**
 * Copyright 2007 IBM Corporation.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletFilter;

import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * This class is for have a portlet for the filter to test destroy filter.
 * @author <a href="mailto:dettborn@minet.uni-jena.de">Torsten Dettborn</a>
 */
public class DestroyFilterTestPortlet extends GenericPortlet {
	public static final String TEST_NAME = "DestroyFilterTest";
	public void init(PortletConfig config) throws PortletException {
        super.init(config);
    }

    public void render(RenderRequest request, RenderResponse response) 
        throws PortletException, IOException {
    }
}
