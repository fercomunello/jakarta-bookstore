<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="static io.github.jakarta.business.book.author.Author.*" %>
<%--@elvariable id="authorBean" type="io.github.jakarta.business.book.author.AuthorForm.AuthorBean"--%>

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

                <div class="form-group">
                    <label for="first-name">First name: </label>
                    <input type="text" id="first-name" name="firstName" placeholder="Author first name"
                           value="<c:out value="${authorBean.firstName}"/>" required>
                </div>

                <div class="form-group">
                    <label for="last-name">Last name: </label>
                    <input type="text" id="last-name" name="lastName" placeholder="Author second name"
                           value="<c:out value="${authorBean.lastName}"/>" required>
                </div>

                <div class="form-group">
                    <label for="nickname">Nickname: </label>
                    <input type="text" id="nickname" name="nickname" placeholder="Author nickname"
                           value="">
                </div>

                <div class="form-group">
                    <label for="nationality">Nationality: </label>
                    <input type="text" id="nationality" name="nationality" placeholder="Author nationality"
                           value="">
                </div>

                <div class="form-group">
                    <label for="date-of-birth">Date of birth: </label>
                    <input type="text" id="date-of-birth" name="dateOfBirth" placeholder="Author birth date"
                           value="">
                </div>

                <div class="form-group">
                    <label for="occupation">Occupation: </label>
                    <input type="text" id="occupation" name="occupation" placeholder="Author occupation"
                           value="">
                </div>

                <div class="form-group">
                    <label for="website">Website: </label>
                    <input type="text" id="website" name="website" placeholder="Author website"
                           value="">
                </div>

                <div class="form-group">
                    <label for="employer">Employer: </label>
                    <input type="text" id="employer" name="employer" placeholder="Author employer"
                           value="">
                </div>

                <div class="form-group">
                    <label for="notable-work">Notable works: </label>
                    <input type="text" id="notable-work" name="notableWork" placeholder="Author notable work"
                           value="<c:out value="${authorBean.notableWork}"/>">
                </div>

<%--                <hr>--%>

                <div class="form-group">
                    <label for="biography">Biography: </label>
                    <textarea id="biography" name="biography" placeholder="Author biography" content="">
                    </textarea>
                </div>

                <button type="submit" form="book-author">Save</button>
            </jsp:body>
        </jakarta:form>
    </jsp:body>
</jakarta:template>