/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the following methods of the
 * HttpServletResponse return null: encodeRedirectURL and
 * encodeRedirectUrl.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseMethodsReturnNullTestServlet extends AbstractTestServlet {
	
	private static final long serialVersionUID = 286L;

	@Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		
        ResultWriter resultWriter = new ResultWriter(testName);
        resultWriter.setStatus(ResultWriter.PASS);
        
        checkEncodeRedirectURL(response, resultWriter);
        checkEncodeRedirectUrl(response, resultWriter);
        
        return resultWriter.toString();
	}


    private void checkEncodeRedirectURL(HttpServletResponse response,
                                        ResultWriter resultWriter) {

        String result = response.encodeRedirectURL(null);

        if (result != null) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("encodeRedirectURL(null):");
            resultWriter.addDetail("Expected result = null");
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    @SuppressWarnings("deprecation")
    private void checkEncodeRedirectUrl(HttpServletResponse response,
                                        ResultWriter resultWriter) {

        String result = response.encodeRedirectUrl(null);

        if (result != null) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("encodeRedirectUrl(null):");
            resultWriter.addDetail("Expected result = null");
            resultWriter.addDetail("Actual result = " + result);
        }
    }
}
