<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.tags.PortletURLTag" %>

<portlet:resourceURL var="url">
  <portlet:param name="ResourceURLVarTest" value="ResourceURLVarTest"/>
</portlet:resourceURL>
<%
    PortletURLTag urlTag = new PortletURLTag();

    // will get a ClassCastException right here if "url" isn't a string
    urlTag.setTagContent(url);
%>
<%= urlTag.toString() %>