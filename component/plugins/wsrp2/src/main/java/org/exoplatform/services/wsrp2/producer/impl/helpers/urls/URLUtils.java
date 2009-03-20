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
package org.exoplatform.services.wsrp2.producer.impl.helpers.urls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.utils.Modes;
import org.exoplatform.services.wsrp2.utils.WindowStates;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Mar 19, 2009
 */
public class URLUtils {

  private static final Log LOG = ExoLogger.getLogger(URLUtils.class);

  /**
   * Parsing parameters from String to Map<String, String[]>.
   */
  public static Map<String, String[]> parseParams(String toRewriteURL) {

    Map<String, List<String>> paramsTemp = new HashMap<String, List<String>>();
    
    toRewriteURL = toRewriteURL.replaceAll(WSRPConstants.NEXT_PARAM_AMP, WSRPConstants.NEXT_PARAM);
    String[] ampersandSignParsed = toRewriteURL.split(WSRPConstants.NEXT_PARAM);
    for (String keyAndValue : ampersandSignParsed) {
      if (keyAndValue.equalsIgnoreCase(""))
        continue;
      String[] equalsSignParsed = keyAndValue.split("=");
      String key = equalsSignParsed[0];
      String value = equalsSignParsed.length == 2 ? equalsSignParsed[1] : "";
      if (key.equalsIgnoreCase(""))
        continue;
      List<String> values = paramsTemp.get(key);
      if (values == null) {
        values = new ArrayList<String>();
        paramsTemp.put(key, values);
      }
      values.add(value);
    }

    // process List<String> to String[]
    Map<String, String[]> params = new HashMap<String, String[]>();
    Set<String> keySet = paramsTemp.keySet();
    for (String key : keySet) {
      List<String> listKey = paramsTemp.get(key);
      params.put(key, listKey.toArray(new String[listKey.size()]));
    }

    return params;
  }

  /**
   * Rewrite ftom WSRP to PC (jsr168-jsr286) specific URL values.
   * 
   * @param params
   */
  public static void processWSRPToPCParams(Map<String, String[]> params) {

    String value = null;

    // Constants.TYPE_PARAMETER
    value = params.get(Constants.TYPE_PARAMETER)[0];
    if (value.equalsIgnoreCase(WSRPConstants.URL_TYPE_BLOCKINGACTION))
      params.put(Constants.TYPE_PARAMETER, new String[] { PCConstants.ACTION_STRING });

    // Constants.SECURE_PARAMETER
    // nothing to rewrite boolean value

    // Constants.PORTLET_MODE_PARAMETER
    value = params.get(Constants.PORTLET_MODE_PARAMETER)[0];
    params.put(Constants.PORTLET_MODE_PARAMETER, new String[] { Modes.delAllPrefixesWSRP(value) });

    // Constants.WINDOW_STATE_PARAMETER
    value = params.get(Constants.WINDOW_STATE_PARAMETER)[0];
    params.put(Constants.WINDOW_STATE_PARAMETER,
               new String[] { WindowStates.delAllPrefixesWSRP(value) });

  }

  public static String getWSRPType(String pcType) {
    return pcType.equalsIgnoreCase(PCConstants.ACTION_STRING) ? WSRPConstants.URL_TYPE_BLOCKINGACTION
                                                             : pcType;
  }

}
