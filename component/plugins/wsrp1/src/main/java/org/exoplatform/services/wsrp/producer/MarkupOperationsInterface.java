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

package org.exoplatform.services.wsrp.producer;

import org.exoplatform.services.wsrp.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp.type.InteractionParams;
import org.exoplatform.services.wsrp.type.MarkupParams;
import org.exoplatform.services.wsrp.type.MarkupResponse;
import org.exoplatform.services.wsrp.type.PortletContext;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.ReturnAny;
import org.exoplatform.services.wsrp.type.RuntimeContext;
import org.exoplatform.services.wsrp.type.UserContext;

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

  public ReturnAny initCookie(RegistrationContext registrationContext) throws java.rmi.RemoteException;

  public ReturnAny releaseSessions(RegistrationContext registrationContext, String[] sessionIDs) throws java.rmi.RemoteException;
}
