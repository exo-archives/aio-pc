<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.tags.PortletURLTag" %>

<%! PortletURLTag urlTag = new PortletURLTag(); %>
<%= urlTag.getStartTag() %><portlet:actionURL copyCurrentRenderParameters="true">
<portlet:param name="language" value="Ada"/>
<portlet:param name="language" value="Pascal"/>
<portlet:param name="OS" value=""/>
<portlet:param name="IDE" value="Eclipse"/>
</portlet:actionURL><%= urlTag.getEndTag() %>