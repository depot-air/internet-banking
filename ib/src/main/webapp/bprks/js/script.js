jQuery(document).ready(function($) {
	$("#mm_list").tabs(".panel", {tabs: 'h3', effect: 'slide', initialIndex: null});
	$.tools.tabs.addEffect("slide", function(i, done) {
	this.getPanes().slideUp().css({backgroundColor: "none"});
	this.getPanes().eq(i).slideDown(function()  {
			$(this).css({backgroundColor: 'transparent'});
			done.call();
		});
	});
	$("h3").click(function(){
		$(this).parent().find("h3 span").removeClass("arrowopen").addClass("arrowclose");
		$(this).find("span").removeClass("arrowclose").addClass("arrowopen");
	});
	
	$('input.toggleCheckAll:checkbox').toggle(function(){
        $('input:checkbox').attr('checked','checked');
        $(this).val('uncheck all');
    },function(){
        $('input:checkbox').removeAttr('checked');
        $(this).val('check all');        
    })
	
	var dateTime = 
	{
	    "format": "%A, %D %B %Y - %H:%M:%S",
	    "seedTime" : parseInt($("#serverTime").html())
	};	
    $('.date_time').jclock(dateTime);
});

function popupInfo(doc) {
	var width = 750;
	var height = 590;	//450
	var left = parseInt((screen.availWidth/2) - (width/2));
	var top = parseInt((screen.availHeight/2) - (height/2));
	var windowPos = "width=" + width + ",height=" + height + ",status,resizable,left=" + left + ",top=" + top + "screenX=" + left + ",screenY=" + top;
	var infoWin;
	infoWin = window.open(IB.fn.contextPath()+ doc,'popupinfodoc',windowPos + 'menubar=0,status=0,scrollbars=1,toolbar=0,location=0');
	infoWin.focus();
}  
function confirmDelete(strConfirm, strNeedCheck) {
	if (!$('form').getInputs('checkbox').pluck('checked').any()) {
		alert(strNeedCheck);
		return false;
	}
	return confirm(strConfirm);
}

function testFunc(idle) {
	now = new Date().getTime();	
	if ((now - idle) >= 5000) {
		window.location.href = IB.fn.contextPath() + '/login?err=screenTimeout';
	} else {
		checkTimeout.setTimer();
	}
}
var ScreenTimeout = Class.create({
	_events: [[window, 'scroll'], [window, 'resize'], [document, 'mousemove'], [document, 'keydown']],
	_timer: null,

	initialize: function(time) {
		this.time = time;

		this.initObservers();
		this.setTimer();
	},

	initObservers: function() {
		this._events.each(function(e) {
			Event.observe(e[0], e[1], this.onInterrupt.bind(this))
		}.bind(this))
	},

	onInterrupt: function() {
		this.setTimer();
	},		
	
	setTimer: function() {
		clearTimeout(this._timer);
		startIdle = new Date().getTime();
		this._timer = setTimeout(function() {testFunc(startIdle)}, this.time);
	}
})

// Extend the Tapestry.Initializer with a static method that instantiates us
Tapestry.Initializer.screenTimeout = function(spec) {
    checkTimeout = new ScreenTimeout(spec.time);
};
