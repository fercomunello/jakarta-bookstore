<%@ tag pageEncoding="UTF-8" description="HTML5 Form template" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ attribute name="of" description="The validation target that matches with the id attribute"
              fragment="false" required="true" %>

<%--@elvariable id="formBean" type="io.github.jakarta.view.model.FormBean"--%>

<c:forEach items="${formBean.validations}" var="validation">
    <c:if test="${validation.target.equals(of)}">
        <p style="color: #fd5757">${validation}</p>
    </c:if>
</c:forEach>