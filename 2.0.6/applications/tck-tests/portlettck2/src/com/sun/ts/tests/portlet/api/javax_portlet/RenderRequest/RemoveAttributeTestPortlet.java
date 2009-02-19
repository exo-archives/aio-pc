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


/**
 * This portlet adds an attribute, then remove an attribute and then
 * check that attribute is not there in the request.
 */

public class RemoveAttributeTestPortlet extends GenericPortlet {

    public static String TEST_NAME="RemoveAttributeTest";
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        request.setAttribute( "BestLanguage", "Java" );
        request.removeAttribute("BestLanguage");
        String attr =(String ) request.getAttribute( "BestLanguage" );

        if(attr == null ) {
           resultWriter.setStatus(ResultWriter.PASS);
        } else {
           resultWriter.setStatus(ResultWriter.FAIL);
           resultWriter.addDetail( 
                "     PortletRequest.getAttribute() returned an "
                + " incorrect result " );
           resultWriter.addDetail( "     Expected result = " + null  );
           resultWriter.addDetail( "     Actual result = " + attr  );
        }
        out.println(resultWriter.toString());
    }
}
