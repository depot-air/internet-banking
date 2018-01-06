if (document.referrer == "") {
    document.location = location.href.match(/^(http:\/\/[^\/]+\/[^\/]+)\//)[1] + "/index";
}

DisabledComponent = Class.create({
    initialize : function() {
        var component = $('responseCode');
        if (component.value != "") {
            component.disabled = true;
            $('actionCode').activate();
        } else {
        	$('responseCode').activate();
        }
    }
})