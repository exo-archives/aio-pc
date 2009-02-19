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
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.BaseURL;

import org.exoplatform.Constants;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public abstract class BaseURLImp implements BaseURL {

  /**
   * Base url string.
   */
  protected String baseURL;

  /**
   * Parameters.
   */
  protected Map<String, String[]> parameters = new HashMap<String, String[]>();

  /**
   * Properties.
   */
  private Map<String, List<String>> properties = new HashMap<String, List<String>>();

  /**
   * Is actually secure.
   */
  private boolean isSecure;

  /**
   * Was setSecure() method called.
   */
  private boolean setSecureCalled;

  /**
   * Type (render, action...).
   */
  private String type;

  /**
   * Is secured.
   */
  private boolean isCurrentlySecured;

  /**
   * Either to escape xml chars.
   */
  private boolean defaultEscapeXml = true;

  /**
   * Portlet datas.
   */
  private final Portlet portletDatas;

  /**
   * @param type type (render, action ...)
   * @param baseURL base url string
   * @param isCurrentlySecured is secured
   * @param defaultEscapeXml either to escape xml chars (default value)
   * @param portletDatas portlet datas
   */
  public BaseURLImp(final String type,
      final String baseURL,
      final boolean isCurrentlySecured,
      final boolean defaultEscapeXml,
      final Portlet portletDatas) {
    this.setType(type);
    this.baseURL = baseURL;
    this.setCurrentlySecured(isCurrentlySecured);
    this.defaultEscapeXml = defaultEscapeXml;
    this.portletDatas = portletDatas;
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @param value value
   * @see javax.portlet.BaseURL#setParameter(java.lang.String, java.lang.String)
   */
  public final void setParameter(final String name, final String value) {
    if (name == null)
      throw new IllegalArgumentException("the key given is null");
    if (value == null)
      throw new IllegalArgumentException("the value given is null");
    parameters.put(name, new String[] {value});
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @param values values
   * @see javax.portlet.BaseURL#setParameter(java.lang.String, java.lang.String[])
   */
  public final void setParameter(final String name, final String[] values) {
    if (name == null)
      throw new IllegalArgumentException("the key given is null");
    if (values == null)
      throw new IllegalArgumentException("the value given is null");
    parameters.put(name, values);
  }

  /**
   * Overridden method.
   *
   * @param map parameter map
   * @see javax.portlet.BaseURL#setParameters(java.util.Map)
   */
  public final void setParameters(final Map<String, String[]> map) {
    if (map == null)
      throw new IllegalArgumentException("the map given is null");
    if (map.containsKey(null))
      throw new IllegalArgumentException("the map given contains a null key");
    Set keys = map.keySet();
    for (Object string : keys)
      if (!(string instanceof String))
        throw new IllegalArgumentException("the map contains a non String key");
    Collection values = map.values();
    for (Object name : values)
      if (!(name instanceof String[]))
        throw new IllegalArgumentException("the map contains a non String[] value");
    parameters = map;
  }

  /**
   * Overridden method.
   *
   * @return parameter map
   * @see javax.portlet.BaseURL#getParameterMap()
   */
  public final Map<String, String[]> getParameterMap() {
    return parameters;
  }

  // Spec draft nr.20 new methods

  /**
   * Overridden method.
   *
   * @param key key
   * @param value value
   * @see javax.portlet.BaseURL#addProperty(java.lang.String, java.lang.String)
   */
  public final void addProperty(final String key, final String value) {
    if (key == null)
      throw new IllegalArgumentException("the property key given is null");

    List<String> propvalues = properties.get(key);
    if (propvalues == null)
      propvalues = new ArrayList<String>();
    propvalues.add(value);
    properties.put(key, propvalues);

  }

  /**
   * Overridden method.
   *
   * @param key key
   * @param value value
   * @see javax.portlet.BaseURL#setProperty(java.lang.String, java.lang.String)
   */
  public final void setProperty(final String key, final String value) {
    if (key == null)
      throw new IllegalArgumentException("the property key given is null");

    ArrayList<String> propvalues = new ArrayList<String>();
    propvalues.add(value);
    properties.put(key, propvalues);

  }

  /**
   * @return property string
   */
  public final String getPropertyString() {
    return getPropertyString(defaultEscapeXml);
  }

  /**
   * @param escapeXML either to escape xml chars
   * @return property string
   */
  public final String getPropertyString(final boolean escapeXML) {
    StringBuffer sb = new StringBuffer();
    try {
      Set<String> names = properties.keySet();
      for (String name : names) {
        List<String> propvalues = properties.get(name);
        for (int i = 0; i <= propvalues.size(); i++) {
          sb.append(PCConstants.AMPERSAND);
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

  /**
   * Overridden method.
   *
   * @param isSecure is secure
   * @see javax.portlet.BaseURL#setSecure(boolean)
   */
  public final void setSecure(final boolean isSecure) {
    this.isSecure = isSecure;
    this.setSetSecureCalled(true);
  }

  /**
   * @param escapeXML either to escape xml chars
   * @return string representation of utl
   */
  public abstract String toString(boolean escapeXML);

  /**
   * Overridden method.
   * The returned URL is not XML escaped.
   *
   * @return string representation of url
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return toString(false);
  }

  /**
   * @param s string to encode
   * @param escapeXML either to escape xml chars
   * @return encoded string
   */
  protected String encode(String s, final boolean escapeXML) {
    if ((s == null) || (s == ""))
      return "";
    if (escapeXML)
      s = encodeChars(s);
    try {
      return URLEncoder.encode(s, "utf-8");
    } catch (java.io.UnsupportedEncodingException e) {
      return s;
    }
  }

  /**
   * @param s string to encode
   * @return encoded string
   */
  protected final String encode(final String s) {
    return encode(s, false);
  }

  /**
   * @param s string to encode
   * @return encoded string
   */
  protected final String encodeChars(final String s) {
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
        .replace("\"", "&#034;").replace("'", "&#039;");
  }

  /**
   * Overridden method.
   * The URL written to the output stream is always XML escaped.
   * 
   * @param out writer
   * @throws IOException exception
   * @see javax.portlet.BaseURL#write(java.io.Writer)
   */
  public final void write(final Writer out) throws IOException {
    out.write(toString(true));
  }

  /**
   * Overridden method.
   *
   * @param out writer
   * @param escapeXML either to escape xml chars
   * @throws IOException exception
   * @see javax.portlet.BaseURL#write(java.io.Writer, boolean)
   */
  public final void write(final Writer out, final boolean escapeXML) throws IOException {
    out.write(toString(escapeXML));
  }

  /**
   * @return the portletDatas
   */
  protected Portlet getPortletDatas() {
    return portletDatas;
  }

  /**
   * @param type the type to set
   */
  protected void setType(String type) {
    this.type = type;
  }

  /**
   * @return the type
   */
  protected String getType() {
    return type;
  }

  /**
   * @param isCurrentlySecured the isCurrentlySecured to set
   */
  protected void setCurrentlySecured(boolean isCurrentlySecured) {
    this.isCurrentlySecured = isCurrentlySecured;
  }

  /**
   * @return the isCurrentlySecured
   */
  protected boolean isCurrentlySecured() {
    return isCurrentlySecured;
  }

  /**
   * @param setSecureCalled the setSecureCalled to set
   */
  protected void setSetSecureCalled(boolean setSecureCalled) {
    this.setSecureCalled = setSecureCalled;
  }

  /**
   * @return the setSecureCalled
   */
  protected boolean isSetSecureCalled() {
    return setSecureCalled;
  }

  /**
   * @return the isSecure
   */
  protected boolean isSecure() {
    return isSecure;
  }

}
