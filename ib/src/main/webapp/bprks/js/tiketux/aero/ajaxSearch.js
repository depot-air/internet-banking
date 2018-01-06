jQuery(document).ready(function ($) {
	var hiddenDeparts = $('#hiddenJsonDeparts').val();
    var hiddenReturns = $('#hiddenJsonReturns').val();
//    alert("hiddenDeparts=" + hiddenDeparts);
    var selectedDepart = $('#hiddenSelectedDepart').val();
    var objDepart = JSON && JSON.parse(selectedDepart) || $.parseJSON(selectedDepart);
    console.log("objDepart.airlineId=" + objDepart.airlineId + " objDepart.flightNumber=" + objDepart.flightNumber + " objDepart.selectedClassId=" + objDepart.selectedClassId);
    var selectedReturn = $('#hiddenSelectedReturn').val();
    var objReturn = JSON && JSON.parse(selectedReturn) || $.parseJSON(selectedReturn);
    console.log("objReturn.airlineId=" + objReturn.airlineId + " objReturn.flightNumber=" + objReturn.flightNumber + " objReturn.selectedClassId=" + objReturn.selectedClassId);

    setFlightTable(hiddenDeparts, "depart");
    setFlightTable(hiddenReturns, "return");

    // fungsi click pemilihan harga
    $('#departList .fLTable3').bind('click', function () {
        //console.log("#departList .fLTable3")
        $('#departList table tr').removeClass('active');
        $('#departList .fLTable3').removeClass('active customClass');
        $(this).addClass('active');
        $(this).parent().addClass('active');
        $(this).find('.cBFlight').prop('checked', 'checked');
        updateRightDetailDepart();
    });
    $('#returnList .fLTable3').bind('click', function () {
        //console.log("#returnList .fLTable3")
        $('#returnList table tr').removeClass('active');
        $('#returnList .fLTable3').removeClass('active customClass');
        $(this).addClass('active');
        $(this).parent().addClass('active');
        $(this).find('.cBFlight').prop('checked', 'checked');
        updateRightDetailReturn();
    });

    // fungsi pilih kelas lain
    $('#departList .flightClassOption').click(function () {
        var defPrice = $(this).find('li:first .valueClassPrice').html();
        $('.flightClassOption .classOptionPop').hide();
        $(this).find('.classOptionPop').show();
    });
    $('#returnList .flightClassOption').click(function () {
        var defPrice = $(this).find('li:first .valueClassPrice').html();
        $('.flightClassOption .classOptionPop').hide();
        $(this).find('.classOptionPop').show();
    });

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
        $(this).parent().parent().parent().parent().find('.hiddenSelectedClass').html(optionClassName);
        console.log("before objDepart.selectedClassId=" + objDepart.selectedClassId);
        objDepart.selectedClassId = optionClassName;
        console.log("after objDepart.selectedClassId=" + objDepart.selectedClassId);
        selectedDepart = JSON.stringify(objDepart);
        $('#hiddenSelectedDepart').val(selectedDepart);
        console.log("$('#hiddenSelectedDepart').val()=" + $('#hiddenSelectedDepart').val());
        return false;
    });
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
        $(this).parent().parent().parent().parent().addClass('customClass').find('.hiddenSelectedClass').html(optionClassName);
        objReturn.selectedClassId = optionClassName;
        console.log("objReturn.selectedClassId=" + objReturn.selectedClassId);
        selectedReturn = JSON.stringify(objReturn);
        $('#hiddenSelectedReturn').val(selectedReturn);
        console.log("$('#hiddenSelectedReturn').val()=" + $('#hiddenSelectedReturn').val());
        return false;
    });

    function setFlightTable(hiddenFlights, departOrReturn) {
        //console.log("hiddenFlights=" + hiddenFlights + " departOrReturn=" + departOrReturn);
        $.each(JSON.parse(hiddenFlights), function(idx, obj) {
            var tbl_row = "<tr " + isRowActive(idx) + ">" +
                "<td class='fLTable1'>" +
                    "<span class='airlineLogo " + getLogo(obj.airlineId) +"'></span>" +
                    "<span class='flightCode'>" + obj.flightNumber + "</span>" +
                "</td>" +
                "<td class='fLTable2'>" +
                    "<span class='departCodeTime'>" + obj.departureAirportCode + "&nbsp;<b>"+ hhmm(obj.etd) +"</b></span><br/>" +
                    "<span class='arriveCodeTime'>" + obj.arrivalAirportCode + "&nbsp;<b>"+ hhmm(obj.eta) +"</b></span>" +
                "</td>" +
                "<td class='fLTable3" + isColumnActive(idx) + "'><input type='radio' " + isSelectedRadio(departOrReturn) + " class='cBFlight' " + isChecked(idx) + " /><br />" +
                    "<span class='flightPrice'>" + formatRupiah(obj.ticketPrice) + "</span><br/>" +
                    "<span class='hiddenSelectedClass' style='display:none'>" + obj.selectedClassId + "</span>" +
                    "<div class='flightClassOption'><span class='icon'></span>Pilih Kelas Lainnya" +
                        "<div class='classOptionPop'>" +
                        "<ul>";
                        var classes = obj.aeroFlightClasses;
                        for( var i = 0; i < classes.length; i++ ) {
                            var fclass = classes[ i ];
                            //console.log("fclass[className]" + fclass["className"] + " fclass[avaliableSeat]" + fclass["avaliableSeat"] + " fclass[price]" + fclass["price"]);
                            var listClass = "<li " + isLiFirst(i) + ">" +
                            "<p class='viewName'>Kelas<br /><span class='className'>" + fclass["className"] + "</span></p>" +
                            "<p class='viewSeat'>Tersedia<br /><span class='classSeat'>" + fclass["avaliableSeat"] + "</span></p>" +
                            "<p class='viewPrice'>Harga<br /><span class='valueClassPrice'>" + formatRupiah(fclass["price"]) + "</span></p>" +
                            "</li>";
                            //console.log("listClass" + listClass);
                            tbl_row = tbl_row + listClass;
                        }
                        tbl_row = tbl_row +
                                    "</ul>" +
                        "</div>" +
                    "</div>" +
                "</td>" +
                "</tr>";
            //console.log("departOrReturn=" + departOrReturn + " tbl_row=" + tbl_row);
            if (departOrReturn == "depart") {
                $('#tbodyDepart').append(tbl_row);
//                updateRightDetailDepart();
            }
            else {
                $('#tbodyReturn').append(tbl_row);
//                updateRightDetailReturn();
            }
        });
    }

    function updateRightDetailDepart() {
        var dFCData = $('#departList .active').find('.airlineLogo').attr('class'),
            dFCArr = dFCData.split(' '),
            dFLogo = dFCArr[1],
            dFCode = $('#departList .active').find('.flightCode').html(),
            dFFData = $('#departList .active').find('.departCodeTime').html(),
            dFFArr = dFFData.split('&nbsp;');
        //console.log("dFFArr=" + dFFArr);
        var dFFCode = dFFArr[0],
            dFFTime = dFFArr[1].substring(3, dFFArr[1].length - 4),
            aFFData = $('#departList .active').find('.arriveCodeTime').html(),
            aFFArr = aFFData.split('&nbsp;'),
            aFFCode = aFFArr[0],
            aFFTime = aFFArr[1].substring(3, aFFArr[1].length - 4),
            dFPData = $('#departList .active').find('.flightPrice').html(),
            dFPArr = dFPData.split(' '),
            dFPARs = parseInt(dFPArr[0].replace(/[^a-z0-9\s]/gi, '')),
            dClass = $('#departList .active').find('.hiddenSelectedClass').html();

        console.log("$('#departList .active').html()=" + $('#departList .active').html() );
        console.log("dFCode=" + dFCode + " dFLogo=" + dFLogo + " dFFCode=" + dFFCode + " aFFCode=" + aFFCode  + " dClass=" + dClass );
            $('.departLogoAndCode .flightCode').html(dFCode);
            $('.departLogoAndCode .flightLogo').removeClass().addClass('flightLogo ' + dFLogo);
            $('.departFlow .departCityCode').html(dFFCode);
            $('.departFlow .arriveCityCode').html(aFFCode);
            $('.departFlow .departTime').html(dFFTime);
            $('.departFlow .arriveTime').html(aFFTime);

        objDepart.flightNumber = dFCode;
        objDepart.departureAirportCode = dFFCode;
        objDepart.arrivalAirportCode = aFFCode;
        objDepart.flightDate = dFFData;
        objDepart.etd = dFFData;
        objDepart.eta = aFFData;
        objDepart.selectedClassId = dClass;
        console.log("objDepart.selectedClassId=" + objDepart.selectedClassId);
        selectedDepart = JSON.stringify(objDepart);
        $('#hiddenSelectedDepart').val(selectedDepart);
        console.log("$('#hiddenSelectedDepart').val()=" + $('#hiddenSelectedDepart').val());
    }

    function updateRightDetailReturn() {
        console.log("updateRightDetailReturn()");
        var rFCData = $('#returnList .active').find('.airlineLogo').attr('class');
        console.log("rFCData=" + rFCData);
        var rFCArr = rFCData.split(' ');
        console.log("rFCArr=" + rFCArr);
        var rFLogo = rFCArr[1];
        console.log("rFLogo=" + rFLogo);
        var rFCode = $('#returnList .active').find('.flightCode').html();
        console.log("rFCode=" + rFCode);
        var rFFData = $('#returnList .active').find('.departCodeTime').html();
        console.log("rFFData=" + rFFData);
        var rFFArr = rFFData.split('&nbsp;');
        console.log("rFFArr=" + rFFArr);
        var rFFCode = rFFArr[0],
            rFFTime = rFFArr[1].substring(3, rFFArr[1].length - 4),
            raFFData = $('#returnList .active').find('.arriveCodeTime').html(),
            raFFArr = raFFData.split('&nbsp;'),
            raFFCode = raFFArr[0],
            raFFTime = raFFArr[1].substring(3, raFFArr[1].length - 4),
            rFPData = $('#returnList .active').find('.flightPrice').html(),
            rFPArr = rFPData.split(' '),
            rFPARs = parseInt(rFPArr[0].replace(/[^a-z0-9\s]/gi, '')),
            rClass = $('#departList .active').find('.hiddenSelectedClass').html();

        console.log("rFCode=" + rFCode + " rFLogo=" + rFLogo + " rFFCode=" + rFFCode + " rFFCode=" + rFFCode);
            $('.returnLogoAndCode .flightCode').html(rFCode);
            $('.returnLogoAndCode .flightLogo').removeClass().addClass('flightLogo ' + rFLogo);
            $('.returnFlow .departCityCode').html(rFFCode);
            $('.returnFlow .arriveCityCode').html(raFFCode);
            $('.returnFlow .departTime').html(rFFTime);
            $('.returnFlow .arriveTime').html(raFFTime);

        objReturn.flightNumber = rFCode;
        objReturn.departureAirportCode = rFFCode;
        objReturn.arrivalAirportCode = raFFCode;
        objReturn.flightDate = rFFData;
        objReturn.etd = rFFData;
        objReturn.eta = raFFData;
        objReturn.selectedClassId = rClass;
        console.log("objReturn.selectedClassId=" + objReturn.selectedClassId);
        selectedReturn = JSON.stringify(objReturn);
        $('#hiddenSelectedReturn').val(selectedReturn);
        console.log("$('#hiddenSelectedReturn').val()=" + $('#hiddenSelectedReturn').val());
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

	function hhmm(datetime) {
		var vdt = datetime;
		//20 03 2014 09:40:00 -> 09:40
		return vdt.substring(16,11);
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

	function formatRupiah(num) {
	    var p = num.toFixed(2).split(".");
	    return p[0].split("").reverse().reduce(function(acc, num, i, orig) {
	        return  num + (i && !(i % 3) ? "." : "") + acc;
	    }, "") + "," + p[1] + " IDR";
	}
});
