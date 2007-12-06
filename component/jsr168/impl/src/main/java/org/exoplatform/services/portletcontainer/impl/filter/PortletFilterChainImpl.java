package org.exoplatform.services.portletcontainer.impl.filter;


import javax.portlet.*;
import org.exoplatform.services.portletcontainer.filter.PortletFilter;
import org.exoplatform.services.portletcontainer.filter.PortletFilterChain;
import java.util.Collection;
import java.util.Iterator;
import java.io.IOException;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 17 nov. 2003
 * Time: 21:21:35
 */
public class PortletFilterChainImpl implements PortletFilterChain{

	private Iterator iterator;
	private Portlet portlet;
	private boolean isAction;

	public void invoke(PortletRequest request, PortletResponse response,
									   Collection filters, Portlet p, boolean isAction)
					throws IOException, PortletException {
		this.iterator = filters.iterator();
		this.isAction = isAction;
		this.portlet = p;
	  PortletFilter portletFilter = (PortletFilter) iterator.next();
		portletFilter.doFilter(request, response, this);
	}

	public void doFilter(PortletRequest request, PortletResponse response)
					throws IOException, PortletException {
		if(iterator.hasNext()){
			PortletFilter portletFilter = (PortletFilter) iterator.next();
			portletFilter.doFilter(request, response, this);
		} else {
			if (isAction)
				portlet.processAction((ActionRequest)request, (ActionResponse)response);
			else
				portlet.render((RenderRequest)request, (RenderResponse)response);
		}
	}


}
