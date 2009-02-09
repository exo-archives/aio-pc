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

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that GenericPortlet.init() is called by
 * GenericPortlet.init(PortletConfig config) and the PortletConfig
 * object can still be retrieved via getPortletConfig().
 */
public class InitTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "InitTest";
    private boolean sawInit = false;

    public void init() throws PortletException {
        sawInit = true;
    }

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        if (sawInit) {
            if (getPortletConfig() != null) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("getPortletConfig() returned null.");
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("GenericPortlet.init(PortletConfig config) "
                                   + "didn't call GenericPortlet.init().");
        }

        out.println(resultWriter.toString());
    }
}
