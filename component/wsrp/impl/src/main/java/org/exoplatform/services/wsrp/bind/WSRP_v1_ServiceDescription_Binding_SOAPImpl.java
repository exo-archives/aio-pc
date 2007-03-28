/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 *
 * Created on 15 janv. 2004
 */
package org.exoplatform.services.wsrp.bind;

import java.rmi.RemoteException;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.wsrp.intf.WSRP_v1_ServiceDescription_PortType;
import org.exoplatform.services.wsrp.producer.ServiceDescriptionInterface;
import org.exoplatform.services.wsrp.type.InvalidRegistrationFault;
import org.exoplatform.services.wsrp.type.OperationFailedFault;
import org.exoplatform.services.wsrp.type.ServiceDescription;
import org.exoplatform.services.wsrp.type.ServiceDescriptionRequest;


/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class WSRP_v1_ServiceDescription_Binding_SOAPImpl implements WSRP_v1_ServiceDescription_PortType {

  private ServiceDescriptionInterface serviceDescriptionInterface;

  public WSRP_v1_ServiceDescription_Binding_SOAPImpl() {
    serviceDescriptionInterface = (ServiceDescriptionInterface) ExoContainerContext.getCurrentContainer().
        getComponentInstanceOfType(ServiceDescriptionInterface.class);
  }

  public ServiceDescription getServiceDescription(ServiceDescriptionRequest getServiceDescription)
      throws RemoteException, InvalidRegistrationFault, OperationFailedFault {    
    return serviceDescriptionInterface.getServiceDescription(getServiceDescription.getRegistrationContext(),
                                                             getServiceDescription.getDesiredLocales());
  }

}
