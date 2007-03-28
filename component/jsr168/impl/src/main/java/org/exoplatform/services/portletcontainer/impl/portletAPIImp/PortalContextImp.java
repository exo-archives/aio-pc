/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 6:15:45 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp;


import javax.portlet.PortalContext;
import org.exoplatform.services.portletcontainer.impl.PortletContainerConf;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class PortalContextImp implements PortalContext {

  private Map properties = new HashMap();
	private PortletContainerConf conf;

  public PortalContextImp(PortletContainerConf conf) {
    properties = conf.getProperties();
		this.conf = conf;
  }

  public String getProperty(String s) {
    return (String) properties.get(s);
  }

  public Enumeration getPropertyNames() {
    return new Vector(properties.keySet()).elements();
  }

  public Enumeration getProperties(String s) {
    return (Enumeration) properties.get(s);
  }

  public void addProperty(String s, Object o) {
    properties.put(s, o);
  }

  public Enumeration getSupportedPortletModes() {
		return conf.getSupportedPortletModes();
  }

  public Enumeration getSupportedWindowStates() {
    return conf.getSupportedWindowStates();
  }

  public String getPortalInfo() {
    String name = conf.getPortletContainerName();
    int major = conf.getMajorVersion();
    int minor = conf.getMinorVersion();
    return name + major + "." + minor;
  }

}
