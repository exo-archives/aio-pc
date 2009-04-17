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
package org.exoplatform.services.portletcontainer.test.plugins;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.model.Description;
import org.exoplatform.services.portletcontainer.pci.model.DisplayName;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.InitParam;
import org.exoplatform.services.portletcontainer.pci.model.SecurityRoleRef;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.portletcontainer.pci.model.UserAttribute;

public class PortletDataTestImpl implements PortletData {
  
  String PORTLET_TITLE = "javax.portlet.title";

  String PORTLET_SHORT_TITLE = "javax.portlet.short-title";

  String KEYWORDS = "javax.portlet.keywords";
  
  

  public List<DisplayName> getDisplayName()
  {
    DisplayName dName = new DisplayName();
    dName.setDisplayName("PortletToTestFilters");
    ArrayList<DisplayName> ret = new ArrayList<DisplayName>();
    ret.add(dName);
    return ret;
    
  }

  public List<SecurityRoleRef> getSecurityRoleRef()
  {
    SecurityRoleRef ref = new SecurityRoleRef();
    ref.setRoleName("TestRoleName");
    ArrayList<SecurityRoleRef> ret = new ArrayList<SecurityRoleRef>();
    ret.add(ref);
    return ret;
  }

  public List<InitParam> getInitParam()
  {
    InitParam param = new InitParam();
    param.setName("TestParamName");
    param.setValue("TestParamValue");
    ArrayList ret = new ArrayList();
    ret.add(param);
    return ret;
    
  }

  public List<Supports> getSupports()
  {
    Supports supp = new Supports();
    supp.setMimeType("text/html");
    supp.addPortletMode("edit");
    supp.addWindowState("half-page");
    ArrayList ret = new ArrayList();
    ret.add(supp);
    return ret;
    
  }

  public List<Description> getDescription()
  {
    Description desc = new Description();
    desc.setDescription("TestDescription");
    desc.setLang("ENG");
    ArrayList ret = new ArrayList();
    ret.add(desc);
    return ret;
  }

  public String getDescription(String lang)
  {
    return "TestDescription";
  }

  public boolean isCacheGlobal()
  {
    return false;
  }

  public String getExpirationCache()
  {
   return "0";  
  }

  public String getPortletName()
  {
   return "TestPortletName"; 
  }

  public List<Locale> getSupportedLocale()
  {
    ArrayList ret = new ArrayList();
    ret.add(Locale.ENGLISH);
    return ret;
  }

  public ExoPortletPreferences getPortletPreferences()
  {
    ExoPortletPreferences pref = new ExoPortletPreferences();
    pref.setId("TestID");
    return pref;
  }

  public boolean isSecure() 
  {
   return true;  
  }

  public List<UserAttribute> getUserAttributes()
  {
    UserAttribute attr = new UserAttribute();
    attr.setName("TestUserAttribute");
    Description descr = new Description();
    descr.setDescription("TestDescription");
    attr.addDescription(descr);
    ArrayList ret = new ArrayList();
    ret.add(attr);
    return ret;
  }

  public List<QName> getSupportedProcessingEvent() 
  {
    ArrayList ret = new ArrayList();
    QName qn =  new QName("TEST", "test");
    ret.add(qn);
    return ret;
  }

  public List<QName> getSupportedPublishingEvent() 
  {
    ArrayList ret = new ArrayList();
    QName qn =  new QName("TEST", "test");
    ret.add(qn);
    return ret;
  }

  public List<String> getSupportedPublicRenderParameter()
  {
    ArrayList ret = new ArrayList();
    ret.add("RenderParamether1");
    ret.add("RenderParamether2");
    return ret; 
  }

  public Map<String, String[]> getContainerRuntimeOption()
  {
    return null;
  }

  public boolean getEscapeXml()
  {
    return true;
  }
}
