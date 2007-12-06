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

import java.io.InputStream;
import java.net.URL;

import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.XMLParser;
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
    assertNotNull(is);
    PortletApp pc = XMLParser.parse(is,false) ; //First portlet specification - JSR 168
    assertNotNull(pc);
  }

  protected String getDescription() {
    return "Test Converter" ;
  }
}