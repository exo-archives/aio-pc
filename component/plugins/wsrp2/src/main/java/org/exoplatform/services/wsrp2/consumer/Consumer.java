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
 * The consumer provides access to consumer specific components.
 * 
 * @author <a href='mailto:peter.fischer@de.ibm.com'>Peter Fischer</a>
 * @author Benjamin Mestrallet
 */
public interface Consumer {

  /**
   * Get the portlet registry of the consumer.
   * 
   * @return Interface to the consumer specific portlet registry
   */
  public PortletRegistry getPortletRegistry();

  /**
   * Get the portlet driver registry of the consumer.
   * 
   * @return Interface to the consumer specific portlet driver registry
   */
  public PortletDriverRegistry getPortletDriverRegistry();

  /**
   * Get the producer registry of the consumer.
   * 
   * @return The consumer specific producer registry
   */
  public ProducerRegistry getProducerRegistry();

  /**
   * Get the user registry of the consumer.
   * 
   * @return The consumer specific user registry
   */
  public UserRegistry getUserRegistry();

  /**
   * Get the url template composer for template proccessing
   * 
   * @return Interface to the consumer specific template composer
   */
  public URLTemplateComposer getTemplateComposer(int version);

  /**
   * Get the url rewriter for consumer url-rewriting
   * 
   * @return The consumer specific url rewriter
   */
  public URLRewriter getURLRewriter(int version);

}
