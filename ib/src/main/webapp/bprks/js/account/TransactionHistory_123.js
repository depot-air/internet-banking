
function myFunction() {
    return jQuery("#historyRadio").attr("checked");    
}

function checkHistoryFrom() {
    return jQuery("#historyRadio").attr("checked");    
}

AutoChecker = Class.create( {
	initialize : function() {		
		Event.observe($("startDate"), 'click', this.doCheckHistoryFrom.bindAsEventListener(this));
		Event.observe($("lblStartDate"), 'click', this.doCheckHistoryFrom.bindAsEventListener(this));
		Event.observe($("endDate"), 'click', this.doCheckHistoryFrom.bindAsEventListener(this));
		Event.observe($("lblEndDate"), 'click', this.doCheckHistoryFrom.bindAsEventListener(this));

	},

	doCheckHistoryFrom : function(e) {
		$("historyRadio").checked = true;
	},
	
} );