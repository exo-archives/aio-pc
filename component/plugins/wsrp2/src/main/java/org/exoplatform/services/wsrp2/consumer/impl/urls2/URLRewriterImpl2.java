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

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.consumer.URLGenerator;
import org.exoplatform.services.wsrp2.consumer.URLRewriter;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.impl.helpers.urls.URLUtils;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * @author <a href="mailto:stefan.behl@de.ibm.com">Stefan Behl</a>
 * @author Richard Jacob Date: 6 févr. 2004 Time: 15:13:40
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

      resultMarkup.append(getRewrittenURL(baseURL, toRewriteURL));
      markupIndex = rewriteEndPos;
    }
    resultMarkup.append(markup.substring(markupIndex));
    log.debug("Markup returned : " + resultMarkup.toString());
    return resultMarkup.toString();
  }

  private String getRewrittenURL(String baseURL, String toRewriteURL) throws WSRPException {

    Map<String, String[]> params = URLUtils.parseParams(toRewriteURL);

    URLUtils.processWSRPToPCParams(params);

    toRewriteURL = toRewriteURL.substring((WSRPConstants.WSRP_URL_TYPE + "=").length());

    return urlGenerator.getURL(baseURL, params);
  }

}
