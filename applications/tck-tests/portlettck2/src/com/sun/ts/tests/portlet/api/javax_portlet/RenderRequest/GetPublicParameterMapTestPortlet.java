/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.MapCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class tests the access to the private and public parameter map. 
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetPublicParameterMapTestPortlet extends GenericPortlet {
	
	public static final String TEST_NAME =
		"GetPublicParameterMapTest";
	
	public static final String PRIVATE_PARAMETER = "private-parameter";
	public static final String PUBLIC_PARAMETER = "public-parameter";
    
	public void render(RenderRequest request, RenderResponse response) 
		throws PortletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		RequestCount requestCount = new RequestCount(request,
											response,
											RequestCount.MANAGED_VIA_SESSION);
		
		Map<String, String[]> privateParameter = new HashMap<String, String[]>(1);
		Map<String, String[]> publicParameter = new HashMap<String, String[]>(1);
		
		privateParameter.put(PRIVATE_PARAMETER, new String[] {"XML", "JAVA"});
		publicParameter.put(PUBLIC_PARAMETER, new String[] {"C"});
		
		if (requestCount.isFirstRequest()) {
			
			PortletURL portletURL = response.createRenderURL();
			
			for (String key : privateParameter.keySet())
				portletURL.setParameter(key, privateParameter.get(key));
			
			for (String key : publicParameter.keySet())
				portletURL.setParameter(key, publicParameter.get(key));
			
			PortletURLTag urlTag = new PortletURLTag();
			urlTag.setTagContent(portletURL.toString());
			
			out.println(urlTag.toString());
			
		} else {
			
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			
			Map<String, String[]> mergedMap = request.getParameterMap();
			
			Map<String, String[]> mergedExpected = new HashMap<String, String[]>();
			mergedExpected.putAll(privateParameter);
			mergedExpected.putAll(publicParameter);
			
			MapCompare mergedCompare = new MapCompare(mergedExpected, mergedMap);
			
			if (mergedCompare.misMatch()) {
				
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail(mergedCompare.getMisMatchReason());
				
			}
			
			Map<String, String[]> actualPrivateMap = request.getPrivateParameterMap();
			MapCompare privateCompare = new MapCompare(privateParameter, actualPrivateMap);
			
			if (privateCompare.misMatch()) {
				
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail(privateCompare.getMisMatchReason());
				
			}
			
			Map<String, String[]> actualPublicMap = request.getPublicParameterMap();
			MapCompare publicCompare = new MapCompare(publicParameter, actualPublicMap);
			
			if (publicCompare.misMatch()) {
				
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail(publicCompare.getMisMatchReason());
				
			}
			
			if (!(mergedCompare.misMatch()
					|| privateCompare.misMatch()
					|| publicCompare.misMatch()))
				resultWriter.setStatus(ResultWriter.PASS);
			
			out.println(resultWriter.toString());
			
		}
	}
}
