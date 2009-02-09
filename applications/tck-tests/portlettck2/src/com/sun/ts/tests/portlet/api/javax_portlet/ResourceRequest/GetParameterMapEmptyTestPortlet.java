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

/**
 * Test for ResourceRequest.getParameterMap() method.
 * First request to the portlet writes a
 * action resourceURL with no paramaters in it, to the output stream.
 * The portlet URL string is extracted and used for second
 * request. In the second request, 
 * portlet uses ResourceRequest.getParameterMap()
 * to see if the empty map is returned.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 */


import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class GetParameterMapEmptyTestPortlet extends GenericPortlet {

    static public final String TEST_NAME = 
    	"GetParameterMapEmptyTest";

    public void render(RenderRequest request, RenderResponse response )
    	throws PortletException, java.io.IOException {
    	
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        PortletURLTag customTag = new PortletURLTag();
        ResourceURL resourceURL = response.createResourceURL();
        customTag.setTagContent(resourceURL.toString());
        
        out.println(customTag.toString());
    }
    
    public void serveResource(ResourceRequest request, ResourceResponse response)
    	throws PortletException, java.io.IOException {
    	
    	response.setContentType("text/html");
    	
    	ResultWriter resultWriter = new ResultWriter(TEST_NAME);
    	PrintWriter out = response.getWriter();

        Map map = request.getParameterMap();
        
        if (map != null && map.size() == 0) {
            resultWriter.setStatus(ResultWriter.PASS);
        }
        else {
            resultWriter.setStatus(ResultWriter.FAIL);
            
            if ( map == null) {
                resultWriter.addDetail("Expected an empty map.");
                resultWriter.addDetail("Found null returned.");
            }
            else {
                resultWriter.addDetail("Expected an empty map.");
                resultWriter.addDetail(
                    "Found a map with size greater than 0.");
            }
        }
    	
    	out.println(resultWriter.toString());
    }
}
