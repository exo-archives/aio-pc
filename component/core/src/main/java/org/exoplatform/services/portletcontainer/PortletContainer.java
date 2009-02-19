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
package org.exoplatform.services.portletcontainer;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.services.portletcontainer.config.Cache;
import org.exoplatform.services.portletcontainer.config.CustomMode;
import org.exoplatform.services.portletcontainer.config.CustomWindowState;
import org.exoplatform.services.portletcontainer.config.DelegatedBundle;
import org.exoplatform.services.portletcontainer.config.Global;
import org.exoplatform.services.portletcontainer.config.Properties;
import org.exoplatform.services.portletcontainer.config.SupportedContent;

/**
 * Jul 7, 2004 .
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: PortletContainer.java,v 1.2 2004/10/26 18:47:54 benjmestrallet
 *           Exp $
 */
public class PortletContainer {

  /**
   * Global.
   */
  private Global global;

  /**
   * Cache.
   */
  private Cache cache;

  /**
   * Supported content list.
   */
  private List<SupportedContent> supportedContent;

  /**
   * Custom portlet mode list.
   */
  private List<CustomMode> customMode;

  /**
   * Custom window state list.
   */
  private List<CustomWindowState> customWindowState;

  /**
   * Properties.
   */
  private List<Properties> properties;

  /**
   * Delegated bundle object.
   */
  private DelegatedBundle bundle;

  /**
   * Simple constructor.
   */
  public PortletContainer() {
    supportedContent = new ArrayList<SupportedContent>();
    customMode = new ArrayList<CustomMode>();
    customWindowState = new ArrayList<CustomWindowState>();
    properties = new ArrayList<Properties>();
  }

  /**
   * @return cache
   */
  public final Cache getCache() {
    return cache;
  }

  /**
   * @param cache cache
   */
  public final void setCache(final Cache cache) {
    this.cache = cache;
  }

  /**
   * @return custom portlet mode list
   */
  public final List<CustomMode> getCustomMode() {
    return customMode;
  }

  /**
   * @param list custom portlet mode list
   */
  public final void setCustomMode(final List<CustomMode> list) {
    this.customMode = list;
  }

  /**
   * @param mode custom portlet mode
   */
  public final void addCustomMode(final CustomMode mode) {
    customMode.add(mode);
  }

  /**
   * @return custom window state list
   */
  public final List<CustomWindowState> getCustomWindowState() {
    return customWindowState;
  }

  /**
   * @param list custom window state list
   */
  public final void setCustomWindowState(final List<CustomWindowState> list) {
    customWindowState = list;
  }

  /**
   * @param state custom window state
   */
  public final void addCustomWindowState(final CustomWindowState state) {
    customWindowState.add(state);
  }

  /**
   * @return global
   */
  public final Global getGlobal() {
    return global;
  }

  /**
   * @param global global
   */
  public final void setGlobal(final Global global) {
    this.global = global;
  }

  /**
   * @return properties list
   */
  public final List<Properties> getProperties() {
    return properties;
  }

  /**
   * @param list properties list
   */
  public final void setProperties(final List<Properties> list) {
    this.properties = list;
  }

  /**
   * @param props properties
   */
  public final void addProperties(final Properties props) {
    properties.add(props);
  }

  /**
   * @param bundle1 delegated bundle object
   */
  public final void setDelegatedBundle(final DelegatedBundle bundle1) {
    this.bundle = bundle1;
  }

  /**
   * @return delegated bundle object
   */
  public final DelegatedBundle getDelegatedBundle() {
    return bundle;
  }

  /**
   * @return supported content list
   */
  public final List<SupportedContent> getSupportedContent() {
    return supportedContent;
  }

  /**
   * @param list supported content list
   */
  public final void setSupportedContent(final List<SupportedContent> list) {
    supportedContent = list;
  }

  /**
   * @param supported supported content
   */
  public final void addSupportedContent(final SupportedContent supported) {
    supportedContent.add(supported);
  }

}
