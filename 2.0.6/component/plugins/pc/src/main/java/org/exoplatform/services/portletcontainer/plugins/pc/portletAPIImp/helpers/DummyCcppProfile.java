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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers;

import java.util.Set;

import javax.ccpp.Attribute;
import javax.ccpp.Component;
import javax.ccpp.Profile;
import javax.ccpp.ProfileDescription;

/**
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: $
 */
public class DummyCcppProfile implements Profile {

  /**
   * Overridden method.
   *
   * @param name name
   * @return attribute
   * @see javax.ccpp.Profile#getAttribute(java.lang.String)
   */
  public final Attribute getAttribute(final String name) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Overridden method.
   *
   * @return attributes
   * @see javax.ccpp.Profile#getAttributes()
   */
  public final Set getAttributes() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @return component
   * @see javax.ccpp.Profile#getComponent(java.lang.String)
   */
  public final Component getComponent(final String name) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Overridden method.
   *
   * @return components
   * @see javax.ccpp.Profile#getComponents()
   */
  public final Set getComponents() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Overridden method.
   *
   * @return description
   * @see javax.ccpp.Profile#getDescription()
   */
  public final ProfileDescription getDescription() {
    // TODO Auto-generated method stub
    return null;
  }

}
