/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.consumer.impl;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.consumer.URLGenerator;
import org.exoplatform.services.wsrp.consumer.URLRewriter;
import org.exoplatform.services.wsrp.exceptions.Faults;
import org.exoplatform.services.wsrp.exceptions.WSRPException;

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

public class URLRewriterImpl implements URLRewriter {

  private URLGenerator urlGenerator;
  private Log log;

  public URLRewriterImpl(URLGenerator urlGenerator, LogService logService) {
    this.urlGenerator = urlGenerator;
    this.log = logService.getLog("org.exoplatform.services.wsrp.consumer");
  }

  public String rewriteURLs(String path, String markup) throws WSRPException {
    log.debug("Rewrite URL : " + markup);
    StringBuffer resultMarkup = new StringBuffer("");
    int markupIndex = 0;
    int rewriteStartPos = -1;
    int rewriteEndPos = -1;
    int currentPos = 0;
    while (markupIndex < markup.length()) {
      rewriteStartPos = -1;
      rewriteEndPos = -1;
      rewriteStartPos = markup.indexOf(WSRPConstants.WSRP_REWITE_PREFIX, markupIndex);
      if (!(rewriteStartPos == -1 ||
          (rewriteStartPos + WSRPConstants.WSRP_REWITE_PREFIX.length() - 1) > (markup.length() - 2))) {
        rewriteEndPos = markup.indexOf(WSRPConstants.WSRP_REWITE_SUFFFIX, markupIndex);
        if (rewriteEndPos != -1) {
          currentPos = rewriteStartPos;
          while ((currentPos != -1) && (currentPos < rewriteEndPos)) {
            rewriteStartPos = currentPos;
            currentPos = markup.indexOf(WSRPConstants.WSRP_REWITE_PREFIX,
                rewriteStartPos + WSRPConstants.WSRP_REWITE_PREFIX.length());
          }
          rewriteEndPos = rewriteEndPos + WSRPConstants.WSRP_REWITE_SUFFFIX.length() - 1;
        }
      }
      if ((rewriteStartPos != -1) && (rewriteEndPos != -1)) {
        resultMarkup.append(markup.substring(markupIndex, rewriteStartPos));
        rewrite(path, resultMarkup, markup.substring(rewriteStartPos, rewriteEndPos + 1));
        markupIndex = rewriteEndPos + 1;
      } else {
        resultMarkup.append(markup.substring(markupIndex, markup.length()));
        markupIndex = markup.length();
      }
    }
    log.debug("Markup returned : " + resultMarkup.toString());
    return resultMarkup.toString();
  }

  private void rewrite(String baseURL, StringBuffer markup, String rewriteURL) throws WSRPException {
    Map params = createParameterMap(rewriteURL);
    if (rewriteURL.indexOf(WSRPConstants.URL_TYPE_BLOCKINGACTION) != -1) {
      markup.append(urlGenerator.getBlockingActionURL(baseURL, params));
    } else if (rewriteURL.indexOf(WSRPConstants.URL_TYPE_RENDER) != -1) {
      markup.append(urlGenerator.getRenderURL(baseURL, params));
    } else if (rewriteURL.indexOf(WSRPConstants.URL_TYPE_RESOURCE) != -1) {
      markup.append(urlGenerator.getResourceURL(baseURL,params));
    }
  }

  private Map createParameterMap(String rewriteURL) throws WSRPException {
    Map params = (Map) new HashMap();
    if (rewriteURL.indexOf(WSRPConstants.URL_TYPE_BLOCKINGACTION) != -1) {
      params.put(WSRPConstants.WSRP_URL_TYPE, WSRPConstants.URL_TYPE_BLOCKINGACTION);
    } else if (rewriteURL.indexOf(WSRPConstants.URL_TYPE_RENDER) != -1) {
      params.put(WSRPConstants.WSRP_URL_TYPE, WSRPConstants.URL_TYPE_RENDER);
    } else if (rewriteURL.indexOf(WSRPConstants.URL_TYPE_RESOURCE) != -1) {
      params.put(WSRPConstants.WSRP_URL_TYPE, WSRPConstants.URL_TYPE_RESOURCE);
    } else {
      throw new WSRPException(Faults.OPERATION_FAILED_FAULT);
    }
    int equals = 0;
    int next = 0;
    int end = rewriteURL.indexOf(WSRPConstants.WSRP_REWITE_SUFFFIX);
    int index = rewriteURL.indexOf(WSRPConstants.NEXT_PARAM);
    int lengthNext = 0;
    String subNext = null;
    while (index != -1) {
      subNext = rewriteURL.substring(index, index + WSRPConstants.NEXT_PARAM_AMP.length());
      if (subNext.equals(WSRPConstants.NEXT_PARAM_AMP)) {
        lengthNext = WSRPConstants.NEXT_PARAM_AMP.length();
      } else {
        lengthNext = WSRPConstants.NEXT_PARAM.length();
      }
      equals = rewriteURL.indexOf("=", index + lengthNext);
      next = rewriteURL.indexOf(WSRPConstants.NEXT_PARAM, equals);
      if (equals != -1) {
        if (next != -1) {
          params.put(rewriteURL.substring(index + lengthNext, equals), rewriteURL.substring(equals + 1, next));
        } else {
          params.put(rewriteURL.substring(index + lengthNext, equals), rewriteURL.substring(equals + 1, end));
        }
      }
      index = next;
    }
    return params;
  }


}