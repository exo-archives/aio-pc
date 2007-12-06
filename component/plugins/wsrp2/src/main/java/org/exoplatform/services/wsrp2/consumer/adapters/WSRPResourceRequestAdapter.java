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

import org.exoplatform.services.wsrp2.consumer.WSRPResourceRequest;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.ResourceContext;
import org.exoplatform.services.wsrp2.type.StateChange;
import org.exoplatform.services.wsrp2.type.UploadContext;

/**
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 * 10.09.2007  
 */
public class WSRPResourceRequestAdapter extends WSRPBaseRequestAdapter implements WSRPResourceRequest {


  private NamedString[] formParameters;
  private UploadContext[] uploadContexts; //WSRP2
  private String resourceID;
  private StateChange portletStateChange; //WSRP2
  private String resourceState;
  private String resourceCacheability; //WSRP2
  
  private ResourceContext cachedResource;
  
  public ResourceContext getCachedResource() {
    return cachedResource;
  }

  public void setCachedResource(ResourceContext cachedResource) {
    this.cachedResource = cachedResource;
  }

  public String getResourceState() {
    return resourceState;
  }

  public void setResourceState(String resourceState) {
    this.resourceState = resourceState;
  }

  public NamedString[] getFormParameters() {
    return formParameters;
  }

  public void setFormParameters(NamedString[] formParameters) {
    this.formParameters = formParameters;
  }
  
  public String getResourceID() {
    return resourceID;
  }
  
  public void setResourceID(String resourceID) {
    this.resourceID = resourceID;
  }
  
  // WSRP2
  
  public UploadContext[] getUploadContexts() {
    return uploadContexts;
  }
  
  public void setUploadContexts(UploadContext[] uploadContexts) {
    this.uploadContexts = uploadContexts;
  }

  public StateChange getPortletStateChange() {
    return portletStateChange;
  }

  public void setPortletStateChange(StateChange portletStateChange) {
    this.portletStateChange = portletStateChange;
  }
  
  public String getResourceCacheability() {
    return resourceCacheability;
  }
  
  public void setResourceCacheability(String resourceCacheability) {
    this.resourceCacheability = resourceCacheability;
  }
  
}
