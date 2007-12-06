package org.exoplatform.services.portletcontainer.impl;

import java.util.Locale;
import java.util.ResourceBundle;

import org.exoplatform.commons.utils.MapResourceBundle;
import org.exoplatform.services.portletcontainer.bundle.ResourceBundleDelegate;
import org.exoplatform.services.resources.ResourceBundleService;

public class ResourceBundleDelegateImpl implements ResourceBundleDelegate {
	  
  private ResourceBundleService resourceBundleService;
	  
  public ResourceBundleDelegateImpl(ResourceBundleService resourceBundleService) {    
    this.resourceBundleService = resourceBundleService;
  }
	  
  public ResourceBundle lookupBundle(String portletBundleName, Locale locale){            
//    String[] bundles = { portletBundleName };
//    return resourceBundleService.getResourceBundle(bundles, locale);
    ResourceBundle rB = 
      ResourceBundle.getBundle(portletBundleName, locale, Thread.currentThread().getContextClassLoader());
    return new MapResourceBundle(rB, locale);
  }

}
