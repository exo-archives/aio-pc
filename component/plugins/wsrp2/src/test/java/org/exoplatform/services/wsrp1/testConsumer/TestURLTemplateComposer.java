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

package org.exoplatform.services.wsrp1.testConsumer;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 9 févr. 2004
 * Time: 17:23:44
 */

public class TestURLTemplateComposer extends BaseTest {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestURLTemplateComposer.setUp()");
  }

  public void testBlockingGeneration() {
    /*    URLTemplateComposer composer = new URLTemplateComposerImpl();

        String path = "/portal/faces/public/portal.jsp?portal:ctx=community&portal:component=wsrp/wsrpportlet/44rc74";
        String returnedS = "http://localhost:8080/portal/faces/public/portal.jsp?portal:ctx=community&portal:co" +
            "mponent=wsrp/wsrpportlet/44rc74&portal:type={wsrp-urlType}&portal:mode={wsrp-portletMode}&portal:windowSt" +
            "ate={wsrp-windowState}&portal:isSecure={wsrp-secureURL}&wsrp-portletHandle={wsrp-portl" +
            "etHandle}&portal:wsrp-portletInstanceKey={wsrp-portletInstanceKey}&wsrp-navigationalSt" +
            "ate={wsrp-navigationalState}&wsrp-sessionID={wsrp-sessionID}&wsrp-userContextKey" +
            "={wsrp-userContextKey}&wsrp-url={wsrp-url}&wsrp-requiresRewrite={wsrp-requiresRe" +
            "write}&wsrp-interactionState={wsrp-interactionState}&wsrp-fragmentID={wsrp-fragm" +
            "entID}";
        assertEquals(returnedS, composer.createDefaultTemplate(path));*/
  }

}
