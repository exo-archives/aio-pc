<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.ResultWriter" %>

<%
    String TEST_NAME = "PortletTaglibImplementedTest";
    ResultWriter resultWriter = new ResultWriter(TEST_NAME);
    resultWriter.setStatus(ResultWriter.PASS);
%>
<%= resultWriter.toString() %>
