/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.GenericPortlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the default implementation of
 * GenericPortlet.init(PortletConfig) stores the PortletConfig object.
 */
public class InitWithConfigParamTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "InitWithConfigParamTest";

    public void init(PortletConfig config) throws PortletException {
        super.init(config);
        PortletConfig portletConfig = getPortletConfig();

        if ((portletConfig == null) || !portletConfig.equals(config)) {
            PortletContext context = config.getPortletContext();

            context.setAttribute(CommonConstants.ERROR_MESSAGE,
                                 "GenericPortlet.init(PortletConfig) "
                                 + "doesn't store the PortletConfig "
                                 + "object.");
        }
    }

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletContext context = getPortletContext();

        String message
            = (String)context.getAttribute(CommonConstants.ERROR_MESSAGE);

        if (message == null) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            context.removeAttribute(CommonConstants.ERROR_MESSAGE);
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail(message);
        }

        out.println(resultWriter.toString());
    }
}
