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

package org.exoplatform.services.wsrp2.consumer;

/**
 * This interface provides methods to generate URL templates.
 * The generated templates will be transmitted to Producers (or respectively
 * portlets) that are willing to properly write URLs for a Consumer. (With
 * templates the Consumer indicates how it needs URLs formatted in order to
 * process them properly.)
 * 
 * @author <a href="mailto:stefan.behl@de.ibm.com">Stefan Behl</a>
 * @author Benjamin Mestrallet
 */
public interface URLTemplateComposer {

//  public void setHost(String host);
//
//  public void setPort(int port);

  /**
   * Creates a blocking action template. Includes tokens for url-type,
   * portletMode, navigationalState, interactionState and windowState to be
   * replaced by the producer.
   * 
   * @return String representing the entire template.
   * @param path
   */
  public String createBlockingActionTemplate(String path);

  /**
   * Creates a secure blocking action template. Includes tokens for url-type,
   * portletMode, navigationalState, interactionState, windowState and secureURL
   * to be replaced by the producer.
   * 
   * @return String representing the entire template.
   */
  public String createSecureBlockingActionTemplate(String path);

  /**
   * Creates a render template. Includes tokens for url-type, portletMode,
   * navigationalState, interactionState and windowState to be replaced by the
   * producer.
   * 
   * @return String representing the entire template.
   */
  public String createRenderTemplate(String path);

  /**
   * Creates a secure render template. Includes tokens for url-type,
   * portletMode, navigationalState, interactionState, windowState and secureURL
   * to be replaced by the producer.
   * 
   * @return String representing the entire template.
   */
  public String createSecureRenderTemplate(String path);

  /**
   * Creates a resource template. Includes tokens for url-type, rewriteResource
   * and url to be replaced by the producer.
   * 
   * @return String representing the entire template.
   */
  public String createResourceTemplate(String path);

  /**
   * Creates a secure resource template. Includes tokens for url-type, url,
   * rewriteResource, and secureURL to be replaced by the producer.
   * 
   * @return String representing the entire template.
   */
  public String createSecureResourceTemplate(String path);

  /**
   * Creates a default template. Includes tokens for url-type, portletMode,
   * navigationalState, interactionState, windowState, url, rewriteResource and
   * secureURL to be replaced by the producer.
   * 
   * @return String representing the entire template.
   */
  public String createDefaultTemplate(String path);

  /**
   * Creates a secure default template. Includes tokens for url-type,
   * portletMode, navigationalState, interactionState, windowState, url,
   * rewriteResource and secureURL to be replaced by the producer.
   * 
   * @return String representing the entire template.
   */
  public String createSecureDefaultTemplate(String path);

  /**
   * Get the consumers namespace prefix which is used by the portlet to
   * namespace tokens which need to be unique on a aggregated page.
   * 
   * @return The namespace prefix of the consumer.
   */
  public String getNamespacePrefix();
}
