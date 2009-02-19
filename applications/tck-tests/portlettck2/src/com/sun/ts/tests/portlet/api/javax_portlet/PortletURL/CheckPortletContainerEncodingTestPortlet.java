/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletURL;

import javax.portlet.ActionRequest;
import javax.portlet.BaseURL;
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
 *	This class will test whether the portlet container is properly
 *  encoding the parameter values. In this class the parameter values has
 *  a space. This needs to be properly encoded by the container.
 */

public class CheckPortletContainerEncodingTestPortlet extends GenericPortlet {

    public static String TEST_NAME="CheckPortletContainerEncodingTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		RequestCount reqCount = new RequestCount(request, response,
										RequestCount.MANAGED_VIA_SESSION);

		if (reqCount.isFirstRequest()) {

			PortletURLTag customTag = new PortletURLTag();
			customTag.setTagContent(getPortletURL(response));
			out.println(customTag.toString());
		} else {
        	String param = "Languages";
        	// getting the request parameter
        	Object result = request.getParameter( param );
        	String expectedResult = "Java XML";

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

    protected String getPortletURL(RenderResponse response) {
        BaseURL baseURL = response.createRenderURL();

        baseURL.setParameter("Languages", "Java XML");
        return baseURL.toString(); 
    }
 }
