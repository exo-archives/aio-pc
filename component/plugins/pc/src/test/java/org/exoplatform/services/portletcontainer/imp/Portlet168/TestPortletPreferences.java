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

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.portlet.PreferencesValidator;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.plugins.pc.PortletApplicationProxy;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletPreferencesImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.persistenceImp.PersistenceManager;

/**
 * Created by The eXo Platform SAS         .
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Oct 6, 2003
 * Time: 1:03:37 PM
 */
public class TestPortletPreferences extends BaseTest {

  javax.portlet.PortletPreferences pP;

  PortletApplicationProxy proxy;
  private ExoWindowID windowID;


  public TestPortletPreferences(String s) {
    super(s);
  }

	public void setUp() throws Exception {
		super.setUp();
    proxy = (PortletApplicationProxy) PortalContainer.getInstance().
                                      getComponentInstance("war_template" +PCConstants.PORTLET_APP_ENCODER);
    ExoPortletPreferences prefs = (portletApp_.getPortlet().get(0)).getPortletPreferences();
    PreferencesValidator validator = proxy.getValidator(prefs.getPreferencesValidator(),
      (portletApp_.getPortlet().get(0)).getPortletName());

    windowID = new ExoWindowID("exotest:/hello/HelloWorld/banner");
    pP = new PortletPreferencesImp(validator, prefs, windowID, persister);
	}

  public void testMap() {
    Map<String, String[]> map = pP.getMap();
    assertTrue("Map non empty is false", !map.isEmpty());
  }

  public void testNames() {
    Enumeration e = pP.getNames();
    while (e.hasMoreElements()) {
      e.nextElement();
    }
  }

  public void testGetValue() {
    assertEquals("DEFAULT", pP.getValue("not_exist", "DEFAULT"));
    assertEquals("http://timeserver.myco.com", pP.getValue("time-server", "ops"));
    assertEquals("HH", pP.getValue("time-format", "ops"));
  }

  public void testGetValues() {
    assertEquals("DEFAULT", pP.getValues("not_exist", new String[]{"DEFAULT"})[0]);
    String[] arr = pP.getValues("time-format", new String[]{});
    assertTrue("HH".equals(arr[0]));
  }

  public void testSetValue() throws ReadOnlyException {
    //test a simple set value on a non read only preference (no tag)
    pP.setValue("time-server", "MyValue");
    assertEquals("MyValue", pP.getValue("time-server", "ops"));

    //test a simple set value on an other non read only preference (with tag)
    pP.setValue("time-format", "MyFormat");
    assertTrue(pP.getValues("time-format", new String[]{}).length == 1);

    //test a simple set value on a read only preference
    try {
      pP.setValue("port", "NotPossible");
    } catch (ReadOnlyException e) {
      assertEquals("the value port can not be changed", e.getMessage());
    }

    //add a new preference (not defined in xml)
    pP.setValue("NewValue", "MyNewValue");
    assertEquals("MyNewValue", pP.getValue("NewValue", "ops"));

    //test xcix : manage the previously added preference as a non read only pref
    pP.setValue("NewValue", "MyNewValue2");
    assertEquals("MyNewValue2", pP.getValue("NewValue", "ops"));
  }

  public void testSetValues() throws ReadOnlyException {
    //test a simple set values on a non read only preference (no tag)
    pP.setValues("time-server", new String[] {"val1", "val2"});
    assertTrue(pP.getValues("time-server", new String[]{}).length == 2);

    //test a simple set values on an other non read only preference (with tag)
    pP.setValues("time-format", new String[] {"val1", "val2"});
    assertTrue(pP.getValues("time-format", new String[]{}).length == 2);

    //test a simple set values on a read only preference
    try {
      pP.setValues("port", new String[]{"NotPossible"});
    } catch (ReadOnlyException e) {
      assertEquals("the value port can not be changed", e.getMessage());
    }

    //add a new preference (not defined in xml)
    pP.setValues("NewValue", new String[]{"MyNewValue1","MyNewValue2"});
    assertEquals("MyNewValue1", pP.getValues("NewValue", new String[]{"ops"})[0]);

    //test xcix : manage the previously added preference as a non read only pref
    pP.setValues("NewValue", new String[]{"MyNewValue1bis","MyNewValue2bis"});
    assertEquals("MyNewValue2bis", pP.getValues("NewValue", new String[]{"ops"})[1]);
  }

  public void testIsReadOnly(){
    assertTrue(!pP.isReadOnly("not_exist"));
    assertTrue(pP.isReadOnly("port"));
    assertTrue(!pP.isReadOnly("time-server"));
    assertTrue(!pP.isReadOnly("time-format"));
  }

  public void testReset() throws ReadOnlyException {
    //test a reset of a value with default (means present in portlet.xml)
    pP.setValue("time-server", "MyValueToBeReseted");
    assertEquals("MyValueToBeReseted", pP.getValue("time-server", "ops"));
    pP.reset("time-server");
    assertEquals("http://timeserver.myco.com", pP.getValue("time-server", "ops"));

    //test a reset of a value with no default (means not present in portlet.xml)
    //test xci
    pP.setValue("NewValue", "MyValueToBeDeleted");
    assertEquals("MyValueToBeDeleted", pP.getValue("NewValue", "ops"));
    pP.reset("NewValue");
    assertEquals("ops", pP.getValue("NewValue", "ops"));

    //test a reset of a null value with no default
    pP.setValue("NewValue", null);
    assertNull(pP.getValue("NewValue", "ops"));
    pP.reset("NewValue");
    assertEquals("ops", pP.getValue("NewValue", "ops"));

    //test a reset of a null value with a default
    pP.setValue("time-server", null);
    assertNull(pP.getValue("time-server", "ops"));
    pP.reset("time-server");
    assertEquals("http://timeserver.myco.com", pP.getValue("time-server", "ops"));
  }

  public void testStore() throws ReadOnlyException, IOException, ValidatorException {
    pP.setValue("param-1", "value-1") ;
    ((PortletPreferencesImp)pP).setMethodCalledIsAction(PCConstants.ACTION_INT);
    pP.store();

    PersistenceManager manager =
        (PersistenceManager) PortalContainer.getInstance().
        getComponentInstance(PersistenceManager.class);
    Input input = new Input();
    input.setInternalWindowID(windowID);
    PortletWindowInternal pwi = manager.getWindow(input, null);
    assertEquals("value-1", pwi.getPreferences().getValue("param-1", "opts"));
  }

  public void testStateChangeFlag(){
    ((PortletPreferencesImp)pP).setMethodCalledIsAction(PCConstants.ACTION_INT);
    ((PortletPreferencesImp)pP).setStateChangeAuthorized(false);
    try {
      pP.store();
      fail("Sate change should NOT be authorized");
    } catch (Throwable t) {
    }
  }

  /** added by  Max Shaposhnik@09.2007
   *  PLT 17.1
   *  If the store method is invoked within the scope
   *  of a render method invocation, it must throw an
   *  IllegalStateException.
   */

  public void testResourceRequest() throws ReadOnlyException, IOException,ValidatorException {
   pP.setValue("param1", "value1");
   ((PortletPreferencesImp)pP).setMethodCalledIsAction(PCConstants.RENDER_INT);
   try {
   pP.store();
   } catch (IllegalStateException e){
   assertEquals("the store() method can not be called from a render method", e.getMessage());
   }
  }

}