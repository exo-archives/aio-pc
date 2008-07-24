/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the following methods of the
 * HttpServletRequest are equivalent to the methods of the
 * PortletRequest of similar name: getLocale, getLocales.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsLocaleMiscTestServlet extends AbstractTestServlet {
	
	private static final long serialVersionUID = 286L;

    
	@Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		
		ResultWriter resultWriter = new ResultWriter(testName);
		
		resultWriter.setStatus(ResultWriter.PASS);
		
        checkGetLocale(request, resultWriter);
        checkGetLocales(request, resultWriter);
        
		return resultWriter.toString();
	}

    private void checkGetLocale(HttpServletRequest request,
                                ResultWriter resultWriter) {

        Locale expectedResult = 
        	(Locale)getAndRemoveSessionAttribute(request, "getLocale");
        
        Locale result = request.getLocale();

        if ((result == null) || !result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getLocale():");
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }
    }

    @SuppressWarnings("unchecked")
    private void checkGetLocales(HttpServletRequest request,
                                 ResultWriter resultWriter) {

    	Enumeration<Locale> expectedResult = 
    		(Enumeration<Locale>)getAndRemoveSessionAttribute(request, "getLocales");
        
        ArrayList<Locale> expectedList = new ArrayList<Locale>();

        while (expectedResult.hasMoreElements()) {
            expectedList.add(expectedResult.nextElement());
        }

        Enumeration<Locale> result = request.getLocales();
        ArrayList<Locale> list = new ArrayList<Locale>();

        while (result.hasMoreElements()) {
            list.add(result.nextElement());
        }

        if (list.size() != expectedList.size()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getLocales():");

            resultWriter.addDetail("Expected size of the list = "
                                   + expectedList.size());

            resultWriter.addDetail("Actual size of the list = " + list.size());
            resultWriter.addDetail("Expected values:");

            for (int i = 0; i < expectedList.size(); i++) {
                resultWriter.addDetail(" " + expectedList.get(i));
            }

            resultWriter.addDetail("Actual values:");

            for (int i = 0; i < list.size(); i++) {
                resultWriter.addDetail(" " + list.get(i));
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).equals(expectedList.get(i))) {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("getLocales():");

                    resultWriter.addDetail("Actual values are not the "
                                           + "same as the expected:");
                    
                    resultWriter.addDetail("Expected values:");

                    for (int j = 0; j < expectedList.size(); j++) {
                        resultWriter.addDetail(" " + expectedList.get(j));
                    }

                    resultWriter.addDetail("Actual values:");

                    for (int j = 0; j < list.size(); j++) {
                        resultWriter.addDetail(" " + list.get(j));
                    }

                    break;
                }
            }
        }
    }
}
