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

package org.exoplatform.services.wsrp2.bind.ext;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.ws.AbstractSingletonWebService;
import org.exoplatform.services.wsrp2.producer.ServiceAdministrationInterface;
import org.exoplatform.services.wsrp2.utils.Utils;

/**
 * 
 */

@javax.jws.WebService(name = "WSRPV0ServiceAdministrationPortType", serviceName = "WSRPService0", portName = "WSRP_v0_ServiceAdministration_Service")
public class WSRPV0ServiceAdministrationPortTypeImpl implements
    WSRPV0ServiceAdministrationPortType, AbstractSingletonWebService {

  private static final Log               LOG = ExoLogger.getLogger(WSRPV0ServiceAdministrationPortTypeImpl.class.getName());

  private ServiceAdministrationInterface serviceAdministrationInterface;

  public WSRPV0ServiceAdministrationPortTypeImpl(ServiceAdministrationInterface serviceAdministrationInterface) {
    this.serviceAdministrationInterface = serviceAdministrationInterface;
  }

  public void getServiceAdministration(javax.xml.ws.Holder<String> properties) {

    LOG.info("Executing operation getServiceAdministration");
    LOG.info(properties);

    try {

      Map<String, String> props = new HashMap<String, String>();
      if (properties.value != null)
        props = Utils.getMapFromString(properties.value);

      ServiceAdministration response = serviceAdministrationInterface.getServiceAdministration(props);
      properties.value = response.getPropertiesAsString();

    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
  }

}
