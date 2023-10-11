import {$, insertHtmlAfterBegin, domReady} from './functions/dom';

const html = (): string => `
   <div id="main-progress-bar" class="progress-bar">
        <div class="progress-indicator"></div>
   </div>
`;

domReady(() => {
    insertHtmlAfterBegin( $('#main-header'), html());
});