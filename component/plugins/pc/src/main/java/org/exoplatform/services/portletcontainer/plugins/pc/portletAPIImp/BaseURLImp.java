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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.BaseURL;
import javax.portlet.PortletSecurityException;

import org.exoplatform.Constants;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public abstract class BaseURLImp implements BaseURL {

  protected String                    baseURL;

  protected Map<String, String[]>     parameters       = new HashMap<String, String[]>();

  protected Map<String, List<String>> properties       = new HashMap<String, List<String>>();

  protected boolean                   isSecure;

  protected boolean                   setSecureCalled;

  protected String                    type;

  protected boolean                   isCurrentlySecured;

  protected boolean                   defaultEscapeXml = true;

  protected final Portlet             portletDatas;

  public BaseURLImp(String type,
                    String baseURL,
                    boolean isCurrentlySecured,
                    boolean defaultEscapeXml,
                    Portlet portletDatas) {
    this.type = type;
    this.baseURL = baseURL;
    this.isCurrentlySecured = isCurrentlySecured;
    this.defaultEscapeXml = defaultEscapeXml;
    this.portletDatas = portletDatas;
  }

  public void setParameter(String name,
                           String value) {
    if (name == null)
      throw new IllegalArgumentException("the key given is null");
    if (value == null)
      throw new IllegalArgumentException("the value given is null");
    parameters.put(name, new String[] { value });
  }

  public void setParameter(String name,
                           String[] values) {
    if (name == null)
      throw new IllegalArgumentException("the key given is null");
    if (values == null)
      throw new IllegalArgumentException("the value given is null");
    parameters.put(name, values);
  }

  public void setParameters(Map<String, String[]> map) {
    if (map == null)
      throw new IllegalArgumentException("the map given is null");
    if (map.containsKey(null))
      throw new IllegalArgumentException("the map given contains a null key");
    Set<String> keys = map.keySet();
    for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
      if (!(iter.next() instanceof String))
        throw new IllegalArgumentException("the map contains a non String key");
    }
    Collection<String[]> values = map.values();
    for (Iterator<String[]> iter = values.iterator(); iter.hasNext();) {
      if (!(iter.next() instanceof String[]))
        throw new IllegalArgumentException("the map contains a non String[] value");
    }
    parameters = map;
  }

  public Map<String, String[]> getParameterMap() {
    return parameters;
  }

  // Spec draft nr.20 new methods

  public void addProperty(String key,
                          String value) {
    if (key == null)
      throw new IllegalArgumentException("the property key given is null");

    List<String> propvalues = (properties.get(key) != null ? properties.get(key) : new ArrayList<String>());
    propvalues.add(value);
    properties.put(key, propvalues);

  }

  public void setProperty(String key,
                          String value) {
    if (key == null)
      throw new IllegalArgumentException("the property key given is null");

    ArrayList<String> propvalues = new ArrayList<String>();
    propvalues.add(value);
    properties.put(key, propvalues);

  }

  public String getPropertyString() {
    return getPropertyString(defaultEscapeXml);
  }

  public String getPropertyString(boolean escapeXML) {
    StringBuffer sb = new StringBuffer();
    try {
      Set<String> names = properties.keySet();
      for (Iterator<String> iterator = names.iterator(); iterator.hasNext();) {
        String name = iterator.next();
        List<String> propvalues = properties.get(name);
        for (int i = 0; i <= propvalues.size(); i++) {
          sb.append(Constants.AMPERSAND);
          sb.append(Constants.PROPERTY_ENCODER);
          sb.append(encode(name, escapeXML));
          sb.append("=");
          sb.append(encode(propvalues.get(i), escapeXML));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return sb.toString();
  }

  public void setSecure(boolean isSecure) throws PortletSecurityException {
    this.isSecure = isSecure;
    this.setSecureCalled = true;
  }

  public abstract String toString(boolean escapeXML);

  public String toString() {
    return toString(defaultEscapeXml);
  }

  protected String encode(String s,
                          boolean escapeXML) {
    if (s == null || s == "")
      return "";
    if (escapeXML)
      s = encodeChars(s);
    try {
      return URLEncoder.encode(s, "utf-8");
    } catch (java.io.UnsupportedEncodingException e) {
      return s;
    }
  }

  protected String encodeChars(String s) {
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&#034;").replace("'", "&#039;");
  }

  public void write(Writer out) throws IOException {
    out.write(toString(defaultEscapeXml));
  }

  public void write(Writer out,
                    boolean escapeXML) throws IOException {
    out.write(toString(escapeXML));
  }

}
