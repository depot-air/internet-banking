var AutoUpdateZone = Class.create();
AutoUpdateZone.prototype = {
	initialize: function(dataholder, datadisplay, url) {
		var updater = new Ajax.PeriodicalUpdater(dataholder, url, {
			method: 'get',
			asynchronous:true,
			frequency:10, // auto update every 10 seconds
			onLoaded: function() {
				$(dataholder).style.display = 'none';
			},
			onSuccess: function(transport) {
				var data = $(dataholder).innerHTML.evalJSON();
				if (data.content != $(datadisplay).innerHTML ) {
					$(datadisplay).innerHTML = data.content;
					new Effect.Highlight($(datadisplay), {
						startcolor: '#ffff99',
						endcolor: '#ffffff',
						restorecolor: '#ffffff'
					} );
				}	//endif
				if (data.stop == 1) {
					updater.stop();
					$('help').style.display = 'none';
				}
			}  //end onSuccess function
		} );  //end updater
		updater.start();
	}    //end init
}
