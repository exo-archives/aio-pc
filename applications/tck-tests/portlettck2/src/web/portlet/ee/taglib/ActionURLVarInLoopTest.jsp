<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.tags.PortletURLTag" %>

<%
    String urlString = "";

    for (int i = 0; i < 2; i++) {
        String value = (i == 0) ? "duh" : "ActionURLVarInLoopTest";
%>
        <portlet:actionURL var="url">
          <portlet:param name="ActionURLVarInLoopTest" value="<%= value %>"/>
        </portlet:actionURL>
<%
        urlString = url.toString();
    }

    PortletURLTag urlTag = new PortletURLTag();
    urlTag.setTagContent(urlString);
%>
<%= urlTag.toString() %>