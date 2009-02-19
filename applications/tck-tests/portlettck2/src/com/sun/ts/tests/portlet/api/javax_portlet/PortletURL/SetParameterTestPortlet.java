/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletURL;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;


/**
 *	This class will test for PortletURL.setParameter() method.
 */

public class SetParameterTestPortlet extends GenericPortlet {

    public static String TEST_NAME="SetParameterTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		RequestCount reqCount = new RequestCount(request, response,
										RequestCount.MANAGED_VIA_SESSION);

		if (reqCount.isFirstRequest()) {

			//write a portlet url to the outputstream
			PortletURLTag customTag = new PortletURLTag();
			customTag.setTagContent(getPortletURL(response));
			out.println(customTag.toString());
        }
        else {
            out.println(request.getPortletSession(true).getAttribute("resultSetParameterTest", PortletSession.PORTLET_SCOPE));
        }

    }

    protected String getPortletURL(RenderResponse response ) {
        PortletURL portletURL = response.createActionURL();
        portletURL.setParameter("BestLanguage", "Java");
        return portletURL.toString(); 
            
    }

    public void processAction(ActionRequest request, ActionResponse response ) throws PortletException, IOException {
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
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
		request.getPortletSession(true).setAttribute("resultSetParameterTest", resultWriter.toString(), PortletSession.PORTLET_SCOPE);
    }
}
