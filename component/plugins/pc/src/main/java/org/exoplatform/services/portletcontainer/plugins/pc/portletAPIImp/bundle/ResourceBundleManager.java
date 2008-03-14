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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.bundle;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.MapResourceBundle;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.bundle.ResourceBundleDelegate;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.PortletInfo;
import org.exoplatform.services.portletcontainer.pci.PortletData;

/**
 * @author Benjamin Mestrallet benjamin.mestrallet@exoplatform.com
 */
public class ResourceBundleManager {

  //elements that must be found in the resource bundle
  /**
   * Title resource key.
   */
  public static final String PORTLET_TITLE = PortletData.PORTLET_TITLE; //"javax.portlet.title";
  /**
   * Short title resource key.
   */
  public static final String PORTLET_SHORT_TITLE = PortletData.PORTLET_SHORT_TITLE; //"javax.portlet.short-title";

  /**
   * Keywords resource key.
   */
  public static final String KEYWORDS = PortletData.KEYWORDS; //"javax.portlet.keywords";

  /**
   * PC conf.
   */
  private final PortletContainerConf conf;

  /**
   * Cache.
   */
  private final ExoCache cache;

  /**
   * Logger.
   */
  private final Log log;

  /**
   * Exo container.
   */
  protected ExoContainer cont;

  /**
   * @param conf PC conf
   * @param cacheService cache service
   * @param context exo container context
   * @throws Exception exception
   */
  public ResourceBundleManager(final PortletContainerConf conf,
      final CacheService cacheService,
      final ExoContainerContext context) throws Exception {
    this.conf = conf;
    this.cache = cacheService.getCacheInstance(getClass().getName());
    log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
    this.cont = context.getContainer();
  }

  /**
   * @param portlet portlet
   * @param locale locale
   * @return resource bundle
   */
  public final ResourceBundle lookupBundle(final Portlet portlet, Locale locale) {
    String bundleName = portlet.getResourceBundle();
    String key = portlet.getPortletClass() + bundleName + locale;
    MapResourceBundle bundle = null;
    try {
      ResourceBundle res = (ResourceBundle) cache.get(key);
      if (res != null)
        return res;
      PortletInfo pI = portlet.getPortletInfo();
      if ((bundleName == null) || bundleName.equals("")) {
        MapResourceBundle bundle2 = new MapResourceBundle(locale);
        initBundle(pI, bundle2);
        cache.put(key, bundle2);
        return bundle2;
      }
      if (locale == null)
        locale = new Locale("en");

      if (conf.isBundleLookupDelegated()) {
        ResourceBundleDelegate delegate = (ResourceBundleDelegate) cont
            .getComponentInstanceOfType(ResourceBundleDelegate.class);
        bundle = (MapResourceBundle) delegate.lookupBundle(bundleName, locale);
        initBundle(pI, bundle);
      } else {
        ResourceBundle rB = ResourceBundle.getBundle(bundleName, locale, Thread.currentThread()
            .getContextClassLoader());
        bundle = new MapResourceBundle(rB, locale);
        initBundle(pI, bundle);
        cache.put(key, bundle);
      }
    } catch (Exception e) {
      log.error("Can not load resource bundle", e);
    }
    return bundle;
  }

  /**
   * @param pI portlet info
   * @param rB resource bundle
   */
  private void initBundle(final PortletInfo pI, final MapResourceBundle rB) {
    if ((pI != null) && (pI.getTitle() != null))
      try {
        rB.getString(PORTLET_TITLE);
      } catch (MissingResourceException ex) {
        rB.add(PORTLET_TITLE, pI.getTitle());
      }
    if ((pI != null) && (pI.getShortTitle() != null))
      try {
        rB.getString(PORTLET_SHORT_TITLE);
      } catch (MissingResourceException ex) {
        rB.add(PORTLET_SHORT_TITLE, pI.getShortTitle());
      }
    if ((pI != null) && (pI.getKeywords() != null))
      try {
        rB.getString(KEYWORDS);
      } catch (MissingResourceException ex) {
        rB.add(KEYWORDS, pI.getKeywords());
      }
  }

}