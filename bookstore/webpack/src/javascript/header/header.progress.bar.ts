import {$, createElementAfterBegin, documentReady} from '../functions/dom';

documentReady((): void => {
    createElementAfterBegin( $('<header>'), `
       <div id="main-progress-bar" class="progress-bar">
            <div class="progress-indicator"></div>
       </div>
   `);
});