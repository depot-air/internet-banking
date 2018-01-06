var hiddenAeroTicketingView = "";
var hiddenCountries = "", hiddenContacts = "", hiddenCustRegAdult = "", hiddenCustRegChild = "", hiddenCustRegInfant = "";
var hiddenContactPerson = "", hiddenAeroPassenger1 = "", hiddenAeroPassenger2 = "", hiddenAeroPassenger3 = "", hiddenAeroPassenger4 = "", hiddenAeroPassenger5 = "", hiddenAeroPassenger6 = "", hiddenAeroPassenger7 = "";
var objAeroTicketing, objContactPerson, objAeroPassenger1, objAeroPassenger2, objAeroPassenger3, objAeroPassenger4, objAeroPassenger5, objAeroPassenger6, objAeroPassenger7;
var arrTitle = [];
var arrTitleChild = [];
var arrCountries = [];
var arrContacts = [];
var arrPassengers = [];
var arrPassengersChild = [];
var arrPassengersInfant = [];
var arrPassParents = [];

var ID_NINES = 999999999999;
var INDONESIA = "INDONESIA";
var MR = "Mr";
var MSTR = "Mstr";
var CHD = "CHD";
var ADULT = "Adult";
var CHILD = "Child";
var INFANT = "Infant";
var VOLT = "VOLT";
var ADULT_ASSOC = "Adult Assoc.";

function format(item) { return item.label; }
function setSelectTitle($) {
    $('.selectTitle').select2({
        minimumResultsForSearch: -1,
        data: { results: arrTitle, text: 'label' },
        val: "", formatSelection: format, formatResult: format
    })
}
function setSelectTitleChild($) {
    $('.selectTitleChild').select2({
        minimumResultsForSearch: -1,
        data: { results: arrTitleChild, text: 'label' },
        val: "", formatSelection: format, formatResult: format
    })
}

function setContactFromList($) {
    $('.contactFromList').select2({
        minimumResultsForSearch: -1,
        data: { results: arrContacts, text: 'label' },
        val: "", formatSelection: format, formatResult: format
    });
}

function setPass1FromList($) {
    $('#pass1FromList').select2({
        minimumResultsForSearch: -1,
        data: { results: arrPassengers, text: 'label' },
        val: "", formatSelection: format, formatResult: format
    });
}

function setPass2FromList($) {
    $('#pass2FromList').select2({
        data: { results: (objAeroPassenger2.passengerType == ADULT) ? arrPassengers :
            (objAeroPassenger2.passengerType == CHILD) ? arrPassengersChild : arrPassengersInfant, text: 'label' },
        minimumResultsForSearch: -1, val: "", formatSelection: format, formatResult: format
    });
}

function setPass3FromList($) {
    $('#pass3FromList').select2({
        data: { results: (objAeroPassenger3.passengerType == ADULT) ? arrPassengers :
            (objAeroPassenger3.passengerType == CHILD) ? arrPassengersChild : arrPassengersInfant, text: 'label' },
        minimumResultsForSearch: -1, val: "", formatSelection: format, formatResult: format
    });
}

function setPass4FromList($) {
    $('#pass4FromList').select2({
        data: { results: (objAeroPassenger4.passengerType == ADULT) ? arrPassengers :
            (objAeroPassenger4.passengerType == CHILD) ? arrPassengersChild : arrPassengersInfant, text: 'label' },
        minimumResultsForSearch: -1, val: "", formatSelection: format, formatResult: format
    });
}

function setPass5FromList($) {
    $('#pass5FromList').select2({
        data: { results: (objAeroPassenger5.passengerType == ADULT) ? arrPassengers :
            (objAeroPassenger5.passengerType == CHILD) ? arrPassengersChild : arrPassengersInfant, text: 'label' },
        minimumResultsForSearch: -1, val: "", formatSelection: format, formatResult: format
    });
}

function setPass6FromList($) {
    $('#pass6FromList').select2({
        data: { results: (objAeroPassenger6.passengerType == ADULT) ? arrPassengers :
            (objAeroPassenger6.passengerType == CHILD) ? arrPassengersChild : arrPassengersInfant, text: 'label' },
        minimumResultsForSearch: -1, val: "", formatSelection: format, formatResult: format
    });
}

function setPass7FromList($) {
    $('#pass7FromList').select2({
        data: { results: (objAeroPassenger7.passengerType == ADULT) ? arrPassengers :
            (objAeroPassenger7.passengerType == CHILD) ? arrPassengersChild : arrPassengersInfant, text: 'label' },
        minimumResultsForSearch: -1, val: "", formatSelection: format, formatResult: format
    });
}

function setPassCountries($) {
    $('.passCountry').select2({
        data: { results: arrCountries, text: 'label' },
        val: "", formatSelection: format, formatResult: format
    });
}

function setPassParents($) {
    $('.passParent').select2({
        data: { results: arrPassParents, text: 'label' },
        val: "", formatSelection: format, formatResult: format
    });
    if (!$('.passParent').val()) $('.passParent').select2("val", ADULT_ASSOC + "1");
}

jQuery(document).ready(function ($) {
    //location.reload(true);
    'use strict';
    $('#tbanner').hide(); $('#terror').hide();

    var departQR = $('.departIDetail .fCCode').html(),
        returnQR = $('.returnIDetail .fCCode').html();

    var hiddenAccount = $('#hiddenAccount').val();
    console.log("hiddenAccount=" + hiddenAccount);
    var objAccount = JSON && JSON.parse(hiddenAccount) || $.parseJSON(hiddenAccount);

    hiddenContacts = $('#hiddenContacts').val();
    console.log("hiddenContact=" + hiddenContacts);

    hiddenCustRegAdult = $('#hiddenCustRegAdult').val();
    hiddenCustRegChild = $('#hiddenCustRegChild').val();
    hiddenCustRegInfant = $('#hiddenCustRegInfant').val();
    console.log("hiddenCustRegAdult=" + hiddenCustRegAdult + " hiddenCustRegChild=" + hiddenCustRegChild + " hiddenCustRegInfant=" + hiddenCustRegInfant);

    hiddenCountries = $('#hiddenCountries').val();
    console.log("hiddenCountries=" + hiddenCountries);

    arrContacts.push({id: ID_NINES, label: 'Pilih Contact Person'});
    $.each(JSON.parse(hiddenContacts), function(idx, obj) {
        arrContacts.push({id: obj.id, label: obj.customerReference});
    });

    arrPassengers.push({id: ID_NINES, label: 'Pilih Penumpang'});
    arrPassengersChild.push({id: ID_NINES, label: 'Pilih Penumpang'});
    arrPassengersInfant.push({id: ID_NINES, label: 'Pilih Penumpang'});
    $.each(JSON.parse(hiddenCustRegAdult), function(idx, obj) {
        arrPassengers.push({id: obj.id, label: obj.customerReference});
    });
    $.each(JSON.parse(hiddenCustRegChild), function(idx, obj) {
        arrPassengersChild.push({id: obj.id, label: obj.customerReference});
    });
    $.each(JSON.parse(hiddenCustRegInfant), function(idx, obj) {
        arrPassengersInfant.push({id: obj.id, label: obj.customerReference});
    });
    console.log("arrPassengers=" + arrPassengers + " arrPassengersChild=" + arrPassengersChild + " arrPassengersInfant=" + arrPassengersInfant);

    $.each(JSON.parse(hiddenCountries), function(idx, obj) {
        arrCountries.push({id: obj, label: obj});
    });
    console.log("arrCountries=" + arrCountries);

    hiddenContactPerson = $('#hiddenContactPerson').val();
    hiddenAeroPassenger1 = $('#hiddenAeroPassenger1').val();
    hiddenAeroPassenger2 = $('#hiddenAeroPassenger2').val(); hiddenAeroPassenger5 = $('#hiddenAeroPassenger5').val();
    hiddenAeroPassenger3 = $('#hiddenAeroPassenger3').val(); hiddenAeroPassenger6 = $('#hiddenAeroPassenger6').val();
    hiddenAeroPassenger4 = $('#hiddenAeroPassenger4').val(); hiddenAeroPassenger7 = $('#hiddenAeroPassenger7').val();

    console.log("hiddenAeroPassenger2=" + hiddenAeroPassenger2 + " hiddenAeroPassenger3=" + hiddenAeroPassenger3 +
        "hiddenAeroPassenger4=" + hiddenAeroPassenger4 + " hiddenAeroPassenger5=" + hiddenAeroPassenger5 +
        "hiddenAeroPassenger6=" + hiddenAeroPassenger6 + " hiddenAeroPassenger7=" + hiddenAeroPassenger7);

    if (hiddenContactPerson != null) objContactPerson = JSON && JSON.parse(hiddenContactPerson) || $.parseJSON(hiddenContactPerson);
    if (hiddenAeroPassenger1 != null) objAeroPassenger1 = JSON && JSON.parse(hiddenAeroPassenger1) || $.parseJSON(hiddenAeroPassenger1);
    if (hiddenAeroPassenger2 != null) objAeroPassenger2 = JSON && JSON.parse(hiddenAeroPassenger2) || $.parseJSON(hiddenAeroPassenger2);
    if (hiddenAeroPassenger3 != null) objAeroPassenger3 = JSON && JSON.parse(hiddenAeroPassenger3) || $.parseJSON(hiddenAeroPassenger3);
    if (hiddenAeroPassenger4 != null) objAeroPassenger4 = JSON && JSON.parse(hiddenAeroPassenger4) || $.parseJSON(hiddenAeroPassenger4);
    if (hiddenAeroPassenger5 != null) objAeroPassenger5 = JSON && JSON.parse(hiddenAeroPassenger5) || $.parseJSON(hiddenAeroPassenger5);
    if (hiddenAeroPassenger6 != null) objAeroPassenger6 = JSON && JSON.parse(hiddenAeroPassenger6) || $.parseJSON(hiddenAeroPassenger6);
    if (hiddenAeroPassenger7 != null) objAeroPassenger7 = JSON && JSON.parse(hiddenAeroPassenger7) || $.parseJSON(hiddenAeroPassenger7);

    console.log("objAeroPassenger2=" + objAeroPassenger2 + " objAeroPassenger3=" + objAeroPassenger3 +
            "objAeroPassenger4=" + objAeroPassenger4 + " objAeroPassenger5=" + objAeroPassenger5 +
            "objAeroPassenger6=" + objAeroPassenger6 + " objAeroPassenger7=" + objAeroPassenger7);

    hiddenAeroTicketingView = $('#hiddenAeroTicketingView').val();
    console.log("hiddenAeroTicketingView=" + hiddenAeroTicketingView);
    objAeroTicketing = JSON && JSON.parse(hiddenAeroTicketingView) || $.parseJSON(hiddenAeroTicketingView);
    console.log("objAeroTicketing=" + objAeroTicketing);
    console.log("objAeroTicketing.totalAdult=" + objAeroTicketing.totalAdult);


    var i;
    for (i = 0; i < objAeroTicketing.totalAdult; i++) {
        arrPassParents.push({id: ADULT_ASSOC + (i + 1), label: ADULT_ASSOC + (i + 1)});
    }
    setPassParents($);

    arrTitle.push({id: MR, label: MR});
    arrTitle.push({id: "Mrs", label: "Mrs"});
    arrTitle.push({id: "Ms", label: "Ms"});

    if (objAeroTicketing.providerCode == VOLT) {
        arrTitleChild.push({id: MSTR, label: MSTR});
        arrTitleChild.push({id: "Miss", label: "Miss"});
    } else {
        arrTitleChild.push({id: CHD, label: CHD});
    }

    setSelectTitle($);
    if (!$('#contactTitle').val()) $('#contactTitle').select2("val", MR);
    if (!$('#pass1Title').val()) $('#pass1Title').select2("val", MR);

    setSelectTitleChild($);
    
    setContactFromList($);
    if (!$('#contactFromList').val()) $('#contactFromList').select2("val", ID_NINES);

    setPass1FromList($);
    if (!$('#pass1FromList').val()) $('#pass1FromList').select2("val", ID_NINES);

    if (objAeroPassenger2 != null) {
        setPass2FromList($);
        if (!$('#pass2FromList').val()) $('#pass2FromList').select2("val", ID_NINES);
        if (objAeroPassenger2.passengerType == ADULT) {
            if (!$('#pass2Title').val()) $('#pass2Title').select2("val", MR);
        } else {
            if (!$('#pass2TitleChild').val()) $('#pass2TitleChild').select2("val", (objAeroTicketing.providerCode == VOLT) ? MSTR : CHD);
        }
    }

    if (objAeroPassenger3 != null) {
        setPass3FromList($);
        if (!$('#pass3FromList').val()) $('#pass3FromList').select2("val", ID_NINES);
        if (objAeroPassenger3.passengerType == ADULT) {
            if (!$('#pass3Title').val()) $('#pass3Title').select2("val", MR);
        } else {
            if (!$('#pass3TitleChild').val()) $('#pass3TitleChild').select2("val", (objAeroTicketing.providerCode == VOLT) ? MSTR : CHD);
        }
    }

    if (objAeroPassenger4 != null) {
        setPass4FromList($);
        if (!$('#pass4FromList').val()) $('#pass4FromList').select2("val", ID_NINES);
        if (objAeroPassenger4.passengerType == ADULT) {
            if (!$('#pass4Title').val()) $('#pass4Title').select2("val", MR);
        } else {
            if (!$('#pass4TitleChild').val()) $('#pass4TitleChild').select2("val", (objAeroTicketing.providerCode == VOLT) ? MSTR : CHD);
        }
    }

    if (objAeroPassenger5 != null) {
        setPass5FromList($);
        if (!$('#pass5FromList').val()) $('#pass5FromList').select2("val", ID_NINES);
        if (objAeroPassenger5.passengerType == ADULT) {
            if (!$('#pass5Title').val()) $('#pass5Title').select2("val", MR);
        } else {
            if (!$('#pass5TitleChild').val()) $('#pass5TitleChild').select2("val", (objAeroTicketing.providerCode == VOLT) ? MSTR : CHD);
        }
    }

    if (objAeroPassenger6 != null) {
        setPass6FromList($);
        if (!$('#pass6FromList').val()) $('#pass6FromList').select2("val", ID_NINES);
        if (objAeroPassenger6.passengerType == ADULT) {
            if (!$('#pass6Title').val()) $('#pass6Title').select2("val", MR);
        } else {
            if (!$('#pass6TitleChild').val()) $('#pass6TitleChild').select2("val", (objAeroTicketing.providerCode == VOLT) ? MSTR : CHD);
        }
    }

    if (objAeroPassenger7 != null) {
        setPass7FromList($);
        if (!$('#pass7FromList').val()) $('#pass7FromList').select2("val", ID_NINES);
        if (objAeroPassenger7.passengerType == ADULT) {
            if (!$('#pass7Title').val()) $('#pass7Title').select2("val", MR);
        } else {
            if (!$('#pass7TitleChild').val()) $('#pass7TitleChild').select2("val", (objAeroTicketing.providerCode == VOLT) ? MSTR : CHD);
        }
    }

    setPassCountries($);
    if (!$('#pass1Country').val()) $('#pass1Country').select2("val",INDONESIA);

    var departureFlight = objAeroTicketing.departureFlight;
    console.log("departureFlight=" + departureFlight);
    try {
        var departureConnecting = departureFlight.aeroConnectingFlight;
        console.log("departureConnecting=" + departureConnecting);

        $('#departConnecting').show();

        $('#departConnectingFrom').html(departureConnecting.connectingFlightFrom);
        console.log("departureConnecting.connectingFlightFrom=" + departureConnecting.connectingFlightFrom);

        $('#departConnectingTo').html(departureConnecting.connectingFlightTo);
        console.log("departureConnecting.connectingFlightTo=" + departureConnecting.connectingFlightTo);

        $('#departConnectingEtd').html(hhmm(toJavascriptDate(departureConnecting.connectingFlightEtd)));
        console.log("departureConnecting.connectingFlightEtd=" + departureConnecting.connectingFlightEtd);

        $('#departConnectingEta').html(hhmm(toJavascriptDate(departureConnecting.connectingFlightEta)));
        console.log("departureConnecting.connectingFlightEta=" + departureConnecting.connectingFlightEta);
    } catch(err) {
        $('#departConnecting').css("display", "none");
    }

    try {
        var departureConnecting2 = departureFlight.aeroConnectingFlight2;
        console.log("departureConnecting2=" + departureConnecting2);
        $('#departConnecting2').show();
        $('#departConnecting2From').html(departureConnecting2.connectingFlightFrom);
        $('#departConnecting2To').html(departureConnecting2.connectingFlightTo);
        $('#departConnecting2Etd').html(hhmm(toJavascriptDate(departureConnecting2.connectingFlightEtd)));
        $('#departConnecting2Eta').html(hhmm(toJavascriptDate(departureConnecting2.connectingFlightEta)));
    } catch(err) {
        $('#departConnecting2').css("display", "none");
    }

    var adultPriceDepart = "";
    var childPriceDepart = "";
    var infantPriceDepart = "";
    if (null != departureFlight.adultPassengerSummary) {
        adultPriceDepart = "<p class='priceCtr'><span class='passNum'>" + objAeroTicketing.totalAdult + "</span> Dewasa <span class='passPrice'>" + formatRupiah(departureFlight.adultPassengerSummary.total) + "</span></p>";
    }
    if (null != departureFlight.childPassengerSummary) {
        childPriceDepart = "<p class='priceCtr'><span class='passNum'>" + objAeroTicketing.totalChildren + "</span> Anak-anak <span class='passPrice'>" + formatRupiah(departureFlight.childPassengerSummary.total) + "</span></p>";
    }
    if (null != departureFlight.infantPassengerSummary) {
        infantPriceDepart = "<p class='priceCtr'><span class='passNum'>" + objAeroTicketing.totalInfant + "</span> Bayi <span class='passPrice'>" + formatRupiah(departureFlight.infantPassengerSummary.total) + "</span></p>";
    }
    var totalPriceDepart = "<p class='priceCtr'>Total<span class='passPrice'>" + formatRupiah(departureFlight.total) + "</span></p>";
    $('#dFPriceDetail').html(adultPriceDepart + childPriceDepart + infantPriceDepart + totalPriceDepart);
    $('#dFPriceTitle').show();  $('#dFPriceDetail').show();

    if (objAeroTicketing.isDepartOnly == false) {
        var returnFlight = objAeroTicketing.returnFlight;
        console.log("returnFlight=" + returnFlight);
        try {
            var returnConnecting = returnFlight.aeroConnectingFlight;
            console.log("returnConnecting=" + returnConnecting);
            $('#returnConnecting').show();
            $('#returnConnectingFrom').html(returnConnecting.connectingFlightFrom);
            $('#returnConnectingTo').html(returnConnecting.connectingFlightTo);
            $('#returnConnectingEtd').html(hhmm(toJavascriptDate(returnConnecting.connectingFlightEtd)));
            $('#returnConnectingEta').html(hhmm(toJavascriptDate(returnConnecting.connectingFlightEta)));
        } catch(err) {
            $('#returnConnecting').css("display", "none");
        }
        try {
            var returnConnecting2 = returnFlight.aeroConnectingFlight2;
            console.log("returnConnecting2=" + returnConnecting2);
            $('#returnConnecting2').show();
            $('#returnConnecting2From').html(returnConnecting2.connectingFlightFrom);
            $('#returnConnecting2To').html(returnConnecting2.connectingFlightTo);
            $('#returnConnecting2Etd').html(hhmm(toJavascriptDate(returnConnecting2.connectingFlightEtd)));
            $('#returnConnecting2Eta').html(hhmm(toJavascriptDate(returnConnecting2.connectingFlightEta)));
        } catch(err) {
            $('#returnConnecting2').css("display", "none");
        }

        var adultPriceReturn = "";
        var childPriceReturn = "";
        var infantPriceReturn = "";

        if (null != returnFlight.adultPassengerSummary) {
            adultPriceReturn = "<p class='priceCtr'><span class='passNum'>" + objAeroTicketing.totalAdult + "</span> Dewasa <span class='passPrice'>" + formatRupiah(returnFlight.adultPassengerSummary.total) + "</span></p>";
        }
        if (null != returnFlight.childPassengerSummary) {
            childPriceReturn = "<p class='priceCtr'><span class='passNum'>" + objAeroTicketing.totalChildren + "</span> Anak-anak <span class='passPrice'>" + formatRupiah(returnFlight.childPassengerSummary.total) + "</span></p>";
        }
        if (null != returnFlight.infantPassengerSummary) {
            infantPriceReturn = "<p class='priceCtr'><span class='passNum'>" + objAeroTicketing.totalInfant + "</span> Bayi <span class='passPrice'>" + formatRupiah(returnFlight.infantPassengerSummary.total) + "</span></p>";
        }
        var totalPriceReturn = "<p class='priceCtr'>Total<span class='passPrice'>" + formatRupiah(returnFlight.total) + "</span></p>";
        $('#rFPriceDetail').html(adultPriceReturn + childPriceReturn + infantPriceReturn + totalPriceReturn);
        $('#rFPriceTitle').show();  $('#rFPriceDetail').show();
    }
    // fungsi untuk memilih maskapai
    var max = 10, // Maksimal maskapai yang bisa di pilih
        adultPassDef = 1, // Jumlah default penumpang dewasa
        adultChildPassMax = 7, // Jumlah maksimal penumpang dewasa
        childBabyPassDef = 0, // Jumlah default penumpang anak / Bayi
        babyPassMax = 4, // Jumlah maksimal penumpang dewasa
        serviceCost = 40000, // Harga service
        availableFlight = [{label: 'Pilihan Tersering', children: [
            {id: 'BDO', label: 'BANDUNG, BDO'}
        ]}];

    $('.passTab li').click(function () {
        $('.passTab li').removeClass('active');
        var activePass = $(this).attr('class');
        $('.passForm div').addClass('hide');
        $('.passForm').find('.' + activePass).removeClass('hide');
        $(this).addClass('active');
        setSelectTitle($);
        setSelectTitleChild($);
        setContactFromList($);
        setPass1FromList($);
        setPassParents($);
        if (objAeroPassenger2 != null) setPass2FromList($); if (objAeroPassenger3 != null) setPass3FromList($);
        if (objAeroPassenger4 != null) setPass4FromList($); if (objAeroPassenger5 != null) setPass5FromList($);
        if (objAeroPassenger6 != null) setPass6FromList($); if (objAeroPassenger7 != null) setPass7FromList($);
        setPassCountries($);
    });

    $('.miniInfo').mouseover(function () {
        $(this).css('z-index', '1');
        $(this).find('span').show();
    }).mouseout(function () {
        $(this).css('z-index', '0');
        $(this).find('span').hide();
    });

    // fungsi untuk memilih tanggal lahir
    $('.datePick').datepicker({
        showOn: "both",
        buttonImage: "img/calendar.png",
        buttonImageOnly: true,
        defaultDate: '+1w',
        dateFormat: 'dd/mm/yy',
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0"
    });

/*
    $('#checkIsContact').change(function(){
        if (this.checked){
            var coolVar = objAccount.name;
            var partsArray = coolVar.split(' ');
            $('#firstName').val(partsArray[0]);
            if (partsArray[1]) {
                $('#lastName').val(partsArray[1]);
            }
            $('#mobileNumber').val(objAccount.phone);
            $('#emailAddress').val(objAccount.email);
        }else{
            console.log("checkIsPassenger unchecked");
        }
    });
*/

    $('#checkIsPassenger').change(function(){
        if (this.checked){
            $('#pass1Title').val($('#contactTitle').val());
            $('#pass1FirstName').val($('#firstName').val());
            $('#pass1LastName').val($('#lastName').val());

            console.log("aeroPassenger1.aeroPassenger.firstName=" + aeroPassenger1.aeroPassenger.firstName);
        }else{
            console.log("checkIsPassenger unchecked");
        }
    });

    $('#contactFromList').on("change", function() {
        var varthis = $(this).val();
        console.log("varthis=" + varthis);
        if (varthis != ID_NINES) {
            $.each(JSON.parse(hiddenContacts), function(idx, obj) {
                if (varthis == obj.id) {
                    var partsArray = obj.customerReference.split(',');
                    var lastName = partsArray[0];
                    var firstName = partsArray[1];

                    $("#firstName").val(firstName);
                    $("#lastName").val(lastName);

                    $('#contactTitle').select2("val", obj.data3);

                    partsArray = obj.data5.split(',');
                    $("#mobileNumber").val(partsArray[0]);
                    $("#emailAddress").val(partsArray[1]);
                }
            });
        } else {
            $("#firstName").val("");
            $("#lastName").val("");
            $('#contactTitle').select2("val", MR);
            $("#mobileNumber").val("");
            $("#emailAddress").val("");
        }
    });

    $('#pass1FromList').on("change", function() {
        var varthis = $(this).val();
        console.log("varthis=" + varthis);
        if (varthis != ID_NINES) {
            $.each(JSON.parse(hiddenCustRegAdult), function(idx, obj) {
                if (varthis == obj.id) {
                    var partsArray = obj.customerReference.split(',');
                    var lastName = partsArray[0];
                    var firstName = partsArray[1];

                    $("#pass1FirstName").val(firstName);
                    $("#pass1LastName").val(lastName);

                    $('#pass1Title').select2("val", obj.data3);

                    partsArray = obj.data5.split(',');
                    console.log("dob=" + partsArray[0]);
                    $("#pass1Dob").val(partsArray[0]);
                    $("#pass1Country").select2("val", partsArray[1]);
                    $("#pass1IdCard").val(partsArray[2]);
                }
            });
        } else {
            $("#pass1FirstName").val("");
            $("#pass1LastName").val("");
            $('#pass1Title').select2("val", MR);
            $("#pass1IdCard").val("");

            $("#pass1Dob").val("");
            $('#pass1Country').select2("val", INDONESIA);
        }
    });

    $( "#pass2FromList" ).change(function() {
        var varthis = $(this).val();
        if (varthis != ID_NINES) {
            $.each(JSON.parse((objAeroPassenger2.passengerType == ADULT) ? hiddenCustRegAdult :
                    (objAeroPassenger2.passengerType == CHILD) ? hiddenCustRegChild : hiddenCustRegInfant), function(idx, obj) {
                if (varthis == obj.id) {
                    var partsArray = obj.customerReference.split(',');
                    var lastName = partsArray[0];
                    var firstName = partsArray[1];
                    $("#pass2FirstName").val(firstName);
                    $("#pass2LastName").val(lastName);
                    $("#pass2Title").select2("val", obj.data3);
                    $("#pass2TitleChild").select2("val", obj.data3);

                    partsArray = obj.data5.split(',');
                    $("#pass2Dob").val(partsArray[0]);
                    $("#pass2Country").select2("val", partsArray[1]);
                    $("#pass2IdCard").val(partsArray[2]);
                }
            });
        } else {
            $("#pass2FirstName").val("");
            $("#pass2LastName").val("");
            $("#pass2Title").select2("val", MR);
            $("#pass2TitleChild").select2("val", MSTR);
            $("#pass2IdCard").val("");

            $("#pass2Dob").val("");
            $("#pass2Country").select2("val", INDONESIA);
        }
    });

    $( "#pass3FromList" ).change(function() {
        var varthis = $(this).val();
        if (varthis != ID_NINES) {
            $.each(JSON.parse((objAeroPassenger3.passengerType == ADULT) ? hiddenCustRegAdult :
                                    (objAeroPassenger3.passengerType == CHILD) ? hiddenCustRegChild : hiddenCustRegInfant), function(idx, obj) {
                if (varthis == obj.id) {
                    var partsArray = obj.customerReference.split(',');
                    var lastName = partsArray[0];
                    var firstName = partsArray[1];
                    $("#pass3FirstName").val(firstName);
                    $("#pass3LastName").val(lastName);
                    $("#pass3Title").select2("val", obj.data3);
                    $("#pass3TitleChild").select2("val", obj.data3);

                    partsArray = obj.data5.split(',');
                    $("#pass3Dob").val(partsArray[0]);
                    $("#pass3Country").select2("val", partsArray[1]);
                    $("#pass3IdCard").val(partsArray[2]);
                }
            });
        } else {
            $("#pass3FirstName").val("");
            $("#pass3LastName").val("");
            $("#pass3Title").select2("val", MR);
            $("#pass3TitleChild").select2("val", MSTR);
            $("#pass3IdCard").val("");

            $("#pass3Dob").val("");
            $("#pass3Country").select2("val", INDONESIA);
        }
    });
    $( "#pass4FromList" ).change(function() {
        var varthis = $(this).val();
        if (varthis != ID_NINES) {
            $.each(JSON.parse((objAeroPassenger4.passengerType == ADULT) ? hiddenCustRegAdult :
                                    (objAeroPassenger4.passengerType == CHILD) ? hiddenCustRegChild : hiddenCustRegInfant), function(idx, obj) {
                if (varthis == obj.id) {
                    var partsArray = obj.customerReference.split(',');
                    var lastName = partsArray[0];
                    var firstName = partsArray[1];
                    $("#pass4FirstName").val(firstName);
                    $("#pass4LastName").val(lastName);
                    $("#pass4Title").select2("val", obj.data3);
                    $("#pass4TitleChild").select2("val", obj.data3);

                    partsArray = obj.data5.split(',');
                    $("#pass4Dob").val(partsArray[0]);
                    $("#pass4Country").select2("val", partsArray[1]);
                    $("#pass4IdCard").val(partsArray[2]);
                }
            });
        } else {
            $("#pass4FirstName").val("");
            $("#pass4LastName").val("");
            $("#pass4Title").select2("val", MR);
            $("#pass4TitleChild").select2("val", MSTR);
            $("#pass4IdCard").val("");

            $("#pass4Dob").val("");
            $("#pass4Country").select2("val", INDONESIA);
        }
    });
    $( "#pass5FromList" ).change(function() {
        var varthis = $(this).val();
        if (varthis != ID_NINES) {
            $.each(JSON.parse((objAeroPassenger5.passengerType == ADULT) ? hiddenCustRegAdult :
                                    (objAeroPassenger5.passengerType == CHILD) ? hiddenCustRegChild : hiddenCustRegInfant), function(idx, obj) {
                if (varthis == obj.id) {
                    var partsArray = obj.customerReference.split(',');
                    var lastName = partsArray[0];
                    var firstName = partsArray[1];
                    $("#pass5FirstName").val(firstName);
                    $("#pass5LastName").val(lastName);
                    $("#pass5Title").select2("val", obj.data3);
                    $("#pass5TitleChild").select2("val", obj.data3);

                    partsArray = obj.data5.split(',');
                    $("#pass5Dob").val(partsArray[0]);
                    $("#pass5Country").select2("val", partsArray[1]);
                    $("#pass5IdCard").val(partsArray[2]);
                }
            });
        } else {
            $("#pass5FirstName").val("");
            $("#pass5LastName").val("");
            $("#pass5Title").select2("val", MR);
            $("#pass5TitleChild").select2("val", MSTR);
            $("#pass5IdCard").val("");

            $("#pass5Dob").val("");
            $("#pass5Country").select2("val", INDONESIA);
        }
    });
    $( "#pass6FromList" ).change(function() {
        var varthis = $(this).val();
        if (varthis != ID_NINES) {
            $.each(JSON.parse((objAeroPassenger6.passengerType == ADULT) ? hiddenCustRegAdult :
                                    (objAeroPassenger6.passengerType == CHILD) ? hiddenCustRegChild : hiddenCustRegInfant), function(idx, obj) {
                if (varthis == obj.id) {
                    var partsArray = obj.customerReference.split(',');
                    var lastName = partsArray[0];
                    var firstName = partsArray[1];
                    $("#pass6FirstName").val(firstName);
                    $("#pass6LastName").val(lastName);
                    $("#pass6Title").select2("val", obj.data3);
                    $("#pass6TitleChild").select2("val", obj.data3);

                    partsArray = obj.data5.split(',');
                    $("#pass6Dob").val(partsArray[0]);
                    $("#pass6Country").select2("val", partsArray[1]);
                    $("#pass6IdCard").val(partsArray[2]);
                }
            });
        } else {
            $("#pass6FirstName").val("");
            $("#pass6LastName").val("");
            $("#pass6Title").select2("val", MR);
            $("#pass6TitleChild").select2("val", MSTR);
            $("#pass6IdCard").val("");

            $("#pass6Dob").val("");
            $("#pass6Country").select2("val", INDONESIA);
        }
    });
    $( "#pass7FromList" ).change(function() {
        var varthis = $(this).val();
        if (varthis != ID_NINES) {
            $.each(JSON.parse((objAeroPassenger7.passengerType == ADULT) ? hiddenCustRegAdult :
                                    (objAeroPassenger7.passengerType == CHILD) ? hiddenCustRegChild : hiddenCustRegInfant), function(idx, obj) {
                if (varthis == obj.id) {
                    var partsArray = obj.customerReference.split(',');
                    var lastName = partsArray[0];
                    var firstName = partsArray[1];
                    $("#pass7FirstName").val(firstName);
                    $("#pass7LastName").val(lastName);
                    $("#pass7Title").select2("val", obj.data3);
                    $("#pass7TitleChild").select2("val", obj.data3);

                    partsArray = obj.data5.split(',');
                    $("#pass7Dob").val(partsArray[0]);
                    $("#pass7Country").select2("val", partsArray[1]);
                    $("#pass7IdCard").val(partsArray[2]);
                }
            });
        } else {
            $("#pass7FirstName").val("");
            $("#pass7LastName").val("");
            $("#pass7Title").select2("val", MR);
            $("#pass7TitleChild").select2("val", MSTR);
            $("#pass7IdCard").val("");

            $("#pass7Dob").val("");
            $("#pass7Country").select2("val", INDONESIA);
        }
    });


    $('#form').submit(function() {
        /*console.log("di dalam submit");
        $("#terror ul").empty();
        var isError = false;

        if (!$("#firstName").val() || !$("#lastName").val() || !$("#mobileNumber").val() || !$("#emailAddress").val()) {
            $("#terror ul").append('<li>Mohon melengkapi data Contact Person Detail</li>');
            isError = true;
        }

        var currentDate = new Date();
        console.log("currentDate=" + currentDate);
        console.log("currentDate.getFullYear()=" + currentDate.getFullYear() + " currentDate.getMonth()=" + currentDate.getMonth() + " currentDate.getDate()=" + currentDate.getDate());
        var lastTwelveYear = new Date(currentDate.getFullYear() - 12, currentDate.getMonth(), currentDate.getDate());
        console.log("lastTwelveYear=" + lastTwelveYear);
        var lastTwoYear = new Date(currentDate.getFullYear() - 2, currentDate.getMonth(), currentDate.getDate());
        var lastNineMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() - 9, currentDate.getDate());
        var lastThreeMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() - 3, currentDate.getDate());
        console.log("lastTwoYear=" + lastTwoYear + " lastNineMonth=" + lastNineMonth + " lastThreeMonth=" + lastThreeMonth);

        if (!$("#pass1FirstName").val() || !$("#pass1LastName").val() || !$("#pass1IdCard").val() || !$("#pass1Dob").val()) {
            $("#terror ul").append('<li>Mohon melengkapi data Penumpang 1</li>');
            isError = true;
        }
        var pass1Dob = $("#pass1Dob").val();
        if (pass1Dob) {
            var pass1Date = new Date(pass1Dob.substring(6), pass1Dob.substring(3, 5) - 1, pass1Dob.substring(0, 2));
            console.log("pass1Date=" + pass1Date + " lastTwelveYear=" + lastTwelveYear);
            if (pass1Date > lastTwelveYear) {
                $("#terror ul").append('<li>Penumpang 1 Harus Berusia Diatas 12 Tahun</li>');
                isError = true;
            }
        }

        if (objAeroPassenger2 != null) {

            if (!$("#pass2FirstName").val() || !$("#pass2LastName").val() || !$("#pass2Dob").val() ||
                (objAeroPassenger2.passengerType == ADULT && !$("#pass2IdCard").val()) ) {
                $("#terror ul").append('<li>Mohon melengkapi data Penumpang 2</li>');
                isError = true;
            }
            var pass2Dob = $("#pass2Dob").val();
            if (pass2Dob) {
                var pass2Date = new Date(pass2Dob.substring(6), pass2Dob.substring(3, 5) - 1, pass2Dob.substring(0, 2));
                console.log("pass2Date=" + pass2Date + " lastThreeMonth=" + lastThreeMonth + " lastTwoYear=" + lastTwoYear + " lastTwelveYear=" + lastTwelveYear);
                if (objAeroPassenger2.passengerType == ADULT && pass2Date > lastTwelveYear) {
                    $("#terror ul").append('<li>Penumpang 2 Harus Berusia Diatas 12 Tahun</li>');
                    isError = true;
                } else if (objAeroPassenger2.passengerType == CHILD && (pass2Date > lastTwoYear || pass2Date < lastTwelveYear)) {
                    $("#terror ul").append('<li>Penumpang 2 Harus Berusia Diantara 2 s.d 12 Tahun</li>');
                    isError = true;
                } else if (objAeroPassenger2.passengerType == INFANT && (pass2Date > lastThreeMonth || pass2Date < lastTwoYear)) {
                    $("#terror ul").append('<li>Penumpang 2 Harus Berusia Diantara 3 s.d 24 Bulan</li>');
                    isError = true;
                }
            }
        }

        if (objAeroPassenger3 != null) {
            if (!$("#pass3FirstName").val() || !$("#pass3LastName").val() || !$("#pass3Dob").val() ||
                    (objAeroPassenger3.passengerType == ADULT && !$("#pass3IdCard").val()) ) {
                $("#terror ul").append('<li>Mohon melengkapi data Penumpang 3</li>');
                isError = true;
            }
            var pass3Dob = $("#pass3Dob").val();
            if (pass3Dob) {
                var pass3Date = new Date(pass3Dob.substring(6), pass3Dob.substring(3, 5) - 1, pass3Dob.substring(0, 2));
                console.log("pass3Date=" + pass3Date);
                if (objAeroPassenger3.passengerType == ADULT && pass3Date > lastTwelveYear) {
                    $("#terror ul").append('<li>Penumpang 3 Harus Berusia Diatas 12 Tahun</li>');
                    isError = true;
                } else if (objAeroPassenger3.passengerType == CHILD && (pass3Date > lastTwoYear || pass3Date < lastTwelveYear)) {
                    $("#terror ul").append('<li>Penumpang 3 Harus Berusia Diantara 2 s.d 12 Tahun</li>');
                    isError = true;
                } else if (objAeroPassenger3.passengerType == INFANT && (pass3Date > lastThreeMonth || pass3Date < lastTwoYear)) {
                    $("#terror ul").append('<li>Penumpang 3 Harus Berusia Diantara 3 s.d 24 Bulan</li>');
                    isError = true;
                }
            }
        }

        if (objAeroPassenger4 != null) {
            console.log("pass4FirstName=" + $("#pass4FirstName").val() + " pass4LastName=" + $("#pass4LastName").val() + " pass4Dob=" + $("#pass4Dob").val());
            if (!$("#pass4FirstName").val() || !$("#pass4LastName").val() || !$("#pass4Dob").val() ||
                (objAeroPassenger4.passengerType == ADULT && !$("#pass4IdCard").val()) ) {
                $("#terror ul").append('<li>Mohon melengkapi data Penumpang 4</li>');
                isError = true;
            }
            var pass4Dob = $("#pass4Dob").val();
            if (pass4Dob) {
                var pass4Date = new Date(pass4Dob.substring(6), pass4Dob.substring(3, 5) - 1, pass4Dob.substring(0, 2));
                console.log("pass4Date=" + pass4Date);
                if (objAeroPassenger4.passengerType == ADULT && pass4Date > lastTwelveYear) {
                    $("#terror ul").append('<li>Penumpang 4 Harus Berusia Diatas 12 Tahun</li>');
                    isError = true;
                } else if (objAeroPassenger4.passengerType == CHILD && (pass4Date > lastTwoYear || pass4Date < lastTwelveYear)) {
                    $("#terror ul").append('<li>Penumpang 4 Harus Berusia Diantara 2 s.d 12 Tahun</li>');
                    isError = true;
                } else if (objAeroPassenger4.passengerType == INFANT && (pass4Date > lastThreeMonth || pass4Date < lastTwoYear)) {
                    $("#terror ul").append('<li>Penumpang 4 Harus Berusia Diantara 3 s.d 24 Bulan</li>');
                    isError = true;
                }
            }
        }

        if (objAeroPassenger5 != null) {
            console.log("pass5FirstName=" + $("#pass5FirstName").val() + " pass5LastName=" + $("#pass5LastName").val() + " pass5Dob=" + $("#pass5Dob").val());
            if (!$("#pass5FirstName").val() || !$("#pass5LastName").val() || !$("#pass5Dob").val() ||
                (objAeroPassenger5.passengerType == ADULT && !$("#pass5IdCard").val()) ) {
                $("#terror ul").append('<li>Mohon melengkapi data Penumpang 5</li>');
                isError = true;
            }
            var pass5Dob = $("#pass5Dob").val();
            if (pass5Dob) {
                var pass5Date = new Date(pass5Dob.substring(6), pass5Dob.substring(3, 5) - 1, pass5Dob.substring(0, 2));
                console.log("pass5Date=" + pass5Date);
                if (objAeroPassenger5.passengerType == ADULT && pass5Date > lastTwelveYear) {
                    $("#terror ul").append('<li>Penumpang 5 Harus Berusia Diatas 12 Tahun</li>');
                    isError = true;
                } else if (objAeroPassenger5.passengerType == CHILD && (pass5Date > lastTwoYear || pass5Date < lastTwelveYear)) {
                    $("#terror ul").append('<li>Penumpang 5 Harus Berusia Diantara 2 s.d 12 Tahun</li>');
                    isError = true;
                } else if (objAeroPassenger5.passengerType == INFANT && (pass5Date > lastThreeMonth || pass5Date < lastTwoYear)) {
                    $("#terror ul").append('<li>Penumpang 5 Harus Berusia Diantara 3 s.d 24 Bulan</li>');
                    isError = true;
                }
            }
        }

        if (objAeroPassenger6 != null) {
            console.log("pass6FirstName=" + $("#pass6FirstName").val() + " pass6LastName=" + $("#pass6LastName").val() + " pass6Dob=" + $("#pass6Dob").val());
            if (!$("#pass6FirstName").val() || !$("#pass6LastName").val() || !$("#pass6Dob").val() ||
                (objAeroPassenger6.passengerType == ADULT && !$("#pass6IdCard").val()) ) {
                $("#terror ul").append('<li>Mohon melengkapi data Penumpang 6</li>');
                isError = true;
            }
            var pass6Dob = $("#pass6Dob").val();
            if (pass6Dob) {
                var pass6Date = new Date(pass6Dob.substring(6), pass6Dob.substring(3, 5) - 1, pass6Dob.substring(0, 2));
                console.log("pass6Date=" + pass6Date);
                if (objAeroPassenger6.passengerType == ADULT && pass6Date > lastTwelveYear) {
                    $("#terror ul").append('<li>Penumpang 6 Harus Berusia Diatas 12 Tahun</li>');
                    isError = true;
                } else if (objAeroPassenger6.passengerType == CHILD && (pass6Date > lastTwoYear || pass6Date < lastTwelveYear)) {
                    $("#terror ul").append('<li>Penumpang 6 Harus Berusia Diantara 2 s.d 12 Tahun</li>');
                    isError = true;
                } else if (objAeroPassenger6.passengerType == INFANT && (pass6Date > lastThreeMonth || pass6Date < lastTwoYear)) {
                    $("#terror ul").append('<li>Penumpang 6 Harus Berusia Diantara 3 s.d 24 Bulan</li>');
                    isError = true;
                }
            }
        }

        if (objAeroPassenger7 != null) {
            console.log("pass7FirstName=" + $("#pass7FirstName").val() + " pass7LastName=" + $("#pass7LastName").val() + " pass7Dob=" + $("#pass7Dob").val());
            if (!$("#pass7FirstName").val() || !$("#pass7LastName").val() || !$("#pass7Dob").val() ||
                (objAeroPassenger7.passengerType == ADULT && !$("#pass7IdCard").val()) ) {
                $("#terror ul").append('<li>Mohon melengkapi data Penumpang 7</li>');
                isError = true;
            }
            var pass7Dob = $("#pass7Dob").val();
            if (pass7Dob) {
                var pass7Date = new Date(pass7Dob.substring(6), pass7Dob.substring(3, 5) - 1, pass7Dob.substring(0, 2));
                console.log("pass7Date=" + pass7Date);
                if (objAeroPassenger7.passengerType == ADULT && pass7Date > lastTwelveYear) {
                    $("#terror ul").append('<li>Penumpang 7 Harus Berusia Diatas 12 Tahun</li>');
                    isError = true;
                } else if (objAeroPassenger7.passengerType == CHILD && (pass7Date > lastTwoYear || pass7Date < lastTwelveYear)) {
                    $("#terror ul").append('<li>Penumpang 7 Harus Berusia Diantara 2 s.d 12 Tahun</li>');
                    isError = true;
                } else if (objAeroPassenger7.passengerType == INFANT && (pass7Date > lastThreeMonth || pass7Date < lastTwoYear)) {
                    $("#terror ul").append('<li>Penumpang 7 Harus Berusia Diantara 3 s.d 24 Bulan</li>');
                    isError = true;
                }
            }
        }

        if (isError) {
            $('#tbanner').show(); $('#terror').show();
            return false;
        } else {
            $('#tbanner').hide(); $('#terror').hide();
        }*/
        return true;
    });

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

    hidingComponents($);
});

function hidingComponents($) {
    if (objAeroTicketing.providerCode == "VOLT") {
        $('#aeroDepartPrice').hide();
        $('#aeroReturnPrice').hide();
    }
 }

function isNumberKey(evt)
{
    var charCode = (evt.which) ? evt.which : event.keyCode;
    //if (charCode < 48 || charCode > 57) return false;
    if (!((charCode >= 48 && charCode <= 57) || charCode == 08 || charCode == 127)) return false;
    return true;
}

function isAlphabetKey(evt){
    var charCode = (evt.which) ? evt.which : event.keyCode;
    if (!((charCode >= 65 && charCode <= 90) || (charCode >= 97 && charCode <= 122)
        || charCode == 08 || charCode == 32 || charCode == 46 || charCode == 127)) return false;
    return true;
}

