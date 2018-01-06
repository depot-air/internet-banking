if (document.referrer == "") {
    document.location = location.href.match(/^(http:\/\/[^\/]+\/[^\/]+)\//)[1] + "/index";
}

DisabledComponent = Class.create({
    initialize : function() {
        var component = $('transactionType');
        if (component.value != "") {
            component.disabled = true;
            $('description').activate();
        } else {
        	$('transactionType').activate();
        }
    }
})