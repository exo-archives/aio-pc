package org;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <class description here>
 */
public class TestServlet extends HttpServlet
{
	private static final String HELLO_TEMPLATE =
		"/WEB-INF/HelloWorld.jsp";

	/**
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void service(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException
	{
		res.getOutputStream().write(("TestServlet: including '" + HELLO_TEMPLATE + "'..").getBytes());
//		res.getWriter().write("TestServlet: including '" + HELLO_TEMPLATE + "'..");
		RequestDispatcher rd = req.getRequestDispatcher(HELLO_TEMPLATE);
		rd.include(req, res);
	}

}

