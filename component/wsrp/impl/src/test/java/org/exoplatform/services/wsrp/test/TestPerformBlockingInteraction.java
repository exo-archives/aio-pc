/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 11 janv. 2004
 */
package org.exoplatform.services.wsrp.test;

import java.rmi.RemoteException;

import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.WindowState;

import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.type.BlockingInteractionRequest;
import org.exoplatform.services.wsrp.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp.type.InteractionParams;
import org.exoplatform.services.wsrp.type.MarkupRequest;
import org.exoplatform.services.wsrp.type.MarkupResponse;
import org.exoplatform.services.wsrp.type.NamedString;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.ServiceDescription;
import org.exoplatform.services.wsrp.type.StateChange;
import org.exoplatform.services.wsrp.type.UpdateResponse;



/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class TestPerformBlockingInteraction extends BaseTest{

  public TestPerformBlockingInteraction(String s) {
    super(s);
  }

  public void testSimplePerformBlockingInteraction() throws Exception {
    ServiceDescription sd = getServiceDescription(new String[]{"en"}) ;
    RegistrationContext rc = null;
    if(sd.isRequiresRegistration())
      rc = new RegistrationContext();
    String portletHandle = "hello/PortletToTestPerformBlockingInteraction";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    NamedString nS1 = new NamedString();
    nS1.setName("name1");
    nS1.setValue("value1");
    NamedString nS2 = new NamedString();
    nS2.setName("name2");
    nS2.setValue("value2");
    NamedString[] array = new NamedString[2];
    array[0] = nS1;
    array[1] = nS2;
    InteractionParams params = new InteractionParams();
    params.setPortletStateChange(StateChange.readWrite);
    params.setFormParameters(array);
    BlockingInteractionRequest performBlockingInteraction = getPerformBlockingInteraction(rc,
        portletContext, params);
    BlockingInteractionResponse response = markupOperationsInterface.
        performBlockingInteraction(performBlockingInteraction);
    UpdateResponse updateResponse = response.getUpdateResponse();
    assertEquals(WSRPConstants.WSRP_PREFIX + WindowState.MAXIMIZED.toString(),
        updateResponse.getNewWindowState());
    assertEquals(WSRPConstants.WSRP_PREFIX + PortletMode.EDIT.toString(),
        updateResponse.getNewMode());
    String navigationalState = updateResponse.getNavigationalState();
    markupParams.setNavigationalState(navigationalState);
    //look if we obtain the portlet state (case of consumer save state)
    byte[] portletState = updateResponse.getPortletContext().getPortletState();
    if( portletState != null){
      System.out.println("[test] consumer save state");
      portletContext.setPortletState(portletState);
    }
    runtimeContext.setSessionID(updateResponse.getSessionContext().getSessionID());
    MarkupRequest getMarkup = getMarkup(rc, portletContext);
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, getMarkup);
    MarkupResponse responseMarkup = markupOperationsInterface.getMarkup(getMarkup);
    assertEquals("value1", responseMarkup.getMarkupContext().getMarkupString());
    //test optimization cases
    if(updateResponse.getMarkupContext() != null)
      assertEquals(responseMarkup.getMarkupContext().getMarkupString(),
                   updateResponse.getMarkupContext().getMarkupString());
  }

  public void testSendRedirect() throws RemoteException{
    ServiceDescription sd = getServiceDescription(new String[]{"en"}) ;
    RegistrationContext rc = null;
    if(sd.isRequiresRegistration())
      rc = new RegistrationContext();
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle("hello/Portlet2TestSendRedirect");
    portletContext.setPortletState(null);//TODO
    InteractionParams params = new InteractionParams();
    params.setPortletStateChange(StateChange.readWrite);
    BlockingInteractionRequest performBlockingInteraction = getPerformBlockingInteraction(rc,
        portletContext, params);
    BlockingInteractionResponse response = markupOperationsInterface.
        performBlockingInteraction(performBlockingInteraction);
    assertEquals("/path/to/redirect/to.jsp", response.getRedirectURL());        
  }


  public void testReadOnlyStateChange() throws RemoteException {
    ServiceDescription sd = getServiceDescription(new String[]{"en"}) ;
    RegistrationContext rc = null;
    if(sd.isRequiresRegistration())
      rc = new RegistrationContext();
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle("hello/Portlet2TestStateUser");
    InteractionParams params = new InteractionParams();
    params.setPortletStateChange(StateChange.readOnly);
    BlockingInteractionRequest performBlockingInteraction = getPerformBlockingInteraction(rc,
        portletContext, params);
    try {
      markupOperationsInterface.performBlockingInteraction(performBlockingInteraction);
      fail("The portlet is in read only state!!!");
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public void testCloneBeforeWriteStateChange() throws RemoteException {
    RegistrationContext rc = registrationOperationsInterface.register(registrationData);
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle("hello/Portlet2TestStateUser2");
    InteractionParams params = new InteractionParams();
    params.setPortletStateChange(StateChange.cloneBeforeWrite);
    BlockingInteractionRequest performBlockingInteraction = getPerformBlockingInteraction(rc,
        portletContext, params);
    BlockingInteractionResponse response = markupOperationsInterface.
        performBlockingInteraction(performBlockingInteraction);
    assertNotSame("hello/Portlet2TestStateUser/windowID",
        response.getUpdateResponse().getPortletContext().getPortletHandle());
  }

  public void testStateSaveOnConsumer() throws RemoteException {
    ServiceDescription sd = getServiceDescription(new String[]{"en"}) ;
    RegistrationContext rc = null;
    if(sd.isRequiresRegistration())
      rc = new RegistrationContext();
    String portletHandle = "hello/Portlet2TestStateUser3";
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(portletHandle);
    InteractionParams params = new InteractionParams();
    params.setPortletStateChange(StateChange.readWrite);
    BlockingInteractionRequest performBlockingInteraction = getPerformBlockingInteraction(rc,
        portletContext, params);
   UpdateResponse updateResponse = markupOperationsInterface.
       performBlockingInteraction(performBlockingInteraction).getUpdateResponse();
    //look if we obtain the portlet state (case of consumer save state)
    byte[] portletState = updateResponse.getPortletContext().getPortletState();
    if( portletState != null){
      System.out.println("[test] consumer save state : " + portletState);
    } else {
      return; //we don't test portlet test save on producer here
    }
    Object o = null;
    try {
      o = IOUtil.deserialize(portletState);
    } catch (Exception e) {
      fail("The object should be deserializable");
    }
    assertTrue(o instanceof PortletPreferences);
    PortletPreferences pref = (PortletPreferences) o;
    String element = (String) pref.getNames().nextElement();
    assertEquals("attName", element);
    assertEquals("attValue", pref.getValue(element, "default"));
    runtimeContext.setSessionID(updateResponse.getSessionContext().getSessionID());
    manageTemplatesOptimization(sd, portletHandle);
    manageUserContextOptimization(sd, portletHandle, performBlockingInteraction);
    portletContext.setPortletState(portletState);
    updateResponse = markupOperationsInterface.
       performBlockingInteraction(performBlockingInteraction).getUpdateResponse();
    portletState = updateResponse.getPortletContext().getPortletState();
    try {
      o = IOUtil.deserialize(portletState);
    } catch (Exception e) {
      fail("The object should be deserializable");
    }
    assertTrue(o instanceof PortletPreferences);
    assertEquals("attValue2", ((PortletPreferences) o).getValue("attName2", "default"));
  }

  private BlockingInteractionRequest getPerformBlockingInteraction(RegistrationContext rc,
                                                                    PortletContext portletContext,
                                                                    InteractionParams params) {
    BlockingInteractionRequest performBlockingInteraction = new BlockingInteractionRequest();
    performBlockingInteraction.setRegistrationContext(rc);
    performBlockingInteraction.setPortletContext(portletContext);
    performBlockingInteraction.setRuntimeContext(runtimeContext);
    performBlockingInteraction.setUserContext(userContext);
    performBlockingInteraction.setMarkupParams(markupParams);
    performBlockingInteraction.setInteractionParams(params);
    return performBlockingInteraction;
  }

}
