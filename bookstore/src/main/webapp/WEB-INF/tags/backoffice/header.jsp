<header>
    <a href="${pageContext.request.contextPath}/bookstore/backoffice" id="dashboard-link"
        lang="en" hx-boost="true" hx-target="#content">Backoffice</a>
    <nav>
        <button id="menu-mobile"
                data-aria-label-open-menu="open_menu"
                data-aria-label-close-menu="close_menu"
                aria-label="open_menu" aria-controls="menu"
                aria-expanded="false" aria-haspopup="true">Menu
            <span></span>
        </button>
        <ul id="menu" role="menu" hx-boost="true" hx-target="#content">
            <li>
                <a href="${pageContext.request.contextPath}/bookstore/backoffice/dashboard"
                lang="en">Home</a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/bookstore/backoffice/authors">
                    Authors</a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/bookstore/backoffice/authors/new">
                    New Author</a>
            </li>
        </ul>
    </nav>
</header>