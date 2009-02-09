<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<c:set var="reqURL" value="${pageContext.request.requestURL}" />
<script type='text/javascript'>
  var pList = { <c:out value="${portletNames}" escapeXml="false" /> };

  function light_portal_helper_pltListSelectApp() {
    var portletListSel = document.getElementById('pApp');
    if (portletListSel.selectedIndex != -1) {
      var portletListSel1 = document.getElementById('pName');
      portletListSel1.options.length = 0;
      var pRow = pList[portletListSel.options[portletListSel.selectedIndex].value];
      for (var i=0; i< pRow.length; i++) {
        portletListSel1.options[portletListSel1.options.length] = new Option(pRow[i], pRow[i]);
      }
    }
  }

var viewport = null;

Ext.onReady(function() {

    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

    viewport = new Ext.Viewport({
        layout:'border',
        items: portal_zones
    });

    var portletListSel2 = document.getElementById('pApp');
    for (var i in pList) {
      portletListSel2.options[portletListSel2.options.length] = new Option(i, i);
    }
    light_portal_helper_pltListSelectApp();

});

function light_portal_helper_getHttpRequest() {
  var req = null;
  try {                           
    // Firefox, Opera 8.0+, Safari
    req = new XMLHttpRequest();   
  } catch (e) {                   
    // Internet Explorer          
    try {
      req = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
      try {
        req = new ActiveXObject("Microsoft.XMLHTTP");
      } catch (e) {
        alert("Your browser does not support AJAX!");
        return false;
      }
    }
  }
  return req;
}

    function light_portal_helper_sendNewLayout() {
      var layout = "";
      var zones = viewport.items.items;
      var zone = null;
      for (var i = 1; i <= zones.length; i++) {
         if (zones[i - 1].region === 'center') {
           zone = zones[i - 1];
           break;
         }
      }
      if (zone === null)
          return;
      var cols = zone.items.items;
      for (var i = 1; i <= cols.length; i++) {
        layout += cols[i - 1].id + ": ";
        var children = cols[i - 1].items.items;
        for (var j = 1; j <= children.length; j++) {
          layout += children[j - 1].id + " ";
        }
        layout += "\n";
      }
      var params = "portal:layout=" + escape(layout);

      portletReq = light_portal_helper_getHttpRequest();
      if (portletReq === false) {
        alert("Exception: I could not find the XMLHttpRequest");
      }
      portletReq.onreadystatechange = function () {};
      portletReq.open('POST', "<c:out value="${reqURL}"/>", true);
      portletReq.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
      portletReq.setRequestHeader("Content-length", params.length);
      portletReq.setRequestHeader("Connection", "close");
      portletReq.send(params);
    }

</script>

  </body>
</html>
