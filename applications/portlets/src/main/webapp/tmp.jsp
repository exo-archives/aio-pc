<%pageContext.include("tmp1.jsp");%>
<%pageContext.include("tmp1.jsp");%>
<%out.println("erez ddd");%>
<%
RequestDispatcher rd=request.getRequestDispatcher("tmp1.jsp");
rd.include(request,response);
rd.include(request,response);
rd.include(request,response);
rd.include(request,response);
%>
