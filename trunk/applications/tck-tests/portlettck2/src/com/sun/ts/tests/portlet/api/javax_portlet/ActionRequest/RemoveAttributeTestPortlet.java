/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 * This portlet adds an attribute, then remove an attribute and then
 * check that attribute is not there in the request.
 */

public class RemoveAttributeTestPortlet extends LogicInProcessActionPortlet {

    public static String TEST_NAME="RemoveAttributeTest";

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {

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
        request.getPortletSession(true).setAttribute("TestResult", resultWriter.toString());
    }
}
