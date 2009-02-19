/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletURL;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class CheckDefaultPortletModeTestPortlet extends GenericPortlet {
	public static String TEST_NAME="CheckDefaultPortletModeTest";
	
	public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		RequestCount reqCount = new RequestCount(request, response,
										RequestCount.MANAGED_VIA_SESSION);

		if (reqCount.isFirstRequest()) {

			PortletURLTag customTag = new PortletURLTag();
			customTag.setTagContent(getPortletURL(response));
			out.println(customTag.toString());
		} else if (reqCount.getRequestNumber() == 1){
			//This is the second request.
			PortletURLTag customTag = new PortletURLTag();
			customTag.setTagContent(getPortletURLNoChange(response));
			out.println(customTag.toString());
		}
		else{
			//This is the third request. Checks the portlet mode.
			if (request.getPortletMode().equals(PortletMode.EDIT)){
				resultWriter.setStatus(ResultWriter.PASS);
			}
			else{
				resultWriter.setStatus(ResultWriter.FAIL);
            	resultWriter.addDetail("request.getPortletMode isn't expected Mode" );
            	resultWriter.addDetail("Expected result = PortletMode.EDIT");
            	resultWriter.addDetail("Actual result = " 
                                            + request.getPortletMode().toString());
			}
		}
		out.println(resultWriter.toString());
	}
	protected String getPortletURL(RenderResponse response) throws PortletModeException {
        PortletURL portletURL = response.createRenderURL();
        portletURL.setPortletMode(PortletMode.EDIT);
        return portletURL.toString(); 
    }
	protected String getPortletURLNoChange(RenderResponse response) throws PortletModeException {
        PortletURL portletURL = response.createRenderURL();
        return portletURL.toString(); 
    }
}
