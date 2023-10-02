import {$, documentReady} from '../functions/dom';

documentReady((): void => {
    const menu = $('#menu-mobile') as HTMLButtonElement;

    menu.addEventListener('click', (event: MouseEvent) => toggleMenu(event));
    menu.addEventListener('touchstart', (event: TouchEvent) => toggleMenu(event));

    const nav = $('header > nav') as HTMLElement;
    const links = nav.getElementsByTagName('a') as HTMLCollectionOf<HTMLAnchorElement>;

    for (let i = 0; i < links.length; i++) {
        links[i].addEventListener('click',
            () => nav.classList.toggle('active'));
    }

    function toggleMenu(event: MouseEvent | TouchEvent): void {
        if (event.type === 'touchstart') {
            event.preventDefault();
        }

        const active: boolean = nav.classList.toggle('active');
        const element = event.currentTarget as HTMLElement;
        element.setAttribute('aria-expanded', String(active));

        let attribute: string | null;
        if (active) {
            attribute = element.getAttribute('data-aria-label-close-menu');
            if (attribute != null) {
                element.setAttribute('aria-label', attribute);
            }
        } else {
            attribute = element.getAttribute('data-aria-label-open-menu');
            if (attribute != null) {
                element.setAttribute('aria-label', attribute);
            }
        }
    }
});