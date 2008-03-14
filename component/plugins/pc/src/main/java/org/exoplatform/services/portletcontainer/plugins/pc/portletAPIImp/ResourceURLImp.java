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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletURLGenerationListener;
import javax.portlet.ResourceURL;

import org.exoplatform.Constants;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;

/**
 * Created by The eXo Platform SAS.
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 11:13:44 PM
 */
public class ResourceURLImp extends BaseURLImp implements ResourceURL {

  /**
   * Resource id.
   */
  protected String resourceID;

  /**
   * Original cache level.
   */
  protected String originalCacheLevel;

  /**
   * Cache level.
   */
  protected String cacheLevel;

  /**
   * Render parameters.
   */
  protected Map<String, String[]> renderParams;

  /**
   * @param type url type
   * @param baseURL base url
   * @param isCurrentlySecured is currently secured
   * @param defaultEscapeXml default escape xml
   * @param cacheLevel cache level
   * @param portletDatas portlet datas
   * @param renderParams render parameters
   */
  public ResourceURLImp(final String type,
      final String baseURL,
      final boolean isCurrentlySecured,
      final boolean defaultEscapeXml,
      final String cacheLevel,
      final Portlet portletDatas,
      final Map<String, String[]> renderParams) {
    super(type, baseURL, isCurrentlySecured, defaultEscapeXml, portletDatas);

    this.renderParams = renderParams;

    if (cacheLevel == null)
      this.originalCacheLevel = ResourceURL.PAGE;
    else
      this.originalCacheLevel = cacheLevel;
    this.cacheLevel = this.originalCacheLevel;
  }

  /**
   * Invokes resource url filters.
   */
  protected final void invokeFilterResourceURL() {
    if (getPortletDatas() == null)
      return;
    List<PortletURLGenerationListener> list = getPortletDatas().getApplication().getUrlListeners();
    if (list == null)
      return;
    for (PortletURLGenerationListener listener : list)
      try {
        listener.filterResourceURL(this);
      } catch (Exception e) {
      }
  }

  /**
   * Overridden method.
   *
   * @param escapeXML escape xml
   * @return string representation of an url
   * @see org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.BaseURLImp#toString(boolean)
   */
  public final String toString(final boolean escapeXML) {
    invokeFilterResourceURL();

    if (!isSetSecureCalled() && isCurrentlySecured())
      setSecure(true);

    StringBuffer sB = new StringBuffer();
    sB.append(baseURL);

    sB.append(Constants.AMPERSAND);
    sB.append(Constants.TYPE_PARAMETER);
    sB.append("=");
    sB.append(getType());

    sB.append(Constants.AMPERSAND);
    sB.append(Constants.SECURE_PARAMETER);
    sB.append("=");
    sB.append(isSecure());

    if (resourceID != null) {
      sB.append(Constants.AMPERSAND);
      sB.append(Constants.RESOURCE_ID_PARAMETER);
      sB.append("=");
      sB.append(resourceID);
    }

    if (cacheLevel != null) {
      sB.append(Constants.AMPERSAND);
      sB.append(Constants.CACHELEVEL_PARAMETER);
      sB.append("=");
      sB.append(cacheLevel);
    }

    try {
      Set<String> names = parameters.keySet();
      for (String name : names) {
        Object obj = parameters.get(name);
        if (obj instanceof String) {
          String value = (String) obj;
          sB.append(Constants.AMPERSAND);
          sB.append(encode(name, escapeXML));
          sB.append("=");
          sB.append(encode(value, escapeXML));
        } else {
          String[] values = (String[]) obj;
          for (String element : values) {
            sB.append(Constants.AMPERSAND);
            sB.append(encode(name, escapeXML));
            sB.append("=");
            sB.append(encode(element, escapeXML));
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if ((renderParams != null)
        && (cacheLevel.equals(ResourceURL.PAGE) || cacheLevel.equals(ResourceURL.PORTLET)))
      try {
        Set<String> names = renderParams.keySet();
        for (String name : names) {
          Object obj = renderParams.get(name);
          if (obj instanceof String) {
            String value = (String) obj;
            sB.append(Constants.AMPERSAND);
            sB.append(encode(name, escapeXML));
            sB.append("=");
            sB.append(encode(value, escapeXML));
          } else {
            String[] values = (String[]) obj;
            for (String element : values) {
              sB.append(Constants.AMPERSAND);
              sB.append(encode(name, escapeXML));
              sB.append("=");
              sB.append(encode(element, escapeXML));
            }
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

    String propertyString = getPropertyString(escapeXML);
    if ((propertyString != "") && (propertyString != null))
      // sB.append(Constants.AMPERSAND);
      sB.append(propertyString);
    return sB.toString();
  }

  /**
   * Overridden method.
   *
   * @param resourceID resource id
   * @see javax.portlet.ResourceURL#setResourceID(java.lang.String)
   */
  public final void setResourceID(final String resourceID) {
    this.resourceID = resourceID;
  }

  /**
   * @return resource id
   */
  public final String getResourceID() {
    return resourceID;
  }

  /**
   * Overridden method.
   *
   * @return cacheability
   * @see javax.portlet.ResourceURL#getCacheability()
   */
  public final String getCacheability() {
    return this.cacheLevel;
  }

  /**
   * Overridden method.
   *
   * cannot set less cache level than was
   * cannot set null cache level
   * default "cacheLevelPage"
   * originalCacheLevel >= cacheLevel should set
   *
   * @param cacheLevel
   * @see javax.portlet.ResourceURL#setCacheability(java.lang.String)
   */
  public final void setCacheability(final String cacheLevel) {
    if (cacheLevel != null) {
      if (!isSupportedCacheLevel(cacheLevel))
        throw new IllegalStateException("Cacheability level error: the cache level '" + cacheLevel
            + "' is unknown to the portlet container");
      if (getSupportedCacheLevel().indexOf(originalCacheLevel) < getSupportedCacheLevel().indexOf(
          cacheLevel))
        throw new IllegalStateException("Cacheability level error: wants to become cache level '"
            + cacheLevel + "' a weaker than the parent '" + originalCacheLevel + "'.");
      this.cacheLevel = cacheLevel;
    }
  }

  /**
   * @param cacheLevel cache level
   * @return is it supported
   */
  public static final boolean isSupportedCacheLevel(final String cacheLevel) {
    return getSupportedCacheLevel().contains(cacheLevel);
  }

  /**
   * @return cache levels
   */
  private static List<String> getSupportedCacheLevel() {
    List<String> result = new ArrayList<String>();
    result.add(ResourceURL.FULL);
    result.add(ResourceURL.PORTLET);
    result.add(ResourceURL.PAGE);
    return result;
  }

}
