/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portletcontainer.imp;


import javax.portlet.PortletPreferences;
import javax.portlet.PreferencesValidator;
import javax.portlet.ValidatorException;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletPreferencesImp;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;

/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 1st, 2003
 **/
public class TestPersistentManager extends BaseTest {
  public TestPersistentManager(String s) {
    super(s);
  }

  public void setUp() throws Exception {
    super.setUp();
  }

  public void testStorePortletPreferences() throws Exception {
  	ExoWindowID windowID = new ExoWindowID("exotest:/hello/HelloWorld/banner");
    ExoPortletPreferences prefs =
        ((Portlet) portletApp_.getPortlet().get(0)).getPortletPreferences();
    PreferencesValidator validator = new PreferencesValidator() {
      public void validate(PortletPreferences portletPreferences) throws ValidatorException {
      }
    };
    PortletPreferencesImp preferences = new PortletPreferencesImp(validator,
        prefs, windowID, persister);
    //preferences.setValue("param-1", "value-1") ; //null pointer exception ??
    //preferences.setValue("param-2", "value-2") ;
    preferences.setMethodCalledIsAction(true);
    preferences.store();
    /*
    CMSPersistenceManager manager =
        (CMSPersistenceManager) PortalContainer.getInstance().getService(CMSPersistenceManager.class);
    String windowLocalId = preferences.windowLocalId;
    //get the window for an anonymous user
    Input input = new Input();
    input.setWindowId("hello/HelloWorld/winID");
    input.setUserId("anon");
    PortletWindowInternal pwi = manager.getWindow(input);
    //get the window for a identified user
    pwi = manager.getWindow(input);
    */
  }
}