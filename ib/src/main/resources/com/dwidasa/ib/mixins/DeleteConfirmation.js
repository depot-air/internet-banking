DeleteConfirmation = Class.create( {
	initialize : function(confirmDelete, needCheck) {
		Event.observe($("delete"), 'click', this.doConfirmDelete.bindAsEventListener(this, confirmDelete, needCheck));
	},
	
	doConfirmDelete : function(e, confirmDelete, needCheck) {
		if (!$('form').getInputs('checkbox').pluck('checked').any()) {
			alert(needCheck);
			e.stop();
			return;
		}
		if (!confirm(confirmDelete)) {
			e.stop();
			return;
		}
	}
} );