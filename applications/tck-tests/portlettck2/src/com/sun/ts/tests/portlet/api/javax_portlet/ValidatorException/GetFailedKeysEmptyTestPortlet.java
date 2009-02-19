/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ValidatorException;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ValidatorException;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Enumeration;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *  This class will test the empty failedKeys.
 */

public class GetFailedKeysEmptyTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetFailedKeysEmptyTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		ValidatorException validatorException = 
                            new ValidatorException(TEST_NAME, null);

        Enumeration keys = validatorException.getFailedKeys();


        if (keys != null && !keys.hasMoreElements()) {
            resultWriter.setStatus(ResultWriter.PASS);
        }
        else {
            resultWriter.setStatus(ResultWriter.FAIL);
            if ( keys == null) {
                resultWriter.addDetail("Expected an empty enumeration.");
                resultWriter.addDetail("Found null returned");
            }
            else {
                resultWriter.addDetail("Expected an empty enumeration.");
                resultWriter.addDetail(
                    "Found a enumeration with size greater than 0");
            }
        }
        out.println(resultWriter.toString());
    }
}
        
