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

import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class verify that only valid public-render-parameter send by the
 * container and only valid targets receive that parameters.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetPublicParameterOnlySeenByTargetTestPortlet extends
		GenericPortlet {
	
	public static final String TEST_NAME =
		"GetPublicParameterOnlySeenByTargetTest";
	
	public static final String PRIVATE_PARAMETER = "private-parameter";
	public static final String PUBLIC_PARAMETER = "public-parameter";
	
    public static Map<String, String[]> map = new HashMap<String, String[]>(2);
    
    static {
    	map.put(PRIVATE_PARAMETER, new String[] {"XML", "JAVA"});
    	map.put(PUBLIC_PARAMETER, new String[] {"C"});
    }
	
	public void render(RenderRequest request, RenderResponse response) 
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		RequestCount requestCount = new RequestCount(request,
											response,
											RequestCount.MANAGED_VIA_SESSION);
		
		if (requestCount.isFirstRequest()) {
			
			PortletURL portletURL = response.createRenderURL();
			portletURL.setParameters(map);
			
			PortletURLTag urlTag = new PortletURLTag();
			urlTag.setTagContent(portletURL.toString());
			
			out.println(urlTag.toString());
		}
	}
}
