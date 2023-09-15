<header id="header" class="header-dark-theme">
    <a href="${pageContext.request.contextPath}/bookstore/backoffice" id="logo-link" lang="en"
       hx-boost="true" hx-target="#content">Backoffice</a>
    <nav id="nav">
        <button id="btn-menu-mobile"
                data-aria-label-open-menu="open_menu"
                data-aria-label-close-menu="close_menu"
                aria-label="open_menu" aria-controls="menu"
                aria-expanded="false" aria-haspopup="true">Menu
            <span id="hamburger"></span>
        </button>
        <ul id="menu" role="menu" hx-boost="true" hx-target="#content" hx-indicator=".progress">
            <li><a href="${pageContext.request.contextPath}/bookstore/backoffice/dashboard" lang="en">Home</a></li>
        </ul>
    </nav>
</header>