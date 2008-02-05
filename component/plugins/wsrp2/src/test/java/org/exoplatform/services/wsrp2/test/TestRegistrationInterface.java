/*
 * Copyright 2001-2007 The eXo platform SAS  All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 15 janv. 2004
 */
package org.exoplatform.services.wsrp2.test;


import java.rmi.RemoteException;

import org.exoplatform.services.wsrp2.type.ModifyRegistrationRequest;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationState;

/**
 * @author Mestrallet Benjamin
 *         benjmestrallet@users.sourceforge.net
 */
public class TestRegistrationInterface extends BaseTest {


  public TestRegistrationInterface(String s) {
    super(s);
  }

  public void testRegistrationHandle() throws RemoteException {
    RegistrationContext returnedContext = registrationOperationsInterface.register(registrationData);
    assertNotNull(returnedContext.getRegistrationHandle());
  }

  public void testIncorrectRegistrationData() throws RemoteException {
    registrationData.setConsumerAgent("exoplatform.1a.0b");
    try {
      registrationOperationsInterface.register(registrationData);
//      fail("the registration of the consumer should return a WS Fault");
//   patch by Pascal LEMOINE avoids exception here
    } catch (RemoteException e) {
      //e.printStackTrace();
    }
  }

  public void testModifyRegistration() throws Exception {
    RegistrationContext returnedContext = registrationOperationsInterface.register(registrationData);
    assertNotNull(returnedContext.getRegistrationHandle());
    resolveRegistrationContext(returnedContext);
    ModifyRegistrationRequest modifyRegistration = getModifyRegistration(returnedContext);
    RegistrationState rS = registrationOperationsInterface.modifyRegistration(modifyRegistration);
    assertNull(rS.getRegistrationState());
  }

  public void testIncorrectModifyRegistration() throws Exception {
    RegistrationContext returnedContext = registrationOperationsInterface.register(registrationData);
    registrationData.setConsumerAgent("exoplatform.1a.0b");
    resolveRegistrationContext(returnedContext);
    ModifyRegistrationRequest modifyRegistration = getModifyRegistration(returnedContext);
    try {
      registrationOperationsInterface.modifyRegistration(modifyRegistration);
      fail("the modify registration of the consumer should return a WS Fault");
    } catch (RemoteException e) {
    }
  }

  public void testDeregister() throws Exception {
    RegistrationContext returnedContext = registrationOperationsInterface.register(registrationData);
    returnedContext.getRegistrationHandle();
    resolveRegistrationContext(returnedContext);
    registrationOperationsInterface.deregister(returnedContext);
    if (returnedContext.getRegistrationState() == null) {      
      ModifyRegistrationRequest modifyRegistration = getModifyRegistration(returnedContext);
      try {
        registrationOperationsInterface.modifyRegistration(modifyRegistration);
        fail("the modify registration of the consumer should return a WS Fault");
      } catch (RemoteException e) {
      }
    } else {
      System.out.println("[test] can not try to modify registration here as the state is saved on consumer");
    }
  }

  public void testIncorrectDeregister() throws Exception {
    RegistrationContext returnedContext = registrationOperationsInterface.register(registrationData);
    resolveRegistrationContext(returnedContext);
    returnedContext.setRegistrationHandle("chunkHandle");
    try {
      registrationOperationsInterface.deregister(returnedContext);
      fail("the deregistration of the consumer should return a WS Fault");
    } catch (RemoteException e) {
    }
  }

  private ModifyRegistrationRequest getModifyRegistration(RegistrationContext registrationContext) {
    ModifyRegistrationRequest modifyRegistration = new ModifyRegistrationRequest();
    modifyRegistration.setRegistrationContext(registrationContext);
    modifyRegistration.setRegistrationData(registrationData);
    return modifyRegistration;
  }

}
