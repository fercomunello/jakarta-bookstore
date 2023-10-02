export function $(selector: string): Element {
    switch (selector.charAt(0)) {
        case '.':
            const classNames: string = selector.replace('.', ' ');
            return document.getElementsByClassName(classNames)[0];
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

export function $$(selector: string): HTMLCollectionOf<Element> {
    switch (selector.charAt(0)) {
        case '.':
            const classNames: string = selector.replace('.', ' ');
            return document.getElementsByClassName(classNames);
        case '<':
            if (selector.charAt(selector.length - 1) === '>') {
                const tagName: string = selector.replace('<', '')
                    .replace('>', '');
                return document.getElementsByTagName(tagName);
            }
    }
    return new HTMLCollection();
}

export function createElementAfterBegin(element: Element, html: string) {
    element?.insertAdjacentHTML('afterbegin', html);
}

export function customEventCallback(event: Event, callback: EventListener): void {
    const customEvent: CustomEvent = event as CustomEvent;
    if (customEvent.detail.success) {
        callback(customEvent);
    }
}

export function documentReady(callback: () => void): void {
    document.addEventListener('DOMContentLoaded', () => callback());
}

export function windowReady(callback: (event: Event) => void): void {
    window.addEventListener('load', (event) => callback(event));
}