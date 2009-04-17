/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.client;

import java.util.HashMap;
import java.util.Collections;
import java.util.Enumeration;

import javax.portlet.WindowState;
import javax.portlet.PortletMode;

/**
 * This class encapsulates all information needed by an implementation of 
 * TSPortletAggregateURIInterface, about a portlet, to generate a URI 
 * for a portal page that includes a portlet.
 * 
 */
public class TSPortletInfo {

	private String _portletApplication;
	private String _portletName;
	private String _portletEntityName;
	private PortletMode _portletMode;
	private WindowState _windowState;
	

	public TSPortletInfo(String portletApplication,
						String portletName) {
		_portletApplication = portletApplication;
		_portletName = portletName;
	}


	/**
	 * Constructor
	 * 
	 * @param portletApplication Portlet Application that portlet belongs too.
	 * @param portletName Name of the portlet
	 * @param portletEntity Name of the portlet entity
	 */
	public TSPortletInfo(String portletApplication,
						String portletName,
                        String portletEntityName) {
		_portletApplication = portletApplication;
		_portletName = portletName;
		_portletEntityName = portletEntityName;
		_portletMode = null;
		_windowState = null;
		
		
	}


	/**
	 * @return the name of portlet application that the portlet belongs too.
	 */
	public String getPortletApplication() {
		return _portletApplication;
	}

	/**
	 * @return Name of the portlet.
	 */
	public String getPortletName() {
		return _portletName;
	}

	/**
	 * @return Name of the portlet entity
	 */
	public String getPortletEntityName() {
		return _portletEntityName;
	}

	/**
	 * @return Portlet Mode desired for the portlet.
	 */
	public PortletMode getPortletMode() {
		return _portletMode;
	}

	/**
	 * @return Window state desired for the portlet.
	 */
	public WindowState getWindowState() {
		return _windowState;
	}
	/**
     * Set the portlet mode
	 */
	public void setPortletMode(PortletMode portletMode) {
		_portletMode = portletMode;
	}
	/**
     * Set the window state
	 */
	public void setWindowState(WindowState windowState) {
		_windowState = windowState;
	}
}




