/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletURLListener;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class IsPortletURLInPortletURLGenerationListenerTestPortlet extends GenericPortlet {
	public static final String TEST_NAME = "IsPortletURLInPortletURLGenerationListenerTest";
	private static String URL_FILTER_PARAMETER = "URLFilterParameterIsPortletURLInPortletURLGenerationListenerTest";

	@Override
    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        RequestCount requestCount
            = new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (requestCount.isFirstRequest()) {
            PortletURL url = response.createRenderURL();
            PortletURLTag urlTag = new PortletURLTag();
            urlTag.setTagContent(url.toString());        
            out.println(urlTag.toString());
        } else {
        	String result = request.getParameter(URL_FILTER_PARAMETER);
        	if (result != null){
        		if (result.equals("portletURL")){
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
}
