/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2006 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import java.util.Enumeration;
import java.util.Vector;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.ListCompare;

/**
 *	This class tests getAttributeNames() method.
 *
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 * @since v2.0
 */

public class GetAttributeNamesTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetAttributeNamesTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletContext context = getPortletContext();

        if (context != null) {

            context.setAttribute( "Chef", "expert" );
            context.setAttribute( "chief", "commanding" );

            String[] expectedResults = new String[] {"Chef", "chief"};
            Enumeration enumeration = context.getAttributeNames();

            if (enumeration != null) {

                ListCompare listCompare = new ListCompare(expectedResults,
                                                          enumeration,
                                                          null,
                                                ListCompare.SUBSET_MATCH);
                if (!listCompare.misMatch()) {
                    resultWriter.setStatus(ResultWriter.PASS);
                } else {
			        resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail(listCompare.getMisMatchReason());
                }
             } else {
			    resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("getAttributeNames() returned an" 
                                     + " empty enumeration" );
             }
        } else {
			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletContext() returned null");
        }
		out.println(resultWriter.toString());
    }
}
