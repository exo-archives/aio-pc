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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletPreferences;
import javax.portlet.PreferencesValidator;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.Preference;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;

/**
 * Created by The eXo Platform SAS. Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: Jul 27, 2003 Time: 9:21:41 PM
 */
public class PortletPreferencesImp implements PortletPreferences, Serializable {

  /**
   * Validator.
   */
  private final transient PreferencesValidator  validator;

  /**
   * Default preferences.
   */
  private final transient ExoPortletPreferences defaultPreferences;

  /**
   * Method called.
   */
  transient private int                         methodCalled;

  /**
   * State change authorized.
   */
  transient private boolean                     stateChangeAuthorized = true;

  /**
   * Preferences.
   */
  private ExoPortletPreferences                 preferences           = new ExoPortletPreferences();

  /**
   * Modified preferences.
   */
  private ExoPortletPreferences                 modifiedPreferences   = new ExoPortletPreferences();

  /**
   * State save on client.
   */
  private boolean                               stateSaveOnClient;

  /**
   * Window id.
   */
  protected WindowID                            windowID;

  /**
   * Persister.
   */
  private final PortletPreferencesPersister     persister;

  /**
   * @param validator validator
   * @param defaultPreferences default preferences
   * @param windowID window id
   * @param persister persister
   */
  public PortletPreferencesImp(final PreferencesValidator validator,
                               final ExoPortletPreferences defaultPreferences,
                               final WindowID windowID,
                               final PortletPreferencesPersister persister) {
    this.validator = validator;
    this.defaultPreferences = defaultPreferences;
    this.windowID = windowID;
    this.persister = persister;
    fillCurrentPreferences();
  }

  /**
   * Initialization.
   */
  private void fillCurrentPreferences() {
    if (defaultPreferences == null)
      return;
    Collection<Preference> collection = defaultPreferences.values();
    Preference wrapper;
    for (Preference preferenceType : collection) {
      wrapper = new Preference();
      wrapper.setName(preferenceType.getName());
      wrapper.setReadOnly(preferenceType.isReadOnly());
      List<String> values = preferenceType.getValues();
      for (int j = 0; j < values.size(); j++) {
        wrapper.addValue(values.get(j));
      }
      preferences.put(preferenceType.getName(), wrapper);
    }
  }

  /**
   * @return preferences
   */
  public ExoPortletPreferences getCurrentPreferences() {
    return this.preferences;
  }

  /**
   * @param map map
   */
  public void setCurrentPreferences(final ExoPortletPreferences map) {
    this.preferences = map;
  }

  /**
   * Overridden method.
   * 
   * @param s name
   * @return is readonly
   * @see javax.portlet.PortletPreferences#isReadOnly(java.lang.String)
   */
  public boolean isReadOnly(final String s) {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    Preference wrapper = modifiedPreferences.get(s);
    if (wrapper == null)
      wrapper = preferences.get(s);
    if (wrapper == null)
      return false;
    return wrapper.isReadOnly();
  }

  /**
   * Overridden method.
   * 
   * @param s name
   * @param s1 default value
   * @return value
   * @see javax.portlet.PortletPreferences#getValue(java.lang.String,
   *      java.lang.String)
   */
  public String getValue(final String s, final String s1) {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    Preference wrapper = modifiedPreferences.get(s);
    if (wrapper == null)
      wrapper = preferences.get(s);
    if ((wrapper == null) || wrapper.getValues().isEmpty())
      return s1;
    return wrapper.getValues().iterator().next();
  }

  /**
   * Overridden method.
   * 
   * @param s name
   * @param strings default values
   * @return values
   * @see javax.portlet.PortletPreferences#getValues(java.lang.String,
   *      java.lang.String[])
   */
  public String[] getValues(final String s, final String[] strings) {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    Preference wrapper = modifiedPreferences.get(s);
    if (wrapper == null)
      wrapper = preferences.get(s);
    if ((wrapper == null) || wrapper.getValues().isEmpty())
      return strings;

    String[] arr = wrapper.getValues().toArray(new String[wrapper.getValues().size()]);
    return arr;
  }

  /**
   * Overridden method.
   * 
   * @param s name
   * @param s1 value
   * @throws ReadOnlyException exception
   * @see javax.portlet.PortletPreferences#setValue(java.lang.String,
   *      java.lang.String)
   */
  public void setValue(final String s, final String s1) throws ReadOnlyException {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    if (isReadOnly(s))
      throw new ReadOnlyException("The value '" + s + "' can not be changed");

    Preference wrapper = new Preference();
    wrapper.setName(s);
    wrapper.setReadOnly(false);
    wrapper.getValues().add(s1);
    modifiedPreferences.put(s, wrapper);
  }

  /**
   * Overridden method.
   * 
   * @param s key
   * @param strings values
   * @throws ReadOnlyException exception
   * @see javax.portlet.PortletPreferences#setValues(java.lang.String,
   *      java.lang.String[])
   */
  public void setValues(final String s, final String[] strings) throws ReadOnlyException {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    if (isReadOnly(s))
      throw new ReadOnlyException("The value '" + s + "' can not be changed");

    Preference wrapper = new Preference();
    wrapper.setName(s);
    wrapper.setReadOnly(false);

    Collection<String> c = new ArrayList<String>(strings.length);
    for (String string : strings)
      c.add(string);
    wrapper.getValues().addAll(c);

    modifiedPreferences.put(s, wrapper);
  }

  /**
   * Overridden method.
   * 
   * @return names
   * @see javax.portlet.PortletPreferences#getNames()
   */
  public Enumeration<String> getNames() {
    Collection<String> c = new ArrayList<String>();
    Set<String> names = mergeModifiedPreference().keySet();
    for (String s : names)
      c.add(s);

    return Collections.enumeration(c);
  }

  /**
   * Overridden method.
   * 
   * @return map
   * @see javax.portlet.PortletPreferences#getMap()
   */
  public Map<String, String[]> getMap() {
    Map<String, String[]> result = new HashMap<String, String[]>();
    Collection<String> keys = mergeModifiedPreference().keySet();
    for (String key : keys) {
      Preference element = mergeModifiedPreference().get(key);
      Collection<String> c2 = element.getValues();
      String[] myArray = new String[c2.size()];
      int i = 0;
      for (Iterator<String> iterator3 = c2.iterator(); iterator3.hasNext(); i++) {
        String value = iterator3.next();
        myArray[i] = value;
      }
      result.put(key, myArray);
    }

    return Collections.unmodifiableMap(result);
  }

  /**
   * @return preferences
   */
  private ExoPortletPreferences mergeModifiedPreference() {
    ExoPortletPreferences result = new ExoPortletPreferences();
    result.putAll(modifiedPreferences);
    Collection<String> keys = preferences.keySet();
    for (String key : keys) {
      if (!result.containsKey(key)) {
        result.put(key, preferences.get(key));
      }
    }
    return result;
  }

  /**
   * Overridden method.
   * 
   * @param s key
   * @throws ReadOnlyException exception
   * @see javax.portlet.PortletPreferences#reset(java.lang.String)
   */
  public void reset(final String s) throws ReadOnlyException {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    if (isReadOnly(s))
      throw new ReadOnlyException("The value '" + s + "' can not be changed");
    Preference preferenceType = null;
    if (defaultPreferences != null)
      preferenceType = defaultPreferences.get(s);
    try {
      if (preferenceType == null)
        preferences.remove(s);
      else {
        Preference wrapper = preferences.get(s);
        wrapper.getValues().clear();
        List<String> defaultValues = preferenceType.getValues();
        for (int i = 0; i < defaultValues.size(); i++)
          wrapper.addValue(defaultValues.get(i));
      }
      modifiedPreferences.remove(s);
      if (persister.getPortletPreferences(windowID) != null)
        save(preferences);
    } catch (Exception e) {
      throw new RuntimeException("can not remove preference", e);
    }
  }

  /**
   * We first validate every field then we delegates the storing to an object
   * that implements the PersistentManager interface
   * 
   * @throws IOException exception
   * @throws ValidatorException exception
   */
  public void store() throws IOException, ValidatorException {
    if (isMethodCalledRender())
      throw new IllegalStateException("the store() method can not be called from a render method");
    if (!isStateChangeAuthorized())
      throw new IllegalStateException("the state of the portlet can not be changed");
    if (validator != null)
      validator.validate(this);
    preferences = mergeModifiedPreference();
    modifiedPreferences = new ExoPortletPreferences();
    if (!isStateSaveOnClient()) {
      save(getCurrentPreferences());
    }
  }

  /**
   * @param preferences preferences
   * @throws IOException exception
   */
  private void save(final ExoPortletPreferences preferences) throws IOException {
    try {
      persister.savePortletPreferences(windowID, preferences);
    } catch (final Exception ex) {
      throw new IOException(ex.getMessage()) {
        @Override
        public Throwable getCause() {
          return ex;
        }
      };
    }
  }

  /**
   * Discard.
   */
  public void discard() {
    modifiedPreferences = new ExoPortletPreferences();
  }

  /**
   * @param b method called
   */
  public void setMethodCalled(final int b) {
    methodCalled = b;
  }

  /**
   * @return called method
   */
  public int getMethodCalled() {
    return methodCalled;
  }

  /**
   * @return if method called is action
   */
  public boolean isMethodCalledAction() {
    return (methodCalled == PCConstants.ACTION_INT);
  }

  /**
   * @return if method called is render
   */
  public boolean isMethodCalledRender() {
    return (methodCalled == PCConstants.RENDER_INT);
  }

  /**
   * @return is state change authorized
   */
  public boolean isStateChangeAuthorized() {
    return stateChangeAuthorized;
  }

  /**
   * @param stateChangeAuthorized state change authorized
   */
  public void setStateChangeAuthorized(final boolean stateChangeAuthorized) {
    this.stateChangeAuthorized = stateChangeAuthorized;
  }

  /**
   * @param stateSaveOnClient state save on client
   */
  public void setStateSaveOnClient(final boolean stateSaveOnClient) {
    this.stateSaveOnClient = stateSaveOnClient;
  }

  /**
   * @return is state save on client
   */
  public boolean isStateSaveOnClient() {
    return stateSaveOnClient;
  }

}
