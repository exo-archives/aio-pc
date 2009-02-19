/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.ee.taglib;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.MapCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class tests the copyCurrentRenderParameters attribute of the renderURL tag.
 * It checks whether the renderURL tag copies the current render parameters and 
 * prepends the parameters that are defined with the param tag.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */
public class RenderURLCopyCurrentRenderParametersAppendTestPortlet extends GenericPortlet {
	
	private final String TEST_NAME = 
		"RenderURLCopyCurrentRenderParametersAppendTest";

	private final String SERVLET_NAME =
		"RenderURLCopyCurrentRenderParametersAppendTestServlet";


	@Override
	public void render(RenderRequest request, RenderResponse response)
		throws PortletException, IOException {

		Map<String,String[]> parameterMap = new HashMap<String,String[]>(2);
		parameterMap.put("language", new String[] {"Java", "C"});
		parameterMap.put("OS", new String[] {"Solaris", "Linux"});

		response.setContentType("text/html");    

		RequestCount requestCount = 
			new RequestCount(request, response,
				RequestCount.MANAGED_VIA_SESSION);

		if (requestCount.isFirstRequest()){//create render PortletURL with parameters
			PrintWriter out = response.getWriter();

			PortletURLTag renderTag = new PortletURLTag();
			PortletURL renderUrl = response.createRenderURL();
			renderUrl.setParameters(parameterMap);
			renderTag.setTagContent(renderUrl.toString());
			
			out.println(renderTag.toString());
		}
		else{
			PortletSession session = request.getPortletSession();	            
			
			if(session.getAttribute(TEST_NAME) != null){//check result
				session.removeAttribute(TEST_NAME);
				
				Map<String,String[]> expectedParameterMap = new HashMap<String,String[]>(3);
				expectedParameterMap.put("language", new String[] {"Ada", "Pascal", "Java", "C"});
				expectedParameterMap.put("IDE", new String[] {"Eclipse"});

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
				PrintWriter out = response.getWriter();
				out.println(resultWriter.toString());
			}
			else{//include servlet
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
					session.setAttribute(TEST_NAME,TEST_NAME);
					dispatcher.include(request, response);
				}
			}
		}
	}
}
