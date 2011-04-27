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
package org.exoplatform.services.wsrp.security;

import org.apache.commons.logging.Log;
import org.apache.ws.security.WSPasswordCallback;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.wsrp.AdminClient;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com">Alexey Zavizionov</a>
 * @version $Id:  $
 *
 */
public class PasswordHandler implements CallbackHandler {

  private final static Log LOG = ExoLogger.getLogger("org.exoplatform.services.wsrp.security.PasswordHandler");

  /**
   * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
   */
  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

    for (int i = 0; i < callbacks.length; i++) {
      if (callbacks[i] instanceof WSPasswordCallback) {
        WSPasswordCallback pc = (WSPasswordCallback)callbacks[i];
        // set the password given a username
        String username = pc.getIdentifier();
        if (LOG.isDebugEnabled())
          LOG.debug("The username is '" + username + "'.");
        String password = null;
        if (AdminClient.getPassword() != null) {
          password = AdminClient.getPassword();
        } else {
          try {
            ExoContainer container = ExoContainerContext.getCurrentContainerIfPresent();
            OrganizationService orgService =
              (OrganizationService)container.getComponentInstanceOfType(OrganizationService.class);
            User user = orgService.getUserHandler().findUserByName(username);
            password = user.getPassword();
          }
          catch (Exception e) {
            LOG.error("Can't get the organization service, user and his password", e);
          }
        }
        if (password != null)
          pc.setPassword(password);
      }
      else {
        throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
      }
    }
  }

}
