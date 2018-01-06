if (document.referrer == "") {
    document.location = location.href.match(/^(http:\/\/[^\/]+\/[^\/]+)\//)[1] + "/index";
}
DisabledComponent = Class.create({
            initialize : function() {
                var component = $('providerName');
                var price = $('price');
                if (price.value != "") {
                    component.disabled = true;
                    $('price').activate();
                } else {
                	$('providerName').activate();
                }
            }
        })