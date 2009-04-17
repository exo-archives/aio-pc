<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.tags.PortletURLTag" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.ResultWriter" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.EscapeXmlTester" %>

<%
String TEST_NAME = "RenderURLEscapeXmlTest";
ResultWriter resultWriter = new ResultWriter(TEST_NAME);

%>
<portlet:renderURL var="url" escapeXml="true">
	<portlet:param name="test&test" value="1<5>2'\"bla"/>
	<portlet:param name="test&test" value="a&b&c&d"/>	
	<portlet:param name="OS" value="Solaris&Linux"/>	
</portlet:renderURL>
<%
if(!EscapeXmlTester.isXmlEscaped(url)){
	resultWriter.setStatus(ResultWriter.FAIL);
	resultWriter.addDetail("escapeXml:");
    resultWriter.addDetail("some characters are not correctly escaped:");		
    resultWriter.addDetail(url);   
}
else{
	resultWriter.setStatus(ResultWriter.PASS);
}
%>
<%= resultWriter.toString() %>






