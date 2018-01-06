if (document.referrer == "") {
    document.location = location.href.match(/^(http:\/\/[^\/]+\/[^\/]+)\//)[1] + "/index";
}
DisabledComponent = Class.create({
    initialize : function() {
        var component = $('providerCode');
        if (component.value != "") {
        	component.disabled = true;
        	$('providerName').activate();
        }  else {
        	$('providerCode').activate();
        }
    }
})