//==========================================================
// head-support.js v1.9.6: https://htmx.org/extensions/head-support
// An extension to htmx 1.0 to add head tag merging.
//==========================================================
// noinspection JSUnresolvedReference

(function(){
    let api = null;

    function mergeHead(newContent, defaultMergeStrategy) {
        if (newContent && newContent.indexOf('<head') > -1) {
            const htmlDoc = document.createElement("html");
            // remove svgs to avoid conflicts
            const contentWithSvgsRemoved = newContent.replace(/<svg(\s[^>]*>|>)([\s\S]*?)<\/svg>/gim, '');
            // extract head tag
            const headTag = contentWithSvgsRemoved.match(/(<head(\s[^>]*>|>)([\s\S]*?)<\/head>)/im);

            // if the  head tag exists...
            if (headTag) {
                const added = [];
                const removed = [];
                const preserved = [];
                const nodesToAppend = [];

                htmlDoc.innerHTML = headTag;
                const newHeadTag = htmlDoc.querySelector("head");
                const currentHead = document.head;

                if (newHeadTag == null) {
                    return;
                } else {
                    // put all new head elements into a Map, by their outerHTML
                    // noinspection ES6ConvertVarToLetConst
                    var srcToNewHeadNodes = new Map();
                    for (const newHeadChild of newHeadTag.children) {
                        srcToNewHeadNodes.set(newHeadChild.outerHTML, newHeadChild);
                    }
                }

                // determine merge strategy
                const mergeStrategy = api.getAttributeValue(newHeadTag, "hx-head") || defaultMergeStrategy;

                // get the current head
                for (const currentHeadElt of currentHead.children) {

                    // If the current head element is in the map
                    const inNewContent = srcToNewHeadNodes.has(currentHeadElt.outerHTML);
                    const isReAppended = currentHeadElt.getAttribute("hx-head") === "re-eval";
                    const isPreserved = api.getAttributeValue(currentHeadElt, "hx-preserve") === "true";
                    if (inNewContent || isPreserved) {
                        if (isReAppended) {
                            // remove the current version and let the new version replace it and re-execute
                            removed.push(currentHeadElt);
                        } else {
                            // this element already exists and should not be re-appended, so remove it from
                            // the new content map, preserving it in the DOM
                            srcToNewHeadNodes.delete(currentHeadElt.outerHTML);
                            preserved.push(currentHeadElt);
                        }
                    } else {
                        if (mergeStrategy === "append") {
                            // we are appending and this existing element is not new content
                            // so if and only if it is marked for re-append do we do anything
                            if (isReAppended) {
                                removed.push(currentHeadElt);
                                nodesToAppend.push(currentHeadElt);
                            }
                        } else {
                            // if this is a merge, we remove this content since it is not in the new head
                            if (api.triggerEvent(document.body, "htmx:removingHeadElement", {headElement: currentHeadElt}) !== false) {
                                removed.push(currentHeadElt);
                            }
                        }
                    }
                }

                // Push the tremaining new head elements in the Map into the
                // nodes to append to the head tag
                nodesToAppend.push(...srcToNewHeadNodes.values());

                for (const newNode of nodesToAppend) {
                    const newElt = document.createRange().createContextualFragment(newNode.outerHTML);
                    if (api.triggerEvent(document.body, "htmx:addingHeadElement", {headElement: newElt}) !== false) {
                        currentHead.appendChild(newElt);
                        added.push(newElt);
                    }
                }

                // remove all removed elements, after we have appended the new elements to avoid
                // additional network requests for things like style sheets
                for (const removedElement of removed) {
                    if (api.triggerEvent(document.body, "htmx:removingHeadElement", {headElement: removedElement}) !== false) {
                        currentHead.removeChild(removedElement);
                    }
                }

                api.triggerEvent(document.body, "htmx:afterHeadMerge", {added: added, kept: preserved, removed: removed});
            }
        }
    }

    // noinspection JSUnusedGlobalSymbols
    htmx.defineExtension("head-support", {
        init: function(apiRef) {
            // store a reference to the internal API.
            api = apiRef;

            htmx.on('htmx:afterSwap', function(evt){
                const serverResponse = evt.detail.xhr.response;
                if (api.triggerEvent(document.body, "htmx:beforeHeadMerge", evt.detail)) {
                    mergeHead(serverResponse, evt.detail.boosted ? "merge" : "append");
                }
            })

            htmx.on('htmx:historyRestore', function(evt){
                if (api.triggerEvent(document.body, "htmx:beforeHeadMerge", evt.detail)) {
                    if (evt.detail.cacheMiss) {
                        mergeHead(evt.detail.serverResponse, "merge");
                    } else {
                        mergeHead(evt.detail.item.head, "merge");
                    }
                }
            })

            htmx.on('htmx:historyItemCreated', function(evt){
                const historyItem = evt.detail.item;
                historyItem.head = document.head.outerHTML;
            })
        }
    });
})()