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

import java.util.Map;

/**
 * This interface provides methods to query the consumer's urls. These methods
 * could be used to implement consumer url rewriting.
 * 
 * @author <a href="mailto:stefan.behl@de.ibm.com">Stefan Behl</a>
 * @author Benjamin Mestrallet
 */
public interface URLGenerator {

  /**
   * Creates a URL pointing to the consumer,triggering a
   * performBlockingInteraction call.
   */
  public String getBlockingActionURL(String baseURL, Map<String, String> params);

  /**
   * Creates a URL pointing to the consumer,triggering a getMarkup call.
   */
  public String getRenderURL(String baseURL, Map<String, String> params);

  /**
   * Creates a URL pointing to the consumer,triggering the consumer to fetch a
   * certain resource.
   */
  public String getResourceURL(String baseURL, Map<String, String> params);

  /**
   * Creates a 'url' that the consumer can use to namespace tokens.
   */
  public String getNamespacedToken(String token);

  public String getExtensionURL(String baseURL, Map<String, String> params);

}
