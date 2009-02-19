<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.tags.PortletURLTag" %>

<portlet:resourceURL var="url">
  <portlet:param name="ResourceURLVarOverwriteTest" value="duh"/>
</portlet:resourceURL>
<portlet:resourceURL var="url">
  <portlet:param name="ResourceURLVarOverwriteTest" value="ResourceURLVarOverwriteTest"/>
</portlet:resourceURL>
<%
    PortletURLTag urlTag = new PortletURLTag();
    urlTag.setTagContent(url.toString());
%>
<%= urlTag.toString() %>