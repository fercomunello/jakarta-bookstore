<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jakarta" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:useBean id="authors" scope="request"
 type="java.util.List<io.github.jakarta.business.book.author.AuthorDataTable.AuthorInfo>"/>

<jakarta:template title="Book authors">
    <jsp:body>
        <p>This is the book authors page.</p>

        <c:forEach var="author" items="${authors}">
            <p>${author.uuid}</p><br>
            <p><c:out value="${author.fullName}"/></p>
        </c:forEach>
    </jsp:body>
</jakarta:template>