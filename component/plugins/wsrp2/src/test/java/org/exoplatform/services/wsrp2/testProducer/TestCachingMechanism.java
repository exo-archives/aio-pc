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

package org.exoplatform.services.wsrp2.testProducer;

import java.rmi.RemoteException;
import java.util.Arrays;

import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.type.CacheControl;
import org.exoplatform.services.wsrp2.type.GetMarkup;
import org.exoplatform.services.wsrp2.type.InvalidHandleFault;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.RuntimeContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.UnsupportedModeFault;
import org.exoplatform.services.wsrp2.type.UnsupportedWindowStateFault;

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
    log();
  }

  public void testExistenceOfValidateTag() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd);
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(CONTEXT_PATH + "/HelloWorld2");
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup(registrationContext,
                                                                            portletContext));
    CacheControl cacheControl = response.getMarkupContext().getCacheControl();
    assertEquals(4, cacheControl.getExpires());
    assertEquals(WSRPConstants.WSRP_USER_SCOPE_CACHE, cacheControl.getUserScope());
    assertNotNull(cacheControl.getValidateTag());
  }

  public void testUseCacheReturn() throws RemoteException, Exception {
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd);

    String portletHandle = CONTEXT_PATH + "/HelloWorld2";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);

    GetMarkup getMarkup = getMarkup(registrationContext, portletContext);
    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup);
    CacheControl cacheControl = response.getMarkupContext().getCacheControl();
    System.out.println("[test] validate tag key : " + cacheControl.getValidateTag());
    markupParams.setValidateTag(cacheControl.getValidateTag());
    sessionParams.setSessionID(response.getSessionContext().getSessionID());
    runtimeContext = new RuntimeContext();
    runtimeContext.setSessionParams(sessionParams);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    response = markupOperationsInterface.getMarkup(getMarkup);
//    assertTrue(response.getMarkupContext().getCacheControl().UseCachedMarkup().booleanValue());
  }

  public void testExistenceOfGlobal() throws RemoteException, Exception {
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd);

    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle("hello/EmptyPortletWithGlobalCache");

    MarkupResponse response = markupOperationsInterface.getMarkup(getMarkup(registrationContext, portletContext));
    CacheControl cacheControl = response.getMarkupContext().getCacheControl();
    assertEquals(-1, cacheControl.getExpires());
    assertEquals(WSRPConstants.WSRP_GLOBAL_SCOPE_CACHE, cacheControl.getUserScope());
    assertNotNull(cacheControl.getValidateTag());
  }

}
