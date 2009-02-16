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

/**
 *	This class will test the setAttribute(String,Object) method.
 */

import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class SetAttributeTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "SetAttributeTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME); 

        PortletContext context = getPortletContext();

        //set and get
        String param1 = "Mankind";
        String param2 = "humane";

        if (context != null) {
            context.setAttribute( param1, param2 );

            Object attr = context.getAttribute( param1 );

            if(attr != null ) {
                if(attr instanceof String ) {
                    String sAttr =(String ) attr;

                    if(sAttr.equals( param2 ) ) {
					    resultWriter.setStatus(ResultWriter.PASS); 
                    } else {
					    resultWriter.setStatus(ResultWriter.FAIL); 
                        resultWriter.addDetail("setAttribute(" + param1 + "," 
                                                + param2 + ") did not set the "
                                                + "attribute properly");
                        resultWriter.addDetail("Expected result = " + param2); 
                        resultWriter.addDetail("Actual result = " + sAttr); 
                    }
                } else {
				    resultWriter.setStatus(ResultWriter.FAIL); 
                    resultWriter.addDetail("setAttribute(" + param1 + "," 
                                        + param2 + ") did not set an attribute "
                                        + "of type String properly" );
                }
             }
          } else {
			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletContext() returned null");
        }
	    out.println(resultWriter.toString()); 
    }
}
