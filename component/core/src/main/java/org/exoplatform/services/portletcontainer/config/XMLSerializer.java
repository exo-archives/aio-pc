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
package org.exoplatform.services.portletcontainer.config;

import java.io.StringWriter;
import java.util.List;
import org.exoplatform.commons.xml.ExoXMLSerializer;
import org.exoplatform.services.portletcontainer.PortletContainer;

/**
 * Jul 8, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: XMLSerializer.java,v 1.1 2004/07/08 19:11:45 tuan08 Exp $
 */
public class XMLSerializer {
  static private String NS = "" ;

	static public void toXML(ExoXMLSerializer ser, PortletContainer pc) throws Exception {
    List list ;
		ser.startTag(NS, "global"); toXML(ser, pc.getGlobal()); ser.endTag(NS, "global");
    ser.startTag(NS, "cache"); toXML(ser, pc.getCache()); ser.endTag(NS, "cache");
    list = pc.getSupportedContent();
    for(int i = 0; i < list.size(); i++) {
      ser.startTag(NS, "supported-content");
      ser.element(NS, "name", ((SupportedContent)list.get(i)).getName());
      ser.endTag(NS, "supported-content");
    }
    list = pc.getCustomMode();
    for(int i = 0; i < list.size(); i++) {
      ser.startTag(NS, "custom-mode");
      toXML(ser, (CustomMode)list.get(i));
      ser.endTag(NS, "custom-mode");
    }
    list = pc.getCustomWindowState();
    for(int i = 0; i < list.size(); i++) {
      ser.startTag(NS, "custom-window-state");
      toXML(ser, (CustomWindowState)list.get(i));
      ser.endTag(NS, "custom-window-state");
    }
    list = pc.getProperties();
    for(int i = 0; i < list.size(); i++) {
      ser.startTag(NS, "properties");
      toXML(ser, (Properties)list.get(i));
      ser.endTag(NS, "properties");
    }
  }

  static public void toXML(ExoXMLSerializer ser, Global global) throws Exception {
    ser.element(NS, "name", global.getName()) ;
    ser.element(NS, "description", global.getDescription()) ;
    ser.element(NS, "major-version", Integer.toString(global.getMajorVersion())) ;
    ser.element(NS, "minor-version", Integer.toString(global.getMinorVersion())) ;
  }

  static public void toXML(ExoXMLSerializer ser, Cache cache) throws Exception {
    ser.element(NS, "enable", cache.getEnable()) ;
  }

  static public void toXML(ExoXMLSerializer ser, CustomMode mode) throws Exception {
    ser.element(NS, "name", mode.getName()) ;
    List descs = mode.getDescription();
    for(int i = 0; i < descs.size(); i++) {
      toXML(ser,(Description)descs.get(i));
    }
  }

  static public void toXML(ExoXMLSerializer ser, CustomWindowState state) throws Exception {
    ser.element(NS, "name", state.getName()) ;
    List descs = state.getDescription();
    for(int i = 0; i < descs.size(); i++) {
      toXML(ser,(Description)descs.get(i));
    }
  }

  static public void toXML(ExoXMLSerializer ser, Properties props) throws Exception {
    ser.element(NS, "description", props.getName()) ;
    ser.element(NS, "name", props.getName()) ;
    ser.element(NS, "value", props.getValue()) ;
  }

  static public void toXML(ExoXMLSerializer ser, Description desc) throws Exception {
    ser.startTag(NS, "description"); ser.attribute(NS, "lang", desc.getLang()) ;
    ser.text(desc.getDescription()) ;
    ser.endTag(NS, "description") ;
  }

  static public String toXML(PortletContainer pc) throws Exception {
  	ExoXMLSerializer ser = ExoXMLSerializer.getInstance() ;
    StringWriter sw = new StringWriter() ;
    ser.setOutput(sw);
    ser.startDocument("UTF-8", null); ser.text("\n") ;
    ser.startTag(NS, "portlet-container") ;
    toXML(ser, pc) ;
    ser.endTag(NS, "portlet-container") ;
    ser.endDocument();
    return sw.getBuffer().toString() ;
  }
}