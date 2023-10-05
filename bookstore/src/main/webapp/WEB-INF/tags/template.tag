<%@ tag pageEncoding="UTF-8" description="Backoffice JSP Template" %>

<jsp:useBean id="invokeJspc" scope="request" type="java.lang.Boolean" />
<% if (! invokeJspc) { %>

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

        <% if (!hxBoosted) { %>
        <link href="${webpack:png('favicon')}" sizes="32x32" type="image/png" rel="icon" hx-preserve="true">
        <link href="${webpack:css('layout.bundle')}" rel="stylesheet" hx-preserve="true">

        <script src="${webpack:javascript('htmx.bundle')}" defer hx-preserve="true"></script>
        <script src="${webpack:javascript('header.bundle')}" defer hx-preserve="true"></script>
        <% } %>

        <jsp:invoke fragment="css" />
        <jsp:invoke fragment="javascript" />
    </head>

    <% if (!hxBoosted) { %>
    <body hx-ext="head-support" hx-indicator="#main-progress-bar">
        <%@ include file="layout/header.jsp" %>

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

<% } %>