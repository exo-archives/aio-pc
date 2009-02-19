/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.ee.taglib;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.MapCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class tests the copyCurrentRenderParameters attribute of the actionURL tag.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> 
 */
public class ActionURLCopyCurrentRenderParametersTestPortlet extends GenericPortlet {
	
    private final String TEST_NAME = "ActionURLCopyCurrentRenderParametersTest";

    private final String SERVLET_NAME="ActionURLCopyCurrentRenderParametersTestServlet";
    
    private final String PROCESS_ACTION_RESULT="processActionResult";
    
    private Map<String,String[]> expectedParameterMap = new HashMap<String,String[]>(2);
    

    @Override
	public void init() throws PortletException {
		super.init();
		expectedParameterMap.put("language", new String[] {"Java", "C"});
        expectedParameterMap.put("OS", new String[] {"Solaris", "Linux"});
	}


	@Override
	public void processAction(ActionRequest request, ActionResponse response) 
		throws PortletException, IOException {
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		Map<String,String[]> resultParameters=request.getParameterMap();
		
        MapCompare mapCompare = new MapCompare(expectedParameterMap, resultParameters);

        if (mapCompare.misMatch()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail(mapCompare.getMisMatchReason());
        } 
        else {
            resultWriter.setStatus(ResultWriter.PASS);
        }
        
        response.setRenderParameter(PROCESS_ACTION_RESULT, resultWriter.toString());
	}


	@Override
	public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");    
        
        RequestCount requestCount = 
        	new RequestCount(request, response,
       						RequestCount.MANAGED_VIA_SESSION);
        
        if (requestCount.isFirstRequest()) {
        	PrintWriter out = response.getWriter();
        	
        	PortletURLTag renderTag = new PortletURLTag();
            PortletURL renderUrl = response.createRenderURL();
            renderUrl.setParameters(expectedParameterMap);
            renderTag.setTagContent(renderUrl.toString());
            out.println(renderTag.toString());
        }
        else{
        	String actionResult = request.getParameter(PROCESS_ACTION_RESULT);
        	
        	if(actionResult != null){
        		PrintWriter out = response.getWriter();
        		out.println(actionResult);
        	}
        	else{
        		PortletRequestDispatcher dispatcher = 
        			getPortletContext().getNamedDispatcher(SERVLET_NAME);
        		if (dispatcher == null) {
        			PrintWriter out = response.getWriter();
        			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        			resultWriter.setStatus(ResultWriter.FAIL);
        			resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
        					+ SERVLET_NAME);
        			out.println(resultWriter.toString());
        		} 
        		else {
        			dispatcher.include(request, response);
        		}
        	}
        }
    }
}
