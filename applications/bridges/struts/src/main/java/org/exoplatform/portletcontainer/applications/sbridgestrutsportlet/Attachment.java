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
package org.exoplatform.portletcontainer.applications.sbridgestrutsportlet;

import org.apache.commons.logging.Log;
import org.apache.struts.upload.FormFile;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS .
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey Zavizionov</a>
 * @version $Id: $
 *
 * Sep 30, 2008  
 */
public class Attachment {
  private Log log = ExoLogger.getLogger(getClass().getName());
  


  private int attachmentId;
  private String fileName;
  private String contentType;
  private byte[] fileData;

  /**
   * @return Returns the attachmentId.
   */
  public int getAttachmentId() {
    return attachmentId;
  }

  /**
   * @param attachmentId The attachmentId to set.
   */
  public void setAttachmentId(int attachmentId) {
    this.attachmentId = attachmentId;
  }

  /**
   * @return Returns the contentType.
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @param contentType The contentType to set.
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * @return Returns the fileData.
   */
  public byte[] getFileData() {
    return fileData;
  }

  /**
   * @param fileData The fileData to set.
   */
  public void setFileData(byte[] fileData) {
    this.fileData = fileData;
  }

  /**
   * @return Returns the fileName.
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @param fileName The fileName to set.
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  
  
  private FormFile file;

//Getter for file
  public FormFile getFile() {
    return this.file;
  }

//Setter for file
  public void setFile(FormFile file) {
    this.file = file;
  }

}
