/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.consumer.adapters;

import org.exoplatform.services.wsrp.consumer.WSRPMarkupRequest;
import org.exoplatform.services.wsrp.type.MarkupContext;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 7 févr. 2004
 * Time: 17:46:06
 */

public class WSRPMarkupRequestAdapter extends WSRPBaseRequestAdapter
    implements WSRPMarkupRequest{

  private MarkupContext cachedMarkup;

  public MarkupContext getCachedMarkup() {
    return cachedMarkup;
  }

  public void setCachedMarkup(MarkupContext cachedMarkup) {
    this.cachedMarkup = cachedMarkup;
  }
}