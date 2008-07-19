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

package org.exoplatform.services.wsrp.test;

import java.rmi.RemoteException;

import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.type.CacheControl;
import org.exoplatform.services.wsrp.type.MarkupResponse;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.ServiceDescription;

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

  public void testExistenceOfValidateTag() throws RemoteException {
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    RegistrationContext rc = null;
    if (sd.isRequiresRegistration())
      rc = new RegistrationContext("", null, null);
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle("hello/HelloWorld2");
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup(rc, portletContext));
    CacheControl cacheControl = response.getMarkupContext().getCacheControl();
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

    String portletHandle = "hello/HelloWorld2";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);

    MarkupRequest getMarkup = getMarkup(rc, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    CacheControl cacheControl = response.getMarkupContext().getCacheControl();
    System.out.println("[test] validate tag key : " + cacheControl.getValidateTag());
    markupParams.setValidateTag(cacheControl.getValidateTag());
    runtimeContext.setSessionID(response.getSessionContext().getSessionID());
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = markupOperationsInterface.getMarkup(getMarkup);
    assertTrue(response.getMarkupContext().getUseCachedMarkup().booleanValue());
  }

  public void testExistenceOfGlobal() throws RemoteException {
    ServiceDescription sd = getServiceDescription(new String[]{"en"});
    RegistrationContext rc = null;
    if(sd.isRequiresRegistration())
      rc = new RegistrationContext();

    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle("hello/EmptyPortletWithGlobalCache");

    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup(rc, portletContext));
    CacheControl cacheControl = response.getMarkupContext().getCacheControl();
    assertEquals(-1, cacheControl.getExpires());
    assertEquals(WSRPConstants.WSRP_GLOBAL_SCOPE_CACHE, cacheControl.getUserScope());
    assertNotNull(cacheControl.getValidateTag());
  }
  */
}
