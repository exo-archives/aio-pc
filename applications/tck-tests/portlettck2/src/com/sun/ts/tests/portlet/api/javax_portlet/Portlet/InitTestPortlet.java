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
 * This class tests the init() method.
 */
public class InitTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "InitTest";
    private boolean sawInit = false;

    public void init(PortletConfig config) throws PortletException {
        super.init(config);
        sawInit = true;
    }

    public void render(RenderRequest request, RenderResponse response) 
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        if (sawInit) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Portlet life cycle is incorrect.");
            resultWriter.addDetail("init() not called before render().");
        }

        out.println(resultWriter.toString());
    }
}
