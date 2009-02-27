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

package org.exoplatform.services.wsrp2.consumer;

import java.util.List;

import org.exoplatform.services.wsrp2.type.ClientData;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.NamedString;

/**
 * The <code>WSRPRequest</code> is the base interface for all requests to a
 * consumer side invocation of a integrated remote portlet. Specialized
 * interfaces exist for markup and action calls.
 * 
 * @author Benjamin Mestrallet
 * @see WSRPMarkupRequest
 * @see WSRPInteractionRequest
 */
public interface WSRPBaseRequest {

  /*****************************************************************************
   * * * * * * * * * * * * * * * * * * * * * * * * * For RuntimeContext * * * * * * * * * * * * * * * * * * * * * * * * * *
   * 
   * @author alexey
   */

  /**
   * Get the ID of the session context.
   * 
   * @return The session context
   */
  public String getSessionID();

  /**
   * Get an opaque string which corresponds to a unique reference to this use of
   * the portlet.
   * 
   * @return The portlet instance key
   */
  public String getPortletInstanceKey();

  /*****************************************************************************
   * * * * * * * * * * * * * * * * * * * * * * * * * For NavigationalContext * * * * * * * * * * * * * * * * * * * * * * * * * *
   * 
   * @author alexey
   */

  /**
   * Get the current navigational state of the portlet.
   * 
   * @return The navigational state
   */

  public String getNavigationalState();

  /**
   * Get the current navigational values of the portlet
   * 
   * @return The navigational values
   */

  public List<NamedString> getNavigationalValues();

  /*****************************************************************************
   * * * * * * * * * * * * * * * * * * * * * * * * * For MimeRequest (There are
   * MarkupParams and ResourceParams which extends MimeRequest) * * * * * * * * * * * * * * * * * * * * * * * * * *
   * 
   * @author alexey
   */

  /**
   * Get the current window state of the portlet.
   * 
   * @return The window state
   */
  public String getWindowState();

  /**
   * Get the current mode of the portlet.
   * 
   * @return The mode of the portlet
   */
  public String getMode();

  /**
   * Get the <code>ClientData</code> structure which carries information about
   * the end user agent.
   * 
   * @return The <code>ClientData</code> specifying the user agent.
   */
  public ClientData getClientData();

  /**
   * Get the locales which are supported by the portlet according to the client
   * connecting to it. The Locales returned are in the form of (ISO-639 + "-" +
   * ISO-3166).
   * 
   * @return Array with string representations of the locales which are
   *         supported by the consumer
   */
  public List<String> getLocales();

  /**
   * Get the list of wsrp modes which are supported by the portlet. This should
   * returned the list of all actuall supported modes and not necessarily the
   * modes returned in the portlet description of the producer.
   * 
   * @return Array with string representations of the portlet modes supported by
   *         the portlet or null
   */
  public List<String> getValidNewModes();

  /**
   * Get the list of wsrp window states which are supported by the portlet. This
   * should returned the list of all actuall supported window states and not
   * necessarily the window states returned in the portlet description of the
   * producer.
   * 
   * @return Array with string representations of the window states supported by
   *         the portlet or null
   */
  public List<String> getValidNewWindowStates();

  /**
   * Get an array of mime types which are supported by the end user device. The
   * order in the array defines the order of preference of the end user.
   * 
   * @return An array of mimes types the consumer supports or null
   */
  public List<String> getMimeTypes();

  /**
   * Get the character sets the consumer wants the remote portlet to use for
   * encoding the markup. Valid character sets are defined <a
   * href='http://www.iana.org/assignments/character-sets'>here</a>
   * 
   * @return Array of string representations of the character encoding.
   */
  public List<String> getMarkupCharacterSets();

  /**
   * Checks wether a given wsrp mode is supported by the portlet.
   * 
   * @param wsrpMode The wsrp mode
   * @return True if the mode is supported by the portlet, false otherwise
   */
  public boolean isModeSupported(String wsrpMode);

  /**
   * Checks wether a given wsrp window state is supported by the portlet.
   * 
   * @param wsrpWindowState The wsrp window state
   * @return True if the window state is supported by the portlet, false
   *         otherwise
   */
  public boolean isWindowStateSupported(String wsrpWindowState);

  public String getValidateTag();

  public boolean isSecureClientCommunication();

  public List<Extension> getExtensions();

  /*****************************************************************************
   * * * * * * * * * * * * * * * * * * * * * * * * * UNKNOWN * * * * * * * * * * * * * * * * * * * * * * * * * *
   * 
   * @author alexey
   */

  /**
   * Get the method which is used by the consumer to authenticate its users.
   * 
   * @return String indicating how end-users were authenticated by the consumer.
   */
  public String getUserAuthentication();

}
