/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class MultipleForwardsTestServlet extends HttpServlet {
	
	static final long serialVersionUID = 286L;
	
    public static final String SERVLET_PATH = "/AccessToPathForwardTestServlet";
    
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    	
    	RequestDispatcher dispatcher = request.getRequestDispatcher(SERVLET_PATH);
        dispatcher.forward(request, response);
    }
}
