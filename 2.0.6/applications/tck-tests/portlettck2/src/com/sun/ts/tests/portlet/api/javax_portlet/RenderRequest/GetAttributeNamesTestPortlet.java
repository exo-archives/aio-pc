/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2006 IBM Corporation
 */



package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;


import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import java.util.Enumeration;
import java.util.Vector;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.ListCompare;

/**	
 * Sets couple of attributes and makes sure
 * getAttributeNames() returns exact same attributes.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 * @since v2.0
 */

public class GetAttributeNamesTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetAttributeNamesTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);


        /*
        ** Set attributes in the request
        */

        request.setAttribute( "BestLanguage", "Java" );
        request.setAttribute( "BestJSP", "Java2" );
        String[] expectedNames = new String[] {"BestLanguage", "BestJSP"};


        /*
        ** Extract attributes from the request
        */

        Enumeration enumeration = request.getAttributeNames();
        ListCompare listCompare = new ListCompare(expectedNames,
                                                enumeration,
                                                null,
                                                ListCompare.SUBSET_MATCH);

        /*
        ** Compare
        */

        if(listCompare.misMatch()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail(
                "PortletRequest.getAttributeNames() did not return expected "
                + " values.");
            resultWriter.addDetail(listCompare.getMisMatchReason());
        }
        else {
            resultWriter.setStatus(ResultWriter.PASS);
        }
            

        out.println(resultWriter.toString());
    }
}
