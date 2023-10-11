export function $(selector: string): Element {
    switch (selector.charAt(0)) {
        case '#':
            const elementId: string = selector.substring(1, selector.length);
            return document.getElementById(elementId) as HTMLElement;
        case '<':
            if (selector.charAt(selector.length - 1) === '>') {
                const tagName: string = selector.replace('<', '')
                    .replace('>', '');
                return document.getElementsByTagName(tagName)[0];
            }
    }
    return document.querySelector(selector) as Element;
}

export function insertHtmlAfterBegin(element: Element, html: string) {
    element?.insertAdjacentHTML('afterbegin', html);
}

export function insertHtmlBeforeBegin(element: Element, html: string) {
    element?.insertAdjacentHTML('beforebegin', html);
}

export function insertHtmlBeforeEnd(element: Element, html: string) {
    element?.insertAdjacentHTML('beforeend', html);
}

export function domReady(callback: (event: Event) => void) {
    document.addEventListener('DOMContentLoaded', (event) => callback(event));
}

export function windowReady(callback: (event: Event) => void) {
    window.addEventListener('load', (event) => callback(event));
}

export function afterSettleHtmx(callback: (event: Event) => void) {
    window.addEventListener('htmx:afterSettle', (event) => callback(event));
}