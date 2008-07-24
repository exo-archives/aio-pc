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
import java.io.File;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class uses the getRealPath() method to locale the path of a html file.
 */

public class GetRealPathTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetRealPathTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        String path = "GetIncluded.html";

        PortletContext context = getPortletContext();

        if (context != null) {
            String realPath = context.getRealPath( "/" + path );

            if ( realPath == null) {
                resultWriter.setStatus(ResultWriter.PASS);
            }
            else if (realPath.indexOf(path) > -1) {
			    File file = new File(realPath);
			    if ((file.exists())  && (file.isFile()) && (file.canRead())) {
				    resultWriter.setStatus(ResultWriter.PASS);
			    } else { 
			       resultWriter.setStatus(ResultWriter.FAIL);
                   resultWriter.addDetail("Unable to read the contents of the "
                                    + "file from the specified path");
                }
			 } else { 
			        resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("PortletContext.getRealPath(" + path 
                                       + ") did not contain the named files" );
                    resultWriter.addDetail("Actual result = " + realPath);
            } 
         } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletContext() returned null");
        }
		out.println(resultWriter.toString());
    }
}
