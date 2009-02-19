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
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class tests getAttribute(String) method.
 */

public class GetAttributeTestPortlet extends GenericPortlet {
	
	static public String TEST_NAME = "GetAttributeTest";

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
            Object attr = context.getAttribute( param );

            if(attr != null ) {
                //attr should  also be an instance of java.lang.String
                if (attr instanceof String) {
                    String sAttr =(String ) attr;
     
                    if(sAttr.equals( param2 ) ) {
					    resultWriter.setStatus(ResultWriter.PASS);
                    } else {
					    resultWriter.setStatus(ResultWriter.FAIL);
                        resultWriter.addDetail("getAttribute(" + param 
                                        + ") returned incorrect results" );
                        resultWriter.addDetail("Expected result = " + param2); 
                        resultWriter.addDetail("Actual result = " + sAttr); 
                    }
                } else {
				    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("getAttribute(" + param + ") did "
                                   + "not return an attribute of type String");
                }
            } else {
			    resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("getAttribute(" + param + ") returned "
                                     + "a null result" );
            }
        } else {
			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletContext() returned null");
        }
	   out.println(resultWriter.toString());
    }
}
