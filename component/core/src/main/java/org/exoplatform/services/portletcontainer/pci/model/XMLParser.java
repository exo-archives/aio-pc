/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.pci.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.commons.xml.ExoXPPParser;
import org.exoplatform.services.log.ExoLogger;

/**
 * Jul 8, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: XMLParser.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class XMLParser {

  static boolean newSpec = false;
  static PortletApp portletapp;

  static public PortletApp parse(InputStream is) throws Exception {
    ExoXPPParser xpp = ExoXPPParser.getInstance();
    xpp.setInput(is, "UTF8");
    xpp.mandatoryNode("portlet-app");
    return readPortletApp(xpp);
  }

  static public PortletApp parse(InputStream is, boolean isnewer) throws Exception {
    newSpec = isnewer;
    ExoXPPParser xpp = ExoXPPParser.getInstance();
    xpp.setInput(is, "UTF8");
    xpp.mandatoryNode("portlet-app");
    return readPortletApp(xpp);
  }

  static public PortletApp readPortletApp(ExoXPPParser xpp ) throws Exception {
    portletapp = new PortletApp();
    portletapp.setVer2(newSpec);
    portletapp.setVersion(xpp.getNodeAttributeValue("version"));
    portletapp.setId(xpp.getNodeAttributeValue("id"));
    while(xpp.node("portlet")) portletapp.addPortlet(readPortlet(xpp));
    while(xpp.node("custom-portlet-mode")) portletapp.addCustomPortletMode(readCustomPortletMode(xpp));
    while(xpp.node("custom-window-state")) portletapp.addCustomWindowState(readCustomWindowState(xpp));
    while(xpp.node("user-attribute")) portletapp.addUserAttribute(readUserAttribute(xpp));
    while(xpp.node("security-constraint")) portletapp.addSecurityConstraint(readSecurityConstraint(xpp));
    // portlet api 2.0
    if (newSpec) {
      if(xpp.node("resource-bundle")) portletapp.setResourceBundle(xpp.getContent().trim());
      while(xpp.node("filter")) portletapp.addFilter(readFilter(xpp));
      while(xpp.node("filter-mapping")) portletapp.addFilterMapping(readFilterMapping(xpp));
      while(xpp.node("default-namespace")) portletapp.setDefaultNamespace(xpp.getContent().trim());
      fillPredefinedEvents(portletapp);
      while(xpp.node("event-definition")) portletapp.addEventDefinition(readEventDefinition(xpp));
      while(xpp.node("public-render-parameter")) portletapp.addPublicRenderParameter(readPublicRenderParameter(xpp));
      while(xpp.node("container-runtime-option")) portletapp.addContainerRuntimeOption(readContainerRuntimeOption(xpp));
    }
    // setting up default namespace for names without one
    if (!portletapp.getDefaultNamespace().equals(javax.xml.XMLConstants.NULL_NS_URI)) {
      String whatToFind = javax.xml.XMLConstants.NULL_NS_URI;
      String whatToReplaceTo = portletapp.getDefaultNamespace();
      List<EventDefinition> events = portletapp.getEventDefinition();
      for (Iterator<EventDefinition> i = events.iterator(); i.hasNext(); ) {
        EventDefinition ed = i.next();
        if (ed.getPrefferedName().getNamespaceURI().equals(whatToFind))
          ed.setPrefferedName(new QName(whatToReplaceTo, ed.getPrefferedName().getLocalPart()));
        ed.setAliases(setNamespaceIfAbsent(whatToFind, whatToReplaceTo, ed.getAliases()));
      }
      List<Portlet> plts = portletapp.getPortlet();
      for (Iterator<Portlet> i = plts.iterator(); i.hasNext(); ) {
        Portlet p = i.next();
        p.setSupportedProcessingEvent(setNamespaceIfAbsent(whatToFind, whatToReplaceTo, p.getSupportedProcessingEvent()));
        p.setSupportedPublishingEvent(setNamespaceIfAbsent(whatToFind, whatToReplaceTo, p.getSupportedPublishingEvent()));
      }
    }
    return portletapp;
  }

  private static List<QName> setNamespaceIfAbsent(String whatToFind, String whatToReplaceTo, List<QName> aliases) {
    if (aliases == null)
      return null;
    List<QName> newAliases = new ArrayList<QName>();
    for (Iterator<QName> i = aliases.iterator(); i.hasNext(); ) {
      QName alias = i.next();
      if (alias.getNamespaceURI().equals(whatToFind))
        newAliases.add(new QName(whatToReplaceTo, alias.getLocalPart()));
      else
        newAliases.add(alias);
    }
    return newAliases;
  }

  static public Portlet readPortlet(ExoXPPParser xpp ) throws Exception {
    Portlet p = new Portlet();
    p.setId(xpp.getNodeAttributeValue("id"));
    while(xpp.node("description")) p.addDescription(readDescription(xpp));
    xpp.mandatoryNode("portlet-name"); p.setPortletName(xpp.getContent());
    while(xpp.node("display-name")) p.addDisplayName(readDisplayName(xpp));
    xpp.mandatoryNode("portlet-class"); p.setPortletClass(xpp.getContent().trim());
    while(xpp.node("init-param")) p.addInitParam(readInitParam(xpp));
    if (newSpec) {
      p.setCaching(xpp.node("expiration-cache")?
        Integer.parseInt(xpp.getContent().trim()):
        null);
      p.setGlobalCache(xpp.node("cache-scope")?
        (xpp.getContent().trim().equalsIgnoreCase("public")? // in the 1-st spec was: global
          "true":
          "false"):
        "false");
    } else {
      if(xpp.node("expiration-cache")) p.setCaching(Integer.parseInt(xpp.getContent().trim()));
    }
    xpp.mandatoryNode("supports"); p.setSupports(readSupports(xpp));
    while(xpp.node("supports")) p.addSupports(readSupports(xpp));
    while(xpp.node("supported-locale")) p.addSupportedLocale(new Locale(xpp.getContent().trim()));
    boolean chosen = false;
    if(xpp.node("resource-bundle")) {
      p.setResourceBundle(xpp.getContent().trim());
      chosen = true;
    }
    if(xpp.node("portlet-info")) {
      p.setPortletInfo(readPortletInfo(xpp));
      chosen = true;
    }
    if ( !chosen ) {
      throw new Exception("expect tag name 'resource-bundle' or 'portlet-info'");
    }
    if(xpp.node("portlet-preferences")) p.setPortletPreferences(readPortletPreferences(xpp));
    while(xpp.node("security-role-ref")) p.addSecurityRoleRef(readSecurityRoleRef(xpp));
    // exo tag only for portlet 1.0
    if (!newSpec) {
      if(xpp.node("global-cache")) p.setGlobalCache(xpp.getContent().trim());
    }
    // portlet api 2.0
    if (newSpec) {
      while(xpp.node("supported-processing-event")) p.addSupportedProcessingEvent(readEventReference(xpp));
      while(xpp.node("supported-publishing-event")) p.addSupportedPublishingEvent(readEventReference(xpp));
      while(xpp.node("supported-public-render-parameter")) p.addSupportedPublicRenderParameter(xpp.getContent().trim());
      while(xpp.node("url-generation-listener")) p.addUrlGenerationListener(xpp.getContent().trim());
      while(xpp.node("container-runtime-option")) p.addContainerRuntimeOption(readContainerRuntimeOption(xpp));

    }
    return p;
  }

  static public CustomPortletMode readCustomPortletMode(ExoXPPParser xpp ) throws Exception {
    CustomPortletMode mode = new CustomPortletMode();
    mode.setId(xpp.getNodeAttributeValue("id"));
     while(xpp.node("description")) mode.addDescription(readDescription(xpp));
     xpp.mandatoryNode("portlet-mode"); mode.setPortletMode(xpp.getContent().trim());
     if (xpp.node("portal-managed")) {
       mode.setPortalManaged(xpp.getContent().trim());
     } else {
       mode.setPortalManaged(Boolean.TRUE.toString());
     }
     if (mode.getPortalManaged() == Boolean.FALSE.toString()) {
       xpp.mandatoryNode("decoration-name"); mode.setResourceID(xpp.getContent().trim());
     } else {
       if (xpp.node("decoration-name")) mode.setResourceID(xpp.getContent().trim());
     }
    return mode;
  }

  static public CustomWindowState readCustomWindowState(ExoXPPParser xpp ) throws Exception {
    CustomWindowState state = new CustomWindowState();
    state.setId(xpp.getNodeAttributeValue("id"));
    while(xpp.node("description")) state.addDescription(readDescription(xpp));
    xpp.mandatoryNode("window-state"); state.setWindowState(xpp.getContent().trim());
    if (!newSpec){
      List portlets = portletapp.getPortlet();
      for (int i = 0; i < portlets.toArray().length; i++) {
        Portlet portlet = (Portlet)portlets.toArray()[i];
        List supports = portlet.getSupports();
        for (int j = 0; j < supports.toArray().length; j++) {
          Supports support = (Supports)supports.toArray()[j];
          support.addWindowState(state.getWindowState());
        }
      }
    }
    return state;
  }

  static public UserAttribute readUserAttribute(ExoXPPParser xpp ) throws Exception {
    UserAttribute att = new UserAttribute();
    att.setId(xpp.getNodeAttributeValue("id"));
    while(xpp.node("description")) att.addDescription(readDescription(xpp));
    xpp.mandatoryNode("name"); att.setName(xpp.getContent().trim());
    return att;
  }

  static public SecurityConstraint readSecurityConstraint(ExoXPPParser xpp ) throws Exception {
    SecurityConstraint sc = new SecurityConstraint();
    sc.setId(xpp.getNodeAttributeValue("id"));
    if(xpp.node("displayName"))sc.addDisplayName(readDisplayName(xpp));
    // portlet api 2.0
    if(xpp.node("display-name"))sc.addDisplayName(readDisplayName(xpp));
    //
    xpp.mandatoryNode("portlet-collection"); sc.setPortletCollection(readPortletCollection(xpp));
    xpp.mandatoryNode("user-data-constraint"); sc.setUserDataConstraint(readUserDataConstraint(xpp));
    return sc;
  }

  static public Description readDescription(ExoXPPParser xpp) throws Exception {
    Description desc = new Description();
    desc.setLang(xpp.getNodeAttributeValue("xml:lang"));
    desc.setDescription(xpp.getContent().trim());
    return desc;
  }

  static public DisplayName readDisplayName(ExoXPPParser xpp) throws Exception {
    DisplayName name = new DisplayName();
    name.setLang(xpp.getNodeAttributeValue("xml:lang"));
    name.setDisplayName(xpp.getContent().trim());
    return name;
  }

  static public InitParam readInitParam(ExoXPPParser xpp) throws Exception {
    InitParam param = new InitParam();
    while(xpp.node("description")) param.addDescription(readDescription(xpp));
    xpp.mandatoryNode("name"); param.setName(xpp.getContent().trim());
    xpp.mandatoryNode("value"); param.setValue(xpp.getContent());
    return param;
  }

  static public Supports readSupports(ExoXPPParser xpp) throws Exception {
    Supports supports = new Supports();
    supports.setId(xpp.getNodeAttributeValue("id"));
    xpp.mandatoryNode("mime-type"); supports.setMimeType(xpp.getContent().trim());
    // getting portlet modes
    List<String> portletMode = new ArrayList<String>();
    while(xpp.node("portlet-mode")) portletMode.add(xpp.getContent().trim());
    if (!portletMode.contains(PortletMode.VIEW.toString()))
      supports.addPortletMode(PortletMode.VIEW.toString());
    Iterator iterModes = portletMode.iterator();
    while (iterModes.hasNext()) {
      supports.addPortletMode(((String) iterModes.next()) );
    }
    if (newSpec) {
      // getting window states
      List<String> windowState = new ArrayList<String>();
      while(xpp.node("window-state")) windowState.add(xpp.getContent().trim());
      if (!windowState.contains(WindowState.MINIMIZED.toString()))
        supports.addWindowState(WindowState.MINIMIZED.toString());
      if (!windowState.contains(WindowState.NORMAL.toString()))
        supports.addWindowState(WindowState.NORMAL.toString());
      if (!windowState.contains(WindowState.MAXIMIZED.toString()))
        supports.addWindowState(WindowState.MAXIMIZED.toString());
      Iterator iterStates = windowState.iterator();
      while (iterStates.hasNext()) {
        supports.addWindowState(((String) iterStates.next()) );
      }
    } else {
      supports.addWindowState(WindowState.MINIMIZED.toString());
      supports.addWindowState(WindowState.NORMAL.toString());
      supports.addWindowState(WindowState.MAXIMIZED.toString());
    }
    return supports;
  }

  static public PortletInfo readPortletInfo(ExoXPPParser xpp) throws Exception {
    PortletInfo info = new PortletInfo();
    if(xpp.node("title")) info.setTitle(xpp.getContent());
    if(xpp.node("short-title")) info.setShortTitle(xpp.getContent());
    if(xpp.node("keywords")) info.setKeywords(xpp.getContent());
    return info;
  }

  static public ExoPortletPreferences readPortletPreferences(ExoXPPParser xpp) throws Exception {
    ExoPortletPreferences prefs = new ExoPortletPreferences();
    prefs.setId(xpp.getNodeAttributeValue("id"));
    while(xpp.node("preference"))prefs.addPreference(readPreference(xpp));
    if(xpp.node("preferences-validator")) prefs.setPreferencesValidator(xpp.getContent().trim());
    return prefs;
  }

  static public Preference readPreference(ExoXPPParser xpp) throws Exception {
    Preference pref = new Preference();
    pref.setId(xpp.getNodeAttributeValue("id"));
    xpp.mandatoryNode("name"); pref.setName(xpp.getContent().trim());
    while(xpp.node("value"))pref.addValue(xpp.getContent());
    if(xpp.node("read-only")) pref.setReadOnly(xpp.getContent().trim());
    return pref;
  }

  static public SecurityRoleRef readSecurityRoleRef(ExoXPPParser xpp) throws Exception {
    SecurityRoleRef ref = new SecurityRoleRef();
    // portlet api 2.0
    ref.setId(xpp.getNodeAttributeValue("id"));
    while(xpp.node("description")) ref.addDescription(readDescription(xpp));
    xpp.mandatoryNode("role-name"); ref.setRoleName(xpp.getContent().trim());
    if(xpp.node("role-link")) ref.setRoleLink(xpp.getContent().trim());
    return ref;
  }

  static public Filter readFilter(ExoXPPParser xpp) throws Exception {
    Filter filter = new Filter();
    xpp.mandatoryNode("filter-name"); filter.setFilterName(xpp.getContent().trim());
    xpp.mandatoryNode("filter-class"); filter.setFilterClass(xpp.getContent().trim());
    xpp.mandatoryNode("lifecycle"); filter.addLifecycle(parseLifecycle(xpp.getContent().trim(), filter.getFilterName()));
    while (xpp.node("lifecycle")) filter.addLifecycle(parseLifecycle(xpp.getContent().trim(), filter.getFilterName()));
    if (filter.getLifecycle() == Constants.EMPTY_LIST)
      filter.addLifecycle(Filter.ALL);
    while (xpp.node("init-param")) filter.addInitParam(readInitParam(xpp));
    return filter;
  }

  static public Integer parseLifecycle(String cycle, String filterName) {
    if (cycle.equalsIgnoreCase("ACTION_PHASE"))
      return(Filter.ACTION);
    else
      if (cycle.equalsIgnoreCase("EVENT_PHASE"))
        return(Filter.EVENT);
    else
    if (cycle.equalsIgnoreCase("RENDER_PHASE"))
      return(Filter.RENDER);
    else
    if (cycle.equalsIgnoreCase("RESOURCE_PHASE"))
      return(Filter.RESOURCE);
    else {
      Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
      log.error("WHILE PARSING portlet.xml the filter '" + filterName + "' doesn't have proper lifecycle.");
    }
    return null;
  }

  static public FilterMapping readFilterMapping(ExoXPPParser xpp) throws Exception {
    FilterMapping filterMapping = new FilterMapping();
    xpp.mandatoryNode("filter-name"); filterMapping.setFilterName(xpp.getContent().trim());
    xpp.mandatoryNode("portlet-name"); filterMapping.addPortletName(xpp.getContent().trim());
    while(xpp.node("portlet-name")) filterMapping.addPortletName(xpp.getContent().trim());
    return filterMapping;
  }

  static public PortletCollection readPortletCollection(ExoXPPParser xpp) throws Exception {
    PortletCollection collection = new PortletCollection();
    xpp.mandatoryNode("portlet-name"); collection.addPortletName(xpp.getContent().trim());
    while(xpp.node("portlet-name")) collection.addPortletName(xpp.getContent().trim());
    return collection;
  }

  static public UserDataConstraint readUserDataConstraint(ExoXPPParser xpp) throws Exception {
    UserDataConstraint u = new UserDataConstraint();
    u.setId(xpp.getNodeAttributeValue("id"));
    while(xpp.node("description")) u.addDescription(readDescription(xpp));
    xpp.mandatoryNode("transport-guarantee"); u.setTransportGuarantie(xpp.getContent().trim());
    return u;
  }

  // portlet api 2.0

  private static QName parseQName(ExoXPPParser xpp) throws Exception {
    String cnt = xpp.getContent().trim();
    try {
      int cm = cnt.indexOf(':');
      String ns = cnt.substring(0, cm);
      cnt = cnt.substring(cm + 1);
      ns = xpp.getNodeAttributeValue("xmlns:" + ns);
      return new QName(ns, cnt);
    } catch (NullPointerException e) {
      return new QName(cnt);
    }
  }

  static public EventDefinition readEventDefinition(ExoXPPParser xpp) throws Exception {
    EventDefinition ed = new EventDefinition();
    ed.setId(xpp.getNodeAttributeValue("id"));
    while (xpp.node("description")) ed.addDescription(readDescription(xpp));
    if (xpp.node("qname"))
      ed.setPrefferedName(parseQName(xpp));
    else {
      xpp.mandatoryNode("name");
      ed.setPrefferedName(new QName(xpp.getContent().trim()));
    }
    while (xpp.node("alias")) ed.addAlias(new QName(xpp.getContent().trim()));
    if (xpp.node("value-type")) ed.setJavaClass(xpp.getContent().trim());
    return ed;
  }

  static public QName readEventReference(ExoXPPParser xpp) throws Exception {
    QName name;
    if (xpp.node("qname"))
      name = parseQName(xpp);
    else {
      xpp.mandatoryNode("name");
      name = new QName(xpp.getContent().trim());
    }
    return name;
  }

  static public void fillPredefinedEvents(PortletApp app) throws Exception {
    EventDefinition ed = new EventDefinition();
    ed.setPrefferedName(new QName("urn:oasis:names:tc:wsrp:v2:types", "wsrp:eventHandlingFailed"));
    app.addEventDefinition(ed);
    ed = new EventDefinition();
    ed.setPrefferedName(new QName("urn:oasis:names:tc:wsrp:v2:types", "wsrp:newNavigationalContextScope"));
    app.addEventDefinition(ed);
  }

  static public PublicRenderParameter readPublicRenderParameter(ExoXPPParser xpp) throws Exception {
    PublicRenderParameter srp = new PublicRenderParameter();
    srp.setId(xpp.getNodeAttributeValue("id"));
    while (xpp.node("description")) srp.addDescription(readDescription(xpp));
    xpp.mandatoryNode("identifier"); srp.setPrefferedName(xpp.getContent().trim());
    if (xpp.node("qname"))
      srp.addName(parseQName(xpp));
    else {
      xpp.mandatoryNode("name"); srp.addName(new QName(xpp.getContent().trim()));
    }
    while (xpp.node("name")) srp.addName(new QName(xpp.getContent().trim()));
    return srp;
  }

  static public Map readContainerRuntimeOption(ExoXPPParser xpp) throws Exception {
    HashMap<String,String[]> options = new HashMap<String,String[]>();
    xpp.mandatoryNode("name"); String name = xpp.getContent().trim();
    ArrayList<String> values = new ArrayList<String>();
    if (xpp.node("value"))
      values.add(xpp.getContent().trim());
    else {
      options.put(name,null);
      return options;
    }
    while (xpp.node("value"))
      values.add(xpp.getContent().trim());
    options.put(name, (String[])values.toArray());
    return options;
  }

}
