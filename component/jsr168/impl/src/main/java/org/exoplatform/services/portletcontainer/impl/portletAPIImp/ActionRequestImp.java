/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 1:38:46 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp;


import javax.portlet.ActionRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.impl.PortletApplicationHandler;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class ActionRequestImp extends PortletRequestImp
    implements ActionRequest {

  private Vector paramNames;
  private Map filteredMap;
  private boolean isCallGetReader;

  private String enc;
  private boolean encodingModified;
  private Map decodedParameters;
  private Log log_;

  public ActionRequestImp(ExoContainer cont, HttpServletRequest httpServletRequest) {
    super(cont, httpServletRequest);
    this.enc = httpServletRequest.getCharacterEncoding();
    log_ = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");        
  }

  public void init() {
    isCallGetReader = false;
    decodedParameters = new HashMap();
    enc = super.getCharacterEncoding();
  }

  public void emptyActionRequest() {
    this.paramNames = null;
    this.filteredMap = null;
    this.decodedParameters = null;
  }

  public String getParameter(String param) {
    if (param == null || param.startsWith(Constants.PARAMETER_ENCODER))
      return null;
    if (decodedParameters.containsKey(param)){      
      if (decodedParameters.get(param) instanceof String) {
        return (String) decodedParameters.get(param);
      } else {
        Object paramValue = decodedParameters.get(param);
        if(paramValue == null)
          return null;
        return ((String[]) paramValue)[0];
      }
    }
    
    Object obj = super.getParameter(param);    
    if (obj instanceof String[]) {
      String[] tmp = decode((String[]) obj);
      decodedParameters.put(param, tmp);
      return tmp[0];
    } else {
      String tmp = decode((String) obj);      
      decodedParameters.put(param, tmp);
      return tmp;
    }
  }

  public Enumeration getParameterNames() {
    if (paramNames == null) {
      Enumeration e = super.getParameterNames();
      paramNames = new Vector();
      while (e.hasMoreElements()) {
        String key = (String) e.nextElement();
        if (!key.startsWith(Constants.PARAMETER_ENCODER))
          paramNames.add(key);
      }
    }
    return paramNames.elements();
  }

  public String[] getParameterValues(String s) {
    if (s == null || s.startsWith(Constants.PARAMETER_ENCODER))
      return null;
    if (decodedParameters.containsKey(s)) {
      if (decodedParameters.get(s) instanceof String[]) {
        return (String[]) decodedParameters.get(s);
      } else {
        String[] array = { (String) decodedParameters.get(s) };
        return array;
      }
    }
    String[] tmp = decode(super.getParameterValues(s));
    decodedParameters.put(s, tmp);
    return tmp;
  }

  public Map getParameterMap() {
    if (filteredMap == null) {
      filteredMap = new HashMap();
      Map requestMap = super.getParameterMap();
      Set keySet = requestMap.keySet();
      Iterator names = keySet.iterator();
      while (names.hasNext()) {
        String name = (String) names.next();
        String[] obj = (String[]) requestMap.get(name);
        if (!name.startsWith(Constants.PARAMETER_ENCODER)) {
          filteredMap.put(name, decode(obj));
        }
      }
    }
    return Collections.unmodifiableMap(filteredMap);
  }

  private String[] decode(String[] tmp) {
    if(tmp == null)
      return null;
    if(!encodingModified)
      return tmp;
    for (int i = 0; i < tmp.length; i++) {
      try {
        tmp[i] = new String(tmp[i].getBytes(), enc);
      } catch (UnsupportedEncodingException e) {
        log_.error(" -- ActionRequestImp.decode[]: " + e);
      }
    }
    return tmp;
  }

  private String decode(String tmp) {
    if(tmp == null)
      return null;
    if(!encodingModified)
      return tmp;
    try {
      tmp = new String(tmp.getBytes(), enc);
    } catch (UnsupportedEncodingException e) {
      log_.error(" -- ActionRequestImp.decode[]: " + e);
    }
    return tmp;

  }

  public java.io.BufferedReader getReader()
      throws java.io.UnsupportedEncodingException, java.io.IOException {
    String contentType = getHeader("content-type");
    if ("application/x-www-form-urlencoded".equals(contentType)) {
      throw new IllegalStateException("content type cannot be application/x-www-form-urlencoded");
    }
    this.isCallGetReader = true;
    return super.getReader();
  }

  public void setCharacterEncoding(String enc) throws java.io.UnsupportedEncodingException {
    if (this.isCallGetReader) {
      throw new IllegalStateException("This method cannot be called, when getReader() method has been called");
    }
    super.setCharacterEncoding(enc);
    this.enc = enc;
    this.encodingModified = true;
  }

  public InputStream getPortletInputStream() throws java.io.IOException {
    if (this.isCallGetReader) {
      throw new IllegalStateException("This method cannot be called, when getReader() method has been called");
    }
    String contentType = getHeader("content-type");
    if ("application/x-www-form-urlencoded".equals(contentType)) {
      throw new IllegalStateException("content type cannot be application/x-www-form-urlencoded");
    }
    return super.getInputStream();
  }

}
