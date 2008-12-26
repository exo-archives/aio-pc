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
package org.exoplatform.services.wsrp2.consumer.adapters.ports1;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp.WSRPTypesTransformer;
import org.exoplatform.services.wsrp1.intf.WS1AccessDenied;
import org.exoplatform.services.wsrp1.intf.WS1InconsistentParameters;
import org.exoplatform.services.wsrp1.intf.WS1InvalidHandle;
import org.exoplatform.services.wsrp1.intf.WS1InvalidRegistration;
import org.exoplatform.services.wsrp1.intf.WS1InvalidUserCategory;
import org.exoplatform.services.wsrp1.intf.WS1MissingParameters;
import org.exoplatform.services.wsrp1.intf.WS1OperationFailed;
import org.exoplatform.services.wsrp1.intf.WSRPV1PortletManagementPortType;
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
import org.exoplatform.services.wsrp2.type.ClonePortlet;
import org.exoplatform.services.wsrp2.type.CopyPortlets;
import org.exoplatform.services.wsrp2.type.CopyPortletsResponse;
import org.exoplatform.services.wsrp2.type.DestroyPortlets;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.ExportPortlets;
import org.exoplatform.services.wsrp2.type.ExportPortletsResponse;
import org.exoplatform.services.wsrp2.type.GetPortletDescription;
import org.exoplatform.services.wsrp2.type.GetPortletProperties;
import org.exoplatform.services.wsrp2.type.GetPortletPropertyDescription;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetime;
import org.exoplatform.services.wsrp2.type.GetPortletsLifetimeResponse;
import org.exoplatform.services.wsrp2.type.ImportPortlets;
import org.exoplatform.services.wsrp2.type.ImportPortletsResponse;
import org.exoplatform.services.wsrp2.type.Lifetime;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.ReleaseExport;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.SetExportLifetime;
import org.exoplatform.services.wsrp2.type.SetPortletProperties;
import org.exoplatform.services.wsrp2.type.SetPortletsLifetime;
import org.exoplatform.services.wsrp2.type.SetPortletsLifetimeResponse;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 25, 2008
 */
public class WSRPV1PortletManagementPortTypeAdapter {

  private WSRPV1PortletManagementPortType portletManagementPort;

  private static final Log                LOG = ExoLogger.getLogger(WSRPV1PortletManagementPortTypeAdapter.class);

  public WSRPV1PortletManagementPortTypeAdapter(WSRPV1PortletManagementPortType portletManagementPort) {
    this.portletManagementPort = portletManagementPort;
  }

  public PortletDescriptionResponse getPortletDescription(GetPortletDescription getPortletDescription) throws OperationNotSupported,
                                                                                                      AccessDenied,
                                                                                                      ResourceSuspended,
                                                                                                      InvalidRegistration,
                                                                                                      InvalidHandle,
                                                                                                      InvalidUserCategory,
                                                                                                      ModifyRegistrationRequired,
                                                                                                      MissingParameters,
                                                                                                      InconsistentParameters,
                                                                                                      OperationFailed {

    System.out.println("Invoking getPortletDescription...");
    org.exoplatform.services.wsrp1.type.WS1RegistrationContext _getPortletDescription_registrationContext = WSRPTypesTransformer.getWS1RegistrationContext(getPortletDescription.getRegistrationContext());
    org.exoplatform.services.wsrp1.type.WS1PortletContext _getPortletDescription_portletContext = WSRPTypesTransformer.getWS1PortletContext(getPortletDescription.getPortletContext());
    org.exoplatform.services.wsrp1.type.WS1UserContext _getPortletDescription_userContext = WSRPTypesTransformer.getWS1UserContext(getPortletDescription.getUserContext());
    java.util.List<java.lang.String> _getPortletDescription_desiredLocales = getPortletDescription.getDesiredLocales();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1PortletDescription> _getPortletDescription_portletDescription = new javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1PortletDescription>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1ResourceList> _getPortletDescription_resourceList = new javax.xml.ws.Holder<org.exoplatform.services.wsrp1.type.WS1ResourceList>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>> _getPortletDescription_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp1.type.WS1Extension>>();
    try {
      portletManagementPort.getPortletDescription(_getPortletDescription_registrationContext,
                                                  _getPortletDescription_portletContext,
                                                  _getPortletDescription_userContext,
                                                  _getPortletDescription_desiredLocales,
                                                  _getPortletDescription_portletDescription,
                                                  _getPortletDescription_resourceList,
                                                  _getPortletDescription_extensions);

      System.out.println("getPortletDescription._getPortletDescription_portletDescription="
          + _getPortletDescription_portletDescription.value);
      System.out.println("getPortletDescription._getPortletDescription_resourceList="
          + _getPortletDescription_resourceList.value);
      System.out.println("getPortletDescription._getPortletDescription_extensions="
          + _getPortletDescription_extensions.value);
    } catch (WS1InvalidRegistration e) {
      System.out.println("Expected exception: InvalidRegistration has occurred.");
      System.out.println(e.toString());
    } catch (WS1MissingParameters e) {
      System.out.println("Expected exception: MissingParameters has occurred.");
      System.out.println(e.toString());
    } catch (WS1InvalidUserCategory e) {
      System.out.println("Expected exception: InvalidUserCategory has occurred.");
      System.out.println(e.toString());
    } catch (WS1AccessDenied e) {
      System.out.println("Expected exception: AccessDenied has occurred.");
      System.out.println(e.toString());
    } catch (WS1InvalidHandle e) {
      System.out.println("Expected exception: InvalidHandle has occurred.");
      System.out.println(e.toString());
    } catch (WS1InconsistentParameters e) {
      System.out.println("Expected exception: InconsistentParameters has occurred.");
      System.out.println(e.toString());
    } catch (WS1OperationFailed e) {
      System.out.println("Expected exception: OperationFailed has occurred.");
      System.out.println(e.toString());
    }

    PortletDescriptionResponse response = new PortletDescriptionResponse();
    response.setPortletDescription(WSRPTypesTransformer.getWS2PortletDescription(_getPortletDescription_portletDescription.value));
    response.setResourceList(WSRPTypesTransformer.getWS2ResourceList(_getPortletDescription_resourceList.value));
    if (_getPortletDescription_extensions.value != null)
      response.getExtensions().addAll(WSRPTypesTransformer.getWS2Extensions(_getPortletDescription_extensions.value));
    return response;

  }

  public PortletContext clonePortlet(ClonePortlet clonePortlet) throws OperationNotSupported,
                                                               AccessDenied,
                                                               ResourceSuspended,
                                                               InvalidRegistration,
                                                               InvalidHandle,
                                                               InvalidUserCategory,
                                                               ModifyRegistrationRequired,
                                                               MissingParameters,
                                                               InconsistentParameters,
                                                               OperationFailed {
    System.out.println("Invoking clonePortlet...");
    org.exoplatform.services.wsrp2.type.RegistrationContext _clonePortlet_registrationContext = clonePortlet.getRegistrationContext();
    org.exoplatform.services.wsrp2.type.PortletContext _clonePortlet_portletContext = clonePortlet.getPortletContext();
    org.exoplatform.services.wsrp2.type.UserContext _clonePortlet_userContext = clonePortlet.getUserContext();
    org.exoplatform.services.wsrp2.type.Lifetime _clonePortlet_lifetime = clonePortlet.getLifetime();
    javax.xml.ws.Holder<java.lang.String> _clonePortlet_portletHandle = new javax.xml.ws.Holder<java.lang.String>();
    javax.xml.ws.Holder<byte[]> _clonePortlet_portletState = new javax.xml.ws.Holder<byte[]>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime> _clonePortlet_scheduledDestruction = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _clonePortlet_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

    portletManagementPort.clonePortlet(_clonePortlet_registrationContext,
                                       _clonePortlet_portletContext,
                                       _clonePortlet_userContext,
                                       _clonePortlet_lifetime,
                                       _clonePortlet_portletHandle,
                                       _clonePortlet_portletState,
                                       _clonePortlet_scheduledDestruction,
                                       _clonePortlet_extensions);

    System.out.println("clonePortlet._clonePortlet_portletHandle="
        + _clonePortlet_portletHandle.value);
    System.out.println("clonePortlet._clonePortlet_portletState="
        + _clonePortlet_portletState.value);
    System.out.println("clonePortlet._clonePortlet_scheduledDestruction="
        + _clonePortlet_scheduledDestruction.value);
    System.out.println("clonePortlet._clonePortlet_extensions=" + _clonePortlet_extensions.value);

    PortletContext response = new PortletContext();
    response.setPortletHandle(_clonePortlet_portletHandle.value);
    response.setPortletState(_clonePortlet_portletState.value);
    response.setScheduledDestruction(_clonePortlet_scheduledDestruction.value);
    if (_clonePortlet_extensions.value != null)
      response.getExtensions().addAll(_clonePortlet_extensions.value);
    return response;

  }

  public DestroyPortletsResponse destroyPortlets(DestroyPortlets destroyPortlets) throws OperationNotSupported,
                                                                                 ResourceSuspended,
                                                                                 InvalidRegistration,
                                                                                 ModifyRegistrationRequired,
                                                                                 MissingParameters,
                                                                                 InconsistentParameters,
                                                                                 OperationFailed {

    System.out.println("Invoking destroyPortlets...");
    org.exoplatform.services.wsrp2.type.RegistrationContext _destroyPortlets_registrationContext = destroyPortlets.getRegistrationContext();
    java.util.List<java.lang.String> _destroyPortlets_portletHandles = destroyPortlets.getPortletHandles();
    org.exoplatform.services.wsrp2.type.UserContext _destroyPortlets_userContext = destroyPortlets.getUserContext();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets>> _destroyPortlets_failedPortlets = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.FailedPortlets>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _destroyPortlets_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

    portletManagementPort.destroyPortlets(_destroyPortlets_registrationContext,
                                          _destroyPortlets_portletHandles,
                                          _destroyPortlets_userContext,
                                          _destroyPortlets_failedPortlets,
                                          _destroyPortlets_extensions);

    System.out.println("destroyPortlets._destroyPortlets_failedPortlets="
        + _destroyPortlets_failedPortlets.value);
    System.out.println("destroyPortlets._destroyPortlets_extensions="
        + _destroyPortlets_extensions.value);

    DestroyPortletsResponse response = new DestroyPortletsResponse();
    if (_destroyPortlets_failedPortlets.value != null)
      response.getFailedPortlets().addAll(_destroyPortlets_failedPortlets.value);
    if (_destroyPortlets_extensions.value != null)
      response.getExtensions().addAll(_destroyPortlets_extensions.value);
    return response;

  }

  public PortletContext setPortletProperties(SetPortletProperties setPortletProperties) throws OperationNotSupported,
                                                                                       AccessDenied,
                                                                                       ResourceSuspended,
                                                                                       InvalidRegistration,
                                                                                       InvalidHandle,
                                                                                       InvalidUserCategory,
                                                                                       ModifyRegistrationRequired,
                                                                                       MissingParameters,
                                                                                       InconsistentParameters,
                                                                                       OperationFailed {

    System.out.println("Invoking setPortletProperties...");
    org.exoplatform.services.wsrp2.type.RegistrationContext _setPortletProperties_registrationContext = setPortletProperties.getRegistrationContext();
    org.exoplatform.services.wsrp2.type.PortletContext _setPortletProperties_portletContext = setPortletProperties.getPortletContext();
    org.exoplatform.services.wsrp2.type.UserContext _setPortletProperties_userContext = setPortletProperties.getUserContext();
    org.exoplatform.services.wsrp2.type.PropertyList _setPortletProperties_propertyList = setPortletProperties.getPropertyList();
    javax.xml.ws.Holder<java.lang.String> _setPortletProperties_portletHandle = new javax.xml.ws.Holder<java.lang.String>();
    javax.xml.ws.Holder<byte[]> _setPortletProperties_portletState = new javax.xml.ws.Holder<byte[]>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime> _setPortletProperties_scheduledDestruction = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.Lifetime>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _setPortletProperties_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

    portletManagementPort.setPortletProperties(_setPortletProperties_registrationContext,
                                               _setPortletProperties_portletContext,
                                               _setPortletProperties_userContext,
                                               _setPortletProperties_propertyList,
                                               _setPortletProperties_portletHandle,
                                               _setPortletProperties_portletState,
                                               _setPortletProperties_scheduledDestruction,
                                               _setPortletProperties_extensions);

    System.out.println("setPortletProperties._setPortletProperties_portletHandle="
        + _setPortletProperties_portletHandle.value);
    System.out.println("setPortletProperties._setPortletProperties_portletState="
        + _setPortletProperties_portletState.value);
    System.out.println("setPortletProperties._setPortletProperties_scheduledDestruction="
        + _setPortletProperties_scheduledDestruction.value);
    System.out.println("setPortletProperties._setPortletProperties_extensions="
        + _setPortletProperties_extensions.value);

    PortletContext response = new PortletContext();
    response.setPortletHandle(_setPortletProperties_portletHandle.value);
    response.setPortletState(_setPortletProperties_portletState.value);
    response.setScheduledDestruction(_setPortletProperties_scheduledDestruction.value);
    if (_setPortletProperties_extensions.value != null)
      response.getExtensions().addAll(_setPortletProperties_extensions.value);
    return response;

  }

  public PropertyList getPortletProperties(GetPortletProperties getPortletProperties) throws OperationNotSupported,
                                                                                     AccessDenied,
                                                                                     ResourceSuspended,
                                                                                     InvalidRegistration,
                                                                                     InvalidHandle,
                                                                                     InvalidUserCategory,
                                                                                     ModifyRegistrationRequired,
                                                                                     MissingParameters,
                                                                                     InconsistentParameters,
                                                                                     OperationFailed {
    System.out.println("Invoking getPortletProperties...");
    org.exoplatform.services.wsrp2.type.RegistrationContext _getPortletProperties_registrationContext = getPortletProperties.getRegistrationContext();
    org.exoplatform.services.wsrp2.type.PortletContext _getPortletProperties_portletContext = getPortletProperties.getPortletContext();
    org.exoplatform.services.wsrp2.type.UserContext _getPortletProperties_userContext = getPortletProperties.getUserContext();
    java.util.List<java.lang.String> _getPortletProperties_names = getPortletProperties.getNames();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Property>> _getPortletProperties_properties = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Property>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ResetProperty>> _getPortletProperties_resetProperties = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.ResetProperty>>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _getPortletProperties_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

    portletManagementPort.getPortletProperties(_getPortletProperties_registrationContext,
                                               _getPortletProperties_portletContext,
                                               _getPortletProperties_userContext,
                                               _getPortletProperties_names,
                                               _getPortletProperties_properties,
                                               _getPortletProperties_resetProperties,
                                               _getPortletProperties_extensions);

    System.out.println("getPortletProperties._getPortletProperties_properties="
        + _getPortletProperties_properties.value);
    System.out.println("getPortletProperties._getPortletProperties_resetProperties="
        + _getPortletProperties_resetProperties.value);
    System.out.println("getPortletProperties._getPortletProperties_extensions="
        + _getPortletProperties_extensions.value);

    PropertyList response = new PropertyList();
    if (_getPortletProperties_properties.value != null)
      response.getProperties().addAll(_getPortletProperties_properties.value);
    if (_getPortletProperties_resetProperties.value != null)
      response.getResetProperties().addAll(_getPortletProperties_resetProperties.value);
    if (_getPortletProperties_extensions.value != null)
      response.getExtensions().addAll(_getPortletProperties_extensions.value);
    return response;

  }

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(GetPortletPropertyDescription getPortletPropertyDescription) throws OperationNotSupported,
                                                                                                                                      AccessDenied,
                                                                                                                                      ResourceSuspended,
                                                                                                                                      InvalidRegistration,
                                                                                                                                      InvalidHandle,
                                                                                                                                      InvalidUserCategory,
                                                                                                                                      ModifyRegistrationRequired,
                                                                                                                                      MissingParameters,
                                                                                                                                      InconsistentParameters,
                                                                                                                                      OperationFailed {

    System.out.println("Invoking getPortletPropertyDescription...");
    org.exoplatform.services.wsrp2.type.RegistrationContext _getPortletPropertyDescription_registrationContext = getPortletPropertyDescription.getRegistrationContext();
    org.exoplatform.services.wsrp2.type.PortletContext _getPortletPropertyDescription_portletContext = getPortletPropertyDescription.getPortletContext();
    org.exoplatform.services.wsrp2.type.UserContext _getPortletPropertyDescription_userContext = getPortletPropertyDescription.getUserContext();
    java.util.List<java.lang.String> _getPortletPropertyDescription_desiredLocales = getPortletPropertyDescription.getDesiredLocales();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ModelDescription> _getPortletPropertyDescription_modelDescription = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ModelDescription>();
    javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList> _getPortletPropertyDescription_resourceList = new javax.xml.ws.Holder<org.exoplatform.services.wsrp2.type.ResourceList>();
    javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>> _getPortletPropertyDescription_extensions = new javax.xml.ws.Holder<java.util.List<org.exoplatform.services.wsrp2.type.Extension>>();

    portletManagementPort.getPortletPropertyDescription(_getPortletPropertyDescription_registrationContext,
                                                        _getPortletPropertyDescription_portletContext,
                                                        _getPortletPropertyDescription_userContext,
                                                        _getPortletPropertyDescription_desiredLocales,
                                                        _getPortletPropertyDescription_modelDescription,
                                                        _getPortletPropertyDescription_resourceList,
                                                        _getPortletPropertyDescription_extensions);

    System.out.println("getPortletPropertyDescription._getPortletPropertyDescription_modelDescription="
        + _getPortletPropertyDescription_modelDescription.value);
    System.out.println("getPortletPropertyDescription._getPortletPropertyDescription_resourceList="
        + _getPortletPropertyDescription_resourceList.value);
    System.out.println("getPortletPropertyDescription._getPortletPropertyDescription_extensions="
        + _getPortletPropertyDescription_extensions.value);

    PortletPropertyDescriptionResponse response = new PortletPropertyDescriptionResponse();
    response.setModelDescription(_getPortletPropertyDescription_modelDescription.value);
    response.setResourceList(_getPortletPropertyDescription_resourceList.value);
    if (_getPortletPropertyDescription_extensions.value != null)
      response.getExtensions().addAll(_getPortletPropertyDescription_extensions.value);
    return response;

  }

  public CopyPortletsResponse copyPortlets(CopyPortlets copyPortlets) throws OperationNotSupported,
                                                                     AccessDenied,
                                                                     ResourceSuspended,
                                                                     InvalidRegistration,
                                                                     InvalidHandle,
                                                                     InvalidUserCategory,
                                                                     ModifyRegistrationRequired,
                                                                     MissingParameters,
                                                                     InconsistentParameters,
                                                                     OperationFailed {

    System.out.println("Invoking copyPortlets...");
    // wsrp1 doesn't have this operation
    return null;
  }

  public ExportPortletsResponse exportPortlets(ExportPortlets exportPortlets) throws OperationNotSupported,
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

    System.out.println("Invoking exportPortlets...");
    // wsrp1 doesn't have this operation
    return null;

  }

  public ImportPortletsResponse importPortlets(ImportPortlets importPortlets) throws OperationNotSupported,
                                                                             ExportNoLongerValid,
                                                                             AccessDenied,
                                                                             ResourceSuspended,
                                                                             InvalidRegistration,
                                                                             InvalidUserCategory,
                                                                             ModifyRegistrationRequired,
                                                                             MissingParameters,
                                                                             InconsistentParameters,
                                                                             OperationFailed {

    System.out.println("Invoking importPortlets...");
    // wsrp1 doesn't have this operation
    return null;
  }

  public ReturnAny releaseExport(ReleaseExport releaseExport) {
    // wsrp1 doesn't have this operation
    return null;
  }

  public Lifetime setExportLifetime(SetExportLifetime setExportLifetime) throws OperationNotSupported,
                                                                        AccessDenied,
                                                                        ResourceSuspended,
                                                                        InvalidRegistration,
                                                                        InvalidHandle,
                                                                        ModifyRegistrationRequired,
                                                                        OperationFailed {
    // wsrp1 doesn't have this operation
    return null;
  }

  public GetPortletsLifetimeResponse getPortletsLifetime(GetPortletsLifetime getPortletsLifetime) throws OperationNotSupported,
                                                                                                 AccessDenied,
                                                                                                 ResourceSuspended,
                                                                                                 InvalidRegistration,
                                                                                                 InvalidHandle,
                                                                                                 ModifyRegistrationRequired,
                                                                                                 InconsistentParameters,
                                                                                                 OperationFailed {
    System.out.println("Invoking getPortletsLifetime...");
    // wsrp1 doesn't have this operation
    return null;
  }

  public SetPortletsLifetimeResponse setPortletsLifetime(SetPortletsLifetime setPortletsLifetime) throws OperationNotSupported,
                                                                                                 AccessDenied,
                                                                                                 ResourceSuspended,
                                                                                                 InvalidRegistration,
                                                                                                 InvalidHandle,
                                                                                                 ModifyRegistrationRequired,
                                                                                                 InconsistentParameters,
                                                                                                 OperationFailed {

    System.out.println("Invoking setPortletsLifetime...");
    // wsrp1 doesn't have this operation
    return null;

  }

}
