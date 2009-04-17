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

import org.exoplatform.Constants;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.consumer.URLTemplateComposer;
import org.exoplatform.services.wsrp2.consumer.impl.urls.ws1.URLTemplateComposerImpl1;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 9
 *         févr. 2004 Time: 17:23:44
 */

public class TestURLTemplateComposer extends BaseTest {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestURLTemplateComposer.setUp()");
  }

  public void testBlockingGeneration() {
    URLTemplateComposer composer = new URLTemplateComposerImpl1();

    String path = "/portal/faces/public/portal.jsp?portal:ctx=community&portal:component=wsrp/wsrpportlet/44rc74";

    // Default        
    assertTrue(composer.createDefaultTemplate(path).contains("&" + Constants.TYPE_PARAMETER + "={"
        + WSRPConstants.WSRP_URL_TYPE + "}"));
    assertTrue(composer.createDefaultTemplate(path).contains("&"
        + WSRPConstants.WSRP_PORTLET_HANDLE + "={" + WSRPConstants.WSRP_PORTLET_HANDLE + "}"));
    assertTrue(composer.createDefaultTemplate(path).contains("&"
        + WSRPConstants.WSRP_USER_CONTEXT_KEY + "={" + WSRPConstants.WSRP_USER_CONTEXT_KEY + "}"));
    assertTrue(composer.createDefaultTemplate(path).contains("&"
        + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "={" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY
        + "}"));
    assertTrue(composer.createDefaultTemplate(path).contains("&" + WSRPConstants.WSRP_SESSION_ID
        + "={" + WSRPConstants.WSRP_SESSION_ID + "}"));
    assertTrue(composer.createDefaultTemplate(path).contains("&" + Constants.SECURE_PARAMETER
        + "={" + WSRPConstants.WSRP_SECURE_URL + "}"));
    assertTrue(composer.createDefaultTemplate(path).contains("&" + WSRPConstants.WSRP_FRAGMENT_ID
        + "={" + WSRPConstants.WSRP_FRAGMENT_ID + "}"));

    //Action
    assertTrue(composer.createBlockingActionTemplate(path).contains("&"
        + WSRPConstants.WSRP_INTERACTION_STATE + "={" + WSRPConstants.WSRP_INTERACTION_STATE + "}"));

    //Render
    assertTrue(composer.createRenderTemplate(path).contains("&" + Constants.PORTLET_MODE_PARAMETER
        + "={" + WSRPConstants.WSRP_MODE + "}"));
    assertTrue(composer.createRenderTemplate(path).contains("&" + Constants.WINDOW_STATE_PARAMETER
        + "={" + WSRPConstants.WSRP_WINDOW_STATE + "}"));
    assertTrue(composer.createRenderTemplate(path).contains("&"
        + WSRPConstants.WSRP_NAVIGATIONAL_STATE + "={" + WSRPConstants.WSRP_NAVIGATIONAL_STATE
        + "}"));

    //Resource
    assertTrue(composer.createResourceTemplate(path).contains("&" + WSRPConstants.WSRP_URL + "={"
        + WSRPConstants.WSRP_URL + "}"));
    assertTrue(composer.createResourceTemplate(path).contains("&"
        + WSRPConstants.WSRP_REQUIRES_REWRITE + "={" + WSRPConstants.WSRP_REQUIRES_REWRITE + "}"));
  }

}
