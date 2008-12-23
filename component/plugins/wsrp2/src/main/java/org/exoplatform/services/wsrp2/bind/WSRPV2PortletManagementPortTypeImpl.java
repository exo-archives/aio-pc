package org.exoplatform.services.wsrp2.bind;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.ws.AbstractSingletonWebService;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.AccessDenied;
import org.exoplatform.services.wsrp2.intf.ExportByValueNotSupported;
import org.exoplatform.services.wsrp2.intf.ExportNoLongerValid;
import org.exoplatform.services.wsrp2.intf.InconsistentParameters;
import org.exoplatform.services.wsrp2.intf.InvalidHandle;
import org.exoplatform.services.wsrp2.intf.InvalidRegistration;
import org.exoplatform.services.wsrp2.intf.InvalidUserCategory;
import org.exoplatform.services.wsrp2.intf.MissingParameters;
import org.exoplatform.services.wsrp2.intf.ModifyRegistrationRequired;
import org.exoplatform.services.wsrp2.intf.OperationFailed;
import org.exoplatform.services.wsrp2.intf.OperationNotSupported;
import org.exoplatform.services.wsrp2.intf.ResourceSuspended;
import org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType;
import org.exoplatform.services.wsrp2.producer.PortletManagementOperationsInterface;
import org.exoplatform.services.wsrp2.type.CopyPortletsResponse;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.ExportPortletsResponse;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.type.ImportPortletsResponse;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.OperationFailedFault;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.SetPortletsLifetimeResponse;

/**
 */

@javax.jws.WebService(name = "WSRPV2PortletManagementPortType", serviceName = "WSRPService", portName = "WSRP_v2_PortletManagement_Service", targetNamespace = "urn:oasis:names:tc:wsrp:v2:wsdl", wsdlLocation = "file:/home/alexey/java/eXoProjects/portlet-container/branches/2.1/component/plugins/wsrp2/wsdl/wsrp-service.wsdl", endpointInterface = "org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType")
public class WSRPV2PortletManagementPortTypeImpl implements WSRPV2PortletManagementPortType,
    AbstractSingletonWebService {

  private static final Log                     LOG = ExoLogger.getLogger(WSRPV2PortletManagementPortTypeImpl.class.getName());

  private PortletManagementOperationsInterface portletManagementOperationsInterface;

  public WSRPV2PortletManagementPortTypeImpl(PortletManagementOperationsInterface portletManagementOperationsInterface) {
    this.portletManagementOperationsInterface = portletManagementOperationsInterface;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType#clonePortlet(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp2.type.PortletContext  portletContext ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)org.exoplatform.services.wsrp2.type.Lifetime  lifetime ,)java.lang.String  portletHandle ,)byte[]  portletState ,)org.exoplatform.services.wsrp2.type.Lifetime  scheduledDestruction ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
   */
  public void clonePortlet(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                           org.exoplatform.services.wsrp2.type.PortletContext portletContext,
                           org.exoplatform.services.wsrp2.type.UserContext userContext,
                           org.exoplatform.services.wsrp2.type.Lifetime lifetime,
                           javax.xml.ws.Holder<java.lang.String> portletHandle,
                           javax.xml.ws.Holder<byte[]> portletState,
                           javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime> scheduledDestruction,
                           javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws OperationNotSupported,
                                                                                                                         AccessDenied,
                                                                                                                         ResourceSuspended,
                                                                                                                         InvalidRegistration,
                                                                                                                         InvalidHandle,
                                                                                                                         InvalidUserCategory,
                                                                                                                         ModifyRegistrationRequired,
                                                                                                                         MissingParameters,
                                                                                                                         InconsistentParameters,
                                                                                                                         OperationFailed {
    LOG.info("Executing operation clonePortlet");
    System.out.println(registrationContext);
    System.out.println(portletContext);
    System.out.println(userContext);
    System.out.println(lifetime);
    try {
      PortletContext response = portletManagementOperationsInterface.clonePortlet(registrationContext,
                                                                                  portletContext,
                                                                                  userContext,
                                                                                  lifetime);
      java.lang.String portletHandleValue = response.getPortletHandle();
      portletHandle.value = portletHandleValue;
      byte[] portletStateValue = response.getPortletState();
      portletState.value = portletStateValue;
      org.exoplatform.services.wsrp2.type.Lifetime scheduledDestructionValue = response.getScheduledDestruction();
      scheduledDestruction.value = scheduledDestructionValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType#exportPortlets(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)java.util.List<org.exoplatform.services.wsrp2.type.PortletContext>  portletContext ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)org.exoplatform.services.wsrp2.type.Lifetime  lifetime ,)java.lang.Boolean  exportByValueRequired ,)byte[]  exportContext ,)java.util.List<org.exoplatform.services.wsrp2.type.ExportedPortlet>  exportedPortlet ,)java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets>  failedPortlets ,)org.exoplatform.services.wsrp2.type.ResourceList  resourceList ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
   */
  public void exportPortlets(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                             java.util.List<org.exoplatform.services.wsrp2.type.PortletContext> portletContext,
                             org.exoplatform.services.wsrp2.type.UserContext userContext,
                             javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime> lifetime,
                             java.lang.Boolean exportByValueRequired,
                             javax.xml.ws.Holder<byte[]> exportContext,
                             javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ExportedPortlet>> exportedPortlet,
                             javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets>> failedPortlets,
                             javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList> resourceList,
                             javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws OperationNotSupported,
                                                                                                                           ExportByValueNotSupported,
                                                                                                                           AccessDenied,
                                                                                                                           ResourceSuspended,
                                                                                                                           InvalidRegistration,
                                                                                                                           InvalidHandle,
                                                                                                                           InvalidUserCategory,
                                                                                                                           ModifyRegistrationRequired,
                                                                                                                           MissingParameters,
                                                                                                                           InconsistentParameters,
                                                                                                                           OperationFailed {
    LOG.info("Executing operation exportPortlets");
    System.out.println(registrationContext);
    System.out.println(portletContext);
    System.out.println(userContext);
    System.out.println(lifetime.value);
    System.out.println(exportByValueRequired);
    try {
      ExportPortletsResponse response = portletManagementOperationsInterface.exportPortlets(registrationContext,
                                                                                            portletContext,
                                                                                            userContext,
                                                                                            lifetime.value,
                                                                                            exportByValueRequired);

      byte[] exportContextValue = response.getExportContext();
      exportContext.value = exportContextValue;
      java.util.List<org.exoplatform.services.wsrp2.type.ExportedPortlet> exportedPortletValue = response.getExportedPortlet();
      exportedPortlet.value = exportedPortletValue;
      java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets> failedPortletsValue = response.getFailedPortlets();
      failedPortlets.value = failedPortletsValue;
      org.exoplatform.services.wsrp2.type.ResourceList resourceListValue = response.getResourceList();
      resourceList.value = resourceListValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType#getPortletsLifetime(org.exoplatform.services.wsrp2.type.GetPortletsLifetime  getPortletsLifetime )*
   */
  public org.exoplatform.services.wsrp2.type.GetPortletsLifetimeResponse getPortletsLifetime(org.exoplatform.services.wsrp2.type.GetPortletsLifetime getPortletsLifetime) throws OperationNotSupported,
                                                                                                                                                                         AccessDenied,
                                                                                                                                                                         ResourceSuspended,
                                                                                                                                                                         InvalidRegistration,
                                                                                                                                                                         InvalidHandle,
                                                                                                                                                                         ModifyRegistrationRequired,
                                                                                                                                                                         InconsistentParameters,
                                                                                                                                                                         OperationFailed {
    LOG.info("Executing operation getPortletsLifetime");
    System.out.println(getPortletsLifetime);
    try {
      GetPortletsLifetimeResponse response = portletManagementOperationsInterface.getPortletsLifetime(getPortletsLifetime.getRegistrationContext(),
                                                                                                      getPortletsLifetime.getPortletContext(),
                                                                                                      getPortletsLifetime.getUserContext());
      org.exoplatform.services.wsrp2.type.GetPortletsLifetimeResponse _return = response;
      return _return;
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType#getPortletPropertyDescription(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp2.type.PortletContext  portletContext ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)java.util.List<java.lang.String>  desiredLocales ,)org.exoplatform.services.wsrp2.type.ModelDescription  modelDescription ,)org.exoplatform.services.wsrp2.type.ResourceList  resourceList ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
   */
  public void getPortletPropertyDescription(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                                            org.exoplatform.services.wsrp2.type.PortletContext portletContext,
                                            org.exoplatform.services.wsrp2.type.UserContext userContext,
                                            java.util.List<java.lang.String> desiredLocales,
                                            javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ModelDescription> modelDescription,
                                            javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList> resourceList,
                                            javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws OperationNotSupported,
                                                                                                                                          AccessDenied,
                                                                                                                                          ResourceSuspended,
                                                                                                                                          InvalidRegistration,
                                                                                                                                          InvalidHandle,
                                                                                                                                          InvalidUserCategory,
                                                                                                                                          ModifyRegistrationRequired,
                                                                                                                                          MissingParameters,
                                                                                                                                          InconsistentParameters,
                                                                                                                                          OperationFailed {
    LOG.info("Executing operation getPortletPropertyDescription");
    System.out.println(registrationContext);
    System.out.println(portletContext);
    System.out.println(userContext);
    System.out.println(desiredLocales);
    try {
      PortletPropertyDescriptionResponse response = portletManagementOperationsInterface.getPortletPropertyDescription(registrationContext,
                                                                                                                       portletContext,
                                                                                                                       userContext,
                                                                                                                       desiredLocales);
      org.exoplatform.services.wsrp2.type.ModelDescription modelDescriptionValue = response.getModelDescription();
      modelDescription.value = modelDescriptionValue;
      org.exoplatform.services.wsrp2.type.ResourceList resourceListValue = response.getResourceList();
      resourceList.value = resourceListValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType#getPortletDescription(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp2.type.PortletContext  portletContext ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)java.util.List<java.lang.String>  desiredLocales ,)org.exoplatform.services.wsrp2.type.PortletDescription  portletDescription ,)org.exoplatform.services.wsrp2.type.ResourceList  resourceList ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
   */
  public void getPortletDescription(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                                    org.exoplatform.services.wsrp2.type.PortletContext portletContext,
                                    org.exoplatform.services.wsrp2.type.UserContext userContext,
                                    java.util.List<java.lang.String> desiredLocales,
                                    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.PortletDescription> portletDescription,
                                    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList> resourceList,
                                    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws OperationNotSupported,
                                                                                                                                  AccessDenied,
                                                                                                                                  ResourceSuspended,
                                                                                                                                  InvalidRegistration,
                                                                                                                                  InvalidHandle,
                                                                                                                                  InvalidUserCategory,
                                                                                                                                  ModifyRegistrationRequired,
                                                                                                                                  MissingParameters,
                                                                                                                                  InconsistentParameters,
                                                                                                                                  OperationFailed {
    LOG.info("Executing operation getPortletDescription");
    System.out.println(registrationContext);
    System.out.println(portletContext);
    System.out.println(userContext);
    System.out.println(desiredLocales);
    try {
      PortletDescriptionResponse response = portletManagementOperationsInterface.getPortletDescription(registrationContext,
                                                                                                       portletContext,
                                                                                                       userContext,
                                                                                                       desiredLocales);
      org.exoplatform.services.wsrp2.type.PortletDescription portletDescriptionValue = response.getPortletDescription();
      portletDescription.value = portletDescriptionValue;
      org.exoplatform.services.wsrp2.type.ResourceList resourceListValue = response.getResourceList();
      resourceList.value = resourceListValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType#releaseExport(org.exoplatform.services.wsrp2.type.ReleaseExport  releaseExport )*
   */
  public org.exoplatform.services.wsrp2.type.ReturnAny releaseExport(org.exoplatform.services.wsrp2.type.ReleaseExport releaseExport) {
    LOG.info("Executing operation releaseExport");
    System.out.println(releaseExport);

    ReturnAny response = portletManagementOperationsInterface.releaseExport(releaseExport.getExportContext(),
                                                                            releaseExport.getUserContext(),
                                                                            releaseExport.getRegistrationContext());
    org.exoplatform.services.wsrp2.type.ReturnAny _return = response;
    return _return;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType#copyPortlets(org.exoplatform.services.wsrp2.type.RegistrationContext  toRegistrationContext ,)org.exoplatform.services.wsrp2.type.UserContext  toUserContext ,)org.exoplatform.services.wsrp2.type.RegistrationContext  fromRegistrationContext ,)org.exoplatform.services.wsrp2.type.UserContext  fromUserContext ,)java.util.List<org.exoplatform.services.wsrp2.type.PortletContext>  fromPortletContexts ,)org.exoplatform.services.wsrp2.type.Lifetime  lifetime ,)java.util.List<org.exoplatform.services.wsrp2.type.CopiedPortlet>  copiedPortlets ,)java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets>  failedPortlets ,)org.exoplatform.services.wsrp2.type.ResourceList  resourceList ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
   */
  public void copyPortlets(org.exoplatform.services.wsrp2.type.RegistrationContext toRegistrationContext,
                           org.exoplatform.services.wsrp2.type.UserContext toUserContext,
                           org.exoplatform.services.wsrp2.type.RegistrationContext fromRegistrationContext,
                           org.exoplatform.services.wsrp2.type.UserContext fromUserContext,
                           java.util.List<org.exoplatform.services.wsrp2.type.PortletContext> fromPortletContexts,
                           org.exoplatform.services.wsrp2.type.Lifetime lifetime,
                           javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.CopiedPortlet>> copiedPortlets,
                           javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets>> failedPortlets,
                           javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList> resourceList,
                           javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws OperationNotSupported,
                                                                                                                         AccessDenied,
                                                                                                                         ResourceSuspended,
                                                                                                                         InvalidRegistration,
                                                                                                                         InvalidHandle,
                                                                                                                         InvalidUserCategory,
                                                                                                                         ModifyRegistrationRequired,
                                                                                                                         MissingParameters,
                                                                                                                         InconsistentParameters,
                                                                                                                         OperationFailed {
    LOG.info("Executing operation copyPortlets");
    System.out.println(toRegistrationContext);
    System.out.println(toUserContext);
    System.out.println(fromRegistrationContext);
    System.out.println(fromUserContext);
    System.out.println(fromPortletContexts);
    System.out.println(lifetime);
    try {
      CopyPortletsResponse response = portletManagementOperationsInterface.copyPortlets(toRegistrationContext,
                                                                                        toUserContext,
                                                                                        fromRegistrationContext,
                                                                                        fromUserContext,
                                                                                        fromPortletContexts,
                                                                                        lifetime);
      java.util.List<org.exoplatform.services.wsrp2.type.CopiedPortlet> copiedPortletsValue = response.getCopiedPortlets();
      copiedPortlets.value = copiedPortletsValue;
      java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets> failedPortletsValue = response.getFailedPortlets();
      failedPortlets.value = failedPortletsValue;
      org.exoplatform.services.wsrp2.type.ResourceList resourceListValue = response.getResourceList();
      resourceList.value = resourceListValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType#setExportLifetime(org.exoplatform.services.wsrp2.type.SetExportLifetime  setExportLifetime )*
   */
  public org.exoplatform.services.wsrp2.type.Lifetime setExportLifetime(org.exoplatform.services.wsrp2.type.SetExportLifetime setExportLifetime) throws OperationNotSupported,
                                                                                                                                                AccessDenied,
                                                                                                                                                ResourceSuspended,
                                                                                                                                                InvalidRegistration,
                                                                                                                                                InvalidHandle,
                                                                                                                                                ModifyRegistrationRequired,
                                                                                                                                                OperationFailed {
    LOG.info("Executing operation setExportLifetime");
    System.out.println(setExportLifetime);
    try {
      Lifetime response = portletManagementOperationsInterface.setExportLifetime(setExportLifetime.getRegistrationContext(),
                                                                                 setExportLifetime.getExportContext(),
                                                                                 setExportLifetime.getUserContext(),
                                                                                 setExportLifetime.getLifetime());
      org.exoplatform.services.wsrp2.type.Lifetime _return = response;
      return _return;
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType#destroyPortlets(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)java.util.List<java.lang.String>  portletHandles ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets>  failedPortlets ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
   */
  public void destroyPortlets(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                              java.util.List<java.lang.String> portletHandles,
                              org.exoplatform.services.wsrp2.type.UserContext userContext,
                              javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets>> failedPortlets,
                              javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws OperationNotSupported,
                                                                                                                            ResourceSuspended,
                                                                                                                            InvalidRegistration,
                                                                                                                            ModifyRegistrationRequired,
                                                                                                                            MissingParameters,
                                                                                                                            InconsistentParameters,
                                                                                                                            OperationFailed {
    LOG.info("Executing operation destroyPortlets");
    System.out.println(registrationContext);
    System.out.println(portletHandles);
    System.out.println(userContext);
    try {
      DestroyPortletsResponse response = portletManagementOperationsInterface.destroyPortlets(registrationContext,
                                                                                              portletHandles,
                                                                                              userContext);
      java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets> failedPortletsValue = response.getFailedPortlets();
      failedPortlets.value = failedPortletsValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType#setPortletsLifetime(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)java.util.List<org.exoplatform.services.wsrp2.type.PortletContext>  portletContext ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)org.exoplatform.services.wsrp2.type.Lifetime  lifetime ,)java.util.List<org.exoplatform.services.wsrp2.type.PortletLifetime>  updatedPortlet ,)java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets>  failedPortlets ,)org.exoplatform.services.wsrp2.type.ResourceList  resourceList ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
   */
  public void setPortletsLifetime(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                                  java.util.List<org.exoplatform.services.wsrp2.type.PortletContext> portletContext,
                                  org.exoplatform.services.wsrp2.type.UserContext userContext,
                                  org.exoplatform.services.wsrp2.type.Lifetime lifetime,
                                  javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.PortletLifetime>> updatedPortlet,
                                  javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets>> failedPortlets,
                                  javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList> resourceList,
                                  javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws OperationNotSupported,
                                                                                                                                AccessDenied,
                                                                                                                                ResourceSuspended,
                                                                                                                                InvalidRegistration,
                                                                                                                                InvalidHandle,
                                                                                                                                ModifyRegistrationRequired,
                                                                                                                                InconsistentParameters,
                                                                                                                                OperationFailed {
    LOG.info("Executing operation setPortletsLifetime");
    System.out.println(registrationContext);
    System.out.println(portletContext);
    System.out.println(userContext);
    System.out.println(lifetime);
    try {
      SetPortletsLifetimeResponse response = portletManagementOperationsInterface.setPortletsLifetime(registrationContext,
                                                                                                      portletContext,
                                                                                                      userContext,
                                                                                                      lifetime);
      java.util.List<org.exoplatform.services.wsrp2.type.PortletLifetime> updatedPortletValue = response.getUpdatedPortlet();
      updatedPortlet.value = updatedPortletValue;
      java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets> failedPortletsValue = response.getFailedPortlets();
      failedPortlets.value = failedPortletsValue;
      org.exoplatform.services.wsrp2.type.ResourceList resourceListValue = response.getResourceList();
      resourceList.value = resourceListValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType#setPortletProperties(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp2.type.PortletContext  portletContext ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)org.exoplatform.services.wsrp2.type.PropertyList  propertyList ,)java.lang.String  portletHandle ,)byte[]  portletState ,)org.exoplatform.services.wsrp2.type.Lifetime  scheduledDestruction ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
   */
  public void setPortletProperties(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                                   org.exoplatform.services.wsrp2.type.PortletContext portletContext,
                                   org.exoplatform.services.wsrp2.type.UserContext userContext,
                                   org.exoplatform.services.wsrp2.type.PropertyList propertyList,
                                   javax.xml.ws.Holder<java.lang.String> portletHandle,
                                   javax.xml.ws.Holder<byte[]> portletState,
                                   javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime> scheduledDestruction,
                                   javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws OperationNotSupported,
                                                                                                                                 AccessDenied,
                                                                                                                                 ResourceSuspended,
                                                                                                                                 InvalidRegistration,
                                                                                                                                 InvalidHandle,
                                                                                                                                 InvalidUserCategory,
                                                                                                                                 ModifyRegistrationRequired,
                                                                                                                                 MissingParameters,
                                                                                                                                 InconsistentParameters,
                                                                                                                                 OperationFailed {
    LOG.info("Executing operation setPortletProperties");
    System.out.println(registrationContext);
    System.out.println(portletContext);
    System.out.println(userContext);
    System.out.println(propertyList);
    try {
      PortletContext response = portletManagementOperationsInterface.setPortletProperties(registrationContext,
                                                                                          portletContext,
                                                                                          userContext,
                                                                                          propertyList);
      java.lang.String portletHandleValue = response.getPortletHandle();
      portletHandle.value = portletHandleValue;
      byte[] portletStateValue = response.getPortletState();
      portletState.value = portletStateValue;
      org.exoplatform.services.wsrp2.type.Lifetime scheduledDestructionValue = response.getScheduledDestruction();
      scheduledDestruction.value = scheduledDestructionValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType#getPortletProperties(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)org.exoplatform.services.wsrp2.type.PortletContext  portletContext ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)java.util.List<java.lang.String>  names ,)java.util.List<org.exoplatform.services.wsrp2.type.Property>  properties ,)java.util.List<org.exoplatform.services.wsrp2.type.ResetProperty>  resetProperties ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
   */
  public void getPortletProperties(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                                   org.exoplatform.services.wsrp2.type.PortletContext portletContext,
                                   org.exoplatform.services.wsrp2.type.UserContext userContext,
                                   java.util.List<java.lang.String> names,
                                   javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Property>> properties,
                                   javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ResetProperty>> resetProperties,
                                   javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws OperationNotSupported,
                                                                                                                                 AccessDenied,
                                                                                                                                 ResourceSuspended,
                                                                                                                                 InvalidRegistration,
                                                                                                                                 InvalidHandle,
                                                                                                                                 InvalidUserCategory,
                                                                                                                                 ModifyRegistrationRequired,
                                                                                                                                 MissingParameters,
                                                                                                                                 InconsistentParameters,
                                                                                                                                 OperationFailed {
    LOG.info("Executing operation getPortletProperties");
    System.out.println(registrationContext);
    System.out.println(portletContext);
    System.out.println(userContext);
    System.out.println(names);
    try {
      PropertyList response = portletManagementOperationsInterface.getPortletProperties(registrationContext,
                                                                                        portletContext,
                                                                                        userContext,
                                                                                        names);
      java.util.List<org.exoplatform.services.wsrp2.type.Property> propertiesValue = response.getProperties();
      properties.value = propertiesValue;
      java.util.List<org.exoplatform.services.wsrp2.type.ResetProperty> resetPropertiesValue = response.getResetProperties();
      resetProperties.value = resetPropertiesValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType#importPortlets(org.exoplatform.services.wsrp2.type.RegistrationContext  registrationContext ,)byte[]  importContext ,)java.util.List<org.exoplatform.services.wsrp2.type.ImportPortlet>  importPortlet ,)org.exoplatform.services.wsrp2.type.UserContext  userContext ,)org.exoplatform.services.wsrp2.type.Lifetime  lifetime ,)java.util.List<org.exoplatform.services.wsrp2.type.ImportedPortlet>  importedPortlets ,)java.util.List<org.exoplatform.services.wsrp2.type.ImportPortletsFailed>  importFailed ,)org.exoplatform.services.wsrp2.type.ResourceList  resourceList ,)java.util.List<org.exoplatform.services.wsrp2.type.Extension>  extensions )*
   */
  public void importPortlets(org.exoplatform.services.wsrp2.type.RegistrationContext registrationContext,
                             byte[] importContext,
                             java.util.List<org.exoplatform.services.wsrp2.type.ImportPortlet> importPortlet,
                             org.exoplatform.services.wsrp2.type.UserContext userContext,
                             org.exoplatform.services.wsrp2.type.Lifetime lifetime,
                             javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ImportedPortlet>> importedPortlets,
                             javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ImportPortletsFailed>> importFailed,
                             javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList> resourceList,
                             javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> extensions) throws OperationNotSupported,
                                                                                                                           ExportNoLongerValid,
                                                                                                                           AccessDenied,
                                                                                                                           ResourceSuspended,
                                                                                                                           InvalidRegistration,
                                                                                                                           InvalidUserCategory,
                                                                                                                           ModifyRegistrationRequired,
                                                                                                                           MissingParameters,
                                                                                                                           InconsistentParameters,
                                                                                                                           OperationFailed {
    LOG.info("Executing operation importPortlets");
    System.out.println(registrationContext);
    System.out.println(importContext);
    System.out.println(importPortlet);
    System.out.println(userContext);
    System.out.println(lifetime);
    try {
      ImportPortletsResponse response = portletManagementOperationsInterface.importPortlets(registrationContext,
                                                                                            importContext,
                                                                                            importPortlet,
                                                                                            userContext,
                                                                                            lifetime);
      java.util.List<org.exoplatform.services.wsrp2.type.ImportedPortlet> importedPortletsValue = response.getImportedPortlets();
      importedPortlets.value = importedPortletsValue;
      java.util.List<org.exoplatform.services.wsrp2.type.ImportPortletsFailed> importFailedValue = response.getImportFailed();
      importFailed.value = importFailedValue;
      org.exoplatform.services.wsrp2.type.ResourceList resourceListValue = response.getResourceList();
      resourceList.value = resourceListValue;
      java.util.List<org.exoplatform.services.wsrp2.type.Extension> extensionsValue = response.getExtensions();
      extensions.value = extensionsValue;
    } catch (WSRPException wsrpe) {
      LOG.error(wsrpe.getMessage(), wsrpe);
      throw new OperationFailed(wsrpe.getMessage(), new OperationFailedFault());
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      throw new OperationFailed(e.getMessage(), new OperationFailedFault());
    }
  }

}
