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
package org.exoplatform.services.portletcontainer.imp.Portlet286;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.XMLParser;

/**
 * Created by The eXo Platform SAS
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 * 24.04.2007
 */
public class TestPortletModelParser  extends BaseTest2 {

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.imp.Portlet286.TestPortletModelParser");

  public TestPortletModelParser(String name) {
    super(name);
  }

  public void setUp() throws Exception {  }

  public void testPortletModelParser() throws Exception {
    log.info("testPortletModelParser...");
    URL url = new URL("file:" + TEST_PATH + "/src/test/war_template2/WEB-INF/portlet.xml");
    InputStream is = url.openStream() ;
    assertNotNull(is);
    PortletApp pc = XMLParser.parse(is,true) ; //Second portlet specification - JSR 286
    assertNotNull(pc);
    log.info("done.");
  }

  protected String getDescription() {
    return "Test Converter" ;
  }
}