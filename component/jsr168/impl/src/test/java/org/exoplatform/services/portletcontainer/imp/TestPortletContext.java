package org.exoplatform.services.portletcontainer.imp;

import javax.portlet.PortletException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Enumeration;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 15 nov. 2003
 * Time: 13:58:26
 */
public class TestPortletContext extends BaseTest{

	public TestPortletContext(String s) {
		super(s);
	}

	/**
	 * test (xli) : There is one instance of the PortletContext interface associated with
	 *              each portlet application deployed into a portlet container.
	 *
	 * PLT.10.1
	 */
  public void testPortletContextUnicityPerPortletApplication() throws PortletException {
    //TODO find a way to test
	}

	/**
	 * test (xliii) : The initialization parameters accessible through the PortletContext
	 *                must be the same that are accessible through the ServletContext of
	 *                the portlet application.
	 *
	 * PLT.10.3
	 */
	public void testEqualityOfServletAndPortletContextParameters(){
    assertEquals("test-parame-value",portletContext.getInitParameter("test-param"));
	}

	/**
	 * test (xliv) : Context attributes set using the PortletContext must be stored in the
	 *               ServletContext of the portlet application. A direct consequence of this
	 *               is that data stored in the ServletContext by servlets or JSPs is accessible
	 *               to portlets through the PortletContext and vice versa.
	 *
	 * PLT.10.3
	 */
	public void testAttributesShareBetweenPortletAndServlets(){
    mockServletContext.setAttribute("testAtt1", "attValue1");
		assertEquals("attValue1", portletContext.getAttribute("testAtt1"));

		portletContext.setAttribute("testAtt2", "attValue2");
		assertEquals("attValue2", mockServletContext.getAttribute("testAtt2"));
	}

	/**
	 * test (xlv) : The PortletContext must offer access to the same set of resources the
	 *              ServletContext exposes.
	 *
	 * PLT.10.3
	 */
	public void testIdentitalResourcesFromPortletAndServletContext() throws IOException {
    InputStream is1 = mockServletContext.getResourceAsStream("/WEB-INF/web.xml");
		InputStream is2 = portletContext.getResourceAsStream("/WEB-INF/web.xml");
    byte[] byteArray1 = new byte[1024];
		byte[] byteArray2 = new byte[1024];
		is1.read(byteArray1);
		is2.read(byteArray2);
		System.out.println(byteArray1);
		boolean equals = true;
		for (int i = 0; i < byteArray1.length; i++) {
			byte b = byteArray1[i];
			if(b != byteArray2[i]){
				equals = false;
				break;
			}
		}
		assertTrue(equals);
	}

	/**
	 * test (xlvi) : The PortletContext must handle the same temporary working directory the
	 *               ServletContext handles. It must be accessible as a context attribute
	 *               using the same constant defined in the Servlet Specification 2.3 SVR 3
	 *               Servlet Context Chapter, javax.servlet.context.tempdir.
	 *
	 * PLT.10.3
	 */
  public void testContextAttributeAccess(){
    assertNotNull(portletContext.getAttribute("javax.servlet.context.tempdir"));
	}

	/**
	 * test (xlvii) : The portlet context must follow the same behavior and functionality that
	 *                the servlet context has for virtual hosting and reloading considerations.
	 *                (see Servlet Specification 2.3 SVR 3 Servlet Context Chapter)
	 *
	 * PLT.10.3
	 */
  //TODO find a way to test that

	/**
	 * test : The following methods of the PortletContext should provide the same functionality
	 *        as the methods of the ServletContext of similar name: getAttribute, getAttributeNames,
	 *        getInitParameter, getInitParameterNames, getMimeType, getRealPath, getResource,
	 *        getResourcePaths, getResourceAsStream, log, removeAttribute and setAttribute.
	 *
	 * PLT.10.3.1
	 */
  public void testCorrespondanceBetweenPortletAndServletContextMethods() throws MalformedURLException {
		//getAttribute has already been tested

		Enumeration e = mockServletContext.getAttributeNames();
		Enumeration e2 = portletContext.getAttributeNames();
		boolean equals = true;
		while (e.hasMoreElements()) {
			String attName = (String) e.nextElement();
			if(!attName.equals(e2.nextElement())){
				equals = false;
				break;
			}
		}
    assertTrue(equals);

		//getInitParameter has already been tested

		e = mockServletContext.getInitParameterNames();
		e2 = portletContext.getInitParameterNames();
		equals = true;
		while (e.hasMoreElements()) {
			String attName = (String) e.nextElement();
			if(!attName.equals(e2.nextElement())){
				equals = false;
				break;
			}
		}
    assertTrue(equals);

		assertEquals(mockServletContext.getMimeType("blbla"), portletContext.getMimeType("blabla"));

		assertEquals(mockServletContext.getRealPath("blabla"), portletContext.getRealPath("blabla"));

		assertEquals(mockServletContext.getResource("/WEB-INF/web.xml"),
						portletContext.getResource("/WEB-INF/web.xml"));

    assertEquals(mockServletContext.getResourcePaths("/"),
						portletContext.getResourcePaths("/"));

		//getResourceAsStream has already been tested

		StringBuffer sB = new StringBuffer();
		sB.append("bad exception in log....");
		portletContext.log(sB.toString());
		assertEquals(mockServletContext.getLogBuffer(), sB.toString());

		sB = new StringBuffer();
		sB.append("bad exception in log....the come back");
		portletContext.log(sB.toString(), new Exception("olala"));
		assertEquals(mockServletContext.getLogBuffer(), sB.toString()+"olala");

		sB = new StringBuffer();
		sB.append("bad exception in log....the come back");
		portletContext.log(sB.toString(), new Throwable("olala"));
		assertEquals(mockServletContext.getLogBuffer(), sB.toString()+"olala");

		portletContext.setAttribute("testAtt2", "attValue2");
		mockServletContext.removeAttribute("testAtt2");
		assertNull(portletContext.getAttribute("testAtt2"));

		//setAttribute has already been tested
	}


}
