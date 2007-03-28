/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 17 janv. 2004
 */
package org.exoplatform.services.wsrp.utils;

import org.exoplatform.services.wsrp.type.LocalizedString;
import org.exoplatform.services.wsrp.type.NamedString;

/**
 * @author Mestrallet Benjamin
 *         benjmestrallet@users.sourceforge.net
 */
public class Utils {

  public static LocalizedString getLocalizedString(String value, String lang, String rn) {
    LocalizedString tmp = new LocalizedString();
    tmp.setValue(value);
    tmp.setLang(lang);
    tmp.setResourceName(rn);
    return tmp;
  }

  public static LocalizedString getLocalizedString(String value, String lang) {
    LocalizedString tmp = new LocalizedString();
    tmp.setValue(value);
    tmp.setLang(lang);
    return tmp;
  }

  public static NamedString getNamesString(String name, String value){
    NamedString tmp = new NamedString();
    tmp.setName(name);
    tmp.setValue(value);
    return tmp;
  }

}
