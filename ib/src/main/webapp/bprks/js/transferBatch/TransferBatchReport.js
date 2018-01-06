AutoChecker = Class.create( {
	initialize : function() {
		Event.observe($("startDate"), 'click', this.doCheckHistory.bindAsEventListener(this));
		Event.observe($("startDate-trigger"), 'click', this.doCheckHistory.bindAsEventListener(this));
		Event.observe($("endDate"), 'click', this.doCheckHistory.bindAsEventListener(this));
		Event.observe($("endDate-trigger"), 'click', this.doCheckHistory.bindAsEventListener(this));
	},
	
	doCheckHistory : function(e) {
		$("historyRadio").checked = true;
	},
} );