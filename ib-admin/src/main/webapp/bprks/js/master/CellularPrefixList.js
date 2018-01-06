AutoChecker = Class.create( {
	initialize : function() {
		Event.observe($("inputPrefix"), 'click', this.doCheckInputSearch.bindAsEventListener(this));
		Event.observe($("transactionType"), 'click', this.doCheckSelectSearch.bindAsEventListener(this));
		Event.observe($("productName"), 'click', this.doCheckSelectSearch.bindAsEventListener(this));
		
	},
	
	doCheckInputSearch : function(e) {
		$("inputSearch").checked = true;
	},
	
	doCheckSelectSearch : function(e) {
		$("selectSearch").checked = true;
	},
	
} );


function checkInputSearch() {
	return $('inputSearch').checked;
}