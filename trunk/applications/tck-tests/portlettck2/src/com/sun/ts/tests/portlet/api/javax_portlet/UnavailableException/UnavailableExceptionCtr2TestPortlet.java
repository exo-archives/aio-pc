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
 *  This class will test the constructor UnavailableException(text, seconds)
 */

public class UnavailableExceptionCtr2TestPortlet extends GenericPortlet {

	static public String TEST_NAME = "UnavailableExceptionCtr2Test";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		UnavailableException unavailableException = 
                                new UnavailableException(TEST_NAME, 5);

        String text = unavailableException.getMessage();
        int unavailableSecs = unavailableException.getUnavailableSeconds();

        if ((text != null && text.equals(TEST_NAME)) &&
             (unavailableSecs == 5)) {
				resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected: "
                                    + TEST_NAME +  " for text" 
                                    + " 5 for number of unavailable seconds"); 
            resultWriter.addDetail("Actual: " 
                                    + text + "  for text" 
                                    + unavailableSecs 
                                    + " for number of unavailable seconds");
        }
		out.println(resultWriter.toString());
    }
}
