/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
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