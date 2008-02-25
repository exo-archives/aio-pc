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
package org.exoplatform.services.portletcontainer.pci.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.exoplatform.commons.utils.ExoEnumeration;

/**
 * Jun 9, 2004 .
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: ExoPortletPreferences.java,v 1.1 2004/07/13 02:31:13 tuan08
 *           Exp $
 */
public class ExoPortletPreferences extends HashMap<String, Preference> implements
    PortletPreferences, Serializable {

  /**
   * Preferences validator.
   */
  private String preferencesValidator;

  // portlet api 2.0

  /**
   * Id.
   */
  private String id;

  /**
   * Overridden method.
   *
   * @return prefs map
   * @see javax.portlet.PortletPreferences#getMap()
   */
  public final Map<String, String[]> getMap() {
    Map<String, String[]> result = new HashMap<String, String[]>();
    Iterator<String> iter = keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      result.put(key, getValues(key, null));
    }
    return Collections.unmodifiableMap(result);
  }

  /**
   * Overridden method.
   *
   * @param key key
   * @return is readonly
   * @see javax.portlet.PortletPreferences#isReadOnly(java.lang.String)
   */
  public final boolean isReadOnly(final String key) {
    if (key == null)
      throw new IllegalArgumentException("preference name is null");
    Preference preference = get(key);
    if (preference == null)
      return false;
    return preference.isReadOnly();
  }

  /**
   * Overridden method.
   *
   * @param key key
   * @param def def
   * @return value
   * @see javax.portlet.PortletPreferences#getValue(java.lang.String, java.lang.String)
   */
  public final String getValue(final String key, final String def) {
    if (key == null)
      throw new IllegalArgumentException("preference name is null");
    Preference preference = get(key);
    return preference.getValue(def);
  }

  /**
   * Overridden method.
   *
   * @param key key
   * @param def def
   * @return values
   * @see javax.portlet.PortletPreferences#getValues(java.lang.String, java.lang.String[])
   */
  public final String[] getValues(final String key, final String[] def) {
    if (key == null)
      throw new IllegalArgumentException("preference name is null");
    Preference preference = get(key);
    if (preference == null)
      return def;
    return preference.getValues(key, def);
  }

  /**
   * Overridden method.
   *
   * @param key key
   * @param value value
   * @throws ReadOnlyException exception
   * @see javax.portlet.PortletPreferences#setValue(java.lang.String, java.lang.String)
   */
  public final void setValue(final String key, final String value) throws ReadOnlyException {
    if (key == null)
      throw new IllegalArgumentException("preference name is null");
    Preference preference = get(key);
    if (preference == null) {
      preference = new Preference();
      preference.setName(key);
      put(key, preference);
    }
    if (preference.isReadOnly())
      throw new ReadOnlyException("This preference is readonly");
    if (value == null)
      return;
    preference.addValue(value);
  }

  public final void setValues(final String key, final String[] value) throws ReadOnlyException {
    if (key == null)
      throw new IllegalArgumentException("preference name is null");
    Preference preference = get(key);
    if (preference == null) {
      preference = new Preference();
      preference.setName(key);
      put(key, preference);
    }
    if (preference.isReadOnly())
      throw new ReadOnlyException("This preference is readonly");
    if (value == null)
      return;
    ArrayList<String> list = new ArrayList<String>();
    for (String element : value)
      list.add(element);
    preference.setValues(list);
  }

  public final Enumeration<String> getNames() {
    return new ExoEnumeration(keySet().iterator());
  }

  public final void reset(final java.lang.String key) throws ReadOnlyException {
    if (key == null)
      throw new IllegalArgumentException("preference name is null");
    Preference preference = get(key);
    if (preference == null)
      return;
    if (preference.isReadOnly())
      throw new ReadOnlyException("This preference is readonly");
    preference.clear();
  }

  public final void store() throws java.io.IOException, ValidatorException {
    throw new Error("NOT SUPPORT");
  }

  public final void addPreference(final Preference pref) {
    put(pref.getName(), pref);
  }

  public final String getPreferencesValidator() {
    return preferencesValidator;
  }

  public final void setPreferencesValidator(final String preferencesValidator) {
    this.preferencesValidator = preferencesValidator;
  }

  public final String getId() {
    return this.id;
  }

  public final void setId(final String value) {
    this.id = value;
  }

}
