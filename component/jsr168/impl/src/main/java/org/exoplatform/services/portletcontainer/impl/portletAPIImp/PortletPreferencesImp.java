/**
 * Copyright 2001-2003 The eXo Platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portletcontainer.impl.portletAPIImp;

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

import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.Preference;
import org.exoplatform.services.portletcontainer.persistence.PortletPreferencesPersister;

/**
 * Created by The eXo Platform SARL Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: Jul 27, 2003 Time: 9:21:41 PM
 * 
 * Should be pooled using fillPortletPreferences() and emptyPortletPrferences()
 * 
 * Every persistent storing is deleguate to a PersistentManager object that is
 * aware of the user identity.
 *  
 */
public class PortletPreferencesImp implements PortletPreferences, Serializable {

  transient private PreferencesValidator validator_;

  transient private ExoPortletPreferences defaultPreferences_;

  transient private boolean methodCalledIsAction_;

  transient private boolean stateChangeAuthorized_ = true;

  private ExoPortletPreferences preferences_ = new ExoPortletPreferences();

  private ExoPortletPreferences modifiedPreferences_ = new ExoPortletPreferences();

  private boolean stateSaveOnClient_;

  protected WindowID windowID_;

  private PortletPreferencesPersister persister_;

  public PortletPreferencesImp(PreferencesValidator validator,
      ExoPortletPreferences defaultPreferences, WindowID windowID,
      PortletPreferencesPersister persister) {
    this.validator_ = validator;
    this.defaultPreferences_ = defaultPreferences;
    this.windowID_ = windowID;
    this.persister_ = persister;
    fillCurrentPreferences();
  }

  private void fillCurrentPreferences() {
    if (defaultPreferences_ == null)
      return;
    Collection collection = defaultPreferences_.values();
    Preference wrapper;
    for (Iterator i = collection.iterator(); i.hasNext();) {
      Preference preferenceType = (Preference) i.next();
      wrapper = new Preference();
      wrapper.setName(preferenceType.getName());
      wrapper.setReadOnly(preferenceType.isReadOnly());
      List values = preferenceType.getValues();
      for (int j = 0; j < values.size(); j++) {
        wrapper.addValue((String) values.get(j));
      }
      preferences_.put(preferenceType.getName(), wrapper);
    }
  }

  public ExoPortletPreferences getCurrentPreferences() {
    return this.preferences_;
  }

  public void setCurrentPreferences(ExoPortletPreferences map) {
    this.preferences_ = map;
  }

  public boolean isReadOnly(String s) {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    Preference wrapper = (Preference) modifiedPreferences_.get(s);
    if (wrapper == null)
      wrapper = (Preference) preferences_.get(s);
    if (wrapper == null)
      return false;
    return wrapper.isReadOnly();
  }

  public String getValue(String s, String s1) {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    Preference wrapper = (Preference) modifiedPreferences_.get(s);
    if (wrapper == null)
      wrapper = (Preference) preferences_.get(s);
    if (wrapper == null || wrapper.getValues().isEmpty())
      return s1;
    return (String) wrapper.getValues().iterator().next();
  }

  public String[] getValues(String s, String[] strings) {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    Preference wrapper = (Preference) modifiedPreferences_.get(s);
    if (wrapper == null)
      wrapper = (Preference) preferences_.get(s);
    if (wrapper == null || wrapper.getValues().isEmpty())
      return strings;

    Object[] arr = wrapper.getValues().toArray();
    String[] sA = new String[arr.length];
    for (int i = 0; i < arr.length; i++) {
      Object o = arr[i];
      sA[i] = (String) o;
    }
    return sA;
  }

  public void setValue(String s, String s1) throws ReadOnlyException {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    if (isReadOnly(s))
      throw new ReadOnlyException("the value " + s + " can not be changed");

    Preference wrapper = new Preference();
    wrapper.setName(s);
    wrapper.setReadOnly(false);
    wrapper.getValues().add(s1);
    modifiedPreferences_.put(s, wrapper);
  }

  public void setValues(String s, String[] strings) throws ReadOnlyException {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    if (isReadOnly(s))
      throw new ReadOnlyException("the value " + s + " can not be changed");

    Preference wrapper = new Preference();
    wrapper.setName(s);
    wrapper.setReadOnly(false);

    Collection c = new ArrayList(strings.length);
    for (int i = 0; i < strings.length; i++) {
      String string = strings[i];
      c.add(string);
    }
    wrapper.getValues().addAll(c);

    modifiedPreferences_.put(s, wrapper);
  }

  public Enumeration getNames() {
    Collection c = new ArrayList();

    Set names = mergeModifiedPreference().keySet();
    for (Iterator iterator = names.iterator(); iterator.hasNext();) {
      String s = (String) iterator.next();
      c.add(s);
    }

    return Collections.enumeration(c);
  }

  public Map getMap() {
    Map toReturn = new HashMap();
    Collection keys = mergeModifiedPreference().keySet();
    for (Iterator iter = keys.iterator(); iter.hasNext();) {
      String key = (String) iter.next();
      Preference element = (Preference) mergeModifiedPreference().get(key);
      Collection c2 = element.getValues();
      String[] myArray = new String[c2.size()];
      int i = 0;
      for (Iterator iterator3 = c2.iterator(); iterator3.hasNext(); i++) {
        String value = (String) iterator3.next();
        myArray[i] = value;
      }
      toReturn.put(key, myArray);
    }

    return Collections.unmodifiableMap(toReturn);
  }

  private ExoPortletPreferences mergeModifiedPreference() {
    ExoPortletPreferences tmpMap = new ExoPortletPreferences();

    tmpMap.putAll(modifiedPreferences_);
    Collection keys = preferences_.keySet();
    for (Iterator iter = keys.iterator(); iter.hasNext();) {
      String key = (String) iter.next();
      if (!tmpMap.containsKey(key))
        tmpMap.put(key, preferences_.get(key));
    }
    return tmpMap;
  }

  public void reset(String s) throws ReadOnlyException {
    if (s == null)
      throw new IllegalArgumentException("the key given is null");
    if (isReadOnly(s))
      throw new ReadOnlyException("the value " + s + " can not be changed");
    Preference preferenceType = null;
    if (defaultPreferences_ != null)
      preferenceType = (Preference) defaultPreferences_.get(s);
    try {
      if (preferenceType == null) {
        preferences_.remove(s);                       
      } else {
        Preference wrapper = (Preference) preferences_.get(s);
        wrapper.getValues().clear();
        List defaultValues = preferenceType.getValues();
        for (int i = 0; i < defaultValues.size(); i++) {
          wrapper.addValue((String) defaultValues.get(i));
        }        
      }      
      modifiedPreferences_.remove(s);
      if(persister_.getPortletPreferences(windowID_) != null)
        save(preferences_);
    } catch (Exception e) {
      throw new RuntimeException("can not remove preference", e);
    }
  }

  /**
   * We first validate every field then we deleguates the storing to an object
   * that implements the PersistentManager interface
   * 
   * @throws IOException
   * @throws ValidatorException
   */
  public void store() throws IOException, ValidatorException {
    if (!isMethodCalledIsAction())
      throw new IllegalStateException(
          "the store() method can not be called from a render method");
    if (!isStateChangeAuthorized())
      throw new IllegalStateException(
          "the state of the portlet can not be changed");
    if (validator_ != null) {
      validator_.validate(this);
    }
    preferences_ = mergeModifiedPreference();
    modifiedPreferences_ = new ExoPortletPreferences();
    if (!isStateSaveOnClient()) {
      save(getCurrentPreferences());
    }
  }
  
  private void save(ExoPortletPreferences preferences) throws IOException{
    try {
      persister_.savePortletPreferences(windowID_, preferences);
    } catch (Exception ex) {
      throw new IOException(ex.getMessage());
    }
  }

  public void discard() {
    modifiedPreferences_ = new ExoPortletPreferences();
  }

  public void setMethodCalledIsAction(boolean b) {
    methodCalledIsAction_ = b;
  }

  public boolean isMethodCalledIsAction() {
    return methodCalledIsAction_;
  }

  public boolean isStateChangeAuthorized() {
    return stateChangeAuthorized_;
  }

  public void setStateChangeAuthorized(boolean stateChangeAuthorized) {
    this.stateChangeAuthorized_ = stateChangeAuthorized;
  }

  public void setStateSaveOnClient(boolean stateSaveOnClient) {
    this.stateSaveOnClient_ = stateSaveOnClient;
  }

  public boolean isStateSaveOnClient() {
    return stateSaveOnClient_;
  }
}