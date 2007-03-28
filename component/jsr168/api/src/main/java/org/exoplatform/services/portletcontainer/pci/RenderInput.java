/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/


package org.exoplatform.services.portletcontainer.pci;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 30, 2003
 * Time: 9:08:20 PM
 */
public class RenderInput extends Input {

  private Map renderParameters;
	private String title;
  private boolean updateCache;

  public Map getRenderParameters() {
    return renderParameters;
  }

  public void setRenderParameters(Map renderParameters) {
    this.renderParameters = renderParameters;
  }

  private static String _decode(String s) {
    try {
      return URLDecoder.decode(s, "utf-8");
    } catch (java.io.UnsupportedEncodingException e) {
      return s;
    }
  }

  private static String[] _decode(String[] ss) {
    ArrayList l = new ArrayList();
    for (String s : ss) {
      l.add(_decode(s));
    }
    return (String[]) l.toArray();
  }

  public void setRenderParameters(Map renderParameters, boolean decode) {
    if (!decode)
      setRenderParameters(renderParameters);
    HashMap temp = new HashMap();
    Iterator keys = renderParameters.keySet().iterator();
    while (keys.hasNext()) {
      Object k = keys.next();
      Object v = renderParameters.get(k);
      if (v instanceof String)
        temp.put(k, _decode((String) v));
      else
        temp.put(k, _decode((String[]) v));
    }
    this.renderParameters = temp;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isUpdateCache() {
    return updateCache;
  }

  public void setUpdateCache(boolean updateCache) {
    this.updateCache = updateCache;
  }

}
