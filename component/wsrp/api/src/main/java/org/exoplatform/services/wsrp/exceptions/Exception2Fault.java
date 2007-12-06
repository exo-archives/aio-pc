/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 14 janv. 2004
 */
package org.exoplatform.services.wsrp.exceptions;

import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.apache.axis.utils.XMLUtils;
import org.exoplatform.services.wsrp.type.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class Exception2Fault {
  
  public static void handleException(WSRPException e) throws RemoteException{    
    AxisFault fault = new AxisFault();
    QName qname = new QName(Fault.getTypeDesc().getXmlType().getNamespaceURI(),
                            e.getFault());
    fault.setFaultCode(qname);
    fault.setFaultString(e.getMessage());

    try {
      Document doc = XMLUtils.newDocument();
      Element element = doc.createElementNS(getNameSpace(e.getFault()), e.getFault());
      fault.clearFaultDetails();
      fault.setFaultDetail(new Element[] { element });

    } catch (Exception ex){
      ex.printStackTrace();
    }
    throw fault;       
  }
  
  public static String getNameSpace(String fault){
    if(Faults.ACCESS_DENIED_FAULT.equals(fault))
      return AccessDeniedFault.getTypeDesc().getXmlType().getNamespaceURI();
    else if(Faults.INCONSISTENT_PARAMETERS_FAULT.equals(fault))
      return InconsistentParametersFault.getTypeDesc().getXmlType().getNamespaceURI();
    else if(Faults.INVALID_COOKIE_FAULT.equals(fault))
      return InvalidCookieFault.getTypeDesc().getXmlType().getNamespaceURI();
    else if(Faults.INVALID_HANDLE_FAULT.equals(fault))
      return InvalidHandleFault.getTypeDesc().getXmlType().getNamespaceURI();
    else if(Faults.INVALID_REGISTRATION_FAULT.equals(fault))
      return InvalidRegistrationFault.getTypeDesc().getXmlType().getNamespaceURI();
    else if(Faults.INVALID_SESSION_FAULT.equals(fault))
      return InvalidSessionFault.getTypeDesc().getXmlType().getNamespaceURI();
    else if(Faults.INVALID_USER_CATEGORY_FAULT.equals(fault))
      return InvalidUserCategoryFault.getTypeDesc().getXmlType().getNamespaceURI();
    else if(Faults.MISSING_PARAMETERS_FAULT.equals(fault))
      return MissingParametersFault.getTypeDesc().getXmlType().getNamespaceURI();
    else if(Faults.OPERATION_FAILED_FAULT.equals(fault))
      return OperationFailedFault.getTypeDesc().getXmlType().getNamespaceURI();
    else if(Faults.PORTLET_STATE_CHANGE_REQUIRED_FAULT.equals(fault))
      return PortletStateChangeRequiredFault.getTypeDesc().getXmlType().getNamespaceURI();
    else if(Faults.UNSUPPORTED_LOCALE_FAULT.equals(fault))
      return UnsupportedLocaleFault.getTypeDesc().getXmlType().getNamespaceURI();
    else if(Faults.UNSUPPORTED_MIME_TYPE_FAULT.equals(fault))
      return UnsupportedMimeTypeFault.getTypeDesc().getXmlType().getNamespaceURI();      
    else if(Faults.UNSUPPORTED_MODE_FAULT.equals(fault))
      return UnsupportedModeFault.getTypeDesc().getXmlType().getNamespaceURI();      
    else if(Faults.UNSUPPORTED_WINDOW_STATE_FAULT.equals(fault))
      return UnsupportedWindowStateFault.getTypeDesc().getXmlType().getNamespaceURI();
                                        
    return null;  
  }

}
