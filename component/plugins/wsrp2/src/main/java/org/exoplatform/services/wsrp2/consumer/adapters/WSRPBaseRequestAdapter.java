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

package org.exoplatform.services.wsrp2.consumer.adapters;

import org.exoplatform.services.wsrp2.consumer.WSRPBaseRequest;
import org.exoplatform.services.wsrp2.type.ClientData;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.NamedString;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 7 févr. 2004
 * Time: 16:25:12
 */

public class WSRPBaseRequestAdapter implements WSRPBaseRequest {

  // For RuntimeContext
  private String        sessionID;

  private String        portletInstanceKey;

  // For NavigationalContext
  private String        navigationalState;

  private NamedString[] navigationalValues;       //WSRP2

  // For MimeRequest (There are MarkupParams and ResourceParams which extends MimeRequest)
  private boolean       secureClientCommunication; //WSRP2

  private String[]      locales;

  private String[]      mimeTypes;

  private String        mode;

  private String        windowState;

  private ClientData    clientData;

  private String[]      markupCharacterSets;      //modified

  private String        validateTag;              //WSRP2

  private String[]      validNewModes;            //modified

  private String[]      validNewWindowStates;     //modified

  private Extension[]   extensions;               //WSRP2

  // UNKNOWN
  private String        userAuthentication;

  public String getSessionID() {
    return sessionID;
  }

  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }

  public String getPortletInstanceKey() {
    return portletInstanceKey;
  }

  public void setPortletInstanceKey(String portletInstanceKey) {
    this.portletInstanceKey = portletInstanceKey;
  }

  public String getNavigationalState() {
    return navigationalState;
  }

  public void setNavigationalState(String navigationalState) {
    this.navigationalState = navigationalState;
  }

  public String getWindowState() {
    return windowState;
  }

  public void setWindowState(String windowState) {
    this.windowState = windowState;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public ClientData getClientData() {
    return clientData;
  }

  public void setClientData(ClientData clientData) {
    this.clientData = clientData;
  }

  public String[] getLocales() {
    return locales;
  }

  public void setLocales(String[] locales) {
    this.locales = locales;
  }

  public String[] getValidNewModes() {
    return validNewModes;
  }

  public void setValidNewModes(String[] validNewModes) {
    this.validNewModes = validNewModes;
  }

  public String[] getValidNewWindowStates() {
    return validNewWindowStates;
  }

  public void setValidNewWindowStates(String[] validNewWindowStates) {
    this.validNewWindowStates = validNewWindowStates;
  }

  public String[] getMimeTypes() {
    return mimeTypes;
  }

  public void setMimeTypes(String[] mimeTypes) {
    this.mimeTypes = mimeTypes;
  }

  public String[] getMarkupCharacterSets() {
    return markupCharacterSets;
  }

  public void setMarkupCharacterSets(String[] markupCharacterSets) {
    this.markupCharacterSets = markupCharacterSets;
  }

  public boolean isModeSupported(String wsrpMode) {
    if (wsrpMode == null) {
      throw new IllegalArgumentException("mode must not be null");
    }
    String[] mods = getValidNewModes();
    for (int i = 0; i < mods.length; i++) {
      if (wsrpMode.equalsIgnoreCase(mods[i])) {
        return true;
      }
    }
    return false;
  }

  public boolean isWindowStateSupported(String wsrpWindowState) {
    if (wsrpWindowState == null) {
      throw new IllegalArgumentException("window state must not be null");
    }
    String[] stats = getValidNewWindowStates();
    for (int i = 0; i < stats.length; i++) {
      if (wsrpWindowState.equalsIgnoreCase(stats[i])) {
        return true;
      }
    }
    return false;
  }

  // WSRP2

  public NamedString[] getNavigationalValues() {
    return navigationalValues;
  }

  public void setNavigationalValues(NamedString[] navigationalValues) {
    this.navigationalValues = navigationalValues;
  }

  public String getValidateTag() {
    return validateTag;
  }

  public void setValidateTag(String validateTag) {
    this.validateTag = validateTag;
  }

  public Extension[] getExtensions() {
    return extensions;
  }

  public void setExtensions(Extension[] extensions) {
    this.extensions = extensions;
  }

  public boolean isSecureClientCommunication() {
    return secureClientCommunication;
  }

  public void setSecureClientCommunication(boolean secureClientCommunication) {
    this.secureClientCommunication = secureClientCommunication;
  }

  // UNKNOWN
  public String getUserAuthentication() {
    return userAuthentication;
  }

  public void setUserAuthentication(String userAuthentication) {
    this.userAuthentication = userAuthentication;
  }

}
