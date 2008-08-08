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

package org.exoplatform.services.wsrp2.test;

import java.rmi.RemoteException;

import org.exoplatform.services.wsrp2.type.Deregister;
import org.exoplatform.services.wsrp2.type.ModifyRegistration;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationState;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class TestRegistrationInterface extends BaseTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestRegistrationInterface.setUp()");
  }

  public void testRegistrationHandle() throws RemoteException {
    RegistrationContext rC = registrationOperationsInterface.register(register);
    assertNotNull(rC.getRegistrationHandle());
  }

  public void testIncorrectRegistrationData() throws RemoteException {
    registrationData.setConsumerAgent("exoplatform.1a.0b");
    try {
      registrationOperationsInterface.register(register);
//      fail("the registration of the consumer should return a WS Fault");
//   patch by Pascal LEMOINE avoids exception here
    } catch (RemoteException e) {
      //e.printStackTrace();
    }
  }

//  public void testModifyRegistration() throws Exception {
//    RegistrationContext returnedContext = registrationOperationsInterface.register(register);
//    assertNotNull(returnedContext.getRegistrationHandle());
//    resolveRegistrationContext(returnedContext);
//    ModifyRegistration modifyRegistration = getModifyRegistration(returnedContext);
//    RegistrationState rS = registrationOperationsInterface.modifyRegistration(modifyRegistration);
//    assertNull(rS.getRegistrationState());
//  }

  public void testIncorrectModifyRegistration() throws Exception {
    RegistrationContext returnedContext = registrationOperationsInterface.register(register);
    registrationData.setConsumerAgent("exoplatform.1a.0b");
    resolveRegistrationContext(returnedContext);
    ModifyRegistration modifyRegistration = getModifyRegistration(returnedContext);
    try {
      registrationOperationsInterface.modifyRegistration(modifyRegistration);
      fail("the modify registration of the consumer should return a WS Fault");
    } catch (RemoteException e) {
    }
  }

  public void testDeregister() throws Exception {
    RegistrationContext returnedContext = registrationOperationsInterface.register(register);
    returnedContext.getRegistrationHandle();
    resolveRegistrationContext(returnedContext);
    Deregister deregister = new Deregister(returnedContext, userContext);
    registrationOperationsInterface.deregister(deregister);
    if (returnedContext.getRegistrationState() == null) {
      ModifyRegistration modifyRegistration = getModifyRegistration(returnedContext);
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
    RegistrationContext returnedContext = registrationOperationsInterface.register(register);
    resolveRegistrationContext(returnedContext);
    returnedContext.setRegistrationHandle("chunkHandle");
    Deregister deregister = new Deregister(returnedContext, userContext);
    try {
      registrationOperationsInterface.deregister(deregister);
      fail("the deregistration of the consumer should return a WS Fault");
    } catch (RemoteException e) {
    }
  }

  private ModifyRegistration getModifyRegistration(RegistrationContext registrationContext) {
    ModifyRegistration modifyRegistration = new ModifyRegistration();
    modifyRegistration.setRegistrationContext(registrationContext);
    modifyRegistration.setRegistrationData(registrationData);
    return modifyRegistration;
  }

}
