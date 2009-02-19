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

import com.sun.ts.tests.portlet.common.util.ListCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that parameters specified in the query string used
 * to create the RequestDispatcher take precedence over other
 * parameters of the same name passed to the included servlet.
 *  
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class QueryStringParametersTestServlet extends AbstractTestServlet {
	
	static final long serialVersionUID = 286L;
	
    
	@Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		
        ResultWriter resultWriter = new ResultWriter(testName);
        
        String[] expectedResult = {"Java", "Oracle"};
        String[] result = request.getParameterValues("preferredLanguage");

        ListCompare listCompare
            = new ListCompare(expectedResult, result, "Java",
                              ListCompare.ALL_ELEMENTS_AND_ORDER_MATCH);

        if (listCompare.misMatch()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail(listCompare.getMisMatchReason());
        } else {
            resultWriter.setStatus(ResultWriter.PASS);
        }

        return resultWriter.toString();
	}

}
