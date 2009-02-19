/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.WindowState;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import java.io.PrintWriter;
import java.io.IOException;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class tests WindowState.equals() method.
 */

public class EqualsTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "EqualsTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        WindowState windowState = new WindowState("minimized");

        if(windowState.equals(WindowState.MINIMIZED)) {
			resultWriter.setStatus(ResultWriter.PASS);
        } else {
			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected Window state : minimized");
            resultWriter.addDetail("Actual Window state : " 
                                                + windowState.toString());
        }
		out.println(resultWriter.toString());
    }
}
