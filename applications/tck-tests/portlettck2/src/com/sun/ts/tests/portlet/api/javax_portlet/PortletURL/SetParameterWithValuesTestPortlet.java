/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletURL;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.ListCompare;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 *	This class will test for PortletURL.setParameter() method by setting
 *  multiple values for a request parameter
 */

public class SetParameterWithValuesTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "SetParameterWithValuesTest";

   public void processAction(ActionRequest request, ActionResponse response ) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String param = "LANGUAGES";

        String[] paramValues = request.getParameterValues( param );
        String[] expectedValues = new String[] {"XML", "JAVA", "PERL"};
        String firstExpectedValue = expectedValues[0];

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
		request.getPortletSession(true).setAttribute("resultSetParameterWithValuesTest",resultWriter.toString(), PortletSession.PORTLET_SCOPE);
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
            out.println(request.getPortletSession(true).getAttribute("resultSetParameterWithValuesTest", PortletSession.PORTLET_SCOPE));
        }
    }

    protected String getPortletURL(RenderResponse response ) {

        PortletURL portletURL = response.createActionURL();
        String[] values = new String[] {"XML", "JAVA", "PERL"};

		portletURL.setParameter("LANGUAGES", values);

        return portletURL.toString(); 
    }
}
