/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp2.testConsumer;

import org.exoplatform.services.wsrp2.exceptions.WSRPException;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Mar 19, 2009
 */

public class TestURLRewriter extends BaseTest {

  String toRewrite = "wsrp_rewrite?portal:type=render&amp;portal:portletMode=wsrp:help&amp;"
                       + "wsrp-navigationalState=rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAh3CAAAAAsAAAAAeA**"
                       + "&amp;portal:windowState=wsrp:normal&amp;portal:isSecure=false/wsrp_rewrite";

  String expected  = "baseURL&wsrp-navigationalState=rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAh3CAAAAAsAAAAAeA**&portal%3AisSecure=false&portal%3Atype=render&portal%3AportletMode=help&portal%3AwindowState=normal";

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    log();
  }

  public void testRewrite() throws WSRPException {
    log();
    String result = urlRewriter.rewriteURLs("baseURL", toRewrite);

    assertNotNull(result);
    assertEquals(expected, result);
  }

}
