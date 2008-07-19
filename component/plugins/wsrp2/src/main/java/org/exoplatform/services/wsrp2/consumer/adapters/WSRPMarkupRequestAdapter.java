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

import org.exoplatform.services.wsrp2.consumer.WSRPMarkupRequest;
import org.exoplatform.services.wsrp2.type.MarkupContext;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 7 févr. 2004
 * Time: 17:46:06
 */

public class WSRPMarkupRequestAdapter extends WSRPBaseRequestAdapter implements WSRPMarkupRequest {

  private MarkupContext cachedMarkup;

  public MarkupContext getCachedMarkup() {
    return cachedMarkup;
  }

  public void setCachedMarkup(MarkupContext cachedMarkup) {
    this.cachedMarkup = cachedMarkup;
  }

}
