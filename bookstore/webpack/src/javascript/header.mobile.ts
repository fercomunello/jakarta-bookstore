import {$, insertHtmlBeforeBegin} from './functions/dom';

const html = (): string => `
    <button id="navbar-menu-toggler" type="button" class="navbar-toggler"
            data-bs-toggle="collapse" data-bs-target="#navbar-menu"
            aria-controls="navbar-menu" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
`;

const navbarMenu = $('#navbar-menu') as HTMLElement;
const navLinks = navbarMenu.querySelectorAll('.nav-link') as NodeListOf<Element>;

const navLinkListener: EventListener = () => {
    const button = $('#navbar-menu-toggler') as HTMLButtonElement
    button.click();
};

const mediaQueryList: MediaQueryList = window.matchMedia('(max-width: 576px)');

mediaQueryList.addEventListener('change', (event) =>
    event.matches ? createNavToggler() : dropNavToggler()
);

if (mediaQueryList.matches) {
    createNavToggler();
}

function createNavToggler() {
    insertHtmlBeforeBegin(navbarMenu, html());

    navLinks.forEach(link =>
        link.addEventListener('click', navLinkListener)
    );
}

function dropNavToggler() {
    $('#navbar-menu-toggler').remove();

    navLinks.forEach(link =>
        link.removeEventListener('click', navLinkListener)
    );
}