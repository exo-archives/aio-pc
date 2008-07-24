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
 * This is the second portlet participating in this test. 
 * This test spans on three request and two portlets.
 * In the first and third request, this portlet does nothing.
 * In the second request, this second test portler will set some
 * other request parameters and return a render URL. 
 */

public class GetRenderParametersForOnlyTargetTest_1_Portlet extends GenericPortlet {

    public static String TEST_NAME= "GetRenderParametersForOnlyTargetTest";
    public static String PARAMETER_NAME = "BestLanguage";
    
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        RequestCount reqCount = new RequestCount(request, response, 
										RequestCount.MANAGED_VIA_SESSION);

        if (reqCount.getRequestNumber() == 1) {
            //write a portlet url to the outputstream
            PortletURLTag customTag = new PortletURLTag();
            customTag.setTagContent(getPortletURL(response));        
            out.println(customTag.toString());
        }
    }

    protected String getPortletURL(RenderResponse response ) {
        PortletURL portletURL = response.createRenderURL();
        portletURL.setParameter(PARAMETER_NAME, "Solaris");
        return portletURL.toString(); 
    }
}
