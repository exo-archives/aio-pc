/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.Portlet;

import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.UnavailableException;

/**
 * This class participates in testing that destroy() is not called if
 * initialization failed.  It throws UnavailableException in init()
 * and stores an attribute in the portlet context in destroy().
 */
public class InitFailedDestroyNotCalledTest_1_Portlet extends GenericPortlet {
    public static final String TEST_NAME
        = InitFailedDestroyNotCalledTestPortlet.TEST_NAME;

    public void init(PortletConfig config) throws PortletException {
        super.init(config);
        throw new UnavailableException(TEST_NAME);
    }

    public void destroy() {     // this method should not be called
        getPortletContext().setAttribute(TEST_NAME, TEST_NAME);
    }

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        // do nothing
    }
}
