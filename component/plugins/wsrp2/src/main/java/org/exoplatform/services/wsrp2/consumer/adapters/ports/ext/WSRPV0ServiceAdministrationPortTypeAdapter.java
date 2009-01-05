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
package org.exoplatform.services.wsrp2.consumer.adapters.ports.ext;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.bind.ext.WSRPV0ServiceAdministrationPortType;
import org.exoplatform.services.wsrp2.utils.Utils;

/**
 * Created by The eXo Platform SAS .
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey Zavizionov</a>
 * @version $Id: $
 *
 * Dec 15, 2008  
 */
public class WSRPV0ServiceAdministrationPortTypeAdapter {
  private static final Log LOG = ExoLogger.getLogger(WSRPV0ServiceAdministrationPortTypeAdapter.class);
  
  
  private WSRPV0ServiceAdministrationPortType serviceAdministrationPort;


  public WSRPV0ServiceAdministrationPortTypeAdapter(String address) {
//    String address = "http://localhost:8080/hello/soap/services/WSRP_v0_ServiceAdministration_Service";
    JaxWsProxyFactoryBean client = new JaxWsProxyFactoryBean();
    client.setServiceClass(WSRPV0ServiceAdministrationPortType.class);
    client.setAddress(address);
//    client.getInInterceptors().add(new LoggingInInterceptor());
//    client.getOutInterceptors().add(new LoggingOutInterceptor());
    Object obj = client.create();
    this.serviceAdministrationPort = (WSRPV0ServiceAdministrationPortType) obj;
  }

  public Map<String, String> getServiceAdministration(String propString) {

//    propString = "{wsrp.uses.method.get=false, wsrp.has.user.specific.state=true, wsrp.requires.registration=true, wsrp.perform.blocking.interaction.optimized=false, wsrp.user.context.stored.in.session=false, wsrp.save.portlet.state.on.consumer=false, wsrp.templates.stored.in.session=false, wsrp.does.url.template.processing=true, wsrp.save.registration.state.on.consumer=false}";

    javax.xml.ws.Holder<String> properties = new javax.xml.ws.Holder<String>(propString);
    serviceAdministrationPort.getServiceAdministration(properties);

    String propStringResult = properties.value;
    Map<String, String> realProps = Utils.getMapFromString(propStringResult);
    return realProps;
    
  }
  
}
