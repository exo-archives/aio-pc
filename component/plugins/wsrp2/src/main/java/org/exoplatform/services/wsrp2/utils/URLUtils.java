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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.wsrp2.WSRPConstants;

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

    Map<String, List<String>> paramsTemp = new LinkedHashMap<String, List<String>>();

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
    Map<String, String[]> params = new LinkedHashMap<String, String[]>();
    Set<String> keySet = paramsTemp.keySet();
    for (String key : keySet) {
      List<String> listKey = paramsTemp.get(key);
      params.put(key, listKey.toArray(new String[listKey.size()]));
    }

    return params;
  }

  /**
   * Rewrite from WSRP to PC (jsr168-jsr286) specific URL values.
   * 
   * @param params
   */
  public static Map<String, String[]> rewriteWSRPToPCParams(Map<String, String[]> params) {

    Map<String, String[]> paramsResult = new LinkedHashMap<String, String[]>();

    Set<String> keySet = params.keySet();
    for (String key : keySet) {
      String value = null;

      if (key.equalsIgnoreCase(WSRPConstants.WSRP_URL_TYPE)) {
        // Constants.TYPE_PARAMETER
        value = params.get(WSRPConstants.WSRP_URL_TYPE)[0];
        if (value.equalsIgnoreCase(WSRPConstants.URL_TYPE_BLOCKINGACTION)) {
          paramsResult.put(Constants.TYPE_PARAMETER, new String[] { PCConstants.ACTION_STRING });
        } else {
          paramsResult.put(Constants.TYPE_PARAMETER, new String[] { value });
        }
      } else

      if (key.equalsIgnoreCase(WSRPConstants.WSRP_SECURE_URL)) {
        // Constants.SECURE_PARAMETER
        value = params.get(WSRPConstants.WSRP_SECURE_URL)[0];
        paramsResult.put(Constants.SECURE_PARAMETER, new String[] { value });
      } else

      if (key.equalsIgnoreCase(WSRPConstants.WSRP_MODE)) {
        // Constants.PORTLET_MODE_PARAMETER
        value = params.get(WSRPConstants.WSRP_MODE)[0];
        paramsResult.put(Constants.PORTLET_MODE_PARAMETER,
                         new String[] { Modes.delAllPrefixesWSRP(value) });
      } else

      if (key.equalsIgnoreCase(WSRPConstants.WSRP_WINDOW_STATE)) {
        // Constants.WINDOW_STATE_PARAMETER
        value = params.get(WSRPConstants.WSRP_WINDOW_STATE)[0];
        paramsResult.put(Constants.WINDOW_STATE_PARAMETER,
                         new String[] { WindowStates.delAllPrefixesWSRP(value) });
      } else {
        paramsResult.put(key, params.get(key));
      }

    } // END of 'for keySet

    return paramsResult;

  }

  public static String getWSRPType(String pcType) {
    return pcType.equalsIgnoreCase(PCConstants.ACTION_STRING) ? WSRPConstants.URL_TYPE_BLOCKINGACTION
                                                             : pcType;
  }

}
