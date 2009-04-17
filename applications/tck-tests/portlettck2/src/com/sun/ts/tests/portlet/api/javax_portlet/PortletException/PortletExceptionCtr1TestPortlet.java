/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletException;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import java.io.PrintWriter;
import java.io.IOException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *  This class will test the constructor PortletException().
 */

public class PortletExceptionCtr1TestPortlet extends GenericPortlet {

	static public String TEST_NAME = "PortletExceptionCtr1Test";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		PortletException pe = new PortletException();
            
        String text = pe.getMessage();

		if (text == null) {
			resultWriter.setStatus(ResultWriter.PASS);
        } else {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Expected : null as text");
			resultWriter.addDetail("Actual : " + text + " as text");
        }
		out.println(resultWriter.toString());
	}
}
