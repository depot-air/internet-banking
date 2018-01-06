if (document.referrer == "") {
    document.location = location.href.match(/^(http:\/\/[^\/]+\/[^\/]+)\//)[1] + "/index";
}

DisabledComponent = Class.create({
    initialize : function() {
        var component = $('parameterName');
        if (component.value != "") {
            component.disabled = true;
            $('parameterValue').activate();
        } else {
        	$('parameterName').activate();
        }
    }
})