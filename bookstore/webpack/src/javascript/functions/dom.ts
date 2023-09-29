export function $(selector: string): HTMLElement | HTMLCollectionOf<Element> | Element | null {
    switch (selector.charAt(0)) {
        case '.':
            const classNames: string = selector.replace('.', ' ');
            return document.getElementsByClassName(classNames) as HTMLCollectionOf<Element>;
        case '#':
            const elementId: string = selector.substring(1, selector.length);
            return document.getElementById(elementId) as HTMLElement;
        case '<':
            if (selector.charAt(selector.length - 1) === '>') {
                const tagName: string = selector.replace('<', '')
                    .replace('>', '');
                return document.getElementsByTagName(tagName) as HTMLCollectionOf<Element>;
            }
            return null;
        default:
            return document.querySelector(selector) as Element;
    }
}

export function customEventCallback(event: Event, callback: EventListener): void {
    const customEvent: CustomEvent = event as CustomEvent;
    if (customEvent.detail.success) {
        callback(customEvent);
    }
}

export function domReady(callback: Function): void {
    if (document.readyState === 'complete' || document.readyState === 'interactive') {
        callback();
        return;
    }
    document.addEventListener('DOMContentLoaded', callback());
}