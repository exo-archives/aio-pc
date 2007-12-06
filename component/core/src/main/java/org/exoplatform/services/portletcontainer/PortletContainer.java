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
 * Jul 7, 2004
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: PortletContainer.java,v 1.2 2004/10/26 18:47:54 benjmestrallet
 *           Exp $
 */
public class PortletContainer {
  private Global                  global;

  private Cache                   cache;

  private List<SupportedContent>  supportedContent;

  private List<CustomMode>        customMode;

  private List<CustomWindowState> customWindowState;

  private List<Properties>        properties;

  private DelegatedBundle         bundle;

  public PortletContainer() {
    supportedContent = new ArrayList<SupportedContent>();
    customMode = new ArrayList<CustomMode>();
    customWindowState = new ArrayList<CustomWindowState>();
    properties = new ArrayList<Properties>();
  }

  public Cache getCache() {
    return cache;
  }

  public void setCache(Cache cache) {
    this.cache = cache;
  }

  public List<CustomMode> getCustomMode() {
    return customMode;
  }

  public void setCustomMode(List<CustomMode> list) {
    this.customMode = list;
  }

  public void addCustomMode(CustomMode mode) {
    customMode.add(mode);
  }

  public List<CustomWindowState> getCustomWindowState() {
    return customWindowState;
  }

  public void setCustomWindowState(List<CustomWindowState> list) {
    customWindowState = list;
  }

  public void addCustomWindowState(CustomWindowState state) {
    customWindowState.add(state);
  }

  public Global getGlobal() {
    return global;
  }

  public void setGlobal(Global global) {
    this.global = global;
  }

  public List<Properties> getProperties() {
    return properties;
  }

  public void setProperties(List<Properties> list) {
    this.properties = list;
  }

  public void addProperties(Properties props) {
    properties.add(props);
  }

  public void setDelegatedBundle(DelegatedBundle bundle) {
    this.bundle = bundle;
  }

  public DelegatedBundle getDelegatedBundle() {
    return bundle;
  }

  public List<SupportedContent> getSupportedContent() {
    return supportedContent;
  }

  public void setSupportedContent(List<SupportedContent> list) {
    supportedContent = list;
  }

  public void addSupportedContent(SupportedContent supported) {
    supportedContent.add(supported);
  }

}
