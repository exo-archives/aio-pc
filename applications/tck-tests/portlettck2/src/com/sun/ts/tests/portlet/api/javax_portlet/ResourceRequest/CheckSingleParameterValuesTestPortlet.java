/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ResourceRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;


/**
 * Test for ResourceRequest.getParameterValues() method.
 * First request to the portlet writes a resourceURL with
 * a parameter having 1 value. The portlet URL string is
 * extracted and used for second request. The portlet uses
 * ResourcetRequest.getParameterValues() to check the returned 
 * array.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 * @since 2.0
 */

public class CheckSingleParameterValuesTestPortlet extends GenericPortlet {

	public static final String TEST_NAME =
		"CheckSingleParameterValuesTest";
	
	static final String key = "LANGUAGES";
	static final String value = "Java";

	public void serveResource(ResourceRequest request, ResourceResponse response)
		throws PortletException, IOException {
		
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        String[] paramValues = request.getParameterValues(key);

        if ((paramValues != null) &&
        	(paramValues.length == 1) &&
            (paramValues[0] != null) &&
            (paramValues[0].equals(value))) {
                resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected a array of size one with: "
            		+ value);
        }
	  
        out.println(resultWriter.toString());
    }


    public void render(RenderRequest request, RenderResponse response) 
    	throws PortletException, IOException {
    	
        response.setContentType("text/html");
        
        PrintWriter out = response.getWriter();

        PortletURLTag customTag = new PortletURLTag();
        customTag.setTagContent(getResourceURL(response));        
        
        out.println(customTag.toString());
    }

    protected String getResourceURL(RenderResponse response) {

        ResourceURL resourceURL = response.createResourceURL();
		resourceURL.setParameter(key, value);

        return resourceURL.toString(); 
    }
}
