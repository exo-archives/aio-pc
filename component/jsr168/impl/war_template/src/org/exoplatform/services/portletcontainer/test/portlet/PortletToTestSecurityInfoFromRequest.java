/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 21 nov. 2003
 */
package org.exoplatform.services.portletcontainer.test.portlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class PortletToTestSecurityInfoFromRequest extends GenericPortlet {
  
  private static String[] arrayOfHeaders = {"header1", "header2", "header3"};

  public void init(PortletConfig portletConfig) throws PortletException {
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
    throws PortletException, IOException {
    String auth = actionRequest.getAuthType();    
      
    if(!(PortletRequest.BASIC_AUTH.equals(auth) || PortletRequest.CLIENT_CERT_AUTH.equals(auth) || 
         PortletRequest.DIGEST_AUTH.equals(auth) || PortletRequest.FORM_AUTH.equals(auth)))
      throw new PortletException("exception in processAction of PortletToTestSecurityInfoFromRequest");
               
    String remoteUserName = actionRequest.getRemoteUser();
    if(!"REMOTE USER FROM MOCK".equals(remoteUserName))
      throw new PortletException("exception in processAction of PortletToTestSecurityInfoFromRequest");
    
    Principal p = actionRequest.getUserPrincipal();
    if(!"PrincipalMackName".equals(p.getName()))
      throw new PortletException("exception in processAction of PortletToTestSecurityInfoFromRequest");
      
    boolean test = actionRequest.isUserInRole("auth-user");          
    if(!test)               
      throw new PortletException("exception in processAction of PortletToTestSecurityInfoFromRequest");

    test = actionRequest.isUserInRole("testRole2");           
    if(test)               
      throw new PortletException("exception in processAction of PortletToTestSecurityInfoFromRequest");
           
    test = actionRequest.isSecure();       
    if(!test)               
      throw new PortletException("exception in processAction of PortletToTestSecurityInfoFromRequest");
                         
    actionResponse.setRenderParameter("status", "Everything is ok");    
  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse)
    throws PortletException, IOException {
    renderResponse.setContentType("text/html");  
    PrintWriter w = renderResponse.getWriter();
    w.println("Everything is ok");
  }

  public void destroy() {
  }
}