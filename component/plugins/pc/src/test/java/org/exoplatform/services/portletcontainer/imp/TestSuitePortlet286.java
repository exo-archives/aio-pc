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
package org.exoplatform.services.portletcontainer.imp;

import junit.framework.TestSuite;

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestBaseURL;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestCacheability;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestDefaultNamespace;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestEvents;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestFilters;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestNamespacing;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestPortletModelParser;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestPortletURL;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestPortletUnavailable;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestPublicRenderParameterNames;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestPublicRenderParameters;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestRenderParamsAviability;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestResourceServing;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestResourceURL;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestServeResource;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestUserInfo;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestWindowID;
import org.exoplatform.services.portletcontainer.imp.Portlet286.TestWindowState2;

/**
 * Created by The eXo Platform SAS
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 * 24.04.2007
 */
public class TestSuitePortlet286 extends TestSuite {

  private static Log log = ExoLogger.getLogger("org.exoplatform.portletcontainer.TestSuitePortlet286");

  public TestSuitePortlet286() {
    log.info("Preparing Portlet286 tests....");

    String newProperty = System.getProperty("basedir") + "/src/test/war_template2";
    System.setProperty("mock.portal.dir", newProperty);
    System.setProperty("maven.exoplatform.dir", newProperty);

    addTestSuite(TestPortletModelParser.class);
    addTestSuite(TestFilters.class);
    addTestSuite(TestEvents.class);
    addTestSuite(TestWindowState2.class);
    addTestSuite(TestResourceServing.class);
    addTestSuite(TestPublicRenderParameters.class);
    addTestSuite(TestPortletURL.class);
    addTestSuite(TestDefaultNamespace.class);
    addTestSuite(TestPublicRenderParameterNames.class);
    addTestSuite(TestBaseURL.class);
    addTestSuite(TestServeResource.class);
    addTestSuite(TestUserInfo.class);
    addTestSuite(TestWindowID.class);
    addTestSuite(TestNamespacing.class);
    addTestSuite(TestResourceURL.class);
    addTestSuite(TestCacheability.class);
    addTestSuite(TestPortletUnavailable.class);
    addTestSuite(TestRenderParamsAviability.class);
  }

  public void testVoid() throws Exception {
  }

}
