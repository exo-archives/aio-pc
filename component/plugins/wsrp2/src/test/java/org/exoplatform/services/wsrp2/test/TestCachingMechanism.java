/*
 * Copyright 2001-2007 The eXo platform SAS  All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 */

package org.exoplatform.services.wsrp2.test;

import java.rmi.RemoteException;

import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.type.CacheControl;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;


/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 25 janv. 2004
 * Time: 19:29:55
 */

public class TestCachingMechanism extends BaseTest{

  public TestCachingMechanism(String s) {
    super(s);
  }

  public void testExistenceOfValidateTag() throws RemoteException {
    ServiceDescription sd = getServiceDescription(new String[]{"en"});
    RegistrationContext rc = null;
    if(sd.isRequiresRegistration())  rc = new RegistrationContext();
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
