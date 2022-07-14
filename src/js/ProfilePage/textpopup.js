/**
 * show() - Shows the popup
 */
export function show() {
    this.isShown = true;
}

/**
 * hide() - Hides the popup
 */
export function hide() {
    this.isShown = false;
}

/**
 * executeCallback() - Executes the callback
 */
export function executeCallback() {
    this.callback()
}