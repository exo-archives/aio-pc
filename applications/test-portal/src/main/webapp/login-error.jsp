<%String loginAction = request.getContextPath() + "/j_security_check";%>
<html>
<head>
  <title>eXo Platform Test Portal - Authorisation error</title>
	<link rel="stylesheet" href="exopcstyle.css">
</head>
<body>
   <!--  h1>eXo Platform Test Portal - Authorisation required </h1  -->
    <table>
    <tr>
        <td><img src="img/SigninPaper162x207.jpg"></td>
        <td><font color="#F9747E"> Login error : <br>
             <form name="loginForm" method="post" action="<%=loginAction%>">
             <input name="j_username" value="admin"/>
             <input type="password" name="j_password" value="admin"/>
             <input name="submit" type="submit" value="Login">
           </form>
        </td>
    </tr>
    </table>
</body>  
</html>

