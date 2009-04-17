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

import java.util.List;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS
 * Author : Max Shaposhnik
 *
 * 20.08.2007
 */
public class TestPublicRenderParameterNames extends BaseTest2 {

private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer.imp.Portlet286.TestPublicRenderParameterNames");

  public TestPublicRenderParameterNames(String s) {
    super(s);
  }

  public void testPublicRenderParameterNames() throws Exception {
    log.info("testPublicRenderParameterNames...");
    List names = portletApp_.getPublicRenderParameter();

    if (names.size() > 0)
    assertTrue(names.size() == 2);
    log.info("Done");
  }

}
