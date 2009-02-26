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

package org.exoplatform.services.wsrp2.consumer.adapters;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.services.wsrp2.consumer.WSRPBaseRequest;
import org.exoplatform.services.wsrp2.type.ClientData;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.NamedString;

/**
 * @author  Mestrallet Benjamin.
 *          benjmestrallet@users.sourceforge.net
 * Date: 7 févr. 2004
 * Time: 16:25:12
 */

public class WSRPBaseRequestAdapter implements WSRPBaseRequest {

  /** 
   * Session ID.
   *         For RuntimeContext 
   */
  private String        sessionID;

  /** 
   *  Portlet instance key.
   */
  private String        portletInstanceKey;

  /** 
   *  Navigational state.
   */
  private String        navigationalState;

  /** 
   *  Navigational values.
   */
  private List<NamedString> navigationalValues;       //WSRP2

  /** 
   *  Secure client communication.
   */
  private boolean       secureClientCommunication; //WSRP2

  /** 
   *  Locales.
   */
  private List<String>      locales = new ArrayList<String>();

  /** 
   *  MIME types.
   */
  private List<String>      mimeTypes = new ArrayList<String>();

  /** 
   *  Portlet mode.
   */
  private String        mode;

  /** 
   *  Window state.
   */
  private String        windowState;

  /** 
   *  Client data.
   */
  private ClientData    clientData;

  /** 
   *  Markup character set. 
   */
  private List<String>      markupCharacterSets = new ArrayList<String>();

  /** 
   *  Validate tag.
   */
  private String        validateTag;              //WSRP2

  /** 
   *  Valid portlet modes. 
   */
  private List<String>      validNewModes = new ArrayList<String>();

  /** 
   *  Valid window states.
   */
  private List<String>      validNewWindowStates = new ArrayList<String>();

  /** 
   *  Extensions.
   */
  private List<Extension>   extensions;               //WSRP2

  /** 
   *  User authentication. 
   */
  private String        userAuthentication;

  /**
   * Gets session ID.
   * @return String sessionID
   */
  public String getSessionID() {
    return sessionID;
  }

  /**
   * Sets session ID.
   * @param  sessionID
   */
  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }

  /**
   * Gets portlet instance key.
   * @return String portletInstanceKey
   */
  public String getPortletInstanceKey() {
    return portletInstanceKey;
  }

  /**
   * Sets portlet instance key.
   * @param  portletInstanceKey
   */
  public void setPortletInstanceKey(String portletInstanceKey) {
    this.portletInstanceKey = portletInstanceKey;
  }

  /**
   * Gets navigational state.
   * @return String navigationalState
   */
  public String getNavigationalState() {
    return navigationalState;
  }

  /**
   * Sets navigational state.
   * @param  navigationalState
   */
  public void setNavigationalState(String navigationalState) {
    this.navigationalState = navigationalState;
  }

  /**
   * Gets window state.
   * @return String windowState
   */
  public String getWindowState() {
    return windowState;
  }

  /**
   * Sets window state.
   * @param  windowState
   */
  public void setWindowState(String windowState) {
    this.windowState = windowState;
  }

  /**
   * Gets portlet mode.
   * @return String mode
   */
  public String getMode() {
    return mode;
  }

  /**
   * Sets portlet mode.
   * @param mode
   */
  public void setMode(String mode) {
    this.mode = mode;
  }

  /**
   * Gets client data.
   * @return ClientData
   */
  public ClientData getClientData() {
    return clientData;
  }

  /**
   * Sets client data.
   * @param clientData
   */
  public void setClientData(ClientData clientData) {
    this.clientData = clientData;
  }

  /**
   * Gets locales.
   * @return locales
   */
  public List<String> getLocales() {
    return locales;
  }

  /**
   * Sets locales.
   * @param locales
   */
  public void setLocales(List<String> locales) {
    this.locales = locales;
  }

  /**
   * Gets new portlet modes.
   * @return validNewModes
   */
  public List<String> getValidNewModes() {
    return validNewModes;
  }

  /**
   * Sets new portlet modes.
   * @param validNewModes
   */
  public void setValidNewModes(List<String> validNewModes) {
    this.validNewModes = validNewModes;
  }

  /**
   * Gets new window states.
   * @return validNewWindowStates
   */
  public List<String> getValidNewWindowStates() {
    return validNewWindowStates;
  }

  /**
   * Sets new window states.
   * @param validNewWindowStates
   */
  public void setValidNewWindowStates(List<String> validNewWindowStates) {
    this.validNewWindowStates = validNewWindowStates;
  }

  /**
   * Gets new MIME types.
   * @return mimeTypes
   */
  public List<String> getMimeTypes() {
    return mimeTypes;
  }

  /**
   * Sets new MIME types.
   * @param mimeTypes
   */
  public void setMimeTypes(List<String> mimeTypes) {
    this.mimeTypes = mimeTypes;
  }

  /**
   * Gets markup character set.
   * @return markupCharacterSets
   */
  public List<String> getMarkupCharacterSets() {
    return markupCharacterSets;
  }

  /**
   * Sets markup character set.
   * @param markupCharacterSets
   */
  public void setMarkupCharacterSets(List<String> markupCharacterSets) {
    this.markupCharacterSets = markupCharacterSets;
  }

  /**
   * Gets is mode supported.
   * @param wsrpMode
   * @return isModeSupported
   */
  public boolean isModeSupported(String wsrpMode) {
    if (wsrpMode == null) {
      throw new IllegalArgumentException("mode must not be null");
    }
    List<String> mods = getValidNewModes();
    return mods.contains(wsrpMode);
  }

  /**
   * Gets is window state supported.
   * @param wsrpWindowState
   * @return isWindowStateSupported
   */
  public boolean isWindowStateSupported(String wsrpWindowState) {
    if (wsrpWindowState == null) {
      throw new IllegalArgumentException("window state must not be null");
    }
    List<String> stats = getValidNewWindowStates();
    return stats.contains(wsrpWindowState);
  }

  /**
   * Gets navigational values.
   * @return navigationalValues
   */
  public List<NamedString> getNavigationalValues() {
    return navigationalValues;
  }

  /**
   * Sets navigational values.
   * @param navigationalValues
   */
  public void setNavigationalValues(List<NamedString> navigationalValues) {
    this.navigationalValues = navigationalValues;
  }

  /**
   * Gets validate tag.
   * @return validateTag
   */
  public String getValidateTag() {
    return validateTag;
  }

  /**
   * Sets validate tag.
   * @param validateTag
   */
  public void setValidateTag(String validateTag) {
    this.validateTag = validateTag;
  }

  /**
   * Gets extensions.
   * @return extensions
   */
  public List<Extension> getExtensions() {
    return extensions;
  }

  /**
   * Sets extensions.
   * @param extensions
   */
  public void setExtensions(List<Extension> extensions) {
    this.extensions = extensions;
  }

  /**
   * Gets is secure client communication.
   * @return secureClientCommunication
   */
  public boolean isSecureClientCommunication() {
    return secureClientCommunication;
  }

  /**
   * Sets is secure client communication.
   * @param secureClientCommunication
   */
  public void setSecureClientCommunication(boolean secureClientCommunication) {
    this.secureClientCommunication = secureClientCommunication;
  }

  /**
   * Gets user authentication.
   * @return userAuthentication
   */
  public String getUserAuthentication() {
    return userAuthentication;
  }

  /**
   * Sets user authentication.
   * @param userAuthentication
   */
  public void setUserAuthentication(String userAuthentication) {
    this.userAuthentication = userAuthentication;
  }

}
