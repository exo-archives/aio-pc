/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class SetAttributeTestPortlet extends GenericPortlet {

    public static String TEST_NAME="SetAttributeTest";
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        String expectedResult = "Java";
        request.setAttribute( "BestLanguage", expectedResult );
        String attr =(String ) request.getAttribute( "BestLanguage" );

        if(attr != null ) {
            if(attr.equals( expectedResult ) ) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail( 
                    "     PortletRequest.getAttribute() returned an "
                    + " incorrect result " );
                resultWriter.addDetail( 
                    "     Expected result = " + expectedResult  );
                resultWriter.addDetail( "     Actual result = " + attr  );
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "     PortletRequest.getAttribute() returned a "
                + " null result " );

        }
        out.println(resultWriter.toString());
    }
}
