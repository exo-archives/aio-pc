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
import com.sun.ts.tests.portlet.common.util.ListCompare;

/**
 *  This class will test the constructor ValidatorException(text, failedKeys).
 */

public class ValidatorExceptionCtr1TestPortlet extends GenericPortlet {

	static public String TEST_NAME = "ValidatorExceptionCtr1Test";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        String[] expectedPreferenceKeys = 
          new String[] {"preferredLanguage", "preferredOS", "preferredSystem"};

        HashSet preferenceKeys = new HashSet();

        preferenceKeys.add("preferredLanguage");
        preferenceKeys.add("preferredOS");
        preferenceKeys.add("preferredSystem");

		ValidatorException validatorException = 
                            new ValidatorException(TEST_NAME, preferenceKeys);

        String message = validatorException.getMessage();
        Enumeration keys = validatorException.getFailedKeys();
        ListCompare listCompare = new ListCompare(expectedPreferenceKeys,
                                                keys, 
                                                null,
                                                ListCompare.ALL_ELEMENTS_MATCH);

        if ((message != null && message.equals(TEST_NAME)) &&
              (!listCompare.misMatch())) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected : " + TEST_NAME + " as text");
            resultWriter.addDetail("Expected : Preference keys");
            for (int i=0; i<expectedPreferenceKeys.length; i++) {
                resultWriter.addDetail(expectedPreferenceKeys[i]);
            }
            resultWriter.addDetail("Actual : " + message + " as text");
            resultWriter.addDetail("Actual : Preference keys obtained");
            resultWriter.addDetail(listCompare.getMisMatchReason());
        }
		out.println(resultWriter.toString());
    }
}
