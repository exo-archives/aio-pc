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
package org.exoplatform.frameworks.portletcontainer.access;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.organization.auth.ExoLoginJAASLoginModule;

/**
 * standalone JAAS login module.
 *
 */
public class StandaloneJAASLoginModule extends ExoLoginJAASLoginModule {

  /**
   * Overridden method.
   *
   * @return exo container
   * @throws Exception exception
   * @see org.exoplatform.services.organization.auth.ExoLoginJAASLoginModule#getContainer()
   */
  @Override
  public final ExoContainer getContainer() throws Exception {
    return StandaloneContainer.getInstance();
  }

  /**
   * Overridden method.
   *
   * @throws Exception exception
   * @see org.exoplatform.services.organization.auth.ExoLoginJAASLoginModule#postProcessOperations()
   */
  @Override
  public void postProcessOperations() throws Exception {

  }

  /**
   * Overridden method.
   *
   * @throws Exception exception
   * @see org.exoplatform.services.organization.auth.ExoLoginJAASLoginModule#preProcessOperations()
   */
  @Override
  public void preProcessOperations() throws Exception {

  }
}
