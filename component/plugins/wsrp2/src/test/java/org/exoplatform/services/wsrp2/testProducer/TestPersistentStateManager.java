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

import java.util.Arrays;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.producer.impl.PersistentStateManagerJCRImpl;
import org.exoplatform.services.wsrp2.producer.impl.WSRP2StateData;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 25 janv. 2004
 * Time: 19:29:55
 */

public class TestPersistentStateManager extends BaseTest {

  protected ExoContainer             container;

  private PersistentStateManagerJCRImpl psmanager_;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    log();
    try {
      // Leaving for compatibility reasons
//      container = PortalContainer.getInstance(); // was that: null
//      container = RootContainer.getInstance().getPortalContainer("portal"); // null
      container = StandaloneContainer.getInstance(Thread.currentThread().getContextClassLoader()); //OK
    } catch (Throwable t) {
      t.printStackTrace();
    }
    psmanager_ = (PersistentStateManagerJCRImpl) container.getComponentInstanceOfType(PersistentStateManager.class);
  }

  public void testPersistentStateData() throws Exception {
    log();
    RegistrationData registrationData = new RegistrationData();
    registrationData.setConsumerName("www.exoplatform.com");
    registrationData.setConsumerAgent(consumerAgent);
    registrationData.setMethodGetSupported(false);
    registrationData.getConsumerModes().addAll(Arrays.asList(CONSUMER_MODES));
    registrationData.getConsumerWindowStates().addAll(Arrays.asList(CONSUMER_STATES));
    registrationData.getConsumerUserScopes().addAll(Arrays.asList(CONSUMER_SCOPES));
//    registrationData.setCustomUserProfileData(CONSUMER_CUSTOM_PROFILES);
//    registrationData.getRegistrationProperties(null);//allows extension of the specs
//    registrationData.getExtensions(null);//allows extension of the specs
    RegistrationContext registrationContext = new RegistrationContext();
    registrationContext.setRegistrationHandle("test");
    
    
    psmanager_.register(registrationContext.getRegistrationHandle(), registrationData);
    
//    psmanager_.save("test", "RegistrationData", registrationData);

    registrationData = psmanager_.getRegistrationData(registrationContext);
    assertTrue("Expect data is not null", registrationData != null);
    
//    WSRP2StateData data = psmanager_.load("test");
//    assertTrue("Expect data is not null", data != null);

    psmanager_.deregister(registrationContext);
    registrationData = psmanager_.getRegistrationData(registrationContext);
    assertTrue("Expect data is null", registrationData == null);
    
//    psmanager_.remove("test");
//    data = psmanager_.load("test");
//    assertTrue("Expect data is null", data == null);
  }
}
