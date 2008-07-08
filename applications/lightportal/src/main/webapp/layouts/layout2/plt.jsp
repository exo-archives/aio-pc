  <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
  
  <c:set var="reqURL" value="${pageContext.request.requestURL}?" />
  <c:set var="resMode" value="${reqURL}portal:componentId=${pinf.portlet}&portal:isSecure=true" />
  <c:set var="pltOut" value="${pinf.out}" scope="request" />
  <% String pltOut = ((String) request.getAttribute("pltOut")).replace("\n", "\\n").replace("\r", "").replace("\"", "\\\"").replace("<", "&lt;"); request.setAttribute("pltOut", pltOut); %>

  {
    title: '<c:out value="${pinf.title}"/>',
    tools: [
      {
        id:'gear',
        handler: function(){
            //Ext.Msg.alert('Message', 'The Settings tool was clicked.');
            sendNewLayout();
        }
      },
      {
        id:'minimize',
        handler: function(){
          top.location.href='<c:out value="${resMode}&portal:windowState=minimize" escapeXml="false" />';
        }
      },
      {
        id:'maximize',
        handler: function(){
          top.location.href='<c:out value="${resMode}&portal:windowState=maximize" escapeXml="false" />';
        }
      },
      {
        id:'restore',
        handler: function(){
          top.location.href='<c:out value="${resMode}&portal:windowState=normal" escapeXml="false" />';
        }
      },
      {
        id: 'close',
        handler: function(e, target, panel){
          panel.ownerCt.remove(panel, true);
          document.getElementById('delPortletId').value='<c:out value="${pinf.portlet}"/>';
          document.getElementById('delPortlet').submit();
        }
      }
    ],
    id: "<c:out value="${pinf.wid}"/>",
    html: "<c:out value="${pltOut}" escapeXml="false"/>".replace(/&lt;/g, "<")
  },
