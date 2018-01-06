function populateValue(json) {
    var parentWindow = window.parent;
    if (parentWindow != null) {
        if (parentWindow.closeReceiverBankPopup != null &&
                typeof(parentWindow.closeReceiverBankPopup) == "function") {
            parentWindow.closeReceiverBankPopup(json);
        }
    }
}

function processSelection(param) {
    populateValue(param);
}