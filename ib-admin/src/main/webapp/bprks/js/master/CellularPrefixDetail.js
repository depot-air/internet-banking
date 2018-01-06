if (document.referrer == "") {
    document.location = location.href.match(/^(http:\/\/[^\/]+\/[^\/]+)\//)[1] + "/index";
}
DisabledComponent = Class.create({
            initialize : function() {
                var component = $('productName');
                var prefix = $('prefix');
                if (prefix.value != "") {
                    component.disabled = true;
                    $('transactionType').disabled = true;
                    $('prefix').activate();
                } else {
                	$('productName').activate();
                }
            }
        })