<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="static io.github.jakarta.business.backoffice.author.Author.*" %>
<%--@elvariable id="authorBean" type="io.github.jakarta.business.backoffice.author.AuthorForm.AuthorBean"--%>

<%@ taglib tagdir="/WEB-INF/tags" prefix="jakarta" %>
<%@ taglib prefix="webpack" uri="/WEB-INF/taglibs/webpack.tld" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jakarta:template title="Book author">

    <jsp:attribute name="css">
    </jsp:attribute>

    <jsp:attribute name="javascript" />

    <jsp:body>
        <p>This is the book author page.</p> <br>

        <jakarta:form action="/backoffice/authors" method="POST" id="book-author">
            <jsp:body>
                <jakarta:validationFeedback of="<%= Metadata.NAME.target() %>"/>
                <jakarta:validationFeedback of="<%= Metadata.NOTABLE_WORK.target() %>" />
                <br>

                <label for="first-name">First name: </label>
                <input type="text" id="first-name" name="firstName" placeholder="Author first name"
                       value="<c:out value="${authorBean.firstName}"/>" required>

                <label for="last-name">Last name: </label>
                <input type="text" id="last-name" name="lastName" placeholder="Author second name"
                       value="<c:out value="${authorBean.lastName}"/>" required>

                <label for="notable-work">Notable work: </label>
                <input type="text" id="notable-work" name="notableWork" placeholder="Author notable work"
                       value="<c:out value="${authorBean.notableWork}"/>">

                <button type="submit" form="book-author">Save</button>
            </jsp:body>
        </jakarta:form>
    </jsp:body>
</jakarta:template>