<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

    <!-- logo and add portlet form -->

    <table width='100%'>
      <tr>
        <td><img src="../img/logotestportal.png"></td>
        <td>&nbsp;</td>
        <td align="right">
          <form method='post' name='addPortlet' action='.'>
            <input type='hidden' name='pAction' value='add'/>
            <select name='pApp' id='pApp' onchange='pltListSelectApp();'>
            </select>
            <select name='pName' id='pName'>
            </select>
            <input type='submit' value='Add portlet'/>
          </form>
          <form method='post' name='delPortlet' id='delPortlet' action='.'>
            <input type='hidden' name='pAction' value='del'/>
            <input type='hidden' name='pId' id='delPortletId' value=''/>
          </form>
          <script language='JavaScript'>
            function pltListSelectApp() {
              var sel = document.getElementById('pApp');
              if (sel.selectedIndex != -1) {
                var sel1 = document.getElementById('pName');
                sel1.options.length = 0;
                var pRow = pList[sel.options[sel.selectedIndex].value];
                for (var i in pRow) {
                  sel1.options[sel1.options.length] = new Option(pRow[i], pRow[i]);
                }
              }
            }
            var pList = { <c:out value="${portletNames}" escapeXml="false" /> };
            var sel = document.getElementById('pApp');
            for (var i in pList) {
              sel.options[sel.options.length] = new Option(i, i);
            }
            pltListSelectApp();
          </script>
        </td>
      </tr>
    </table>
