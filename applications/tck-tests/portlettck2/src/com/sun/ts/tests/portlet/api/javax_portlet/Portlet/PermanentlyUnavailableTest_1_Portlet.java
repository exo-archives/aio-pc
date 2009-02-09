/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.Portlet;

import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.UnavailableException;

/**
 * This class participates in testing that destroy() is called if a
 * permanent unavailability is indicated by the UnavailableException.
 * It throws UnavailableException in render() and stores an attribute
 * in the portlet context in destroy().
 */
public class PermanentlyUnavailableTest_1_Portlet extends GenericPortlet {
    public static final String TEST_NAME
        = PermanentlyUnavailableTestPortlet.TEST_NAME;

    public void destroy() {
        getPortletContext().setAttribute(TEST_NAME, TEST_NAME);
    }

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        throw new UnavailableException(TEST_NAME);
    }
}
