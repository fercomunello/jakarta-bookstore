<%@ tag pageEncoding="UTF-8" description="Backoffice JSP Template" %>

<%@ attribute name="title" description="The page title displayed in the browser tab."
              type="java.lang.String" required="false" %>

<%@ attribute name="css" description="Head fragment for per-page stylesheets. "
              fragment="true" required="false" %>

<%@ attribute name="javascript" description="Head fragment for per-page scripts. "
              fragment="true" required="false" %>

<%@ attribute name="metatags" description="Page specific meta tags for SEO optimization. "
              fragment="true" required="false" %>

<%@ taglib prefix="webpack" uri="/WEB-INF/taglibs/webpack.tld" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:useBean id="hxBoosted" scope="request" type="java.lang.Boolean" />

<% if (!hxBoosted) { %>
<jsp:useBean id="template" scope="request"
             type="io.github.jakarta.business.backoffice.BackofficeTemplate" />

<!DOCTYPE html>
<html lang="en"> <% } %>
    <head>
        <% if (!hxBoosted) { %>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <meta name="author" content="Fernando Comunello">

        <jsp:invoke fragment="metatags" /> <% } %>

        <c:choose>
            <c:when test="${not empty title}">
                <title>${title} | Backoffice</title>
            </c:when>
            <c:otherwise>
                <title>Backoffice</title>
            </c:otherwise>
        </c:choose>

        <link type="image/png" sizes="32x32" rel="icon" href="${webpack:png('favicon')}">
        <link rel="stylesheet" href="${webpack:css('layout.bundle')}">
        <jsp:invoke fragment="css" />

        <script type="text/javascript" src="${webpack:javascript('main.bundle')}" defer></script>

        <jsp:invoke fragment="javascript" />
    </head>

    <% if (!hxBoosted) { %>
    <body hx-ext="head-support">
        <%@ include file="header.jsp" %>

        <main>
            <div id="content" class="container"> <% } %>
                <c:if test="${not empty title}">
                    <h1>${title}</h1>
                </c:if>
                <jsp:doBody />
            <% if (!hxBoosted) { %>
            </div>
        </main><% } %>

        <% if (!hxBoosted) { %>
            <footer id="footer">
                <p>&copy; ${template.year} - All rights reserved.</p>
            </footer>
    </body>
</html> <% } %>