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

  private List<NamedString> navigationalValues;       //WSRP2

  // For MimeRequest (There are MarkupParams and ResourceParams which extends MimeRequest)
  private boolean       secureClientCommunication; //WSRP2

  private List<String>      locales = new ArrayList<String>();

  private List<String>      mimeTypes = new ArrayList<String>();

  private String        mode;

  private String        windowState;

  private ClientData    clientData;

  private List<String>      markupCharacterSets = new ArrayList<String>();

  private String        validateTag;              //WSRP2

  private List<String>      validNewModes = new ArrayList<String>();

  private List<String>      validNewWindowStates = new ArrayList<String>();

  private List<Extension>   extensions;               //WSRP2

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

  public List<String> getLocales() {
    return locales;
  }

  public void setLocales(List<String> locales) {
    this.locales = locales;
  }

  public List<String> getValidNewModes() {
    return validNewModes;
  }

  public void setValidNewModes(List<String> validNewModes) {
    this.validNewModes = validNewModes;
  }

  public List<String> getValidNewWindowStates() {
    return validNewWindowStates;
  }

  public void setValidNewWindowStates(List<String> validNewWindowStates) {
    this.validNewWindowStates = validNewWindowStates;
  }

  public List<String> getMimeTypes() {
    return mimeTypes;
  }

  public void setMimeTypes(List<String> mimeTypes) {
    this.mimeTypes = mimeTypes;
  }

  public List<String> getMarkupCharacterSets() {
    return markupCharacterSets;
  }

  public void setMarkupCharacterSets(List<String> markupCharacterSets) {
    this.markupCharacterSets = markupCharacterSets;
  }

  public boolean isModeSupported(String wsrpMode) {
    if (wsrpMode == null) {
      throw new IllegalArgumentException("mode must not be null");
    }
    List<String> mods = getValidNewModes();
    return mods.contains(wsrpMode);
  }

  public boolean isWindowStateSupported(String wsrpWindowState) {
    if (wsrpWindowState == null) {
      throw new IllegalArgumentException("window state must not be null");
    }
    List<String> stats = getValidNewWindowStates();
    return stats.contains(wsrpWindowState);
  }

  // WSRP2

  public List<NamedString> getNavigationalValues() {
    return navigationalValues;
  }

  public void setNavigationalValues(List<NamedString> navigationalValues) {
    this.navigationalValues = navigationalValues;
  }

  public String getValidateTag() {
    return validateTag;
  }

  public void setValidateTag(String validateTag) {
    this.validateTag = validateTag;
  }

  public List<Extension> getExtensions() {
    return extensions;
  }

  public void setExtensions(List<Extension> extensions) {
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
