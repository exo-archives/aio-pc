package org.exoplatform.services.wsrp.consumer;

import org.exoplatform.services.wsrp.exceptions.WSRPException;

/**
 * <p>
 * This interface provides a method performing Consumer URLRewriting.
 * </p>
 * 
 * @author <a href="mailto:stefan.behl@de.ibm.com">Stefan Behl</a>
 * @author Benjamin Mestrallet
 */
public interface URLRewriter {

  /**
   * Parses the entire markup and rewrites all found URLs. The URLs to be
   * rewritten are enclosed by the tokens "wsrp-rewrite" and "/wsrp-rewrite".
   */
  public String rewriteURLs(String baseURL, String markup) throws WSRPException;

}
