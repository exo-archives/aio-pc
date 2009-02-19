<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<script type="text/javascript">
<c:set var="body" scope="request">
<jsp:include page="header2.jsp"/>
</c:set>
<% String body = ((String) request.getAttribute("body")).replace("\n", "\\n").replace("\r", "").replace("\"", "\\\"").replace("<", "&lt;"); request.setAttribute("body", body); %>
var data = "<c:out value="${body}" escapeXml="false"/>";
portal_zones[portal_zones.length] = {
  region: 'north',
  margins: '5 5 5 5',
  html: data.replace(/&lt;/g, "<")
}
</script>
