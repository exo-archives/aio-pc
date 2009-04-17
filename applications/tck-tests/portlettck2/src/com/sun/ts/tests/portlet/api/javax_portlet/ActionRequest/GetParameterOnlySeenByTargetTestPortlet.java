/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;

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
 *	A  portlet participating in the test that checks that the parameters 
 * are seen by only the target portlet.
 * This is the target portlet, one of the two portlets used for the test.
 * It writes an action URL on the first request.
 * It checks for expected parameter in the second request.
 */


public class GetParameterOnlySeenByTargetTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetParameterOnlySeenByTargetTest";
    public static String PARAMETER_NAME = "BestLanguage";

    // called only when there is an action for this portlet.
    public void processAction(ActionRequest request, ActionResponse response ) throws PortletException, IOException {
        ResultWriter resultWriter = null;
        // getting the request parameter
        String result = request.getParameter( PARAMETER_NAME );
        String expectedResult = "Java";

        if(result == null ) {
            resultWriter = new ResultWriter(TEST_NAME);
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "PortletRequest.getParameter(" + PARAMETER_NAME + ") "
                + "returned a null result " );
		    request.getPortletSession().setAttribute(
                "GetParameterOnlySeenByTargetTestResult", 
                resultWriter.toString());
		}
    }

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
            String str = (String)request.getPortletSession().getAttribute(
                "GetParameterOnlySeenByTargetTestResult");
            if(str != null) {
                out.println(str);
            }
        }
    }

    protected String getPortletURL(RenderResponse response ) {
        PortletURL portletURL = response.createActionURL();
        portletURL.setParameter(PARAMETER_NAME, "Java");
        return portletURL.toString(); 
    }
}
