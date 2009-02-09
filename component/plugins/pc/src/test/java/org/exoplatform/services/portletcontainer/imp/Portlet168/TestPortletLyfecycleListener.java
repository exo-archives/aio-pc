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
package org.exoplatform.services.portletcontainer.imp.Portlet168;

import javax.portlet.PortletConfig;
import javax.servlet.ServletContext;

import org.exoplatform.services.portletcontainer.PortletLifecycleListener;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public class TestPortletLyfecycleListener extends BaseTest{
  public TestPortletLyfecycleListener(String s) {
    super(s);
  }

  public void testRegisterListener(){
    //portletApplicationRegister.addListener(new Listener());
  }

  class Listener implements PortletLifecycleListener {
    public void preDeploy(String portletApplicationName, PortletApp portletApplication, ServletContext servletContext) {

    }

    public void postDeploy(String portletApplicationName, PortletApp portletApplication, ServletContext servletContext) {

    }

    public void preInit(PortletConfig portletConfig) {   }

    public void postInit(PortletConfig portletConfig) {  }

    public void preDestroy() {  }

    public void postDestroy() {  }

    public void preUndeploy(String portletApplicationName, PortletApp portletApplication, ServletContext servletContext) {

    }

    public void postUndeploy(String portletApplicationName, PortletApp portletApplication, ServletContext servletContext) {

    }

    public String getName() {   return "listener";  }

    public void setName(String arg0) {  }

    public String getDescription() {    return "NA" ;  }

    public void setDescription(String arg0) {   }

  }
}
