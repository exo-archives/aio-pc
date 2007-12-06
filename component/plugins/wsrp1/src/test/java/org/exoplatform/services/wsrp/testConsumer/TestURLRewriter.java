/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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

package org.exoplatform.services.wsrp.testConsumer;

import org.exoplatform.services.wsrp.exceptions.WSRPException;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 6 févr. 2004
 * Time: 17:17:29
 */

public class TestURLRewriter extends BaseTest {

    String s = "wsrp_rewrite?wsrp-urlType=render&amp;wsrp-portletMode=wsrp:help&amp;" +
        "wsrp-navigationalState=rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAh3CAAAAAsAAAAAeA**" +
        "&amp;wsrp-windowState=wsrp:normal&amp;amp;wsrp-secureURL=false/wsrp_rewrite";

  public void testRewrite() throws WSRPException {
    System.out.println("Rewritten : " + urlRewriter.rewriteURLs("baseURL", s));
    //assertEquals(returned, urlRewriter.rewriteURLs(s));
  }

}