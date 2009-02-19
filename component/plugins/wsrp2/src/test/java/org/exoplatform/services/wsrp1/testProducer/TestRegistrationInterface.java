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

package org.exoplatform.services.wsrp1.testProducer;

import org.exoplatform.services.wsrp1.type.WS1ModifyRegistration;
import org.exoplatform.services.wsrp1.type.WS1RegistrationContext;
import org.exoplatform.services.wsrp1.type.WS1RegistrationState;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.type.Deregister;
import org.exoplatform.services.wsrp2.utils.WSRPTypesTransformer;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class TestRegistrationInterface extends BaseTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestRegistrationInterface.setUp()");
  }

  public void testRegistrationHandle() throws Exception {
    WS1RegistrationContext returnedContext = register(registrationData);
    assertNotNull(returnedContext.getRegistrationHandle());
  }

  public void testIncorrectRegistrationData() throws Exception {
    registrationData.setConsumerAgent("exoplatform.1a.0b");
    try {
      register(registrationData);
      fail("the registration of the consumer should return a WS Fault");
//   patch by Pascal LEMOINE avoids exception here
    } catch (OperationFailed e) {
      //e.printStackTrace();
    }
  }

  public void testModifyRegistration() throws Exception {
    WS1RegistrationContext returnedContext = register(registrationData);
    assertNotNull(returnedContext.getRegistrationHandle());
    resolveRegistrationContext(returnedContext);
    WS1ModifyRegistration modifyRegistration = getModifyRegistration(returnedContext);
    WS1RegistrationState rS = WSRPTypesTransformer.getWS1RegistrationState(registrationOperationsInterface.modifyRegistration(WSRPTypesTransformer.getWS2ModifyRegistration(modifyRegistration)));
    assertNull(rS.getRegistrationState());
  }

  public void testIncorrectModifyRegistration() throws Exception {
    WS1RegistrationContext returnedContext = register(registrationData);
    registrationData.setConsumerAgent("exoplatform.1a.0b");
    resolveRegistrationContext(returnedContext);
    WS1ModifyRegistration modifyRegistration = getModifyRegistration(returnedContext);
    try {
      WS1RegistrationState rS = WSRPTypesTransformer.getWS1RegistrationState(registrationOperationsInterface.modifyRegistration(WSRPTypesTransformer.getWS2ModifyRegistration(modifyRegistration)));
      fail("the modify registration of the consumer should return a WS Fault");
    } catch (Exception e) {
    }
  }

  public void testDeregister() throws Exception {
    WS1RegistrationContext returnedContext = register(registrationData);
    returnedContext.getRegistrationHandle();
    resolveRegistrationContext(returnedContext);
    Deregister deregister = new Deregister();
    deregister.setRegistrationContext(WSRPTypesTransformer.getWS2RegistrationContext(returnedContext));
    registrationOperationsInterface.deregister(deregister);
    if (returnedContext.getRegistrationState() == null) {
      WS1ModifyRegistration modifyRegistration = getModifyRegistration(returnedContext);
      try {
        WS1RegistrationState rS = WSRPTypesTransformer.getWS1RegistrationState(registrationOperationsInterface.modifyRegistration(WSRPTypesTransformer.getWS2ModifyRegistration(modifyRegistration)));
        fail("the modify registration of the consumer should return a WS Fault");
      } catch (Exception e) {
      }
    } else {
      System.out.println("[test] can not try to modify registration here as the state is saved on consumer");
    }
  }

  public void testIncorrectDeregister() throws Exception {
    WS1RegistrationContext returnedContext = register(registrationData);
    resolveRegistrationContext(returnedContext);
    returnedContext.setRegistrationHandle("chunkHandle");
    try {
      Deregister deregister = new Deregister();
      deregister.setRegistrationContext(WSRPTypesTransformer.getWS2RegistrationContext(returnedContext));
      registrationOperationsInterface.deregister(deregister);
      fail("the deregistration of the consumer should return a WS Fault");
    } catch (Exception e) {
    }
  }

  private WS1ModifyRegistration getModifyRegistration(WS1RegistrationContext registrationContext) {
    WS1ModifyRegistration modifyRegistration = new WS1ModifyRegistration();
    modifyRegistration.setRegistrationContext(registrationContext);
    modifyRegistration.setRegistrationData(registrationData);
    return modifyRegistration;
  }

}
