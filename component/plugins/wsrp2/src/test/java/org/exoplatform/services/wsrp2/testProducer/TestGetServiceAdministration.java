/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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
package org.exoplatform.services.wsrp2.testProducer;

import java.util.Map;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.exoplatform.services.wsrp2.bind.extensions.WSRPV0ServiceAdministrationPortType;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.ext.WSRPV0ServiceAdministrationPortTypeAdapter;
import org.exoplatform.services.wsrp2.producer.impl.WSRPConfiguration;
import org.exoplatform.services.wsrp2.utils.Utils;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Dec 15, 2008
 */
public class TestGetServiceAdministration extends BaseTest {


  public void setUp() throws Exception {
    super.setUp();
    log();
  }

  public void testGetServiceAdministration() throws Exception {

    JaxWsProxyFactoryBean client = new JaxWsProxyFactoryBean();
    client.setServiceClass(WSRPV0ServiceAdministrationPortType.class);
    client.setAddress(ADMINISTRATION_ADDRESS);
    client.getInInterceptors().add(new LoggingInInterceptor());
    client.getOutInterceptors().add(new LoggingOutInterceptor());
    Object obj = client.create();
    WSRPV0ServiceAdministrationPortType serviceAdministrationPort = (WSRPV0ServiceAdministrationPortType) obj;
    assertNotNull(serviceAdministrationPort);

    String propString = null;
//    propString = "{wsrp.uses.method.get=false, wsrp.has.user.specific.state=true, wsrp.requires.registration=true, wsrp.perform.blocking.interaction.optimized=false, wsrp.user.context.stored.in.session=false, wsrp.save.portlet.state.on.consumer=false, wsrp.templates.stored.in.session=false, wsrp.does.url.template.processing=true, wsrp.save.registration.state.on.consumer=false}";
//    propString = "wsrp.requires.registration=true";

    // it gets boolean value property on a producer
    javax.xml.ws.Holder<String> properties = new javax.xml.ws.Holder<String>(propString);
    serviceAdministrationPort.getServiceAdministration(properties);

    String propStringResult1 = properties.value;
    assertNotNull(propStringResult1);
    Map<String, String> realProps = Utils.getMapFromString(propStringResult1);

    if (realProps.containsKey(WSRPConfiguration.REQUIRES_REGISTRATION)) {
      String value = realProps.get(WSRPConfiguration.REQUIRES_REGISTRATION);
      assertNotNull(value);
      propString = WSRPConfiguration.REQUIRES_REGISTRATION.concat("=")
                                                          .concat(value.equalsIgnoreCase("true") ? "false"
                                                                                                : "true");
    } else {
      fail();
    }

    // it changes boolean value property on a producer
    properties = new javax.xml.ws.Holder<String>(propString);
    serviceAdministrationPort.getServiceAdministration(properties);

    String propStringResult2 = properties.value;
    assertNotNull(propStringResult2);
    Map<String, String> realProps2 = Utils.getMapFromString(propStringResult2);

    if (realProps.containsKey(WSRPConfiguration.REQUIRES_REGISTRATION)) {
      String value = realProps2.get(WSRPConfiguration.REQUIRES_REGISTRATION);
      assertNotNull(value);
      propString = WSRPConfiguration.REQUIRES_REGISTRATION.concat("=")
                                                          .concat(value.equalsIgnoreCase("true") ? "false"
                                                                                                : "true");
    } else {
      fail();
    }

    // it changes back boolean value property on a producer
    properties = new javax.xml.ws.Holder<String>(propString);
    serviceAdministrationPort.getServiceAdministration(properties);

    String propStringResult3 = properties.value;
    assertNotNull(propStringResult3);

  }

  public void testGetServiceAdministrationAdapter() throws Exception {

    WSRPV0ServiceAdministrationPortTypeAdapter administration = new WSRPV0ServiceAdministrationPortTypeAdapter(ADMINISTRATION_ADDRESS);
    assertNotNull(administration);

    // it gets boolean value property on a producer
    Map<String, String> realProps = administration.getServiceAdministration(null);
    assertNotNull(realProps);

    if (realProps.containsKey(WSRPConfiguration.REQUIRES_REGISTRATION)) {
      String value = realProps.get(WSRPConfiguration.REQUIRES_REGISTRATION);
      assertNotNull(value);
    } else {
      fail();
    }
  }

}
