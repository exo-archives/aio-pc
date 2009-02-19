/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletException;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *  This class will test the method 
 *  PortletException.printStackTrace(PrintWriter out)
 */

public class PrintStackTraceWriterTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "PrintStackTraceWriterTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter outWriter = response.getWriter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(out, true);
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		PortletException pe = new PortletException();

		pe.printStackTrace(pw);
		resultWriter.setStatus(ResultWriter.PASS);

		outWriter.println(resultWriter.toString());
	}
}
