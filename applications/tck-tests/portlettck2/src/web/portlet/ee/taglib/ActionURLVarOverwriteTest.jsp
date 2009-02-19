<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.tags.PortletURLTag" %>

<portlet:actionURL var="url">
  <portlet:param name="ActionURLVarOverwriteTest" value="duh"/>
</portlet:actionURL>
<portlet:actionURL var="url">
  <portlet:param name="ActionURLVarOverwriteTest" value="ActionURLVarOverwriteTest"/>
</portlet:actionURL>
<%
    PortletURLTag urlTag = new PortletURLTag();
    urlTag.setTagContent(url.toString());
%>
<%= urlTag.toString() %>
