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

package org.exoplatform.services.wsrp2.exceptions;

import java.rmi.RemoteException;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class Exception2Fault {

  public static void handleException(WSRPException e) throws RemoteException {
    String fault = e.getFault();

//    AxisFault fault = new AxisFault();
//    QName qname = new QName(Fault.getTypeDesc().getXmlType().getNamespaceURI(), e.getFault());
//    fault.setFaultCode(qname);
//    fault.setFaultString(e.getMessage());
//
//    try {
//      Document doc = XMLUtils.newDocument();
//      Element element = doc.createElementNS(getNameSpace(e.getFault()), e.getFault());
//      fault.clearFaultDetails();
//      fault.setFaultDetail(new Element[] { element });
//
//    } catch (Exception ex) {
//      ex.printStackTrace();
//    }
    throw new RemoteException(fault, e);
  }

  public static String getNameSpace(String fault) {
//    if (Faults.ACCESS_DENIED_FAULT.equals(fault))
//      return AccessDeniedFault.getTypeDesc().getXmlType().getNamespaceURI();
//    else if (Faults.INCONSISTENT_PARAMETERS_FAULT.equals(fault))
//      return InconsistentParametersFault.getTypeDesc().getXmlType().getNamespaceURI();
//    else if (Faults.INVALID_COOKIE_FAULT.equals(fault))
//      return InvalidCookieFault.getTypeDesc().getXmlType().getNamespaceURI();
//    else if (Faults.INVALID_HANDLE_FAULT.equals(fault))
//      return InvalidHandleFault.getTypeDesc().getXmlType().getNamespaceURI();
//    else if (Faults.INVALID_REGISTRATION_FAULT.equals(fault))
//      return InvalidRegistrationFault.getTypeDesc().getXmlType().getNamespaceURI();
//    else if (Faults.INVALID_SESSION_FAULT.equals(fault))
//      return InvalidSessionFault.getTypeDesc().getXmlType().getNamespaceURI();
//    else if (Faults.INVALID_USER_CATEGORY_FAULT.equals(fault))
//      return InvalidUserCategoryFault.getTypeDesc().getXmlType().getNamespaceURI();
//    else if (Faults.MISSING_PARAMETERS_FAULT.equals(fault))
//      return MissingParametersFault.getTypeDesc().getXmlType().getNamespaceURI();
//    else if (Faults.OPERATION_FAILED_FAULT.equals(fault))
//      return OperationFailedFault.getTypeDesc().getXmlType().getNamespaceURI();
//    else if (Faults.PORTLET_STATE_CHANGE_REQUIRED_FAULT.equals(fault))
//      return PortletStateChangeRequiredFault.getTypeDesc().getXmlType().getNamespaceURI();
//    else if (Faults.UNSUPPORTED_LOCALE_FAULT.equals(fault))
//      return UnsupportedLocaleFault.getTypeDesc().getXmlType().getNamespaceURI();
//    else if (Faults.UNSUPPORTED_MIME_TYPE_FAULT.equals(fault))
//      return UnsupportedMimeTypeFault.getTypeDesc().getXmlType().getNamespaceURI();
//    else if (Faults.UNSUPPORTED_MODE_FAULT.equals(fault))
//      return UnsupportedModeFault.getTypeDesc().getXmlType().getNamespaceURI();
//    else if (Faults.UNSUPPORTED_WINDOW_STATE_FAULT.equals(fault))
//      return UnsupportedWindowStateFault.getTypeDesc().getXmlType().getNamespaceURI();

    return null;
  }

}
