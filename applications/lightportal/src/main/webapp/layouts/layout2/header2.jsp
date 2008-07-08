<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

    <table width='100%'>
      <tr>
        <td><img src="../img/logotestportal.png"></td>
        <td>&nbsp;</td>
        <td align="right">
          <form method='post' name='addPortlet' action='.'>
            <input type='hidden' name='pAction' value='add'/>
            <select name='pApp' id='pApp' onchange='light_portal_helper_pltListSelectApp();'>
            </select>
            <select name='pName' id='pName'>
            </select>
            <input type='submit' value='Add portlet'/>
          </form>
          <form method='post' name='delPortlet' id='delPortlet' action='.'>
            <input type='hidden' name='pAction' value='del'/>
            <input type='hidden' name='pId' id='delPortletId' value=''/>
          </form>
        </td>
      </tr>
    </table>
