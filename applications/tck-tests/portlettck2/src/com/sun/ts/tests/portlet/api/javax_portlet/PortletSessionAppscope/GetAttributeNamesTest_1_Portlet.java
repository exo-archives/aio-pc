/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2006 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSessionAppscope;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletConfig;
import java.util.Enumeration;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;


import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.ListCompare;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;



/**
 * 	A test for PortletSession.getAttributeNames() method
 * The second portlet that gets the attributes written in first portlet - 
 * GetAttributeNamesTestPortlet
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 * @since v2.0
 */


public class GetAttributeNamesTest_1_Portlet extends GenericPortlet {

    public static String TEST_NAME = "GetAttributeNamesTest";



    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession( true );
        RequestCount reqCount = new RequestCount(request, response,
                                            RequestCount.MANAGED_VIA_SESSION);

        if (!reqCount.isFirstRequest()) {

            int count = 0;
            int expectedCount = 2;
            String expectedResult[] = new String[] {
                                GetAttributeNamesTestPortlet.FIRST_ATTRIBUTE,
                                GetAttributeNamesTestPortlet.SECOND_ATTRIBUTE
                                };
            Enumeration enumeration = 
                    session.getAttributeNames(PortletSession.APPLICATION_SCOPE);

            // Spec should say the behavior about 
            // what container can put.(SUBSET or EQUAL).
            ListCompare listCompare = new ListCompare(expectedResult,
                                                enumeration,
                                                null,
                                                ListCompare.SUBSET_MATCH);

            
            /*
            ** Compare
            */

            if(listCompare.misMatch()) {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("PortletSession.getAttributeNames() didn't"
                                + " return expected values.");
                resultWriter.addDetail(listCompare.getMisMatchReason());
            }
            else {
                resultWriter.setStatus(ResultWriter.PASS);
            }

            out.println(resultWriter.toString());
        }
    }
}
