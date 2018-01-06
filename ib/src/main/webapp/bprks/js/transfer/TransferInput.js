var IB;
if (!IB) IB = {};
if (!IB.transferInput) IB.transferInput = {};
if (!IB.transferInput.fn) IB.transferInput.fn = {};

IB.transferInput.fn.instrumentizeControls = (function($jq) {
    return function() {
        $jq("#periodicTransfer").bind("click", function() {
            $jq("#every").prop("checked", true);
        });
        $jq("#transferNow,#transferOnDate").bind("click", function() {
            $jq("#every").prop("checked", false);
            $jq("#everyDay").prop("checked", false);
            $jq("#everyDate").prop("checked", false);

        });
        $jq("#every, #everyDay, #everyDate").bind("click", function() {
            $jq("#periodicTransfer").prop("checked", true);
        });
    }
})(jQuery);

jQuery(document).ready(function($) {
    IB.transferInput.fn.instrumentizeControls();
});

// -- validators -- //

function checkTransferOnDate() {
    return jQuery("#transferOnDate").attr("checked");
}

function checkInputAccount() {
    return jQuery("#inputAccount").attr("checked");
}

function checkTransferList() {
    return jQuery("#transferList").attr("checked");
}

function checkEvery() {
    return jQuery("#every").attr("checked");
}

function checkEveryDate() {
    return jQuery("#everyDate").attr("checked");
}

function checkPeriodicTransfer() {
    return jQuery("#periodicTransfer").attr("checked");
}

function validateAccount() {
    if (jQuery("#ownAccount").attr("checked")) {
        var firstAcc = jQuery("select[id^=accountNumber]").val();
        var secondAcc = jQuery("select[id^=ownAccountSelect]").val();

        if (firstAcc == secondAcc) {
            return false;
        }
    }
    return true;
}