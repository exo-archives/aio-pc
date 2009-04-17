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

import java.util.List;

import org.exoplatform.services.wsrp2.consumer.WSRPInteractionRequest;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.StateChange;
import org.exoplatform.services.wsrp2.type.UploadContext;

/**
 * @author  Mestrallet Benjamin.
 *          benjmestrallet@users.sourceforge.net
 * Date: 7 févr. 2004
 * Time: 16:26:36
 */

public class WSRPInteractionRequestAdapter extends WSRPBaseRequestAdapter implements
    WSRPInteractionRequest {

  /** 
   *  State change.
   */
  private StateChange     portletStateChange; //WSRP2

  /** 
   *  Interaction state.
   */
  private String          interactionState;

  /** 
   *  From parameters.
   */
  private List<NamedString>   formParameters;

  /** 
   *  Upload context.
   */
  private List<UploadContext> uploadContexts;    //WSRP2

  /**
   * Gets interaction state.
   * @return  interactionState
   */
  public String getInteractionState() {
    return interactionState;
  }

  /**
   * Sets interaction state.
   * @param  interactionState
   */
  public void setInteractionState(String interactionState) {
    this.interactionState = interactionState;
  }

  /**
   * Gets form parameters.
   * @return  formParameters
   */
  public List<NamedString> getFormParameters() {
    return formParameters;
  }

  /**
   * Sets form parameters.
   * @param  formParameters
   */
  public void setFormParameters(List<NamedString> formParameters) {
    this.formParameters = formParameters;
  }

  /**
   * Gets portlet state change.
   * @return  portletStateChange
   */
  public StateChange getPortletStateChange() {
    return portletStateChange;
  }

  /**
   * Sets portlet state change.
   * @param  portletStateChange
   */
  public void setPortletStateChange(StateChange portletStateChange) {
    this.portletStateChange = portletStateChange;
  }

  /**
   * Gets upload context.
   * @return  uploadContexts
   */
  public List<UploadContext> getUploadContexts() {
    return uploadContexts;
  }

  /**
   * Sets upload context.
   * @param  uploadContexts
   */
  public void setUploadContexts(List<UploadContext> uploadContexts) {
    this.uploadContexts = uploadContexts;
  }

}
