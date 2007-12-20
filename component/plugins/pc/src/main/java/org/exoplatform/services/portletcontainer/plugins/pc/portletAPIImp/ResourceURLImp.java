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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletURLGenerationListener;
import javax.portlet.ResourceURL;

import org.exoplatform.Constants;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;

/**
 * Created by The eXo Platform SAS Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: Jul 29, 2003 Time: 11:13:44 PM
 */
public class ResourceURLImp extends BaseURLImp implements ResourceURL {

  protected String                resourceID;

  protected String                originalCacheLevel;

  protected String                cacheLevel;

  protected Map<String, String[]> renderParams;

  public ResourceURLImp(String type,
                        String baseURL,
                        boolean isCurrentlySecured,
                        boolean defaultEscapeXml,
                        String cacheLevel) {
    this(type, baseURL, isCurrentlySecured, defaultEscapeXml, cacheLevel, null, null);
  }

  public ResourceURLImp(String type,
                        String baseURL,
                        boolean isCurrentlySecured,
                        boolean defaultEscapeXml,
                        String cacheLevel,
                        Portlet portletDatas,
                        Map renderParams) {
    super(type, baseURL, isCurrentlySecured, defaultEscapeXml, portletDatas);

    this.renderParams = renderParams;

    if (cacheLevel == null) {
      this.originalCacheLevel = ResourceURL.PAGE;
    } else {
      this.originalCacheLevel = cacheLevel;
    }
    this.cacheLevel = this.originalCacheLevel;
  }

  protected void invokeFilterResourceURL() {
    if (portletDatas == null)
      return;
    List<PortletURLGenerationListener> list = portletDatas.getApplication().getUrlListeners();
    if (list == null)
      return;
    for (Iterator<PortletURLGenerationListener> i = list.iterator(); i.hasNext();) {
      PortletURLGenerationListener listener = i.next();
      try {
        listener.filterResourceURL(this);
      } catch (Exception e) {
      }
    }
  }

  public String toString(boolean escapeXML) {
    invokeFilterResourceURL();

    if (!setSecureCalled && isCurrentlySecured)
      isSecure = true;

    StringBuffer sB = new StringBuffer();
    sB.append(baseURL);

    sB.append(Constants.AMPERSAND);
    sB.append(Constants.TYPE_PARAMETER);
    sB.append("=");
    sB.append(type);

    sB.append(Constants.AMPERSAND);
    sB.append(Constants.SECURE_PARAMETER);
    sB.append("=");
    sB.append(isSecure);

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
      for (Iterator<String> iterator = names.iterator(); iterator.hasNext();) {
        String name = iterator.next();
        Object obj = parameters.get(name);
        if (obj instanceof String) {
          String value = (String) obj;
          sB.append(Constants.AMPERSAND);
          sB.append(encode(name, escapeXML));
          sB.append("=");
          sB.append(encode(value, escapeXML));
        } else {
          String[] values = (String[]) obj;
          for (int i = 0; i < values.length; i++) {
            sB.append(Constants.AMPERSAND);
            sB.append(encode(name, escapeXML));
            sB.append("=");
            sB.append(encode(values[i], escapeXML));
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (renderParams != null && (cacheLevel.equals(ResourceURL.PAGE) || cacheLevel.equals(ResourceURL.PORTLET))) {
      try {
        Set<String> names = renderParams.keySet();
        for (Iterator<String> iterator = names.iterator(); iterator.hasNext();) {
          String name = iterator.next();
          Object obj = renderParams.get(name);
          if (obj instanceof String) {
            String value = (String) obj;
            sB.append(Constants.AMPERSAND);
            sB.append(encode(name, escapeXML));
            sB.append("=");
            sB.append(encode(value, escapeXML));
          } else {
            String[] values = (String[]) obj;
            for (int i = 0; i < values.length; i++) {
              sB.append(Constants.AMPERSAND);
              sB.append(encode(name, escapeXML));
              sB.append("=");
              sB.append(encode(values[i], escapeXML));
            }
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    String propertyString = getPropertyString(escapeXML);
    if (propertyString != "" && propertyString != null) {
      // sB.append(Constants.AMPERSAND);
      sB.append(propertyString);
    }
    return sB.toString();
  }

  public void setResourceID(String resourceID) {
    this.resourceID = resourceID;
  }

  public String getResourceID() {
    return resourceID;
  }

  public String getCacheability() {
    return this.cacheLevel;
  }

  // cannot set less cache level than was
  // cannot set null cache level
  // default "cacheLevelPage"
  // originalCacheLevel >= cacheLevel should set
  public void setCacheability(String cacheLevel) {
    if (cacheLevel != null) {
      if (!isSupportedCacheLevel(cacheLevel)) {
        throw new IllegalStateException("Cacheability level error: the cache level '" + cacheLevel + "' is unknown to the portlet container");
      }
      if (getSupportedCacheLevel().indexOf(originalCacheLevel) < getSupportedCacheLevel().indexOf(cacheLevel)) {
        throw new IllegalStateException("Cacheability level error: wants to become cache level '" + cacheLevel + "' a weaker than the parent '"
            + originalCacheLevel + "'.");
      }
      this.cacheLevel = cacheLevel;
    }
  }

  public static boolean isSupportedCacheLevel(String cacheLevel) {
    return getSupportedCacheLevel().contains(cacheLevel);
  }

  private static List<String> getSupportedCacheLevel() {
    List<String> result = new ArrayList<String>();
    result.add(ResourceURL.FULL);
    result.add(ResourceURL.PORTLET);
    result.add(ResourceURL.PAGE);
    return result;
  }

}
