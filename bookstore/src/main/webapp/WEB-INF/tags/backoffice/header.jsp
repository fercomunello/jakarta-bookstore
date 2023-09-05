<header id="header" class="header-dark-theme">
    <a href="${pageContext.request.contextPath}/backoffice" id="logo-link" lang="en"
       hx-boost="true" hx-target="main">Backoffice</a>
    <nav id="nav">
        <button id="btn-menu-mobile"
                data-aria-label-open-menu="open_menu"
                data-aria-label-close-menu="close_menu"
                aria-label="open_menu}" aria-controls="menu"
                aria-expanded="false" aria-haspopup="true">Menu
            <span id="hamburger"></span>
        </button>
        <ul id="menu" role="menu" hx-boost="true" hx-target="main">
            <li><a href="${pageContext.request.contextPath}/backoffice/dashboard" lang="en">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/backoffice/products">Product</a></li>
        </ul>
    </nav>
</header>