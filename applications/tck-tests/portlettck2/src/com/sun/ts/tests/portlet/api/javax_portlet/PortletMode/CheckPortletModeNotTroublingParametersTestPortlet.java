/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletMode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.MapCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class will test, if change the portlet mode not disturb the
 * render parameters.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 */

public class CheckPortletModeNotTroublingParametersTestPortlet extends
		GenericPortlet {
	
	public static final String TEST_NAME =
		"CheckPortletModeNotTroublingParametersTest";
	
    static Map<String, String[]> map = new HashMap<String, String[]>(2);
    
    static {
    	map.put("LANGUAGES", new String[] {"XML", "JAVA"});
    	map.put("BestLanguage", new String[] {"C"});
    }
	
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		 
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
	
		PortletURL portletURL = response.createRenderURL();
		portletURL.setParameters(map);
		portletURL.setPortletMode(PortletMode.EDIT);
		
		PortletURLTag urlTag = new PortletURLTag();
		urlTag.setTagContent(portletURL.toString());
		
		out.println(urlTag.toString());

	}
	
	public void doEdit(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		Map<String, String[]> actualMap = request.getParameterMap();
		
		MapCompare mapCompare = new MapCompare(map, actualMap);
		
		ResultWriter writer = new ResultWriter(TEST_NAME);
		
		if (!mapCompare.misMatch())
			writer.setStatus(ResultWriter.PASS);
		else {
			
			writer.setStatus(ResultWriter.FAIL);
			writer.addDetail(mapCompare.getMisMatchReason());
		}
		
		out.println(writer.toString());
	}
}
