import uniqid from 'uniqid';

export default class BrowserTab {
    private readonly sessionStorageKey: string;
    private readonly sessionStorageKey_parentTab: string;
    private gotResponse = false;
    private channel: BroadcastChannel | null = null;

    /**
     * A callback that is called with the tabId, isTabNew and parentTabId,
     * after the Broadcast Channel communication is done.
     */
    callback!: (param: BrowserTabIdCallback) => {};
    /**
     * The duration in milliseconds that we will wait for all the other
     * tabs to respond back when asked for search.
     */
    WAIT_TIMEOUT = 1000;

    constructor(appId: string = "client") {
        this.init = this.init.bind(this);
        this.removeTabId = this.removeTabId.bind(this);

        this.sessionStorageKey = `unique-tabid-${appId}`;
        this.sessionStorageKey_parentTab = `unique-tabid-${appId}_parent`;
        this.channel = new BroadcastChannel(`unique-tabid-broadcast-channel-${appId}`);
        this.channel.onmessage = (msg) => {
            const type = msg && msg.data && msg.data.type;
            let tabId = msg && msg.data && msg.data.tabId;

            if (type === "SEARCH") {
                if (window.sessionStorage.getItem(this.sessionStorageKey) === tabId) {
                    this.channel!.postMessage({
                        type: "FOUND",
                        tabId
                    })
                }
            } else if (type === "FOUND") {
                if (window.sessionStorage.getItem(this.sessionStorageKey) === tabId) {
                    this.gotResponse = true;
                    const parentTabId = tabId;
                    tabId = uniqid();
                    window.sessionStorage.setItem(this.sessionStorageKey_parentTab, parentTabId);
                    window.sessionStorage.setItem(this.sessionStorageKey, tabId);
                    this.callback({tabId, isTabNew: true, parentTabId});
                }
            }
        };
    }

    removeTabId() {
        window.sessionStorage.removeItem(this.sessionStorageKey);
    }

    init() {
        let tabId = window.sessionStorage.getItem(this.sessionStorageKey);
        if (tabId === null) {
            tabId = uniqid();
            window.sessionStorage.setItem(this.sessionStorageKey, tabId);
            this.callback({tabId, isTabNew: true, parentTabId: null});
        } else {
            this.gotResponse = false;
            this.channel!.postMessage({
                type: "SEARCH",
                tabId
            });
            setTimeout(() => {
                if (!this.gotResponse) {
                    const parentTabId = window.sessionStorage.getItem(this.sessionStorageKey_parentTab);
                    this.callback({tabId: tabId!, isTabNew: false, parentTabId});
                }
            }, this.WAIT_TIMEOUT);
        }
    }
}

export interface BrowserTabIdCallback {
    tabId: string,
    isTabNew: boolean,
    parentTabId: string | null
}