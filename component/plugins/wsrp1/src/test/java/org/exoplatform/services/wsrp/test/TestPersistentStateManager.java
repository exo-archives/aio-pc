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

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.wsrp.producer.PersistentStateManager;
import org.exoplatform.services.wsrp.producer.impl.PersistentStateManagerImpl;
import org.exoplatform.services.wsrp.producer.impl.WSRP1StateData;
import org.exoplatform.services.wsrp.type.RegistrationData;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 25 janv. 2004
 * Time: 19:29:55
 */

public class TestPersistentStateManager extends BaseTest {

  private PersistentStateManagerImpl psmanager_;
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestPersistentStateManager.setUp()");
    PortalContainer manager = PortalContainer.getInstance();
    psmanager_ = (PersistentStateManagerImpl) manager.getComponentInstanceOfType(PersistentStateManager.class);
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

    psmanager_.save("test", "RegistrationData", registrationData);

    WSRP1StateData data = psmanager_.load("test");
    assertTrue("Expect data is not null", data != null);

    psmanager_.remove("test");
    data = psmanager_.load("test");
    assertTrue("Expect data is null", data == null);
  }
}
