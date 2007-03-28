package org.exoplatform.services.portletcontainer.imp;


import javax.portlet.PortletConfig;
import org.exoplatform.Constants;
import org.exoplatform.services.portletcontainer.impl.PortletDataImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletConfigImp;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Copyright 2001-2003 The eXo Platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL        .
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
		PortletDataImp portletDatas = (PortletDataImp) portletContainer.
                 getAllPortletMetaData().get("hello" + Constants.PORTLET_META_DATA_ENCODER  
                 + "HelloWorld");

		config = new PortletConfigImp(portletDatas.getWrappedPortletTyped(), portletContext,
						portletApp_.getSecurityConstraint(),
						portletApp_.getUserAttribute(),
						portletApp_.getCustomPortletMode(),
						portletApp_.getCustomWindowState());
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
                 getAllPortletMetaData().get("hello" + Constants.PORTLET_META_DATA_ENCODER  
                 + "HelloWorld2");            

		config = new PortletConfigImp(portletDatas.getWrappedPortletTyped(), portletContext,
						portletApp_.getSecurityConstraint(),
						portletApp_.getUserAttribute(),
						portletApp_.getCustomPortletMode(),
						portletApp_.getCustomWindowState());

		Locale l = Locale.US;
		ResourceBundle rB = config.getResourceBundle(l);

    assertEquals("HelloWorld2",rB.getString("javax.portlet.title"));
    assertEquals("HelloWorld2s",rB.getString("javax.portlet.short-title"));
		assertEquals("Time, Zone, World, Clock",rB.getString("javax.portlet.keywords"));
		assertEquals(l, rB.getLocale());
	}


}
