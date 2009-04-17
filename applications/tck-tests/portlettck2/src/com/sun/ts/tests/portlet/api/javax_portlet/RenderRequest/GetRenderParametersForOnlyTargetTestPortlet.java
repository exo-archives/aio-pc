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
 * This is one of the portlets, participating in the test.
 * This test spans on three request and two portlets.
 * In the first request, this portlet will set some
 * render parameters and return a render URL. In the
 * second request which is targeted to another portlet, this portlet does
 * nothing.
 * In the third request this first portlet will check for the
 * parameters that was set in the first request. Test passes
 * if this portlet is able to retrieve the original 
 * request parameters.
 */

public class GetRenderParametersForOnlyTargetTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetRenderParametersForOnlyTargetTest";
    public static String PARAMETER_NAME = "BestLanguage";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
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
            if (reqCount.getRequestNumber() == 2) {
               // getting the request parameter
               String result = request.getParameter( PARAMETER_NAME );
               String expectedResult = "Java";

               if (result != null && result.equals(expectedResult)) {
                   resultWriter.setStatus(ResultWriter.PASS);
               } else {
                   resultWriter.setStatus(ResultWriter.FAIL);
                   resultWriter.addDetail(
                        "Expected: Parameter value = " + expectedResult);
                   resultWriter.addDetail(
                        "Actual:Parameter value = " + result);
               }
            }
            out.println(resultWriter.toString());
        }
    }

    protected String getPortletURL(RenderResponse response ) {
        PortletURL portletURL = response.createRenderURL();
        portletURL.setParameter(PARAMETER_NAME, "Java");
        return portletURL.toString(); 
    }
}
