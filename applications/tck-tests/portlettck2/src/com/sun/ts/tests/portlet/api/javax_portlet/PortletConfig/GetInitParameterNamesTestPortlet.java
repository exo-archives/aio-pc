/**
 * Copyright 2007 IBM Corporation.
 */
/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2006 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ListCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class tests the getInitParameterNames() method. This method
 *  returns an Enumeration of values associated with the init parameter.
 *
 *  @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *  @since v2.0
 */

public class GetInitParameterNamesTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetInitParameterNamesTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletConfig config = getPortletConfig();

        String[] expectedResults = 
                        new String[] {"FirstParameter", "SecondParameter"};

        Enumeration enumeration = config.getInitParameterNames();

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
            resultWriter.addDetail("getInitParameterNames() has an empty "
                                 + "enumeration " );
        }
        out.println(resultWriter.toString());
    }
}
