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
import java.util.Enumeration;
import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 * This portlet sets an attribute and then sets it again with value null, 
 * then 
 * checks that attribute is not there in the request.
 */

public class SetAttributeNullTestPortlet extends LogicInProcessActionPortlet {

    public static String TEST_NAME="SetAttributeNullTest";

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        request.setAttribute( "BestLanguage", "Java" );
        request.setAttribute( "BestLanguage", null );
        Enumeration attrNames = request.getAttributeNames();

        resultWriter.setStatus(ResultWriter.PASS);
        if ( attrNames != null) {
            while(attrNames.hasMoreElements()) {
                String attr = (String)attrNames.nextElement();

                if (attr.equals("BestLanguage")) {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail(" PortletRequest.getAttributeNames"
                            + " returned BestLanguage attribute, even when"
                            + " a setAttribute with null was called for it.");
                }
            }
        }
        request.getPortletSession(true).setAttribute("TestResult", resultWriter.toString());
    }
}
