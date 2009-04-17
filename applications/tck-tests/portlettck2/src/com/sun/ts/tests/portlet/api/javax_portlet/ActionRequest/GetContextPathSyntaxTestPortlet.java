/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;



/**
 *	A Test for syntax for path returned by GetContextPath() method
 */

public class GetContextPathSyntaxTestPortlet extends LogicInProcessActionPortlet {

    public static String TEST_NAME="GetContextPathSyntaxTest";

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        String contextPath = request.getContextPath();
        if (contextPath != null) {
            if ((contextPath.length() ==0) || ((contextPath.startsWith("/")) &&
                (!contextPath.endsWith("/")))) {
                resultWriter.setStatus(ResultWriter.PASS);
            }
            else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("getContextPath returned " + contextPath);
            }
        }
        else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getContextPath returned null"); 
        }
        request.getPortletSession(true).setAttribute("TestResult", resultWriter.toString());
    }
}
