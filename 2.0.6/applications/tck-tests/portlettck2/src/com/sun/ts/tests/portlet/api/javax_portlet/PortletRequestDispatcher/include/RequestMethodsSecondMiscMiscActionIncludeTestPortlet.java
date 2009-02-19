/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.include;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsSecondMiscMiscActionIncludeTestPortlet 
	extends AbstractActionIncludeTestPortlet {
    
	private final String TEST_NAME = 
		"RequestMethodsSecondMiscMiscActionIncludeTest";
	
	private final String SERVLET_PATH = 
		"/RequestMethodsSecondMiscMiscTestServlet";
    
    
	@Override
	protected String getServletPath() {
		return SERVLET_PATH;
	}



	@Override
	protected String getTestName() {
		return TEST_NAME;
	}


	@Override
	protected void prepareInclude(PortletSession session, 
			ActionRequest request, ActionResponse response) {
		
		super.prepareInclude(session, request, response);

		session.setAttribute("getCharacterEncoding",
				request.getCharacterEncoding(),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getContentType",
				request.getContentType(),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getContentLength",
				new Integer(request.getContentLength()),
				PortletSession.APPLICATION_SCOPE);
		
		/*try{
			session.setAttribute("getInputStream",
					request.getPortletInputStream(),
					PortletSession.APPLICATION_SCOPE);
		}
		catch(IOException e){
			e.printStackTrace();
		}*/

		session.setAttribute("getMethod",
				request.getMethod(),
				PortletSession.APPLICATION_SCOPE);

		session.setAttribute("getProtocol",
				"HTTP/1.1",
				PortletSession.APPLICATION_SCOPE);
		/*try{
			session.setAttribute("getReader",
					request.getReader(),
					PortletSession.APPLICATION_SCOPE);
		}
		catch(IOException e){
			e.printStackTrace();
		}*/
		
		session.setAttribute("setCharacterEncoding",
				new Boolean(true),//op
				PortletSession.APPLICATION_SCOPE);

	}	

}
