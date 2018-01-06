if (document.referrer == "") {
    document.location = location.href.match(/^(http:\/\/[^\/]+\/[^\/]+)\//)[1] + "/index";
}
DisabledComponent = Class.create({
    initialize : function() {
        var component = $('transactionType');
        if (component.value != "") {
            component.disabled = true;
            $('billerCode').disabled = true;
            $('billerName').activate();
        } else {
        	$('transactionType').activate();
        }
    }
})