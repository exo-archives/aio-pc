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

package org.exoplatform.services.wsrp2.consumer.impl;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.consumer.ConsumerEnvironment;
import org.exoplatform.services.wsrp2.consumer.PortletDriverRegistry;
import org.exoplatform.services.wsrp2.consumer.PortletRegistry;
import org.exoplatform.services.wsrp2.consumer.ProducerRegistry;
import org.exoplatform.services.wsrp2.consumer.URLRewriter;
import org.exoplatform.services.wsrp2.consumer.URLTemplateComposer;
import org.exoplatform.services.wsrp2.consumer.UserRegistry;
import org.exoplatform.services.wsrp2.consumer.impl.urls.URLGeneratorImpl;
import org.exoplatform.services.wsrp2.consumer.impl.urls.URLRewriterImpl;
import org.exoplatform.services.wsrp2.consumer.impl.urls.ws1.URLTemplateComposerImpl1;
import org.exoplatform.services.wsrp2.consumer.impl.urls.ws2.URLTemplateComposerImpl2;
import org.exoplatform.services.wsrp2.type.StateChange;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 2
 *         févr. 2004 Time: 17:13:04
 */

public class ConsumerEnvironmentImpl implements ConsumerEnvironment {

  private String                consumerAgent        = WSRPConstants.DEFAULT_consumerAgentName;

  private String                userAuthentication;

  private PortletRegistry       portletRegistry;

  private PortletDriverRegistry portletDriverRegistry;

  private ProducerRegistry      producerRegistry;

  private UserRegistry          userRegistry;

  private URLTemplateComposer   urlTemplateComposer1;

  private URLTemplateComposer   urlTemplateComposer2;

  private URLRewriter           urlRewriter;

  private List<String>          supportedLocales     = new ArrayList<String>();

  private List<String>          supportedModes       = new ArrayList<String>();

  private List<String>          windowStates         = new ArrayList<String>();

  private StateChange           stateChange;

  private List<String>          characterEncodingSet = new ArrayList<String>();

  private List<String>          mimeTypes            = new ArrayList<String>();

  public ConsumerEnvironmentImpl(PortletRegistry portletRegistry,
                                 PortletDriverRegistry portletDriverRegistry,
                                 ProducerRegistry producerRegistry,
                                 UserRegistry userRegistry) {
    this.portletRegistry = portletRegistry;
    this.portletDriverRegistry = portletDriverRegistry;
    this.producerRegistry = producerRegistry;
    this.userRegistry = userRegistry;

    this.urlTemplateComposer1 = new URLTemplateComposerImpl1();
    this.urlTemplateComposer2 = new URLTemplateComposerImpl2();

    this.urlRewriter = new URLRewriterImpl(new URLGeneratorImpl());
  }

  public PortletRegistry getPortletRegistry() {
    return portletRegistry;
  }

  public PortletDriverRegistry getPortletDriverRegistry() {
    return portletDriverRegistry;
  }

  public ProducerRegistry getProducerRegistry() {
    return producerRegistry;
  }

  public UserRegistry getUserRegistry() {
    return userRegistry;
  }

  public URLTemplateComposer getTemplateComposer(int version) {
    if (version == 1)
      return urlTemplateComposer1;
    else
      return urlTemplateComposer2;
  }

  public URLRewriter getURLRewriter() {
    return urlRewriter;
  }

  public String getConsumerAgent() {
    return consumerAgent;
  }

  public void setConsumerAgent(String name) {
    this.consumerAgent = name;
  }

  public String getUserAuthentication() {
    return userAuthentication;
  }

  public void setUserAuthentication(String authMethod) {
    this.userAuthentication = authMethod;
  }

  public List<String> getSupportedLocales() {
    return supportedLocales;
  }

  public void setSupportedLocales(List<String> locales) {
    this.supportedLocales = locales;
  }

  public List<String> getSupportedModes() {
    return supportedModes;
  }

  public void setSupportedModes(List<String> modes) {
    this.supportedModes = modes;
  }

  public List<String> getSupportedWindowStates() {
    return windowStates;
  }

  public void setSupportedWindowStates(List<String> states) {
    this.windowStates = states;
  }

  public StateChange getPortletStateChange() {
    return stateChange;
  }

  public void setPortletStateChange(StateChange portletStateChange) {
    this.stateChange = portletStateChange;
  }

  public List<String> getCharacterEncodingSet() {
    return characterEncodingSet;
  }

  public void setCharacterEncodingSet(List<String> charEncoding) {
    this.characterEncodingSet = charEncoding;
  }

  public List<String> getMimeTypes() {
    return mimeTypes;
  }

  public void setMimeTypes(List<String> mimeTypes) {
    this.mimeTypes = mimeTypes;
  }
}
