<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.tags.PortletURLTag" %>

<%
    String urlString = "";

    for (int i = 0; i < 2; i++) {
        String value = (i == 0) ? "duh" : "ResourceURLVarInLoopTest";
%>
        <portlet:resourceURL var="url">
          <portlet:param name="ResourceURLVarInLoopTest" value="<%= value %>"/>
        </portlet:resourceURL>
<%
        urlString = url.toString();
    }

    PortletURLTag urlTag = new PortletURLTag();
    urlTag.setTagContent(urlString);
%>
<%= urlTag.toString() %>