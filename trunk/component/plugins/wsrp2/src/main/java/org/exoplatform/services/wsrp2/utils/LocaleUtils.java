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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Feb 27, 2009
 */
public class LocaleUtils {

  private static final Log LOG = ExoLogger.getLogger(LocaleUtils.class);

  public static List<String> processLocalesToStrings(List<Locale> locales,
                                                     List<String> supportedLocales) {
    if (locales != null && !locales.isEmpty()) {
      List<String> resultLocales = new ArrayList<String>();
      for (Locale locale : locales) {
        resultLocales.add(locale.toString());
      }
      return resultLocales;
    } else {
      return supportedLocales;
    }
  }

  public static List<Locale> processStringsToLocales(List<String> locales,
                                                     List<Locale> defaultLocales) {
    if (locales != null && !locales.isEmpty()) {
      List<Locale> resultLocales = new ArrayList<Locale>();
      for (String locale : locales) {
        resultLocales.add(new Locale(locale.toString()));
      }
      return resultLocales;
    } else {
      return defaultLocales;
    }
  }

}
