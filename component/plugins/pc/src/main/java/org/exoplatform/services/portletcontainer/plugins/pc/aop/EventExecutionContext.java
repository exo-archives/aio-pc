/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.plugins.pc.aop;

import javax.portlet.Portlet;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import org.exoplatform.container.component.ExecutionContext;

/**
 * Created by The eXo Platform SAS Author : Roman Pedchenko
 * roman.pedchenko@exoplatform.com.ua
 */
public class EventExecutionContext extends ExecutionContext {
  EventRequest  request_;

  EventResponse response_;

  Portlet       portlet_;

  public EventExecutionContext(Portlet p,
                               EventRequest req,
                               EventResponse res) {
    portlet_ = p;
    request_ = req;
    response_ = res;
  }
}
