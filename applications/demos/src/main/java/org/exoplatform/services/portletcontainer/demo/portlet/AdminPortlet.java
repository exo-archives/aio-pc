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
package org.exoplatform.services.portletcontainer.demo.portlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Oct 10, 2008
 */
public class AdminPortlet extends GenericPortlet {
  private Log                 LOGGER = ExoLogger.getLogger(AdminPortlet.class);

//  private String              style_buffer = "";

//  private String              logic_id;

//  private Properties          props        = new Properties();

  private Map<String, Object> files  = new HashMap<String, Object>();

  @Override
  public void render(RenderRequest request, RenderResponse response) throws PortletException,
                                                                    java.io.IOException {

//    LOGGER.info("Render portlet, logic : " + logic_id);
//
//    if (StringUtils.isBlank(logic_id)) {
//      Properties props = new Properties();
//      props.setProperty("operation_id", "root");
//    }

    super.render(request, response);

  }

  @Override
  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException,
                                                                                   IOException {
    renderResponse.setContentType("text/html; charset = UTF-8");

    String templatePath = "/web/jsp/view.jsp";

    PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(templatePath);

    dispatcher.include(renderRequest, renderResponse);

  }

  @Override
  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException,
                                                                                       java.io.IOException {
    LOGGER.info("!!! === >>> Process in action");

    cleanDataBinder();

    parseRequest(actionRequest);

  }

  private void cleanDataBinder() {
//    props.clear();
    files.clear();
  }

  private void parseRequest(ActionRequest actionRequest) {
    LOGGER.info("!!! === >>> Parsing request");
//    String logic_id = "";
    boolean isMultipart = PortletFileUpload.isMultipartContent(actionRequest);
    if (isMultipart) {

      FileItemFactory factory = new DiskFileItemFactory();
      PortletFileUpload upload = new PortletFileUpload(factory);

      try {
        for (FileItemIterator iterator = upload.getItemIterator(actionRequest); iterator.hasNext();) {
          FileItemStream item = iterator.next();
          InputStream stream = item.openStream();
          String name = item.getFieldName();
          String filename = item.getName();

//          if (item.isFormField()) { // form field
//            String value = Streams.asString(stream);
//            props.setProperty(name, value);
//            LOGGER.info("Parameters from form: " + name + " : " + value);
//            if (name.equals("id"))
//              logic_id = value;
//          }
          if (!item.isFormField()) { // uploaded file

            LOGGER.info("Parameters form STREAM: " + name);
            LOGGER.info("Filename === >>> " + filename);

//            if (FilenameUtils.getExtension(filename).toLowerCase().equals("jar")) {
//              LOGGER.info("Set property in hash");
//              props.setProperty("filename", filename);
//            }

            String realPath = getPortletContext().getRealPath("/");
            realPath = realPath.substring(0, realPath.lastIndexOf("/"));
            realPath = realPath.substring(0, realPath.lastIndexOf("/"));
            File fileDir = new File(realPath);
            String newPathName = fileDir.getCanonicalFile() + String.valueOf(File.separatorChar)
                + filename;
            File temp = new File(newPathName);
            LOGGER.info("File name = " + temp);
            FileOutputStream fout = new FileOutputStream(temp);
            IOUtils.copy(stream, fout);
            fout.flush();
            fout.close();
            stream.close();

            files.put(name, new FileInputStream(temp));
          }
        }
      } catch (FileUploadException e) {
        LOGGER.error("FileUpload exception", e);
      } catch (IOException e) {
        LOGGER.error("Common IO exception", e);
      }
    } else { //non multipart form
      LOGGER.info("Non multipart form");
//      Enumeration<String> param_name = actionRequest.getParameterNames();
//      while (param_name.hasMoreElements()) {
//        String param = param_name.nextElement();
//        String value = actionRequest.getParameter(param);
//        props.setProperty(param, value);
//        if (param.equals("id"))
//          logic_id = value;
//      }
    }

//    return logic_id;

  }

}
