/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSessionDistributableapp;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletURL;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;


public class CheckPortletInDistributeAppTestPortlet extends GenericPortlet {

    public static String TEST_NAME="CheckPortletInDistributeAppTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		RequestCount reqCount = new RequestCount(request, response,
										RequestCount.MANAGED_VIA_SESSION);
        int num = 10;
        PortletSession session = request.getPortletSession();

		if (reqCount.isFirstRequest()) {
             SampleSerializable serialClass = new SampleSerializable(num);

            if(session != null) {
                session.setAttribute("distributeApp", serialClass, PortletSession.APPLICATION_SCOPE);
            }
			//write a portlet url to the outputstream
			PortletURLTag customTag = new PortletURLTag();
			customTag.setTagContent(getPortletURL(response));
			out.println(customTag.toString());
        }
        else {
            SampleSerializable newSerialClass = 
                (SampleSerializable)session.getAttribute("distributeApp", PortletSession.APPLICATION_SCOPE);

            if (newSerialClass != null) {
                if (newSerialClass.getNum() == num) {
                    resultWriter.setStatus(ResultWriter.PASS);
                } else {
                   resultWriter.setStatus(ResultWriter.FAIL);
                   resultWriter.addDetail(
                            "Expected value to be read from serialized object = " + num);
                   resultWriter.addDetail(
                            "Actual value read from serialized object= " + newSerialClass.getNum());
                }
            } else {
               resultWriter.setStatus(ResultWriter.FAIL);
               resultWriter.addDetail("The method session.getAttribute() returned null on the second request");
            }
          out.println(resultWriter.toString());
        }
    }

    protected String getPortletURL(RenderResponse response ) {
        PortletURL portletURL = response.createRenderURL();
        return portletURL.toString(); 
    }
}
