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

package org.exoplatform.services.wsrp.consumer.adapters;

import org.exoplatform.services.wsrp.consumer.InteractionRequest;
import org.exoplatform.services.wsrp.type.NamedString;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 7 févr. 2004
 * Time: 16:26:36
 */

public class InteractionRequestAdapter extends WSRPBaseRequestAdapter
    implements InteractionRequest{

  private String interactionState;
  private NamedString[] formParameters;

  public String getInteractionState() {
    return interactionState;
  }

  public void setInteractionState(String interactionState) {
    this.interactionState = interactionState;
  }

  public NamedString[] getFormParameters() {
    return formParameters;
  }

  public void setFormParameters(NamedString[] formParameters) {
    this.formParameters = formParameters;
  }


}