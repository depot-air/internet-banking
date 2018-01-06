var hiddenServerIp, hiddenAirports, hiddenOftenDepart, hiddenOftenReturn, hiddenOftenRoute, hiddenCustomerId, hiddenAeroTicketingView;
var arrMostOftenDepart = [];
var arrMostOftenReturn = [];
var arrAllAirports = [];
var arrAirportsDepart = [];
var arrAirportsReturn = [];
var max = 5; // Maksimal maskapai yang bisa di pilih
var valDepartReturn;

jQuery(document).ready(function ($) {
    $('#tbanner').hide(); $('#terror').hide();
    //location.reload(true);
    'use strict';
    hiddenServerIp = $('#hiddenServerIp').val();
    hiddenCustomerId = $('#hiddenCustomerId').val();
    var hiddenSelectedAirlines = $('#hiddenSelectedAirlines').val();
    console.log("hiddenSelectedAirlines=" + hiddenSelectedAirlines);
    function setHiddenSelectedAirlines() {
        if (hiddenSelectedAirlines.contains(',')) {
            //parse, kemudian tampilkan
            var airlines = hiddenSelectedAirlines.split(',');
            console.log("airlines=" + airlines);
            for (var i = 0; i < airlines.length; i++) {
                var airlineCode = getAirlineLogo(airlines[i]);
                $('.airLines').find('#' + airlineCode).show();
                setHiddenAirlineValue(airlineCode);
                $('.airLines').find('#checkBox' + airlineCode).parent().addClass('wEnb');
            }
        } else {
            var airlineCode = getAirlineLogo(hiddenSelectedAirlines);
            $('.airLines').find('#' + airlineCode).show();
            setHiddenAirlineValue(airlineCode);
            $('.airLines').find('#checkBox' + airlineCode).parent().addClass('wEnb');
        }
    }

    if (hiddenSelectedAirlines !== "") {
        console.log("hiddenSelectedAirlines.contains(',')=" + hiddenSelectedAirlines.contains(','));
        setHiddenSelectedAirlines();
    }

    var departQR = $('.departIDetail .fCCode').html(),
        returnQR = $('.returnIDetail .fCCode').html();
    // fungsi untuk memilih maskapai
    var adultPassDef = 1, // Jumlah default penumpang dewasa
        adultChildPassMax = 7, // Jumlah maksimal penumpang dewasa
        childBabyPassDef = 0, // Jumlah default penumpang anak / Bayi
        childBabyPassMax = 3, // Jumlah maksimal penumpang dewasa
        serviceCost = 40000; // Harga service

    var availableProvider = [
        {id: 'VOLT', label: 'VOLTRAS'},
        {id: 'AERO', label: 'AEROTICKETING'}];


    $('.passTab li').click(function () {
        $('.passTab li').removeClass('active');
        var activePass = $(this).attr('class');
        $('.passForm div').addClass('hide');
        $('.passForm').find('.' + activePass).removeClass('hide');
        $(this).addClass('active');
    });

    $('.miniInfo').mouseover(function () {
        $(this).css('z-index', '1');
        $(this).find('span').show();
    }).mouseout(function () {
        $(this).css('z-index', '0');
        $(this).find('span').hide();
    });

    // fungsi untuk memilih tipe keberangkatan
    $('.searchForm .rounded20').click(function () {
        var findAType = $(this).find('span').attr('class');
        $('.searchForm .rounded20').removeClass('active');
        $(this).addClass('active');
        $(this).parent().parent().find('.airType').val('');
        $(this).parent().parent().find('.airType').val(findAType);
        if (findAType === 'fTReturn') {
            $('.returnDateContainer').show();
            document.getElementById('airType').value = 'return';
        } else {
            $('.returnDateContainer').hide();
            document.getElementById('airType').value = 'oneWay';
        }
    });

    // fungsi untuk memilih tanggal keberangkatan
    $('.searchForm .departDate').datepicker({
        showOn: "both",
        buttonImage: "img/calendar.png",
        buttonImageOnly: true,
        defaultDate: '+1w',
        dateFormat: 'dd/mm/yy',
        changeMonth: true,
        numberOfMonths: 2,
        yearRange: "-100:+0",
        onClose: function (selectedDate) {
            $('.returnDate').datepicker('option', 'minDate', selectedDate);
        }
    });
    
    // fungsi untuk memilih tanggal kembali
    $('.searchForm .returnDate').datepicker({
        showOn: "both",
        buttonImage: "img/calendar.png",
        buttonImageOnly: true,
        defaultDate: '+1w',
        dateFormat: 'dd/mm/yy',
        changeMonth: true,
        numberOfMonths: 2,
        yearRange: "-100:+0",
        onClose: function (selectedDate) {
            $('.departDate').datepicker('option', 'maxDate', selectedDate);
        }
    });

    // fungsi untuk buka pilihan maskapai
//    $('.pMBtn').click(function () {
//        $(this).find('.pMBText').addClass('pMBTAct');
//        $(this).find('.aLOption').show();
//    });

    $('.pMBOpen').click(function () {
        $(this).hide();
        $('.pMBClose').show();
        $('.aLOption').show(); //.show();
    });

    $('.pMBClose').click(function () {
        $(this).hide();
        $('.pMBOpen').show();
        $('.aLOption').hide();   //hide();
    });

//    $(document).click(function(e){
//        if($(e.target).closest('.pMBtn').length != 0) {
//            return false;
//        }
//        $('.pMBClose').hide();
//        $('.pMBOpen').show();
//        $('.aLOption').hide();
//    });

    $(document).click(function(e){
        var target = e.target;
        console.log("target.className=" + target.className);
        if (target.className.indexOf("aLList") >= 0  || target.className.indexOf("airLogo") >= 0 ||
            target.className.indexOf("aLOption rounded8") >= 0 || target.className.indexOf("pMBOpen") >= 0 ||
            target.className.indexOf("accountData") >= 0 || target.className.indexOf("airlineName") >= 0 )  {
            $('.pMBClose').show();
            $('.pMBOpen').hide();
            $('.aLOption').show();
            return false;
        }
        $('.pMBClose').hide();
        $('.pMBOpen').show();
        $('.aLOption').hide();
    });

    $('.airLine').mouseover(function(){
        $(this).find('.hoverClose').show();
    }).mouseout(function(){
        $(this).find('.hoverClose').hide();
    })

    $('.hoverClose').click(function(){
        var findCheckboxBind = $(this).parent().attr('id');
        $('input[value="'+findCheckboxBind+'"].mKPL').removeAttr('checked');
        $('input[value="'+findCheckboxBind+'"].mKPL').parent().removeClass('wEnb');
        $(this).parent().hide();
    })
    
    // fungsi jumlah penumpan (dewasa, anak, bayi)
    $('#fieldAdultValue').val(adultPassDef);
    $('#fieldChildValue, #fieldBabyValue').val(childBabyPassDef);

    // fungsu penambahan jumlah penumpang
    $('.reduceValue').click(function () {
        var getInputValue = $(this).parent().find('.passCount').val(),
            getInputID = $(this).parent().find('.passCount').attr('id'),
            decval = parseInt(getInputValue, 10) - 1;
        if (getInputID === 'fieldAdultValue') {
            if (getInputValue > adultPassDef) {
                $(this).parent().find('.passCount').val(decval);
            }
        } else {
            if (getInputValue > childBabyPassDef) {
                $(this).parent().find('.passCount').val(decval);
            }
        }
    });
    
    // fungsu pengurangan jumlah penumpang
    $('.increaseValue').click(function () {
        var getInputValue = $(this).parent().find('.passCount').val(),
            getInputID = $(this).parent().find('.passCount').attr('id'),
            incval = parseInt(getInputValue, 10) + 1;
        if (getInputID === 'fieldBabyValue') {
            if (getInputValue < childBabyPassMax) {
                $(this).parent().find('.passCount').val(incval);
            }
        } else {
            if (getInputValue < adultChildPassMax) {
                $(this).parent().find('.passCount').val(incval);
            }
        }
    });

    // fungsi mendeteksi jumlah penumpang
    // dimana bila lebih tinggi dari yang di tentukan maka akan memasukkan nilai maximal
    $('.passCount').change(function () {
        var getInputValue = $(this).val(),
            getInputID = $(this).attr('id');
        if (getInputID === 'fieldBabyValue') {
            
            if (getInputValue > childBabyPassMax) {
                alert('Maaf, Jumlah tidak boleh lebih dari ' + childBabyPassMax);
                $(this).val(childBabyPassMax);
            }
        } else {
            if (getInputValue > adultChildPassMax) {
                alert('Maaf, Jumlah tidak boleh lebih dari ' + adultChildPassMax);
                $(this).val(adultChildPassMax);
            }
        }
    });
    
    // fungsi untuk binding checkbox
    $('.aLList').bind('click', function () {
        $(this).find('input').click();
    });
    console.log("function checkbox change");
    // fungsi checkbox beserta properti styling
    checkboxChanging($);


    // Parse Json
    function format(item) { return item.label; }

    $('.departInput').select2({placeholder: 'Kota asal',
            data: { results: arrAirportsDepart, text: 'label' },
            val: "",
            formatSelection: format,
            formatResult: format});
    $('.arriveInput').select2({placeholder: 'Kota tujuan',
            data: { results: arrAirportsReturn, text: 'label' },
            val: "",
            formatSelection: format,
            formatResult: format});
    $('.providerInput').select2({placeholder: 'Penyedia Jasa',
            data: { results: availableProvider, text: 'label' },
            minimumResultsForSearch: -1,
            formatSelection: format,
            formatResult: format});

    $('.departInput').on("change", function() {
        var $this = $(this);
        console.log('autosubmitting, $this.val()=' + $this.val());
        var destination = null;
        if (hiddenOftenRoute != null) {
            $.each(JSON.parse(hiddenOftenRoute), function(idx, obj) {
                if (destination == null && $this.val() == obj.data2) {
                    destination = obj.data3;
                }
            });
            console.log('route destination=' + destination);
            $('#arriveInput').select2("val", destination);
        }
    });

/*
    valDepartReturn = 'vDepartReturn';
    $('.departReturn li').click(function() {
        valDepartReturn = $(this).attr('value');
    });
*/


    $('#form').submit(function() {
        console.log("di dalam submit");
        $("#terror ul").empty();
        var isError = false;

        console.log("$('#hiddenAirasia').val()=" + $('#hiddenAirasia').val());
        var hidAirasia = $('#hiddenAirasia').val(); var hidCitilink = $('#hiddenCitilink').val();
        var hidGaruda = $('#hiddenGaruda').val(); var hidGarudaExec = $('#hiddenGarudaExec').val();
        var hidSriwijaya = $('#hiddenSriwijaya').val(); var hidLionair = $('#hiddenLionair').val();
        var hidKalstar = $('#hiddenKalstar').val(); var hidTiger = $('#hiddenTiger').val();
        var hidXpress = $('#hiddenXpress').val(); var hidAirasiaMalaysia = $('#hiddenAirasiaMalaysia').val();
        var hidAviaStar = $('#hiddenAviaStar').val(); var hidBatikAir = $('#hiddenBatikAir').val();
        var hidMalindo = $('#hiddenMalindo').val(); var hidNamAir = $('#hiddenNamAir').val();
        var hidSkyAviation = $('#hiddenSkyAviation').val(); var hidThaiLion =  $('#hiddenThaiLion').val();
        var hidTigerAirSing = $('#hiddenTigerAirSing').val(); var hidTriganaAir = $('#hiddenTriganaAir').val();
        var hidWIngsAir = $('#hiddenWingsAir').val();

        if (!hidAirasia && !hidCitilink && !hidGaruda && !hidGarudaExec && !hidSriwijaya &&
            !hidLionair && !hidKalstar && !hidTiger && !hidXpress && !hidAirasiaMalaysia &&
            !hidAviaStar && !hidBatikAir && !hidMalindo && !hidNamAir && !hidSkyAviation &&
            !hidThaiLion && !hidTigerAirSing && !hidTriganaAir && !hidWIngsAir
            ) {
            $("#terror ul").append('<li>Silahkan memilih Maskapai Penerbangan</li>');
            isError = true;
        }

        var departInput = $('#departInput').val();
        var arriveInput = $('#arriveInput').val();
        var providerInput = $('#providerInput').val();

        if (!departInput) {
            $("#terror ul").append("<li >Silahkan memilih Kota Keberangkatan</li>");
            isError = true;
        }
        if (!arriveInput) {
            $("#terror ul").append('<li>Silahkan memilih Kota Tujuan</li>');
            isError = true;
        } else {
            if (departInput == arriveInput) {
                $("#terror ul").append('<li>Kota Tujuan harus berbeda dengan Kota Keberangkatan</li>');
            }
        }
        if (!providerInput) {
            $("#terror ul").append('<li>Silahkan memilih Provider</li>');
            isError = true;
        }

        var departDate = $('#departDate').val();
        var returnDate = $('#returnDate').val();

        if (!departDate) {
            $("#terror ul").append("<li>Silahkan memilih Tanggal Keberangkatan</li>");
            isError = true;
        } else {
            var currentDate = new Date();
            console.log("currentDate=" + currentDate);

            var selectedDepartDate = new Date(departDate.substring(6), departDate.substring(3, 5) - 1, departDate.substring(0, 2));
            console.log("selectedDepartDate=" + selectedDepartDate);

            if(currentDate > selectedDepartDate){
                $("#terror ul").append("<li>Tanggal Keberangkatan Harus Hari Ini Atau Setelahnya</li>");
                isError = true;
            }
        }
        if (valDepartReturn == 'vDepartReturn') {
            if (!returnDate) {
                $("#terror ul").append('<li>Silahkan memilih Tanggal Kembali</li>');
                isError = true;
            } else {
                var selectedDepartDate = new Date(departDate.substring(6), departDate.substring(3, 5) - 1, departDate.substring(0, 2));
                var selectedReturnDate = new Date(returnDate.substring(6), returnDate.substring(3, 5) - 1, returnDate.substring(0, 2));

                if(selectedReturnDate < selectedDepartDate){
                    $("#terror ul").append("<li>Tanggal Kembali Harus Setelah Tanggal Berangkat</li>");
                    isError = true;
                }
            }
        }

        var fieldAdultValue = parseInt($('#fieldAdultValue').val()) ;
        console.log("fieldAdultValue=" + fieldAdultValue);
        var fieldChildValue = parseInt($('#fieldChildValue').val()) ;
        var fieldBabyValue = parseInt($('#fieldBabyValue').val()) ;

        if ((fieldAdultValue + fieldChildValue + fieldBabyValue) > 7) {
            $("#terror ul").append("<li>Total Penumpang Maksimal 7 Orang</li>");
            isError = true;
        }
        if (fieldAdultValue < fieldBabyValue) {
            $("#terror ul").append("<li>Jumlah Penumpang Dewasa Harus Lebih Banyak Daripada Jumlah Penumpang Bayi</li>");
            isError = true;
        }

        if (isError) {
            $('#tbanner').show(); $('#terror').show();
            return false;
        } else {
            $('#tbanner').hide(); $('#terror').hide();
        }
        return true;
    });

    postMostOftenAirportsDepart($);
});

function postMostOftenAirportsDepart($) {
    $.ajax({
        url: hiddenServerIp + "/rest/aero/getOftenAirportsPost",
        type: 'POST',
        data: 'customerId=' + hiddenCustomerId + "&fromOrTo=FROM",
        success: function (res) {
            console.log("res=" + res);

            hiddenOftenDepart = JSON.stringify(res);
            console.log("hiddenOftenDepart=" + hiddenOftenDepart);

            if (hiddenOftenDepart && (hiddenOftenDepart != "") ) {
                $.each(JSON.parse(hiddenOftenDepart), function(idx, obj) {
                    arrMostOftenDepart.push({id: obj.airportCode, label: obj.airportCity + ", " + obj.airportCode});
                });
                arrAirportsDepart.push({label: 'Pilihan Tersering', children: arrMostOftenDepart});
            }
            postMostOftenAirportsReturn($);
        } ,
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log("Status: " + textStatus);
            console.log("Error: " + errorThrown);
        }
    });
}

function postMostOftenAirportsReturn($) {
    $.ajax({
        url: hiddenServerIp + "/rest/aero/getOftenAirportsPost",
        type: 'POST',
        data: 'customerId=' + hiddenCustomerId + "&fromOrTo=TO",
        success: function (res) {
            console.log("res=" + res);

            hiddenOftenReturn = JSON.stringify(res);
            console.log("hiddenOftenReturn=" + hiddenOftenReturn);

            if (hiddenOftenReturn && (hiddenOftenReturn != "") ) {
                $.each(JSON.parse(hiddenOftenReturn), function(idx, obj) {
                    arrMostOftenReturn.push({id: obj.airportCode, label: obj.airportCity + ", " + obj.airportCode});
                });
                arrAirportsReturn.push({label: 'Pilihan Tersering', children: arrMostOftenReturn});
            }
            postAirports($);
        } ,
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log("Status: " + textStatus);
            console.log("Error: " + errorThrown);
        }
    });
}

function postAirports($) {
    $.ajax({
        url: hiddenServerIp + "/rest/aero/getAirportsPost",
        type: 'POST',
        data: 'json=nothing',
        success: function (res) {
            console.log("res=" + res);

            hiddenAirports = JSON.stringify(res);
            console.log("hiddenAirports=" + hiddenAirports);

            $.each(JSON.parse(hiddenAirports), function(idx, obj) {
                arrAllAirports.push({id: obj.airportCode, label: obj.airportCity + ", " + obj.airportCode});
            });

            arrAirportsDepart.push({label: 'Pilih Kota', children: arrAllAirports});

            arrAirportsReturn.push({label: 'Pilih Kota', children: arrAllAirports});

            postOftenRoutes($);
        } ,
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log("Status: " + textStatus);
            console.log("Error: " + errorThrown);
        }
    });
}

function postOftenRoutes($) {
    $.ajax({
        url: hiddenServerIp + "/rest/aero/getOftenRoutesPost",
        type: 'POST',
        data: 'customerId=' + hiddenCustomerId,
        success: function (res) {
            console.log("res=" + res);

            hiddenOftenRoute = JSON.stringify(res);
            console.log("hiddenOftenRoute=" + hiddenOftenRoute);

            setAirportAndPassenger($);
        } ,
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log("Status: " + textStatus);
            console.log("Error: " + errorThrown);
        }
    });
}

function setAirportAndPassenger($) {
//if aerotiketingview not null set airport from, to, totPassanger
    console.log("hiddenAeroTicketingView=" + hiddenAeroTicketingView);
    hiddenAeroTicketingView = $('#hiddenAeroTicketingView').val();
    if (hiddenAeroTicketingView) {
        var objAeroTiketingView = JSON && JSON.parse(hiddenAeroTicketingView) || $.parseJSON(hiddenAeroTicketingView);

        $('#departInput').select2("val", objAeroTiketingView.departureAirportCode);
        $('#arriveInput').select2("val", objAeroTiketingView.destinationAirportCode);

        $('#fieldAdultValue').val(objAeroTiketingView.totalAdult);
        $('#fieldChildValue').val(objAeroTiketingView.totalChildren);
        $('#fieldBabyValue').val(objAeroTiketingView.totalInfant);

        if (!objAeroTiketingView.isDepartOnly) {
            $('.returnDateContainer').show();
            document.getElementById('airType').value = 'return';
            $('#vDepartReturn').addClass('active');
            $('#vDepartOnly').removeClass('active');

            valDepartReturn = 'vDepartReturn';
        } else {
            $('.returnDateContainer').hide();
            document.getElementById('airType').value = 'oneWay';
            $('#vDepartReturn').removeClass('active');
            $('#vDepartOnly').addClass('active');

            valDepartReturn = 'vDepartOnly';
        }
    }
}

function getAirlineLogo(airlineCode) {

       if (airlineCode == 'QZ') return "logoAirasia";
       else if (airlineCode == 'QG') return "logoCitilink";
       else if (airlineCode == 'GA') return "logoGaruda";
       else if (airlineCode == 'SJ') return "logoSriwijaya";
       else if (airlineCode == 'JT') return "logoLion";
       else if (airlineCode == 'KD') return "logoKalstar";
       else if (airlineCode == 'GE') return "logoGE";
       else if (airlineCode == 'XP') return "logoXpress";

       else if (airlineCode == 'AK') return "logoAirasiaMalaysia";
       else if (airlineCode == 'MV') return "logoAviaStar";
       else if (airlineCode == 'ID') return "logoBatikAir";
       else if (airlineCode == 'OD') return "logoMalindo";
       else if (airlineCode == 'XX') return "logoNamAir";
       else if (airlineCode == 'SY') return "logoSkyAviation";
       else if (airlineCode == 'SL') return "logoThaiLion";
       else if (airlineCode == 'RI') return "logoTiger";
       else if (airlineCode == 'TR') return "logoTigerAirSing";
       else if (airlineCode == 'IL') return "logoTriganaAir";
       else if (airlineCode == 'IW') return "logoWingsAir";

}

function setHiddenAirlineValue(logo) {
    if (logo.match("logoAirasia")) { document.getElementById('hiddenAirasia').value = 'QZ'; }
    else if (logo.match("logoCitilink")) { document.getElementById('hiddenCitilink').value = 'QG'; }
    else if (logo.match("logoGaruda")) { document.getElementById('hiddenGaruda').value = 'GA'; }
    else if (logo.match("logoGE")) { document.getElementById('hiddenGarudaExec').value = 'GE'; }
    else if (logo.match("logoKalstar")) { document.getElementById('hiddenKalstar').value = 'KD'; }
    else if (logo.match("logoLion")) { document.getElementById('hiddenLionair').value = 'JT'; }
    else if (logo.match("logoSriwijaya")) { document.getElementById('hiddenSriwijaya').value = 'SJ'; }
    else if (logo.match("logoXpress")) { document.getElementById('hiddenXpress').value = 'XP'; }

    else if (logo.match("logoAirasiaMalaysia")) { document.getElementById('hiddenAirasiaMalaysia').value = 'AK'; }
    else if (logo.match("logoAviaStar")) { document.getElementById('hiddenAviaStar').value = 'MV'; }
    else if (logo.match("logoBatikAir")) { document.getElementById('hiddenBatikAir').value = 'ID'; }
    else if (logo.match("logoMalindo")) { document.getElementById('hiddenMalindo').value = 'OD'; }
    else if (logo.match("logoNamAir")) { document.getElementById('hiddenNamAir').value = 'XX'; }
    else if (logo.match("logoSkyAviation")) { document.getElementById('hiddenSkyAviation').value = 'SY'; }
    else if (logo.match("logoThaiLion")) { document.getElementById('hiddenThaiLion').value = 'SL'; }
    else if (logo.match("logoTiger")) { document.getElementById('hiddenTiger').value = 'RI'; }
    else if (logo.match("logoTigerAirSing")) { document.getElementById('hiddenTigerAirSing').value = 'TR'; }
    else if (logo.match("logoTriganaAir")) { document.getElementById('hiddenTriganaAir').value = 'IL'; }
    else if (logo.match("logoWingsAir")) { document.getElementById('hiddenWingsAir').value = 'IW'; }
}

function resetAllAirlines() {
    resetHiddenAirlineValue("logoAirasia"); resetHiddenAirlineValue("logoSriwijaya");
    resetHiddenAirlineValue("logoCitilink"); resetHiddenAirlineValue("logoGaruda");
    resetHiddenAirlineValue("logoGE"); resetHiddenAirlineValue("logoKalstar");
    resetHiddenAirlineValue("logoLion"); resetHiddenAirlineValue("logoXpress");

    resetHiddenAirlineValue("logoAirasiaMalaysia"); resetHiddenAirlineValue("logoAviaStar");
    resetHiddenAirlineValue("logoBatikAir"); resetHiddenAirlineValue("logoMalindo");
    resetHiddenAirlineValue("logoNamAir"); resetHiddenAirlineValue("logoSkyAviation");
    resetHiddenAirlineValue("logoThaiLion"); resetHiddenAirlineValue("logoTigerAirSing");
    resetHiddenAirlineValue("logoTiger");
    resetHiddenAirlineValue("logoTriganaAir"); resetHiddenAirlineValue("logoWingsAir");
}

function resetHiddenAirlineValue(logo) {
    if (logo.match("logoAirasia")) { document.getElementById('hiddenAirasia').value = ''; }
    else if (logo.match("logoCitilink")) { document.getElementById('hiddenCitilink').value = ''; }
    else if (logo.match("logoGaruda")) { document.getElementById('hiddenGaruda').value = ''; }
    else if (logo.match("logoGE")) { document.getElementById('hiddenGarudaExec').value = ''; }
    else if (logo.match("logoKalstar")) { document.getElementById('hiddenKalstar').value = ''; }
    else if (logo.match("logoLion")) { document.getElementById('hiddenLionair').value = ''; }
    else if (logo.match("logoXpress")) { document.getElementById('hiddenXpress').value = ''; }
    else if (logo.match("logoSriwijaya")) { document.getElementById('hiddenSriwijaya').value = ''; }

    else if (logo.match("logoAirasiaMalaysia")) { document.getElementById('hiddenAirasiaMalaysia').value = ''; }
    else if (logo.match("logoAviaStar")) { document.getElementById('hiddenAviaStar').value = ''; }
    else if (logo.match("logoBatikAir")) { document.getElementById('hiddenBatikAir').value = ''; }
    else if (logo.match("logoMalindo")) { document.getElementById('hiddenMalindo').value = ''; }
    else if (logo.match("logoNamAir")) { document.getElementById('hiddenNamAir').value = ''; }
    else if (logo.match("logoSkyAviation")) { document.getElementById('hiddenSkyAviation').value = ''; }
    else if (logo.match("logoThaiLion")) { document.getElementById('hiddenThaiLion').value = ''; }
    else if (logo.match("logoTiger")) { document.getElementById('hiddenTiger').value = ''; }
    else if (logo.match("logoTigerAirSing")) { document.getElementById('hiddenTigerAirSing').value = ''; }
    else if (logo.match("logoTriganaAir")) { document.getElementById('hiddenTriganaAir').value = ''; }
    else if (logo.match("logoWingsAir")) { document.getElementById('hiddenWingsAir').value = ''; }
}

function checkboxChanging($) {
    $('.mKPL').change(function () {     //input[type="checkbox"]
        console.log("$(this).val()=" + $(this).val());
        var current = $('input[type="checkbox"].mKPL').filter(':checked').length,
            checkClass = $(this).parent().hasClass('wEnb'),
            mKPLogo = $(this).val();
        $('input[type="checkbox"].mKPL').filter(':not(:checked)').prop('disabled', current >= max);
        if (current >= max) {
            $('.aLList').addClass('wDsb');
            $('.wDsb').find('.aLTrans').show();
        } else {
            $('.aLList').removeClass('wDsb');
            $('.wEnb').find('.aLTrans').hide();
        }

        if ((checkClass !== true) && (current <= max + 1)) {
            console.log("$(this)=" + $(this));
            console.log("$(this).parent()=" + $(this).parent());
            console.log("mKPLogo=" + mKPLogo);
            $(this).parent().addClass('wEnb');
            $('.airLines').find('#' + mKPLogo).show();
            setHiddenAirlineValue(mKPLogo);
        } else {
            $(this).parent().removeClass('wEnb');
            $('.airLines').find('#' + mKPLogo).hide();
            resetHiddenAirlineValue(mKPLogo);
        }
        if (current >= 1) {
            $('.resetPBtnDsb, .submitPBtnDsb').hide();
            $('.resetPBtn, .submitPBtn').show();
        } else {
            $('.resetPBtn, .submitPBtn').hide();
            $('.resetPBtnDsb, .submitPBtnDsb').show();
        }
    });
}

function isNumberKey(evt)
{
    var charCode = (evt.which) ? evt.which : event.keyCode;
    if (!((charCode >= 48 && charCode <= 57) || charCode == 08 || charCode == 127)) return false;
    return true;
}
