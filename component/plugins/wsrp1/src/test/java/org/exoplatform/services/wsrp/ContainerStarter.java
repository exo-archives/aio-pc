/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp;

/**
 * Created by The eXo Platform SAS Author : Max Shaposhnik
 * max.shaposhnik@exoplatform.com.ua 15.07.2008
 */

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.cargo.container.ContainerType;
import org.codehaus.cargo.container.InstalledLocalContainer;
import org.codehaus.cargo.container.configuration.ConfigurationType;
import org.codehaus.cargo.container.configuration.LocalConfiguration;
import org.codehaus.cargo.container.deployable.WAR;
import org.codehaus.cargo.container.installer.Installer;
import org.codehaus.cargo.container.installer.ZipURLInstaller;
import org.codehaus.cargo.container.property.ServletPropertySet;
import org.codehaus.cargo.generic.DefaultContainerFactory;
import org.codehaus.cargo.generic.configuration.DefaultConfigurationFactory;

public class ContainerStarter {

  private static InstalledLocalContainer container;

  protected static final String          TEST_PATH        = (System.getProperty("testPath") == null ? "."
                                                                                                   : System.getProperty("testPath"));

  protected static final String          PORTLET_WAR_PATH = TEST_PATH + "/target/hello.war";

  //protected static final String                 WSRP_STARTER_PATH        =  TEST_PATH + "/../../../applications/wsrp1-starter/target/wsrp1.war";
  protected static final String          WSRP_LIB_PATH    = TEST_PATH + "/target/test";

  protected static boolean               isStarted;

  static boolean start() {

    //Configuring & starting an Tomcat5x container
    try {
      Installer installer = new ZipURLInstaller(new java.net.URL("http://www.apache.org/dist/tomcat/tomcat-5/v5.5.25/bin/apache-tomcat-5.5.25.zip"),
                                                System.getProperty("java.io.tmpdir"));
      installer.install();

      LocalConfiguration configuration = (LocalConfiguration) new DefaultConfigurationFactory().createConfiguration("tomcat5x",
                                                                                                                    ContainerType.INSTALLED,
                                                                                                                    ConfigurationType.STANDALONE);

      configuration.setProperty(ServletPropertySet.PORT, "8080");
      System.setProperty("catalina.home", installer.getHome());

      configuration.addDeployable(new WAR(PORTLET_WAR_PATH));
      // configuration.addDeployable(new WAR(WSRP_STARTER_PATH));

      container = (InstalledLocalContainer) new DefaultContainerFactory().createContainer("tomcat5x",
                                                                                          ContainerType.INSTALLED,
                                                                                          configuration);

      container.setHome(installer.getHome());
      
      container.setOutput(TEST_PATH + "/target/container.log");

    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    // Looking for dependencies
    String[] arr;
    List<String> lst = new ArrayList<String>();
    File dir = new File(WSRP_LIB_PATH);
    arr = dir.list(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith(".jar");
      }
    });
    for (String name : arr) {
      lst.add(WSRP_LIB_PATH + "/" + name);
    }

    String[] arr2 = new String[lst.size()];
    lst.toArray(arr2);
    container.setExtraClasspath(arr2);

    container.start();
    isStarted = container.getState().isStarted();
    System.out.println("Container is started : " + isStarted);
    return isStarted;
  }

  static boolean stop() {
    try {
      container.stop();
      isStarted = container.getState().isStarted();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return isStarted;
  }

  public boolean isStarted() {
    return isStarted;
  }

}
