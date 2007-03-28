 /***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.portletcontainer.bundle;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public interface ResourceBundleDelegate {
  
  public ResourceBundle lookupBundle(String portletBundledName, Locale locale);

}
