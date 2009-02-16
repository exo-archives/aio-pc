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
 *  This class will test the constructor PortletException(text, cause).
 */

public class PortletExceptionCtr3TestPortlet extends GenericPortlet {

	static public String TEST_NAME = "PortletExceptionCtr3Test";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        Throwable expectedCause = new Throwable();

		PortletException pe = new PortletException(TEST_NAME, expectedCause);

        String text = pe.getMessage();
        Throwable cause = pe.getCause();

        if ((text != null && text.equals(TEST_NAME)) && 
             (cause != null && cause.equals(expectedCause))) {
			   resultWriter.setStatus(ResultWriter.PASS);
        } else {
		    resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Expected : " 
                                    + TEST_NAME + " as text"
                                    + expectedCause + " as cause");
			resultWriter.addDetail("Actual : "
                                    + text +  " as text"
                                    + cause + " as cause");
        }
		out.println(resultWriter.toString());
	}
}
