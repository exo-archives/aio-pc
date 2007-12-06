/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.pci.model;

import java.io.Serializable;
import java.util.*;
import javax.portlet.* ;
import org.exoplatform.commons.utils.ExoEnumeration;

/**
 * Jun 9, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ExoPortletPreferences.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class ExoPortletPreferences extends HashMap  implements PortletPreferences, Serializable {
  private String preferencesValidator ;

  public Map getMap() { return this ; }

  public boolean isReadOnly(String key) {
    if(key == null) throw new IllegalArgumentException("preference name is null") ;
    Preference preference = (Preference) get(key) ; 
    if(preference == null) return false ;
    return preference.isReadOnly() ;
  }

  public java.lang.String getValue(String key, String def) {
    if(key == null) throw new IllegalArgumentException("preference name is null") ;
    Preference preference = (Preference) get(key) ; 
    return preference.getValue(def) ;
  }

  public String[] getValues(String key, String[] def) {
    if(key == null) throw new IllegalArgumentException("preference name is null") ;
    Preference preference = (Preference) get(key) ; 
    if(preference == null) return def ;
    return preference.getValues(key, def) ;
  }

  public void setValue(String key, String value) throws ReadOnlyException {
    if(key == null) throw new IllegalArgumentException("preference name is null") ;
    Preference preference = (Preference) get(key) ;
    if(preference == null) {
      preference = new Preference() ;
      preference.setName(key) ;
      put(key, preference) ;
    }
    if(preference.isReadOnly()) throw new ReadOnlyException("This preference is readonly") ;
    if(value == null)  return ;
    preference.addValue(value) ;
  }

  public void setValues(String key, String[] value) throws ReadOnlyException {
    if(key == null) throw new IllegalArgumentException("preference name is null") ;
    Preference preference = (Preference) get(key) ;
    if(preference == null) {
      preference = new Preference() ;
      preference.setName(key) ;
      put(key, preference) ;
    }
    if(preference.isReadOnly()) throw new ReadOnlyException("This preference is readonly") ;
    if(value == null)  return ;
    ArrayList list = new ArrayList() ;
    for(int i = 0; i < value.length; i++) {
      list.add(value[i]) ;
    }
    preference.setValues(list) ;
  }

  public Enumeration getNames() {
    return new ExoEnumeration(keySet().iterator()) ;
  }

  public void reset(java.lang.String key) throws ReadOnlyException {
    if(key == null) throw new IllegalArgumentException("preference name is null") ;
    Preference preference = (Preference) get(key) ;
    if(preference == null) return  ;
    if(preference.isReadOnly()) throw new ReadOnlyException("This preference is readonly") ;
    preference.clear() ;
  }

  public void store()  throws java.io.IOException,  ValidatorException {
    throw new Error("NOT SUPPORT") ;
  }

  public void addPreference(Preference pref) {
    put(pref.getName(), pref) ;
  }

  public String getPreferencesValidator() {
    return preferencesValidator;
  }

  public void setPreferencesValidator(String preferencesValidator) {
    this.preferencesValidator = preferencesValidator;
  }
}