<%@ tag pageEncoding="UTF-8" description="HTML5 Form template" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ attribute name="action" description="Form submit destination"
              fragment="false" required="true" %>

<%@ attribute name="method" description="Form submit HTTP method"
              fragment="false" required="true" %>

<%@ attribute name="id" description="Form element id"
              fragment="false" required="true" %>

<form id="${id}" method="${method}"
      action="${pageContext.request.contextPath}/bookstore/backoffice${action}"
      hx-boost="true" hx-target="#content" hx-replace-url="true">

    <c:if test="${method eq 'PUT'}">
        <input name="_method" type="hidden" value="PUT">
    </c:if>

    <jsp:doBody/>
</form>