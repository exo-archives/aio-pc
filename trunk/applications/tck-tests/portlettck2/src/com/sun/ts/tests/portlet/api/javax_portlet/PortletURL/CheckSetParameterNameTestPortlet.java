/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletURL;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;


/**
 *	This class will test for PortletURL.setParameter() method.
 */

public class CheckSetParameterNameTestPortlet extends GenericPortlet {

    public static String TEST_NAME="CheckSetParameterNameTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		RequestCount reqCount = new RequestCount(request, response,
										RequestCount.MANAGED_VIA_SESSION);

		if (reqCount.isFirstRequest()) {

			// SetParameter with value being "XML"
			PortletURLTag customTag = new PortletURLTag();
			customTag.setTagContent(getPortletURL(response, "XML"));
			out.println(customTag.toString());
        }
        else if (reqCount.getRequestNumber() == 1) {

			// SetParameter for the same name with value being "Java"
			PortletURLTag customTag = new PortletURLTag();
			customTag.setTagContent(getPortletURL(response, "Java"));
			out.println(customTag.toString());
		} else {
        	String param = "BestLanguage";
        	// getting the request parameter
        	Object result = request.getParameter( param );
        	String expectedResult = "Java";

        	if(result != null ) {
            	// is param an instance of java.lang.String
            	if(result instanceof String ) {
                	if(((String ) result ).equals( expectedResult ) ) {
                    	resultWriter.setStatus(ResultWriter.PASS);
                	} else {
                    	resultWriter.setStatus(ResultWriter.FAIL);
                    	resultWriter.addDetail("PortletRequest.getParameter(" 
                       	         + param + ") returned an incorrect result" );
                    	resultWriter.addDetail("Expected result = " 
                                                    + expectedResult);
                    	resultWriter.addDetail("Actual result = " 
                                                    +(String )result);
                	}
            	} else {
                	resultWriter.setStatus(ResultWriter.FAIL);
                	resultWriter.addDetail("PortletRequest.getParameter(" 
                   			       + param + ") failed to returned a " 
                   	               + " result that is an instance of a String");
            	}
        	} else {
            	resultWriter.setStatus(ResultWriter.FAIL);
            	resultWriter.addDetail("PortletRequest.getParameter(" 
               	                     + param + ") returned a null result");
        	}
			out.println(resultWriter.toString());
    	}
	}

    protected String getPortletURL(RenderResponse response, String value) {
        PortletURL portletURL = response.createRenderURL();
        portletURL.setParameter("BestLanguage", value);
        return portletURL.toString(); 
            
    }
 }
