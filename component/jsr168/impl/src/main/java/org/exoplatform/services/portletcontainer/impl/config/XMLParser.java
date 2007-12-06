/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.config;

import java.io.InputStream;
import org.exoplatform.commons.xml.ExoXPPParser;


/**
 * Jul 8, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: XMLParser.java,v 1.3 2004/10/26 18:47:54 benjmestrallet Exp $
 */
public class XMLParser {
    
	static public PortletContainer readPortletContainer(ExoXPPParser xpp ) throws Exception {
		PortletContainer pc = new PortletContainer() ;
		xpp.mandatoryNode("global"); pc.setGlobal(readGlobal(xpp)) ;
		if(xpp.node("shared-session")) pc.setSharedSession(readSharedSession(xpp)) ;
    
    if(xpp.node("delegated-bundle")) pc.setDelegatedBundle(readDelegatedBundle(xpp)) ;
		if(xpp.node("cache")) pc.setCache(readCache(xpp)) ;
		while(xpp.node("supported-content")) pc.addSupportedContent(readSupportedContent(xpp)) ;
		while(xpp.node("custom-mode")) pc.addCustomMode(readCustomMode(xpp)) ;
		while(xpp.node("custom-window-state")) pc.addCustomWindowState(readCustomWindowState(xpp)) ;
		while(xpp.node("properties")) pc.addProperties(readProperties(xpp)) ;
		return pc ;
	}



  static public Global readGlobal(ExoXPPParser xpp) throws Exception {
		Global global = new Global() ;
		xpp.mandatoryNode("name"); global.setName(xpp.getContent()) ;
		if(xpp.node("description")) global.setDescription(xpp.getContent()) ;
		xpp.mandatoryNode("major-version"); global.setMajorVersion(Integer.parseInt(xpp.getContent())) ;
		xpp.mandatoryNode("minor-version"); global.setMinorVersion(Integer.parseInt(xpp.getContent())) ;
		return global ;
	}
	
	static public SharedSession readSharedSession(ExoXPPParser xpp) throws Exception {
		SharedSession ss = new SharedSession() ;
		xpp.mandatoryNode("enable"); ss.setEnable(xpp.getContent()) ;
		return ss ;
	}
  
  public static DelegatedBundle readDelegatedBundle(ExoXPPParser xpp) throws Exception {    
    DelegatedBundle dB = new DelegatedBundle();
    xpp.mandatoryNode("enable"); dB.setEnable(xpp.getContent());
    return dB;
  }  
	
	static public Cache readCache(ExoXPPParser xpp) throws Exception {
		Cache cache = new Cache() ;
		xpp.mandatoryNode("enable"); cache.setEnable(xpp.getContent()) ;
		return cache  ;
	}
	
	static public Description readDescription(ExoXPPParser xpp) throws Exception {
		Description desc = new Description() ;
		desc.setLang(xpp.getNodeAttributeValue("lang")) ;
		desc.setDescription(xpp.getContent()) ;
		return desc ;
	}
	
	static public SupportedContent readSupportedContent(ExoXPPParser xpp) throws Exception {
		SupportedContent supportedContent = new SupportedContent() ;
		xpp.mandatoryNode("name"); supportedContent.setName(xpp.getContent()) ;
		return supportedContent ;
	}
	
	static public CustomMode readCustomMode(ExoXPPParser xpp) throws Exception {
		CustomMode cmode = new CustomMode() ;
		xpp.mandatoryNode("name"); cmode.setName(xpp.getContent()) ;
		while(xpp.node("description")) {
			cmode.addDescription(readDescription(xpp));
		}
		return cmode ;
	}
	
	static public Properties readProperties(ExoXPPParser xpp) throws Exception {
		Properties props = new Properties() ;
		xpp.mandatoryNode("description") ; props.setDescription(xpp.getContent()) ;
		xpp.mandatoryNode("name"); props.setName(xpp.getContent()) ;
		xpp.mandatoryNode("value"); props.setValue(xpp.getContent()) ;
		return props ;
	}
	
	static public CustomWindowState readCustomWindowState(ExoXPPParser xpp) throws Exception {
		CustomWindowState state = new CustomWindowState() ;
		xpp.mandatoryNode("name"); state.setName(xpp.getContent()) ;
		while(xpp.node("description")) {
			state.addDescription(readDescription(xpp));
		}
		return state ;
	}
   
	static public PortletContainer parse(InputStream is) throws Exception {
		ExoXPPParser xpp = ExoXPPParser.getInstance()  ;
		xpp.setInput (is, "UTF8");
		xpp.mandatoryNode("portlet-container") ;
		return readPortletContainer(xpp) ;
	}
}