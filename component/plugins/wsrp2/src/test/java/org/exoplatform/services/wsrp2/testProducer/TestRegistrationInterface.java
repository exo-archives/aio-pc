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

package org.exoplatform.services.wsrp2.testProducer;

import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.type.Deregister;
import org.exoplatform.services.wsrp2.type.GetRegistrationLifetime;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.ModifyRegistration;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.SetRegistrationLifetime;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class TestRegistrationInterface extends BaseTest {

  private static final String INCORRECT_CONSUMER_AGENT = "";
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    log();
  }

  public void testRegister() throws Exception {
    log();
    RegistrationContext rC = registrationOperationsInterface.register(register);
    assertNotNull(rC.getRegistrationHandle());
  }

  public void testIncorrectRegistrationData() throws Exception {
    log();
    registrationData.setConsumerAgent(INCORRECT_CONSUMER_AGENT);
    try {
      registrationOperationsInterface.register(register);
      fail("the registration of the consumer should return a WS Fault");
    } catch (Exception e) {
    }
  }

  public void testModifyRegistration() throws Exception {
    log();
    RegistrationContext returnedContext = registrationOperationsInterface.register(register);
    assertNotNull(returnedContext.getRegistrationHandle());
    assertRegistrationContext(returnedContext);
    ModifyRegistration modifyRegistration = getModifyRegistration(returnedContext);
    RegistrationState rS = registrationOperationsInterface.modifyRegistration(modifyRegistration);
    assertNull(rS.getRegistrationState());
  }

  public void testIncorrectModifyRegistration() throws Exception {
    log();
    RegistrationContext returnedContext = registrationOperationsInterface.register(register);
    registrationData.setConsumerAgent(INCORRECT_CONSUMER_AGENT);
    assertRegistrationContext(returnedContext);
    ModifyRegistration modifyRegistration = getModifyRegistration(returnedContext);
    try {
      registrationOperationsInterface.modifyRegistration(modifyRegistration);
      fail("the modify registration of the consumer should return a WS Fault");
    } catch (Exception e) {
    }
  }

  public void testDeregister() throws Exception {
    log();
    RegistrationContext returnedContext = registrationOperationsInterface.register(register);
    returnedContext.getRegistrationHandle();
    assertRegistrationContext(returnedContext);
    Deregister deregister = new Deregister();
    deregister.setRegistrationContext(returnedContext);
    deregister.setUserContext(userContext);
    registrationOperationsInterface.deregister(deregister);
    if (returnedContext.getRegistrationState() == null) {
      ModifyRegistration modifyRegistration = getModifyRegistration(returnedContext);
      try {
        registrationOperationsInterface.modifyRegistration(modifyRegistration);
        fail("the modify registration of the consumer should return a WS Fault");
      } catch (Exception e) {
      }
    } else {
      System.out.println("[test] can not try to modify registration here as the state is saved on consumer");
    }
  }

  public void testIncorrectDeregister() throws Exception {
    log();
    RegistrationContext returnedContext = registrationOperationsInterface.register(register);
    assertRegistrationContext(returnedContext);

    returnedContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);
    Deregister deregister = new Deregister();
    deregister.setRegistrationContext(returnedContext);
    deregister.setUserContext(userContext);
    try {
      registrationOperationsInterface.deregister(deregister);
      fail("the deregistration of the consumer should return a WS Fault");
    } catch (Exception e) {
    }
  }

  private ModifyRegistration getModifyRegistration(RegistrationContext registrationContext) {
    ModifyRegistration modifyRegistration = new ModifyRegistration();
    modifyRegistration.setRegistrationContext(registrationContext);
    modifyRegistration.setRegistrationData(registrationData);
    return modifyRegistration;
  }

  public void testRegistrationHandleWithLifetime() throws Exception {
    log();
    register.setLifetime(getLifetimeInSec(5));
    RegistrationContext rC = registrationOperationsInterface.register(register);
    assertNotNull(rC.getRegistrationHandle());

    register.setLifetime(null);

    register.setLifetime(getLifetimeInSec(-5));
    try {
      registrationOperationsInterface.register(register);
      fail("the registration of the consumer should return a WS Fault");
    } catch (Exception e) {
    }
    register.setLifetime(null);
  }

  public void testGetRegistrationLifetime() throws Exception {
    log();
    register.setLifetime(getLifetimeInSec(5));
    RegistrationContext rC = registrationOperationsInterface.register(register);
    assertNotNull(rC.getRegistrationHandle());

    updateCurrentTime(rC);
    GetRegistrationLifetime getRegistrationLifetime = new GetRegistrationLifetime();
    getRegistrationLifetime.setRegistrationContext(rC);
    Lifetime lifetime2 = registrationOperationsInterface.getRegistrationLifetime(getRegistrationLifetime);
    assertNotNull(lifetime2);
    assertNotNull(lifetime2.getTerminationTime());
    assertNotNull(lifetime2.getCurrentTime());
    register.setLifetime(null);
  }

  public void testGetRegistrationLifetimeNull() throws Exception {
    log();
    RegistrationContext rC = registrationOperationsInterface.register(register);
    assertNotNull(rC.getRegistrationHandle());

    GetRegistrationLifetime getRegistrationLifetime = new GetRegistrationLifetime();
    getRegistrationLifetime.setRegistrationContext(rC);
    Lifetime lifetime2 = registrationOperationsInterface.getRegistrationLifetime(getRegistrationLifetime);
    assertNull(lifetime2);
    register.setLifetime(null);
  }

  public void testSetRegistrationLifetime() throws Exception {
    log();
    RegistrationContext rC = registrationOperationsInterface.register(register);
    assertNotNull(rC.getRegistrationHandle());

    SetRegistrationLifetime setRegistrationLifetime = new SetRegistrationLifetime();
    setRegistrationLifetime.setRegistrationContext(rC);
    setRegistrationLifetime.setUserContext(userContext);
    setRegistrationLifetime.setLifetime(getLifetimeInSec(5));
    Lifetime lifetime2 = registrationOperationsInterface.setRegistrationLifetime(setRegistrationLifetime);
    assertNotNull(lifetime2);

    updateCurrentTime(rC);
    GetRegistrationLifetime getRegistrationLifetime = new GetRegistrationLifetime();
    getRegistrationLifetime.setRegistrationContext(rC);
    Lifetime lifetime3 = registrationOperationsInterface.getRegistrationLifetime(getRegistrationLifetime);
    assertNotNull(lifetime3);
    register.setLifetime(null);
  }

  public void testSetRegistrationLifetimeNull() throws Exception {
    log();
    register.setLifetime(getLifetimeInSec(5));
    RegistrationContext rC = registrationOperationsInterface.register(register);
    assertNotNull(rC.getRegistrationHandle());
    assertNotNull(rC.getScheduledDestruction());

    updateCurrentTime(rC);
    SetRegistrationLifetime setRegistrationLifetime = new SetRegistrationLifetime();
    setRegistrationLifetime.setRegistrationContext(rC);
    setRegistrationLifetime.setUserContext(userContext);
    setRegistrationLifetime.setLifetime(null);
    Lifetime lifetime2 = registrationOperationsInterface.setRegistrationLifetime(setRegistrationLifetime);
    assertNull(lifetime2);

    updateCurrentTime(rC);
    GetRegistrationLifetime getRegistrationLifetime = new GetRegistrationLifetime();
    getRegistrationLifetime.setRegistrationContext(rC);
    Lifetime lifetime3 = registrationOperationsInterface.getRegistrationLifetime(getRegistrationLifetime);
    assertNull(lifetime3);
    register.setLifetime(null);
  }

  public void testModifyRegistrationWithLifetime() throws Exception {
    log();
    register.setLifetime(getLifetimeInSec(5));
    RegistrationContext returnedContext = registrationOperationsInterface.register(register);
    assertNotNull(returnedContext.getRegistrationHandle());
    assertRegistrationContext(returnedContext);
    assertNotNull(returnedContext.getScheduledDestruction());

    updateCurrentTime(returnedContext);
    ModifyRegistration modifyRegistration = getModifyRegistration(returnedContext);
    RegistrationState rS = registrationOperationsInterface.modifyRegistration(modifyRegistration);
    assertNotNull(rS.getScheduledDestruction());
    register.setLifetime(null);
  }

  // Tests With Invalid Registration

  public void testModifyRegistrationWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);
    ModifyRegistration modifyRegistration = getModifyRegistration(registrationContext);
    try {
      registrationOperationsInterface.modifyRegistration(modifyRegistration);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }

    registrationContext.setRegistrationHandle(null);
    modifyRegistration = getModifyRegistration(registrationContext);
    try {
      registrationOperationsInterface.modifyRegistration(modifyRegistration);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }

    registrationContext = null;
    modifyRegistration = getModifyRegistration(registrationContext);
    try {
      registrationOperationsInterface.modifyRegistration(modifyRegistration);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }
  }

  public void testDeregisterWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);

    Deregister deregister = new Deregister();
    deregister.setRegistrationContext(registrationContext);
    deregister.setUserContext(userContext);
    try {
      registrationOperationsInterface.deregister(deregister);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }

    registrationContext.setRegistrationHandle(null);
    deregister = new Deregister();
    deregister.setRegistrationContext(registrationContext);
    deregister.setUserContext(userContext);
    try {
      registrationOperationsInterface.deregister(deregister);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }

    registrationContext = null;
    deregister = new Deregister();
    deregister.setRegistrationContext(registrationContext);
    deregister.setUserContext(userContext);
    try {
      registrationOperationsInterface.deregister(deregister);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }

  }

  public void testGetRegistrationLifetimeWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);

    GetRegistrationLifetime getRegistrationLifetime = new GetRegistrationLifetime();
    getRegistrationLifetime.setRegistrationContext(registrationContext);
    try {
      registrationOperationsInterface.getRegistrationLifetime(getRegistrationLifetime);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }

    registrationContext.setRegistrationHandle(null);

    getRegistrationLifetime = new GetRegistrationLifetime();
    getRegistrationLifetime.setRegistrationContext(registrationContext);
    try {
      registrationOperationsInterface.getRegistrationLifetime(getRegistrationLifetime);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }

    registrationContext = null;
    getRegistrationLifetime = new GetRegistrationLifetime();
    getRegistrationLifetime.setRegistrationContext(registrationContext);
    try {
      registrationOperationsInterface.getRegistrationLifetime(getRegistrationLifetime);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }

  }

  public void testSetRegistrationLifetimeWithInvalidRegistration() throws Exception {
    log();
    ServiceDescription sd = getServiceDescription(new String[] { "en" });
    createRegistrationContext(sd, true);
    registrationContext.setRegistrationHandle(DUMMY_REGISTRATION_HANDLE);

    SetRegistrationLifetime setRegistrationLifetime = new SetRegistrationLifetime();
    setRegistrationLifetime.setRegistrationContext(registrationContext);
    setRegistrationLifetime.setUserContext(userContext);
    setRegistrationLifetime.setLifetime(null);
    try {
      registrationOperationsInterface.setRegistrationLifetime(setRegistrationLifetime);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }

    registrationContext.setRegistrationHandle(null);
    setRegistrationLifetime = new SetRegistrationLifetime();
    setRegistrationLifetime.setRegistrationContext(registrationContext);
    setRegistrationLifetime.setUserContext(userContext);
    setRegistrationLifetime.setLifetime(null);
    try {
      registrationOperationsInterface.setRegistrationLifetime(setRegistrationLifetime);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }

    registrationContext = null;
    setRegistrationLifetime = new SetRegistrationLifetime();
    setRegistrationLifetime.setRegistrationContext(registrationContext);
    setRegistrationLifetime.setUserContext(userContext);
    setRegistrationLifetime.setLifetime(null);
    try {
      registrationOperationsInterface.setRegistrationLifetime(setRegistrationLifetime);
      fail("Should be an InvalidRegistration exception, because the given registration handle was incorrect");
    } catch (InvalidRegistration e) {
    }
  }

}
