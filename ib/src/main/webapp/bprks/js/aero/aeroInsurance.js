
var hiddenAeroTicketingView, objAeroTiketingView;
var adultPriceDepart = "", childPriceDepart = "", infantPriceDepart = "", insurancePriceDepart = "", totalPriceDepart = "";
var adultPriceReturn = "", childPriceReturn = "", infantPriceReturn = "", insurancePriceReturn = "", totalPriceReturn = "";


jQuery(document).ready(function ($) {
    hiddenAeroTicketingView = $('#hiddenAeroTicketingView').val();
    console.log("hiddenAeroTicketingView=" + hiddenAeroTicketingView);
    objAeroTiketingView = JSON && JSON.parse(hiddenAeroTicketingView) || $.parseJSON(hiddenAeroTicketingView);
    console.log("objAeroTiketingView=" + objAeroTiketingView);
/*
    var departureFlight = objAeroTiketingView.departureFlight;

    if (null != departureFlight.adultPassengerSummary) {
        adultPriceDepart = "<p class='priceCtr'><span class='passNum'>" + objAeroTiketingView.totalAdult + "</span> Dewasa <span class='passPrice'>" + formatRupiah(departureFlight.adultPassengerSummary.total) + "</span></p>";
    }
    if (null != departureFlight.childPassengerSummary && objAeroTiketingView.totalChildren > 0) {
        childPriceDepart = "<p class='priceCtr'><span class='passNum'>" + objAeroTiketingView.totalChildren + "</span> Anak-anak <span class='passPrice'>" + formatRupiah(departureFlight.childPassengerSummary.total) + "</span></p>";
    }
    if (null != departureFlight.infantPassengerSummary && objAeroTiketingView.totalInfant > 0) {
        infantPriceDepart = "<p class='priceCtr'><span class='passNum'>" + objAeroTiketingView.totalInfant + "</span> Bayi <span class='passPrice'>" + formatRupiah(departureFlight.infantPassengerSummary.total) + "</span></p>";
    }
    totalPriceDepart = "<p class='priceCtr'>Total<span class='passPrice'>" + formatRupiah(departureFlight.total) + "</span></p>";
    $('#dFPriceDetail').html(adultPriceDepart + childPriceDepart + infantPriceDepart + totalPriceDepart);
    $('#dFPriceTitle').show();  $('#dFPriceDetail').show();
*/

    hidingComponents($);

    $('#allInsurance').hide();
    $("#noAssurance, #yesAssurance, #yesMudik, #yesTravel").change(function () {
        console.log("$(this).attr('id')=" + $(this).attr('id'));
        if ($(this).attr('id') === "noAssurance") {
            console.log("no assurance is selected");
            $('#allInsurance').hide();
            $('#totalPrice').html(formatRupiah(objAeroTiketingView.total));
            objAeroTiketingView.assuranceType = null;

        } else {
            console.log("yes assurance is selected");
            objAeroTiketingView.assuranceType = "SIMAS MUDIK";

            console.log("objAeroTiketingView.totalAdult + objAeroTiketingView.totalChildren + objAeroTiketingView.totalInfant=" + (objAeroTiketingView.totalAdult + objAeroTiketingView.totalChildren + objAeroTiketingView.totalInfant));
            var totalPass = objAeroTiketingView.totalAdult + objAeroTiketingView.totalChildren + objAeroTiketingView.totalInfant;
            console.log("totalPass=" + totalPass);
            var insuranceValue = totalPass * 10000;

            if (objAeroTiketingView.providerCode === "AERO") {
                if ($(this).attr('id') === "yesTravel") {
                    objAeroTiketingView.assuranceType = "SIMAS TRAVEL";
                    insuranceValue = totalPass * 15000;
                }
            }

            $('#allInsurance').show();
            $('#ticketPrice').html(formatRupiah(objAeroTiketingView.total));
            if (!objAeroTiketingView.isDepartOnly) {
                objAeroTiketingView.assurancePrice = 2 * insuranceValue;
                objAeroTiketingView.total = objAeroTiketingView.total + 2 * insuranceValue;
            } else {
                objAeroTiketingView.assurancePrice = insuranceValue;
                objAeroTiketingView.total = objAeroTiketingView.total + insuranceValue;
            }
            $('#insurancePrice').html(formatRupiah(objAeroTiketingView.assurancePrice));
            $('#totalPrice').html(formatRupiah(objAeroTiketingView.total));
        }
        hiddenAeroTicketingView  = JSON.stringify(objAeroTiketingView);
        $('#hiddenAeroTicketingView').val(hiddenAeroTicketingView);

    });

    function formatRupiah(numOri) {
           var num  = numOri;
           num += '';
           //num  = num.replace(/,/g, '.');
           num = num.split(',').join('.');

           var p = parseFloat(num).toFixed(2).split(".");

           return p[0].split("").reverse().reduce(
               function(acc, num, i, orig) {
                   return  num + (i && !(i % 3) ? "." : "") + acc;
               }, "");
   	}


});

function hidingComponents($) {
    if (objAeroTiketingView.providerCode == "VOLT") {
        $('#aeroDepartPrice').hide();
        $('#aeroReturnPrice').hide();
    }
 }
