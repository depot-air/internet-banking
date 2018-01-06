if (document.referrer == "") {
    document.location = location.href.match(/^(http:\/\/[^\/]+\/[^\/]+)\//)[1] + "/index";
}

DisabledComponent = Class.create({
            initialize : function() {
                var component = $('accountType');
                if (component.value != "") {
                    component.disabled = true;
                    $('accountName').activate();
                } else {
                	$('accountType').activate();
                }
            }
        })