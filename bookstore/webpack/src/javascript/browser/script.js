import BrowserTab from './BrowserTab';
import {$} from '../functions/dom';

const browserTab = new BrowserTab('bookstore');

browserTab.callback = ({tabId, isTabNew, parentTabId}) => {
    if (isTabNew && parentTabId !== null) {
        const loadingText = 'Loading... 🚬';
        let content = $('#content');
        if (content) {
            content.innerHTML = loadingText;
        } else {
            document.body.innerHTML = loadingText;
        }
        window.location.reload();
    }
    console.log(`
        TabId: ${tabId}; New Tab: ${isTabNew}; 
        Duplicated: ${parentTabId !== null ? `Yes. ParentTabId = ${parentTabId}` : "No"}.
    `);
};

browserTab.init();