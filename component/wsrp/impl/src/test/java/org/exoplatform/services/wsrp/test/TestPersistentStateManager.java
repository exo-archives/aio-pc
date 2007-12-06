/*
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 */
package org.exoplatform.services.wsrp.test;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.wsrp.producer.PersistentStateManager;
import org.exoplatform.services.wsrp.producer.impl.PersistentStateManagerImpl;
import org.exoplatform.services.wsrp.producer.impl.StateData;
import org.exoplatform.services.wsrp.type.RegistrationData;
/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 25 janv. 2004
 * Time: 19:29:55
 */

public class TestPersistentStateManager extends BaseTest{
  private PersistentStateManagerImpl psmanager_ ;

  public TestPersistentStateManager(String s) {
    super(s);
  }

  public void setUp() throws Exception {
    super.setUp() ;
    PortalContainer manager = PortalContainer.getInstance();
    psmanager_ = (PersistentStateManagerImpl) manager.
        getComponentInstanceOfType(PersistentStateManager.class);
  }
  
  public void testPersistentStateData() throws Exception {
    RegistrationData registrationData = new RegistrationData();
    registrationData.setConsumerName("www.exoplatform.com");
    registrationData.setConsumerAgent("exoplatform.1.0");
    registrationData.setMethodGetSupported(false);
    registrationData.setConsumerModes(CONSUMER_MODES);
    registrationData.setConsumerWindowStates(CONSUMER_STATES);
    registrationData.setConsumerUserScopes(CONSUMER_SCOPES);
    registrationData.setCustomUserProfileData(CONSUMER_CUSTOM_PROFILES);
    registrationData.setRegistrationProperties(null);//allows extension of the specs
    registrationData.setExtensions(null);//allows extension of the specs

    psmanager_.save("test" , "RegistrationData" , registrationData) ;

    StateData data = psmanager_.load("test");
    assertTrue("Expect data is not null", data != null ) ;

    psmanager_.remove("test");
    data = psmanager_.load("test");
    assertTrue("Expect data is null", data == null ) ;
  }
}
