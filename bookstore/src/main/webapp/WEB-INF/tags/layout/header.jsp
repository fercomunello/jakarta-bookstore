<%@ taglib prefix="webpack" uri="/WEB-INF/taglibs/webpack.tld" %>

<header id="main-header" class="navbar navbar-expand-sm sticky-lg-top mb-3">
    <nav class="container">
        <a id="dashboard-link" lang="en"
           class="navbar-brand link-body-emphasis text-decoration-none"
           href="${pageContext.request.contextPath}/bookstore/backoffice"
           hx-boost="true" hx-target="#content" hx-swap="innerHTML show:window:top">
            Backoffice
        </a>
        <div class="collapse navbar-collapse" id="navbar-menu">
            <ul id="menu" class="navbar-nav" role="menu"
                hx-boost="true" hx-target="#content" hx-swap="innerHTML show:window:top">
                <li class="nav-item">
                    <a class="nav-link px-2 link-body-emphasis"
                       href="${pageContext.request.contextPath}/bookstore/backoffice/dashboard" lang="en">
                        Home
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link px-2 link-body-emphasis"
                       href="${pageContext.request.contextPath}/bookstore/backoffice/authors">
                        Authors
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link px-2 link-body-emphasis"
                       href="${pageContext.request.contextPath}/bookstore/backoffice/authors/new">
                        New Author
                    </a>
                </li>
            </ul>
        </div>
    </nav>
</header>
