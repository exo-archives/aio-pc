/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class tests getResource(path) method.
 */

public class GetResourceTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetResourceTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        PortletContext context = getPortletContext();

        String path = "/WEB-INF/resource.xml";

        if (context != null) {
		    try {
        	    URL resourceURL = context.getResource( path );

        	    if(resourceURL != null ) {
                    
                    // Open the URL Connection and try reading the file
                    InputStream ios = resourceURL.openStream();

                    String xmlHeader = 
                           "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

                    byte[] inputByteData = new byte[80];

                    int iData = ios.read(inputByteData);

                    String xmlData = new String(inputByteData);

                    // Check whether the first line of web.xml has the header
                    // info. If it is then we are reading the correct file.
                    if (xmlData.startsWith(xmlHeader)) {
					    resultWriter.setStatus(ResultWriter.PASS);
                    } else {
				        resultWriter.setStatus(ResultWriter.FAIL);
            	        resultWriter.addDetail("Expected data = " + xmlHeader);
            	        resultWriter.addDetail("Actual data = " + xmlData);
                    }
        	    } else {
				    resultWriter.setStatus(ResultWriter.FAIL);
            	    resultWriter.addDetail("getResource(" + path + ") returned" 
                                         + " null." );
        	    }
		    } catch (MalformedURLException m) { 
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("PortletContext.getResource(" + path + 
                            ") threw MalformedURLException." + m.getMessage());
		    }
        } else {
           resultWriter.setStatus(ResultWriter.FAIL);
           resultWriter.addDetail("getPortletContext() returned null");
        }
	    out.println(resultWriter.toString());	
    }
}
