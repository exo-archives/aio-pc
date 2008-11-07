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

package org.exoplatform.services.wsrp2.consumer;

import java.util.List;

import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.ResourceContext;
import org.exoplatform.services.wsrp2.type.StateChange;
import org.exoplatform.services.wsrp2.type.UploadContext;

/**
 * Author : Alexey Zavizionov alexey.zavizionov@exoplatform.com.ua 10.09.2007
 */
public interface WSRPResourceRequest extends WSRPBaseRequest {

  public ResourceContext getCachedResource();

  public String getResourceState();

  public List<NamedString> getFormParameters();

  public String getResourceID();

  public List<UploadContext> getUploadContexts();

  public StateChange getPortletStateChange();

  public java.lang.String getResourceCacheability();

}
