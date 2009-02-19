/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletURL;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.Map;
import java.util.HashMap;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the IllegalArgumentException thrown by the
 * setParameters() method.
 */
public class SetParametersIllegalArgumentExceptionTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "SetParametersIllegalArgumentExceptionTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		boolean success = true;
        try {
            response.createRenderURL().setParameters(null);

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("IllegalArgumentException was not "
                                   + "thrown when parameters was null.");
			success = false;
        } catch (IllegalArgumentException e) {
			// pass behavior
		}

		if ( success) {
			try {
        		Map parameters = new HashMap(2);
        		parameters.put("language", new String[] {"Java", "C"});
        		parameters.put(null, new String[] {"Solaris", "Linux"});
            
            	response.createRenderURL().setParameters(parameters);

            	resultWriter.setStatus(ResultWriter.FAIL);
            	resultWriter.addDetail("IllegalArgumentException was not "
                                   + "thrown because one of the keys is null");

				success = false;
			} catch (IllegalArgumentException e1) {
				// pass behavior
			}
		}

		if ( success) {
			try {
				Map parameters = new HashMap(2);
				Integer myIntKey = new Integer(1);
				Integer myIntValue = new Integer(1);
				parameters.put(myIntKey, myIntValue);
				parameters.put("OS", new String[] {"Solaris", "Linux"});
				response.createRenderURL().setParameters(parameters);

            	resultWriter.setStatus(ResultWriter.FAIL);
            	resultWriter.addDetail("IllegalArgumentException was not "
                                   + "thrown because one of the keys is not "
								   + "a string");
				success = false;
			} catch (IllegalArgumentException e2) {
				// pass behavior
			}
		}

		if ( success) {
			try {
				Map parameters = new HashMap(2);
				parameters.put("language", new String[] {"Java", "C"});
				parameters.put("OS", "Solaris");
				response.createRenderURL().setParameters(parameters);

            	resultWriter.setStatus(ResultWriter.FAIL);
            	resultWriter.addDetail("IllegalArgumentException was not "
                                   + "thrown because the parameter values is "
								   + "not a string array");

				success = false;
			} catch (IllegalArgumentException e3) {
				// pass behavior
			}
		}

		if ( success) {
			resultWriter.setStatus(ResultWriter.PASS);
		}
        out.println(resultWriter.toString());
    }
}
