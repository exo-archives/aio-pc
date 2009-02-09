/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */
package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.include;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class QueryStringParametersActionIncludeTestPortlet 
	extends AbstractActionIncludeTestPortlet {
	
    private static final String TEST_NAME = 
    	"QueryStringParametersActionIncludeTest";
    
    private static final String SERVLET_PATH = 
    	"/QueryStringParametersTestServlet?preferredLanguage=Java";

    
	@Override
	protected Map<String, String[]> getParametersMap() {
		Map<String, String[]> result = new HashMap<String, String[]>();
		result.put("preferredLanguage", new String[]{"Oracle"});
		return result;
	}


	@Override
	protected String getServletPath() {
		return SERVLET_PATH;
	}

	
	@Override
	protected String getTestName() {
		return TEST_NAME;
	}

}
