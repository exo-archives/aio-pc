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
package org.exoplatform.services.resources;

import java.util.Locale;
import java.util.ResourceBundle;

import org.exoplatform.commons.utils.PageList;

/**
 * May 7, 2004
 * 
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: ResourceBundleService.java 5799 2006-05-28 17:55:42Z geaz $
 *           This class is used to manage the Resoucebunlde. It should work like
 *           the java.util.ResourceBundle class except that the properties file
 *           can be in the database or a a directory. The class
 *           java.util.ResourceBundle require that the properties file has to be
 *           in the classpath.
 **/
public interface ResourceBundleService {
  /**
   * This method should search for a template in the database that the service
   * manage or the resource in the classpath of the current thread class loader
   * 
   * @param name The name of the resource bunlde
   * @param locale the locale
   * @return A ResourceBunlde or null if no ResourceBundle is found
   */
  public ResourceBundle getResourceBundle(String name, Locale locale);

  /**
   * This method should search for a template in the database that the service
   * manage or the resource in the classpath of the specified class loader
   * 
   * @param name the name of the resource
   * @param locale the locale
   * @param cl The classloader
   * @return
   */
  public ResourceBundle getResourceBundle(String name, Locale locale, ClassLoader cl);

  /**
   * This method will call the method public ResourceBundle
   * getResourceBundle(String[] name, Locale locale, ClassLoader cl) and using
   * the the classloader of the current thread
   */
  public ResourceBundle getResourceBundle(String[] name, Locale locale);

  /**
   * This method will look for all the resources with the given names and merge
   * into one resource bundle, the properties in the later resource bundle name
   * will have the higher priority than the previous one.
   */
  public ResourceBundle getResourceBundle(String[] name, Locale locale, ClassLoader cl);

  /**
   * This method should look for a resource bundle in the database that match
   * the given id. The ResourceBundleData store the data as text instead of a
   * properties map.
   * 
   * @param id The id of the resource bundle data
   * @return A ResourceBundleData instance or no record is found.
   * @throws Exception
   */
  public ResourceBundleData getResourceBundleData(String id) throws Exception;

  /**
   * This method remove the data record in the databas and return the data
   * instance after it has been removed
   * 
   * @param id The id of the data record
   * @return A ResourceBundleData instance
   * @throws Exception
   */
  public ResourceBundleData removeResourceBundleData(String id) throws Exception;

  /**
   * This method shoudl create or update a ResourceBundleData instance
   * 
   * @param data the ResourceBundleData instance to update or create
   * @throws Exception
   */
  public void saveResourceBundle(ResourceBundleData data) throws Exception;

  /**
   * This method search and return a page description iterator
   * 
   * @param q The search criteria
   * @return A PageDescription Iterator
   * @throws Exception
   */
  public PageList findResourceDescriptions(Query q) throws Exception;

  /**
   * This method is acted as a factory of
   * 
   * @return
   */
  public ResourceBundleData createResourceBundleDataInstance();

  /**
   * The developer can store the common properties in certain resource bundles
   * so later he can merge a resource bundle with the shared resource bunldes
   * 
   * @return the name of the shared resource bundle
   */
  public String[] getSharedResourceBundleNames();
}
