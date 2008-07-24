/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletURL;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;


/**
 * First request to the portlet writes a
 * action portletURL with a paramater in it, to the output stream.
 * The portlet URL string is extracted and used for second
 * request. In the second request, 
 * portlet uses PortletRequest.getParameter()
 * to see if the expected parameter is returned.
 */


public class GetParameterTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetParameterTest";

    /*
     * called only in second request, to check for paramter written 
     */

    public void computeResults(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String param = "BestLanguage";

        // getting the request parameter
        String result = request.getParameter( param );
        String expectedResult = "Java is great.";

        if(result != null ) {
            if (result.equals( expectedResult)) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail( 
                    "PortletRequest.getParameter(" + param + ") returned an "
                    + "incorrect result" );
                resultWriter.addDetail( 
                    "     Expected result = " + expectedResult  );
                resultWriter.addDetail( 
                    "     Actual result = |" +(String ) result  );
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "PortletRequest.getParameter(" 
                + param 
                + ") returned a null result " );
        }
		request.getPortletSession(true).setAttribute("GetParameterTestResult", resultWriter.toString());
    }


    /**
     * In first request, writes an actionURL with parameter to the output steam.
     * In second request, writes the test results
     */

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestCount reqCount = new RequestCount(request,response,
										RequestCount.MANAGED_VIA_SESSION);
        if (reqCount.isFirstRequest()) {
            //write a portlet url to the outputstream
            PortletURLTag customTag = new PortletURLTag();
            customTag.setTagContent(getPortletURL(response));        
            out.println(customTag.toString());
        }
        else {
            computeResults(request, response);
            out.println(request.getPortletSession(true).getAttribute("GetParameterTestResult"));
        }
    }

    protected String getPortletURL(RenderResponse response ) {

        PortletURL portletURL = response.createRenderURL();
        portletURL.setParameter("BestLanguage", "Java is great.");
        return portletURL.toString(); 
    }
}
