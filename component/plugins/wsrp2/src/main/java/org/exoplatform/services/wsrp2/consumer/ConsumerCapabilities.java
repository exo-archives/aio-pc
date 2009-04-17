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

import org.exoplatform.services.wsrp2.type.StateChange;

/**
 * The consumer capababilities provides access to consumer related information.
 * 
 * @author <a href='mailto:peter.fischer@de.ibm.com'>Peter Fischer</a>
 * @author Benjamin Mestrallet
 */
public interface ConsumerCapabilities {

  /**
   * Get the name of the consumer
   * 
   * @return The name of the consumer
   */
  public String getConsumerAgent();

  /**
   * Get the method which is used by the consumer to authenticate its users.
   * 
   * @return String indicating how end-users were authenticated by the consumer.
   */
  public String getUserAuthentication();

  /**
   * Get the locales which are supported by the consumer. (ISO-639 + "_" +
   * ISO-3166)
   * 
   * @return Array with string representations of the locales which are
   *         supported by the consumer
   */
  public List<String> getSupportedLocales();

  /**
   * Get the portlet modes the consumer is willing to manage.
   * 
   * @return Array with string representations of the portlet modes which are
   *         supported by the consumer
   */
  public List<String> getSupportedModes();

  /**
   * Get the window states the consumer is willing to manage.
   * 
   * @return Array with string representations of the window states which are
   *         supported by the consumer
   */
  public List<String> getSupportedWindowStates();

  /**
   * Returns a flag which is used to indicate the producer wether or not the
   * processing of portlets is allowed to modify the portlet state.
   * 
   * @return A flag
   */
  public StateChange getPortletStateChange();

  /**
   * Get the character sets the consumer wants the remote portlet to use for
   * encoding the markup. Valid character sets are defined <a
   * href='http://www.iana.org/assignments/character-sets'>here</a>
   * 
   * @return Array of string representations of the character encoding.
   */
  public List<String> getCharacterEncodingSet();

  /**
   * Get an array of mime types which are supported by the consumer. The order
   * in the array defines the order of preference of the consumer.
   * 
   * @return An array of mimes types the consumer supports.
   */
  public List<String> getMimeTypes();

  /**
   * Set the name of the consumer
   * 
   * @param name The new name of the consumer
   */
  public void setConsumerAgent(String name);

  /**
   * Set the method of end user authentication used by the consumer..
   */
  public void setUserAuthentication(String authMethod);

  /**
   * Set the mime types the consumer supports The order in the array defines the
   * order of preference of the consumer.
   */
  public void setMimeTypes(List<String> mimeTypes);

  /**
   * Set the locales which are supported by the consumer. Pattern: ISO-639 + "_" +
   * ISO-3166
   * 
   * @param locales Array of string representations of supported locales
   */
  public void setSupportedLocales(List<String> locales);

  /**
   * Set the portlet modes which are supported by the consumer.
   * 
   * @param modes Array of string representations of portlet modes
   */
  public void setSupportedModes(List<String> modes);

  /**
   * Set the window states which are supported by the consumer.
   */
  public void setSupportedWindowStates(List<String> states);

  /**
   * Set a flag which is used to indicate the producer wether or not the
   * processing of portlets is allowed to modify the portlet state.
   * 
   * @param portletStateChange A flag with one of the following values:
   *          StateChange.OK, StateChange.Clone, StateChange.Fault
   */
  public void setPortletStateChange(StateChange portletStateChange);

  /**
   * Set the character set the consumer wants the remote portlet to use for
   * encoding the markup. Valid character sets are defined <a
   * href='http://www.iana.org/assignments/character-sets'>here</a>
   * 
   * @param charEncoding Array of string representations of the character
   *          encoding.
   */
  public void setCharacterEncodingSet(List<String> charEncoding);

}
