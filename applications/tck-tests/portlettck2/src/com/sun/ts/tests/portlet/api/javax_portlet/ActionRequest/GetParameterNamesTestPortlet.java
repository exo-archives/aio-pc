/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2006 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;

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
import java.util.Set;
import java.util.Map;
import java.util.Enumeration;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.ListCompare;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;


/**
* First request to the portlet writes a
* action portletURL with paramaters in it, to the output stream.
* The portlet URL string is extracted and used for second
* request. In the second request, in processAction,
* portlet uses PortletRequest.getParameterNames()
* to see if the expected parameter is returned.
* 
* @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
* @since v2.0
*/


public class GetParameterNamesTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetParameterNamesTest";
	
	public void processAction(ActionRequest request, ActionResponse response ) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        String[] expectedResult = new String[] {
                                    "BestLanguage",
                                    "BestJSP"
                                };
        Enumeration enumeration = request.getParameterNames();
        ListCompare compare = new ListCompare(
                                    expectedResult,
                                    enumeration,
                                    null,
                                    ListCompare.ALL_ELEMENTS_MATCH);
        if (compare.misMatch()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("PortletRequest.getParameterNames(" +
                            ") returned unexpected results.");
            resultWriter.addDetail(compare.getMisMatchReason());
        }
        else {
            resultWriter.setStatus(ResultWriter.PASS);
        }

        request.getPortletSession().setAttribute(
                "GetParameterNamesTestResult", 
                resultWriter.toString());

        }

        public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {
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
                out.println(request.getPortletSession().getAttribute("GetParameterNamesTestResult"));
            }
        }

            
    protected String getPortletURL(RenderResponse response ) {

        PortletURL portletURL = response.createActionURL();
		portletURL.setParameter("BestLanguage", "Java");
		portletURL.setParameter("BestJSP", "Java2");		

        return portletURL.toString(); 
    }

   
}
