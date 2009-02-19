/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.exoplatform.services.log.ExoLogger;

public class SubmitAction extends Action {

  /**
   * Logger.
   */
  private static final Log LOG = ExoLogger.getLogger(SubmitAction.class);

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

    DynaActionForm dform = (DynaActionForm) form;
    LOG.info("SubmitAction.execute() dform = " + dform);
    try {
      LOG.info("SubmitAction.execute abc = " + dform.get("abc"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      LOG.info("SubmitAction.execute file = " + dform.get("file"));
    } catch (Exception e) {
      e.printStackTrace();
    }

//    ActionRedirect redirect = new ActionRedirect(mapping.findForward("success"));
    ActionForward forward = new ActionForward(mapping.findForward("success"));
    
//    try {
//      redirect.addParameter("abc", dform.get("abc"));
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    try {
//      redirect.addParameter("file", dform.get("file"));
//    } catch (Exception e) {
//      e.printStackTrace();
//    }

    return forward;

  }

  private FormFile file;

  public FormFile getFile() {
    return this.file;
  }

  public void setFile(FormFile file) {
    this.file = file;
  }

}
