var IB;
if (!IB) IB = {};
if (!IB.fn) IB.fn = {};

IB.fn.showDialog = function(url, params) {
    var width = params["width"] != null ? params["width"] : 550;
    var height = params["height"] != null ? params["height"] : 320;

    var divStr = '<div id="validationDialog" >' +
            '<iframe frameborder="no" border="0" src="' + url + '" width="' + width +
            '" height="' + height + '" style = " overflow:hidden; "  ></iframe>' +
            '</div>';

    Modalbox.show(divStr, params);
};

IB.fn.contextPath = function() {
    return location.href.match(/^(http:\/\/[^\/]+\/[^\/]+)\//)[1];
};


