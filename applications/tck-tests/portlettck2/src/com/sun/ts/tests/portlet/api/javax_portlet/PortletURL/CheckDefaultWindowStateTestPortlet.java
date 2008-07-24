/*
 * Copyright 2007 IBM Corporation
 */
package com.sun.ts.tests.portlet.api.javax_portlet.PortletURL;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletModeException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class CheckDefaultWindowStateTestPortlet extends GenericPortlet{
	public static String TEST_NAME="CheckDefaultWindowStateTest";
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
			//This is the third request. Checks the Window State.
			if (request.getWindowState().equals(WindowState.MAXIMIZED)){
				resultWriter.setStatus(ResultWriter.PASS);
			}
			else{
				resultWriter.setStatus(ResultWriter.FAIL);
            	resultWriter.addDetail("request.getWindowState isn't expected State" );
            	resultWriter.addDetail("Expected result = WindowState.MAXIMIZED");
            	resultWriter.addDetail("Actual result = " 
                                            + request.getWindowState().toString());
			}
		}
		out.println(resultWriter.toString());
	}
	protected String getPortletURL(RenderResponse response) throws WindowStateException {
        PortletURL portletURL = response.createRenderURL();
        portletURL.setWindowState(WindowState.MAXIMIZED);
        return portletURL.toString(); 
    }
	protected String getPortletURLNoChange(RenderResponse response) throws PortletModeException {
        PortletURL portletURL = response.createRenderURL();
        return portletURL.toString(); 
    }
}
