<%@ tag pageEncoding="UTF-8" description="Backoffice JSP Template" %>

<%@ tag import="static io.github.bookstore.jakarta.servlet.BrowserAction.*" %>
<%@ taglib prefix="webpack" uri="/WEB-INF/taglibs/webpack.tld" %>

<%@ attribute name="title" description="The page title displayed in the browser tab."
              type="java.lang.String" required="true" %>

<%@ attribute name="metatags" description="Page specific meta tags for SEO optimization. "
              fragment="true" required="false" %>

<jsp:useBean id="browserAction" scope="request" type="io.github.bookstore.jakarta.servlet.BrowserAction" />

<% if (browserAction == REQUEST) { %>
<jsp:useBean id="year" scope="request" type="java.lang.Integer" />

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <meta name="author" content="Fernando Comunello">
        <jsp:invoke fragment="metatags" />

        <link type="image/png" sizes="32x32" rel="icon" href="${webpack:png('favicon')}">
        <link rel="stylesheet" href="${webpack:css('layout.bundle')}">

        <script type="text/javascript" src="${webpack:javascript('main.bundle')}" defer></script>

        <title>${title} | Backoffice</title>
    </head>

    <body>
        <%@ include file="header.jsp" %> <% } %>

    <% if (browserAction == XHR_REQUEST || browserAction == REQUEST) { %>
        <main>
            <div class="container">
                <h1>${title}</h1>
                <jsp:doBody />
            </div>
        </main>
    <% } %>

    <% if (browserAction == REQUEST) { %>
        <footer id="footer">
            <p>&copy; ${year} - All rights reserved.</p>
        </footer>
    </body>
</html> <% } %>