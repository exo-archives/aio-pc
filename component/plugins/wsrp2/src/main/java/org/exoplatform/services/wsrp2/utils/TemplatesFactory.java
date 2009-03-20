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
package org.exoplatform.services.wsrp2.utils;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.type.Templates;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Mar 4, 2009
 */
public class TemplatesFactory {

  private static final Log LOG = ExoLogger.getLogger(TemplatesFactory.class);

  public static String getTemplate(Templates templates, boolean secure, String type) {
    if (type == null || type == "") {
      if (secure) {
        return templates.getSecureDefaultTemplate();
      } else {
        return templates.getDefaultTemplate();
      }
    }
    if (type.equalsIgnoreCase(WSRPConstants.URL_TYPE_RESOURCE)) {
      if (secure) {
        return templates.getSecureResourceTemplate();
      } else {
        return templates.getResourceTemplate();
      }
    }
    if (type.equalsIgnoreCase(WSRPConstants.URL_TYPE_RENDER)) {
      if (secure) {
        return templates.getSecureRenderTemplate();
      } else {
        return templates.getRenderTemplate();
      }
    }
    if (type.equalsIgnoreCase(WSRPConstants.URL_TYPE_BLOCKINGACTION)) {
      if (secure) {
        return templates.getSecureBlockingActionTemplate();
      } else {
        return templates.getBlockingActionTemplate();
      }
    }
    return null;
  }

}
