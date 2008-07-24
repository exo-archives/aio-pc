<%@ page import="com.sun.ts.tests.portlet.api.javax_portlet.PortletContext.GetRequestDispatcherTestPortlet" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.ResultWriter" %>
<%
    String TEST_NAME = GetRequestDispatcherTestPortlet.TEST_NAME;
    ResultWriter resultWriter = new ResultWriter(TEST_NAME);
    resultWriter.setStatus(ResultWriter.PASS);
%>
<%= resultWriter.toString() %>
