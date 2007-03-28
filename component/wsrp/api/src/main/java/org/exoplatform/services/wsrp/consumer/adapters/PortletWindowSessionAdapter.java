/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.consumer.adapters;

import org.exoplatform.services.wsrp.consumer.PortletSession;
import org.exoplatform.services.wsrp.consumer.PortletWindowSession;
import org.exoplatform.services.wsrp.type.MarkupContext;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 9 févr. 2004
 * Time: 15:49:27
 */

public class PortletWindowSessionAdapter implements PortletWindowSession{

  private String windowID;
  private PortletSession portletSession;
  private MarkupContext markupContext;
  private String navigationalState;

  public String getWindowID() {
    return windowID;
  }

  public void setWindowID(String windowID) {
    this.windowID = windowID;
  }

  public MarkupContext getCachedMarkup() {
    return markupContext;
  }

  public void updateMarkupCache(MarkupContext markupContext) {
    this.markupContext = markupContext;
  }

  public PortletSession getPortletSession() {
    return portletSession;
  }

  public void setPortletSession(PortletSession portletSession) {
    this.portletSession = portletSession;
  }

  public MarkupContext getMarkupContext() {
    return markupContext;
  }

  public void setMarkupContext(MarkupContext markupContext) {
    this.markupContext = markupContext;
  }

  public String getNavigationalState() {
    return navigationalState;
  }

  public void setNavigationalState(String navigationalState) {
    this.navigationalState = navigationalState;
  }


}