/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 5:50:44 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp;


import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;
import org.exoplatform.Constants;
import org.exoplatform.container.ExoContainer;
import java.util.*;

public class RenderRequestImp extends PortletRequestImp
    implements RenderRequest {

  private boolean isRenderRequest;
  private Map renderParameters;
	private Vector paramNames;
	private Map filteredMap;

  public RenderRequestImp(ExoContainer cont, HttpServletRequest httpServletRequest) {
    super(cont, httpServletRequest);
  }

  public void fillRenderRequest(Map renderParameters, boolean isRenderRequest) {
    if(renderParameters == null)
      this.renderParameters = new HashMap();
    else   
      this.renderParameters = renderParameters;
    this.isRenderRequest = isRenderRequest;
  }

  public void emptyRenderRequest() {    
		this.paramNames = null;
		this.filteredMap = null;
  }

  public String getParameter(String param) {
		if(param == null || param.startsWith(Constants.PARAMETER_ENCODER))
      throw new IllegalArgumentException("parameter must not be null");
    Object obj = renderParameters.get(param);
    if (obj instanceof String[]) {
      String[] tmp = (String[]) obj ;
      return tmp[0] ;
    }
    return (String) obj ;
  }

  public Enumeration getParameterNames() {
		if(paramNames == null){
			Set set = renderParameters.keySet();
			paramNames = new Vector();
    	for (Iterator iterator = set.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
      	if(!key.startsWith(Constants.PARAMETER_ENCODER))
					paramNames.add(key);
			}
		}
    return paramNames.elements();
  }

  public String[] getParameterValues(String s) {
		if(s == null || s.startsWith(Constants.PARAMETER_ENCODER))
			throw new IllegalArgumentException("parameter must not be null");
		Object o = renderParameters.get(s);
		if (o == null)
		  return null;
		if (o instanceof String) {
		  String[] a = {(String)o};
      return a;
    }
    return (String[]) o;
  }

  public Map getParameterMap() {
		if(filteredMap == null){
			Set set = renderParameters.keySet();
			filteredMap = new HashMap();
    	for (Iterator iterator = set.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				Object values = renderParameters.get(key);
				if (values instanceof String) {
				  String[] a = {(String) values};
				  values = a;
				}
      	if(!key.startsWith(Constants.PARAMETER_ENCODER))
					filteredMap.put(key, values);
			}
		}
    return Collections.unmodifiableMap(filteredMap);
  }

  public boolean isRenderRequest() {
    return isRenderRequest;
  }

}
