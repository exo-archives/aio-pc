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

package org.exoplatform.services.wsrp2.consumer.impl.urls1;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.consumer.URLGenerator;
import org.exoplatform.services.wsrp2.consumer.URLRewriter;
import org.exoplatform.services.wsrp2.exceptions.Faults;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;

/*
 * Some part of this class implementation are taken from the WSRP4J project
 *
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 *
 * @author  <a href="mailto:stefan.behl@de.ibm.com">Stefan Behl</a>
 * @author Richard Jacob
 *
 * Date: 6 févr. 2004
 * Time: 15:13:40
 */

public class URLRewriterImpl1 implements URLRewriter {

  private URLGenerator urlGenerator;

  private Log          log;

  public URLRewriterImpl1(URLGenerator urlGenerator) {
    this.urlGenerator = urlGenerator;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp1.consumer");
  }

  public String rewriteURLs(String path, String markup) throws WSRPException {
    log.debug("Rewrite URL : " + markup);
    StringBuffer resultMarkup = new StringBuffer("");
    int markupIndex = 0;
    int rewriteStartPos = -1;
    int rewriteEndPos = -1;
    while (markupIndex < markup.length()) {
      rewriteStartPos = -1;
      rewriteEndPos = -1;
      rewriteStartPos = markup.indexOf(WSRPConstants1.WSRP_REWRITE_PREFIX, markupIndex);
      if (!(rewriteStartPos == -1 || (rewriteStartPos + WSRPConstants1.WSRP_REWRITE_PREFIX.length() - 1) > (markup.length() - 2))) {
        rewriteEndPos = markup.indexOf(WSRPConstants1.WSRP_REWRITE_SUFFFIX, markupIndex);
        if (rewriteEndPos != -1) {
          rewriteEndPos = rewriteEndPos + WSRPConstants1.WSRP_REWRITE_SUFFFIX.length();
        }
      }
      if ((rewriteStartPos != -1) && (rewriteEndPos != -1)) {
        resultMarkup.append(markup.substring(markupIndex, rewriteStartPos));
        String submarkup = markup.substring(rewriteStartPos, rewriteEndPos);
        rewrite(path, resultMarkup, submarkup);
        markupIndex = rewriteEndPos;
      } else {
        resultMarkup.append(markup.substring(markupIndex, markup.length()));
        markupIndex = markup.length();
      }
    }
    log.debug("Markup returned : " + resultMarkup.toString());
    return resultMarkup.toString();
  }

  private void rewrite(String baseURL, StringBuffer markup, String rewriteURL) throws WSRPException {
    Map<String, String> params = createParameterMap(rewriteURL);
    if (rewriteURL.indexOf(WSRPConstants1.URL_TYPE_BLOCKINGACTION) != -1) {
      markup.append(urlGenerator.getBlockingActionURL(baseURL, params));
    } else if (rewriteURL.indexOf(WSRPConstants1.URL_TYPE_RENDER) != -1) {
      markup.append(urlGenerator.getRenderURL(baseURL, params));
    } else if (rewriteURL.indexOf(WSRPConstants1.URL_TYPE_RESOURCE) != -1) {
      markup.append(urlGenerator.getResourceURL(baseURL, params));
    }
  }

  private Map<String, String> createParameterMap(String rewriteURL) throws WSRPException {
    Map<String, String> params = new HashMap<String, String>();
    if (rewriteURL.indexOf(WSRPConstants1.URL_TYPE_BLOCKINGACTION) != -1) {
      params.put(WSRPConstants1.WSRP_URL_TYPE, WSRPConstants1.URL_TYPE_BLOCKINGACTION);
    } else if (rewriteURL.indexOf(WSRPConstants1.URL_TYPE_RENDER) != -1) {
      params.put(WSRPConstants1.WSRP_URL_TYPE, WSRPConstants1.URL_TYPE_RENDER);
    } else if (rewriteURL.indexOf(WSRPConstants1.URL_TYPE_RESOURCE) != -1) {
      params.put(WSRPConstants1.WSRP_URL_TYPE, WSRPConstants1.URL_TYPE_RESOURCE);
    } else {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT);
    }
    int equals = 0;
    int next = 0;
    int end = rewriteURL.indexOf(WSRPConstants1.WSRP_REWRITE_SUFFFIX);
    int index = rewriteURL.indexOf(WSRPConstants1.NEXT_PARAM);
    int lengthNext = 0;
    String subNext = null;
    while (index != -1) {
      subNext = rewriteURL.substring(index, index + WSRPConstants1.NEXT_PARAM.length());
      if (subNext.equals(WSRPConstants1.NEXT_PARAM)) {
        lengthNext = WSRPConstants1.NEXT_PARAM.length();
      } else {
        lengthNext = WSRPConstants1.NEXT_PARAM.length();
      }
      equals = rewriteURL.indexOf("=", index + lengthNext);
      next = rewriteURL.indexOf(WSRPConstants1.NEXT_PARAM, equals);
      if (equals != -1) {
        if (next != -1) {
          params.put(rewriteURL.substring(index + lengthNext, equals),
                     rewriteURL.substring(equals + 1, next));
        } else {
          params.put(rewriteURL.substring(index + lengthNext, equals),
                     rewriteURL.substring(equals + 1, end));
        }
      }
      index = next;
    }
    return params;
  }

}
