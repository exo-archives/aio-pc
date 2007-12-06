/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.plugins.pc;

import java.util.Locale;
import java.util.ResourceBundle;

import org.exoplatform.commons.utils.MapResourceBundle;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.database.DBObjectPageList;
import org.exoplatform.services.database.ObjectQuery;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.resources.ExoResourceBundle;
import org.exoplatform.services.resources.LocaleConfigService;
import org.exoplatform.services.resources.Query;
import org.exoplatform.services.resources.ResourceBundleData;
import org.exoplatform.services.resources.ResourceBundleDescription;
import org.exoplatform.services.resources.impl.BaseResourceBundleService;
import org.hibernate.Session;

/**
 * Created by The eXo Platform SAS
 * Author : Roman Pedchenko
 *          roman.pedchenko@exoplatform.com.ua
 * Apr 10, 2007
 */
public class ResourceBundleServiceImpl extends BaseResourceBundleService {

  private HibernateService hService_;

  public ResourceBundleServiceImpl(HibernateService service, LocaleConfigService localeService, CacheService cService,
    InitParams params) throws Exception {

    log_ = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
    cache_ = cService.getCacheInstance(getClass().getName());
    localeService_ = localeService;
    hService_ = service;

    initParams(params);
  }

  public ResourceBundleData getResourceBundleData(String name) throws Exception {
    return (ResourceBundleData) hService_.findOne(ResourceBundleData.class, name);
  }

  public ResourceBundleData removeResourceBundleData(String id) throws Exception {
    ResourceBundleData data =  (ResourceBundleData) hService_.remove(ResourceBundleData.class, id);
    cache_.remove(data.getId());
    return data;
  }

  public PageList findResourceDescriptions(Query q) throws Exception {
    String name = q.getName();
    if (name == null || name.length() == 0)	name = "%";
    ObjectQuery oq = new ObjectQuery(ResourceBundleDescription.class);
    oq.addLIKE("name", name);
    oq.addLIKE("language", q.getLanguage());
    oq.setDescOrderBy("name");
    return new DBObjectPageList(hService_, oq);
  }

  public void saveResourceBundle(ResourceBundleData data) throws Exception {
    hService_.save(data);
    cache_.remove(data.getId());
  }

  protected ResourceBundle getResourceBundleFromDb(String id,	ResourceBundle parent,
      Locale locale) throws Exception {
    Session session = hService_.openSession();
    ResourceBundleData data =
      (ResourceBundleData) session.get(ResourceBundleData.class, id);
    if (data != null) {
      ResourceBundle res = new ExoResourceBundle(data.getData(), parent);
      MapResourceBundle mres = new MapResourceBundle(res, locale) ;
      return mres;
    }
    return null;
  }
}
