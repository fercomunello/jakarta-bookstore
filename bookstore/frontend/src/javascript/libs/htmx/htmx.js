// noinspection JSUnresolvedReference

import 'htmx.org';

window.htmx = require("htmx.org");

document.body.addEventListener('htmx:beforeSwap', (event) => {
    switch (event.detail.xhr.status) {
        case 404: // alert the user when a 404 occurs (maybe use a nicer mechanism than alert())
            alert("Error: Could Not Find Resource");
            break;
        case 400: // allow 400 responses to swap as we are using this as a signal that
            // a form was submitted with bad data and want to rerender with the
            // errors
            event.detail.shouldSwap = true;
            event.detail.isError = false;
            break;
        case 418: // if the response code 418 (I'm a teapot) is returned, retarget the
            // content of the response to the element with the id `teapot`
            event.detail.shouldSwap = true;
            event.detail.target = htmx.find("#teapot");
            alert("I'm a teapot.");
            break;
    }
});