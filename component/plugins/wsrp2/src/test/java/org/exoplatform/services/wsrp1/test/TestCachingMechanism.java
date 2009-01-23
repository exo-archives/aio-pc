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

package org.exoplatform.services.wsrp1.test;

import org.exoplatform.services.wsrp1.type.WS1CacheControl;
import org.exoplatform.services.wsrp1.type.WS1MarkupResponse;
import org.exoplatform.services.wsrp1.type.WS1PortletContext;
import org.exoplatform.services.wsrp1.type.WS1ServiceDescription;
import org.exoplatform.services.wsrp2.WSRPConstants;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 25 janv. 2004
 * Time: 19:29:55
 */

public class TestCachingMechanism extends BaseTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestCachingMechanism.setUp()");
  }

  public void testExistenceOfValidateTag() throws Exception {
    WS1ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd);
    WS1PortletContext portletContext = new WS1PortletContext();
    portletContext.setPortletHandle(CONTEXT_PATH + "/HelloWorld2");
    WS1MarkupResponse response = getMarkup(getMarkup(registrationContext, portletContext));
    WS1CacheControl cacheControl = response.getMarkupContext().getCacheControl();
    assertEquals(4, cacheControl.getExpires());
    assertEquals(WSRPConstants.WSRP_USER_SCOPE_CACHE, cacheControl.getUserScope());
    assertNotNull(cacheControl.getValidateTag());
  }
  /*
  public void testUseCacheReturn() throws RemoteException, UnsupportedWindowStateFault, InvalidHandleFault, UnsupportedModeFault {
    ServiceDescription sd = getServiceDescription(new String[]{"en"});
    RegistrationContext rc = null;
    if(sd.isRequiresRegistration())
      rc = new RegistrationContext();

    String portletHandle = CONTEXT_PATH + "/HelloWorld2";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);

    MarkupRequest getMarkup = getMarkup(rc, portletContext);
    MarkupResponse response = getMarkup(getMarkup);
    CacheControl cacheControl = response.getMarkupContext().getCacheControl();
    System.out.println("[test] validate tag key : " + cacheControl.getValidateTag());
    markupParams.setValidateTag(cacheControl.getValidateTag());
    runtimeContext.setSessionID(response.getSessionContext().getSessionID());
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = getMarkup(getMarkup);
    assertTrue(response.getMarkupContext().getUseCachedMarkup().booleanValue());
  }

  public void testExistenceOfGlobal() throws RemoteException {
    ServiceDescription sd = getServiceDescription(new String[]{"en"});
    RegistrationContext rc = null;
    if(sd.isRequiresRegistration())
      rc = new RegistrationContext();

    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(CONTEXT_PATH + "/EmptyPortletWithGlobalCache");

    MarkupResponse response = getMarkup(getMarkup(rc, portletContext));
    CacheControl cacheControl = response.getMarkupContext().getCacheControl();
    assertEquals(-1, cacheControl.getExpires());
    assertEquals(WSRPConstants.WSRP_GLOBAL_SCOPE_CACHE, cacheControl.getUserScope());
    assertNotNull(cacheControl.getValidateTag());
  }
  */
}
