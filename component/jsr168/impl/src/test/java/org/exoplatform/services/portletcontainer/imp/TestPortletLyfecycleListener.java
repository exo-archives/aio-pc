package org.exoplatform.services.portletcontainer.imp;


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
