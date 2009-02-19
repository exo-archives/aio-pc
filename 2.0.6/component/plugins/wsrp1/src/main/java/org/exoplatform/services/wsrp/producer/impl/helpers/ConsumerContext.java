/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp.producer.impl.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.exoplatform.services.wsrp.type.RegistrationData;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class ConsumerContext implements Serializable {

  private String             registrationHandle;

  private RegistrationData   datas;

  private Collection<String> clonedPortletHandles;

  public ConsumerContext(String registrationHandle, RegistrationData datas) {
    this.registrationHandle = registrationHandle;
    this.datas = datas;
    clonedPortletHandles = new ArrayList<String>();
  }

  public void addPortletHandle(String portletHandle) {
    clonedPortletHandles.add(portletHandle);
  }

  public void removePortletHandle(String portletHandle) {
    clonedPortletHandles.remove(portletHandle);
  }

  public String getRegistrationHandle() {
    return registrationHandle;
  }

  public RegistrationData getRegistationData() {
    return datas;
  }

  public boolean isPortletHandleRegistered(String portletHandle) {
    return clonedPortletHandles.contains(portletHandle);
  }

}
