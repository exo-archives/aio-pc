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

import com.sun.ts.tests.portlet.common.util.ListCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;



/**
* First request to the portlet writes a
* resource portletURL with a paramater with multiple values in it, 
* to the output stream.
* The portlet URL string is extracted and used for second
* request. In the second request, in serveResource,
* portlet uses ResourceRequest.getParameterValues()
* to see if the expected parameter values are returned.
* 
* @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
* @since 2.0
*/



public class GetParameterValuesTestPortlet extends GenericPortlet {

	public static final String TEST_NAME = 
		"GetParameterValuesTest";
	
	static final String key = "LANGUAGES";
	static final String[] values = new String[] {"XML", "JAVA"};

	public void serveResource(ResourceRequest request, ResourceResponse response)
		throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        String[] paramValues = request.getParameterValues(key);
        String firstExpectedValue = request.getParameter(key);

        ListCompare compare = new ListCompare(
                                    values, 
                                    paramValues,
                                    firstExpectedValue,
                                    ListCompare.ALL_ELEMENTS_MATCH);
        if (compare.misMatch()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("ResourceRequest.getParameterValues("
            		+ key + ") returned unexpected results.");
            resultWriter.addDetail(compare.getMisMatchReason());
        }
        else {
            resultWriter.setStatus(ResultWriter.PASS);
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
		resourceURL.setParameter(key, values);

        return resourceURL.toString(); 
    }
}
