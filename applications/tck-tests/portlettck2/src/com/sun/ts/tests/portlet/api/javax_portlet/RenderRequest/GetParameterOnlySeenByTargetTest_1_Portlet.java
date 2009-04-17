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
 *	A  portlet paticipating in the test that checks that the parameters are 
 * seen by only the target portlet.
 * This is the untargetted portlet, one of the two portlets used for the test.
 */


public class GetParameterOnlySeenByTargetTest_1_Portlet extends GenericPortlet {

    public static String TEST_NAME=
                    GetParameterOnlySeenByTargetTestPortlet.TEST_NAME;
    
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        RequestCount reqCount = new RequestCount(request, response, 
										RequestCount.MANAGED_VIA_SESSION);

        if (!reqCount.isFirstRequest()) {
            if(request.getParameter(
                GetParameterOnlySeenByTargetTestPortlet.PARAMETER_NAME) 
                                                    != null) {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail(
                    "Untargeted portlet can see the parameters set by "
                    + " targeted portlets.");
            }
            else {
                resultWriter.setStatus(ResultWriter.PASS);
            }
            out.println(resultWriter.toString());
        }
    }
}
