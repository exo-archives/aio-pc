/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;


import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;


/**
 * First request is to a portlet that writes a Action URL
 *  to the response object. In the second request in the
 *  processAction() method the getInputStream() method is
 *  invoked. Test passes if no exception is thrown and the
 *  client contents are successfully read when this method
 *  is executed.
 */


public class GetInputStreamInProcessActionTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetInputStreamInProcessActionTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {
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
            out.println(
                request.getPortletSession().getAttribute("GetInputStreamInProcessActionResult"));
        }
    }

   public void processAction(ActionRequest request, ActionResponse response ) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        try {
            StringBuffer buf = new StringBuffer();
            InputStream stream = request.getPortletInputStream();
            int c;

            // getting input stream

            if(stream != null ) {
                // read from the reader

                while((c = stream.read()) != -1 ) {
                   buf.append((char)c);
                }

                // did we get what we wrote
                if (( buf.toString().equals(CommonConstants.CLIENT_CONTENT))) {
                    resultWriter.setStatus(ResultWriter.PASS);
                } else {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail( 
                        "     Expected Value returned ->" 
                        + CommonConstants.CLIENT_CONTENT );
                    resultWriter.addDetail( 
                        "     Actual Value returned -> " + buf.toString() );
                }
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail( 
                    "    PortletRequest.getInputStream() returned a null " );
             }
          } catch(IOException ex) {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail( 
                    "    PortletRequest.getInputStream() threw IOException");
            }
		 request.getPortletSession().setAttribute(
            "GetInputStreamInProcessActionResult",resultWriter.toString());
    }

    protected String getPortletURL(RenderResponse response ) {

        PortletURL portletURL = response.createActionURL();

        return portletURL.toString(); 
    }
}
