jQuery(document).ready(function ($) {
    'use strict';
    var departQR = $('.departIDetail .fCCode').html(),
        returnQR = $('.returnIDetail .fCCode').html();
    // fungsi untuk memilih maskapai
    var max = 3, // Maksimal maskapai yang bisa di pilih
        adultPassDef = 1, // Jumlah default penumpang dewasa
        adultChildPassMax = 7, // Jumlah maksimal penumpang dewasa
        childBabyPassDef = 0, // Jumlah default penumpang anak / Bayi
        babyPassMax = 4, // Jumlah maksimal penumpang dewasa
        serviceCost = 40000, // Harga service
        availableFlight = [{label: 'Pilihan Tersering', children: [{id: 1, label: 'Bandung, BDO'},
            {id: 2, label: 'Denpasar, DPS'},
            {id: 3, label: 'Jakarta, CKG'},
            {id: 4, label: 'Medan, MES'},
            {id: 5, label: 'Surabaya, SUB'}]}, {label: 'Pilih Kota', children: [
            {id: 6, label: 'Alor Island, ARD'},
            {id: 7, label: 'Ambon, AMQ'},
            {id: 8, label: 'Balikpapan, BPN'},
            {id: 9, label: 'Banda, BTJ'},
            {id: 10, label: 'Bandar, TLG'},
            {id: 11, label: 'Bandung, BDO'},
            {id: 12, label: 'Banjarmasin, BDJ'},
            {id: 13, label: 'Batam/Batu, BTH'},
            {id: 14, label: 'Batulicin, BTW'},
            {id: 15, label: 'Bau Bau, BUW'},
            {id: 16, label: 'Bengkulu, BKS'},
            {id: 17, label: 'Berau, BEJ'},
            {id: 18, label: 'Biak, BIK'},
            {id: 19, label: 'Bima, BMU'},
            {id: 20, label: 'Botang, BXT'},
            {id: 21, label: 'Denpasar, DPS'},
            {id: 22, label: 'Dumai, DUM'},
            {id: 23, label: 'Ende, ENE'},
            {id: 24, label: 'Fak Fak, FKQ'},
            {id: 25, label: 'Gorontalo, GTO'},
            {id: 26, label: 'Jakarta, CKG'},
            {id: 27, label: 'Jakarta Halim, HLP'},
            {id: 28, label: 'Jambi, DJB'},
            {id: 29, label: 'Jayapura, DJJ'},
            {id: 30, label: 'Kaimana, KNG'},
            {id: 31, label: 'Kendari, KDI'},
            {id: 32, label: 'Ketapang, KTG'},
            {id: 33, label: 'Kota Baru, KBU'},
            {id: 34, label: 'Kupang, KOE'},
            {id: 35, label: 'Labuanbajo, LBJ'},
            {id: 36, label: 'Lhokseumawe, LSW'},
            {id: 37, label: 'Luwuk, LUW'},
            {id: 38, label: 'Makassar, UPG'},
            {id: 39, label: 'Malang, MLG'},
            {id: 40, label: 'Maluku, LUV'},
            {id: 41, label: 'Mamuju, MJU'},
            {id: 42, label: 'Manado, MDC'},
            {id: 43, label: 'Manowari, MKW'},
            {id: 44, label: 'Mataram, LOP'},
            {id: 45, label: 'Maumere, MOF'},
            {id: 46, label: 'Medan, KNO'},
            {id: 47, label: 'Medan, MES'},
            {id: 48, label: 'Melangguane, MNA'},
            {id: 49, label: 'Merauke, MKQ'},
            {id: 50, label: 'Meulaboh, MEQ'},
            {id: 51, label: 'Nabire, NBX'},
            {id: 52, label: 'Naha, NAH'},
            {id: 53, label: 'Nias, GNS'},
            {id: 54, label: 'NTT, BJW'},
            {id: 55, label: 'Nunukan, NNX'},
            {id: 56, label: 'Padang, PDG'},
            {id: 57, label: 'Padang Sidempuan, AEG'},
            {id: 58, label: 'Palangkaraya, PKY'},
            {id: 59, label: 'Palembang, PLM'},
            {id: 60, label: 'Palu, PLW'},
            {id: 61, label: 'Pangkalabuan, PKN'},
            {id: 62, label: 'Pangkalpinang, PGK'},
            {id: 63, label: 'Pekanbaru, PKU'},
            {id: 64, label: 'Poso, PSJ'},
            {id: 65, label: 'Pulau Sewu, SAU'},
            {id: 66, label: 'Putussibau, PSU'},
            {id: 67, label: 'Riau, NTX'},
            {id: 68, label: 'Ruteng, RTG'},
            {id: 69, label: 'Samarinda, SRI'},
            {id: 70, label: 'Sampit, SMQ'},
            {id: 71, label: 'Semarang, SRG'},
            {id: 72, label: 'Sibolga, SBQ'},
            {id: 73, label: 'Siborong-borong, SQT'},
            {id: 74, label: 'Sinabang, SNX'},
            {id: 75, label: 'Sintang, SQG'},
            {id: 76, label: 'Solo, SOC'},
            {id: 77, label: 'Sorong, SOQ'},
            {id: 78, label: 'Sumbawa, SWQ'},
            {id: 79, label: 'Surabaya, SUB'},
            {id: 80, label: 'Tambolaka, TMC'},
            {id: 81, label: 'Tanjung Pandan, TJQ'},
            {id: 82, label: 'Tanjung Pinang, TNJ'},
            {id: 83, label: 'Tarkan, TRK'},
            {id: 84, label: 'Ternate, TTE'},
            {id: 85, label: 'Timika, TIM'},
            {id: 86, label: 'Toli Toli, TLI'},
            {id: 87, label: 'Waingaru, WGP'},
            {id: 88, label: 'Warnena, WMZ'},
            {id: 89, label: 'Yogyakarta, JOG'}
        ]}];
    
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
        if (findAType !== 'fTReturn') {
            $('.returnDateContainer').hide();
        } else {
            $('.returnDateContainer').show();
        }
    });
    
    // fungsi untuk memilih tanggal lahir
    $('.passForm .birthDate').datepicker({
        showOn: "both",
        buttonImage: "img/calendar.png",
        buttonImageOnly: true,
        defaultDate: '+1w',
        dateFormat: 'dd-mm-yy',
        changeMonth: true,
        changeYear: true
    });
    
    // fungsi untuk memilih tanggal keberangkatan
    $('.searchForm .departDate, .searchFormTrain .departDate').datepicker({
        showOn: "both",
        buttonImage: "img/calendar.png",
        buttonImageOnly: true,
        defaultDate: '+1w',
        dateFormat: 'dd-mm-yy',
        changeMonth: true,
        numberOfMonths: 2,
        onClose: function (selectedDate) {
            $('.returnDate').datepicker('option', 'minDate', selectedDate);
        }
    });
    
    // fungsi untuk memilih tanggal kembali
    $('.searchForm .returnDate, .searchFormTrain .returnDate').datepicker({
        showOn: "both",
        buttonImage: "img/calendar.png",
        buttonImageOnly: true,
        defaultDate: '+1w',
        dateFormat: 'dd-mm-yy',
        changeMonth: true,
        numberOfMonths: 2,
        onClose: function (selectedDate) {
            $('.departDate').datepicker('option', 'maxDate', selectedDate);
        }
    });
    
    // fungsi untuk buka pilihan maskapai
    $('.pMBtn').click(function () {
        $(this).find('.pMBText').addClass('pMBTAct');
        $(this).find('.aLOption').show();
    });
    
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
            if (getInputValue < babyPassMax) {
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
            
            if (getInputValue > babyPassMax) {
                alert('Maaf, Jumlah tidak boleh lebih dari ' + babyPassMax);
                $(this).val(babyPassMax);
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
    
    // fungsi checkbox beserta properti styling
    $('input[type="checkbox"].mKPL').change(function () {
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
            $(this).parent().addClass('wEnb');
            $('.airLines').find('#' + mKPLogo).show();
        } else {
            $(this).parent().removeClass('wEnb');
            $('.airLines').find('#' + mKPLogo).hide();
        }
        if (current >= 1) {
            $('.resetPBtnDsb, .submitPBtnDsb').hide();
            $('.resetPBtn, .submitPBtn').show();
        } else {
            $('.resetPBtn, .submitPBtn').hide();
            $('.resetPBtnDsb, .submitPBtnDsb').show();
        }
    });
    
    // fungsi untuk mereset pilihan maskapai
    $('.resetPBtn').click(function () {
        $('input[type="checkbox"].mKPL').removeAttr('checked');
        $('.aLList').removeClass('wDsb wEnb');
        $('.resetPBtn, .submitPBtn').hide();
        $('.resetPBtnDsb, .submitPBtnDsb').show();
        $('.aLList').removeClass('wDsb');
        $('.wEnb').find('.aLTrans').hide();
        $('input[type="checkbox"].mKPL').removeAttr('disabled');
        $('.airLines').find('.airLine').hide();
    });
    
    // fungsi untuk membatalkan pilihan maskapai
    $('.cancelPBtn').click(function () {
        $('input[type="checkbox"].mKPL').removeAttr('checked');
        $('.aLList').removeClass('wDsb wEnb');
        $('.resetPBtn, .submitPBtn').hide();
        $('.resetPBtnDsb, .submitPBtnDsb').show();
        $('.aLList').removeClass('wDsb');
        $('.wEnb').find('.aLTrans').hide();
        $('input[type="checkbox"].mKPL').removeAttr('disabled');
        $('.aLOption').hide();
        $('.pMBText').removeClass('pMBTAct');
        $('.airLines').find('.airLine').hide();
        return false;
    });
    
    // fungsi untuk memilih pilihan maskapai
    $('.submitPBtn').click(function () {
        $('.aLOption').hide();
        $('.pMBText').removeClass('pMBTAct');
        return false;
    });
    
    // Parse Json
    function format(item) { return item.label; }
    
    $('.departInput').select2({placeholder: 'Kota asal',
            data: { results: availableFlight, text: 'label' },
            formatSelection: format,
            formatResult: format});
    $('.arriveInput').select2({placeholder: 'Kota tujuan',
            data: { results: availableFlight, text: 'label' },
            formatSelection: format,
            formatResult: format});
});