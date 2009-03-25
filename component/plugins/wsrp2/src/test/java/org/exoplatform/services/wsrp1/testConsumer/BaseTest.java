/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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

package org.exoplatform.services.wsrp1.testConsumer;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.exoplatform.Constants;
import org.exoplatform.commons.Environment;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.wsrp2.consumer.PortletRegistry;
import org.exoplatform.services.wsrp2.consumer.Producer;
import org.exoplatform.services.wsrp2.consumer.ProducerRegistry;
import org.exoplatform.services.wsrp2.consumer.URLGenerator;
import org.exoplatform.services.wsrp2.consumer.URLRewriter;
import org.exoplatform.services.wsrp2.consumer.UserRegistry;
import org.exoplatform.services.wsrp2.type.PersonName;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.UserContext;
import org.exoplatform.services.wsrp2.type.UserProfile;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 2
 *         févr. 2004 Time: 17:39:19 Revision: Max Shaposhnik 17.07.2008
 */

public class BaseTest extends TestCase {

  protected static final String   CONTEXT_PATH                                    = "/war_template1";

  protected static final String   TEST_PATH                                       = (System.getProperty("testPath") == null ? "."
                                                                                                                           : System.getProperty("testPath"));

  protected static final String   PORTLET_APP_PATH                                = "file:"
                                                                                      + TEST_PATH
                                                                                      + "/src/test"
                                                                                      + CONTEXT_PATH;

  protected ProducerRegistry      producerRegistry;

  protected Producer              producer;

  protected RegistrationData      registrationData;

//  protected PortalContainer          container;

  protected static final String[] USER_CATEGORIES_ARRAY                           = { "full",
      "standard", "minimal"                                                      };

  public static final String[]    CONSUMER_MODES                                  = { "wsrp:view",
      "wsrp:edit"                                                                };

  public static final String[]    CONSUMER_STATES                                 = {
      "wsrp:normal", "wsrp:maximized"                                            };

  public static final String[]    CONSUMER_SCOPES                                 = { "chunk_data" };

  public static final String[]    CONSUMER_CUSTOM_PROFILES                        = { "what_more" };

  public static final String      PRODUCER_ID                                     = "producerID";

  public static final String      PRODUCER_DESCRIPTION                            = "producerDescription";

  public static final String      PRODUCER_NAME                                   = "producerName";

  public static final String      PRODUCER_MARKUP_INTERFACE_ENDPOINT              = "markupInterfaceEndpoint";

  public static final String      PRODUCER_PORTLET_MANAGEMENT_INTERFACE_ENDPOINT  = "PortletManagementInterfaceEndpoint";

  public static final String      PRODUCER_REGISTRATION_INTERFACE_ENDPOINT        = "RegistrationInterfaceEndpoint";

  public static final String      PRODUCER_SERVICE_DESCRIPTION_INTERFACE_ENDPOINT = "ServiceDescriptionInterfaceEndpoint";

  public static final String[]    desiredLocales                                  = { "en" };

  protected PortletRegistry       portletRegistry;

  protected UserRegistry          userRegistry;

  protected UserContext           userContext;

  protected PersonName            personName;

  protected UserProfile           userProfile;

  public static final String      BASE_URL                                        = "/portal/faces/portal/portal.jsp?portal:ctx="
                                                                                      + Constants.DEFAUL_PORTAL_OWNER;

  protected URLGenerator          urlGenerator;

//  protected URLRewriter           urlRewriter;

//  private MockServletContext         mockServletContext;
//
//  private PortletApp                 portletApp_;
//
//  private PortletApplicationsHolder  holder;
//
//  private PortletApplicationRegister portletApplicationRegister;
//
//  private MockServletRequest         mockServletRequest;
//
//  private MockServletResponse        mockServletResponse;

  protected int                   platform                                        = 0;

  protected ExoContainer          container;

  protected void setUp() throws Exception {

    URL url = new URL(PORTLET_APP_PATH + "/WEB-INF/portlet.xml");
    InputStream is = url.openStream();
//    portletApp_ = XMLParser.parse(is, false);

    try {
      // Leaving for compatibility reasons
      //container = PortalContainer.getInstance();
      //container = RootContainer.getInstance().getPortalContainer("portal");
      container = StandaloneContainer.getInstance(Thread.currentThread().getContextClassLoader());
    } catch (Throwable t) {
      t.printStackTrace();
    }

    int platform = Environment.getInstance().getPlatform();
    System.out.println(">>> Consumer BaseTest.setUp() platform = " + platform);
    Collection<String> roles = new ArrayList<String>();
    roles.add("auth-user");

//    urlRewriter = (URLRewriter) container.getComponentInstanceOfType(URLRewriter.class);

  }

  public void tearDown() throws Exception {
    try {
//      portletApplicationRegister.removePortletApplication(mockServletContext, "war_template1");
      HibernateService hservice = (HibernateService) container.getComponentInstanceOfType(HibernateService.class);
      hservice.closeSession();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
