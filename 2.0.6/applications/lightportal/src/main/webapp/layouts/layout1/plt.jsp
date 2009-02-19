<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

  <c:set var="reqURL" value="${pageContext.request.requestURL}?" />
  <div id="<c:out value="${pinf.wid}"/>" class="portlet">
    <div class="portletHeader">
      <font size='4' face='Verdana,Arial'>
        <div id="p<c:out value="${pinf.wid}"/>title"><c:out value="${pinf.title}"/>
        <input type="image" src="../img/icon_del.gif"
          onclick="document.getElementById('delPortletId').value='<c:out value="${pinf.portlet}"/>'; document.getElementById('delPortlet').submit(); return false;"/>
        <!-- {mode: <c:out value="${pinf.mode}"/>; state: <c:out value="${pinf.state}"/>} -->
        </div>
      </font>
      <!-- font size="-2">(<c:out value="${pinf.portlet}"/>)</font><br/ -->
      <c:set var="resMode" value="${reqURL}portal:componentId=${pinf.portlet}&portal:isSecure=true" />
      <c:forEach var="mode" items="${pinf.modes}">
        <a href='<c:out value="${resMode}&portal:portletMode=${mode}" escapeXml="false" />'>
          <c:out value="${mode}" />
        </a>
      </c:forEach>
      <br>
      <c:forEach var="state" items="${pinf.states}">
        <a href='<c:out value="${resMode}&portal:windowState=${state}" escapeXml="false" />'>
          <c:out value="${state}" />
        </a>
      </c:forEach>
    </div>
    <div id="p<c:out value="${pinf.wid}"/>content" class="portletContent"><c:out value="${pinf.out}" escapeXml="false" /></div>
  </div>
  <!-- hr style='border: 1px dashed;' width="100%"/ -->
