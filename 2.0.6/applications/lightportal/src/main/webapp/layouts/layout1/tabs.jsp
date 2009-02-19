<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<table class="tabs">
  <tr>
    <c:set var="reqURL" value="${pageContext.request.requestURL}" />
    <c:forEach var="page" items="${portalPages}">
      <c:if test="${portalPage eq page}">
        <form action='<c:out value="${reqURL}" escapeXml="false" />' method="post">
      </c:if>
      <td class="tabs">
        <c:if test="${portalPage eq page}">
          <b>[
        </c:if>
          <a href='<c:out value="${reqURL}?portal:page=${page}" escapeXml="false" />'><c:out value="${page}" /></a>
        <c:if test="${portalPage eq page}">
          <input type="hidden" name="portal:delpage" value="<c:out value="${portalPage}"/>"/>
          <input type="image" src="../img/icon_del.gif"/>
          ]</b>
        </c:if>
      </td>
      <c:if test="${portalPage eq page}">
        </form>
      </c:if>
    </c:forEach>
    <form action='<c:out value="${reqURL}" escapeXml="false" />' method="post">
      <td align="center" valign="center">
        <input type="text" name="portal:newpage"/>
        <input type="submit" value="Add new page"/>
      </td>
    </form>
  </tr>
</table>
<hr/>
