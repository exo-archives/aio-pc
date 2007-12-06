/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 30, 2003
 * Time: 9:13:06 PM
 */
package org.exoplatform.services.portletcontainer.pci;

public class ActionInput extends Input {
  private boolean stateChangeAuthorized;

  public boolean isStateChangeAuthorized() {
    return stateChangeAuthorized;
  }

  public void setStateChangeAuthorized(boolean stateChangeAuthorized) {
    this.stateChangeAuthorized = stateChangeAuthorized;
  }

}
