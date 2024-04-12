<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>테스트</title>
</head>
<body>


<%--        <p>--%>
<%--            1. 당신의 역할은 무엇입니까?<br/>--%>

<%--            <label><input type="radio"--%>
<%--                          name="dataList[0]" value="서버">--%>
<%--                서버</label>--%>

<%--            <label><input type="radio"--%>
<%--                          name="dataList[0]" value="프론트">--%>
<%--                프론트</label>--%>

<%--            <label><input type="radio"--%>
<%--                          name="dataList[0]" value="풀스택">--%>
<%--                풀스택</label>--%>

<%--            <label><input type="radio"--%>
<%--                          name="dataList[0]" value="백엔드">--%>
<%--                백엔드</label>--%>
<%--        </p>--%>
    <h2>테스트</h2>
    <form method="post">
        <c:forEach var="q" items="${questions}" varStatus="sta">
            <p>
<%--                    ${question.title}<br/>--%>
                   ${sta.index+1} - ${q.title}<br/>
<%--                <c:forEach var="opt" items="${question.options}">--%>
                <c:forEach var="opt" items="${q.options}">
                    <label>
<%--                        <input type="radio" name="dataList[0]" value="${opt}"/>${opt}--%>
                        <input type="radio" name="dataList[${sta.index}]" value="${opt}"/>${opt}
                    </label>
<%--                    <c:if test="${q.choice}">--%>
<%--                        ${q.choice} <br/>--%>
<%--                    </c:if>--%>
                    <c:if test="${!q.choice}">
                        없다<br/>
                    </c:if>
                </c:forEach>
            </p>
        </c:forEach>


        <input type="submit" value="확인">
    </form>
</body>
</html>
