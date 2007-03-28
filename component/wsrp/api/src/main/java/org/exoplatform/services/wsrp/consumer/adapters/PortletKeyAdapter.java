/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.consumer.adapters;

import org.exoplatform.services.wsrp.consumer.PortletKey;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 8 févr. 2004
 * Time: 22:56:54
 */

public class PortletKeyAdapter implements PortletKey{

  private String portletHandle;
  private String producerId;

  public String getPortletHandle() {
    return portletHandle;
  }

  public void setPortletHandle(String portletHandle) {
    this.portletHandle = portletHandle;
  }

  public String getProducerId() {
    return producerId;
  }

  public void setProducerId(String producerId) {
    this.producerId = producerId;
  }
  
}