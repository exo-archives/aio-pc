/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;


import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class tests the getServerInfo() method.
 */

public class GetServerInfoTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetServerInfoTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME); 
        PortletContext context = getPortletContext();

        if (context != null) {
            String info = context.getServerInfo();

            // it just needs to be a not null value
            if(info != null ) {
			    StringTokenizer st = new StringTokenizer(info, "/");
			    if (st.countTokens() == 2) {
			  		resultWriter.setStatus(ResultWriter.PASS); 
			    } else {
			    	resultWriter.setStatus(ResultWriter.FAIL); 
                	resultWriter.addDetail("Invalid number of tokens obtained");
            		resultWriter.addDetail( "Expected number of tokens: 2" );
            		resultWriter.addDetail( "Actual number of tokens: "+ 
                                                st.countTokens());
            		resultWriter.addDetail( "Expected format:" + 
                                                "containername/versionnumber");
            		resultWriter.addDetail( "Actual value:" + info);
			    }
            } else {
			    resultWriter.setStatus(ResultWriter.FAIL); 
                resultWriter.addDetail("getServerInfo() returned a null" );
            }
        } else {
           resultWriter.setStatus(ResultWriter.FAIL);
           resultWriter.addDetail("getPortletContext() returned null");
        }
		out.println(resultWriter.toString()); 
    }
}
