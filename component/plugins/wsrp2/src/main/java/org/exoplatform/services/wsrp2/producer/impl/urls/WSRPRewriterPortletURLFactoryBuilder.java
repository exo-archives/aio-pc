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
package org.exoplatform.services.wsrp2.producer.impl.urls;

import java.util.List;

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.pci.PortletURLFactory;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.producer.TransientStateManager;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpSession;
import org.exoplatform.services.wsrp2.type.RuntimeContext;
import org.exoplatform.services.wsrp2.type.Templates;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Jan 15, 2009
 */
public class WSRPRewriterPortletURLFactoryBuilder {
  private static final Log LOG = ExoLogger.getLogger(WSRPRewriterPortletURLFactoryBuilder.class);

  public static PortletURLFactory getFactory(boolean isDoesUrlTemplateProcessing,
                                             RuntimeContext runtimeContext,
                                             WSRPHttpSession session,

                                             boolean isTemplatesStoredInSession,
                                             TransientStateManager transientStateManager,

                                             String mimeType,
                                             List<Supports> supports,
                                             boolean isCurrentlySecured,
                                             String portletHandle,
                                             PersistentStateManager persistentStateManager,
                                             String sessionID,
                                             boolean defaultEscapeXml,
                                             String cacheLevel,
                                             List<String> supportedPublicRenderParameter,
                                             Portlet portlet,
                                             String user) {
    if (isDoesUrlTemplateProcessing) {// default is true
      LOG.debug("Producer URL rewriting");
      Templates templates = manageTemplates(runtimeContext,
                                            session,
                                            isTemplatesStoredInSession,
                                            transientStateManager);
      return new WSRPProducerRewriterPortletURLFactory(mimeType,
                                                       templates,
                                                       supports,
                                                       isCurrentlySecured,
                                                       portletHandle,
                                                       persistentStateManager,
                                                       sessionID,
                                                       defaultEscapeXml,
                                                       cacheLevel,
                                                       supportedPublicRenderParameter,
                                                       portlet,
                                                       user,
                                                       runtimeContext);
    } else {
      LOG.debug("Consumer URL rewriting");
      return new WSRPConsumerRewriterPortletURLFactory(mimeType,
                                                       supports,
                                                       isCurrentlySecured,
                                                       portletHandle,
                                                       persistentStateManager,
                                                       sessionID,
                                                       defaultEscapeXml,
                                                       cacheLevel,
                                                       supportedPublicRenderParameter,
                                                       portlet,
                                                       user);
    }

  }

  private static Templates manageTemplates(RuntimeContext runtimeContext,
                                           WSRPHttpSession session,
                                           boolean isTemplatesStoredInSession,
                                           TransientStateManager transientStateManager) {
    Templates templates = runtimeContext.getTemplates();
    if (isTemplatesStoredInSession) { // default is false
      LOG.debug("Optimized mode : templates store in session");
      if (templates == null) {
        LOG.debug("Optimized mode : retrieves the template from session");
        templates = transientStateManager.getTemplates(session);
      } else {
        LOG.debug("Optimized mode : store the templates in session");
        transientStateManager.storeTemplates(templates, session);
      }
    }
    return templates;
  }

}
