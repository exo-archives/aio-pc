/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ResourceRequest;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;

import com.sun.ts.tests.portlet.common.util.MapCompare;

/**
 * Test for ResourceRequest.getParameterMap() method
 * First request to the portlet writes a
 * action resourceURL with few paramaters in it, to the output stream.
 * The portlet URL string is extracted and used for second
 * request. In the second request, 
 * portlet uses ResourceRequest.getParameterMap()
 * to see if the expected parameter map is returned and
 * unmodifiable.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 */


import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class GetParameterMapTestPortlet extends GenericPortlet {

    static public final String TEST_NAME = 
    	"GetParameterMapTest";
    
    static Map<String, String[]> map = new HashMap<String, String[]>(2);
    
    static {
    	map.put("LANGUAGES", new String[] {"XML", "JAVA"});
    	map.put("BestLanguage", new String[] {"C"});
    }

    public void render(RenderRequest request, RenderResponse response )
    	throws PortletException, java.io.IOException {
    	
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        PortletURLTag customTag = new PortletURLTag();
        ResourceURL resourceURL = response.createResourceURL();
        resourceURL.setParameters(map);
        customTag.setTagContent(resourceURL.toString());
        
        out.println(customTag.toString());
    }
    
    public void serveResource(ResourceRequest request, ResourceResponse response)
    	throws PortletException, java.io.IOException {
    	
    	response.setContentType("text/html");
    	
    	ResultWriter resultWriter = new ResultWriter(TEST_NAME);
    	PrintWriter out = response.getWriter();
    	
    	Map<String, String[]> actual = request.getParameterMap();
    	
    	if (actual != null) {
    		
    		try {
    			actual.put(TEST_NAME, new String[] {TEST_NAME});
    		} catch (Exception e) {}
    		
    		MapCompare compare = new MapCompare(map, actual);
    		
    		if (!compare.misMatch())
    			resultWriter.setStatus(ResultWriter.PASS);
    		else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail(compare.getMisMatchReason());   			
    		}
    	} else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( "ResourceRequest.getParameterMap() "
                                    + " returned an empty map." );    		
    	}
    	
    	out.println(resultWriter.toString());
    }
}
