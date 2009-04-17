/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ActionResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.MapCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class tests the setRenderParameters() method.
 */
public class SetRenderParametersTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "SetRenderParametersTest";

    public void processAction(ActionRequest request,
                              ActionResponse actionResponse)
        throws PortletException, IOException {

        actionResponse.setRenderParameter(TEST_NAME, TEST_NAME + "duh");
        Map parameters = new HashMap(1);
        parameters.put(TEST_NAME, new String[] {TEST_NAME});

        // The previously set parameter should be cleared by this call.
        actionResponse.setRenderParameters(parameters);
    }

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        RequestCount requestCount
            = new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (requestCount.isFirstRequest()) {
            PortletURL url = response.createActionURL();
            PortletURLTag urlTag = new PortletURLTag();
            urlTag.setTagContent(url.toString());        
            out.println(urlTag.toString());
        } else {
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
            Map expectedResult = new HashMap(1);
            expectedResult.put(TEST_NAME, new String[] {TEST_NAME});
            Map result = request.getParameterMap();
            MapCompare mapCompare = new MapCompare(expectedResult, result);

            if (mapCompare.misMatch()) {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail(mapCompare.getMisMatchReason());
            } else {
                resultWriter.setStatus(ResultWriter.PASS);
            }

            out.println(resultWriter.toString());            
        }
    }
}
