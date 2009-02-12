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

package org.exoplatform.services.wsrp2.consumer.impl.urls2;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.consumer.URLGenerator;
import org.exoplatform.services.wsrp2.consumer.URLRewriter;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 *
 * @author  <a href="mailto:stefan.behl@de.ibm.com">Stefan Behl</a>
 * @author Richard Jacob
 *
 * Date: 6 févr. 2004
 * Time: 15:13:40
 */

public class URLRewriterImpl2 implements URLRewriter {

  private URLGenerator urlGenerator;

  private Log          log;

  public URLRewriterImpl2(URLGenerator urlGenerator) {
    this.urlGenerator = urlGenerator;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp2.consumer");
  }

  public String rewriteURLs(String baseURL, String markup) throws WSRPException {
    log.debug("Rewrite URL : " + markup);
    StringBuffer resultMarkup = new StringBuffer();
    int processIndex = 0;
    int rewriteStartPos = -1;
    int rewriteEndPos = -1;
    while (processIndex < markup.length()) {
      rewriteStartPos = -1;
      rewriteEndPos = -1;
      rewriteStartPos = markup.indexOf(WSRPConstants.WSRP_REWRITE_PREFIX, processIndex);
      if (rewriteStartPos == -1)
        break; // no index to start rewrite -> exits the loop
      rewriteEndPos = markup.indexOf(WSRPConstants.WSRP_REWRITE_SUFFFIX, rewriteStartPos
          + WSRPConstants.WSRP_REWRITE_PREFIX.length());
      if (rewriteEndPos == -1)
        break; // no index to stop rewrite -> exits the loop
      rewriteEndPos += WSRPConstants.WSRP_REWRITE_SUFFFIX.length();
      resultMarkup.append(markup.substring(processIndex, rewriteStartPos));
      String toRewriteURL = markup.substring(rewriteStartPos
          + WSRPConstants.WSRP_REWRITE_PREFIX.length(), rewriteEndPos
          - WSRPConstants.WSRP_REWRITE_SUFFFIX.length());

      resultMarkup.append(getRewrittenURL(baseURL, toRewriteURL));
      processIndex = rewriteEndPos;
    }
    resultMarkup.append(markup.substring(processIndex));
    log.debug("Markup returned : " + resultMarkup.toString());
    return resultMarkup.toString();
  }

  private String getRewrittenURL(String baseURL, String toRewriteURL) throws WSRPException {
    if (!(toRewriteURL.startsWith(WSRPConstants.WSRP_URL_TYPE + "="))) {
      return toRewriteURL;
    }
    toRewriteURL = toRewriteURL.substring((WSRPConstants.WSRP_URL_TYPE + "=").length());
    Map<String, String> params = new TreeMap<String, String>();
    if (toRewriteURL.startsWith(WSRPConstants.URL_TYPE_BLOCKINGACTION)) {
      // action
      params.put(Constants.TYPE_PARAMETER, PCConstants.ACTION_STRING);
      toRewriteURL = toRewriteURL.substring(WSRPConstants.URL_TYPE_BLOCKINGACTION.length() + 1);
      fillParameterMap(params, toRewriteURL);
      return urlGenerator.getBlockingActionURL(baseURL, params);
    } else if (toRewriteURL.startsWith(WSRPConstants.URL_TYPE_RENDER)) {
      // render
      params.put(Constants.TYPE_PARAMETER, PCConstants.RENDER_STRING);
      toRewriteURL = toRewriteURL.substring(WSRPConstants.URL_TYPE_RENDER.length() + 1);
      fillParameterMap(params, toRewriteURL);
      return urlGenerator.getRenderURL(baseURL, params);
    } else if (toRewriteURL.startsWith(WSRPConstants.URL_TYPE_RESOURCE)) {
      // resource
      params.put(Constants.TYPE_PARAMETER, PCConstants.RESOURCE_STRING);
      toRewriteURL = toRewriteURL.substring(WSRPConstants.URL_TYPE_RESOURCE.length() + 1);
      fillParameterMap(params, toRewriteURL);
      return urlGenerator.getResourceURL(baseURL, params);
    } else {
      // extension
      params.put(Constants.TYPE_PARAMETER,
                 toRewriteURL.substring(0, toRewriteURL.indexOf(WSRPConstants.NEXT_PARAM) - 1));
      toRewriteURL = toRewriteURL.substring(toRewriteURL.indexOf(WSRPConstants.NEXT_PARAM));
      fillParameterMap(params, toRewriteURL);
      return urlGenerator.getExtensionURL(baseURL, params);
    }
  }

  private void fillParameterMap(Map<String, String> params, String toRewriteURL) throws WSRPException {
    String[] parameterPairs = toRewriteURL.split(WSRPConstants.NEXT_PARAM);
    for (String string : parameterPairs) {
      String[] nameAndValue = string.split("=");
      if (nameAndValue[0].startsWith(WSRPConstants.NEXT_PARAM.substring(1)))
        nameAndValue[0] = nameAndValue[0].substring(WSRPConstants.NEXT_PARAM.substring(1).length());
      if (nameAndValue.length == 1)
        params.put(nameAndValue[0], "");
      else
        params.put(nameAndValue[0], nameAndValue[1]);
    }
  }
}
