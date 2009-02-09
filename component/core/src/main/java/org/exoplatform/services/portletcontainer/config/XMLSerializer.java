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
 * Jul 8, 2004 .
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: XMLSerializer.java,v 1.1 2004/07/08 19:11:45 tuan08 Exp $
 */
public class XMLSerializer {

  /**
   * Namespace.
   */
  private static String pcNS = "";

  /**
   * Simple constructor.
   */
  protected XMLSerializer() {
  }

  /**
   * @param ser serializer
   * @param pc PC configuration
   * @throws Exception exception
   */
  public static void toXML(final ExoXMLSerializer ser, final PortletContainer pc) throws Exception {
    List list;
    ser.startTag(pcNS, "global");
    toXML(ser, pc.getGlobal());
    ser.endTag(pcNS, "global");
    ser.startTag(pcNS, "cache");
    toXML(ser, pc.getCache());
    ser.endTag(pcNS, "cache");
    list = pc.getSupportedContent();
    for (int i = 0; i < list.size(); i++) {
      ser.startTag(pcNS, "supported-content");
      ser.element(pcNS, "name", ((SupportedContent) list.get(i)).getName());
      ser.endTag(pcNS, "supported-content");
    }
    list = pc.getCustomMode();
    for (int i = 0; i < list.size(); i++) {
      ser.startTag(pcNS, "custom-mode");
      toXML(ser, (CustomMode) list.get(i));
      ser.endTag(pcNS, "custom-mode");
    }
    list = pc.getCustomWindowState();
    for (int i = 0; i < list.size(); i++) {
      ser.startTag(pcNS, "custom-window-state");
      toXML(ser, (CustomWindowState) list.get(i));
      ser.endTag(pcNS, "custom-window-state");
    }
    list = pc.getProperties();
    for (int i = 0; i < list.size(); i++) {
      ser.startTag(pcNS, "properties");
      toXML(ser, (Properties) list.get(i));
      ser.endTag(pcNS, "properties");
    }
  }

  /**
   * @param ser serializer
   * @param global global data
   * @throws Exception exception
   */
  public static void toXML(final ExoXMLSerializer ser, final Global global) throws Exception {
    ser.element(pcNS, "name", global.getName());
    ser.element(pcNS, "description", global.getDescription());
    ser.element(pcNS, "major-version", Integer.toString(global.getMajorVersion()));
    ser.element(pcNS, "minor-version", Integer.toString(global.getMinorVersion()));
  }

  /**
   * @param ser serializer
   * @param cache cache
   * @throws Exception exception
   */
  public static void toXML(final ExoXMLSerializer ser, final Cache cache) throws Exception {
    ser.element(pcNS, "enable", cache.getEnable());
  }

  /**
   * @param ser serializer
   * @param mode portlet mode
   * @throws Exception exception
   */
  public static void toXML(final ExoXMLSerializer ser, final CustomMode mode) throws Exception {
    ser.element(pcNS, "name", mode.getName());
    List<Description> descs = mode.getDescription();
    for (int i = 0; i < descs.size(); i++)
      toXML(ser, descs.get(i));
  }

  /**
   * @param ser serializer
   * @param state window state
   * @throws Exception exception
   */
  public static void toXML(final ExoXMLSerializer ser, final CustomWindowState state) throws Exception {
    ser.element(pcNS, "name", state.getName());
    List<Description> descs = state.getDescription();
    for (int i = 0; i < descs.size(); i++)
      toXML(ser, descs.get(i));
  }

  /**
   * @param ser serializer
   * @param props properties
   * @throws Exception exception
   */
  public static void toXML(final ExoXMLSerializer ser, final Properties props) throws Exception {
    ser.element(pcNS, "description", props.getName());
    ser.element(pcNS, "name", props.getName());
    ser.element(pcNS, "value", props.getValue());
  }

  /**
   * @param ser serializer
   * @param desc description
   * @throws Exception exception
   */
  public static void toXML(final ExoXMLSerializer ser, final Description desc) throws Exception {
    ser.startTag(pcNS, "description");
    ser.attribute(pcNS, "lang", desc.getLang());
    ser.text(desc.getDescription());
    ser.endTag(pcNS, "description");
  }

  /**
   * @param pc PC configuration
   * @return XML document
   * @throws Exception exception
   */
  public static String toXML(final PortletContainer pc) throws Exception {
    ExoXMLSerializer ser = ExoXMLSerializer.getInstance();
    StringWriter sw = new StringWriter();
    ser.setOutput(sw);
    ser.startDocument("UTF-8", null);
    ser.text("\n");
    ser.startTag(pcNS, "portlet-container");
    toXML(ser, pc);
    ser.endTag(pcNS, "portlet-container");
    ser.endDocument();
    return sw.getBuffer().toString();
  }
}
