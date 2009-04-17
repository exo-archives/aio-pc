<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.tags.PortletURLTag" %>

<%! PortletURLTag urlTag = new PortletURLTag(); %>
<%= urlTag.getStartTag() %><portlet:renderURL>
  <portlet:param name="RenderURLTest" value="One"/>
  <portlet:param name="RenderURLTest" value="Two"/>
  <portlet:param name="RenderURLTest" value="Three"/>
</portlet:renderURL><%= urlTag.getEndTag() %>