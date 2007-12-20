/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.imp.Portlet168;

import javax.portlet.PortletPreferences;
import javax.portlet.PreferencesValidator;
import javax.portlet.ValidatorException;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletPreferencesImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.persistenceImp.DefaultPersistenceManager;

/**
 * Created by The eXo Platform SAS         .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 1st, 2003
 **/
public class TestHibernatePersistenceManager extends BaseTest {
  public TestHibernatePersistenceManager(String s) {
    super(s);
  }

  public void setUp() throws Exception {
    super.setUp();
  }

  public void testStorePortletPreferences() throws Exception {
    ExoWindowID windowID = new ExoWindowID("exotest:/hello/HelloWorld/banner");
    ExoPortletPreferences prefs =
        (portletApp_.getPortlet().get(0)).getPortletPreferences();
    //PreferencesValidator validator =
    //    proxy.getValidator(prefs.getPreferencesValidator());
    PreferencesValidator validator = new PreferencesValidator() {
      public void validate(PortletPreferences portletPreferences) throws ValidatorException {
      }
    };
    PortletPreferencesImp preferences =
    	new PortletPreferencesImp(validator, prefs, windowID, persister);
    //preferences.setValue("param-1", "value-1") ; //null pointer exception ??
    //preferences.setValue("param-2", "value-2") ;
    preferences.setMethodCalledIsAction(PCConstants.actionInt);
    preferences.store();
    DefaultPersistenceManager manager =
        (DefaultPersistenceManager) PortalContainer.getInstance().
        getComponentInstanceOfType(DefaultPersistenceManager.class);
    //get the window for an anonymous user
    Input input = new Input();
    input.setInternalWindowID(windowID);
    PortletWindowInternal pwi = manager.getWindow(input, null);
    //get the window for a identified user
    pwi = manager.getWindow(input, null);
  }
}