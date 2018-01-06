var isRequest = false; //bool
var isAlreadySelectDepart = false;
var isAlreadySelectReturn = false;

var objDepart, selectedDepart;
var objReturn, selectedReturn;
var hiddenServerIp;
var hiddenAeroTicketingView, objAeroTiketingView;
var maxList, ListHeightAllowed, departMaxListHeight, returnMaxListHeight;

var arrDeparts = [];
var arrReturns = [];

function defineSelectFlightDepartClick($) {
    $('#departList .fLTable3').bind('click', function () {
        isAlreadySelectDepart = true;
        console.log("click other ticket depart");
        $('#departList table tr').removeClass('active');
        $('#departList .fLTable3').removeClass('active customClass');
        console.log("$(this)=" + $(this));
        $(this).addClass('active');
        $(this).parent().addClass('active');
        $(this).find('.cBFlight').prop('checked', 'checked');
        updateRightDetailDepart($);
    });
}

function defineSelectFlightReturnClick($) {
    $('#returnList .fLTable3').bind('click', function () {
        isAlreadySelectReturn = true;
        $('#returnList table tr').removeClass('active');
        $('#returnList .fLTable3').removeClass('active customClass');
        $(this).addClass('active');
        $(this).parent().addClass('active');
        $(this).find('.cBFlight').prop('checked', 'checked');
        updateRightDetailReturn($);
    });
}

function defineShowOtherClassDepart($) {
    $('#departList .flightClassOption').click(function () {
        var defPrice = $(this).find('li:first .valueClassPrice').html();
        $('.flightClassOption .classOptionPop').hide();
        $(this).find('.classOptionPop').show();
    });
}

function defineShowOtherClassReturn($) {
    $('#returnList .flightClassOption').click(function () {
        var defPrice = $(this).find('li:first .valueClassPrice').html();
        $('.flightClassOption .classOptionPop').hide();
        $(this).find('.classOptionPop').show();
    });
}

function defineSelectPopUpClassDepart($) {
    $('#departList .classOptionPop li').click(function () {
        var defClass = $(this).attr('class');
        if (defClass !== 'first') {
            var optionPrice = $(this).find('.valueClassPrice').html();
            $(this).parent().parent().parent().parent().addClass('customClass').find('.flightPrice').html(optionPrice);
            $(this).parent().parent().hide();
        } else {
            var optionPrice = $(this).find('.valueClassPrice').html();
            $(this).parent().parent().parent().parent().find('.flightPrice').html(optionPrice);
            $(this).parent().parent().hide();
        }
        var optionClassName = $(this).find('.className').html();
        var optionClassAero = $(this).find('.hiddenClassAero').html();
        //console.log("$(this).find('.hiddenClassAero').html()=" + $(this).find('.hiddenClassAero').html());
        $(this).parent().parent().parent().parent().find('.hiddenSelectedClass').html(optionClassName);
        $(this).parent().parent().parent().parent().find('.hiddenSelectedClassAero').html(optionClassAero);
        objDepart.selectedClassId = optionClassName;
        objDepart.selectedClass.classId = optionClassAero;
        //console.log("objDepart.selectedClass.classId=" + objDepart.selectedClass.classId);
        selectedDepart = JSON.stringify(objDepart);

        updateRightDetailDepart($);
        return false;
    });
}

function defineSelectPopUpClassReturn($) {
    $('#returnList .classOptionPop li').click(function () {
        var defClass = $(this).attr('class');
        if (defClass !== 'first') {
            var optionPrice = $(this).find('.valueClassPrice').html();
            $(this).parent().parent().parent().parent().addClass('customClass').find('.flightPrice').html(optionPrice);
            $(this).parent().parent().hide();
        } else {
            var optionPrice = $(this).find('.valueClassPrice').html();
            $(this).parent().parent().parent().parent().find('.flightPrice').html(optionPrice);
            $(this).parent().parent().hide();
        }
        var optionClassName = $(this).find('.className').html();
        //var optionClassAero = $(this).find('.hiddenClassAero').html();
        //console.log("$(this).find('.hiddenClassAero').html()=" + $(this).find('.hiddenClassAero').html());
        $(this).parent().parent().parent().parent().addClass('customClass').find('.hiddenSelectedClass').html(optionClassName);
        //$(this).parent().parent().parent().parent().find('.hiddenSelectedClassAero').html(optionClassAero);
        objReturn.selectedClassId = optionClassName;
        //objReturn.selectedClass.classId = optionClassAero;
        selectedReturn = JSON.stringify(objReturn);

        updateRightDetailReturn($);

        return false;
    });
}

function refreshRightDepart($) {
    var dFCData = $('#departList .active').find('.airlineLogo').attr('class');
    var dFCArr = dFCData.split(' '),
        dFLogo = dFCArr[1],
        dFCode = $('#departList .active').find('.flightCode').html(),
        dFFData = $('#departList .active').find('.departCodeTime').html(),
        dFFArr = dFFData.split('&nbsp;');
    //console.log("dFFData=" + dFFData);
    var dFFCode = dFFArr[0],
    //dFFTime = dFFArr[1].substring(3, dFFArr[1].length - 4),
        aFFData = $('#departList .active').find('.arriveCodeTime').html(),
        aFFArr = aFFData.split('&nbsp;'),
        aFFCode = aFFArr[0],
    //aFFTime = aFFArr[1].substring(3, aFFArr[1].length - 4),
        dFPData = $('#departList .active').find('.flightPrice').html(),
        dFPArr = dFPData.split(' '),
        dFPARs = parseInt(dFPArr[0].replace(/[^a-z0-9\s]/gi, '')),
        dClass = $('#departList .active').find('.hiddenSelectedClass').html(),
        dAirlineId = $('#departList .active').find('.hiddenSelectedAirlineId').html(),

        dConnecting = $('#departList .active').find('.hiddenConnecting').html(),
        dConnecting2 = $('#departList .active').find('.hiddenConnecting2').html(),
        dEtd = $('#departList .active').find('.hiddenEtd').html(),
        dEta = $('#departList .active').find('.hiddenEta').html(),
        hiddenCityFrom = $('#departList .active').find('.hiddenCityFrom').html(),
        hiddenCityTo = $('#departList .active').find('.hiddenCityTo').html();

    console.log("dEtd=" + dEtd + ", dEta=" + dEta);
    console.log("hhmm(toJavascriptDate(dEtd))=" + hhmm(toJavascriptDate(dEtd)));
    console.log("hhmm(toJavascriptDate(dEta))=" + hhmm(toJavascriptDate(dEta)));
    $('#departDetail').show();

    $('.departLogoAndCode .flightCode').html(dFCode);
    $('.departLogoAndCode .flightLogo').removeClass().addClass('flightLogo ' + dFLogo);
    $('#departFrom').html(dFFCode);     //$('.departFlow .departCityCode').html(dFFCode);
    $('#departTo').html(aFFCode);     //$('.departFlow .arriveCityCode').html(aFFCode);
    $('#departCityFrom').html(hiddenCityFrom);
    $('#departCityTo').html(hiddenCityTo);

    $('#departEtd').html(hhmm(toJavascriptDate(dEtd)));     //$('.departFlow .departTime').html(dFFTime);
    $('#departEta').html(hhmm(toJavascriptDate(dEta)));     //$('.departFlow .arriveTime').html(aFFTime);

    console.log("before selectedDepart=" + selectedDepart);
    console.log("objDepart.selectedClass.classId=" + objDepart.selectedClass.classId);
    console.log("dConnecting=" + dConnecting + " dConnecting2=" + dConnecting2);

    try {
        objDepart.aeroConnectingFlight = JSON && JSON.parse(dConnecting) || $.parseJSON(dConnecting);
        objDepart.isConnectingFlight = true;
    } catch(err) {
        objDepart.aeroConnectingFlight = null;
        objDepart.isConnectingFlight = false;
    }
    console.log("objDepart.aeroConnectingFlight=" + objDepart.aeroConnectingFlight + " objDepart.isConnectingFlight=" + objDepart.isConnectingFlight);

    try {
        objDepart.aeroConnectingFlight2 = JSON && JSON.parse(dConnecting2) || $.parseJSON(dConnecting2);
    } catch(err) {
        objDepart.aeroConnectingFlight2 = null;
    }

    objDepart.airlineId = dAirlineId;
    objDepart.selectedClassId = dClass;
    objDepart.selectedClass.classId = dClass;
    objDepart.departureAirportCode = dFFCode;
    objDepart.arrivalAirportCode = aFFCode;
    objDepart.flightNumber = dFCode;
    objDepart.flightDate = dEtd;
    objDepart.etd = dEtd;   //getDateConcate(objDepart.etd, dFFTime);
    objDepart.eta = dEta;   //getDateConcate(objDepart.eta, aFFTime);

    selectedDepart = JSON.stringify(objDepart);
    console.log("after selectedDepart=" + selectedDepart);


    if (objAeroTiketingView.providerCode === "AERO") {

    } else {

        var ticketPrice = "<p class='priceCtr priceTotal'>Tiket Keberangkatan <span class='passPrice'>" + dFPData + "</span></p>";
        console.log("ticketPrice=" + ticketPrice);
        $('#dFPriceDetail').html(ticketPrice);
    }


    try {
        var objConnecting = JSON && JSON.parse(dConnecting) || $.parseJSON(dConnecting);
        console.log("dConnecting=" + dConnecting);
        $('#departFlightCodeConnecting').show();
        $('#departConnecting').show();

        console.log("objConnecting=" + objConnecting);
        console.log("objConnecting.connectingFlightFrom=" + objConnecting.connectingFlightFrom);
        console.log("objConnecting.connectingFlightTo=" + objConnecting.connectingFlightTo);

        $('#departFlightCodeConnecting').html(objConnecting.connectingFlightFno);
        $('#departConnectingFrom').html(objConnecting.connectingFlightFrom);
        $('#departConnectingTo').html(objConnecting.connectingFlightTo);
        $('#departConnectingCityFrom').html(objConnecting.connectingFlightCityFrom);
        $('#departConnectingCityTo').html(objConnecting.connectingFlightCityTo);


        objDepart.departureAirportCode = dFFCode;
        objDepart.arrivalAirportCode = aFFCode;

        $('#departConnectingEtd').html(hhmm(toJavascriptDate(objConnecting.connectingFlightEtd)));
        $('#departConnectingEta').html(hhmm(toJavascriptDate(objConnecting.connectingFlightEta)));
    }
    catch (err) {
        $('#departFlightCodeConnecting').css("display", "none");
        $('#departConnecting').css("display", "none");
    }

    console.log("after if dConnecting");

    try {
        var objConnecting2 = JSON && JSON.parse(dConnecting2) || $.parseJSON(dConnecting2);
        $('#departConnecting2').show();

        console.log("objConnecting2=" + objConnecting2);
        console.log("objConnecting2.connectingFlightFrom=" + objConnecting2.connectingFlightFrom);
        console.log("objConnecting2.connectingFlightTo=" + objConnecting2.connectingFlightTo);

        $('#departFlightCodeConnecting2').html(objConnecting2.connectingFlightFno);
        $('#departFlightCodeConnecting2').show();
        $('#departConnecting2From').html(objConnecting2.connectingFlightFrom);
        $('#departConnecting2To').html(objConnecting2.connectingFlightTo);
        $('#departConnecting2CityFrom').html(objConnecting2.connectingFlightCityFrom);
        $('#departConnecting2CityTo').html(objConnecting2.connectingFlightCityTo);

        $('#departConnecting2Etd').html(hhmm(toJavascriptDate(objConnecting2.connectingFlightEtd)));
        $('#departConnecting2Eta').html(hhmm(toJavascriptDate(objConnecting2.connectingFlightEta)));
    }
    catch (err) {
        $('#departConnecting2').css("display", "none");
    }

    console.log("after if dConnecting2");

    objDepart.flightNumber = dFCode;
    objDepart.departureAirportCode = dFFCode;
    objDepart.arrivalAirportCode = aFFCode;
    console.log("objDepart.arrivalAirportCode=" + objDepart.arrivalAirportCode);

    objDepart.selectedClassId = dClass;
    console.log("objDepart.selectedClassId=" + objDepart.selectedClassId);
    selectedDepart = JSON.stringify(objDepart);
    console.log("selectedDepart=" + selectedDepart);
    $('#hiddenSelectedDepart').val(selectedDepart);
    console.log("objAeroTiketingView.providerCode=" + objAeroTiketingView.providerCode);
}

function updateRightDetailDepart($) {
    refreshRightDepart($);
    console.log("isRequest=" + isRequest + "  isAlreadySelectDepart=" + isAlreadySelectDepart + " isAlreadySelectReturn=" + isAlreadySelectReturn);
    if (objAeroTiketingView.providerCode === "AERO") {
        postPriceDetailDepart($);
    } else {
        if (isRequest == false && isAlreadySelectDepart && isAlreadySelectReturn) {
            if (objAeroTiketingView.isDepartOnly) {
                postVoltrasAirFare($);
            } else {
                //tambah pengecekan tanggal kembali harus setelah tgl keberangkatan
                console.log("objDepart.eta=" + objDepart.eta + " objReturn.etd=" + objReturn.etd);
                var departEta = toJavascriptDate(objDepart.eta);
                var returnEtd = toJavascriptDate(objReturn.etd);
                console.log("departEta=" + departEta + " returnEtd=" + returnEtd);
                if (returnEtd <= departEta) {
                    $("#terror ul").empty();
                    $("#terror ul").append('<li>Penerbangan Kembali harus setelah Penerbangan Keberangkatan</li>');
                    $('#tbanner').show(); $('#terror').show();
                } else {
                    postVoltrasAirFare($);
                }
            }
        }
    }

}

function updateRightDetailReturn($) {
    var rFCData = $('#returnList .active').find('.airlineLogo').attr('class'),
        rFCArr = rFCData.split(' '),
        rFLogo = rFCArr[1],
        rFCode = $('#returnList .active').find('.flightCode').html(),
        rFFData = $('#returnList .active').find('.departCodeTime').html(),
        rFFArr = rFFData.split('&nbsp;');
    console.log("rFFData=" + rFFData);
    var rFFCode = rFFArr[0],
        rFFTime = rFFArr[1].substring(3, rFFArr[1].length - 4),
        raFFData = $('#returnList .active').find('.arriveCodeTime').html(),
        raFFArr = raFFData.split('&nbsp;'),
        raFFCode = raFFArr[0],
        raFFTime = raFFArr[1].substring(3, raFFArr[1].length - 4),
        rFPData = $('#returnList .active').find('.flightPrice').html(),
        rFPArr = rFPData.split(' '),
        rFPARs = parseInt(rFPArr[0].replace(/[^a-z0-9\s]/gi, '')),
        rClass = $('#returnList .active').find('.hiddenSelectedClass').html(),
        rAirlineId = $('#returnList .active').find('.hiddenSelectedAirlineId').html(),

        rConnecting = $('#returnList .active').find('.hiddenConnecting').html(),
        rConnecting2 = $('#returnList .active').find('.hiddenConnecting2').html(),
        rEtd = $('#returnList .active').find('.hiddenEtd').html(),
        rEta = $('#returnList .active').find('.hiddenEta').html(),
        rHiddenCityFrom = $('#returnList .active').find('.hiddenCityFrom').html(),
        rHiddenCityTo = $('#returnList .active').find('.hiddenCityTo').html();

        if (objDepart && objDepart.eta) {
            console.log("objDepart.eta=" + objDepart.eta);
            var departEta = toJavascriptDate(objDepart.eta);
            var returnEtd = toJavascriptDate(rEtd);
            console.log("departEta=" + departEta + " returnEtd=" + returnEtd);
            if (returnEtd <= departEta) {
                $("#terror ul").empty();
                $("#terror ul").append('<li>Penerbangan Kembali harus setelah Penerbangan Keberangkatan</li>');
                $('#tbanner').show(); $('#terror').show();

                $('#totalPrice').hide();
                $('#dFPriceTitle').hide(); $('#dFPriceDetail').hide();
                $('#divSpinner').hide();
                $('input[type="submit"]').attr('disabled','disabled');
                return;
            } else {
                $('#tbanner').hide(); $('#terror').hide();
            }
        }

        $('.returnLogoAndCode .flightCode').html(rFCode);
        $('.returnLogoAndCode .flightLogo').removeClass().addClass('flightLogo ' + rFLogo);
        $('#returnFrom').html(rFFCode);     //$('.returnFlow .departCityCode').html(dFFCode);
        $('#returnTo').html(raFFCode);     //$('.returnFlow .arriveCityCode').html(aFFCode);
        $('#returnCityFrom').html(rHiddenCityFrom);
        $('#returnCityTo').html(rHiddenCityTo);

        $('#returnEtd').html(hhmm(toJavascriptDate(rEtd)));     //$('.returnFlow .departTime').html(dFFTime);
        $('#returnEta').html(hhmm(toJavascriptDate(rEta)));     //$('.returnFlow .arriveTime').html(aFFTime);

        console.log("rFFTime=" + rFFTime + " raFFTime=" + raFFTime);

    try {
        objReturn.aeroConnectingFlight = JSON && JSON.parse(rConnecting) || $.parseJSON(rConnecting);
        objReturn.isConnectingFlight = true;
    } catch(err) {
        objReturn.aeroConnectingFlight = null;
        objReturn.isConnectingFlight = false;
    }
    try {
        objReturn.aeroConnectingFlight2 = JSON && JSON.parse(rConnecting2) || $.parseJSON(rConnecting2);
    } catch(err) {
        objReturn.aeroConnectingFlight2 = null;
    }

    objReturn.airlineId = rAirlineId;
    objReturn.selectedClassId = rClass;
    objReturn.selectedClass.classId = rClass;
    objReturn.departureAirportCode = rFFCode;
    objReturn.arrivalAirportCode = raFFCode;
    objReturn.flightNumber = rFCode;
    objReturn.etd = rEtd;   //getDateConcate(objReturn.etd, rFFTime);
    objReturn.eta = rEta;   //getDateConcate(objReturn.eta, raFFTime);

    selectedReturn = JSON.stringify(objReturn);
    console.log("selectedReturn=" + selectedReturn);
    $('#returnDetail').css("display", "block");
    if (objReturn.providerCode === "AERO") {

    } else {

        var ticketPrice = "<p class='priceCtr priceTotal'>Tiket Kembali <span class='passPrice'>" + rFPData + "</span></p>";
        $('#rFPriceTitle').html(ticketPrice);

    }

    console.log("rConnecting=" + rConnecting);
    try {
        var objConnecting = JSON && JSON.parse(rConnecting) || $.parseJSON(rConnecting);
        console.log("return objConnecting=" + objConnecting);
        $('#returnFlightCodeConnecting').show();
        $('#returnConnecting').show();

        console.log("return objConnecting=" + objConnecting);
        console.log("return objConnecting.connectingFlightFrom=" + objConnecting.connectingFlightFrom);
        console.log("return objConnecting.connectingFlightTo=" + objConnecting.connectingFlightTo);

        $('#returnFlightCodeConnecting').html(objConnecting.connectingFlightFno);
        $('#returnConnectingFrom').html(objConnecting.connectingFlightFrom);
        $('#returnConnectingTo').html(objConnecting.connectingFlightTo);
        $('#returnConnectingCityFrom').html(objConnecting.connectingFlightCityFrom);
        $('#returnConnectingCityTo').html(objConnecting.connectingFlightCityTo);

        $('#returnConnectingEtd').html(hhmm(toJavascriptDate(objConnecting.connectingFlightEtd)));
        $('#returnConnectingEta').html(hhmm(toJavascriptDate(objConnecting.connectingFlightEta)));
    }
    catch(err) {
        $('#returnFlightCodeConnecting').css("display", "none");
        $('#returnConnecting').css("display", "none");
    }

    try {
        var objConnecting2 = JSON && JSON.parse(rConnecting2) || $.parseJSON(rConnecting2);
        $('#returnFlightCodeConnecting2').show();
        $('#returnConnecting2').show();

        console.log("objConnecting2=" + objConnecting);
        console.log("objConnecting2.connectingFlightFrom=" + objConnecting2.connectingFlightFrom);
        console.log("objConnecting2.connectingFlightTo=" + objConnecting2.connectingFlightTo);

        $('#returnFlightCodeConnecting2').html(objConnecting2.connectingFlightFno);
        $('#returnConnecting2From').html(objConnecting2.connectingFlightFrom);
        $('#returnConnecting2To').html(objConnecting2.connectingFlightTo);
        $('#returnConnecting2CityFrom').html(objConnecting2.connectingFlightCityFrom);
        $('#returnConnecting2CityTo').html(objConnecting2.connectingFlightCityTo);

        $('#returnConnecting2Etd').html(hhmm(toJavascriptDate(objConnecting2.connectingFlightEtd)));
        $('#returnConnecting2Eta').html(hhmm(toJavascriptDate(objConnecting2.connectingFlightEta)));
    }
    catch(err) {
        $('#returnFlightCodeConnecting2').css("display", "none");
        $('#returnConnecting2').css("display", "none");
    }

    objReturn.flightNumber = rFCode;
    objReturn.departureAirportCode = rFFCode;
    objReturn.arrivalAirportCode = raFFCode;
    //console.log("objReturn.arrivalAirportCode=" + objReturn.arrivalAirportCode);

    objReturn.selectedClassId = rClass;
    //console.log("objReturn.selectedClassId=" + objReturn.selectedClassId);
    selectedReturn = JSON.stringify(objReturn);
    //console.log("selectedReturn before call=" + selectedReturn);
    $('#hiddenSelectedReturn').val(selectedReturn);

    //tambah pengecekan tanggal kembali harus setelah tgl keberangkatan
    console.log("objDepart.eta=" + objDepart.eta + " objReturn.etd=" + objReturn.etd);

    if (objAeroTiketingView.providerCode === "AERO") {
        postPriceDetailReturn($);
    } else {
        if (isRequest == false && isAlreadySelectDepart && isAlreadySelectReturn) {
            postVoltrasAirFare($);
        }
    }
}

function hhmm(datetime) {

        var hour = datetime.getHours();
        var minuite = datetime.getMinutes();

        var shour = hour.toString();
        var sminuite = minuite.toString();
        if (hour < 10) shour = "0" + shour;
        if (minuite < 10) sminuite = "0" + sminuite;
   		return  shour + ":" + sminuite;
   	}

    function getDateConcate(vdate, hourmin) {

        var datefull = toJavascriptDate(vdate);
        var partsArray = hourmin.split(':');
        var hour = partsArray[0];
        var minuite = partsArray[1];

        var newDate = new Date(datefull.getFullYear(), datefull.getMonth(), datefull.getDate(), hour, minuite, 0, 0);

        return toJsonDate(newDate);
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
    //Thu Apr 10 2014 06:10:00 GMT+0700 (SE Asia Standard Time) -> 20 03 2014 09:40:00
    function toJsonDate(datetime) {

       var dt = datetime.getDate();
       var month = datetime.getMonth() + 1;
       var year = datetime.getFullYear();
       var hour = datetime.getHours();
       var minuite = datetime.getMinutes();
       var second = datetime.getSeconds();
       if ((datetime.getMonth() + 1) < 10) month = "0" + month;
       if (datetime.getDate() < 10) dt = "0" + dt;
       if (datetime.getHours() < 10) hour = "0" + hour;
       if (datetime.getMinutes() < 10) minuite = "0" + minuite;
       if (datetime.getSeconds() < 10) second = "0" + second;

       var newDate = dt + " " + month + " " + year + " " + hour + ":" + minuite + ":"+ second;

       return newDate;
    }

	function getLogo(airlineId) {
		var logo = "";

		if (airlineId == "1" || airlineId == "QZ") logo = "logoAirasia";        //airasia
		else if (airlineId == "2" || airlineId == "Y6") logo = "logoBatavia";   //batavia
		else if (airlineId == "3" || airlineId == "QG") logo = "logoCitilink";  //citilink
		else if (airlineId == "4" || airlineId == "GA") logo = "logoGaruda";    //garuda
		else if (airlineId == "5" || airlineId == "MZ") logo = "logoMerpati";   //Merpati
        else if (airlineId == "6" || airlineId == "SJ") logo = "logoSriwijaya"; //Sriwijaya
		else if (airlineId == "7" || airlineId == "JT") logo = "logoLion";      //lionaor
        else if (airlineId == "8" || airlineId == "KD") logo = "logoKalstar";   //Kalstar
        else if (airlineId == "9" || airlineId == "GE") logo = "logoGE";        //Garuda executif
		else if (airlineId == "11" || airlineId == "IR") logo = "logoMandala";  //Mandala tiger
        else if (airlineId == "12" || airlineId == "XP") logo = "logoXpress";  //Mandala tiger

        else if (airlineId == "21" || airlineId == "AK") logo = "logoAirasiaMalaysia";
        else if (airlineId == "22" || airlineId == "MV") logo = "logoAviaStar";
        else if (airlineId == "23" || airlineId == "ID") logo = "logoBatikAir";
        else if (airlineId == "24" || airlineId == "OD") logo = "logoMalindo";
        else if (airlineId == "25" || airlineId == "XX") logo = "logoNamAir";
		else if (airlineId == "26" || airlineId == "SY") logo = "logoSkyAviation";
        else if (airlineId == "27" || airlineId == "SL") logo = "logoThaiLion";
        else if (airlineId == "28" || airlineId == "TR") logo = "logoTigerAirSing";
        else if (airlineId == "29" || airlineId == "IL") logo = "logoTriganaAir";
        else if (airlineId == "30" || airlineId == "IW") logo = "logoWingsAir";

		return logo;
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

//	    var p = num.toFixed(2).split(".");
//	    return p[0].split("").reverse().reduce(function(acc, num, i, orig) {
//	        return  num + (i && !(i % 3) ? "." : "") + acc;
//	    }, "") + "," + p[1] + " IDR";
	}

    function postPriceDetailDepart($) {
        console.log("postPriceDetailDepart, isRequest=" + isRequest);
        if (isRequest == false) {
            isRequest = true;
            console.log("selectedDepart=" + selectedDepart);
            objDepart = JSON && JSON.parse(selectedDepart) || $.parseJSON(selectedDepart);
            console.log("objDepart=" + objDepart);
            objDepart.transactionType = "9m";
            objDepart.providerCode = "AERO";
            objDepart.aeroFlightClasses = [];

            console.log("objDepart.transactionType=" + objDepart.transactionType + " objDepart.providerCode=" + objDepart.providerCode);
            selectedDepart = JSON.stringify(objDepart);
            console.log("2 selectedDepart=" + selectedDepart);

            $('#divSpinner').show();
            $('#dFPriceTitle').hide();  $('#dFPriceDetail').hide();
            $('#tbanner').hide(); $('#terror').hide();


            console.log("hiddenServerIp + /rest/aero/priceDetail=" + hiddenServerIp + "/rest/aero/priceDetail");
            $.ajax({
                url: hiddenServerIp + "/rest/aero/priceDetail",
                type: 'POST',
                data: 'jsonView=' + hiddenAeroTicketingView + '&jsonFlight=' + selectedDepart ,
                success: function (res) {
                    console.log("res=" + res);
                    isRequest = false;

                    var objRes = res;
                    console.log("objRes.adultPassengerSummary=" + objRes.adultPassengerSummary);

                    if (null != objRes.adultPassengerSummary) {
                        if (objAeroTiketingView.isDepartOnly) {
                            objAeroTiketingView.total = objRes.total + objAeroTiketingView.agentMargin;
                            $('input[type="submit"]').removeAttr('disabled');
                        } else if (objReturn && objReturn.total) {
                            objAeroTiketingView.total = objRes.total + objReturn.total + objAeroTiketingView.agentMargin;
                            $('input[type="submit"]').removeAttr('disabled');
                        } else {
                            //$('.lbTotalPrice').html("Silahkan Memilih Penerbangan Kembali");
                        }

                        if (objAeroTiketingView.total) {
                            $('#allTotalPrice').show();
                            $('#totalPrice').html(formatRupiah(objAeroTiketingView.total));
                            $('input[type="submit"]').removeAttr('disabled');
                        }

                        $('#departAdultPrice').html(formatRupiah(objRes.adultPassengerSummary.total));
                        if (objAeroTiketingView.totalChildren > 0) {
                            $('#departChildPrice').html(formatRupiah(objRes.childPassengerSummary.total));
                        }
                        if (objAeroTiketingView.totalInfant > 0) {
                            $('#departInfantPrice').html(formatRupiah(objRes.infantPassengerSummary.total));
                        }
                        $('#departPrice').html(formatRupiah(objRes.total));

                        if (objRes.total) {
                            objDepart = objRes
                            selectedDepart = JSON.stringify(objDepart);
                            console.log("3. selectedDepart=" + selectedDepart);
                            $('#hiddenSelectedDepart').val(selectedDepart);
                        }
                        objAeroTiketingView.departureFlight = objRes;
                        hiddenAeroTicketingView  = JSON.stringify(objAeroTiketingView);
                        $('#hiddenAeroTicketingView').val(hiddenAeroTicketingView);
                    }  else {
                        var showMessage = "<p class='priceCtr' style='color: #ff0000;'><span class='passPrice'>" + objRes.description + "</span></p>";

                        //$('.lbTotalPrice').show(); $('.totalPrice').show();
                    }
                    $('#divSpinner').hide();
                    $('#aeroDepartPrice').show();
                } ,
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    console.log("Status: " + textStatus);
                    console.log("Error: " + errorThrown);

                    isRequest = false;
                    //alert("Status: " + textStatus); alert("Error: " + errorThrown);
                }
            });
        }
    }

    function postPriceDetailReturn($) {
        if (isRequest == false) {
            isRequest = true;
            objReturn = JSON && JSON.parse(selectedReturn) || $.parseJSON(selectedReturn);
            objReturn.transactionType = "9m";
            objReturn.providerCode = "AERO";
            selectedReturn = JSON.stringify(objReturn);

            $('#divSpinner').show();
            //$('.lbTotalPrice').hide(); $('.totalPrice').hide();
            $('#rFPriceTitle').hide(); $('#rFPriceDetail').hide();
            $('#tbanner').hide(); $('#terror').hide();
            $.ajax({
                url: hiddenServerIp + "/rest/aero/priceDetail",
                type: 'POST',
                data: 'jsonView=' + hiddenAeroTicketingView + '&jsonFlight=' + selectedReturn,
                success: function (res) {
                    isRequest = false;
                    var objRes = res;
                    console.log("objRes.adultPassengerSummary=" + objRes.adultPassengerSummary);

                    if (null != objRes.adultPassengerSummary) {

                        $('#returnAdultPrice').html(formatRupiah(objRes.adultPassengerSummary.total));
                        if (objAeroTiketingView.totalChildren > 0) {
                            $('#returnChildPrice').html(formatRupiah(objRes.childPassengerSummary.total));
                        }
                        if (objAeroTiketingView.totalInfant > 0) {
                            $('#returnInfantPrice').html(formatRupiah(objRes.infantPassengerSummary.total));
                        }
                        $('#returnPrice').html(formatRupiah(objRes.total));

                        if (objRes.total) {
                            objReturn = objRes;
                            selectedReturn = JSON.stringify(objReturn);
                            console.log("3. selectedReturn=" + selectedReturn);
                            $('#hiddenSelectedReturn').val(selectedReturn);

                            objAeroTiketingView.returnFlight = objRes;

                            console.log("before objAeroTiketingView.total=" + objAeroTiketingView.total);
                            if (isAlreadySelectDepart) {
                                objAeroTiketingView.total = objDepart.total + objRes.total  + objAeroTiketingView.agentMargin;
                                console.log("after objAeroTiketingView.total=" + objAeroTiketingView.total);

                                if (objAeroTiketingView.total) {
                                    console.log("masuk if (objAeroTiketingView.total)");
                                    console.log("formatRupiah(objAeroTiketingView.total=" + formatRupiah(objAeroTiketingView.total));
                                    $('#allTotalPrice').show();
                                    $('#totalPrice').html(formatRupiah(objAeroTiketingView.total));
                                    console.log("$('#totalPrice').innerHTML=" + $('#totalPrice').innerHTML);
                                    $('input[type="submit"]').removeAttr('disabled');
                                }
                            }

                            hiddenAeroTicketingView  = JSON.stringify(objAeroTiketingView);
                            $('#hiddenAeroTicketingView').val(hiddenAeroTicketingView);
                        }
                        $('#divSpinner').hide();
                        $('#aeroReturnPrice').show();

                    } else {
                        $('#divSpinner').hide();
                        //$('.lbTotalPrice').show(); $('.totalPrice').show();

                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    console.log("Status: " + textStatus);
                    console.log("Error: " + errorThrown);

                    isRequest = false;
                }
            });
        }
    }

    function postVoltrasAirFare($) {
        //alert("Sistem sedang request detail harga, mohon tunggu sebentar");
        isRequest = true;
        console.log("before hiddenAeroTicketingView=" + hiddenAeroTicketingView);
        console.log("path=" + hiddenServerIp + "/rest/aero/voltrasAirFare");
        $('#allTotalPrice').hide();
        $('#divSpinner').show();
        $('#tbanner').hide(); $('#terror').hide();
        $.ajax({
            url: hiddenServerIp + "/rest/aero/voltrasAirFare",
            type: 'POST',
            data: 'json=' + hiddenAeroTicketingView + '&jsonDepart=' + selectedDepart + '&jsonReturn=' + selectedReturn,
            success: function (res) {
                var objRes = res;

                console.log("objRes.total=" + objRes.total);
                if (objRes.total && objRes.total > 0) {
//                    hiddenAeroTicketingView =  JSON.stringify(res);
//                    $('#hiddenAeroTicketingView').val(hiddenAeroTicketingView);
//                    console.log("after hiddenAeroTicketingView=" + hiddenAeroTicketingView);
                    objAeroTiketingView = objRes;
                    objAeroTiketingView.total = objAeroTiketingView.total + objAeroTiketingView.agentMargin;

                    hiddenAeroTicketingView =  JSON.stringify(objAeroTiketingView);
                    $('#hiddenAeroTicketingView').val(hiddenAeroTicketingView);
                    console.log("after hiddenAeroTicketingView=" + hiddenAeroTicketingView);

                    $('#allTotalPrice').show();
                    $('#totalPrice').html(formatRupiah(objAeroTiketingView.total));

                    $('input[type="submit"]').removeAttr('disabled');
                } else {
                    $("#terror ul").empty();
                    $("#terror ul").append('<li>Silahkan memilih penerbangan lain</li>');
                    $('#tbanner').show(); $('#terror').show();
                }
                $('#divSpinner').hide();
                //$('.lbTotalPrice').show();
                $('#totalPrice').show();
                isRequest = false;
                refreshRightDepart($);

                console.log("after refreshRightDepart($)");
                $('#aeroDepartPrice').hide();
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                console.log("Status: " + textStatus);
                console.log("Error: " + errorThrown);
                //$('.lbTotalPrice').html("<p style='color: #ff0000;'>Silahkan memilih penerbangan lain</p>");
                isRequest = false;
                //alert("Status: " + textStatus); alert("Error: " + errorThrown);
            }
        });
    }

function hidingComponents($) {
    $('#departDetail').hide();
    $('#departFlightCodeConnecting').hide();
    $('#departConnecting').hide();
    $('#departFlightCodeConnecting2').hide();
    $('#departConnecting2').hide();

    $('#returnDetail').hide();
    $('#returnFlightCodeConnecting').hide();
    $('#returnConnecting').hide();
    $('#returnFlightCodeConnecting2').hide();
    $('#returnConnecting2').hide();
    console.log("after hiding departDetail, departConnecting, etc ");

    $('#aeroDepartPrice').hide();
    $('#aeroReturnPrice').hide();
    $('#allTotalPrice').hide();

    $('.departLogoAndCode .flightCode').html("&nbsp;<br/>");
    $('.departLogoAndCode .flightLogo').removeClass().addClass('flightLogo');
    $('.returnLogoAndCode .flightCode').html("&nbsp;<br/>");
    $('.returnLogoAndCode .flightLogo').removeClass().addClass('flightLogo');
}

jQuery(document).ready(function ($) {
    $('#tbanner').hide(); $('#terror').hide();
    //location.reload(true);
    $('input[type="submit"]').attr('disabled','disabled');
    //hide spinner, show spinner Big
    $('#divSpinner').hide();
    $('#divSpinnerBig').show();

    $('#dFPriceTitle').hide();  $('#dFPriceDetail').hide();
    $('#rFPriceTitle').hide();  $('#rFPriceDetail').hide();

    hiddenServerIp = $('#hiddenServerIp').val();
    hiddenAeroTicketingView = $('#hiddenAeroTicketingView').val();
    console.log("start hiddenAeroTicketingView=" + hiddenAeroTicketingView);
    objAeroTiketingView = JSON && JSON.parse(hiddenAeroTicketingView) || $.parseJSON(hiddenAeroTicketingView);

    selectedReturn = null;
    objReturn = null;

    defineSelectFlightDepartClick($);

    defineSelectFlightReturnClick($);

    // fungsi pilih kelas lain
    defineShowOtherClassDepart($);

    defineShowOtherClassReturn($);

    defineSelectPopUpClassDepart($);

    defineSelectPopUpClassReturn($);

    // fungsi popup filter pencarian
    $('.flightFilter .tier1').click(function () {
        $(this).addClass('active');
        $(this).find('ul').show();
    });

    $('.flightFilter .tier1 ul li').click(function () {
        console.log("$(this).html()=" + $(this).html());
        $(this).parent().find('li').removeClass('active');
        $(this).addClass('active');
        $(this).parent().parent().removeClass('active');
        $('.flightFilter li ul').hide();

        console.log("arrDeparts[0] sebelum sort=" + arrDeparts[0].etd);
        if ($(this).html().indexOf("Harga") > -1) {
            arrDeparts.sort(function(obj1, obj2) {
                return obj1.ticketPrice - obj2.ticketPrice;
            });
        } else if ($(this).html().indexOf("Waktu") > -1) {
            arrDeparts.sort(function(obj1, obj2) {
                return toJavascriptDate(obj1.etd) - toJavascriptDate(obj2.etd);
            });
        } else {    //maskapai
            arrDeparts.sort(function(obj1, obj2) {
                return obj1.airlineId.localeCompare(obj2.airlineId);
            });
        }
        console.log("arrDeparts[0] sesudah sort=" + arrDeparts[0].etd);
        setFlightTableArrayPublic($,arrDeparts, "depart");

        if ($(this).html().indexOf("Harga") > -1) {
            arrReturns.sort(function(obj1, obj2) {
                return obj1.ticketPrice - obj2.ticketPrice;
            });
        } else if ($(this).html().indexOf("Waktu") > -1) {
            arrReturns.sort(function(obj1, obj2) {
                return toJavascriptDate(obj1.etd) - toJavascriptDate(obj2.etd);
            });
        } else {    //maskapai
            arrReturns.sort(function(obj1, obj2) {
                return obj1.airlineId.localeCompare(obj2.airlineId);
            });
        }

        setFlightTableArrayPublic($,arrReturns, "return");
        hidingComponents($);
        return false;
    });

    // fungsi untuk menutup panel filter
    $(document).mouseup(function (e) {
		var container = $('.flightFilter li ul, .classOptionPop');

		if (container.has(e.target).length === 0) {
			$('.flightFilter li ul, .classOptionPop').hide();
            $('.flightFilter .tier1').removeClass('active');
		}

	});

    hidingComponents($);

    postGetFlights($);

});

function postGetFlights($) {
    $('#divSpinnerBig').show();
    console.log("$('#hiddenAeroTicketingViewInit').val()=" + $('#hiddenAeroTicketingViewInit').val());
    $.ajax({
        url: hiddenServerIp + "/rest/aero/getFlightListPost",
        type: 'POST',
        data: 'json=' + $('#hiddenAeroTicketingViewInit').val(),
        success: function (res) {
            $('#divSpinnerBig').hide();
            console.log("res=" + res);

            var sFlights = JSON.stringify(res);
            console.log("sFlights=" + sFlights);
            if (sFlights) {
                if ( sFlights.indexOf("errorCode") > -1 || sFlights.indexOf("IB-1009") > -1) {
                    console.log("res.description=" + res.description);
                    $('#divSpinnerBig').hide();
                    $("#terror ul").empty();
                    $("#terror ul").append('<li>' + res.description + '</li>');
                    $('#tbanner').show(); $('#terror').show();
                }
                else {
                    selectedReturn = null;
                    objReturn = null;

                    $.each(JSON.parse(sFlights), function(idx, obj) {
                        console.log("obj.isDepartureFlight=" + obj.isDepartureFlight);
                        if (obj.isDepartureFlight == true) {
                            arrDeparts.push( obj );
                            if (arrDeparts.size() == 1) {
                                objDepart = obj;
                            }
                        } else {
                            arrReturns.push(obj);
                            if (arrReturns.size() == 1) {
                                objReturn = obj;
                            }
                        }
                    });
                    console.log("arrDeparts=" + arrDeparts);
                    console.log("arrReturns=" + arrReturns);

                    objAeroTiketingView = JSON && JSON.parse(hiddenAeroTicketingView) || $.parseJSON(hiddenAeroTicketingView);
                    if (objAeroTiketingView.isDepartOnly) {
                        isAlreadySelectReturn = true;
                    } else {
                        setFlightTableArrayPublic($, arrReturns, "return");
                    }

                    setFlightTableArrayPublic($, arrDeparts, "depart");

                    hidingComponents($);

                    maxList = 5;
                    ListHeightAllowed = ($('.listPilihan tr').height() * maxList) + 1,
                    departMaxListHeight = $('#departList').height(),
                    returnMaxListHeight = $('#returnList').height();

                    if (departMaxListHeight > ListHeightAllowed){
                        $('#departList').addClass('scrollList');
                    } else {
                        $('#departList').removeClass('scrollList');
                    }

                    if (returnMaxListHeight > ListHeightAllowed){
                        $('#returnList').addClass('scrollList');
                    } else {
                        $('#returnList').removeClass('scrollList');
                    }
                }
            } else {
                $("#terror ul").empty();
                $("#terror ul").append('<li>Sambungan Provider Down</li>');
                $('#tbanner').show(); $('#terror').show();
            }
        } ,
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log("Status: " + textStatus);
            console.log("Error: " + errorThrown);
            $('#divSpinnerBig').hide();
            $("#terror ul").empty();
            $("#terror ul").append('<li>' + textStatus + '</li>');
            $('#tbanner').show(); $('#terror').show();
        }
    });
}

function setFlightTableArrayPublic($, flightArray, departOrReturn) {
    if (departOrReturn == "depart") {
        $('#tbodyDepart').empty();
    }
    else {
        $('#tbodyReturn').empty();
    }
    //console.log("flightArray=" + flightArray);
    for( var i = 0; i < flightArray.length; i++ ) {
        var sConnecting = null;
        var sConnecting2 = null;
        //console.log("i=" + i + " flightArray[i].aeroConnectingFlight=" + flightArray[i].aeroConnectingFlight);
        if (flightArray[i].aeroConnectingFlight) {
            //console.log("flightArray[i].aeroConnectingFlight=" + flightArray[i].aeroConnectingFlight);
            sConnecting = JSON.stringify(flightArray[i].aeroConnectingFlight);
        }
        if (flightArray[i].aeroConnectingFlight2) {
            sConnecting2 = JSON.stringify(flightArray[i].aeroConnectingFlight2);
        }
        //console.log("public sConnecting=" + sConnecting);
        var tbl_row = "<tr " + isRowActive(9) + ">" +
            "<td class='fLTable1'>" +
                "<span class='airlineLogo " + getLogo(flightArray[i].airlineId) +"'></span>" +
                "<span class='flightCode'>" + flightArray[i].flightNumber + "</span>" +
            "</td>" +
            "<td class='fLTable2'>" +
                "<span class='departCodeTime'>" + flightArray[i].departureAirportCode + "&nbsp;<b>"+ hhmm(toJavascriptDate(flightArray[i].etd)) +"</b></span><br/>" +
                "<span class='arriveCodeTime'>" + flightArray[i].arrivalAirportCode + "&nbsp;<b>"+ hhmm(toJavascriptDate(flightArray[i].eta)) +"</b></span>" +
            "</td>" +
            "<td class='fLTable3" + isColumnActive(9) + "'><input type='radio' " + isSelectedRadio(departOrReturn) + " class='cBFlight' " + isChecked(i) + " /><br />" +
                "<span class='flightPrice'>" + formatRupiah(flightArray[i].ticketPrice) + "</span><br/>" +
                "<span class='hiddenSelectedAirlineId' style='display:none'>" + flightArray[i].airlineId + "</span>" +
                "<span class='hiddenSelectedClass' style='display:none'>" + flightArray[i].selectedClassId + "</span>" +
                "<span class='hiddenSelectedClassAero' style='display:none'>" + flightArray[i].selectedClass.classId + "</span>" +
                "<span class='hiddenConnecting' style='display:none'>" + sConnecting + "</span>" +
                "<span class='hiddenConnecting2' style='display:none'>" + sConnecting2 + "</span>" +
                "<span class='hiddenEtd' style='display:none'>" + flightArray[i].etd + "</span>" +
                "<span class='hiddenEta' style='display:none'>" + flightArray[i].eta + "</span>" +
                "<span class='hiddenCityFrom' style='display:none'>" + flightArray[i].departureAirportFullName + "</span>" +
                "<span class='hiddenCityTo' style='display:none'>" + flightArray[i].arrivalAirportFullName + "</span>" +

                "<div class='flightClassOption'><span class='icon'></span>Pilih Kelas Lainnya" +
                    "<div class='classOptionPop'>" +
                    "<ul>";
                    var classes = flightArray[i].aeroFlightClasses;
                    for( var j = 0; j < classes.length; j++ ) {
                        var fclass = classes[ j ];

                        var listClass = "<li " + isLiFirst(i) + ">" +
                        "<p class='viewName'>Kelas<br /><span class='className'>" + fclass["className"] + "</span></p>" +
                        "<p class='viewSeat'>Tersedia<br /><span class='classSeat'>" + fclass["avaliableSeat"] + "</span></p>" +
                        "<p class='viewPrice'>Harga<br /><span class='valueClassPrice'>" + formatRupiah(fclass["price"]) + "</span></p>" +
                        "<span class='hiddenClassAero' style='display:none'>" + fclass["classId"] + "</span>" +
                        "</li>";

                        tbl_row = tbl_row + listClass;
                    }
                    tbl_row = tbl_row +
                                "</ul>" +
                    "</div>" +
                "</div>" +
            "</td>" +
            "</tr>";
        //console.log("public tbl_row=" + tbl_row);
        if (departOrReturn == "depart") {
            $('#tbodyDepart').append(tbl_row);
        }
        else {
            $('#tbodyReturn').append(tbl_row);
        }
    }
    if (departOrReturn == "depart") {
        defineSelectFlightDepartClick($);
        defineShowOtherClassDepart($);
        defineSelectPopUpClassDepart($);
    }
    else {
        defineSelectFlightReturnClick($);
        defineShowOtherClassReturn($);
        defineSelectPopUpClassReturn($);
    }
}

function isSelectedRadio(departOrReturn) {
    if (departOrReturn == "depart") {
        return " name='departFlightPrice' ";
    }
    return " name='returnFlightPrice' ";
}

function isRowActive(index) {
    if (index == 0) {
        return " class='active' ";
    }
    return "";
}
function isColumnActive(index) {
    if (index == 0) {
        return " active";
    }
return "";
}
function isLiFirst(index) {
    if (index == 0) {
        return " class='first' ";
    }
    return "";
}
function isChecked(index) {
    if (index == 0) {
        return " checked='checked' ";
    }
    return "";
}
