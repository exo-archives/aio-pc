/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.UnavailableException;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.UnavailableException;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import java.io.PrintWriter;
import java.io.IOException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *  This class will test the constructor UnavailableException(text)
 */

public class UnavailableExceptionCtr1TestPortlet extends GenericPortlet {

	static public String TEST_NAME = "UnavailableExceptionCtr1Test";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		UnavailableException unavailableException = 
                            new UnavailableException(TEST_NAME);

        String text = unavailableException.getMessage();

        if (text != null && text.equals(TEST_NAME)) {
			resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected value of text : " 
                                     + "UnavailableExceptionCtr1Test ");
            resultWriter.addDetail("Actual value of text : " + text);
        }
		out.println(resultWriter.toString());
    }
}
