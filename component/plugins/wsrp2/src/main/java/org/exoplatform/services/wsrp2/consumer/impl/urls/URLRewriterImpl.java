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

package org.exoplatform.services.wsrp2.consumer.impl.urls;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.consumer.URLGenerator;
import org.exoplatform.services.wsrp2.consumer.URLRewriter;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.utils.URLUtils;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Nov 21, 2008
 */

public class URLRewriterImpl implements URLRewriter {

  private URLGenerator urlGenerator;

  private Log          log;

  public URLRewriterImpl(URLGenerator urlGenerator) {
    this.urlGenerator = urlGenerator;
    this.log = ExoLogger.getLogger("org.exoplatform.services.wsrp2.consumer");
  }

  public String rewriteURLs(String baseURL, String markup) throws WSRPException {
    log.debug("Rewrite URL : " + markup);
    StringBuffer resultMarkup = new StringBuffer();
    int markupIndex = 0;
    int rewriteStartPos = -1;
    int rewriteEndPos = -1;
    while (markupIndex < markup.length()) {
      rewriteStartPos = -1;
      rewriteEndPos = -1;
      rewriteStartPos = markup.indexOf(WSRPConstants.WSRP_REWRITE_PREFIX, markupIndex);
      if (rewriteStartPos == -1)
        break; // no index to start rewrite -> exits the loop
      rewriteEndPos = markup.indexOf(WSRPConstants.WSRP_REWRITE_SUFFFIX, rewriteStartPos
          + WSRPConstants.WSRP_REWRITE_PREFIX.length());
      if (rewriteEndPos == -1)
        break; // no index to stop rewrite -> exits the loop
      rewriteEndPos += WSRPConstants.WSRP_REWRITE_SUFFFIX.length();
      // appends the skipped before a block of markup
      resultMarkup.append(markup.substring(markupIndex, rewriteStartPos));
      String toRewriteURL = markup.substring(rewriteStartPos
          + WSRPConstants.WSRP_REWRITE_PREFIX.length(), rewriteEndPos
          - WSRPConstants.WSRP_REWRITE_SUFFFIX.length());

      String newURL = getRewrittenURL(baseURL, toRewriteURL);
      resultMarkup.append(newURL);
      markupIndex = rewriteEndPos;
    }
    if (markupIndex < markup.length()) {
      resultMarkup.append(markup.substring(markupIndex));
    }
    markup = resultMarkup.toString();

    markup = rewriteBlockingActionParameter(baseURL, markup);

    log.debug("Markup returned : " + markup);
    return markup;
  }

  public String rewriteURLAfterTemplateProcessing(String baseURL, String markup) throws WSRPException {
    log.debug("Rewrite URL : " + markup);
    StringBuffer resultMarkup = new StringBuffer();
    int rewriteStartPos = -1;

    while (markup.indexOf(baseURL) != -1) {
      rewriteStartPos = markup.indexOf(baseURL) + baseURL.length();
      resultMarkup.append(markup.substring(0, rewriteStartPos));
      markup = markup.substring(rewriteStartPos);
      String regex = null;
      String replacement = null;
      // Process PORTLET_MODE_PARAMETER
      regex = WSRPConstants.NEXT_PARAM + Constants.PORTLET_MODE_PARAMETER + "="
          + WSRPConstants.WSRP_PREFIX;
      replacement = WSRPConstants.NEXT_PARAM + Constants.PORTLET_MODE_PARAMETER + "=";
      markup = markup.replaceFirst(regex, replacement);
      // Process WINDOW_STATE_PARAMETER
      regex = WSRPConstants.NEXT_PARAM + Constants.WINDOW_STATE_PARAMETER + "="
          + WSRPConstants.WSRP_PREFIX;
      replacement = WSRPConstants.NEXT_PARAM + Constants.WINDOW_STATE_PARAMETER + "=";
      markup = markup.replaceFirst(regex, replacement);
    }

    resultMarkup.append(markup);
    markup = resultMarkup.toString();

    markup = rewriteBlockingActionParameter(baseURL, markup);

    log.debug("Markup returned : " + markup);
    return markup;
  }
  
  private String rewriteBlockingActionParameter(String baseURL, String markup) {
    String oldBaseURL = baseURL + WSRPConstants.NEXT_PARAM + Constants.TYPE_PARAMETER + "="
        + WSRPConstants.URL_TYPE_BLOCKINGACTION;
    String newBaseURL = baseURL + WSRPConstants.NEXT_PARAM + Constants.TYPE_PARAMETER + "="
        + PCConstants.ACTION_STRING;
    markup = markup.replace(oldBaseURL, newBaseURL);
    return markup;
  }

  private String getRewrittenURL(String baseURL, String toRewriteURL) throws WSRPException {

    Map<String, String[]> params = URLUtils.parseParams(toRewriteURL);

    URLUtils.processWSRPToPCParams(params);

    toRewriteURL = toRewriteURL.substring((WSRPConstants.WSRP_URL_TYPE + "=").length());

    return urlGenerator.getURL(baseURL, params);
  }

}
