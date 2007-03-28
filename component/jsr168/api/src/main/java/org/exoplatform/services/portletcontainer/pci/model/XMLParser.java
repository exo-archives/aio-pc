/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.pci.model;

import java.io.InputStream;
import java.util.Iterator;
import javax.portlet.PortletMode;
import org.exoplatform.commons.xml.ExoXPPParser;

/**
 * Jul 8, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: XMLParser.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class XMLParser {

  static public PortletApp readPortletApp(ExoXPPParser xpp ) throws Exception {
    PortletApp pa = new PortletApp()  ;
    pa.setVersion(xpp.getNodeAttributeValue("version")) ;
    while(xpp.node("portlet")) pa.addPortlet(readPortlet(xpp)) ;
    while(xpp.node("custom-portlet-mode")) pa.addCustomPortletMode(readCustomPortletMode(xpp)) ;
    while(xpp.node("custom-window-state")) pa.addCustomWindowState(readCustomWindowState(xpp)) ;
    while(xpp.node("user-attribute")) pa.addUserAttribute(readUserAttribute(xpp)) ;
    while(xpp.node("security-constraint")) pa.addSecurityConstraint(readSecurityConstraint(xpp)) ;
    return pa ;
  }

  static public Portlet readPortlet(ExoXPPParser xpp ) throws Exception {
    Portlet p = new Portlet()  ;
    while(xpp.node("description")) p.addDescription(readDescription(xpp)) ;
    xpp.mandatoryNode("portlet-name"); p.setPortletName(xpp.getContent()) ;
    while(xpp.node("display-name")) p.addDisplayName(readDisplayName(xpp)) ;
    xpp.mandatoryNode("portlet-class"); p.setPortletClass(xpp.getContent()) ;
    while(xpp.node("init-param")) p.addInitParam(readInitParam(xpp)) ;
    if(xpp.node("expiration-cache")) p.setExpirationCache(xpp.getContent()) ;
    while(xpp.node("supports")) p.addSupports(readSupports(xpp)) ;
    while(xpp.node("supported-locale")) p.addSupportedLocale(xpp.getContent()) ;
    if(xpp.node("resource-bundle")) p.setResourceBundle(xpp.getContent()) ;
    if(xpp.node("portlet-info")) p.setPortletInfo(readPortletInfo(xpp)) ;
    if(xpp.node("portlet-preferences")) p.setPortletPreferences(readPortletPreferences(xpp)) ;
    while(xpp.node("security-role-ref")) p.addSecurityRoleRef(readSecurityRoleRef(xpp)) ;
    while(xpp.node("filter")) p.addFilter(readFilter(xpp)) ;
    if(xpp.node("global-cache")) p.setGlobalCache(xpp.getContent()) ;
    return p ;
  }

  static public CustomPortletMode readCustomPortletMode(ExoXPPParser xpp ) throws Exception {
    CustomPortletMode mode = new CustomPortletMode()  ;
     while(xpp.node("description")) mode.addDescription(readDescription(xpp)) ;
     xpp.mandatoryNode("portlet-mode"); mode.setPortletMode(xpp.getContent()) ;
    return mode ;
  }

  static public CustomWindowState readCustomWindowState(ExoXPPParser xpp ) throws Exception {
    CustomWindowState state = new CustomWindowState()  ;
    while(xpp.node("description")) state.addDescription(readDescription(xpp)) ;
    xpp.mandatoryNode("window-state"); state.setWindowState(xpp.getContent()) ;
    return state ;
  }

  static public UserAttribute readUserAttribute(ExoXPPParser xpp ) throws Exception {
    UserAttribute att = new UserAttribute()  ;
    while(xpp.node("description")) att.addDescription(readDescription(xpp)) ;
    xpp.mandatoryNode("name"); att.setName(xpp.getContent()) ;
    return att ;
  }

  static public SecurityConstraint readSecurityConstraint(ExoXPPParser xpp ) throws Exception {
    SecurityConstraint sc = new SecurityConstraint()  ;
    if(xpp.node("displayName"))sc.setDisplayName(xpp.getContent()) ;
    xpp.mandatoryNode("portlet-collection"); sc.setPortletCollection(readPortletCollection(xpp)) ;
    xpp.mandatoryNode("user-data-constraint"); sc.setUserDataConstraint(readUserDataConstraint(xpp)) ;
    return sc ;
  }

  static public Description readDescription(ExoXPPParser xpp) throws Exception {
    Description desc = new Description() ;
    desc.setLang(xpp.getNodeAttributeValue("xml:lang")) ;
    desc.setDescription(xpp.getContent()) ;
    return desc ;
  }

  static public DisplayName readDisplayName(ExoXPPParser xpp) throws Exception {
    DisplayName name = new DisplayName() ;
    name.setLang(xpp.getNodeAttributeValue("xml:lang")) ;
    name.setDisplayName(xpp.getContent()) ;
    return name ;
  }

  static public InitParam readInitParam(ExoXPPParser xpp) throws Exception {
    InitParam param = new InitParam() ;
    while(xpp.node("description")) param.addDescription(readDescription(xpp)) ;
    xpp.mandatoryNode("name"); param.setName(xpp.getContent()) ;
    xpp.mandatoryNode("value"); param.setValue(xpp.getContent()) ;
    return param ;
  }

  static public Supports readSupports(ExoXPPParser xpp) throws Exception {
    Supports supports = new Supports() ;
    xpp.mandatoryNode("mime-type"); supports.setMimeType(xpp.getContent()) ;
    while(xpp.node("portlet-mode")) supports.addPortletMode(xpp.getContent()) ;
    Iterator i = supports.getPortletMode().iterator();
    boolean viewListed = false;
    while(i.hasNext())
      if (((String) i.next()).equalsIgnoreCase(PortletMode.VIEW.toString())) {
        viewListed = true;
        break;
      }
    if (!viewListed)
      supports.addPortletMode(PortletMode.VIEW.toString());
    return supports ;
  }

  static public PortletInfo readPortletInfo(ExoXPPParser xpp) throws Exception {
    PortletInfo info = new PortletInfo() ;
    xpp.mandatoryNode("title"); info.setTitle(xpp.getContent()) ;
    if(xpp.node("short-title")) info.setShortTitle(xpp.getContent()) ;
    if(xpp.node("keywords")) info.setKeywords(xpp.getContent()) ;
    return info ;
  }

  static public ExoPortletPreferences readPortletPreferences(ExoXPPParser xpp) throws Exception {
    ExoPortletPreferences prefs = new ExoPortletPreferences() ;
    while(xpp.node("preference"))prefs.addPreference(readPreference(xpp)) ;
    if(xpp.node("preferences-validator")) prefs.setPreferencesValidator(xpp.getContent()) ;
    return prefs ;
  }

  static public Preference readPreference(ExoXPPParser xpp) throws Exception {
    Preference pref = new Preference() ;
    xpp.mandatoryNode("name"); pref.setName(xpp.getContent()) ;
    while(xpp.node("value"))pref.addValue(xpp.getContent()) ;
    if(xpp.node("read-only")) pref.setReadOnly(xpp.getContent()) ;
    return pref ;
  }

  static public SecurityRoleRef readSecurityRoleRef(ExoXPPParser xpp) throws Exception {
    SecurityRoleRef ref = new SecurityRoleRef() ;
    xpp.mandatoryNode("role-name"); ref.setRoleName(xpp.getContent()) ;
    if(xpp.node("role-link")) ref.setRoleLink(xpp.getContent()) ;
    return ref ;
  }

  static public Filter readFilter(ExoXPPParser xpp) throws Exception {
    Filter filter = new Filter() ;
    xpp.mandatoryNode("filter-name"); filter.setFilterName(xpp.getContent()) ;
    xpp.mandatoryNode("filter-class"); filter.setFilterClass(xpp.getContent()) ;
    while(xpp.node("init-param")) filter.addInitParam(readInitParam(xpp)) ;
    return filter ;
  }

  static public PortletCollection readPortletCollection(ExoXPPParser xpp) throws Exception {
    PortletCollection collection = new PortletCollection() ;
    while(xpp.node("portlet-name")) collection.addPortletName(xpp.getContent()) ;
    return collection ;
  }

  static public UserDataConstraint readUserDataConstraint(ExoXPPParser xpp) throws Exception {
    UserDataConstraint u = new UserDataConstraint() ;
    while(xpp.node("description")) u.addDescription(readDescription(xpp)) ;
    xpp.mandatoryNode("transport-guarantee"); u.setTransportGuarantie(xpp.getContent()) ;
    return u ;
  }

  static public PortletApp parse(InputStream is) throws Exception {
    ExoXPPParser xpp = ExoXPPParser.getInstance()  ;
    xpp.setInput(is, "UTF8");
    xpp.mandatoryNode("portlet-app") ;
    return readPortletApp(xpp) ;
  }
}