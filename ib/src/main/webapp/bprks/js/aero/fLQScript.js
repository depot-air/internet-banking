var urlTicket;

jQuery(document).ready(function ($) {
    'use strict';
    var hiddenJsonAeroView = $('#hiddenJsonAeroView').val();
    var objAeroView = JSON && JSON.parse(hiddenJsonAeroView) || $.parseJSON(hiddenJsonAeroView);

    var hiddenJsonDepartFlight = $('#hiddenJsonDepartFlight').val();
    var objDepartFlight = JSON && JSON.parse(hiddenJsonDepartFlight) || $.parseJSON(hiddenJsonDepartFlight);
    var hiddenJsonReturnFlight = $('#hiddenJsonReturnFlight').val();
    var objReturnFlight = JSON && JSON.parse(hiddenJsonReturnFlight) || $.parseJSON(hiddenJsonReturnFlight);

    urlTicket = $('#urlTicket').val();

    $( '#keberangkatan tr:even, #kembali tr:even' ).css( 'background-color', '#FFFFFF');

    console.log("$('#hiddenDepartFrom').val()=" + $('#hiddenDepartFrom').val());
    $('#departFrom').html($('#hiddenDepartFrom').val());
    $('#departTo').html($('#hiddenDepartTo').val());
    console.log("$('#departFrom').innerHTML=" + $('#departFrom').innerHTML);

    var departureConnecting = objDepartFlight.aeroConnectingFlight;
    console.log("departureConnecting=" + departureConnecting);
    if (departureConnecting) {
        $('#departConnectingFrom').html($('#hiddenDepartConnectingFrom').val());
        $('#departConnectingTo').html($('#hiddenDepartConnectingTo').val());
    }
    else {

    }

    var departureConnecting2 = objDepartFlight.aeroConnectingFlight2;
    console.log("departureConnecting2=" + departureConnecting2);

    if (departureConnecting2) {
        $('#departConnecting2From').html($('#hiddenDepartConnecting2From').val());
        $('#departConnecting2To').html($('#hiddenDepartConnecting2To').val());
    }
    else {

    }

////------------------------------------------------------------------------------


    var departPlane = "maskLogo " + getLogo(objDepartFlight.airlineId);

    $('#departLogo').addClass(departPlane);

    if (objReturnFlight) {
        console.log("objReturnFlight.bookInfo=" + objReturnFlight.bookInfo);

        $('#returnFrom').html($('#hiddenReturnFrom').val());
        $('#returnTo').html($('#hiddenReturnTo').val());

        var returnPlane = "maskLogo " + getLogo(objReturnFlight.airlineId);
        $('#returnLogo').addClass(returnPlane);

        var returnConnecting = objReturnFlight.aeroConnectingFlight;
           console.log("returnConnecting=" + returnConnecting);

           if (returnConnecting) {
               $('#returnConnectingFrom').html($('#hiddenReturnConnectingFrom').val());
               $('#returnConnectingTo').html($('#hiddenReturnConnectingTo').val());

           } else {

           }

           var returnConnecting2 = objReturnFlight.aeroConnectingFlight2;
           console.log("returnConnecting2=" + returnConnecting2);
           if (returnConnecting2) {
               $('#returnConnecting2From').html($('#hiddenReturnConnecting2From').val());
               $('#returnConnecting2To').html($('#hiddenReturnConnecting2To').val());
           } else {

           }
    }

    function getLogo(airlineId) {
   		var logo = "";
           /*
           1;"QZ";"Airasia"
           2;"Y6";"Batavia"
           3;"QG";"Citilink"
           4;"GA";"Garuda"
           5;"MZ";"Merpati"
           6;"SJ";"Sriwijaya"
           7;"JT";"Lionair"
           8;"KD";"Kalstar"
           9;"GA";"Garuda Executive"
           11;"IR";"tiger"
            */
   		if (airlineId == "1" || airlineId == "QZ") logo = "airLine1";
   		else if (airlineId == "2" || airlineId == "Y6") logo = "airLine2";
   		else if (airlineId == "3" || airlineId == "QG") logo = "airLine3";
   		else if (airlineId == "4" || airlineId == "GA") logo = "airLine4";
   		else if (airlineId == "5" || airlineId == "MZ") logo = "airLine5";
   		else if (airlineId == "6" || airlineId == "SJ") logo = "airLine6";
   		else if (airlineId == "7" || airlineId == "JT") logo = "airLine7";
   		else if (airlineId == "8" || airlineId == "KD") logo = "airLine8";
   		else if (airlineId == "9" || airlineId == "GA") logo = "airLine9";
   		else if (airlineId == "10" || airlineId == "XX") logo = "airLine10";
   		else if (airlineId == "11" || airlineId == "IR") logo = "airLine11";
   		return logo;
   	}

    //20 03 2014 09:40:00 -> Thu Apr 10 2014 06:10:00 GMT+0700 (SE Asia Standard Time)
    function toJavascriptDate(datetime) {

      var partsDate = datetime.split(' ')

      var day = partsDate[0];
      var month = partsDate[1];
      var year = partsDate[2];

      month = month - 1;
      if (month.length < 10) month = "0" + month;
      if (day.length < 10) day = "0" + day;

      var vTime = partsDate[3];
      var partsTime = vTime.split(':');
      var hour = partsTime[0];
      var minuite = partsTime[1];
      if (hour.length < 10) hour = "0" + hour;
      if (minuite.length < 10) minuite = "0" + minuite;

      var newDate = new Date(year, month, day, hour, minuite, 0, 0);

      return newDate;
  }

    //Thu Apr 10 2014 06:10:00 GMT+0700 (SE Asia Standard Time) -> 06:10
    function hhmm(datetime) {

        var hour = datetime.getHours();
        var minuite = datetime.getMinutes();

        var shour = hour.toString();
        var sminuite = minuite.toString();
        if (hour < 10) shour = "0" + shour;
        if (minuite < 10) sminuite = "0" + sminuite;
        return  shour + ":" + sminuite;
    }

    function ddMMMyyyy(datetime) {
        var month = new Array();
        month[0] = "January";
        month[1] = "February";
        month[2] = "March";
        month[3] = "April";
        month[4] = "May";
        month[5] = "June";
        month[6] = "July";
        month[7] = "August";
        month[8] = "September";
        month[9] = "October";
        month[10] = "November";
        month[11] = "December";

        var dt = datetime.getDate();
        var month = month[datetime.getMonth()];
        var year = datetime.getFullYear();
        console.log("date month year=" + dt + " " + month + " " + year);
        return  dt + " " + month + " " + year;
    }

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

    $("a#changeme").attr('href', urlTicket);

    console.log("objAeroView.total=" + objAeroView.total);
    console.log("objAeroView.assuranceType=" + objAeroView.assuranceType + " - " + objAeroView.assurancePrice);
    if (objAeroView.assuranceType) {
        $('#ticketPrice').html(formatRupiah(objAeroView.total - objAeroView.assurancePrice));
        $('#insurancePrice').html(formatRupiah(objAeroView.assurancePrice));
    }
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
