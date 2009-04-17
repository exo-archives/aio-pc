/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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

import java.util.List;
import java.util.Map;

import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public interface PersistentStateManager {

  /**
   * Register.
   * 
   * @param registrationHandle the registration handle
   * @param data the data
   * 
   * @return the byte[]
   * 
   * @throws WSRPException the WSRP exception
   */
  public byte[] register(String registrationHandle, RegistrationData data) throws WSRPException;

  /**
   * Gets the registration data.
   * 
   * @param registrationContext the registration context
   * 
   * @return the registration data
   * 
   * @throws WSRPException the WSRP exception
   */
  public RegistrationData getRegistrationData(RegistrationContext registrationContext) throws WSRPException;

  /**
   * Deregister.
   * 
   * @param registrationContext the registration context
   * 
   * @throws WSRPException the WSRP exception
   */
  public void deregister(RegistrationContext registrationContext) throws WSRPException;

  /**
   * Checks if is registered.
   * 
   * @param registrationContext the registration context
   * 
   * @return true, if is registered
   * 
   * @throws WSRPException the WSRP exception
   */
  public boolean isRegistered(RegistrationContext registrationContext) throws WSRPException;

  /**
   * Checks if is consumer configured portlet.
   * 
   * @param portletHandle the portlet handle
   * @param registrationContext the registration context
   * 
   * @return true, if is consumer configured portlet
   * 
   * @throws WSRPException the WSRP exception
   */
  public boolean isConsumerConfiguredPortlet(String portletHandle,
                                             RegistrationContext registrationContext) throws WSRPException;

  /**
   * Adds the consumer configured portlet handle.
   * 
   * @param portletHandle the portlet handle
   * @param registrationContext the registration context
   * 
   * @throws WSRPException the WSRP exception
   */
  public void addConsumerConfiguredPortletHandle(String portletHandle,
                                                 RegistrationContext registrationContext) throws WSRPException;

  /**
   * Removes the consumer configured portlet handle.
   * 
   * @param portletHandle the portlet handle
   * @param registrationContext the registration context
   * 
   * @throws WSRPException the WSRP exception
   */
  public void removeConsumerConfiguredPortletHandle(String portletHandle,
                                                    RegistrationContext registrationContext) throws WSRPException;

  /**
   * Gets the navigational state.
   * 
   * @param navigationalState the navigational state
   * 
   * @return the navigational state
   * 
   * @throws WSRPException the WSRP exception
   */
  public Map<String, String[]> getNavigationalState(String navigationalState) throws WSRPException;

  /**
   * Put navigational state.
   * 
   * @param navigationalState the navigational state
   * @param renderParameters the render parameters
   * 
   * @throws WSRPException the WSRP exception
   */
  public void putNavigationalState(String navigationalState, Map<String, String[]> renderParameters) throws WSRPException;

  /**
   * Gets the interaction sate.
   * 
   * @param interactionState the interaction state
   * 
   * @return the interaction sate
   * 
   * @throws WSRPException the WSRP exception
   */
  public Map<String, String[]> getInteractionSate(String interactionState) throws WSRPException;

  /**
   * Put interaction state.
   * 
   * @param interactionState the interaction state
   * @param interactionParameters the interaction parameters
   * 
   * @throws WSRPException the WSRP exception
   */
  public void putInteractionState(String interactionState,
                                  Map<String, String[]> interactionParameters) throws WSRPException;

  /**
   * Gets the resource state.
   * 
   * @param resourceState the resource state
   * 
   * @return the resource state
   * 
   * @throws WSRPException the WSRP exception
   */
  public Map<String, String[]> getResourceState(String resourceState) throws WSRPException;

  /**
   * Put resource state.
   * 
   * @param resourceState the resource state
   * @param resourceParameters the resource parameters
   * 
   * @throws WSRPException the WSRP exception
   */
  public void putResourceState(String resourceState, Map<String, String[]> resourceParameters) throws WSRPException;

  /**
   * Gets the registration lifetime.
   * 
   * @param registrationContext the registration context
   * 
   * @return the registration lifetime
   * 
   * @throws WSRPException the WSRP exception
   */
  public Lifetime getRegistrationLifetime(RegistrationContext registrationContext) throws WSRPException;

  /**
   * Gets the registration lifetime by the key.
   * 
   * @param registrationContext the registration context
   * 
   * @return the registration lifetime
   * 
   * @throws WSRPException the WSRP exception
   */
  public Lifetime getLifetime(String key) throws WSRPException;
  /**
   * Put registration lifetime.
   * 
   * @param registrationHandle the registration handle
   * @param lifetime the lifetime
   * 
   * @return the lifetime
   * 
   * @throws WSRPException the WSRP exception
   */
  public Lifetime putRegistrationLifetime(String registrationHandle, Lifetime lifetime) throws WSRPException;

  /**
   * Put portlet lifetime.
   * 
   * @param portletHandle the portlet handle
   * @param lifetime the lifetime
   * 
   * @return the lifetime
   * 
   * @throws WSRPException the WSRP exception
   */
  public Lifetime putPortletLifetime(String portletHandle, Lifetime lifetime) throws WSRPException;

  /**
   * Gets the portlet lifetime.
   * 
   * @param portletHandle the portlet handle
   * 
   * @return the portlet lifetime
   * 
   * @throws WSRPException the WSRP exception
   */
  public Lifetime getPortletLifetime(String portletHandle) throws WSRPException;
  
  public List<String>  getRegistrationHandles()  throws WSRPException;
}
