/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.test.portlet;

import javax.portlet.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class PortletToTestCacheability extends GenericPortlet {
  
      protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
      }

      public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
        throws PortletException, IOException {
      }
      
      public void render(RenderRequest renderRequest, RenderResponse renderResponse)
        throws PortletException, IOException {
      super.render(renderRequest, renderResponse);
      }
      
      public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
        throws PortletException, IOException {

      resourceResponse.setContentType("text/html; charset=UTF-8");
      PrintWriter w = resourceResponse.getWriter();
        
      if (resourceRequest.getParameter("test").equals("1"))
      {
        ResourceURL rURL = resourceResponse.createResourceURL();
        rURL.setCacheability(ResourceURL.FULL);
        w.print(rURL);  
      } 
      
      else if (resourceRequest.getParameter("test").equals("2")) {
          ResourceURL rURL2 = resourceResponse.createResourceURL();
          try {
            rURL2.setCacheability(ResourceURL.PAGE);
          } catch (IllegalStateException e) {
            w.println("OK");
          }
      }  
      
      else if (resourceRequest.getParameter("test").equals("3")) {
        ResourceURL rURL3 = resourceResponse.createResourceURL();
         w.print(rURL3);
      }
      
      
//      else if (resourceRequest.getParameter("test").equals("6")) {
//          ResourceURL rURL = resourceResponse.createResourceURL();
//          rURL.setCacheability(ResourceURL.FULL);
//      }
      
      else if (resourceRequest.getParameter("test").equals("7")) {
        try {
        PortletURL rURL = resourceResponse.createRenderURL();
        } catch (IllegalStateException e) {
          w.print("OK");
        }
        
    } 


      
    }
   }
