/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.consumer.impl;

import org.exoplatform.services.wsrp.consumer.ConsumerEnvironment;
import org.exoplatform.services.wsrp.consumer.PortletDriverRegistry;
import org.exoplatform.services.wsrp.consumer.PortletRegistry;
import org.exoplatform.services.wsrp.consumer.ProducerRegistry;
import org.exoplatform.services.wsrp.consumer.URLRewriter;
import org.exoplatform.services.wsrp.consumer.URLTemplateComposer;
import org.exoplatform.services.wsrp.consumer.UserRegistry;
import org.exoplatform.services.wsrp.type.StateChange;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 2 févr. 2004
 * Time: 17:13:04
 */

public class ConsumerEnvironmentImpl implements ConsumerEnvironment{

  private String consumerAgent = "exoplatform.1.0";
  private String userAuthentication;

  private PortletRegistry portletRegistry;
  private PortletDriverRegistry portletDriverRegistry;
  private ProducerRegistry producerRegistry;
  private UserRegistry userRegistry;
  private URLTemplateComposer urlTemplateComposer;
  private URLRewriter urlRewriter;
  private String[] supportedLocales;
  private String[] supportedModes;
  private String[] windowStates;
  private StateChange stateChange;
  private String[] characterEncodingSet;
  private String[] mimeTypes;

  public ConsumerEnvironmentImpl(PortletRegistry portletRegistry,
                                  PortletDriverRegistry portletDriverRegistry,
                                  ProducerRegistry producerRegistry,
                                  UserRegistry userRegistry,
                                  URLTemplateComposer urlTemplateComposer,
                                  URLRewriter urlRewriter) {
    this.portletRegistry = portletRegistry;
    this.portletDriverRegistry = portletDriverRegistry;
    this.producerRegistry = producerRegistry;
    this.userRegistry = userRegistry;
    this.urlTemplateComposer = urlTemplateComposer;
    this.urlRewriter = urlRewriter;
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

  public URLTemplateComposer getTemplateComposer() {
    return urlTemplateComposer;
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

  public String[] getSupportedLocales() {
    return supportedLocales;
  }

  public void setSupportedLocales(String[] locales) {
    this.supportedLocales = locales;
  }

  public String[] getSupportedModes() {
    return supportedModes;
  }

  public void setSupportedModes(String[] modes) {
    this.supportedModes = modes;
  }

  public String[] getSupportedWindowStates() {
    return windowStates;
  }

  public void setSupportedWindowStates(String[] states) {
    this.windowStates = states;
  }

  public StateChange getPortletStateChange() {
    return stateChange;
  }

  public void setPortletStateChange(StateChange portletStateChange) {
    this.stateChange = portletStateChange;
  }

  public String[] getCharacterEncodingSet() {
    return characterEncodingSet;
  }

  public void setCharacterEncodingSet(String[] charEncoding) {
    this.characterEncodingSet = charEncoding;
  }

  public String[] getMimeTypes() {
    return mimeTypes;
  }

  public void setMimeTypes(String[] mimeTypes) {
    this.mimeTypes = mimeTypes;
  }
}