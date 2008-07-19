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

package org.exoplatform.services.wsrp2.producer;

import java.util.Map;

import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public interface PersistentStateManager {

  public byte[] register(String registrationHandle, RegistrationData data) throws WSRPException;

  public RegistrationData getRegistrationData(RegistrationContext registrationContext) throws WSRPException;

  public void deregister(RegistrationContext registrationContext) throws WSRPException;

  public boolean isRegistered(RegistrationContext registrationContext) throws WSRPException;

  public boolean isConsumerConfiguredPortlet(String portletHandle,
                                             RegistrationContext registrationContext) throws WSRPException;

  public void addConsumerConfiguredPortletHandle(String portletHandle,
                                                 RegistrationContext registrationContext) throws WSRPException;

  public void removeConsumerConfiguredPortletHandle(String portletHandle,
                                                    RegistrationContext registrationContext) throws WSRPException;

  public Map<String, String[]> getNavigationalState(String navigationalState) throws WSRPException;

  public void putNavigationalState(String navigationalState, Map<String, String[]> renderParameters) throws WSRPException;

  public Map<String, String[]> getInteractionSate(String interactionState) throws WSRPException;

  public void putInteractionState(String interactionState,
                                  Map<String, String[]> interactionParameters) throws WSRPException;

  public Map<String, String[]> getResourceState(String resourceState) throws WSRPException;

  public void putResourceState(String resourceState, Map<String, String[]> resourceParameters) throws WSRPException;

}
