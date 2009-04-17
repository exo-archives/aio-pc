/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletURL;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;


/**
 *	Test for PortletRequest.getParameterValues() method
 * First request to the portlet writes a portletURL with
 * a parameter having 1 value. The portlet URL string is
 * extracted and used for second request. The portlet uses
 * PortletRequest.getParameterValues() to check the returned 
 * array.
 */

public class CheckSingleParameterValuesTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "CheckSingleParameterValuesTest";

   public void computeResults(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String param = "LANGUAGES";

        String[] paramValues = request.getParameterValues( param );
        String expectedValue = "JAVA";

        if ((paramValues.length == 1) &&
            (paramValues[0] != null) &&
            (paramValues[0].equals(expectedValue))) {
                resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected a array of size one with:JAVA");
        }
        request.getPortletSession(true).setAttribute("GetSingleParameterValuesResult",resultWriter.toString());
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
        } else {
            computeResults(request, response);
            out.println(request.getPortletSession(true).getAttribute("GetSingleParameterValuesResult"));
        }
    }

    protected String getPortletURL(RenderResponse response ) {

        PortletURL portletURL = response.createRenderURL();
		portletURL.setParameter("LANGUAGES", "JAVA");

        return portletURL.toString(); 
    }
}
