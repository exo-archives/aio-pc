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

import org.exoplatform.services.wsrp2.consumer.WSRPResourceRequest;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.ResourceContext;
import org.exoplatform.services.wsrp2.type.StateChange;
import org.exoplatform.services.wsrp2.type.UploadContext;

/**
 * Author : Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua 10.09.2007
 */
public class WSRPResourceRequestAdapter extends WSRPBaseRequestAdapter implements
    WSRPResourceRequest {

  /** 
   *  Form parameters.
   */
  private List<NamedString>   formParameters;

  /** 
   *  Upload contexts.
   */
  private List<UploadContext> uploadContexts;      //WSRP2

  /** 
   *  Resource ID.
   */
  private String          resourceID;

  /** 
   *  Portlet state change.
   */
  private StateChange     portletStateChange;  //WSRP2

  /** 
   *  Resource state.
   */
  private String          resourceState;

  /** 
   *  Resource cacheability.
   */
  private String          resourceCacheability; //WSRP2

  /** 
   *  cached resource.
   */
  private ResourceContext cachedResource;

  
  /**
   * Gets chached resource.
   * @return  cachedResource
   */
  public ResourceContext getCachedResource() {
    return cachedResource;
  }

  /**
   * Sets chached resource.
   * @param  cachedResource
   */
  public void setCachedResource(ResourceContext cachedResource) {
    this.cachedResource = cachedResource;
  }

  /**
   * Gets resource state.
   * @return  resourceState
   */
  public String getResourceState() {
    return resourceState;
  }

  /**
   * Sets resource state.
   * @param  resourceState
   */
  public void setResourceState(String resourceState) {
    this.resourceState = resourceState;
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
   * Gets resource ID.
   * @return  resourceID
   */
  public String getResourceID() {
    return resourceID;
  }

  /**
   * Sets resource ID.
   * @param  resourceID
   */
  public void setResourceID(String resourceID) {
    this.resourceID = resourceID;
  }

  // WSRP2

  /**
   * Gets upload contexts.
   * @return  uploadContexts
   */
  public List<UploadContext> getUploadContexts() {
    return uploadContexts;
  }

  /**
   * Sets upload contexts.
   * @param  uploadContexts
   */
  public void setUploadContexts(List<UploadContext> uploadContexts) {
    this.uploadContexts = uploadContexts;
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
   * Gets resource cacheability.
   * @return  resourceCacheability
   */
  public String getResourceCacheability() {
    return resourceCacheability;
  }

  /**
   * Sets resource cacheability.
   * @param  resourceCacheability
   */
  public void setResourceCacheability(String resourceCacheability) {
    this.resourceCacheability = resourceCacheability;
  }

}
