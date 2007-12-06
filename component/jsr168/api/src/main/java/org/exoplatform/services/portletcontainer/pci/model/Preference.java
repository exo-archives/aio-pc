/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portletcontainer.pci.model;


import java.io.Serializable;
import java.util.*;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 27, 2003
 * Time: 9:21:41 PM
 */
public class Preference implements Serializable{
  private String name;
  private List values = new ArrayList();
  private boolean readOnly = false;  
  
  public Preference () {
  }
    
  public String getName() { return name; }

  public void setName(String name) { this.name = name; }

  public String getValue(String defaultValue) { 
  	if(values.size() > 0) {
  		return (String )values.get(0) ;
    }
    return defaultValue ;
  }
  
  public String[] getValues(String key, String[] def) {
    int size=values.size();
    if(size == 0) return def ;
    return (String[]) values.toArray(new String[size]) ;
  }
  
  public List getValues() { return values; }
  public void setValues(List values) { this.values = values; }

  public void addValue(String value) { values.add(value); }

  public boolean isReadOnly() { return readOnly; }

  public void setReadOnly(boolean readOnly) { this.readOnly = readOnly; }
  public void setReadOnly(String readOnly) { this.readOnly = "true".equals(readOnly); }
  
  public void clear() { values.clear() ; }
}
