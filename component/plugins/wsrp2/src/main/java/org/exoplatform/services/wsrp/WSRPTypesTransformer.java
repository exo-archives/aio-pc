package org.exoplatform.services.wsrp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.exoplatform.services.wsrp1.type.WS1CacheControl;
import org.exoplatform.services.wsrp1.type.WS1ClientData;
import org.exoplatform.services.wsrp1.type.WS1Contact;
import org.exoplatform.services.wsrp1.type.WS1CookieProtocol;
import org.exoplatform.services.wsrp1.type.WS1EmployerInfo;
import org.exoplatform.services.wsrp1.type.WS1Extension;
import org.exoplatform.services.wsrp1.type.WS1InteractionParams;
import org.exoplatform.services.wsrp1.type.WS1ItemDescription;
import org.exoplatform.services.wsrp1.type.WS1LocalizedString;
import org.exoplatform.services.wsrp1.type.WS1MarkupContext;
import org.exoplatform.services.wsrp1.type.WS1MarkupParams;
import org.exoplatform.services.wsrp1.type.WS1MarkupType;
import org.exoplatform.services.wsrp1.type.WS1ModelDescription;
import org.exoplatform.services.wsrp1.type.WS1ModelTypes;
import org.exoplatform.services.wsrp1.type.WS1NamedString;
import org.exoplatform.services.wsrp1.type.WS1Online;
import org.exoplatform.services.wsrp1.type.WS1PersonName;
import org.exoplatform.services.wsrp1.type.WS1PortletContext;
import org.exoplatform.services.wsrp1.type.WS1PortletDescription;
import org.exoplatform.services.wsrp1.type.WS1Postal;
import org.exoplatform.services.wsrp1.type.WS1Property;
import org.exoplatform.services.wsrp1.type.WS1PropertyDescription;
import org.exoplatform.services.wsrp1.type.WS1PropertyList;
import org.exoplatform.services.wsrp1.type.WS1RegistrationContext;
import org.exoplatform.services.wsrp1.type.WS1RegistrationData;
import org.exoplatform.services.wsrp1.type.WS1ResetProperty;
import org.exoplatform.services.wsrp1.type.WS1Resource;
import org.exoplatform.services.wsrp1.type.WS1ResourceList;
import org.exoplatform.services.wsrp1.type.WS1ResourceValue;
import org.exoplatform.services.wsrp1.type.WS1RuntimeContext;
import org.exoplatform.services.wsrp1.type.WS1ServiceDescription;
import org.exoplatform.services.wsrp1.type.WS1SessionContext;
import org.exoplatform.services.wsrp1.type.WS1StateChange;
import org.exoplatform.services.wsrp1.type.WS1Telecom;
import org.exoplatform.services.wsrp1.type.WS1TelephoneNum;
import org.exoplatform.services.wsrp1.type.WS1Templates;
import org.exoplatform.services.wsrp1.type.WS1UpdateResponse;
import org.exoplatform.services.wsrp1.type.WS1UploadContext;
import org.exoplatform.services.wsrp1.type.WS1UserContext;
import org.exoplatform.services.wsrp1.type.WS1UserProfile;
import org.exoplatform.services.wsrp2.type.CacheControl;
import org.exoplatform.services.wsrp2.type.ClientData;
import org.exoplatform.services.wsrp2.type.Contact;
import org.exoplatform.services.wsrp2.type.CookieProtocol;
import org.exoplatform.services.wsrp2.type.EmployerInfo;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.InteractionParams;
import org.exoplatform.services.wsrp2.type.ItemDescription;
import org.exoplatform.services.wsrp2.type.LocalizedString;
import org.exoplatform.services.wsrp2.type.MarkupContext;
import org.exoplatform.services.wsrp2.type.MarkupParams;
import org.exoplatform.services.wsrp2.type.MarkupType;
import org.exoplatform.services.wsrp2.type.ModelDescription;
import org.exoplatform.services.wsrp2.type.ModelTypes;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.NavigationalContext;
import org.exoplatform.services.wsrp2.type.Online;
import org.exoplatform.services.wsrp2.type.PersonName;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.Postal;
import org.exoplatform.services.wsrp2.type.Property;
import org.exoplatform.services.wsrp2.type.PropertyDescription;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.ResetProperty;
import org.exoplatform.services.wsrp2.type.Resource;
import org.exoplatform.services.wsrp2.type.ResourceList;
import org.exoplatform.services.wsrp2.type.ResourceValue;
import org.exoplatform.services.wsrp2.type.RuntimeContext;
import org.exoplatform.services.wsrp2.type.ServiceDescription;
import org.exoplatform.services.wsrp2.type.SessionContext;
import org.exoplatform.services.wsrp2.type.SessionParams;
import org.exoplatform.services.wsrp2.type.StateChange;
import org.exoplatform.services.wsrp2.type.Telecom;
import org.exoplatform.services.wsrp2.type.TelephoneNum;
import org.exoplatform.services.wsrp2.type.Templates;
import org.exoplatform.services.wsrp2.type.UpdateResponse;
import org.exoplatform.services.wsrp2.type.UploadContext;
import org.exoplatform.services.wsrp2.type.UserContext;
import org.exoplatform.services.wsrp2.type.UserProfile;

public class WSRPTypesTransformer {

  public static RegistrationContext getWS2RegistrationContext(WS1RegistrationContext ws1RegContext) {
    if (ws1RegContext == null) {
      return null;
    }
    RegistrationContext regContext = new RegistrationContext();
    regContext.setRegistrationHandle(ws1RegContext.getRegistrationHandle());
    regContext.setRegistrationState(ws1RegContext.getRegistrationState());
    return regContext;
  }

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

  public static Extension getWS2Extension(WS1Extension ws1Extension) {
    if (ws1Extension == null) {
      return null;
    }
    Extension extension = new Extension();
    extension.setAny(ws1Extension.getAny());
    return extension;
  }

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

  public static WS1Extension getWS1Extension(Extension ext) {
    if (ext == null) {
      return null;
    }
    WS1Extension ws1Extension = new WS1Extension();
    ws1Extension.setAny(ext.getAny());
    return ws1Extension;
  }

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
    return ws1UpdateResponse;
  }

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

  public static StateChange getWS2StateChange(WS1StateChange ws1StateChange) {
    if (ws1StateChange == null) {
      return null;
    }
    return StateChange.fromValue(ws1StateChange.value());
  }

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

  public static ClientData getWS2ClientData(WS1ClientData ws1ClientData) {
    if (ws1ClientData == null) {
      return null;
    }
    ClientData clientData = new ClientData();
    clientData.setUserAgent(ws1ClientData.getUserAgent());
    clientData.getExtensions().addAll(getWS2Extensions(ws1ClientData.getExtensions()));
    return clientData;
  }

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

  public static NamedString getWS2NamedString(WS1NamedString ws1NamedString) {
    if (ws1NamedString == null) {
      return null;
    }
    NamedString namedString = new NamedString();
    namedString.setName(ws1NamedString.getName());
    namedString.setValue(ws1NamedString.getValue());
    return namedString;
  }

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

  public static Online getWS2Online(WS1Online ws1Online) {
    if (ws1Online == null) {
      return null;
    }
    Online online = new Online();
    online.setEmail(ws1Online.getEmail());
    online.setUri(ws1Online.getUri());
    return online;
  }

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

  public static List<PortletDescription> getWS2PortletDescriptions(List<WS1PortletDescription> ws1portletDescriptions) {
    if (ws1portletDescriptions == null) {
      return null;
    }
    List<PortletDescription> portletDescriptions = new ArrayList<PortletDescription>();
    for (Iterator<WS1PortletDescription> it = ws1portletDescriptions.iterator(); it.hasNext();) {
      PortletDescription portletDescription = getWS2PortletDescription(it.next());
      if (portletDescription != null) {
        portletDescriptions.add(portletDescription);
      }
    }
    return portletDescriptions;
  }

  public static PortletDescription getWS2PortletDescription(WS1PortletDescription ws1portletDescription) {
    if (ws1portletDescription == null) {
      return null;
    }
    PortletDescription portletDescription = new PortletDescription();
    portletDescription.setDefaultMarkupSecure(ws1portletDescription.isDefaultMarkupSecure());
    portletDescription.setDescription(getWS2LocalizedString(ws1portletDescription.getDescription()));
    portletDescription.setDisplayName(getWS2LocalizedString(ws1portletDescription.getDisplayName()));
    portletDescription.setDoesUrlTemplateProcessing(ws1portletDescription.isDoesUrlTemplateProcessing());
    portletDescription.setGroupID(portletDescription.getGroupID());
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

  public static CookieProtocol getWS2CookieProtocol(WS1CookieProtocol ws1cookieProtocol) {
    if (ws1cookieProtocol == null) {
      return null;
    }
    return CookieProtocol.fromValue(ws1cookieProtocol.value());
  }

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

  public static ResourceList getWS2ResourceList(WS1ResourceList ws1resourceList) {
    if (ws1resourceList == null) {
      return null;
    }
    ResourceList resourceList = new ResourceList();
    resourceList.getExtensions().addAll(getWS2Extensions(ws1resourceList.getExtensions()));
    resourceList.getResources().addAll(getWS2Resources(ws1resourceList.getResources()));
    return resourceList;
  }

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

  public static ModelTypes getWS2ModelTypes(WS1ModelTypes ws1modelTypes) {
    if (ws1modelTypes == null) {
      return null;
    }
    ModelTypes modelTypes = new ModelTypes();
    modelTypes.setAny(ws1modelTypes.getAny());
    return modelTypes;
  }

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

  public static List<WS1PortletDescription> getWS1PortletDescriptions(List<PortletDescription> portletDescriptions) {
    if (portletDescriptions == null) {
      return null;
    }
    List<WS1PortletDescription> ws1portletDescriptions = new ArrayList<WS1PortletDescription>();
    for (Iterator<PortletDescription> it = portletDescriptions.iterator(); it.hasNext();) {
      WS1PortletDescription ws1portletDescription = getWS1PortletDescription(it.next());
      if (ws1portletDescription != null) {
        ws1portletDescriptions.add(ws1portletDescription);
      }
    }
    return ws1portletDescriptions;
  }

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

  public static WS1CookieProtocol getWS1CookieProtocol(CookieProtocol cookieProtocol) {
    if (cookieProtocol == null) {
      return null;
    }
    return WS1CookieProtocol.fromValue(cookieProtocol.value());
  }

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

  public static WS1ResourceList getWS1ResourceList(ResourceList resourceList) {
    if (resourceList == null) {
      return null;
    }
    WS1ResourceList ws1resourceList = new WS1ResourceList();
    ws1resourceList.getExtensions().addAll(getWS1Extensions(resourceList.getExtensions()));
    ws1resourceList.getResources().addAll(getWS1Resources(resourceList.getResources()));
    return ws1resourceList;
  }

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

  public static WS1ModelTypes getWS1ModelTypes(ModelTypes modelTypes) {
    if (modelTypes == null) {
      return null;
    }
    WS1ModelTypes ws1modelTypes = new WS1ModelTypes();
    ws1modelTypes.setAny(modelTypes.getAny());
    return ws1modelTypes;
  }

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

  public static List<WS1ItemDescription> getWS1ItemDescriptions(List<ItemDescription> itemDescription) {
    if (itemDescription == null) {
      return null;
    }
    List<WS1ItemDescription> ws1itemDescriptions = new ArrayList<WS1ItemDescription>();
    for (Iterator<ItemDescription> it = itemDescription.iterator(); it.hasNext();) {
      WS1ItemDescription ws1itemDescription = getWS1ItemDescription(it.next());
      if (ws1itemDescription != null) {
        ws1itemDescriptions.add(ws1itemDescription);
      }
    }
    return ws1itemDescriptions;
  }

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

  public static ResetProperty getWS2ResetProperty(WS1ResetProperty ws1resetProperty) {
    if (ws1resetProperty == null) {
      return null;
    }
    ResetProperty resetProperty = new ResetProperty();
    resetProperty.setName(new QName(ws1resetProperty.getName()));
    return resetProperty;
  }

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

  public static WS1ResetProperty getWS1ResetProperty(ResetProperty resetProperty) {
    if (resetProperty == null) {
      return null;
    }
    WS1ResetProperty ws1resetProperty = new WS1ResetProperty();
    ws1resetProperty.setName(resetProperty.getName().getNamespaceURI());
    return ws1resetProperty;
  }

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

  public static WS1Property getWS1Property(Property property) {
    if (property == null) {
      return null;
    }
    WS1Property ws1property = new WS1Property();
    ws1property.setLang(property.getLang());
    ws1property.setName(property.getName().toString());
    ws1property.setStringValue(property.getStringValue());
    return ws1property;
  }

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
    return ws1sd;
  }

  public static WS1RegistrationContext getWS1RegistrationContext(RegistrationContext rc) {
    if (rc == null) {
      return null;
    }
    WS1RegistrationContext ws1rContext = new WS1RegistrationContext();
    ws1rContext.setRegistrationHandle(rc.getRegistrationHandle());
    ws1rContext.setRegistrationState(rc.getRegistrationState());
    return ws1rContext;
  }

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

}
