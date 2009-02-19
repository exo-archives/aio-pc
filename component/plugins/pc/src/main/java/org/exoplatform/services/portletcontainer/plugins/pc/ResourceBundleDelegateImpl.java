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
package org.exoplatform.services.portletcontainer.plugins.pc;

import java.util.Locale;
import java.util.ResourceBundle;

import org.exoplatform.services.portletcontainer.bundle.ResourceBundleDelegate;
import org.exoplatform.services.resources.ResourceBundleService;

/**
 * Resource bundle delegate implementation.
 */
public class ResourceBundleDelegateImpl implements ResourceBundleDelegate {

  /**
   * Resource bundle service.
   */
  private final ResourceBundleService resourceBundleService;

  /**
   * @param resourceBundleService resource bundle service
   */
  public ResourceBundleDelegateImpl(final ResourceBundleService resourceBundleService) {
    this.resourceBundleService = resourceBundleService;
  }

  /**
   * Overridden method.
   *
   * @param portletBundleName portlet bundle name
   * @param locale locale
   * @return resource bundle
   * @see org.exoplatform.services.portletcontainer.bundle.ResourceBundleDelegate#lookupBundle(java.lang.String, java.util.Locale)
   */
  public final ResourceBundle lookupBundle(final String portletBundleName, final Locale locale) {
        String[] bundles = { portletBundleName };
        ResourceBundle rb = resourceBundleService.getResourceBundle(bundles, locale);
        return rb;
        
        
//    ResourceBundle rB = ResourceBundle.getBundle(portletBundleName, locale, Thread.currentThread()
//        .getContextClassLoader());
//    return new MapResourceBundle(rB, locale);
  }

}
