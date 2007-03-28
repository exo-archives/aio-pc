/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.portletcontainer.impl.portletAPIImp.bundle;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.MapResourceBundle;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.bundle.ResourceBundleDelegate;
import org.exoplatform.services.portletcontainer.impl.PortletContainerConf;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.PortletInfo;

/**
 * @author Benjamin Mestrallet benjamin.mestrallet@exoplatform.com
 */
public class ResourceBundleManager {
  //elements that must be found in the resource bundle
  public static final String PORTLET_TITLE = "javax.portlet.title";
  public static final String PORTLET_SHORT_TITLE = "javax.portlet.short-title";
  public static final String KEYWORDS = "javax.portlet.keywords";

  private PortletContainerConf conf;
  private ExoCache cache;
  private Log log_;
  protected ExoContainer cont;

  public ResourceBundleManager(PortletContainerConf conf,  LogService logService,
                               CacheService cacheService,
                               ExoContainerContext context) throws Exception {
    this.conf = conf;
    this.cache = cacheService.getCacheInstance(getClass().getName());
    log_ = logService.getLog("org.exoplatform.services.portletcontainer");
    this.cont = context.getContainer();
  }

  public ResourceBundle lookupBundle(Portlet portlet, Locale locale) {    
    String bundleName = portlet.getResourceBundle();
    String key = portlet.getPortletClass() + bundleName + locale;
    MapResourceBundle bundle = null;
    try {
      ResourceBundle res = (ResourceBundle) cache.get(key);
      if (res != null) return res ;
      PortletInfo pI = portlet.getPortletInfo();
      if (bundleName == null || bundleName.equals("")) {
        MapResourceBundle bundle2 = new MapResourceBundle(locale);
        initBundle(pI, bundle2);
        cache.put(key, bundle2);
        return bundle2;
      }
      if (locale == null) locale = new Locale("en");
      
      if (conf.isBundleLookupDelegated()) {
        ResourceBundleDelegate delegate = 
          (ResourceBundleDelegate) cont.getComponentInstanceOfType(ResourceBundleDelegate.class);
        bundle = (MapResourceBundle) delegate.lookupBundle(bundleName, locale);
        initBundle(pI, bundle);
      } else {
        ResourceBundle rB = 
          ResourceBundle.getBundle(bundleName, locale, Thread.currentThread().getContextClassLoader());
        bundle = new MapResourceBundle(rB, locale);
        initBundle(pI, bundle);
        cache.put(key, bundle);  
      }
    } catch (Exception e) {
      log_.error("Can not load resource bundle", e);            
    }
    return bundle;
  }

  private void initBundle(PortletInfo pI, MapResourceBundle rB) {
    if (pI != null && pI.getTitle() != null){
      try {
        rB.getString(PORTLET_TITLE);
      } catch(MissingResourceException ex) {
        rB.add(PORTLET_TITLE, pI.getTitle());
      }
    }

    if (pI != null && pI.getShortTitle() != null){
      try {
        rB.getString(PORTLET_SHORT_TITLE);
      } catch(MissingResourceException ex) {
        rB.add(PORTLET_SHORT_TITLE, pI.getShortTitle());
      }            
    }

    if (pI != null && pI.getKeywords() != null){
      try {
        rB.getString(KEYWORDS);
      } catch(MissingResourceException ex) {
        rB.add(KEYWORDS, pI.getKeywords());
      }                  
    }
  }

}