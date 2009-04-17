/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;


import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;



/**
 *	A  Test for getContentLength
 */

public class GetContentLengthTestPortlet extends LogicInProcessActionPortlet {

    public static String TEST_NAME="GetContentLengthTest";

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {


        /**
         *	We get the content length using getContentLength and
         * 	we read from the input stream. if the number of chars read
         * 	matches the no returned by getContentLength
         *	we pass the test
         */

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);


        try {
            // get the content length
            int contentLength = request.getContentLength();
            int len = 0;
            StringBuffer buf = new StringBuffer();
            BufferedReader reader = request.getReader();
            int c;
    
            // getting input stream
    
            if(reader != null ) {
                // read from the reader 
    
                while((c = reader.read()) != -1 ) {
                    len++;
                }
    
                // did we get what we wrote
                if(( contentLength == len ) ||(contentLength == -1 ) ) {
                    resultWriter.setStatus(ResultWriter.PASS);
                } else {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail( 
                        "Expected Value returned ->" + contentLength );
                    resultWriter.addDetail( 
                        "Actual Value returned -> " + len );
                }
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail( 
                    "    PortletRequest.getReader() returned a null " );
            }

        } catch(IllegalStateException ise) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "    PortletRequest.getReader() threw an " +
                    " unexpected IllegalStateException. " );
        }
        request.getPortletSession(true).setAttribute("TestResult", resultWriter.toString());
    }

}
