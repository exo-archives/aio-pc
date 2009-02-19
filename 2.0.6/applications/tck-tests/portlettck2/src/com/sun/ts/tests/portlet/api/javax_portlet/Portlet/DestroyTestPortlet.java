/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.Portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the destroy() method.
 */
public class DestroyTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "DestroyTest";
    private boolean sawDestroy = false;

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        if (sawDestroy) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Portlet life cycle is incorrect.");
            resultWriter.addDetail("destroy() is called before render().");
        } else {
            resultWriter.setStatus(ResultWriter.PASS);
        }

        out.println(resultWriter.toString());
    }

    public void destroy() {
        sawDestroy = true;
    }
}
