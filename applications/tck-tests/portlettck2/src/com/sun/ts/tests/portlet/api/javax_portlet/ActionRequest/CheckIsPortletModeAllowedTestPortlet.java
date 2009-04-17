/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletMode;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;




/**
 *	A  Test for PortletRequest.isPortletModeAllowed(portletMode)
 *   The VIEW portlet mode must be supported , so we'll call
 *                 this method with VIEW portlet mode and make sure it
 *                 returns true.
 */


public class CheckIsPortletModeAllowedTestPortlet extends LogicInProcessActionPortlet {

    public static String TEST_NAME="CheckIsPortletModeAllowedTest";

    public void processAction(ActionRequest request, ActionResponse response ) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        boolean isAllowed = request.isPortletModeAllowed(PortletMode.VIEW);

        if (isAllowed) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail(
                " Every Portlet should allow the VIEW portlet mode");
        }

		request.getPortletSession(true).setAttribute("TestResult", resultWriter.toString());
    }


}
