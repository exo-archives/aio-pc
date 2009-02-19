<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.tags.PortletURLTag" %>

<portlet:renderURL var="url">
  <portlet:param name="RenderURLVarOverwriteTest" value="duh"/>
</portlet:renderURL>
<portlet:renderURL var="url">
  <portlet:param name="RenderURLVarOverwriteTest" value="RenderURLVarOverwriteTest"/>
</portlet:renderURL>
<%
    PortletURLTag urlTag = new PortletURLTag();
    urlTag.setTagContent(url.toString());
%>
<%= urlTag.toString() %>