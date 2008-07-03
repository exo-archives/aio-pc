<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<script type="text/javascript">
portal_central_zone.items[portal_central_zone.items.length] = {
  <c:if test="${col_width ne ''}">
    columnWidth: .<c:out value="${col_width}"/>,
  </c:if>
  style: 'padding:10px 0 10px 10px',
  id: "<c:out value="${col_id}"/>",
  items: [
