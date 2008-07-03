<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<c:set var="reqURL" value="${pageContext.request.requestURL}" />
<script type="text/javascript">
portal_zones[portal_zones.length] = {
  region: 'west',
  title: 'Pages',
  width: 200,
  margins: '0 5 0 5',
  collapsible: false,
  cmargins:'35 5 5 5',
  iconCls: 'tabs',
  extraCls: 'x-page-tab',
  layout: 'row',
  items: [
  {
    title: 'New page',
    autoScroll: true,
    border: false,
    iconCls: 'add',
    collapsible: true,
    collapsed: true,
    html: '<form action="<c:out value="${reqURL}" escapeXml="false" />" method="post"><input type="text" name="portal:newpage"/><input type="submit" value="Add new page"/></form>'
  },
  <c:forEach var="page" items="${portalPages}">
    {
      title:'<a href="<c:out value="${reqURL}?portal:page=${page}" escapeXml="false" />"><c:out value="${page}" /></a>',
      autoScroll: true,
      border: false,
      iconCls: 'page',
      itemCls: 'x-page-tab',
      collapsible: false,
      <c:if test="${portalPage eq page}">
        activeItem: true,
      </c:if>
    },
  </c:forEach>

]}
</script>
