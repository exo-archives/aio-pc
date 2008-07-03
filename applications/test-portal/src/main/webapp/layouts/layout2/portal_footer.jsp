<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<script type='text/javascript'>
  var pList = { <c:out value="${portletNames}" escapeXml="false" /> };

  function pltListSelectApp() {
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

Ext.onReady(function() {

    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

    var viewport = new Ext.Viewport({
        layout:'border',
        items: portal_zones
    });

    var portletListSel2 = document.getElementById('pApp');
    for (var i in pList) {
      portletListSel2.options[portletListSel2.options.length] = new Option(i, i);
    }
    pltListSelectApp();

});
</script>

  </body>
</html>
