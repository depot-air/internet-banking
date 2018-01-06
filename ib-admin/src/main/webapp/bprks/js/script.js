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
});

function popupInfo(doc) {
	var width = 650;
	var height = 590;	//450
	var left = parseInt((screen.availWidth/2) - (width/2));
	var top = parseInt((screen.availHeight/2) - (height/2));
	var windowPos = "width=" + width + ",height=" + height + ",status,resizable,left=" + left + ",top=" + top + "screenX=" + left + ",screenY=" + top;
	var infoWin;
	infoWin = window.open(IB.fn.contextPath()+ doc,'popupinfodoc',windowPos + 'menubar=0,status=0,scrollbars=1,toolbar=0,location=0');
	infoWin.focus();
}  

