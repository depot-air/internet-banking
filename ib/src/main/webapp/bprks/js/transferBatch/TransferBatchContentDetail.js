//var IB;
//if (!IB) IB = {};
//if (!IB.transferInput) IB.transferInput = {};
//if (!IB.transferInput.fn) IB.transferInput.fn = {};
//
//IB.transferInput.fn.instrumentizeControls = (function($jq) {
//    return function() {
//        $jq("#periodicTransfer").bind("click", function() {
//            $jq("#every").prop("checked", true);
//        });
//        $jq("#transferNow,#transferOnDate").bind("click", function() {
//            $jq("#every").prop("checked", false);
//            $jq("#everyDay").prop("checked", false);
//            $jq("#everyDate").prop("checked", false);
//
//        });
//        $jq("#every, #everyDay, #everyDate").bind("click", function() {
//            $jq("#periodicTransfer").prop("checked", true);
//        });
//    }
//})(jQuery);
//
//jQuery(document).ready(function($) {
//    IB.transferInput.fn.instrumentizeControls();
//});
//
// -- validators -- //

function checkInputAccount() {
    return jQuery("#inputAccount").attr("checked");
}

function checkTransferList() {
    return jQuery("#transferList").attr("checked");
}


AutoChecker = Class.create( {
	initialize : function() {
		Event.observe($("transferListSelect"), 'click', this.doCheckTransferListInput.bindAsEventListener(this));
		Event.observe($("inputAccountField"), 'click', this.doCheckInputAccountInput.bindAsEventListener(this));
	},
	
	doCheckTransferListInput : function(e) {
		$("transferList").checked = true;
	},
	
	doCheckInputAccountInput : function(e) {
		$("inputAccount").checked = true;
	},
	
} );