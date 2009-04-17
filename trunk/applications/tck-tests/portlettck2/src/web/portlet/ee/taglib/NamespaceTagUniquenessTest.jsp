<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.tags.PortletTCKCustomTag" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.tags.PortletURLTag"%>

<%! PortletURLTag urlTag = new PortletURLTag(); %>
<%= urlTag.getStartTag() %><portlet:renderURL></portlet:renderURL><%= urlTag.getEndTag() %>

<%! PortletTCKCustomTag namespaceTag = new PortletTCKCustomTag("namespace"); %>
<%= namespaceTag.getStartTag() %><portlet:namespace /><%= namespaceTag.getEndTag() %>

