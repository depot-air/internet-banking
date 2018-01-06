if (document.referrer == "") {
    document.location = location.href.match(/^(http:\/\/[^\/]+\/[^\/]+)\//)[1] + "/index";
}
DisabledComponent = Class.create({
            initialize : function() {
                var component = $('currencyCode');
                if (component.value != "") {
                    component.disabled = true;
                    $('swiftCode').activate();
                } else {
                	$('currencyCode').activate();
                }
            }
        })