jQuery(document).ready(function ($) {
'use strict';
var serviceCost = 40000,
        
    /* For Demo Purpose Only */
    /* Start Sample */
    adultCount = 4,
    childCount = 2,
    babyCount = 1,
    /* End Sample */
    
    // Max List Allowed to Depart and Return
    maxList = 6,
    
    // Depart detail variable and price counting
    dFCData = $('#departList .active').find('.airlineLogo').attr('class'),
    dFCArr = dFCData.split(' '),
    dFLogo = dFCArr[1],
    dFCode = $('#departList .active').find('.flightCode').html(),
    dFFData = $('#departList .active').find('.departCodeTime').html(),
    dFFArr = dFFData.split(' '),
    dFFCode = dFFArr[0],
    dFFTime = dFFArr[1].substring(3, dFFArr[1].length - 4),
    aFFData = $('#departList .active').find('.arriveCodeTime').html(),
    aFFArr = aFFData.split(' '),
    aFFCode = aFFArr[0],
    aFFTime = aFFArr[1].substring(3, aFFArr[1].length - 4),
    dFPData = $('#departList .active').find('.flightPrice').html(),
    dFPArr = dFPData.split(' '),
    dFPARs = parseInt(dFPArr[0].replace(/[^a-z0-9\s]/gi, '')),
    dFPCost = dFPARs + serviceCost,
    dFAdultPrice = (dFPCost * adultCount),
    dFAdultPriceFormat = dFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'),
    dFChildPrice = (dFPCost * childCount),
    dFChildPriceFormat = dFChildPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'),
    dFBabyPrice = (dFPCost * babyCount),
    dFBabyPriceFormat = dFBabyPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'),
    
    // Return detail variable and price counting
    rDFCData = $('#returnList .active').find('.airlineLogo').attr('class'),
    rDFCArr = rDFCData.split(' '),
    rDFLogo = rDFCArr[1],
    rDFCode = $('#returnList .active').find('.flightCode').html(),
    rDFFData = $('#returnList .active').find('.departCodeTime').html(),
    rDFFArr = rDFFData.split(' '),
    rDFFCode = rDFFArr[0],
    rDFFTime = rDFFArr[1].substring(3, rDFFArr[1].length - 4),
    rAFFData = $('#returnList .active').find('.arriveCodeTime').html(),
    rAFFArr = rAFFData.split(' '),
    rAFFCode = rAFFArr[0],
    rAFFTime = rAFFArr[1].substring(3, rAFFArr[1].length - 4),
    rDFPData = $('#returnList .active').find('.flightPrice').html(),
    rDFPArr = rDFPData.split(' '),
    rDFPARs = parseInt(rDFPArr[0].replace(/[^a-z0-9\s]/gi, '')),
    rDFPCost = rDFPARs + serviceCost,
    rDFAdultPrice = (rDFPCost * adultCount),
    rDFAdultPriceFormat = rDFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'),
    rDFChildPrice = (rDFPCost * childCount),
    rDFChildPriceFormat = rDFChildPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'),
    rDFBabyPrice = (rDFPCost * babyCount),
    rDFBabyPriceFormat = rDFBabyPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'),
    ListHeightAllowed = ($('.listPilihan tr').height() * maxList) + 1,
    departMaxListHeight = $('#departList').height(),
    returnMaxListHeight = $('#returnList').height();
    
    if (departMaxListHeight > ListHeightAllowed){
        $('#departList').addClass('scrollList');
    } else {
        $('#departList').removeClass('scrollList');
    }
    
    if (returnMaxListHeight > ListHeightAllowed){
        $('#returntList').addClass('scrollList');
    } else {
        $('#returntList').removeClass('scrollList');
    }
    
    $('.listPilihan .active').find('.cBFlight').prop('checked', 'checked');
    
    // Depart override default string
    $('.departLogoAndCode .flightCode').html(dFCode);
    $('.departLogoAndCode .flightLogo').addClass(dFLogo);
    $('.departFlow .departCityCode').html(dFFCode);
    $('.departFlow .arriveCityCode').html(aFFCode);
    $('.departFlow .departTime').html(dFFTime);
    $('.departFlow .arriveTime').html(aFFTime);
    
    // Return override default string
    $('.returnLogoAndCode .flightCode').html(rDFCode);
    $('.returnLogoAndCode .flightLogo').addClass(rDFLogo);
    $('.returnFlow .departCityCode').html(rDFFCode);
    $('.returnFlow .arriveCityCode').html(rAFFCode);
    $('.returnFlow .departTime').html(rDFFTime);
    $('.returnFlow .arriveTime').html(rAFFTime);
    
    // fungsi hitung dari pilihan default
    if ((childCount !== 0) && (babyCount === 0)) {
        $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + dFChildPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (dFAdultPrice + dFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
        $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + rDFChildPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (rDFAdultPrice + rDFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>')
        $('.totalPrice').html((dFAdultPrice + dFChildPrice + rDFAdultPrice + rDFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
    } else if ((childCount !== 0) && (babyCount !== 0)) {
        $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + dFChildPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + babyCount + '</span> Bayi <span class="passPrice">' + dFBabyPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (dFAdultPrice + dFChildPrice + dFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
        $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + rDFChildPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + babyCount + '</span> Bayi <span class="passPrice">' + rDFBabyPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (rDFAdultPrice + rDFChildPrice + rDFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
        $('.totalPrice').html((dFAdultPrice + dFChildPrice + dFBabyPrice + rDFAdultPrice + rDFChildPrice + rDFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
    } else {
        $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + dFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
        $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + rDFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
        $('.totalPrice').html((dFAdultPrice + rDFAdultPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
    }
    
    // fungsi popup filter pencarian
    $('.flightFilter .tier1').click(function () {
        $(this).addClass('active');
        $(this).find('ul').show();
    });
    
    $('.flightFilter .tier1 ul li').click(function () {
        $(this).parent().find('li').removeClass('active');
        $(this).addClass('active');
        $(this).parent().parent().removeClass('active');
        $('.flightFilter li ul').hide();
        return false;
    });
    
    // fungsi mousehover option available class
    $('.flightClassOption').mouseover(function () {
        $(this).addClass('active');
    }).mouseout(function () {
        $(this).removeClass('active');
    });
    
//    // fungsi click pemilihan harga
//    $('#departList .fLTable3').bind('click', function () {        
//        $('#departList table tr').removeClass('active');
//        $('#departList .fLTable3').removeClass('active customClass');
//        $(this).addClass('active');
//        $(this).parent().addClass('active');
//        $(this).find('.cBFlight').prop('checked', 'checked');
//        $(this).find('.flightPrice').html($(this).find('li:first .valueClassPrice').html() + ' IDR');
//        
//    var dFCData = $('#departList .active').find('.airlineLogo').attr('class'),
//        dFCArr = dFCData.split(' '),
//        dFLogo = dFCArr[1],
//        dFCode = $('#departList .active').find('.flightCode').html(),
//        dFFData = $('#departList .active').find('.departCodeTime').html(),
//        dFFArr = dFFData.split(' '),
//        dFFCode = dFFArr[0],
//        dFFTime = dFFArr[1].substring(3, dFFArr[1].length - 4),
//        aFFData = $('#departList .active').find('.arriveCodeTime').html(),
//        aFFArr = aFFData.split(' '),
//        aFFCode = aFFArr[0],
//        aFFTime = aFFArr[1].substring(3, aFFArr[1].length - 4),
//        dFPData = $('#departList .active').find('.flightPrice').html(),
//        dFPArr = dFPData.split(' '),
//        dFPARs = parseInt(dFPArr[0].replace(/[^a-z0-9\s]/gi, '')),
//        dFPCost = dFPARs + serviceCost,
//        dFAdultPrice = (dFPCost * adultCount),
//        dFAdultPriceFormat = dFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'),
//        dFChildPrice = (dFPCost * childCount),
//        dFChildPriceFormat = dFChildPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'),
//        dFBabyPrice = (dFPCost * babyCount),
//        dFBabyPriceFormat = dFBabyPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
//        $('.departLogoAndCode .flightCode').html(dFCode);
//        $('.departLogoAndCode .flightLogo').removeClass().addClass('flightLogo ' + dFLogo);
//        $('.departFlow .departCityCode').html(dFFCode);
//        $('.departFlow .arriveCityCode').html(aFFCode);
//        $('.departFlow .departTime').html(dFFTime);
//        $('.departFlow .arriveTime').html(aFFTime);
//        
//        // fungsi hitung dari pilihan default
//        if ((childCount !== 0) && (babyCount === 0)) {
//            $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + dFChildPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (dFAdultPrice + dFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
//            $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + rDFChildPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (rDFAdultPrice + rDFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>')
//            $('.totalPrice').html((dFAdultPrice + dFChildPrice + rDFAdultPrice + rDFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
//        } else if ((childCount !== 0) && (babyCount !== 0)) {
//            $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + dFChildPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + babyCount + '</span> Bayi <span class="passPrice">' + dFBabyPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (dFAdultPrice + dFChildPrice + dFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
//            $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + rDFChildPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + babyCount + '</span> Bayi <span class="passPrice">' + rDFBabyPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (rDFAdultPrice + rDFChildPrice + rDFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
//            $('.totalPrice').html((dFAdultPrice + dFChildPrice + dFBabyPrice + rDFAdultPrice + rDFChildPrice + rDFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
//        } else {
//            $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + dFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
//            $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + rDFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
//            $('.totalPrice').html((dFAdultPrice + rDFAdultPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
//        }
//    });
    
    $('#returnList .fLTable3').bind('click', function () {
        $('#returnList table tr').removeClass('active');
        $('#returnList .fLTable3').removeClass('active customClass');
        $(this).addClass('active');
        $(this).parent().addClass('active');
        $(this).find('.cBFlight').prop('checked', 'checked');
        $(this).find('.flightPrice').html($(this).find('li:first .valueClassPrice').html() + ' IDR');
    var rDFCData = $('#returnList .active').find('.airlineLogo').attr('class'),
        rDFCArr = rDFCData.split(' '),
        rDFLogo = rDFCArr[1],
        rDFCode = $('#returnList .active').find('.flightCode').html(),
        rDFFData = $('#returnList .active').find('.departCodeTime').html(),
        rDFFArr = rDFFData.split(' '),
        rDFFCode = rDFFArr[0],
        rDFFTime = rDFFArr[1].substring(3, rDFFArr[1].length - 4),
        rAFFData = $('#returnList .active').find('.arriveCodeTime').html(),
        rAFFArr = rAFFData.split(' '),
        rAFFCode = rAFFArr[0],
        rAFFTime = rAFFArr[1].substring(3, rAFFArr[1].length - 4),
        rDFPData = $('#returnList .active').find('.flightPrice').html(),
        rDFPArr = rDFPData.split(' '),
        rDFPARs = parseInt(rDFPArr[0].replace(/[^a-z0-9\s]/gi, '')),
        rDFPCost = rDFPARs + serviceCost,
        rDFAdultPrice = (rDFPCost * adultCount),
        rDFAdultPriceFormat = rDFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'),
        rDFChildPrice = (rDFPCost * childCount),
        rDFChildPriceFormat = rDFChildPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'),
        rDFBabyPrice = (rDFPCost * babyCount),
        rDFBabyPriceFormat = rDFBabyPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
        $('.returnLogoAndCode .flightCode').html(rDFCode);
        $('.returnLogoAndCode .flightLogo').removeClass().addClass('flightLogo ' + rDFLogo);
        $('.returnFlow .departCityCode').html(rDFFCode);
        $('.returnFlow .arriveCityCode').html(rAFFCode);
        $('.returnFlow .departTime').html(rDFFTime);
        $('.returnFlow .arriveTime').html(rAFFTime);
        
        // fungsi hitung dari pilihan default
        if ((childCount !== 0) && (babyCount === 0)) {
            $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + dFChildPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (dFAdultPrice + dFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
            $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + rDFChildPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (rDFAdultPrice + rDFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>')
            $('.totalPrice').html((dFAdultPrice + dFChildPrice + rDFAdultPrice + rDFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
        } else if ((childCount !== 0) && (babyCount !== 0)) {
            $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + dFChildPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + babyCount + '</span> Bayi <span class="passPrice">' + dFBabyPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (dFAdultPrice + dFChildPrice + dFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
            $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + rDFChildPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + babyCount + '</span> Bayi <span class="passPrice">' + rDFBabyPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (rDFAdultPrice + rDFChildPrice + rDFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
            $('.totalPrice').html((dFAdultPrice + dFChildPrice + dFBabyPrice + rDFAdultPrice + rDFChildPrice + rDFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
        } else {
            $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + dFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
            $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + rDFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
            $('.totalPrice').html((dFAdultPrice + rDFAdultPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
        }
        
    });
    
    // fungsi pilih kelas lain
    $('#departList .flightClassOption').click(function () {
        var defPrice = $(this).find('li:first .valueClassPrice').html();
        $('.flightClassOption .classOptionPop').hide();
        $(this).find('.classOptionPop').show();
        $(this).parent().find('.flightPrice').html(defPrice + ' IDR');
    });
    
    $('#returnList .flightClassOption').click(function () {
        var defPrice = $(this).find('li:first .valueClassPrice').html();
        $('.flightClassOption .classOptionPop').hide();
        $(this).find('.classOptionPop').show();
        $(this).parent().find('.flightPrice').html(defPrice + ' IDR');
    });
    
    $('#departList .classOptionPop li').click(function () {
        var defClass = $(this).attr('class');
        if (defClass !== 'first') {
            var optionPrice = $(this).find('.valueClassPrice').html();
            $(this).parent().parent().parent().parent().addClass('customClass').find('.flightPrice').html(optionPrice + ' IDR');
            $(this).parent().parent().hide();
            var dFCData = $('#departList .active').find('.airlineLogo').attr('class'),
                dFCArr = dFCData.split(' '),
                dFLogo = dFCArr[1],
                dFCode = $('#departList .active').find('.flightCode').html(),
                dFFData = $('#departList .active').find('.departCodeTime').html(),
                dFFArr = dFFData.split(' '),
                dFFCode = dFFArr[0],
                dFFTime = dFFArr[1].substring(3, dFFArr[1].length - 4),
                aFFData = $('#departList .active').find('.arriveCodeTime').html(),
                aFFArr = aFFData.split(' '),
                aFFCode = aFFArr[0],
                aFFTime = aFFArr[1].substring(3, aFFArr[1].length - 4),
                dFPData = $('#departList .active').find('.flightPrice').html(),
                dFPArr = dFPData.split(' '),
                dFPARs = parseInt(dFPArr[0].replace(/[^a-z0-9\s]/gi, '')),
                dFPCost = dFPARs + serviceCost,
                dFAdultPrice = (dFPCost * adultCount),
                dFAdultPriceFormat = dFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'),
                dFChildPrice = (dFPCost * childCount),
                dFChildPriceFormat = dFChildPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'),
                dFBabyPrice = (dFPCost * babyCount),
                dFBabyPriceFormat = dFBabyPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
                $('.departLogoAndCode .flightCode').html(dFCode);
                $('.departLogoAndCode .flightLogo').removeClass().addClass('flightLogo ' + dFLogo);
                $('.departFlow .departCityCode').html(dFFCode);
                $('.departFlow .arriveCityCode').html(aFFCode);
                $('.departFlow .departTime').html(dFFTime);
                $('.departFlow .arriveTime').html(aFFTime);
                
                // fungsi hitung dari pilihan default
                if ((childCount !== 0) && (babyCount === 0)) {
                    $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + dFChildPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (dFAdultPrice + dFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
                    $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + rDFChildPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (rDFAdultPrice + rDFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>')
                    $('.totalPrice').html((dFAdultPrice + dFChildPrice + rDFAdultPrice + rDFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
                } else if ((childCount !== 0) && (babyCount !== 0)) {
                    $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + dFChildPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + babyCount + '</span> Bayi <span class="passPrice">' + dFBabyPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (dFAdultPrice + dFChildPrice + dFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
                    $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + rDFChildPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + babyCount + '</span> Bayi <span class="passPrice">' + rDFBabyPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (rDFAdultPrice + rDFChildPrice + rDFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
                    $('.totalPrice').html((dFAdultPrice + dFChildPrice + dFBabyPrice + rDFAdultPrice + rDFChildPrice + rDFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
                } else {
                    $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + dFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
                    $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + rDFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
                    $('.totalPrice').html((dFAdultPrice + rDFAdultPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
                }
        } else {
            var optionPrice = $(this).find('.valueClassPrice').html();
            $(this).parent().parent().parent().parent().find('.flightPrice').html(optionPrice + ' IDR');
            $(this).parent().parent().hide();
        }
        return false;
    });
    
    $('#returnList .classOptionPop li').click(function () {
        var defClass = $(this).attr('class');
        if (defClass !== 'first') {
            var optionPrice = $(this).find('.valueClassPrice').html();
            $(this).parent().parent().parent().parent().addClass('customClass').find('.flightPrice').html(optionPrice + ' IDR');
            $(this).parent().parent().hide();
            var rDFCData = $('#returnList .active').find('.airlineLogo').attr('class'),
                rDFCArr = rDFCData.split(' '),
                rDFLogo = rDFCArr[1],
                rDFCode = $('#returnList .active').find('.flightCode').html(),
                rDFFData = $('#returnList .active').find('.departCodeTime').html(),
                rDFFArr = rDFFData.split(' '),
                rDFFCode = rDFFArr[0],
                rDFFTime = rDFFArr[1].substring(3, rDFFArr[1].length - 4),
                rAFFData = $('#returnList .active').find('.arriveCodeTime').html(),
                rAFFArr = rAFFData.split(' '),
                rAFFCode = rAFFArr[0],
                rAFFTime = rAFFArr[1].substring(3, rAFFArr[1].length - 4),
                rDFPData = $('#returnList .active').find('.flightPrice').html(),
                rDFPArr = rDFPData.split(' '),
                rDFPARs = parseInt(rDFPArr[0].replace(/[^a-z0-9\s]/gi, '')),
                rDFPCost = rDFPARs + serviceCost,
                rDFAdultPrice = (rDFPCost * adultCount),
                rDFAdultPriceFormat = rDFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'),
                rDFChildPrice = (rDFPCost * childCount),
                rDFChildPriceFormat = rDFChildPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'),
                rDFBabyPrice = (rDFPCost * babyCount),
                rDFBabyPriceFormat = rDFBabyPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
                $('.returnLogoAndCode .flightCode').html(rDFCode);
                $('.returnLogoAndCode .flightLogo').removeClass().addClass('flightLogo ' + rDFLogo);
                $('.returnFlow .departCityCode').html(rDFFCode);
                $('.returnFlow .arriveCityCode').html(rAFFCode);
                $('.returnFlow .departTime').html(rDFFTime);
                $('.returnFlow .arriveTime').html(rAFFTime);
                
                // fungsi hitung dari pilihan default
                if ((childCount !== 0) && (babyCount === 0)) {
                    $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + dFChildPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (dFAdultPrice + dFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
                    $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + rDFChildPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (rDFAdultPrice + rDFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>')
                    $('.totalPrice').html((dFAdultPrice + dFChildPrice + rDFAdultPrice + rDFChildPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
                } else if ((childCount !== 0) && (babyCount !== 0)) {
                    $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + dFChildPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + babyCount + '</span> Bayi <span class="passPrice">' + dFBabyPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (dFAdultPrice + dFChildPrice + dFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
                    $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + childCount + '</span> Anak <span class="passPrice">' + rDFChildPriceFormat + ' IDR</span></p><p class="priceCtr"><span class="passNum">' + babyCount + '</span> Bayi <span class="passPrice">' + rDFBabyPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + (rDFAdultPrice + rDFChildPrice + rDFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
                    $('.totalPrice').html((dFAdultPrice + dFChildPrice + dFBabyPrice + rDFAdultPrice + rDFChildPrice + rDFBabyPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
                } else {
                    $('#dFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + dFAdultPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + dFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
                    $('#rFPriceDetail').html('<p class="priceCtr"><span class="passNum">' + adultCount + '</span> Dewasa <span class="passPrice">' + rDFAdultPriceFormat + ' IDR</span></p><p class="priceCtr priceTotal"><span class="passPrice">' + rDFAdultPrice.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + ' IDR</span></p>');
                    $('.totalPrice').html((dFAdultPrice + rDFAdultPrice).toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,'))
                }
        } else {
            var optionPrice = $(this).find('.valueClassPrice').html();
            $(this).parent().parent().parent().parent().find('.flightPrice').html(optionPrice + ' IDR');
            $(this).parent().parent().hide();
        }
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
});