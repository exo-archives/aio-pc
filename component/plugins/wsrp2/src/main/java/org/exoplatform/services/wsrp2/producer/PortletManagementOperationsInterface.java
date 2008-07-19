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

package org.exoplatform.services.wsrp2.producer;

import java.rmi.RemoteException;

import org.exoplatform.services.wsrp2.type.CopyPortletsResponse;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.ExportPortletsResponse;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.type.ImportPortlet;
import org.exoplatform.services.wsrp2.type.ImportPortletsResponse;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.SetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public interface PortletManagementOperationsInterface {

  public PortletDescriptionResponse getPortletDescription(RegistrationContext registrationContext,
                                                          PortletContext portletContext,
                                                          UserContext userContext,
                                                          String[] desiredLocales) throws RemoteException;

  public DestroyPortletsResponse destroyPortlets(RegistrationContext registrationContext,
                                                 String[] portletHandles,
                                                 UserContext userContext) throws RemoteException;

  public DestroyPortletsResponse destroyPortlets(RegistrationContext registrationContext,
                                                 String[] portletHandles)

  throws RemoteException;

  public PortletContext setPortletProperties(RegistrationContext registrationContext,
                                             PortletContext portletContext,
                                             UserContext userContext,
                                             PropertyList propertyList) throws RemoteException;

  public PropertyList getPortletProperties(RegistrationContext registrationContext,
                                           PortletContext portletContext,
                                           UserContext userContext,
                                           String[] names) throws RemoteException;

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(RegistrationContext registrationContext,
                                                                          PortletContext portletContext,
                                                                          UserContext userContext,
                                                                          String[] desiredLocales) throws RemoteException;

  public PortletContext clonePortlet(RegistrationContext registrationContext,
                                     PortletContext portletContext,
                                     UserContext userContext,
                                     Lifetime lifetime) throws RemoteException;

  public PortletContext clonePortlet(RegistrationContext registrationContext,
                                     PortletContext portletContext,
                                     UserContext userContext) throws RemoteException;

  public GetPortletsLifetimeResponse getPortletsLifetime(RegistrationContext registrationContext,
                                                         PortletContext[] portletContext,
                                                         UserContext userContext) throws RemoteException;

  public SetPortletsLifetimeResponse setPortletsLifetime(RegistrationContext registrationContext,
                                                         PortletContext[] portletContext,
                                                         UserContext userContext,
                                                         Lifetime lifetime) throws RemoteException;

  public CopyPortletsResponse copyPortlets(RegistrationContext toRegistrationContext,
                                           UserContext toUserContext,
                                           RegistrationContext fromRegistrationContext,
                                           UserContext UserContext,
                                           PortletContext[] fromPortletContexts,
                                           Lifetime lifetime) throws RemoteException;

  public ExportPortletsResponse exportPortlets(RegistrationContext registrationContext,
                                               PortletContext[] portletContext,
                                               UserContext userContext,
                                               Lifetime lifetime,
                                               boolean exportByValueRequired) throws RemoteException;

  public ImportPortletsResponse importPortlets(RegistrationContext registrationContext,
                                               byte[] importContext,
                                               ImportPortlet[] importPortlet,
                                               UserContext userContext,
                                               Lifetime lifetime) throws RemoteException;

  public ReturnAny releaseExport(byte[] exportContext, UserContext userContext);

  public Lifetime setExportLifetime(RegistrationContext registrationContext,
                                    byte[] exportContext,
                                    UserContext userContext,
                                    Lifetime lifetime) throws RemoteException;

}
