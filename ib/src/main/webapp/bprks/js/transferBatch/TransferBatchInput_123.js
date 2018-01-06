function checkTransferOnDate() {
    return jQuery("#transferOnDate").attr("checked");
}

function checkEvery() {
    return jQuery("#every").attr("checked");    
}

function checkEveryDay() {
    return jQuery("#everyDay").attr("checked");    
}

function checkEveryDate() {
    return jQuery("#everyDate").attr("checked");    
}

function checkEndDate() {
    return jQuery("#periodicTransfer").attr("checked");    
}

AutoChecker = Class.create( {
	initialize : function() {
		Event.observe($("transferDate"), 'click', this.doCheckTransferOnDate.bindAsEventListener(this));
		Event.observe($("transferDate-trigger"), 'click', this.doCheckTransferOnDate.bindAsEventListener(this));
		
		Event.observe($("everyField"), 'click', this.doCheckEvery.bindAsEventListener(this));
		Event.observe($("every"), 'click', this.doCheckEvery.bindAsEventListener(this));

		Event.observe($("day"), 'change', this.doCheckEveryDay.bindAsEventListener(this));
		Event.observe($("everyDay"), 'click', this.doCheckEveryDay.bindAsEventListener(this));
		
		Event.observe($("everyDateField"), 'click', this.doCheckEveryDate.bindAsEventListener(this));
		Event.observe($("everyDate"), 'click', this.doCheckEveryDate.bindAsEventListener(this));
		
		Event.observe($("endDate"), 'click', this.doCheckEndDate.bindAsEventListener(this));
		Event.observe($("endDate-trigger"), 'click', this.doCheckEndDate.bindAsEventListener(this));
		
	},
	
	doCheckTransferOnDate : function(e) {
		$("transferOnDate").checked = true;
	},
	
	doCheckEvery : function(e) {
		$("periodicTransfer").checked = true;
		$("every").checked = true;		
	},

	doCheckEveryDay : function(e) {
		$("periodicTransfer").checked = true;
		$("everyDay").checked = true;		
	},
	
	doCheckEveryDate : function(e) {
		$("periodicTransfer").checked = true;
		$("everyDate").checked = true;		
	},

	doCheckEndDate : function(e) {
		$("periodicTransfer").checked = true;
	}
	
} );