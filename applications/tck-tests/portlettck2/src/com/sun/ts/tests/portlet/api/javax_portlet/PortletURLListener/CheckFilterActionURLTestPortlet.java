/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletURLListener;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class CheckFilterActionURLTestPortlet extends GenericPortlet {
	public static final String TEST_NAME = "CheckFilterActionURLTest";
	private static String URL_FILTER_PARAMETER = "URLFilterParameterURLFilter";

	@Override
    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        RequestCount requestCount
            = new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (requestCount.isFirstRequest()) {
            PortletURL url = response.createActionURL();
            PortletURLTag urlTag = new PortletURLTag();
            urlTag.setTagContent(url.toString());        
            out.println(urlTag.toString());
        } else {
        	PortletSession session = request.getPortletSession();
        	String result = (String)session.getAttribute(URL_FILTER_PARAMETER);
        	if (result != null){
        		if (result.equals("action")){
        			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
                    resultWriter.setStatus(ResultWriter.PASS);
                    out.println(resultWriter.toString()); 
        		}
        		else{
        			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("Expected result: action");
                    resultWriter.addDetail("result: " + result);
                    out.println(resultWriter.toString()); 
        		}
        	}
        	else{
        		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Parameter wasn't set to the URL.");
                out.println(resultWriter.toString()); 
        	}
                       
        }
    }

	@Override
	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
		//check the parameter
		PortletSession session = request.getPortletSession();
		session.setAttribute(URL_FILTER_PARAMETER, request.getParameter(URL_FILTER_PARAMETER));
	}
}
