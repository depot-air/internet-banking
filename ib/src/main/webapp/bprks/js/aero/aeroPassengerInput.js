jQuery(document).ready(function ($) {
    'use strict';
    var hiddenCustReg = $('#hiddenCustReg').val();
    console.log("pasengerInput hiddenCustReg=" + hiddenCustReg);
    var objCustRegs = JSON && JSON.parse(hiddenCustReg) || $.parseJSON(hiddenCustReg);

    $( ".passengerSave" ).change(function() {
        console.log("$(this).val()=" + $(this).val());
        var varthis = $(this).val();
        $.each(JSON.parse(hiddenCustReg), function(idx, obj) {
            console.log("obj.customerReference=" + obj.customerReference);
            var varcustref = obj.customerReference;
            if (varthis == obj.customerReference) {
                var partsArray = obj.customerReference.split(',');
                var lastName = partsArray[0];
                var firstName = partsArray[1];

                $("#firstName").val(firstName);
                $("#lastName").val(lastName);
                $("#title").val(obj.data3);

                $("#idCard").val(obj.data4);

                partsArray = obj.data5.split(',');
                console.log("dob=" + partsArray[0]);
                $("#dob").val(partsArray[0]);
                $("#country").val(partsArray[1]);
            }

        });
        $("#fromList").val(varthis);
    });
});