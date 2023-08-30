<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="webpack" uri="/WEB-INF/taglibs/webpack.tld" %>
<c:set var="servletContext" value="${pageContext.servletContext}"/>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="${webpack:css('dash')}">
    <script type="text/javascript" src="${webpack:javascript('dash')}" defer></script>
</head>
<body>
    <h1>Hello, world!</h1>
</body>
</html>