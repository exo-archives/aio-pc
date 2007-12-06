/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.imp;

import java.io.InputStream;
import java.net.URL;
import org.exoplatform.services.portletcontainer.pci.model.*;
import org.exoplatform.test.BasicTestCase;

/**
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestPortletModelParser.java,v 1.1 2004/07/13 02:28:53 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestPortletModelParser  extends BasicTestCase {

  public TestPortletModelParser(String name) {
    super(name);
  }

  public void setUp() throws Exception {  }
  
  
  public void testPortletModelParser() throws Exception {
    URL url = new URL("file:" + System.getProperty("testPath") + "/war_template/WEB-INF/portlet.xml");
    InputStream is = url.openStream() ;
    PortletApp pc = XMLParser.parse(is) ; 
  }
  
  protected String getDescription() {
    return "Test Converter" ;
  }
}