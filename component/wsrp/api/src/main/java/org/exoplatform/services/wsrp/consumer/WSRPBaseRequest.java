package org.exoplatform.services.wsrp.consumer;

import org.exoplatform.services.wsrp.type.ClientData;

/**
 * The <code>WSRPRequest</code> is the base interface for all
 * requests to a consumer side invocation of a integrated remote portlet.
 * Specialized interfaces exist for markup and action calls.
 *
 * @author Benjamin Mestrallet
 *
 * @see WSRPMarkupRequest
 * @see InteractionRequest
 */
public interface WSRPBaseRequest {
  /**
   * Get the ID of the session context
   *
   * @return The session context
   */
  public String getSessionID();

  /**
   * Get an opaque string which corresponds to a unique reference to this use of the portlet.
   *
   * @return The portlet instance key
   */
  public String getPortletInstanceKey();

  /**
   * Get the current navigational state of the portlet
   *
   * @return The navigational state
   */
  public String getNavigationalState();

  /**
   * Get the current window state of the portlet
   *
   * @return The window state
   */
  public String getWindowState();

  /**
   * Get the current mode of the portlet
   *
   * @return The mode of the portlet
   */
  public String getMode();

  /**
   * Get the <code>ClientData</code> structure which carries
   * information about the end user agent.
   *
   * @return The <code>ClientData</code> specifying the user agent.
   */
  public ClientData getClientData();

  /**
   * Get the locales which are supported by the portlet according to the client connecting to it.
   * The Locales returned are in the form of (ISO-639 + "-" + ISO-3166)
   *
   * @return Array with string representations of the locales which are
   *         supported by the consumer
   */
  public String[] getLocales();

  /**
   * Get the list of wsrp modes which are supported by the portlet.
   * This should returned the list of all actuall supported modes and
   * not necessarily the modes returned in the portlet description of the producer.
   *
   * @return Array with string representations of the portlet modes
   *         supported by the portlet or null
   */
  public String[] getModes();

  /**
   * Get the list of wsrp window states which are supported by the portlet.
   * This should returned the list of all actuall supported window states and
   * not necessarily the window states returned in the portlet description of the producer.
   *
   * @return Array with string representations of the window states
   *         supported by the portlet or null
   */
  public String[] getWindowStates();

  /**
   * Get an array of mime types which are supported by the end user device.
   * The order in the array defines the order of preference of the end user.
   *
   * @return An array of mimes types the consumer supports or null
   */
  public String[] getMimeTypes();

  /**
   * Get the character sets the consumer wants the remote portlet to use for encoding the markup.
   * Valid character sets are defined <a href='http://www.iana.org/assignments/character-sets'>here</a>
   *
   * @return Array of string representations of the character encoding.
   */
  public String[] getCharacterEncodingSet();

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
   * @return True if the window state is supported by the portlet, false otherwise
   */
  public boolean isWindowStateSupported(String wsrpWindowState);

  /**
   * Get the method which is used by the consumer to authenticate its users.
   *
   * @return String indicating how end-users were authenticated by the consumer.
   */
  public String getUserAuthentication();
}
