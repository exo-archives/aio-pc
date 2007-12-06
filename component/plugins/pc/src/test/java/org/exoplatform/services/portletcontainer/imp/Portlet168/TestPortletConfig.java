/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.imp.Portlet168;

import javax.portlet.PortletConfig;
import org.exoplatform.Constants;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletDataImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletAPIObjectFactory;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletConfigImp;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by The eXo Platform SAS         .
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 15 oct. 2003
 * Time: 16:25:42
 */
public class TestPortletConfig extends BaseTest{

	PortletConfig config;

	public TestPortletConfig(String s) {
		super(s);
	}

	public void setUp() throws Exception {
		super.setUp();
		portletDatas = (PortletDataImp) portletContainer.
                 getAllPortletMetaData().get(CONTEXT_PATH.substring(1) + Constants.PORTLET_META_DATA_ENCODER
                 + "HelloWorld");
    portletContext = PortletAPIObjectFactory.getInstance().createPortletContext(portalContainer,
        mockServletContext, portletDatas.getWrappedPortletTyped());
		config = new PortletConfigImp(portletDatas.getWrappedPortletTyped(), portletContext,
						portletApp_.getSecurityConstraint(),
						portletApp_.getUserAttribute(),
						portletApp_.getCustomPortletMode(),
						portletApp_.getCustomWindowState(),
						portletApp_.getDefaultNamespace());
	}

	/**
	 * test : The getInitParameterNames and getInitParameter methods of the PortletConfig
	 *        interface return the initialization parameter names and values found in the
	 *        portlet definition in the deployment descriptor.
	 *
	 * PLT.6.1
	 */
	public void testInitializationParam(){
		Enumeration e = config.getInitParameterNames();
		assertEquals("initName", e.nextElement());
		assertFalse(e.hasMoreElements());

		assertEquals("initValue", config.getInitParameter("initName"));
	}

	/**
	 * test (xxiv) : If the portlet definition defines a resource bundle, the portlet-container
	 *               must look up these values in the ResourceBundle. If the root resource bundle
	 *               does not contain the resources for these values and the values are defined
	 *               inline, the portlet container must add the inline values as resources of the
	 *               root resource bundle.
	 *
	 * PLT.6.2
	 */
  public void testResourceBundleCreation(){
		Locale l = Locale.ENGLISH ;
		ResourceBundle rB = config.getResourceBundle(l);

    assertEquals("HelloWorld title",rB.getString("javax.portlet.title"));
    assertEquals("Hello World",rB.getString("javax.portlet.short-title"));
		assertEquals("sample, hello",rB.getString("javax.portlet.keywords"));
    //assertTrue(rB.getStringArray("key") instanceof String[]);
		assertEquals(l, rB.getLocale());

		l = Locale.FRENCH ;
		rB = config.getResourceBundle(l);
    assertEquals("Bonjour le monde Portlet",rB.getString("javax.portlet.title"));
    assertEquals("Bonjour",rB.getString("javax.portlet.short-title"));
		assertEquals("exemple, bonjour",rB.getString("javax.portlet.keywords"));
		assertEquals(l, rB.getLocale());
	}

	/**
	 * test (xxv) : If the portlet definition does not define a resource bundle
	 *              and the information is defined inline in the deployment descriptor,
	 *              the portlet container must create a ResourceBundle and populate it,
	 *              with the inline values, using the keys defined in the PLT.21.10
	 *              Resource Bundles Section.
	 *
	 * PLT.6.2
	 */
	public void testInlineResourceBundleCreation(){
    PortletDataImp portletDatas = (PortletDataImp) portletContainer.
                 getAllPortletMetaData().get("war_template" + Constants.PORTLET_META_DATA_ENCODER
                 + "HelloWorld2");
    portletContext = PortletAPIObjectFactory.getInstance().createPortletContext(portalContainer,
        mockServletContext, portletDatas.getWrappedPortletTyped());
		config = new PortletConfigImp(portletDatas.getWrappedPortletTyped(), portletContext,
						portletApp_.getSecurityConstraint(),
						portletApp_.getUserAttribute(),
						portletApp_.getCustomPortletMode(),
						portletApp_.getCustomWindowState(),
						portletApp_.getDefaultNamespace());

		Locale l = Locale.US;
		ResourceBundle rB = config.getResourceBundle(l);

    assertEquals("HelloWorld2",rB.getString("javax.portlet.title"));
    assertEquals("HelloWorld2s",rB.getString("javax.portlet.short-title"));
		assertEquals("Time, Zone, World, Clock",rB.getString("javax.portlet.keywords"));
		assertEquals(l, rB.getLocale());
	}


}
