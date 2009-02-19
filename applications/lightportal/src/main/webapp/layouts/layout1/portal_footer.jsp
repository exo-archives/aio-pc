<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

  <c:set var="reqURL" value="${pageContext.request.requestURL}" />
    <script type='text/javascript'>
  
    $(document).ready(
      function () {
        $('div.portletColumn').Sortable(
          {
            accept: 'portlet',
            helperclass: 'sortHelper',
            opacity: 0.5,
            activeclass :   'sortableactive',
            hoverclass :  'sortablehover',
            handle: 'div.portletHeader',
            tolerance: 'pointer',
            onChange : function(ser) { },
            onStart : function(el) { },
            onStop : function() { sendNewLayout(); }
          }
        );
      }
    );
  
    function sendNewLayout() {
      var layout = "";
      var cols = document.getElementsByName("portletColumn");
      for (var i = 1; i <= cols.length; i++) {
        layout += cols[i - 1].id + ": ";
        var children = cols[i - 1].childNodes;
        for (var j = 1; j <= children.length; j++) {
          if (children[j - 1].className == "portlet")
            layout += children[j - 1].id + " ";
        }
        layout += "\n";
      }
      var param = "portal:layout=" + escape(layout);
      $.ajax({
        type: "POST",
        url: "<c:out value="${reqURL}"/>",
        data: param,
        success: function(msg) { }
      }); 
    }
  
    </script>

  </body>
</html>
