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

package org.exoplatform.services.wsrp1.producer;

import org.exoplatform.services.wsrp1.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp1.type.Extension;
import org.exoplatform.services.wsrp1.type.InteractionParams;
import org.exoplatform.services.wsrp1.type.MarkupParams;
import org.exoplatform.services.wsrp1.type.MarkupResponse;
import org.exoplatform.services.wsrp1.type.PortletContext;
import org.exoplatform.services.wsrp1.type.RegistrationContext;
import org.exoplatform.services.wsrp1.type.RuntimeContext;
import org.exoplatform.services.wsrp1.type.UserContext;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public interface MarkupOperationsInterface {
  public MarkupResponse getMarkup(RegistrationContext registrationContext,
                                  PortletContext portletContext,
                                  RuntimeContext runtimeContext,
                                  UserContext userContext,
                                  MarkupParams markupParams) throws java.rmi.RemoteException;

  public BlockingInteractionResponse performBlockingInteraction(RegistrationContext registrationContext,
                                                                PortletContext portletContext,
                                                                RuntimeContext runtimeContext,
                                                                UserContext userContext,
                                                                MarkupParams markupParams,
                                                                InteractionParams interactionParams) throws java.rmi.RemoteException;

  public Extension initCookie(RegistrationContext registrationContext) throws java.rmi.RemoteException;

  public Extension releaseSessions(RegistrationContext registrationContext, String[] sessionIDs) throws java.rmi.RemoteException;
}
