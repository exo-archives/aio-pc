/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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

package org.exoplatform.services.wsrp2.producer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.helper.IOUtil;
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
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.producer.PortletContainerProxy;
import org.exoplatform.services.wsrp2.producer.PortletManagementOperationsInterface;
import org.exoplatform.services.wsrp2.producer.impl.helpers.LifetimeHelper;
import org.exoplatform.services.wsrp2.producer.impl.utils.CalendarUtils;
import org.exoplatform.services.wsrp2.type.CopiedPortlet;
import org.exoplatform.services.wsrp2.type.CopyPortletsResponse;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.ExportPortletsResponse;
import org.exoplatform.services.wsrp2.type.ExportedPortlet;
import org.exoplatform.services.wsrp2.type.FailedPortlets;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.type.ImportPortlet;
import org.exoplatform.services.wsrp2.type.ImportPortletsFailed;
import org.exoplatform.services.wsrp2.type.ImportPortletsResponse;
import org.exoplatform.services.wsrp2.type.ImportedPortlet;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.LocalizedString;
import org.exoplatform.services.wsrp2.type.ModelDescription;
import org.exoplatform.services.wsrp2.type.OperationNotSupportedFault;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PortletLifetime;
import org.exoplatform.services.wsrp2.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp2.type.Property;
import org.exoplatform.services.wsrp2.type.PropertyDescription;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.ResourceList;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.SetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class PortletManagementOperationsInterfaceImpl implements
    PortletManagementOperationsInterface {

  private PortletContainerProxy  proxy;

  private Log                    log;

  private PersistentStateManager stateManager;

  private WSRPConfiguration      conf;

  public PortletManagementOperationsInterfaceImpl(PersistentStateManager stateManager,
                                                  PortletContainerProxy portletContainerProxy,
                                                  WSRPConfiguration conf) {
    this.proxy = portletContainerProxy;
    this.stateManager = stateManager;
    this.conf = conf;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp2");
  }

  public PortletContext clonePortlet(RegistrationContext registrationContext,
                                     PortletContext portletContext,
                                     UserContext userContext,
                                     Lifetime lifetime) throws OperationNotSupported,
                                                       AccessDenied,
                                                       ResourceSuspended,
                                                       InvalidRegistration,
                                                       InvalidHandle,
                                                       InvalidUserCategory,
                                                       ModifyRegistrationRequired,
                                                       MissingParameters,
                                                       InconsistentParameters,
                                                       OperationFailed,
                                                       WSRPException {

    if (registrationContext == null) {
      throw new InvalidRegistration("registrationContext is null");
    }

    if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
      LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
      LifetimeVerifier.checkPortletLifetime(registrationContext, portletContext, userContext);
    }

    log.debug("Clone a portlet for the registered consumer : "
        + registrationContext.getRegistrationHandle());

    String portletHandle = portletContext.getPortletHandle();

    String newPortletHandle = createNewPortletHandle(portletHandle);

    PortletContext clonedPortletContext = new PortletContext();
    clonedPortletContext.setPortletHandle(newPortletHandle);

    try {
      clonedPortletContext = processCopyPortletHandle(registrationContext,
                                                      userContext,
                                                      clonedPortletContext,
                                                      registrationContext,
                                                      userContext,
                                                      portletContext);

    } catch (WSRPException e) {
      throw new WSRPException("Can not clone portlet", e);
    }
    log.debug("New portlet handle : " + newPortletHandle);
    return clonedPortletContext;
  }

  public CopyPortletsResponse copyPortlets(RegistrationContext toRegistrationContext,
                                           UserContext toUserContext,
                                           RegistrationContext fromRegistrationContext,
                                           UserContext fromUserContext,
                                           List<PortletContext> fromPortletContexts,
                                           Lifetime lifetime) throws OperationNotSupported,
                                                             AccessDenied,
                                                             ResourceSuspended,
                                                             InvalidRegistration,
                                                             InvalidHandle,
                                                             InvalidUserCategory,
                                                             ModifyRegistrationRequired,
                                                             MissingParameters,
                                                             InconsistentParameters,
                                                             OperationFailed,
                                                             WSRPException {

    if (fromRegistrationContext == null) {
      throw new InvalidRegistration("registrationContext is null");
    }
    if (RegistrationVerifier.checkRegistrationContext(fromRegistrationContext)) {
      LifetimeVerifier.checkRegistrationLifetime(fromRegistrationContext, fromUserContext);
      for (PortletContext fromPortletContext : fromPortletContexts) {
        LifetimeVerifier.checkPortletLifetime(fromRegistrationContext,
                                              fromPortletContext,
                                              fromUserContext);
      }
    }
    log.debug("Copying portlets for the registered consumer : "
        + fromRegistrationContext.getRegistrationHandle());

    Collection<CopiedPortlet> copiedPortlets = new ArrayList<CopiedPortlet>();
    Collection<FailedPortlets> failedPortlets = new ArrayList<FailedPortlets>();

    for (PortletContext fromPortletContext : fromPortletContexts) {
      String fromPortletHandle = fromPortletContext.getPortletHandle();
//      byte[] portletState = fromPortletContext.getPortletState();
//      Lifetime portletLifetime = fromPortletContext.getScheduledDestruction();

      String newPortletHandle = null;

      try {

        newPortletHandle = createNewPortletHandle(fromPortletHandle);

        PortletContext toPortletContext = new PortletContext();
        toPortletContext.setPortletHandle(newPortletHandle);

        toPortletContext = processCopyPortletHandle(toRegistrationContext,
                                                    toUserContext,
                                                    toPortletContext,
                                                    fromRegistrationContext,
                                                    fromUserContext,
                                                    fromPortletContext);

        CopiedPortlet copiedPortlet = new CopiedPortlet();
        copiedPortlet.setFromPortletHandle(fromPortletHandle);
        copiedPortlet.setNewPortletContext(toPortletContext);

        copiedPortlets.add(copiedPortlet);

      } catch (Exception e) {
        log.error("Can not copy portlet", e);
        FailedPortlets failedPortlet = new FailedPortlets();
        failedPortlet.getPortletHandles().add(fromPortletHandle);
        LocalizedString reason = new LocalizedString();
        reason.setValue("Cannot copy portlet");
        failedPortlet.setReason(reason);
        failedPortlets.add(failedPortlet);
      }

    } // END for each fromPortletContext

    CopyPortletsResponse copyPortletsResponse = new CopyPortletsResponse();
    if (copiedPortlets != null)
      copyPortletsResponse.getCopiedPortlets().addAll(copiedPortlets);
    if (failedPortlets != null)
      copyPortletsResponse.getFailedPortlets().addAll(failedPortlets);
    copyPortletsResponse.setResourceList(null);

    return copyPortletsResponse;

  }

  public ExportPortletsResponse exportPortlets(RegistrationContext registrationContext,
                                               List<PortletContext> portletContexts,
                                               UserContext userContext,
                                               Lifetime lifetime,
                                               boolean exportByValueRequired) throws OperationNotSupported,
                                                                             ExportByValueNotSupported,
                                                                             AccessDenied,
                                                                             ResourceSuspended,
                                                                             InvalidRegistration,
                                                                             InvalidHandle,
                                                                             InvalidUserCategory,
                                                                             ModifyRegistrationRequired,
                                                                             MissingParameters,
                                                                             InconsistentParameters,
                                                                             OperationFailed,
                                                                             WSRPException {
    if (registrationContext == null) {
      throw new InvalidRegistration("registrationContext is null");
    }
    if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
      LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
      Iterator<PortletContext> portletContextsIterator = portletContexts.iterator();
      while (portletContextsIterator.hasNext()) {
        PortletContext portletContext = (PortletContext) portletContextsIterator.next();
        LifetimeVerifier.checkPortletLifetime(registrationContext, portletContext, userContext);
      }
    }

    log.debug("Exporting portlets for the registered consumer : "
        + registrationContext.getRegistrationHandle());

    if (exportByValueRequired) {
      throw new ExportByValueNotSupported();
    }

    RegistrationVerifier.checkRegistrationContext(registrationContext);
    Collection<ExportedPortlet> exportedPortlets = new ArrayList<ExportedPortlet>();
    Collection<FailedPortlets> failedPortlets = new ArrayList<FailedPortlets>();

    for (PortletContext portletContext : portletContexts) {
      try {
//        byte[] exportData = portletContext.getPortletState();

        byte[] exportData = IOUtil.serialize(portletContext);

        ExportedPortlet exportedPortlet = new ExportedPortlet();
        exportedPortlet.setExportData(exportData);
        exportedPortlet.setPortletHandle(portletContext.getPortletHandle());
        exportedPortlets.add(exportedPortlet);
      } catch (Exception e) {
        log.error("Can not copy portlet", e);
        LocalizedString reason = new LocalizedString();
        reason.setValue("Cannot copy portlet");

        FailedPortlets failedPortlet = new FailedPortlets();
        failedPortlet.getPortletHandles().add(portletContext.getPortletHandle());
        failedPortlet.setReason(reason);
        failedPortlets.add(failedPortlet);
      }
    }

    byte[] exportContext = {};

    ExportPortletsResponse exportPortletsResponse = new ExportPortletsResponse();
    exportPortletsResponse.setExportContext(exportContext);
    if (exportedPortlets != null)
      exportPortletsResponse.getExportedPortlet().addAll(exportedPortlets);
    if (failedPortlets != null)
      exportPortletsResponse.getFailedPortlets().addAll(failedPortlets);
    if (lifetime != null)
      exportPortletsResponse.setLifetime(lifetime);
    exportPortletsResponse.setResourceList(null);

    return exportPortletsResponse;
  }

  public ImportPortletsResponse importPortlets(RegistrationContext registrationContext,
                                               byte[] importContext,
                                               List<ImportPortlet> importPortlets,
                                               UserContext userContext,
                                               Lifetime lifetime)

                                                                 throws OperationNotSupported,
                                                                 ExportNoLongerValid,
                                                                 AccessDenied,
                                                                 ResourceSuspended,
                                                                 InvalidRegistration,
                                                                 InvalidUserCategory,
                                                                 ModifyRegistrationRequired,
                                                                 MissingParameters,
                                                                 InconsistentParameters,
                                                                 OperationFailed,
                                                                 WSRPException {
    if (registrationContext == null) {
      throw new InvalidRegistration("registrationContext is null");
    }
    try {
      if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
        LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
      }
    } catch (InvalidHandle ih) {
      throw new InvalidRegistration(ih.getMessage(), ih);
    } catch (AccessDenied ih) {
      throw new InvalidRegistration(ih.getMessage(), ih);
    }
    log.debug("Importing portlets for the registered consumer : "
        + registrationContext.getRegistrationHandle());

    Collection<ImportedPortlet> importedPortlets = null;
    Collection<ImportPortletsFailed> failedPortlets = null;

    for (ImportPortlet importPortlet : importPortlets) {

      try {
        String importID = importPortlet.getImportID();
        byte[] importData = importPortlet.getExportData();

        PortletContext newPortletContext = resolvePortletContext(importData);

        String[] k = StringUtils.split(newPortletContext.getPortletHandle(),
                                       Constants.PORTLET_META_DATA_ENCODER);
        String portletApplicationName = k[0];
        String portletName = k[1];
        if (log.isDebugEnabled()) {
          log.debug("importPortlets of portlet in application: " + portletApplicationName);
          log.debug("importPortlets of portlet: " + portletName);
        }

        String fromPortletHandle = k[0] + "/" + k[1];
        if (!proxy.isPortletOffered(fromPortletHandle)) {
          log.debug("The portlet handle is not offered by the Producer");
          throw new InvalidHandle();
        }

//        PortletPreferences prefsImp = null;
//        byte[] portletState = newPortletContext.getPortletState();
//        if (portletState != null) {
//          try {
//            prefsImp = (PortletPreferences) IOUtil.deserialize(portletState);
//          } catch (Exception e) {
//            log.error("Error: ", e);
//          }
//        }

        ImportedPortlet importedPortlet = new ImportedPortlet();
        importedPortlet.setImportID(importID);
        importedPortlet.setNewPortletContext(newPortletContext);

        if (importedPortlets == null)
          importedPortlets = new ArrayList<ImportedPortlet>();
        importedPortlets.add(importedPortlet);
      } catch (Exception e) {
        log.error("Can not import portlet", e);
        LocalizedString reason = new LocalizedString();
        reason.setValue("Cannot import portlet");

        ImportPortletsFailed failedPortlet = new ImportPortletsFailed();
        failedPortlet.getImportID().add(importPortlet.getImportID());
        failedPortlet.setReason(reason);
        if (failedPortlets == null)
          failedPortlets = new ArrayList<ImportPortletsFailed>();
        failedPortlets.add(failedPortlet);
      }
    }

    ImportPortletsResponse importPortletsResponse = new ImportPortletsResponse();
    if (importedPortlets != null)
      importPortletsResponse.getImportedPortlets().addAll(importedPortlets);
    if (failedPortlets != null)
      importPortletsResponse.getImportFailed().addAll(failedPortlets);

    return importPortletsResponse;
  }

  private PortletContext resolvePortletContext(byte[] importData) throws WSRPException {
    Object o = null;
    try {
      o = IOUtil.deserialize(importData);
    } catch (Exception e) {
      throw new WSRPException("deserialize fault", e);
    }
    if (o instanceof PortletContext) {
      return (PortletContext) o;
    }
    return null;
  }

  public ReturnAny releaseExport(byte[] exportContext,
                                 UserContext userContext,
                                 RegistrationContext registrationContext) {
//    throw new OperationNotSupported("The releaseExport Operation Not Supported", new OperationNotSupportedFault());
    return new ReturnAny();
  }

  public Lifetime setExportLifetime(RegistrationContext registrationContext,
                                    byte[] exportContext,
                                    UserContext userContext,
                                    Lifetime lifetime) throws OperationNotSupported,
                                                      AccessDenied,
                                                      ResourceSuspended,
                                                      InvalidRegistration,
                                                      InvalidHandle,
                                                      ModifyRegistrationRequired,
                                                      OperationFailed,
                                                      WSRPException {
    if (registrationContext == null) {
      throw new InvalidRegistration("registrationContext is null");
    }
    if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
      LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
    }

    throw new OperationNotSupported("The setExportLifetime Operation Not Supported",
                                    new OperationNotSupportedFault());
//    return lifetime;
  }

  public DestroyPortletsResponse destroyPortlets(RegistrationContext registrationContext,
                                                 List<String> portletHandles,
                                                 UserContext userContext) throws OperationNotSupported,
                                                                         ResourceSuspended,
                                                                         InvalidRegistration,
                                                                         ModifyRegistrationRequired,
                                                                         MissingParameters,
                                                                         InconsistentParameters,
                                                                         OperationFailed,
                                                                         WSRPException {
    if (registrationContext == null) {
      throw new InvalidRegistration("registrationContext is null");
    }
    try {
      if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
        LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
      }
    } catch (InvalidHandle ih) {
      throw new InvalidRegistration(ih.getMessage(), ih);
    } catch (AccessDenied ih) {
      throw new InvalidRegistration(ih.getMessage(), ih);
    }
    log.debug("Destroy portlet for registration handle "
        + registrationContext.getRegistrationHandle());

    List<FailedPortlets> fails = new ArrayList<FailedPortlets>();

    for (String portletHandle : portletHandles) {
      try {
        if (stateManager.isConsumerConfiguredPortlet(portletHandle, registrationContext)) {
          log.debug("Destroy a consumer configured portlet : " + portletHandle);
          stateManager.removeConsumerConfiguredPortletHandle(portletHandle, registrationContext);
        } else {
          log.debug("Can't destroy a portlet that did not exist : " + portletHandle);
          FailedPortlets failedPortlets = new FailedPortlets();
          failedPortlets.getPortletHandles().add(portletHandle);
          LocalizedString reason = new LocalizedString();
          reason.setValue("Can't destroy a portlet that did not exist");
          failedPortlets.setReason(reason);
          fails.add(failedPortlets);
        }
      } catch (WSRPException e) {
        throw new WSRPException();
      }
    }

    DestroyPortletsResponse response = new DestroyPortletsResponse();
    // Convert from Collection<FailedPortlets> to array FailedPortlets[]
    if (fails != null)
      response.getFailedPortlets().addAll(fails);
    Iterator<FailedPortlets> failedPortletsIter = response.getFailedPortlets().iterator();
    while (failedPortletsIter.hasNext()) {
      FailedPortlets elem = (FailedPortlets) failedPortletsIter.next();
      Iterator<String> portletHandlesIter = elem.getPortletHandles().iterator();
      while (portletHandlesIter.hasNext()) {
        String failedPortletsHandle = (String) portletHandlesIter.next();
        if (log.isDebugEnabled()) {
          log.debug(" failedPortletsHandle = " + failedPortletsHandle);
        }
      }
    }

    return response;
  }

  public GetPortletsLifetimeResponse getPortletsLifetime(RegistrationContext registrationContext,
                                                         List<PortletContext> portletContexts,
                                                         UserContext userContext) throws OperationNotSupported,
                                                                                 AccessDenied,
                                                                                 ResourceSuspended,
                                                                                 InvalidRegistration,
                                                                                 InvalidHandle,
                                                                                 ModifyRegistrationRequired,
                                                                                 InconsistentParameters,
                                                                                 OperationFailed,
                                                                                 WSRPException {

    if (registrationContext == null) {
      throw new InvalidRegistration("registrationContext is null");
    }
    if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
      LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
    }
    log.debug("Set portlet properties for registration handle "
        + registrationContext.getRegistrationHandle());

    Collection<PortletLifetime> portletLifetimes = new ArrayList<PortletLifetime>();
    Collection<FailedPortlets> failedPortlets = new ArrayList<FailedPortlets>();

    for (PortletContext portletContext : portletContexts) {
      String portletHandle = portletContext.getPortletHandle();
      try {

        Lifetime lifetimeResult = getPortletLifetimePrivate(portletHandle);

        PortletLifetime portletLifetime = new PortletLifetime();
        portletLifetime.setPortletContext(portletContext);
        portletLifetime.setScheduledDestruction(lifetimeResult);
        portletLifetimes.add(portletLifetime);
      } catch (Exception e) {
        FailedPortlets fPortlet = new FailedPortlets();
        failedPortlets.add(fPortlet);
      }
    }

    GetPortletsLifetimeResponse lfResponse = new GetPortletsLifetimeResponse();
    if (failedPortlets != null)
      lfResponse.getFailedPortlets().addAll(failedPortlets);
    if (portletLifetimes != null)
      lfResponse.getPortletLifetime().addAll(portletLifetimes);
    return lfResponse;

  }

  private Lifetime getPortletLifetimePrivate(String portletHandle) throws WSRPException {
    Lifetime lifetimeResult = stateManager.getPortletLifetime(portletHandle);
    if (lifetimeResult != null)
      lifetimeResult.setCurrentTime(CalendarUtils.getNow());
    LifetimeHelper.lifetimeExpired(lifetimeResult);
    return lifetimeResult;
  }

  public SetPortletsLifetimeResponse setPortletsLifetime(RegistrationContext registrationContext,
                                                         List<PortletContext> portletContexts,
                                                         UserContext userContext,
                                                         Lifetime lifetime) throws OperationNotSupported,
                                                                           AccessDenied,
                                                                           ResourceSuspended,
                                                                           InvalidRegistration,
                                                                           InvalidHandle,
                                                                           ModifyRegistrationRequired,
                                                                           InconsistentParameters,
                                                                           OperationFailed,
                                                                           WSRPException {

    if (registrationContext == null) {
      throw new InvalidRegistration("registrationContext is null");
    }
    if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
      LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
      Iterator<PortletContext> portletContextsIterator = portletContexts.iterator();
      while (portletContextsIterator.hasNext()) {
        PortletContext portletContext = (PortletContext) portletContextsIterator.next();
        LifetimeVerifier.checkPortletLifetime(registrationContext, portletContext, userContext);
      }
    }
    log.debug("Set portlet properties for registration handle "
        + registrationContext.getRegistrationHandle());

    Collection<PortletLifetime> portletLifetimes = new ArrayList<PortletLifetime>();
    Collection<FailedPortlets> failedPortlets = new ArrayList<FailedPortlets>();

    for (PortletContext portletContext : portletContexts) {
      String portletHandle = portletContext.getPortletHandle();
      try {
        Lifetime lifetimeResult = stateManager.putPortletLifetime(portletHandle, lifetime);
        PortletLifetime portletLifetime = new PortletLifetime();
        portletLifetime.setPortletContext(portletContext);
        portletLifetime.setScheduledDestruction(lifetimeResult);
        portletLifetimes.add(portletLifetime);
      } catch (Exception e) {
        FailedPortlets fPortlet = new FailedPortlets();
        failedPortlets.add(fPortlet);
      }
    }

    SetPortletsLifetimeResponse setLifetimeResponse = new SetPortletsLifetimeResponse();
    if (failedPortlets != null)
      setLifetimeResponse.getFailedPortlets().addAll(failedPortlets);
    if (portletLifetimes != null)
      setLifetimeResponse.getUpdatedPortlet().addAll(portletLifetimes);

    return setLifetimeResponse;
  }

  public PortletDescriptionResponse getPortletDescription(RegistrationContext registrationContext,
                                                          PortletContext portletContext,
                                                          UserContext userContext,
                                                          List<String> desiredLocales) throws OperationNotSupported,
                                                                                      AccessDenied,
                                                                                      ResourceSuspended,
                                                                                      InvalidRegistration,
                                                                                      InvalidHandle,
                                                                                      InvalidUserCategory,
                                                                                      ModifyRegistrationRequired,
                                                                                      MissingParameters,
                                                                                      InconsistentParameters,
                                                                                      OperationFailed,
                                                                                      WSRPException {

    if (registrationContext == null) {
      throw new InvalidRegistration("registrationContext is null");
    }
    if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
      LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
      LifetimeVerifier.checkPortletLifetime(registrationContext, portletContext, userContext);
    }
    log.debug("Get portlet description for registration handle "
        + registrationContext.getRegistrationHandle());

    String portletHandle = portletContext.getPortletHandle();

//    try {
//      if (!stateManager.isConsumerConfiguredPortlet(portletHandle, registrationContext)) {
//        log.debug("This portlet handle " + portletHandle
//            + " is not valid in the scope of that registration ");
//        throw new AccessDenied();
//      }
//    } catch (WSRPException e) {
//      throw new WSRPException();
//    }

    PortletDescription pD = proxy.getPortletDescription(portletHandle,
                                                        desiredLocales.toArray(new String[desiredLocales.size()]));
    ResourceList resourceList = proxy.getResourceList(desiredLocales.toArray(new String[desiredLocales.size()]));

    PortletDescriptionResponse response = new PortletDescriptionResponse();
    response.setPortletDescription(pD);
    response.setResourceList(resourceList);

    return response;
  }

  public PortletContext setPortletProperties(RegistrationContext registrationContext,
                                             PortletContext portletContext,
                                             UserContext userContext,
                                             PropertyList propertyList) throws OperationNotSupported,
                                                                       AccessDenied,
                                                                       ResourceSuspended,
                                                                       InvalidRegistration,
                                                                       InvalidHandle,
                                                                       InvalidUserCategory,
                                                                       ModifyRegistrationRequired,
                                                                       MissingParameters,
                                                                       InconsistentParameters,
                                                                       OperationFailed,
                                                                       WSRPException {

    if (registrationContext == null) {
      throw new InvalidRegistration("registrationContext is null");
    }
    if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
      LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
      LifetimeVerifier.checkPortletLifetime(registrationContext, portletContext, userContext);
    }
    log.debug("Set portlet properties for registration handle "
        + registrationContext.getRegistrationHandle());

    String portletHandle = portletContext.getPortletHandle();

    if (!stateManager.isConsumerConfiguredPortlet(portletHandle, registrationContext)) {
      log.debug("This portlet handle " + portletHandle
          + " is not valid in the scope of that registration ");
      throw new AccessDenied();
    }

    String userID = userContext != null ? userContext.getUserContextKey() : null;

    // manage portlet state
    byte[] portletState = managePortletState(portletContext);

    proxy.setPortletProperties(portletHandle, userID, propertyList, portletState);

    return portletContext;
  }

  public PropertyList getPortletProperties(RegistrationContext registrationContext,
                                           PortletContext portletContext,
                                           UserContext userContext,
                                           List<String> names) throws OperationNotSupported,
                                                              AccessDenied,
                                                              ResourceSuspended,
                                                              InvalidRegistration,
                                                              InvalidHandle,
                                                              InvalidUserCategory,
                                                              ModifyRegistrationRequired,
                                                              MissingParameters,
                                                              InconsistentParameters,
                                                              OperationFailed,
                                                              WSRPException {
    if (registrationContext == null) {
      throw new InvalidRegistration("registrationContext is null");
    }
    if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
      LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
      LifetimeVerifier.checkPortletLifetime(registrationContext, portletContext, userContext);
    }
    log.debug("get portlet properties for registration handle "
        + registrationContext.getRegistrationHandle());

//    String portletHandle = portletContext.getPortletHandle();

//    try {
//      if (!stateManager.isConsumerConfiguredPortlet(portletHandle, registrationContext)) {
//        log.debug("This portlet handle " + portletHandle
//            + " is not valid in the scope of that registration ");
//        throw new AccessDenied();
//      }
//    } catch (WSRPException e) {
//      throw new WSRPException();
//    }

    PropertyList list = getPortletPropertiesWithFlagParameter(portletContext,
                                                              userContext,
                                                              names,
                                                              false);

    return list;
  }

  private PropertyList getPortletPropertiesWithFlagParameter(PortletContext portletContext,
                                                             UserContext userContext,
                                                             List<String> names,
                                                             boolean readWriteOnly) throws WSRPException {
    Map<String, String[]> properties = null;

    String userID = userContext != null ? userContext.getUserContextKey() : null;

    // manage portlet state
    byte[] portletState = managePortletState(portletContext);

    properties = proxy.getPortletProperties(portletContext.getPortletHandle(),
                                            userID,
                                            portletState,
                                            readWriteOnly);

    Collection<Property> properties2return = null;
    Set<String> keys = properties.keySet();
    for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
      String key = (String) iterator.next();

      if (names == null || names.size() == 0 || names.contains(key)) {

        String[] values = (String[]) properties.get(key);
        Property prop = new Property();
        prop.setName(new QName(key));
        prop.setType(new QName(values[0].getClass().getCanonicalName()));
        prop.setStringValue(values[0]);
        if (properties2return == null)
          properties2return = new ArrayList<Property>();
        properties2return.add(prop);
      }
    }

    PropertyList list = new PropertyList();
    if (properties2return != null)
      list.getProperties().addAll(properties2return);
    return list;
  }

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(RegistrationContext registrationContext,
                                                                          PortletContext portletContext,
                                                                          UserContext userContext,
                                                                          List<String> desiredLocales) throws OperationNotSupported,
                                                                                                      AccessDenied,
                                                                                                      ResourceSuspended,
                                                                                                      InvalidRegistration,
                                                                                                      InvalidHandle,
                                                                                                      InvalidUserCategory,
                                                                                                      ModifyRegistrationRequired,
                                                                                                      MissingParameters,
                                                                                                      InconsistentParameters,
                                                                                                      OperationFailed,
                                                                                                      WSRPException {
    if (registrationContext == null) {
      throw new InvalidRegistration("registrationContext is null");
    }
    if (RegistrationVerifier.checkRegistrationContext(registrationContext)) {
      LifetimeVerifier.checkRegistrationLifetime(registrationContext, userContext);
      LifetimeVerifier.checkPortletLifetime(registrationContext, portletContext, userContext);
    }

    String registrationHandle = registrationContext.getRegistrationHandle();

    PropertyList list = getPortletPropertiesWithFlagParameter(portletContext,
                                                              userContext,
                                                              null,
                                                              false);

    Collection<Property> properties2return = list.getProperties();

    Collection<PropertyDescription> propertyDescriptionList = new ArrayList<PropertyDescription>();

    for (Property property : properties2return) {
      PropertyDescription propertyDescription = new PropertyDescription();
      propertyDescription.setName(property.getName());
      propertyDescription.setType(property.getType());
      propertyDescriptionList.add(propertyDescription);
    }

    ModelDescription modelDescription = new ModelDescription();
    modelDescription.getPropertyDescriptions().addAll(propertyDescriptionList);
    ResourceList resourceList = new ResourceList();
    //    resourceList.getResources().add(c);

    PortletPropertyDescriptionResponse portletPropertyDescriptionResponse = new PortletPropertyDescriptionResponse();
    portletPropertyDescriptionResponse.setModelDescription(modelDescription);
    portletPropertyDescriptionResponse.setResourceList(resourceList);
    return portletPropertyDescriptionResponse;
  }

  private String createNewPortletHandle(String portletHandle) {
    String[] keys = StringUtils.split(portletHandle, Constants.PORTLET_HANDLE_ENCODER);
    String newPortletHandle = keys[0] + Constants.PORTLET_HANDLE_ENCODER + keys[1]
        + Constants.PORTLET_HANDLE_ENCODER + IdentifierUtil.generateUUID(portletHandle);
    return newPortletHandle;
  }

  private byte[] managePortletState(PortletContext portletContext) {
    // default is "wsrp.save.portlet.state.on.consumer"="false"
    if (conf.isSavePortletStateOnConsumer()) {
      log.debug("Save state on consumer");
      return portletContext.getPortletState();
    }
    log.debug("Save state on producer");
    return null;
  }

  private PortletContext processCopyPortletHandle(RegistrationContext toRegistrationContext,
                                                  UserContext toUserContext,
                                                  PortletContext toPortletContext,
                                                  RegistrationContext fromRegistrationContext,
                                                  UserContext fromUserContext,
                                                  PortletContext fromPortletContext) throws OperationNotSupported,
                                                                                    AccessDenied,
                                                                                    ResourceSuspended,
                                                                                    InvalidRegistration,
                                                                                    InvalidHandle,
                                                                                    InvalidUserCategory,
                                                                                    ModifyRegistrationRequired,
                                                                                    MissingParameters,
                                                                                    InconsistentParameters,
                                                                                    OperationFailed,
                                                                                    WSRPException {
    String fromPortletHandle = fromPortletContext.getPortletHandle();
    String toPortletHandle = toPortletContext.getPortletHandle();

    try {
      // is Consumer Configured Portlet
      if (stateManager.isConsumerConfiguredPortlet(fromPortletHandle, fromRegistrationContext)) {
        if (log.isDebugEnabled())
          log.debug("processCopyPortletHandle a Consumer Configured Portlet with handle : "
              + fromPortletHandle);
        // add toPortletHandle as a Consumer Configured Portlet
        stateManager.addConsumerConfiguredPortletHandle(toPortletHandle, toRegistrationContext);
      } else {
        // is Producer Offered Portlet
        if (log.isDebugEnabled())
          log.debug("processCopyPortletHandle a Producer Offered Portlet with handle : "
              + fromPortletHandle);
        if (!proxy.isPortletOffered(fromPortletHandle)) {
          log.debug("The portlet handle is not offered by the Producer");
          throw new InvalidHandle();
        }
        // add toPortletHandle as a Consumer Configured Portlet
        stateManager.addConsumerConfiguredPortletHandle(toPortletHandle, toRegistrationContext);
      }

      // set PortletProperties for toPortletHandle
      processCopyPortletProperties(toRegistrationContext,
                                   toUserContext,
                                   toPortletContext,
                                   fromRegistrationContext,
                                   fromUserContext,
                                   fromPortletContext);

      processCopyPortletsLifetime(toPortletContext, fromPortletContext);

    } catch (WSRPException e) {
      throw new WSRPException("Can not clone portlet", e);
    }
    return toPortletContext;
  }

  private void processCopyPortletsLifetime(PortletContext toPortletContext,
                                           PortletContext fromPortletContext) throws WSRPException {
    String fromPortletHandle = fromPortletContext.getPortletHandle();
    String toPortletHandle = toPortletContext.getPortletHandle();

    Lifetime fromPortletLifetime = getPortletLifetimePrivate(fromPortletHandle);
    if (fromPortletLifetime != null) {
      stateManager.putPortletLifetime(toPortletHandle, fromPortletLifetime);
    }
  }

  private void processCopyPortletProperties(RegistrationContext toRegistrationContext,
                                            UserContext toUserContext,
                                            PortletContext toPortletContext,
                                            RegistrationContext fromRegistrationContext,
                                            UserContext fromUserContext,
                                            PortletContext fromPortletContext) throws OperationNotSupported,
                                                                              AccessDenied,
                                                                              ResourceSuspended,
                                                                              InvalidRegistration,
                                                                              InvalidHandle,
                                                                              InvalidUserCategory,
                                                                              ModifyRegistrationRequired,
                                                                              MissingParameters,
                                                                              InconsistentParameters,
                                                                              OperationFailed,
                                                                              WSRPException {

    if (!stateManager.isConsumerConfiguredPortlet(fromPortletContext.getPortletHandle(),
                                                  fromRegistrationContext)) {
      // we shoulnd't copy default portlet preferences to the cloned portlet  
      return;
    }

    PropertyList list = getPortletPropertiesWithFlagParameter(fromPortletContext,
                                                              fromUserContext,
                                                              null,
                                                              true);

    setPortletProperties(toRegistrationContext, toPortletContext, toUserContext, list);

  }

}
