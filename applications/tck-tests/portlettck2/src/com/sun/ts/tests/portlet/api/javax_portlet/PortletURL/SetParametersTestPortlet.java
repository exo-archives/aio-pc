/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletURL;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
 * This class tests the setParameters() method.
 */
public class SetParametersTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "SetParametersTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Map parameters = new HashMap(2);
        parameters.put("language", new String[] {"Java", "C"});
        parameters.put("OS", new String[] {"Solaris", "Linux"});

        RequestCount requestCount
            = new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (requestCount.isFirstRequest()) {
            PortletURL url = response.createRenderURL();
            url.setParameters(parameters);
            PortletURLTag urlTag = new PortletURLTag();
            urlTag.setTagContent(url.toString());        
            out.println(urlTag.toString());
        } else {
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
            Map expectedResult = parameters;
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
