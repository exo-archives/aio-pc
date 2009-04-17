/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.ee.ClassLoader;

import java.io.PrintWriter;
import java.io.IOException;
import java.net.URL;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class GetResourceInJarTest_1_Servlet extends GenericServlet {
    public static final String TEST_NAME = GetResourceInJarTestPortlet.TEST_NAME;

    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        String path = "/resource.properties";

        URL resourceURL = getClass().getResource(path);

        if (resourceURL == null) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Classloader unable to access the property file resouce.properties inside the jar file");
        } else {
            resultWriter.setStatus(ResultWriter.PASS);
        }
        out.println(resultWriter.toString());
    }
}
