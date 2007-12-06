/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.consumer.adapters;

import org.exoplatform.services.wsrp.consumer.WSRPBaseRequest;
import org.exoplatform.services.wsrp.type.ClientData;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 7 févr. 2004
 * Time: 16:25:12
 */

public class WSRPBaseRequestAdapter implements WSRPBaseRequest{

  private String sessionID;
  private String portletInstanceKey;
  private String navigationalState;
  private String windowState;
  private String mode;
  private ClientData clientData;
  private String[] locales;
  private String[] modes;
  private String[] windowStates;
  private String[] mimeTypes;
  private String[] characterEncodingSet;
  private String userAuthentication;

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

  public String[] getModes() {
    return modes;
  }

  public void setModes(String[] modes) {
    this.modes = modes;
  }

  public String[] getWindowStates() {
    return windowStates;
  }

  public void setWindowStates(String[] windowStates) {
    this.windowStates = windowStates;
  }

  public String[] getMimeTypes() {
    return mimeTypes;
  }

  public void setMimeTypes(String[] mimeTypes) {
    this.mimeTypes = mimeTypes;
  }

  public String[] getCharacterEncodingSet() {
    return characterEncodingSet;
  }

  public void setCharacterEncodingSet(String[] characterEncodingSet) {
    this.characterEncodingSet = characterEncodingSet;
  }

  public String getUserAuthentication() {
    return userAuthentication;
  }

  public void setUserAuthentication(String userAuthentication) {
    this.userAuthentication = userAuthentication;
  }

  public boolean isModeSupported(String wsrpMode) {
    if (wsrpMode == null) {
      throw new IllegalArgumentException("mode must not be null");
    }
    String[] mods = getModes();
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
    String[] stats = getWindowStates();
    for (int i = 0; i < stats.length; i++) {
      if (wsrpWindowState.equalsIgnoreCase(stats[i])) {
        return true;
      }
    }
    return false;
  }
  


}