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

package org.exoplatform.services.wsrp2.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.exoplatform.services.wsrp1.type.WS1BlockingInteractionResponse;
import org.exoplatform.services.wsrp1.type.WS1CacheControl;
import org.exoplatform.services.wsrp1.type.WS1ClientData;
import org.exoplatform.services.wsrp1.type.WS1ClonePortlet;
import org.exoplatform.services.wsrp1.type.WS1Contact;
import org.exoplatform.services.wsrp1.type.WS1CookieProtocol;
import org.exoplatform.services.wsrp1.type.WS1DestroyFailed;
import org.exoplatform.services.wsrp1.type.WS1DestroyPortlets;
import org.exoplatform.services.wsrp1.type.WS1DestroyPortletsResponse;
import org.exoplatform.services.wsrp1.type.WS1EmployerInfo;
import org.exoplatform.services.wsrp1.type.WS1Extension;
import org.exoplatform.services.wsrp1.type.WS1GetMarkup;
import org.exoplatform.services.wsrp1.type.WS1GetPortletProperties;
import org.exoplatform.services.wsrp1.type.WS1GetServiceDescription;
import org.exoplatform.services.wsrp1.type.WS1InteractionParams;
import org.exoplatform.services.wsrp1.type.WS1ItemDescription;
import org.exoplatform.services.wsrp1.type.WS1LocalizedString;
import org.exoplatform.services.wsrp1.type.WS1MarkupContext;
import org.exoplatform.services.wsrp1.type.WS1MarkupParams;
import org.exoplatform.services.wsrp1.type.WS1MarkupResponse;
import org.exoplatform.services.wsrp1.type.WS1MarkupType;
import org.exoplatform.services.wsrp1.type.WS1ModelDescription;
import org.exoplatform.services.wsrp1.type.WS1ModelTypes;
import org.exoplatform.services.wsrp1.type.WS1ModifyRegistration;
import org.exoplatform.services.wsrp1.type.WS1NamedString;
import org.exoplatform.services.wsrp1.type.WS1Online;
import org.exoplatform.services.wsrp1.type.WS1PerformBlockingInteraction;
import org.exoplatform.services.wsrp1.type.WS1PersonName;
import org.exoplatform.services.wsrp1.type.WS1PortletContext;
import org.exoplatform.services.wsrp1.type.WS1PortletDescription;
import org.exoplatform.services.wsrp1.type.WS1Postal;
import org.exoplatform.services.wsrp1.type.WS1Property;
import org.exoplatform.services.wsrp1.type.WS1PropertyDescription;
import org.exoplatform.services.wsrp1.type.WS1PropertyList;
import org.exoplatform.services.wsrp1.type.WS1RegistrationContext;
import org.exoplatform.services.wsrp1.type.WS1RegistrationData;
import org.exoplatform.services.wsrp1.type.WS1RegistrationState;
import org.exoplatform.services.wsrp1.type.WS1ReleaseSessions;
import org.exoplatform.services.wsrp1.type.WS1ResetProperty;
import org.exoplatform.services.wsrp1.type.WS1Resource;
import org.exoplatform.services.wsrp1.type.WS1ResourceList;
import org.exoplatform.services.wsrp1.type.WS1ResourceValue;
import org.exoplatform.services.wsrp1.type.WS1RuntimeContext;
import org.exoplatform.services.wsrp1.type.WS1ServiceDescription;
import org.exoplatform.services.wsrp1.type.WS1SessionContext;
import org.exoplatform.services.wsrp1.type.WS1SetPortletProperties;
import org.exoplatform.services.wsrp1.type.WS1StateChange;
import org.exoplatform.services.wsrp1.type.WS1Telecom;
import org.exoplatform.services.wsrp1.type.WS1TelephoneNum;
import org.exoplatform.services.wsrp1.type.WS1Templates;
import org.exoplatform.services.wsrp1.type.WS1UpdateResponse;
import org.exoplatform.services.wsrp1.type.WS1UploadContext;
import org.exoplatform.services.wsrp1.type.WS1UserContext;
import org.exoplatform.services.wsrp1.type.WS1UserProfile;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.CacheControl;
import org.exoplatform.services.wsrp2.type.ClientData;
import org.exoplatform.services.wsrp2.type.ClonePortlet;
import org.exoplatform.services.wsrp2.type.Contact;
import org.exoplatform.services.wsrp2.type.CookieProtocol;
import org.exoplatform.services.wsrp2.type.DestroyPortlets;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.EmployerInfo;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.FailedPortlets;
import org.exoplatform.services.wsrp2.type.GetMarkup;
import org.exoplatform.services.wsrp2.type.GetPortletProperties;
import org.exoplatform.services.wsrp2.type.GetServiceDescription;
import org.exoplatform.services.wsrp2.type.InteractionParams;
import org.exoplatform.services.wsrp2.type.ItemDescription;
import org.exoplatform.services.wsrp2.type.LocalizedString;
import org.exoplatform.services.wsrp2.type.MarkupContext;
import org.exoplatform.services.wsrp2.type.MarkupParams;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.MarkupType;
import org.exoplatform.services.wsrp2.type.ModelDescription;
import org.exoplatform.services.wsrp2.type.ModelTypes;
import org.exoplatform.services.wsrp2.type.ModifyRegistration;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.NavigationalContext;
import org.exoplatform.services.wsrp2.type.Online;
import org.exoplatform.services.wsrp2.type.PerformBlockingInteraction;
import org.exoplatform.services.wsrp2.type.PersonName;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.Postal;
import org.exoplatform.services.wsrp2.type.Property;
import org.exoplatform.services.wsrp2.type.PropertyDescription;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.ReleaseSessions;
import org.exoplatform.services.wsrp2.type.ResetProperty;
import org.exoplatform.services.wsrp2.type.Resource;
import org.exoplatform.services.wsrp2.type.ResourceList;
import org.exoplatform.services.wsrp2.type.ResourceValue;
import org.exoplatform.services.wsrp2.type.RuntimeContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.SessionContext;
import org.exoplatform.services.wsrp2.type.SessionParams;
import org.exoplatform.services.wsrp2.type.SetPortletProperties;
import org.exoplatform.services.wsrp2.type.StateChange;
import org.exoplatform.services.wsrp2.type.Telecom;
import org.exoplatform.services.wsrp2.type.TelephoneNum;
import org.exoplatform.services.wsrp2.type.Templates;
import org.exoplatform.services.wsrp2.type.UpdateResponse;
import org.exoplatform.services.wsrp2.type.UploadContext;
import org.exoplatform.services.wsrp2.type.UserContext;
import org.exoplatform.services.wsrp2.type.UserProfile;

public class WSRPTypesTransformer {

  /**
   * Gets the WS2 registration context.
   * 
   * @param WS1RegistrationContext
   * @return RegistrationContext
   */
  public static RegistrationContext getWS2RegistrationContext(WS1RegistrationContext ws1RegContext) {
    if (ws1RegContext == null) {
      return null;
    }
    RegistrationContext regContext = new RegistrationContext();
    regContext.setRegistrationHandle(ws1RegContext.getRegistrationHandle());
    regContext.setRegistrationState(ws1RegContext.getRegistrationState());
    return regContext;
  }

  /**
   * Gets the WS2 registration data.
   * @param WS1RegistrationData
   * @return RegistrationData
   */
  public static RegistrationData getWS2RegistrationData(WS1RegistrationData ws1RegData) {
    if (ws1RegData == null) {
      return null;
    }
    RegistrationData regData = new RegistrationData();
    regData.setConsumerAgent(ws1RegData.getConsumerAgent());
    regData.setConsumerName(ws1RegData.getConsumerName());
    regData.setMethodGetSupported(ws1RegData.isMethodGetSupported());
    regData.getConsumerModes().addAll(ws1RegData.getConsumerModes());
    regData.getConsumerUserScopes().addAll(ws1RegData.getConsumerUserScopes());
    regData.getConsumerWindowStates().addAll(ws1RegData.getConsumerWindowStates());
    List<Property> ws2RegProperties = getWS2Properties(ws1RegData.getRegistrationProperties());
    regData.getRegistrationProperties().addAll(ws2RegProperties);
    return regData;
  }

  /**
   * Gets the WS2 properties.
   * 
   * @param List<WS1Property>
   * @return WS2  List of properties
   */
  public static List<Property> getWS2Properties(List<WS1Property> ws1Properties) {
    if (ws1Properties == null) {
      return null;
    }
    List<Property> properties = new ArrayList<Property>();
    for (Iterator<WS1Property> it = ws1Properties.iterator(); it.hasNext();) {
      Property property = getWS2Property(it.next());
      if (property != null) {
        properties.add(property);
      }
    }
    return properties;
  }

  /**
   * Gets the WS2 property.
   * 
   * @param WS1Property
   * @return WS2 property
   */
  public static Property getWS2Property(WS1Property ws1property) {
    if (ws1property == null) {
      return null;
    }
    Property property = new Property();
    property.setLang(ws1property.getLang());
    property.setName(new QName(ws1property.getName()));
    property.setStringValue(ws1property.getStringValue());
    return property;
  }

  /**
   * Gets the WS2 extensions.
   * 
   * @param List<WS1Extension>
   * @return List<Extension>
   */
  public static List<Extension> getWS2Extensions(List<WS1Extension> ws1Extensions) {
    if (ws1Extensions == null) {
      return null;
    }
    List<Extension> extensions = new ArrayList<Extension>();
    for (Iterator<WS1Extension> it = ws1Extensions.iterator(); it.hasNext();) {
      Extension ext = getWS2Extension(it.next());
      if (ext != null) {
        extensions.add(ext);
      }
    }
    return extensions;
  }

  /**
   * Gets the WS2 extension.
   * 
   * @param  WS1Extension
   * @return   Extension
   */
  public static Extension getWS2Extension(WS1Extension ws1Extension) {
    if (ws1Extension == null) {
      return null;
    }
    Extension extension = new Extension();
    extension.setAny(ws1Extension.getAny());
    return extension;
  }

  /**
   * Gets the WS1 extensions.
   * 
   * @param List<Extension>
   * @return List<WS1Extension>
   */
  public static List<WS1Extension> getWS1Extensions(List<Extension> extensions) {
    if (extensions == null) {
      return null;
    }
    List<WS1Extension> ws1Extensions = new ArrayList<WS1Extension>();
    for (Iterator<Extension> it = extensions.iterator(); it.hasNext();) {
      WS1Extension ws1Ext = new WS1Extension();
      ws1Ext = getWS1Extension(it.next());
      if (ws1Ext != null) {
        ws1Extensions.add(ws1Ext);
      }
    }
    return ws1Extensions;
  }

  /**
   * Gets the WS1 extension.
   * 
   * @param Extension
   * @return WS1Extension
   */
  public static WS1Extension getWS1Extension(Extension ext) {
    if (ext == null) {
      return null;
    }
    WS1Extension ws1Extension = new WS1Extension();
    ws1Extension.setAny(ext.getAny());
    return ws1Extension;
  }

  /**
   * Gets the WS2 interaction params.
   * 
   * @param WS1InteractionParams
   * @return InteractionParams
   */
  public static InteractionParams getWS2InteractionParams(WS1InteractionParams ws1InteractionParams) {
    if (ws1InteractionParams == null) {
      return null;
    }
    InteractionParams interactionParams = new InteractionParams();
    interactionParams.setInteractionState(ws1InteractionParams.getInteractionState());
    interactionParams.setPortletStateChange(getWS2StateChange(ws1InteractionParams.getPortletStateChange()));
    interactionParams.getFormParameters().addAll(getWS2FormParameters(ws1InteractionParams.getFormParameters()));
    interactionParams.getUploadContexts().addAll(getWS2UploadContext(ws1InteractionParams.getUploadContexts()));
    return interactionParams;
  }

  /**
   * Gets the WS2 markup params.
   * 
   * @param WS1MarkupParams
   * @return MarkupParams
   */
  public static MarkupParams getWS2MarkupParams(WS1MarkupParams ws1MarkupParams) {
    if (ws1MarkupParams == null) {
      return null;
    }
    MarkupParams markupParams = new MarkupParams();
    markupParams.setClientData(getWS2ClientData(ws1MarkupParams.getClientData()));
    markupParams.setMode(ws1MarkupParams.getMode());
    NavigationalContext navigationalContext = new NavigationalContext();
    navigationalContext.setOpaqueValue(ws1MarkupParams.getNavigationalState());
    markupParams.setNavigationalContext(navigationalContext);
    markupParams.setSecureClientCommunication(ws1MarkupParams.isSecureClientCommunication());
    markupParams.getLocales().addAll(ws1MarkupParams.getLocales());
    markupParams.setValidateTag(ws1MarkupParams.getValidateTag());
    markupParams.setWindowState(ws1MarkupParams.getWindowState());
    markupParams.getMimeTypes().addAll(ws1MarkupParams.getMimeTypes());

    return markupParams;
  }

  /**
   * Gets the WS2 runtime context.
   * 
   * @param WS1RuntimeContext
   * @return RuntimeContext
   */
  public static RuntimeContext getWS2RuntimeContext(WS1RuntimeContext ws1RuntimeContext) {
    if (ws1RuntimeContext == null) {
      return null;
    }
    RuntimeContext runtimeContext = new RuntimeContext();
    runtimeContext.setNamespacePrefix(ws1RuntimeContext.getNamespacePrefix());
    runtimeContext.setPortletInstanceKey(ws1RuntimeContext.getPortletInstanceKey());
    SessionParams sessionParams = new SessionParams();
    sessionParams.setSessionID(ws1RuntimeContext.getSessionID());
    runtimeContext.setSessionParams(sessionParams);
    runtimeContext.setTemplates(getWS2Templates(ws1RuntimeContext.getTemplates()));
    runtimeContext.setUserAuthentication(ws1RuntimeContext.getUserAuthentication());
    return runtimeContext;
  }

  /**
   * Gets the WS2 user context.
   * 
   * @param WS1UserContext
   * @return UserContext
   */
  public static UserContext getWS2UserContext(WS1UserContext ws1UserContext) {
    if (ws1UserContext == null) {
      return null;
    }
    UserContext userContext = new UserContext();
    userContext.setProfile(getWS2UserProfile(ws1UserContext.getProfile()));
    userContext.setUserContextKey(ws1UserContext.getUserContextKey());
    userContext.getUserCategories().addAll(ws1UserContext.getUserCategories());
    userContext.getExtensions().addAll(getWS2Extensions(ws1UserContext.getExtensions()));
    return userContext;
  }

  /**
   * Gets the WS2 portlet context.
   * 
   * @param WS1PortletContext
   * @return PortletContext
   */
  public static PortletContext getWS2PortletContext(WS1PortletContext ws1PortletContext) {
    if (ws1PortletContext == null) {
      return null;
    }
    PortletContext portletContext = new PortletContext();
    portletContext.setPortletHandle(ws1PortletContext.getPortletHandle());
    portletContext.setPortletState(ws1PortletContext.getPortletState());
    portletContext.getExtensions().addAll(getWS2Extensions(ws1PortletContext.getExtensions()));
    return portletContext;
  }

  /**
   * Gets the WS1 update response.
   * 
   * @param UpdateResponse
   * @return WS1UpdateResponse
   */
  public static WS1UpdateResponse getWS1UpdateResponse(UpdateResponse updateResponse) {
    if (updateResponse == null) {
      return null;
    }
    WS1UpdateResponse ws1UpdateResponse = new WS1UpdateResponse();

    ws1UpdateResponse.setMarkupContext(getWS1MarkupContext(updateResponse.getMarkupContext()));

    if (updateResponse.getNavigationalContext() != null) {
      ws1UpdateResponse.setNavigationalState(updateResponse.getNavigationalContext().getOpaqueValue());
    }
    ws1UpdateResponse.setNewMode(updateResponse.getNewMode());
    ws1UpdateResponse.setPortletContext(getWS1PortletContext(updateResponse.getPortletContext()));
    ws1UpdateResponse.setSessionContext(getWS1SessionContext(updateResponse.getSessionContext()));
    ws1UpdateResponse.setNewWindowState(updateResponse.getNewWindowState());
    return ws1UpdateResponse;
  }

  /**
   * Gets the WS2 update response.
   * 
   * @param WS1UpdateResponse
   * @return UpdateResponse
   */
  public static UpdateResponse getWS2UpdateResponse(WS1UpdateResponse ws1UpdateResponse) {
    if (ws1UpdateResponse == null) {
      return null;
    }
    UpdateResponse updateResponse = new UpdateResponse();
    updateResponse.setMarkupContext(getWS2MarkupContext(ws1UpdateResponse.getMarkupContext()));
    NavigationalContext navigationalContext = new NavigationalContext();
    navigationalContext.setOpaqueValue(ws1UpdateResponse.getNavigationalState());
    updateResponse.setNavigationalContext(navigationalContext);
    updateResponse.setNewMode(ws1UpdateResponse.getNewMode());
    updateResponse.setNewWindowState(ws1UpdateResponse.getNewWindowState());
    updateResponse.setPortletContext(getWS2PortletContext(ws1UpdateResponse.getPortletContext()));
    updateResponse.setSessionContext(getWS2SessionContext(ws1UpdateResponse.getSessionContext()));
    return updateResponse;
  }

  /**
   * Gets the WS2 upload context list.
   * 
   * @param List<WS1UploadContext>
   * @return List<UploadContext>
   */
  public static List<UploadContext> getWS2UploadContext(List<WS1UploadContext> ws1UploadContexts) {
    if (ws1UploadContexts == null) {
      return null;
    }
    List<UploadContext> uploadContexts = new ArrayList<UploadContext>();
    for (Iterator<WS1UploadContext> it = ws1UploadContexts.iterator(); it.hasNext();) {
      UploadContext uploadContext = getWS2UploadContext(it.next());
      if (uploadContext != null) {
        uploadContexts.add(uploadContext);
      }
    }
    return uploadContexts;
  }
  
  /**
   * Gets the WS2 state change.
   * 
   * @param WS1StateChange
   * @return StateChange
   */
  public static StateChange getWS2StateChange(WS1StateChange ws1StateChange) {
    if (ws1StateChange == null) {
      return null;
    }
    return StateChange.fromValue(ws1StateChange.value());
  }

  /**
   * Gets the WS2 form parameters list.
   * 
   * @param List<WS1NamedString> parameters names
   * @return List<NamedString> parameters
   */
  public static List<NamedString> getWS2FormParameters(List<WS1NamedString> ws1NamedStrings) {
    if (ws1NamedStrings == null) {
      return null;
    }
    List<NamedString> namedStrings = new ArrayList<NamedString>();
    for (Iterator<WS1NamedString> it = ws1NamedStrings.iterator(); it.hasNext();) {
      NamedString namedString = getWS2NamedString(it.next());
      if (namedString != null) {
        namedStrings.add(namedString);
      }
    }
    return namedStrings;
  }

  /**
   * Gets the WS2 form templates.
   * 
   * @param WS1Templates
   * @return Templates
   */
  public static Templates getWS2Templates(WS1Templates ws1Templates) {
    if (ws1Templates == null) {
      return null;
    }
    Templates templates = new Templates();
    templates.setBlockingActionTemplate(ws1Templates.getBlockingActionTemplate());
    templates.setDefaultTemplate(ws1Templates.getDefaultTemplate());
    templates.setRenderTemplate(ws1Templates.getRenderTemplate());
    templates.setResourceTemplate(ws1Templates.getResourceTemplate());
    templates.setSecureBlockingActionTemplate(ws1Templates.getSecureBlockingActionTemplate());
    templates.setSecureDefaultTemplate(ws1Templates.getSecureDefaultTemplate());
    templates.setSecureRenderTemplate(ws1Templates.getSecureRenderTemplate());
    templates.setSecureResourceTemplate(ws1Templates.getSecureResourceTemplate());
    templates.getExtensions().addAll(getWS2Extensions(ws1Templates.getExtensions()));
    return templates;
  }

  /**
   * Gets the WS2 client data.
   * 
   * @param WS1ClientData
   * @return ClientData
   */
  public static ClientData getWS2ClientData(WS1ClientData ws1ClientData) {
    if (ws1ClientData == null) {
      return null;
    }
    ClientData clientData = new ClientData();
    clientData.setUserAgent(ws1ClientData.getUserAgent());
    clientData.getExtensions().addAll(getWS2Extensions(ws1ClientData.getExtensions()));
    return clientData;
  }

  /**
   * Gets the WS2 user profile.
   * 
   * @param WS1UserProfile
   * @return UserProfile
   */
  public static UserProfile getWS2UserProfile(WS1UserProfile ws1UserProfile) {
    if (ws1UserProfile == null) {
      return null;
    }
    UserProfile userProfile = new UserProfile();
    userProfile.setName(getWS2PersonName(ws1UserProfile.getName()));
    userProfile.setHomeInfo(getWS2Contact(ws1UserProfile.getHomeInfo()));
    userProfile.setGender(ws1UserProfile.getGender());
    userProfile.setEmployerInfo(getWS2EmployerInfo(ws1UserProfile.getEmployerInfo()));
    userProfile.setBusinessInfo(getWS2Contact(ws1UserProfile.getBusinessInfo()));
    userProfile.setBdate(ws1UserProfile.getBdate());
    return userProfile;
  }

  /**
   * Gets the WS1 markup context.
   * 
   * @param MarkupContext
   * @return WS1MarkupContext
   */
  public static WS1MarkupContext getWS1MarkupContext(MarkupContext markupContext) {
    if (markupContext == null) {
      return null;
    }
    WS1MarkupContext ws1MarkupContext = new WS1MarkupContext();
    ws1MarkupContext.setCacheControl(getWS1CacheControl(markupContext.getCacheControl()));
    ws1MarkupContext.setMarkupBinary(markupContext.getItemBinary());
    ws1MarkupContext.setMarkupString(markupContext.getItemString());
    ws1MarkupContext.setLocale(markupContext.getLocale());
    ws1MarkupContext.setMimeType(markupContext.getMimeType());
    ws1MarkupContext.setPreferredTitle(markupContext.getPreferredTitle());
    ws1MarkupContext.setRequiresUrlRewriting(markupContext.isRequiresRewriting());
    ws1MarkupContext.setUseCachedMarkup(markupContext.isUseCachedItem());
    return ws1MarkupContext;
  }

  /**
   * Gets the WS1 portlet context.
   * 
   * @param PortletContext
   * @return WS1PortletContext
   */
  public static WS1PortletContext getWS1PortletContext(PortletContext portletContext) {
    if (portletContext == null) {
      return null;
    }
    WS1PortletContext ws1PortletContext = new WS1PortletContext();
    ws1PortletContext.setPortletHandle(portletContext.getPortletHandle());
    ws1PortletContext.setPortletState(portletContext.getPortletState());
    ws1PortletContext.getExtensions().addAll(getWS1Extensions(portletContext.getExtensions()));
    return ws1PortletContext;
  }

  /**
   * Gets the WS2 markup context.
   * 
   * @param WS1MarkupContext
   * @return MarkupContext
   */
  public static MarkupContext getWS2MarkupContext(WS1MarkupContext ws1MarkupContext) {
    if (ws1MarkupContext == null) {
      return null;
    }
    MarkupContext markupContext = new MarkupContext();
    markupContext.setCacheControl(getWS2CacheControl(ws1MarkupContext.getCacheControl()));
    markupContext.setItemBinary(ws1MarkupContext.getMarkupBinary());
    markupContext.setItemString(ws1MarkupContext.getMarkupString());
    markupContext.setLocale(ws1MarkupContext.getLocale());
    markupContext.setMimeType(ws1MarkupContext.getMimeType());
    markupContext.setPreferredTitle(ws1MarkupContext.getPreferredTitle());
    markupContext.setRequiresRewriting(ws1MarkupContext.isRequiresUrlRewriting());
    markupContext.setUseCachedItem(ws1MarkupContext.isUseCachedMarkup());
    return markupContext;
  }

  /**
   * Gets the WS1 session context.
   * 
   * @param SessionContext
   * @return WS1SessionContext
   */
  public static WS1SessionContext getWS1SessionContext(SessionContext sessionContext) {
    if (sessionContext == null) {
      return null;
    }
    WS1SessionContext ws1SessionContext = new WS1SessionContext();
    ws1SessionContext.setExpires(sessionContext.getExpires());
    ws1SessionContext.setSessionID(sessionContext.getSessionID());
    ws1SessionContext.getExtensions().addAll(getWS1Extensions(sessionContext.getExtensions()));
    return ws1SessionContext;
  }

  /**
   * Gets the WS2 session context.
   * 
   * @param WS1SessionContext
   * @return SessionContext
   */
  public static SessionContext getWS2SessionContext(WS1SessionContext ws1SessionContext) {
    if (ws1SessionContext == null) {
      return null;
    }
    SessionContext sessionContext = new SessionContext();
    sessionContext.setExpires(ws1SessionContext.getExpires());
    sessionContext.setSessionID(ws1SessionContext.getSessionID());
    sessionContext.getExtensions().addAll(getWS2Extensions(ws1SessionContext.getExtensions()));
    return sessionContext;
  }

  /**
   * Gets the WS2 upload context.
   * 
   * @param WS1UploadContext
   * @return UploadContext
   */
  public static UploadContext getWS2UploadContext(WS1UploadContext ws1UploadContext) {
    if (ws1UploadContext == null) {
      return null;
    }
    UploadContext uploadContext = new UploadContext();
    uploadContext.setMimeType(ws1UploadContext.getMimeType());
    uploadContext.setUploadData(ws1UploadContext.getUploadData());
    uploadContext.getMimeAttributes().addAll(getWS2NamedStrings(ws1UploadContext.getMimeAttributes()));
    uploadContext.getExtensions().addAll(getWS2Extensions(ws1UploadContext.getExtensions()));
    return uploadContext;
  }

  /**
   * Gets the WS2 named string.
   * 
   * @param WS1NamedString
   * @return NamedString
   */
  public static NamedString getWS2NamedString(WS1NamedString ws1NamedString) {
    if (ws1NamedString == null) {
      return null;
    }
    NamedString namedString = new NamedString();
    namedString.setName(ws1NamedString.getName());
    namedString.setValue(ws1NamedString.getValue());
    return namedString;
  }

  /**
   * Gets the WS2 named strings list.
   * 
   * @param List<WS1NamedString>
   * @return List<NamedString>
   */
  public static List<NamedString> getWS2NamedStrings(List<WS1NamedString> ws1NamedStrings) {
    if (ws1NamedStrings == null) {
      return null;
    }
    List<NamedString> namedStrings = new ArrayList<NamedString>();
    for (Iterator<WS1NamedString> it = ws1NamedStrings.iterator(); it.hasNext();) {
      NamedString namedString = getWS2NamedString(it.next());
      if (namedString != null) {
        namedStrings.add(namedString);
      }
    }
    return namedStrings;
  }

  /**
   * Gets the WS2 persom name.
   * 
   * @param WS1PersonName
   * @return PersonName
   */
  public static PersonName getWS2PersonName(WS1PersonName ws1PersonName) {
    if (ws1PersonName == null) {
      return null;
    }
    PersonName personName = new PersonName();
    personName.setFamily(ws1PersonName.getFamily());
    personName.setGiven(ws1PersonName.getGiven());
    personName.setMiddle(ws1PersonName.getMiddle());
    personName.setNickname(ws1PersonName.getNickname());
    personName.setPrefix(ws1PersonName.getPrefix());
    personName.setSuffix(ws1PersonName.getSuffix());
    return personName;
  }

  /**
   * Gets the WS2 contact.
   * 
   * @param WS1Contact
   * @return Contact
   */
  public static Contact getWS2Contact(WS1Contact ws1Contact) {
    if (ws1Contact == null) {
      return null;
    }
    Contact contact = new Contact();
    contact.setOnline(getWS2Online(ws1Contact.getOnline()));
    contact.setPostal(getWS2Postal(ws1Contact.getPostal()));
    contact.setTelecom(getWS2Telecom(ws1Contact.getTelecom()));
    return contact;
  }

  /**
   * Gets the WS2 employer info.
   * 
   * @param WS1EmployerInfo
   * @return EmployerInfo
   */
  public static EmployerInfo getWS2EmployerInfo(WS1EmployerInfo ws1EmployerInfo) {
    if (ws1EmployerInfo == null) {
      return null;
    }
    EmployerInfo employerInfo = new EmployerInfo();
    employerInfo.setDepartment(ws1EmployerInfo.getDepartment());
    employerInfo.setEmployer(ws1EmployerInfo.getEmployer());
    employerInfo.setJobtitle(ws1EmployerInfo.getJobtitle());
    return employerInfo;
  }

  /**
   * Gets the WS2 online.
   * 
   * @param WS1Online
   * @return Online
   */
  public static Online getWS2Online(WS1Online ws1Online) {
    if (ws1Online == null) {
      return null;
    }
    Online online = new Online();
    online.setEmail(ws1Online.getEmail());
    online.setUri(ws1Online.getUri());
    return online;
  }

  /**
   * Gets the WS2 postal.
   * 
   * @param WS1Postal
   * @return Postal
   */
  public static Postal getWS2Postal(WS1Postal ws1postal) {
    if (ws1postal == null) {
      return null;
    }
    Postal postal = new Postal();
    postal.setCity(ws1postal.getCity());
    postal.setCountry(ws1postal.getCountry());
    postal.setName(ws1postal.getName());
    postal.setOrganization(ws1postal.getOrganization());
    postal.setPostalcode(ws1postal.getPostalcode());
    postal.setStateprov(ws1postal.getStateprov());
    postal.setStreet(ws1postal.getStreet());
    return postal;
  }

  /**
   * Gets the WS2 telecom.
   * 
   * @param WS1Telecom
   * @return Telecom
   */
  public static Telecom getWS2Telecom(WS1Telecom ws1telecom) {
    if (ws1telecom == null) {
      return null;
    }
    Telecom telecom = new Telecom();
    telecom.setFax(getWS2TelephoneNum(ws1telecom.getTelephone()));
    telecom.setMobile(getWS2TelephoneNum(ws1telecom.getMobile()));
    telecom.setPager(getWS2TelephoneNum(ws1telecom.getPager()));
    telecom.setTelephone(getWS2TelephoneNum(ws1telecom.getTelephone()));
    return telecom;
  }

  /**
   * Gets the WS2 telephone number.
   * 
   * @param WS1TelephoneNum
   * @return TelephoneNum
   */
  public static TelephoneNum getWS2TelephoneNum(WS1TelephoneNum ws1telephoneNum) {
    if (ws1telephoneNum == null) {
      return null;
    }
    TelephoneNum telephoneNo = new TelephoneNum();
    telephoneNo.setComment(ws1telephoneNum.getComment());
    telephoneNo.setExt(ws1telephoneNum.getExt());
    telephoneNo.setIntcode(ws1telephoneNum.getIntcode());
    telephoneNo.setLoccode(ws1telephoneNum.getLoccode());
    telephoneNo.setNumber(ws1telephoneNum.getNumber());
    return telephoneNo;
  }
  
  /**
   * Gets the WS2 cache control.
   * 
   * @param WS1CacheControl
   * @return CacheControl
   */
  public static CacheControl getWS2CacheControl(WS1CacheControl ws1cacheControl) {
    if (ws1cacheControl == null) {
      return null;
    }
    CacheControl cacheControl = new CacheControl();
    cacheControl.setExpires(ws1cacheControl.getExpires());
    cacheControl.setUserScope(ws1cacheControl.getUserScope());
    cacheControl.setValidateTag(ws1cacheControl.getValidateTag());
    return cacheControl;
  }

  /**
   * Gets the WS1 cache control.
   * 
   * @param CacheControl
   * @return WS1CacheControl
   */
  public static WS1CacheControl getWS1CacheControl(CacheControl cacheControl) {
    if (cacheControl == null) {
      return null;
    }
    WS1CacheControl ws1cacheControl = new WS1CacheControl();
    ws1cacheControl.setExpires(cacheControl.getExpires());
    ws1cacheControl.setUserScope(cacheControl.getUserScope());
    ws1cacheControl.setValidateTag(cacheControl.getValidateTag());
    return ws1cacheControl;
  }

  /**
   * Gets the WS2 portlet descriptions list.
   * 
   * @param List<WS1PortletDescription>
   * @return List<PortletDescription>
   */
  public static List<PortletDescription> getWS2PortletDescriptions(List<WS1PortletDescription> ws1portletDescriptions) {
    if (ws1portletDescriptions == null) {
      return null;
    }
    List<PortletDescription> portletDescriptions = new ArrayList<PortletDescription>();
    Iterator<WS1PortletDescription> it = ws1portletDescriptions.iterator();
    while (it.hasNext()) {
      WS1PortletDescription elem = (WS1PortletDescription) it.next();
      PortletDescription portletDescription = getWS2PortletDescription(elem);
      if (portletDescription != null) {
        portletDescriptions.add(portletDescription);
      }
    }
    return portletDescriptions;
  }

  /**
   * Gets the WS2 portlet description.
   * 
   * @param WS1PortletDescription
   * @return PortletDescription
   */
  public static PortletDescription getWS2PortletDescription(WS1PortletDescription ws1portletDescription) {
    if (ws1portletDescription == null) {
      return null;
    }
    PortletDescription portletDescription = new PortletDescription();
    portletDescription.setDefaultMarkupSecure(ws1portletDescription.isDefaultMarkupSecure());
    portletDescription.setDescription(getWS2LocalizedString(ws1portletDescription.getDescription()));
    portletDescription.setDisplayName(getWS2LocalizedString(ws1portletDescription.getDisplayName()));
    portletDescription.setDoesUrlTemplateProcessing(ws1portletDescription.isDoesUrlTemplateProcessing());
    portletDescription.setGroupID(ws1portletDescription.getGroupID());
    portletDescription.setHasUserSpecificState(ws1portletDescription.isHasUserSpecificState());
    portletDescription.setMayReturnPortletState(false);
    portletDescription.setOnlySecure(ws1portletDescription.isOnlySecure());
    portletDescription.setPortletHandle(ws1portletDescription.getPortletHandle());
    portletDescription.setShortTitle(getWS2LocalizedString(ws1portletDescription.getShortTitle()));
    portletDescription.setTemplatesStoredInSession(ws1portletDescription.isTemplatesStoredInSession());
    portletDescription.setTitle(getWS2LocalizedString(ws1portletDescription.getTitle()));
    portletDescription.setUserContextStoredInSession(ws1portletDescription.isUserContextStoredInSession());
    portletDescription.setUsesMethodGet(ws1portletDescription.isUsesMethodGet());
    portletDescription.getExtensions().addAll(getWS2Extensions(ws1portletDescription.getExtensions()));
    portletDescription.getKeywords().addAll(getWS2LocalizedStrings(ws1portletDescription.getKeywords()));
    portletDescription.getMarkupTypes().addAll(getWS2MarkupTypes(ws1portletDescription.getMarkupTypes()));
    portletDescription.getUserProfileItems().addAll(ws1portletDescription.getUserProfileItems());

    return portletDescription;
  }

  /**
   * Gets the WS2 item descriptions list.
   * 
   * @param List<WS1ItemDescription>
   * @return List<ItemDescription>
   */
  public static List<ItemDescription> getWS2ItemDescriptions(List<WS1ItemDescription> ws1itemDescriptions) {
    if (ws1itemDescriptions == null) {
      return null;
    }
    List<ItemDescription> itemDescriptions = new ArrayList<ItemDescription>();
    for (Iterator<WS1ItemDescription> it = ws1itemDescriptions.iterator(); it.hasNext();) {
      ItemDescription itemDescription = getWS2ItemDescription(it.next());
      if (itemDescription != null) {
        itemDescriptions.add(itemDescription);
      }
    }
    return itemDescriptions;
  }

  /**
   * Gets the WS2 item description.
   * 
   * @param WS1ItemDescription
   * @return ItemDescription
   */
  public static ItemDescription getWS2ItemDescription(WS1ItemDescription ws1itemDescription) {
    if (ws1itemDescription == null) {
      return null;
    }
    ItemDescription itemDescription = new ItemDescription();
    itemDescription.setDescription(getWS2LocalizedString(ws1itemDescription.getDescription()));
    itemDescription.setItemName(ws1itemDescription.getItemName());
    itemDescription.getExtensions().addAll(getWS2Extensions(ws1itemDescription.getExtensions()));
    return itemDescription;
  }

  /**
   * Gets the WS2 cookie protocol.
   * 
   * @param WS1CookieProtocol
   * @return CookieProtocol
   */
  public static CookieProtocol getWS2CookieProtocol(WS1CookieProtocol ws1cookieProtocol) {
    if (ws1cookieProtocol == null) {
      return null;
    }
    return CookieProtocol.fromValue(ws1cookieProtocol.value());
  }

  /**
   * Gets the WS2 model description.
   * 
   * @param WS1ModelDescription
   * @return ModelDescription
   */
  public static ModelDescription getWS2ModelDescription(WS1ModelDescription ws1modelDescription) {
    if (ws1modelDescription == null) {
      return null;
    }
    ModelDescription modelDescription = new ModelDescription();
    modelDescription.setModelTypes(getWS2ModelTypes(ws1modelDescription.getModelTypes()));
    modelDescription.getExtensions().addAll(getWS2Extensions(ws1modelDescription.getExtensions()));
    modelDescription.getPropertyDescriptions().addAll(
        getWS2PropertyDescriptions(ws1modelDescription.getPropertyDescriptions()));
    return modelDescription;
  }

  /**
   * Gets the WS2 resource list.
   * 
   * @param WS1ResourceList
   * @return ResourceList
   */
  public static ResourceList getWS2ResourceList(WS1ResourceList ws1resourceList) {
    if (ws1resourceList == null) {
      return null;
    }
    ResourceList resourceList = new ResourceList();
    resourceList.getExtensions().addAll(getWS2Extensions(ws1resourceList.getExtensions()));
    resourceList.getResources().addAll(getWS2Resources(ws1resourceList.getResources()));
    return resourceList;
  }

  /**
   * Gets the WS2 localized string.
   * 
   * @param WS1LocalizedString
   * @return LocalizedString
   */
  public static LocalizedString getWS2LocalizedString(WS1LocalizedString ws1localizedString) {
    if (ws1localizedString == null) {
      return null;
    }
    LocalizedString localizedString = new LocalizedString();
    localizedString.setLang(ws1localizedString.getLang());
    localizedString.setResourceName(ws1localizedString.getResourceName());
    localizedString.setValue(ws1localizedString.getValue());
    return localizedString;
  }

  /**
   * Gets the WS2 localized strings list.
   * 
   * @param List<WS1LocalizedString>
   * @return List<LocalizedString>
   */
  public static List<LocalizedString> getWS2LocalizedStrings(List<WS1LocalizedString> ws1localizedStrings) {
    if (ws1localizedStrings == null) {
      return null;
    }
    List<LocalizedString> localizedStrings = new ArrayList<LocalizedString>();
    for (Iterator<WS1LocalizedString> it = ws1localizedStrings.iterator(); it.hasNext();) {
      LocalizedString localizedString = getWS2LocalizedString(it.next());
      if (localizedString != null) {
        localizedStrings.add(localizedString);
      }
    }
    return localizedStrings;
  }

  /**
   * Gets the WS2 markup types list.
   * 
   * @param List<WS1MarkupType>
   * @return List<MarkupType>
   */
  public static List<MarkupType> getWS2MarkupTypes(List<WS1MarkupType> ws1markupTypes) {
    if (ws1markupTypes == null) {
      return null;
    }
    List<MarkupType> markupTypes = new ArrayList<MarkupType>();
    for (Iterator<WS1MarkupType> it = ws1markupTypes.iterator(); it.hasNext();) {
      MarkupType markupType = getWS2MarkupType(it.next());
      if (markupType != null) {
        markupTypes.add(markupType);
      }
    }
    return markupTypes;
  }

  /**
   * Gets the WS2 markup type.
   * 
   * @param WS1MarkupType
   * @return MarkupType
   */
  public static MarkupType getWS2MarkupType(WS1MarkupType ws1markupType) {
    if (ws1markupType == null) {
      return null;
    }
    MarkupType markupType = new MarkupType();
    markupType.setMimeType(ws1markupType.getMimeType());
    markupType.getLocales().addAll(ws1markupType.getLocales());
    markupType.getExtensions().addAll(getWS2Extensions(ws1markupType.getExtensions()));
    markupType.getWindowStates().addAll(ws1markupType.getWindowStates());
    markupType.getModes().addAll(ws1markupType.getModes());
    return markupType;
  }

  /**
   * Gets the WS2 model types.
   * 
   * @param WS1ModelTypes
   * @return ModelTypes
   */
  public static ModelTypes getWS2ModelTypes(WS1ModelTypes ws1modelTypes) {
    if (ws1modelTypes == null) {
      return null;
    }
    ModelTypes modelTypes = new ModelTypes();
    modelTypes.setAny(ws1modelTypes.getAny());
    return modelTypes;
  }

  /**
   * Gets the WS2 property descriptions list.
   * 
   * @param List<WS1PropertyDescription>
   * @return List<PropertyDescription>
   */
  public static List<PropertyDescription> getWS2PropertyDescriptions(List<WS1PropertyDescription> ws1propertyDescriptions) {
    if (ws1propertyDescriptions == null) {
      return null;
    }
    List<PropertyDescription> propertyDescriptions = new ArrayList<PropertyDescription>();
    for (Iterator<WS1PropertyDescription> it = ws1propertyDescriptions.iterator(); it.hasNext();) {
      PropertyDescription propertyDescription = getWS2PropertyDescription(it.next());
      if (propertyDescription != null) {
        propertyDescriptions.add(propertyDescription);
      }
    }
    return propertyDescriptions;
  }

  /**
   * Gets the WS2 property description.
   * 
   * @param WS1PropertyDescription
   * @return PropertyDescription
   */
  public static PropertyDescription getWS2PropertyDescription(WS1PropertyDescription ws1propertyDescription) {
    if (ws1propertyDescription == null) {
      return null;
    }
    PropertyDescription propertyDescription = new PropertyDescription();
    propertyDescription.setHint(getWS2LocalizedString(ws1propertyDescription.getHint()));
    propertyDescription.setLabel(getWS2LocalizedString(ws1propertyDescription.getLabel()));
    propertyDescription.setName(new QName(ws1propertyDescription.getName()));
    propertyDescription.setType(ws1propertyDescription.getType());
    return propertyDescription;
  }

  /**
   * Gets the WS2  resources list.
   * 
   * @param List<WS1Resource>
   * @return List<Resource>
   */
  public static List<Resource> getWS2Resources(List<WS1Resource> ws1resources) {
    if (ws1resources == null) {
      return null;
    }
    List<Resource> resources = new ArrayList<Resource>();
    for (Iterator<WS1Resource> it = ws1resources.iterator(); it.hasNext();) {
      Resource resource = getWS2Resource(it.next());
      if (resource != null) {
        resources.add(resource);
      }
    }
    return resources;
  }

  /**
   * Gets the WS2 resource.
   * 
   * @param WS1Resource
   * @return Resource
   */
  public static Resource getWS2Resource(WS1Resource ws1resource) {
    if (ws1resource == null) {
      return null;
    }
    Resource resource = new Resource();
    resource.setResourceName(ws1resource.getResourceName());
    resource.getExtensions().addAll(getWS2Extensions(ws1resource.getExtensions()));
    resource.getValues().addAll(getWS2ResourceValues(ws1resource.getValues()));
    return resource;
  }

  /**
   * Gets the WS2 resource values list.
   * 
   * @param List<WS1ResourceValue>
   * @return List<ResourceValue>
   */
  public static List<ResourceValue> getWS2ResourceValues(List<WS1ResourceValue> ws1resourceValues) {
    if (ws1resourceValues == null) {
      return null;
    }
    List<ResourceValue> resourceValues = new ArrayList<ResourceValue>();
    for (Iterator<WS1ResourceValue> it = ws1resourceValues.iterator(); it.hasNext();) {
      ResourceValue resourceValue = getWS2ResourceValue(it.next());
      if (resourceValue != null) {
        resourceValues.add(resourceValue);
      }
    }
    return resourceValues;
  }

  /**
   * Gets the WS2 resource value.
   * 
   * @param WS1ResourceValue
   * @return ResourceValue
   */
  public static ResourceValue getWS2ResourceValue(WS1ResourceValue ws1resourceValue) {
    if (ws1resourceValue == null) {
      return null;
    }
    ResourceValue resourceValue = new ResourceValue();
    resourceValue.setLang(ws1resourceValue.getLang());
    resourceValue.setValue(ws1resourceValue.getValue());
    resourceValue.getExtensions().addAll(getWS2Extensions(ws1resourceValue.getExtensions()));
    return resourceValue;
  }

  /**
   * Gets the WS1 portlet descriptions list.
   * 
   * @param List<PortletDescription>
   * @return List<WS1PortletDescription>
   */
  public static List<WS1PortletDescription> getWS1PortletDescriptions(List<PortletDescription> portletDescriptions) {
    if (portletDescriptions == null) {
      return null;
    }
    List<WS1PortletDescription> ws1portletDescriptions = new ArrayList<WS1PortletDescription>();
    Iterator<PortletDescription> it = portletDescriptions.iterator();
    while (it.hasNext()) {
      PortletDescription elem = (PortletDescription) it.next();
      WS1PortletDescription ws1portletDescription = getWS1PortletDescription(elem);
      if (ws1portletDescription != null) {
        ws1portletDescriptions.add(ws1portletDescription);
      }
    }
    return ws1portletDescriptions;
  }

  /**
   * Gets the WS1 portlet description.
   * 
   * @param PortletDescription
   * @return WS1PortletDescription
   */
  public static WS1PortletDescription getWS1PortletDescription(PortletDescription portletDescription) {
    if (portletDescription == null) {
      return null;
    }
    WS1PortletDescription ws1portletDescription = new WS1PortletDescription();
    ws1portletDescription.setDefaultMarkupSecure(portletDescription.isDefaultMarkupSecure());
    ws1portletDescription.setDescription(getWS1LocalizedString(portletDescription.getDescription()));
    ws1portletDescription.setDisplayName(getWS1LocalizedString(portletDescription.getDisplayName()));
    ws1portletDescription.setDoesUrlTemplateProcessing(portletDescription.isDoesUrlTemplateProcessing());
    ws1portletDescription.setGroupID(portletDescription.getGroupID());
    ws1portletDescription.setHasUserSpecificState(portletDescription.isHasUserSpecificState());
    ws1portletDescription.setOnlySecure(portletDescription.isOnlySecure());
    ws1portletDescription.setPortletHandle(portletDescription.getPortletHandle());
    ws1portletDescription.setShortTitle(getWS1LocalizedString(portletDescription.getShortTitle()));
    ws1portletDescription.setTemplatesStoredInSession(portletDescription.isTemplatesStoredInSession());
    ws1portletDescription.setTitle(getWS1LocalizedString(portletDescription.getTitle()));
    ws1portletDescription.setUserContextStoredInSession(portletDescription.isUserContextStoredInSession());
    ws1portletDescription.setUsesMethodGet(portletDescription.isUsesMethodGet());
    ws1portletDescription.getExtensions().addAll(getWS1Extensions(portletDescription.getExtensions()));
    ws1portletDescription.getKeywords().addAll(getWS1LocalizedStrings(portletDescription.getKeywords()));
    ws1portletDescription.getMarkupTypes().addAll(getWS1MarkupTypes(portletDescription.getMarkupTypes()));
    ws1portletDescription.getUserProfileItems().addAll(portletDescription.getUserProfileItems());

    return ws1portletDescription;
  }

  /**
   * Gets the WS1 item description.
   * 
   * @param ItemDescription
   * @return WS1ItemDescription
   */
  public static WS1ItemDescription getWS1ItemDescription(ItemDescription itemDescription) {
    if (itemDescription == null) {
      return null;
    }
    WS1ItemDescription ws1itemDescription = new WS1ItemDescription();
    ws1itemDescription.setDescription(getWS1LocalizedString(itemDescription.getDescription()));
    ws1itemDescription.setItemName(itemDescription.getItemName());
    ws1itemDescription.getExtensions().addAll(getWS1Extensions(itemDescription.getExtensions()));
    return ws1itemDescription;
  }

  /**
   * Gets the WS1 cookie protocol.
   * 
   * @param CookieProtocol
   * @return WS1CookieProtocol
   */
  public static WS1CookieProtocol getWS1CookieProtocol(CookieProtocol cookieProtocol) {
    if (cookieProtocol == null) {
      return null;
    }
    return WS1CookieProtocol.fromValue(cookieProtocol.value());
  }

  /**
   * Gets the WS1 model description.
   * 
   * @param ModelDescription
   * @return WS1ModelDescription
   */
  public static WS1ModelDescription getWS1ModelDescription(ModelDescription modelDescription) {
    if (modelDescription == null) {
      return null;
    }
    WS1ModelDescription ws1modelDescription = new WS1ModelDescription();
    ws1modelDescription.setModelTypes(getWS1ModelTypes(modelDescription.getModelTypes()));
    ws1modelDescription.getExtensions().addAll(getWS1Extensions(modelDescription.getExtensions()));
    ws1modelDescription.getPropertyDescriptions().addAll(
        getWS1PropertyDescriptions(modelDescription.getPropertyDescriptions()));
    return ws1modelDescription;
  }

  /**
   * Gets the WS1 resource list.
   * 
   * @param ResourceList
   * @return WS1ResourceList
   */
  public static WS1ResourceList getWS1ResourceList(ResourceList resourceList) {
    if (resourceList == null) {
      return null;
    }
    WS1ResourceList ws1resourceList = new WS1ResourceList();
    ws1resourceList.getExtensions().addAll(getWS1Extensions(resourceList.getExtensions()));
    ws1resourceList.getResources().addAll(getWS1Resources(resourceList.getResources()));
    return ws1resourceList;
  }

  /**
   * Gets the WS1 localized string.
   * 
   * @param LocalizedString
   * @return WS1LocalizedString
   */
  public static WS1LocalizedString getWS1LocalizedString(LocalizedString localizedString) {
    if (localizedString == null) {
      return null;
    }
    WS1LocalizedString ws1localizedString = new WS1LocalizedString();
    ws1localizedString.setLang(localizedString.getLang());
    ws1localizedString.setResourceName(localizedString.getResourceName());
    ws1localizedString.setValue(localizedString.getValue());
    return ws1localizedString;
  }

  /**
   * Gets the WS1 localized strings list.
   * 
   * @param List<LocalizedString>
   * @return List<WS1LocalizedString>
   */
  public static List<WS1LocalizedString> getWS1LocalizedStrings(List<LocalizedString> localizedStrings) {
    if (localizedStrings == null) {
      return null;
    }
    List<WS1LocalizedString> ws1localizedStrings = new ArrayList<WS1LocalizedString>();

    for (Iterator<LocalizedString> it = localizedStrings.iterator(); it.hasNext();) {
      WS1LocalizedString ws1localizedString = getWS1LocalizedString(it.next());
      if (ws1localizedString != null) {
        ws1localizedStrings.add(ws1localizedString);
      }
    }
    return ws1localizedStrings;
  }

  /**
   * Gets the WS1 markup types list.
   * 
   * @param List<MarkupType>
   * @return List<WS1MarkupType>
   */
  public static List<WS1MarkupType> getWS1MarkupTypes(List<MarkupType> markupTypes) {
    if (markupTypes == null) {
      return null;
    }
    List<WS1MarkupType> ws1markupTypes = new ArrayList<WS1MarkupType>();
    for (Iterator<MarkupType> it = markupTypes.iterator(); it.hasNext();) {
      WS1MarkupType ws1markupType = getWS1MarkupType(it.next());
      if (ws1markupType != null) {
        ws1markupTypes.add(ws1markupType);
      }
    }
    return ws1markupTypes;
  }

  /**
   * Gets the WS1 markup type.
   * 
   * @param MarkupType
   * @return WS1MarkupType
   */
  public static WS1MarkupType getWS1MarkupType(MarkupType markupType) {
    if (markupType == null) {
      return null;
    }
    WS1MarkupType ws1markupType = new WS1MarkupType();
    ws1markupType.setMimeType(markupType.getMimeType());
    ws1markupType.getLocales().addAll(markupType.getLocales());
    ws1markupType.getExtensions().addAll(getWS1Extensions(markupType.getExtensions()));
    ws1markupType.getWindowStates().addAll(markupType.getWindowStates());
    ws1markupType.getModes().addAll(markupType.getModes());
    return ws1markupType;
  }

  /**
   * Gets the WS1 model types.
   * 
   * @param ModelTypes
   * @return WS1ModelTypes
   */
  public static WS1ModelTypes getWS1ModelTypes(ModelTypes modelTypes) {
    if (modelTypes == null) {
      return null;
    }
    WS1ModelTypes ws1modelTypes = new WS1ModelTypes();
    ws1modelTypes.setAny(modelTypes.getAny());
    return ws1modelTypes;
  }

  /**
   * Gets the WS1 property descriptions list.
   * 
   * @param List<PropertyDescription>
   * @return List<WS1PropertyDescription>
   */
  public static List<WS1PropertyDescription> getWS1PropertyDescriptions(List<PropertyDescription> propertyDescriptions) {
    if (propertyDescriptions == null) {
      return null;
    }
    List<WS1PropertyDescription> ws1propertyDescriptions = new ArrayList<WS1PropertyDescription>();
    for (Iterator<PropertyDescription> it = propertyDescriptions.iterator(); it.hasNext();) {
      WS1PropertyDescription ws1propertyDescription = getWS1PropertyDescription(it.next());
      if (ws1propertyDescription != null) {
        ws1propertyDescriptions.add(ws1propertyDescription);
      }
    }
    return ws1propertyDescriptions;
  }

  /**
   * Gets the WS1 property description.
   * 
   * @param PropertyDescription
   * @return WS1PropertyDescription
   */
  public static WS1PropertyDescription getWS1PropertyDescription(PropertyDescription propertyDescription) {
    if (propertyDescription == null) {
      return null;
    }
    WS1PropertyDescription ws1propertyDescription = new WS1PropertyDescription();
    ws1propertyDescription.setHint(getWS1LocalizedString(propertyDescription.getHint()));
    ws1propertyDescription.setLabel(getWS1LocalizedString(propertyDescription.getLabel()));
    ws1propertyDescription.setName(propertyDescription.getName().toString());
    ws1propertyDescription.setType(propertyDescription.getType());
    return ws1propertyDescription;
  }

  /**
   * Gets the WS1 resources list.
   * 
   * @param List<Resource>
   * @return List<WS1Resource>
   */
  public static List<WS1Resource> getWS1Resources(List<Resource> resources) {
    if (resources == null) {
      return null;
    }
    List<WS1Resource> ws1resources = new ArrayList<WS1Resource>();
    for (Iterator<Resource> it = resources.iterator(); it.hasNext();) {
      WS1Resource ws1resource = getWS1Resource(it.next());
      if (ws1resource != null) {
        ws1resources.add(ws1resource);
      }
    }
    return ws1resources;
  }

  /**
   * Gets the WS1 resource.
   * 
   * @param Resource
   * @return WS1Resource
   */
  public static WS1Resource getWS1Resource(Resource resource) {
    if (resource == null) {
      return null;
    }
    WS1Resource ws1resource = new WS1Resource();
    ws1resource.setResourceName(resource.getResourceName());
    ws1resource.getExtensions().addAll(getWS1Extensions(resource.getExtensions()));
    ws1resource.getValues().addAll(getWS1ResourceValues(resource.getValues()));
    return ws1resource;
  }

  /**
   * Gets the WS1 resource values list.
   * 
   * @param List<ResourceValue>
   * @return List<WS1ResourceValue>
   */
  public static List<WS1ResourceValue> getWS1ResourceValues(List<ResourceValue> resourceValues) {
    if (resourceValues == null) {
      return null;
    }
    List<WS1ResourceValue> ws1resourceValues = new ArrayList<WS1ResourceValue>();
    for (Iterator<ResourceValue> it = resourceValues.iterator(); it.hasNext();) {
      WS1ResourceValue ws1resourceValue = getWS1ResourceValue(it.next());
      if (ws1resourceValue != null) {
        ws1resourceValues.add(ws1resourceValue);
      }
    }
    return ws1resourceValues;
  }

  /**
   * Gets the WS1 resource value.
   * 
   * @param ResourceValue
   * @return WS1ResourceValue
   */
  public static WS1ResourceValue getWS1ResourceValue(ResourceValue resourceValue) {
    if (resourceValue == null) {
      return null;
    }
    WS1ResourceValue ws1resourceValue = new WS1ResourceValue();
    ws1resourceValue.setLang(resourceValue.getLang());
    ws1resourceValue.setValue(resourceValue.getValue());
    ws1resourceValue.getExtensions().addAll(getWS1Extensions(resourceValue.getExtensions()));
    return ws1resourceValue;
  }

  /**
   * Gets the WS1 item descriptions list.
   * 
   * @param List<ItemDescription>
   * @return List<WS1ItemDescription>
   */
  public static List<WS1ItemDescription> getWS1ItemDescriptions(List<ItemDescription> itemDescription) {
    if (itemDescription == null) {
      return null;
    }
    List<WS1ItemDescription> ws1ItemDescriptions = new ArrayList<WS1ItemDescription>();
    for (Iterator<ItemDescription> it = itemDescription.iterator(); it.hasNext();) {
      WS1ItemDescription ws1itemDescription = getWS1ItemDescription(it.next());
      if (ws1itemDescription != null) {
        ws1ItemDescriptions.add(ws1itemDescription);
      }
    }
    return ws1ItemDescriptions;
  }

  /**
   * Gets the WS2 reset properties list.
   * 
   * @param List<WS1ResetProperty>
   * @return List<ResetProperty>
   */
  public static List<ResetProperty> getWS2ResetProperties(List<WS1ResetProperty> ws1resetProperties) {
    if (ws1resetProperties == null) {
      return null;
    }
    List<ResetProperty> resetProperties = new ArrayList<ResetProperty>();
    for (Iterator<WS1ResetProperty> it = ws1resetProperties.iterator(); it.hasNext();) {
      ResetProperty resetProperty = getWS2ResetProperty(it.next());
      if (resetProperty != null) {
        resetProperties.add(resetProperty);
      }
    }
    return resetProperties;
  }

  /**
   * Gets the WS1 reset property list.
   * 
   * @param WS1PropertyList
   * @return PropertyList
   */
  public static PropertyList getWS2PropertyList(WS1PropertyList ws1propertyList) {
    if (ws1propertyList == null) {
      return null;
    }
    PropertyList propertyList = new PropertyList();
    propertyList.getProperties().addAll(getWS2Properties(ws1propertyList.getProperties()));
    propertyList.getResetProperties().addAll(getWS2ResetProperties(ws1propertyList.getResetProperties()));
    propertyList.getExtensions().addAll(getWS2Extensions(ws1propertyList.getExtensions()));
    return propertyList;
  }

  /**
   * Gets the WS2 reset property.
   * 
   * @param WS1ResetProperty
   * @return ResetProperty
   */
  public static ResetProperty getWS2ResetProperty(WS1ResetProperty ws1resetProperty) {
    if (ws1resetProperty == null) {
      return null;
    }
    ResetProperty resetProperty = new ResetProperty();
    resetProperty.setName(new QName(ws1resetProperty.getName()));
    return resetProperty;
  }

  /**
   * Gets the WS1 reset properties list.
   * 
   * @param List<ResetProperty>
   * @return List<WS1ResetProperty>
   */
  public static List<WS1ResetProperty> getWS1ResetProperties(List<ResetProperty> resetProperties) {
    if (resetProperties == null) {
      return null;
    }
    List<WS1ResetProperty> ws1resetProperties = new ArrayList<WS1ResetProperty>();
    for (Iterator<ResetProperty> it = resetProperties.iterator(); it.hasNext();) {
      WS1ResetProperty ws1resetProperty = getWS1ResetProperty(it.next());
      if (ws1resetProperty != null) {
        ws1resetProperties.add(ws1resetProperty);
      }
    }
    return ws1resetProperties;
  }

  /**
   * Gets the WS1 reset property list.
   * 
   * @param PropertyList
   * @return WS1PropertyList
   */
  public static WS1PropertyList getWS1PropertyList(PropertyList propertyList) {
    if (propertyList == null) {
      return null;
    }
    WS1PropertyList ws1propertyList = new WS1PropertyList();
    ws1propertyList.getProperties().addAll(getWS1Properties(propertyList.getProperties()));
    ws1propertyList.getResetProperties().addAll(getWS1ResetProperties(propertyList.getResetProperties()));
    ws1propertyList.getExtensions().addAll(getWS1Extensions(propertyList.getExtensions()));
    return ws1propertyList;
  }

  /**
   * Gets the WS1 reset property.
   * 
   * @param ResetProperty
   * @return WS1ResetProperty
   */
  public static WS1ResetProperty getWS1ResetProperty(ResetProperty resetProperty) {
    if (resetProperty == null) {
      return null;
    }
    WS1ResetProperty ws1resetProperty = new WS1ResetProperty();
    ws1resetProperty.setName(resetProperty.getName().getNamespaceURI());
    return ws1resetProperty;
  }

  /**
   * Gets the WS1  properties list.
   * 
   * @param List<Property>
   * @return List<WS1Property>
   */
  public static List<WS1Property> getWS1Properties(List<Property> properties) {
    if (properties == null) {
      return null;
    }
    List<WS1Property> ws1properties = new ArrayList<WS1Property>();
    for (Iterator<Property> it = properties.iterator(); it.hasNext();) {
      WS1Property ws1property = getWS1Property(it.next());
      if (ws1property != null) {
        ws1properties.add(ws1property);
      }
    }
    return ws1properties;
  }

  /**
   * Gets the WS1  property.
   * 
   * @param Property
   * @return WS1Property
   */
  public static WS1Property getWS1Property(Property property) {
    if (property == null) {
      return null;
    }
    WS1Property ws1property = new WS1Property();
    ws1property.setLang(property.getLang());
    ws1property.setName(property.getName().getLocalPart());
    ws1property.setStringValue(property.getStringValue());
    return ws1property;
  }

  /**
   * Gets the WS1 templates.
   * 
   * @param Templates
   * @return WS1Templates
   */
  public static WS1Templates getWS1Templates(Templates templates) {
    if (templates == null) {
      return null;
    }
    WS1Templates ws1templates = new WS1Templates();
    ws1templates.setBlockingActionTemplate(templates.getBlockingActionTemplate());
    ws1templates.setDefaultTemplate(templates.getDefaultTemplate());
    ws1templates.setRenderTemplate(templates.getRenderTemplate());
    ws1templates.setResourceTemplate(templates.getResourceTemplate());
    ws1templates.setSecureBlockingActionTemplate(templates.getSecureBlockingActionTemplate());
    ws1templates.setSecureDefaultTemplate(templates.getSecureDefaultTemplate());
    ws1templates.setSecureRenderTemplate(templates.getSecureRenderTemplate());
    ws1templates.setSecureResourceTemplate(templates.getSecureResourceTemplate());
    return ws1templates;
  }

  /**
   * Gets the WS1 service description.
   * 
   * @param ServiceDescription
   * @return WS1ServiceDescription
   */
  public static WS1ServiceDescription getWS1ServiceDescription(ServiceDescription sd) {
    if (sd == null) {
      return null;
    }
    WS1ServiceDescription ws1sd = new WS1ServiceDescription();
    ws1sd.setRegistrationPropertyDescription(getWS1ModelDescription(sd.getRegistrationPropertyDescription()));
    ws1sd.setRequiresInitCookie(getWS1CookieProtocol(sd.getRequiresInitCookie()));
    ws1sd.setRequiresRegistration(sd.isRequiresRegistration());
    ws1sd.setResourceList(getWS1ResourceList(sd.getResourceList()));
    ws1sd.getCustomModeDescriptions().addAll(getWS1ItemDescriptions(sd.getCustomModeDescriptions()));
    ws1sd.getCustomWindowStateDescriptions().addAll(getWS1ItemDescriptions(sd.getCustomWindowStateDescriptions()));
    ws1sd.getExtensions().addAll(getWS1Extensions(sd.getExtensions()));
    ws1sd.getOfferedPortlets().addAll(getWS1PortletDescriptions(sd.getOfferedPortlets()));
    ws1sd.getUserCategoryDescriptions().addAll(getWS1ItemDescriptions(sd.getUserCategoryDescriptions()));
    if (sd.getLocales() != null)
      ws1sd.getLocales().addAll(sd.getLocales());
    return ws1sd;
  }

  /**
   * Gets the WS1 registration context.
   * 
   * @param RegistrationContext
   * @return WS1RegistrationContext
   */
  public static WS1RegistrationContext getWS1RegistrationContext(RegistrationContext rc) {
    if (rc == null) {
      return null;
    }
    WS1RegistrationContext ws1rContext = new WS1RegistrationContext();
    ws1rContext.setRegistrationHandle(rc.getRegistrationHandle());
    ws1rContext.setRegistrationState(rc.getRegistrationState());
    return ws1rContext;
  }

  /**
   * Gets the WS1 registration data.
   * 
   * @param RegistrationData
   * @return WS1RegistrationData
   */
  public static WS1RegistrationData getWS1RegistrationData(RegistrationData rd) {
    if (rd == null) {
      return null;
    }
    WS1RegistrationData ws1rData = new WS1RegistrationData();
    ws1rData.setConsumerAgent(rd.getConsumerAgent());
    ws1rData.setConsumerName(rd.getConsumerName());
    ws1rData.setMethodGetSupported(rd.isMethodGetSupported());
    ws1rData.getConsumerModes().addAll(rd.getConsumerModes());
    ws1rData.getConsumerUserScopes().addAll(rd.getConsumerUserScopes());
    ws1rData.getConsumerWindowStates().addAll(rd.getConsumerWindowStates());
    List<WS1Property> ws1rProperties = getWS1Properties(rd.getRegistrationProperties());
    ws1rData.getRegistrationProperties().addAll(ws1rProperties);
    return ws1rData;
  }

  /**
   * Gets the WS1 destroy failed list.
   * 
   * @param List<FailedPortlets>
   * @return List<WS1DestroyFailed>
   */
  public static List<WS1DestroyFailed> getWS1DestroyFailed(List<FailedPortlets> fp) {
    if (fp == null) {
      return null;
    }
    List<WS1DestroyFailed> ws1destroyfaileds = new ArrayList<WS1DestroyFailed>();
    for (Iterator<FailedPortlets> it = fp.iterator(); it.hasNext();) {
      WS1DestroyFailed ws1destroyFailed = getWS1DestroyFailed(it.next());
      if (ws1destroyFailed != null) {
        ws1destroyfaileds.add(ws1destroyFailed);
      }
    }
    return ws1destroyfaileds;
  }

  /**
   * Gets the WS1 destroy failed.
   * 
   * @param FailedPortlets
   * @return WS1DestroyFailed
   */
  public static WS1DestroyFailed getWS1DestroyFailed(FailedPortlets fp) {
    if (fp == null) {
      return null;
    }
    WS1DestroyFailed ws1destroyFailed = new WS1DestroyFailed();
    if (fp.getPortletHandles() != null && fp.getPortletHandles().size() != 0)
      ws1destroyFailed.setPortletHandle(fp.getPortletHandles().get(0));
    ws1destroyFailed.setReason(fp.getReason().getValue());
    return ws1destroyFailed;
  }

  /**
   * Gets the WS1 runtime context.
   * 
   * @param RuntimeContext
   * @return WS1RuntimeContext
   */
  public static WS1RuntimeContext getWS1RuntimeContext(RuntimeContext runtimeContext) {
    if (runtimeContext == null) {
      return null;
    }
    WS1RuntimeContext ws1RuntimeContext = new WS1RuntimeContext();
    ws1RuntimeContext.setUserAuthentication(runtimeContext.getUserAuthentication());
    ws1RuntimeContext.setPortletInstanceKey(runtimeContext.getPortletInstanceKey());
    ws1RuntimeContext.setNamespacePrefix(runtimeContext.getNamespacePrefix());
    ws1RuntimeContext.setTemplates(getWS1Templates(runtimeContext.getTemplates()));
    ws1RuntimeContext.setSessionID(runtimeContext.getSessionParams().getSessionID());
    ws1RuntimeContext.getExtensions().addAll(getWS1Extensions(runtimeContext.getExtensions()));
    return ws1RuntimeContext;
  }

  /**
   * Gets the WS1 runtime context.
   * 
   * @param UserContext
   * @return WS1UserContext
   */
  public static WS1UserContext getWS1UserContext(UserContext userContext) {
    if (userContext == null) {
      return null;
    }
    WS1UserContext ws1UserContext = new WS1UserContext();
    ws1UserContext.setUserContextKey(userContext.getUserContextKey());
    ws1UserContext.getUserCategories().addAll(userContext.getUserCategories());
    ws1UserContext.setProfile(getWS1Profile(userContext.getProfile()));
    ws1UserContext.getExtensions().addAll(getWS1Extensions(userContext.getExtensions()));
    return null;
  }

  /**
   * Gets the WS1 user profile.
   * 
   * @param UserProfile
   * @return WS1UserProfile
   */
  public static WS1UserProfile getWS1Profile(UserProfile profile) {
    if (profile == null) {
      return null;
    }
    WS1UserProfile ws1UserProfile = new WS1UserProfile();
    ws1UserProfile.setName(getWS1PersonName(profile.getName()));
    ws1UserProfile.setBdate(profile.getBdate());
    ws1UserProfile.setGender(profile.getGender());
    ws1UserProfile.setEmployerInfo(getWS1EmployerInfo(profile.getEmployerInfo()));
    ws1UserProfile.setHomeInfo(getWS1Contact(profile.getHomeInfo()));
    ws1UserProfile.setBusinessInfo(getWS1Contact(profile.getBusinessInfo()));
    ws1UserProfile.getExtensions().addAll(getWS1Extensions(profile.getExtensions()));
    return null;
  }

  /**
   * Gets the WS1 contact.
   * 
   * @param Contact
   * @return WS1Contact
   */
  public static WS1Contact getWS1Contact(Contact homeInfo) {
    if (homeInfo == null) {
      return null;
    }
    WS1Contact ws1Contact = new WS1Contact();
    ws1Contact.setPostal(getWS1Postal(homeInfo.getPostal()));
    ws1Contact.setTelecom(getWS1Telacom(homeInfo.getTelecom()));
    ws1Contact.setOnline(getWS1Online(homeInfo.getOnline()));
    ws1Contact.getExtensions().addAll(getWS1Extensions(homeInfo.getExtensions()));
    return ws1Contact;
  }

  /**
   * Gets the WS1 online.
   * 
   * @param Online
   * @return WS1Online
   */
  public static WS1Online getWS1Online(Online online) {
    if (online == null) {
      return null;
    }
    WS1Online ws1Online = new WS1Online();
    ws1Online.setEmail(online.getEmail());
    ws1Online.setUri(online.getUri());
    ws1Online.getExtensions().addAll(getWS1Extensions(online.getExtensions()));
    return ws1Online;
  }

  /**
   * Gets the WS1 online.
   * 
   * @param Telecom
   * @return WS1Telecom
   */
  public static WS1Telecom getWS1Telacom(Telecom telecom) {
    if (telecom == null) {
      return null;
    }
    WS1Telecom ws1Telecom = new WS1Telecom();
    ws1Telecom.setTelephone(getWS1TelephoneNum(telecom.getTelephone()));
    ws1Telecom.setFax(getWS1TelephoneNum(telecom.getFax()));
    ws1Telecom.setMobile(getWS1TelephoneNum(telecom.getMobile()));
    ws1Telecom.setPager(getWS1TelephoneNum(telecom.getPager()));
    ws1Telecom.getExtensions().addAll(getWS1Extensions(telecom.getExtensions()));
    return ws1Telecom;
  }

  /**
   * Gets the WS1 telephone num.
   * 
   * @param TelephoneNum
   * @return WS1TelephoneNum
   */
  public static WS1TelephoneNum getWS1TelephoneNum(TelephoneNum telephone) {
    if (telephone == null) {
      return null;
    }
    WS1TelephoneNum ws1TelephoneNum = new WS1TelephoneNum();
    ws1TelephoneNum.setIntcode(telephone.getIntcode());
    ws1TelephoneNum.setLoccode(telephone.getLoccode());
    ws1TelephoneNum.setNumber(telephone.getNumber());
    ws1TelephoneNum.setExt(telephone.getExt());
    ws1TelephoneNum.setComment(telephone.getComment());
    return ws1TelephoneNum;
  }

  /**
   * Gets the WS1 portal.
   * 
   * @param Postal
   * @return WS1Postal
   */
  public static WS1Postal getWS1Postal(Postal postal) {
    if (postal == null) {
      return null;
    }
    WS1Postal ws1Postal = new WS1Postal();
    ws1Postal.setName(postal.getName());
    ws1Postal.setStreet(postal.getStreet());
    ws1Postal.setCity(postal.getCity());
    ws1Postal.setStateprov(postal.getStateprov());
    ws1Postal.setPostalcode(postal.getPostalcode());
    ws1Postal.setCountry(postal.getCountry());
    ws1Postal.setOrganization(postal.getOrganization());
    ws1Postal.getExtensions().addAll(getWS1Extensions(postal.getExtensions()));
    return ws1Postal;
  }

  /**
   * Gets the WS1 employer info.
   * 
   * @param EmployerInfo
   * @return WS1EmployerInfo
   */
  public static WS1EmployerInfo getWS1EmployerInfo(EmployerInfo employerInfo) {
    if (employerInfo == null) {
      return null;
    }
    WS1EmployerInfo ws1EmployerInfo = new WS1EmployerInfo();
    ws1EmployerInfo.setEmployer(employerInfo.getEmployer());
    ws1EmployerInfo.setDepartment(employerInfo.getDepartment());
    ws1EmployerInfo.setJobtitle(employerInfo.getJobtitle());
    ws1EmployerInfo.getExtensions().addAll(getWS1Extensions(employerInfo.getExtensions()));
    return ws1EmployerInfo;
  }

  /**
   * Gets the WS1 person name.
   * 
   * @param PersonName
   * @return WS1PersonName
   */
  public static WS1PersonName getWS1PersonName(PersonName name) {
    if (name == null) {
      return null;
    }
    WS1PersonName ws1PersonName = new WS1PersonName();
    ws1PersonName.setPrefix(name.getPrefix());
    ws1PersonName.setGiven(name.getGiven());
    ws1PersonName.setFamily(name.getFamily());
    ws1PersonName.setMiddle(name.getMiddle());
    ws1PersonName.setSuffix(name.getSuffix());
    ws1PersonName.setNickname(name.getNickname());
    ws1PersonName.getExtensions().addAll(getWS1Extensions(name.getExtensions()));
    return ws1PersonName;
  }

  /**
   * Gets the WS1 markup params.
   * 
   * @param MarkupParams
   * @return WS1MarkupParams
   */
  public static WS1MarkupParams getWS1MarkupParams(MarkupParams markupParams) {
    if (markupParams == null) {
      return null;
    }
    WS1MarkupParams ws1MarkupParams = new WS1MarkupParams();
    ws1MarkupParams.setSecureClientCommunication(markupParams.isSecureClientCommunication());
    ws1MarkupParams.getLocales().addAll(markupParams.getLocales());
    ws1MarkupParams.getMimeTypes().addAll(markupParams.getMimeTypes());
    ws1MarkupParams.setMode(markupParams.getMode());
    ws1MarkupParams.setWindowState(markupParams.getWindowState());
    ws1MarkupParams.setClientData(getWS1ClientData(markupParams.getClientData()));
    ws1MarkupParams.setNavigationalState(markupParams.getNavigationalContext().getOpaqueValue());
    ws1MarkupParams.getMarkupCharacterSets().addAll(markupParams.getMarkupCharacterSets());
    ws1MarkupParams.setValidateTag(markupParams.getValidateTag());
    ws1MarkupParams.getValidNewModes().addAll(markupParams.getValidNewModes());
    ws1MarkupParams.getValidNewWindowStates().addAll(markupParams.getValidNewWindowStates());
    ws1MarkupParams.getExtensions().addAll(getWS1Extensions(markupParams.getExtensions()));
    return ws1MarkupParams;
  }

  /**
   * Gets the WS1 client data.
   * 
   * @param ClientData
   * @return WS1ClientData
   */
  public static WS1ClientData getWS1ClientData(ClientData clientData) {
    if (clientData == null) {
      return null;
    }
    WS1ClientData ws1ClientData = new WS1ClientData();
    ws1ClientData.setUserAgent(clientData.getUserAgent());
    ws1ClientData.getExtensions().addAll(getWS1Extensions(clientData.getExtensions()));
    return ws1ClientData;
  }

  /**
   * Gets the WS1 interaction params.
   * 
   * @param InteractionParams
   * @return WS1InteractionParams
   */
  public static WS1InteractionParams getWS1InteractionParams(InteractionParams interactionParams) {
    if (interactionParams == null) {
      return null;
    }
    WS1InteractionParams ws1InteractionParams = new WS1InteractionParams();
    ws1InteractionParams.setPortletStateChange(getWS1StateChange(interactionParams.getPortletStateChange()));
    ws1InteractionParams.setInteractionState(interactionParams.getInteractionState());
    ws1InteractionParams.getFormParameters().addAll(getWS1NamedStrings(interactionParams.getFormParameters()));
    ws1InteractionParams.getUploadContexts().addAll(getWS1UploadContexts(interactionParams.getUploadContexts()));
    ws1InteractionParams.getExtensions().addAll(getWS1Extensions(interactionParams.getExtensions()));
    return ws1InteractionParams;
  }

  /**
   * Gets the WS1 upload contexts list.
   * 
   * @param List<UploadContext>
   * @return List<WS1UploadContext>
   */
  public static List<WS1UploadContext> getWS1UploadContexts(List<UploadContext> uploadContexts) {
    if (uploadContexts == null) {
      return null;
    }
    List<WS1UploadContext> ws1uploadContexts = new ArrayList<WS1UploadContext>();
    for (Iterator<UploadContext> it = uploadContexts.iterator(); it.hasNext();) {
      WS1UploadContext ws1uploadContext = getWS1UploadContext(it.next());
      if (ws1uploadContext != null) {
        ws1uploadContexts.add(ws1uploadContext);
      }
    }
    return ws1uploadContexts;
  }

  /**
   * Gets the WS1 upload context.
   * 
   * @param UploadContext
   * @return WS1UploadContext
   */
  public static WS1UploadContext getWS1UploadContext(UploadContext uploadContext) {
    WS1UploadContext ws1UploadContext = new WS1UploadContext();
    ws1UploadContext.setMimeType(uploadContext.getMimeType());
    ws1UploadContext.setUploadData(uploadContext.getUploadData());
    ws1UploadContext.getMimeAttributes().addAll(getWS1NamedStrings(uploadContext.getMimeAttributes()));
    ws1UploadContext.getExtensions().addAll(getWS1Extensions(uploadContext.getExtensions()));
    return ws1UploadContext;
  }

  /**
   * Gets the WS1 named strings list.
   * 
   * @param List<NamedString>
   * @return List<WS1NamedString>
   */
  public static List<WS1NamedString> getWS1NamedStrings(List<NamedString> namedStrings) {
    if (namedStrings == null) {
      return null;
    }
    List<WS1NamedString> ws1namedStrings = new ArrayList<WS1NamedString>();
    for (Iterator<NamedString> it = namedStrings.iterator(); it.hasNext();) {
      WS1NamedString ws1namedString = getWS1NamedString(it.next());
      if (ws1namedString != null) {
        ws1namedStrings.add(ws1namedString);
      }
    }
    return ws1namedStrings;
  }

  /**
   * Gets the WS1 named string.
   * 
   * @param NamedString
   * @return WS1NamedString
   */
  public static WS1NamedString getWS1NamedString(NamedString namedString) {
    WS1NamedString ws1NamedString = new WS1NamedString();
    ws1NamedString.setName(namedString.getName());
    ws1NamedString.setValue(namedString.getValue());
    return ws1NamedString;
  }

  /**
   * Gets the WS1 state change.
   * 
   * @param StateChange
   * @return WS1StateChange
   */
  public static WS1StateChange getWS1StateChange(StateChange portletStateChange) {
    if (portletStateChange == null) {
      return null;
    }
    return WS1StateChange.fromValue(portletStateChange.value());
  }

  /**
   * Gets the WS1 failed portlets list.
   * 
   * @param List<WS1DestroyFailed>
   * @return List<FailedPortlets>
   */
  public static List<FailedPortlets> getWS2FailedPortlets(List<WS1DestroyFailed> destroyFaileds) {
    if (destroyFaileds == null) {
      return null;
    }
    List<FailedPortlets> failedPortletss = new ArrayList<FailedPortlets>();
    for (Iterator<WS1DestroyFailed> it = destroyFaileds.iterator(); it.hasNext();) {
      FailedPortlets failedPortlets = getWS2FailedPortlets(it.next());
      if (failedPortlets != null) {
        failedPortletss.add(failedPortlets);
      }
    }
    return failedPortletss;
  }

  /**
   * Gets the WS2 failed portlets.
   * 
   * @param WS1DestroyFailed
   * @return FailedPortlets
   */
  public static FailedPortlets getWS2FailedPortlets(WS1DestroyFailed ws1DestroyFailed) {
    if (ws1DestroyFailed == null) {
      return null;
    }
    FailedPortlets failedPortlets = new FailedPortlets();
    //    failedPortlets.setErrorCode(value);
    failedPortlets.getPortletHandles().add(ws1DestroyFailed.getPortletHandle());
    LocalizedString reason = new LocalizedString();
    reason.setValue(ws1DestroyFailed.getReason());
    failedPortlets.setReason(reason);
//        failedPortlets.setResourceList(value);
    return failedPortlets;
  }

  /**
   * Gets the WS2 servide description.
   * 
   * @param WS1GetServiceDescription
   * @return GetServiceDescription
   */
  public static GetServiceDescription getWS2GetServiceDescription(WS1GetServiceDescription ws1GetServiceDescription) {
    GetServiceDescription getServiceDescription = new GetServiceDescription();
    getServiceDescription.setRegistrationContext(getWS2RegistrationContext(ws1GetServiceDescription
        .getRegistrationContext()));
    //    getServiceDescription.setUserContext());
    getServiceDescription.getDesiredLocales().addAll(ws1GetServiceDescription.getDesiredLocales());
    getServiceDescription.getPortletHandles();
    return getServiceDescription;
  }

  /**
   * Gets the WS2 markup.
   * 
   * @param WS1GetMarkup
   * @return GetMarkup
   */
  public static GetMarkup getWS2GetMarkup(WS1GetMarkup ws1GetMarkup) {
    GetMarkup getMarkup = new GetMarkup();
    getMarkup.setMarkupParams(getWS2MarkupParams(ws1GetMarkup.getMarkupParams()));
    getMarkup.setPortletContext(getWS2PortletContext(ws1GetMarkup.getPortletContext()));
    getMarkup.setRegistrationContext(getWS2RegistrationContext(ws1GetMarkup.getRegistrationContext()));
    getMarkup.setRuntimeContext(getWS2RuntimeContext(ws1GetMarkup.getRuntimeContext()));
    //    getMarkup.setUserContext(value);
    return getMarkup;
  }

  /**
   * Gets the WS1 markup response.
   * 
   * @param MarkupResponse
   * @return WS1MarkupResponse
   */
  public static WS1MarkupResponse getWS1MarkupResponse(MarkupResponse ws2MarkupResponse) {
    WS1MarkupResponse ws1MarkupResponse = new WS1MarkupResponse();
    ws1MarkupResponse.setMarkupContext(getWS1MarkupContext(ws2MarkupResponse.getMarkupContext()));
    ws1MarkupResponse.setSessionContext(getWS1SessionContext(ws2MarkupResponse.getSessionContext()));
    try {
      ws1MarkupResponse.getExtensions().addAll(getWS1Extensions(ws2MarkupResponse.getExtensions()));
    } catch (NullPointerException npe) {
    }
    return ws1MarkupResponse;
  }

  /**
   * Gets the WS2 clone portlet.
   * 
   * @param WS1ClonePortlet
   * @return ClonePortlet
   */
  public static ClonePortlet getWS2ClonePortlet(WS1ClonePortlet ws1ClonePortlet) {
    ClonePortlet clonePortlet = new ClonePortlet();
    clonePortlet.setPortletContext(getWS2PortletContext(ws1ClonePortlet.getPortletContext()));
    clonePortlet.setRegistrationContext(getWS2RegistrationContext(ws1ClonePortlet.getRegistrationContext()));
    clonePortlet.setUserContext(getWS2UserContext(ws1ClonePortlet.getUserContext()));
    //    clonePortlet.setLifetime(value);
    return clonePortlet;
  }

  /**
   * Gets the WS2 set portlet properties.
   * 
   * @param WS1SetPortletProperties
   * @return SetPortletProperties
   */
  public static SetPortletProperties getWS2SetPortletProperties(WS1SetPortletProperties ws1SetPortletProperties) {
    SetPortletProperties setPortletProperties = new SetPortletProperties();
    setPortletProperties.setPortletContext(getWS2PortletContext(ws1SetPortletProperties.getPortletContext()));
    setPortletProperties.setRegistrationContext(getWS2RegistrationContext(ws1SetPortletProperties
        .getRegistrationContext()));
    setPortletProperties.setUserContext(getWS2UserContext(ws1SetPortletProperties.getUserContext()));
    setPortletProperties.setPropertyList(getWS2PropertyList(ws1SetPortletProperties.getPropertyList()));
    return setPortletProperties;
  }

  /**
   * Gets the WS2 perform blocking interaction.
   * 
   * @param WS1PerformBlockingInteraction
   * @return PerformBlockingInteraction
   */
  public static PerformBlockingInteraction getWS2PerformBlockingInteraction(WS1PerformBlockingInteraction ws1PerformBlockingInteraction) {
    PerformBlockingInteraction performBlockingInteraction = new PerformBlockingInteraction();
    performBlockingInteraction.setInteractionParams(getWS2InteractionParams(ws1PerformBlockingInteraction
        .getInteractionParams()));
    performBlockingInteraction.setMarkupParams(getWS2MarkupParams(ws1PerformBlockingInteraction.getMarkupParams()));
    performBlockingInteraction
        .setPortletContext(getWS2PortletContext(ws1PerformBlockingInteraction.getPortletContext()));
    performBlockingInteraction.setRegistrationContext(getWS2RegistrationContext(ws1PerformBlockingInteraction
        .getRegistrationContext()));
    performBlockingInteraction
        .setRuntimeContext(getWS2RuntimeContext(ws1PerformBlockingInteraction.getRuntimeContext()));
    performBlockingInteraction.setUserContext(getWS2UserContext(ws1PerformBlockingInteraction.getUserContext()));
    return performBlockingInteraction;
  }

  /**
   * Gets the WS1 blocking interaction response.
   * 
   * @param BlockingInteractionResponse
   * @return WS1BlockingInteractionResponse
   */
  public static WS1BlockingInteractionResponse getWS1BlockingInteractionResponse(BlockingInteractionResponse ws2PerformBlockingInteraction) {
    WS1BlockingInteractionResponse ws1BlockingInteractionResponse = new WS1BlockingInteractionResponse();
    ws1BlockingInteractionResponse.setRedirectURL(ws2PerformBlockingInteraction.getRedirectURL());
    ws1BlockingInteractionResponse.setUpdateResponse(getWS1UpdateResponse(ws2PerformBlockingInteraction
        .getUpdateResponse()));
    ws1BlockingInteractionResponse.getExtensions().addAll(
        getWS1Extensions(ws2PerformBlockingInteraction.getExtensions()));
    return ws1BlockingInteractionResponse;
  }

  /**
   * Gets the WS2 portlet properties.
   * 
   * @param WS1GetPortletProperties
   * @return GetPortletProperties
   */
  public static GetPortletProperties getWS2GetPortletProperties(WS1GetPortletProperties ws1GetPortletProperties) {
    GetPortletProperties getPortletProperties = new GetPortletProperties();
    getPortletProperties.setPortletContext(getWS2PortletContext(ws1GetPortletProperties.getPortletContext()));
    getPortletProperties.setRegistrationContext(getWS2RegistrationContext(ws1GetPortletProperties
        .getRegistrationContext()));
    getPortletProperties.setUserContext(getWS2UserContext(ws1GetPortletProperties.getUserContext()));
    getPortletProperties.getNames().addAll(ws1GetPortletProperties.getNames());
    return getPortletProperties;
  }

  /**
   * Gets the WS2 destroy portlets.
   * 
   * @param WS1DestroyPortlets
   * @return DestroyPortlets
   */
  public static DestroyPortlets getWS2DestroyPortlets(WS1DestroyPortlets ws1DestroyPortlets) {
    DestroyPortlets destroyPortlets = new DestroyPortlets();
    destroyPortlets.setRegistrationContext(getWS2RegistrationContext(ws1DestroyPortlets.getRegistrationContext()));
    //    destroyPortlets.setUserContext();
    destroyPortlets.getPortletHandles().addAll(ws1DestroyPortlets.getPortletHandles());
    return destroyPortlets;
  }

  /**
   * Gets the WS1 destroy portlets response.
   * 
   * @param DestroyPortletsResponse
   * @return WS1DestroyPortletsResponse
   */
  public static WS1DestroyPortletsResponse getWS1DestroyPortletsResponse(DestroyPortletsResponse ws2DestroyPortlets) {
    WS1DestroyPortletsResponse ws1DestroyPortletsResponse = new WS1DestroyPortletsResponse();
    ws1DestroyPortletsResponse.getDestroyFailed().addAll(getWS1DestroyFailed(ws2DestroyPortlets.getFailedPortlets()));
    ws1DestroyPortletsResponse.getExtensions().addAll(getWS1Extensions(ws2DestroyPortlets.getExtensions()));
    return ws1DestroyPortletsResponse;
  }

  /**
   * Gets the WS2 release sessions.
   * 
   * @param WS1ReleaseSessions
   * @return ReleaseSessions
   */
  public static ReleaseSessions getWS2ReleaseSessions(WS1ReleaseSessions ws1ReleaseSessions) {
    ReleaseSessions releaseSessions = new ReleaseSessions();
    releaseSessions.setRegistrationContext(getWS2RegistrationContext(ws1ReleaseSessions.getRegistrationContext()));
    //    releaseSessions.setUserContext();
    releaseSessions.getSessionIDs().addAll(ws1ReleaseSessions.getSessionIDs());
    return releaseSessions;
  }

  /**
   * Gets the WS2 modify registration.
   * 
   * @param WS1ModifyRegistration
   * @return ModifyRegistration
   */
  public static ModifyRegistration getWS2ModifyRegistration(WS1ModifyRegistration ws1ModifyRegistration) {
    ModifyRegistration modifyRegistration = new ModifyRegistration();
    modifyRegistration
        .setRegistrationContext(getWS2RegistrationContext(ws1ModifyRegistration.getRegistrationContext()));
    modifyRegistration.setRegistrationData(getWS2RegistrationData(ws1ModifyRegistration.getRegistrationData()));
    //    modifyRegistration.setUserContext(value);
    return modifyRegistration;
  }

  /**
   * Gets the WS1 registration state.
   * 
   * @param RegistrationState
   * @return WS1RegistrationState
   */
  public static WS1RegistrationState getWS1RegistrationState(RegistrationState ws2ModifyRegistration) {
    WS1RegistrationState ws1RegistrationState = new WS1RegistrationState();
    ws1RegistrationState.setRegistrationState(ws2ModifyRegistration.getRegistrationState());
    ws1RegistrationState.getExtensions().addAll(getWS1Extensions(ws2ModifyRegistration.getExtensions()));
    return ws1RegistrationState;
  }

}
