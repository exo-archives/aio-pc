/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class tests the getAttribute(String) method when an invalid attribute
 *  is specified.
 */

public class GetAttributeNullTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetAttributeNullTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        PortletContext context = getPortletContext();
        String param = "ManKind";
        String param2 = "humane";

        if (context != null) {
            //set attribute
            context.setAttribute( param, param2 );

            //then get attribute
            Object attr = context.getAttribute( "doesnotexist" );

            if(attr == null ) {
			    resultWriter.setStatus(ResultWriter.PASS);
		    } else {
			    String sAttr = (String) attr;
			    resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Expected result = null "); 
                resultWriter.addDetail("Actual result = " + sAttr); 
		    }
        } else {
			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletContext() returned null");
        }
		out.println(resultWriter.toString());
    }
}
