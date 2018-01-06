function populateValue(json) {
    var parentWindow = window.parent;
    if (parentWindow != null) {
        if (parentWindow.closeTransferPopup != null &&
                typeof(parentWindow.closeTransferPopup) == "function") {
            parentWindow.closeTransferPopup(json);
        }
    }
}

function processSelection(param) {
    populateValue(param);
}
