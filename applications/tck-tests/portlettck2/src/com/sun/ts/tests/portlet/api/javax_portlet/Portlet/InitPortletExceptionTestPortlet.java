/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.Portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the PortletException thrown by the init() method.
 */
public class InitPortletExceptionTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "InitPortletExceptionTest";

    public void init(PortletConfig config) throws PortletException {
        throw new PortletException();
    }

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        resultWriter.setStatus(ResultWriter.FAIL);

        resultWriter.addDetail("render() is called after init() throws "
                               + "PortletException.");

        out.println(resultWriter.toString());
    }
}
