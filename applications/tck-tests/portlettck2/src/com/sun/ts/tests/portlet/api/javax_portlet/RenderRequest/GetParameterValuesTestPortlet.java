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
import java.util.Enumeration;
import java.util.Vector;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.ListCompare;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;



/**
* First request to the portlet writes a
* action portletURL with a paramater with multiple values in it, 
* to the output stream.
* The portlet URL string is extracted and used for second
* request. In the second request, 
* portlet uses PortletRequest.getParameterValues()
* to see if the expected parameter values are returned.
*/



public class GetParameterValuesTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetParameterValuesTest";

   public void computeResults(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String param = "LANGUAGES";

        String[] paramValues = request.getParameterValues( param );
        String[] expectedValues = new String[] {"XML", "JAVA"};
        String firstExpectedValue = request.getParameter(param);

        ListCompare compare = new ListCompare(
                                    expectedValues, 
                                    paramValues,
                                    firstExpectedValue,
                                    ListCompare.ALL_ELEMENTS_MATCH);
        if (compare.misMatch()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("PortletRequest.getParameterValues(" +
                        param +
                        " returned unexpected results.");
            resultWriter.addDetail(compare.getMisMatchReason());
        }
        else {
            resultWriter.setStatus(ResultWriter.PASS);
        }
		request.getPortletSession(true).setAttribute("GetParameterValuesResult",resultWriter.toString());
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
            out.println(request.getPortletSession(true).getAttribute("GetParameterValuesResult"));
        }
    }

    protected String getPortletURL(RenderResponse response ) {

        PortletURL portletURL = response.createRenderURL();
        String[] values = new String[] {"XML", "JAVA"};

		portletURL.setParameter("LANGUAGES", values);

        return portletURL.toString(); 
    }
}
