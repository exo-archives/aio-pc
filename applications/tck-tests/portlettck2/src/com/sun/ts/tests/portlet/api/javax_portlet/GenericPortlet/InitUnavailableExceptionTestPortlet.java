/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.GenericPortlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.UnavailableException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the UnavailableException thrown by the init() method.
 */
public class InitUnavailableExceptionTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "InitUnavailableExceptionTest";

    public void init() throws PortletException {
        throw new UnavailableException(TEST_NAME);
    }

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        resultWriter.setStatus(ResultWriter.FAIL);

        resultWriter.addDetail("render() is called after init() throws "
                               + "UnavailableException.");

        out.println(resultWriter.toString());
    }
}
