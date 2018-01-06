jQuery(document).ready(function ($) {
    'use strict';
    var departQR = $('.departIDetail .fCCode').html(),
        returnQR = $('.returnIDetail .fCCode').html();
    // fungsi untuk memilih maskapai
    var max = 2, // Maksimal maskapai yang bisa di pilih
        adultPassDef = 1, // Jumlah default penumpang dewasa
        adultChildPassMax = 7, // Jumlah maksimal penumpang dewasa
        childBabyPassDef = 0, // Jumlah default penumpang anak / Bayi
        babyPassMax = 4, // Jumlah maksimal penumpang dewasa
        serviceCost = 40000, // Harga service
        availableFlight = [{label: 'Pilihan Tersering', children: [
            {id: 'BDO', label: 'BANDUNG, BDO'},
            {id: 'DPS', label: 'DENPASAR, DPS'},
            {id: 'CGK', label: 'JAKARTA, CGK'},
            {id: 'KNO', label: 'MEDAN, KNO'},
            {id: 'SUB', label: 'SURABAYA, SUB'}]},{label: 'Pilih Kota', children: [
{id: 'ARD', label: 'ALOR ISLAND, ARD'},
{id: 'AMQ', label: 'AMBON, AMQ'},
{id: 'BPN', label: 'BALIKPAPAN, BPN'},
{id: 'BTJ', label: 'BANDA, BTJ'},
{id: 'TKG', label: 'BANDAR, TKG'},
{id: 'BDO', label: 'BANDUNG, BDO'},
{id: 'BDJ', label: 'BANJARMASIN, BDJ'},
{id: 'BTH', label: 'BATAM\/BATU, BTH'},
{id: 'BTW', label: 'BATULICIN, BTW'},
{id: 'BUW', label: 'BAU BAU, BUW'},
{id: 'BKS', label: 'BENGKULU, BKS'},
{id: 'BEJ', label: 'BERAU, BEJ'},
{id: 'BIK', label: 'BIAK, BIK'},
{id: 'BMU', label: 'BIMA, BMU'},
{id: 'BXT', label: 'BONTANG, BXT'},
{id: 'DPS', label: 'DENPASAR, DPS'},
{id: 'DUM', label: 'DUMAI, DUM'},
{id: 'ENE', label: 'ENDE, ENE'},
{id: 'FKQ', label: 'FAK FAK, FKQ'},
{id: 'GTO', label: 'GORONTALO, GTO'},
{id: 'CGK', label: 'JAKARTA, CGK'},
{id: 'HLP', label: 'JAKARTA HALIM, HLP'},
{id: 'DJB', label: 'JAMBI, DJB'},
{id: 'DJJ', label: 'JAYAPURA, DJJ'},
{id: 'KNG', label: 'KAIMANA, KNG'},
{id: 'KDI', label: 'KENDARI, KDI'},
{id: 'KTG', label: 'KETAPANG, KTG'},
{id: 'KBU', label: 'KOTA BARU, KBU'},
{id: 'KOE', label: 'KUPANG, KOE'},
{id: 'LBJ', label: 'LABUANBAJO, LBJ'},
{id: 'LSW', label: 'LHOKSEUMAWE, LSW'},
{id: 'LUW', label: 'LUWUK, LUW'},
{id: 'UPG', label: 'MAKASSAR, UPG'},
{id: 'MLG', label: 'MALANG, MLG'},
{id: 'LUV', label: 'MALUKU, LUV'},
{id: 'MJU', label: 'MAMUJU, MJU'},
{id: 'MDC', label: 'MANADO, MDC'},
{id: 'MKW', label: 'MANOKWARI, MKW'},
{id: 'LOP', label: 'MATARAM, LOP'},
{id: 'MOF', label: 'MAUMERE, MOF'},
{id: 'KNO', label: 'MEDAN, KNO'},
{id: 'MES', label: 'MEDAN, MES'},
{id: 'MNA', label: 'MELANGGUANE, MNA'},
{id: 'MKQ', label: 'MERAUKE, MKQ'},
{id: 'MEQ', label: 'MEULABOH, MEQ'},
{id: 'NBX', label: 'NABIRE, NBX'},
{id: 'NAH', label: 'NAHA, NAH'},
{id: 'GNS', label: 'NIAS, GNS'},
{id: 'BJW', label: 'NTT, BJW'},
{id: 'NNX', label: 'NUNUKAN, NNX'},
{id: 'PDG', label: 'PADANG, PDG'},
{id: 'AEG', label: 'PADANG SIDEMPUAN, AEG'},
{id: 'PKY', label: 'PALANGKARAYA, PKY'},
{id: 'PLM', label: 'PALEMBANG, PLM'},
{id: 'PLW', label: 'PALU, PLW'},
{id: 'PKN', label: 'PANGKALANBUN, PKN'},
{id: 'PGK', label: 'PANGKALPINANG, PGK'},
{id: 'PKU', label: 'PEKANBARU, PKU'},
{id: 'PNK', label: 'PONTIANAK, PNK'},
{id: 'PSJ', label: 'POSO, PSJ'},
{id: 'SAU', label: 'PULAU SAWU, SAU'},
{id: 'PSU', label: 'PUTUSSIBAU, PSU'},
{id: 'NTX', label: 'RIAU, NTX'},
{id: 'RTG', label: 'RUTENG, RTG'},
{id: 'SRI', label: 'SAMARINDA, SRI'},
{id: 'SMQ', label: 'SAMPIT, SMQ'},
{id: 'SRG', label: 'SEMARANG, SRG'},
{id: 'SBQ', label: 'SIBOLGA, SBQ'},
{id: 'SQT', label: 'SIBORONG-BORONG, SQT'},
{id: 'SNX', label: 'SINABANG, SNX'},
{id: 'SQG', label: 'SINTANG, SQG'},
{id: 'SOC', label: 'SOLO, SOC'},
{id: 'SOQ', label: 'SORONG, SOQ'},
{id: 'SWQ', label: 'SUMBAWA, SWQ'},
{id: 'SUB', label: 'SURABAYA, SUB'},
{id: 'TMC', label: 'TAMBOLAKA, TMC'},
{id: 'TJQ', label: 'TANJUNG PANDAN, TJQ'},
{id: 'TNJ', label: 'TANJUNG PINANG, TNJ'},
{id: 'TRK', label: 'TARAKAN, TRK'},
{id: 'TTE', label: 'TERNATE, TTE'},
{id: 'TIM', label: 'TIMIKA, TIM'},
{id: 'TLI', label: 'TOLI TOLI, TLI'},
{id: 'WGP', label: 'WAINGAPU, WGP'},
{id: 'WMX', label: 'WAMENA, WMX'},
{id: 'JOG', label: 'YOGYAKARTA, JOG'}
        ]}];
    var availableProvider = [
                {id: 'AERO', label: 'AEROTICKETING'},
                {id: 'VOLT', label: 'VOLTRAS'}];

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
            document.getElementById('airType').value='oneWay';
        } else {
            $('.returnDateContainer').show();
            document.getElementById('airType').value='return';
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
    $('.searchForm .departDate').datepicker({
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
    $('.searchForm .returnDate').datepicker({
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
            if (mKPLogo.match("airLine1")) { document.getElementById('hiddenAirasia').value='QZ'; }
            else if (mKPLogo.match("airLine2")) { document.getElementById('hiddenBatavia').value='Y6'; }
            else if (mKPLogo.match("airLine3")) { document.getElementById('hiddenCitilink').value='QG'; }
            else if (mKPLogo.match("airLine4")) { document.getElementById('hiddenGaruda').value='GA'; }
            else if (mKPLogo.match("airLine5")) { document.getElementById('hiddenGarudaExec').value='GE'; }
            else if (mKPLogo.match("airLine6")) { document.getElementById('hiddenKalstar').value='KD'; }
            else if (mKPLogo.match("airLine7")) { document.getElementById('hiddenLionair').value='JT'; }
            else if (mKPLogo.match("airLine8")) { document.getElementById('hiddenMerpati').value='MZ'; }
            else if (mKPLogo.match("airLine9")) { document.getElementById('hiddenSriwijaya').value='SJ'; }
            else if (mKPLogo.match("airLine10")) { document.getElementById('hiddenTiger').value='IR'; }
        }
        if ((checkClass !== true) && (current <= max + 1)) {
            $(this).parent().addClass('wEnb');
            $('.djatiKursi').find('#' + mKPLogo).show();
            if (mKPLogo ==  new String("djatiKursi1")) { document.getElementById('hiddendjatiKursi1').value='1'; }
            else if (mKPLogo ==  new String("djatiKursi2")) { document.getElementById('hiddendjatiKursi2').value='2'; }
            else if (mKPLogo ==  new String("djatiKursi3")) { document.getElementById('hiddendjatiKursi3').value='3'; }
            else if (mKPLogo ==  new String("djatiKursi4")) { document.getElementById('hiddendjatiKursi4').value='4'; }
            else if (mKPLogo ==  new String("djatiKursi5")) { document.getElementById('hiddendjatiKursi5').value='5'; }
            else if (mKPLogo ==  new String("djatiKursi6")) { document.getElementById('hiddendjatiKursi6').value='6'; }
            else if (mKPLogo ==  new String("djatiKursi7")) { document.getElementById('hiddendjatiKursi7').value='7'; }
            else if (mKPLogo ==  new String("djatiKursi8")) { document.getElementById('hiddendjatiKursi8').value='8'; }
            else if (mKPLogo ==  new String("djatiKursi9")) { document.getElementById('hiddendjatiKursi9').value='9'; }
            else if (mKPLogo ==  new String("djatiKursi10")) { document.getElementById('hiddendjatiKursi10').value='10'; }
            
            else if (mKPLogo ==  new String("djatiKursi11")) { document.getElementById('hiddendjatiKursi11').value='11'; }
            else if (mKPLogo ==  new String("djatiKursi12")) { document.getElementById('hiddendjatiKursi12').value='12'; }
            else if (mKPLogo ==  new String("djatiKursi13")) { document.getElementById('hiddendjatiKursi13').value='13'; }
            else if (mKPLogo ==  new String("djatiKursi14")) { document.getElementById('hiddendjatiKursi14').value='14'; }
            else if (mKPLogo ==  new String("djatiKursi15")) { document.getElementById('hiddendjatiKursi15').value='15'; }
            else if (mKPLogo ==  new String("djatiKursi16")) { document.getElementById('hiddendjatiKursi16').value='16'; }
            else if (mKPLogo ==  new String("djatiKursi17")) { document.getElementById('hiddendjatiKursi17').value='17'; }
            else if (mKPLogo ==  new String("djatiKursi18")) { document.getElementById('hiddendjatiKursi18').value='18'; }
            else if (mKPLogo ==  new String("djatiKursi19")) { document.getElementById('hiddendjatiKursi19').value='19'; }
            else if (mKPLogo ==  new String("djatiKursi20")) { document.getElementById('hiddendjatiKursi20').value='20'; }
            
            else if (mKPLogo ==  new String("djatiKursi21")) { document.getElementById('hiddendjatiKursi21').value='21'; }
            else if (mKPLogo ==  new String("djatiKursi22")) { document.getElementById('hiddendjatiKursi22').value='22'; }
            else if (mKPLogo ==  new String("djatiKursi23")) { document.getElementById('hiddendjatiKursi23').value='23'; }
            else if (mKPLogo ==  new String("djatiKursi24")) { document.getElementById('hiddendjatiKursi24').value='24'; }
            else if (mKPLogo ==  new String("djatiKursi25")) { document.getElementById('hiddendjatiKursi25').value='25'; }
            else if (mKPLogo ==  new String("djatiKursi26")) { document.getElementById('hiddendjatiKursi26').value='26'; }
            else if (mKPLogo ==  new String("djatiKursi27")) { document.getElementById('hiddendjatiKursi27').value='27'; }
            else if (mKPLogo ==  new String("djatiKursi28")) { document.getElementById('hiddendjatiKursi28').value='28'; }
            else if (mKPLogo ==  new String("djatiKursi29")) { document.getElementById('hiddendjatiKursi29').value='29'; }
            else if (mKPLogo ==  new String("djatiKursi30")) { document.getElementById('hiddendjatiKursi30').value='30'; }
            
            else if (mKPLogo ==  new String("djatiKursi31")) { document.getElementById('hiddendjatiKursi31').value='31'; }
            else if (mKPLogo ==  new String("djatiKursi32")) { document.getElementById('hiddendjatiKursi32').value='32'; }
            else if (mKPLogo ==  new String("djatiKursi33")) { document.getElementById('hiddendjatiKursi33').value='33'; }
            else if (mKPLogo ==  new String("djatiKursi34")) { document.getElementById('hiddendjatiKursi34').value='34'; }
            else if (mKPLogo ==  new String("djatiKursi35")) { document.getElementById('hiddendjatiKursi35').value='35'; }
            else if (mKPLogo ==  new String("djatiKursi36")) { document.getElementById('hiddendjatiKursi36').value='36'; }
            else if (mKPLogo ==  new String("djatiKursi37")) { document.getElementById('hiddendjatiKursi37').value='37'; }
            else if (mKPLogo ==  new String("djatiKursi38")) { document.getElementById('hiddendjatiKursi38').value='38'; }
            else if (mKPLogo ==  new String("djatiKursi39")) { document.getElementById('hiddendjatiKursi39').value='39'; }
            else if (mKPLogo ==  new String("djatiKursi40")) { document.getElementById('hiddendjatiKursi40').value='40'; }
            
            else if (mKPLogo ==  new String("djatiKursi41")) { document.getElementById('hiddendjatiKursi41').value='41'; }
            else if (mKPLogo ==  new String("djatiKursi42")) { document.getElementById('hiddendjatiKursi42').value='42'; }
            else if (mKPLogo ==  new String("djatiKursi43")) { document.getElementById('hiddendjatiKursi43').value='43'; }
            else if (mKPLogo ==  new String("djatiKursi44")) { document.getElementById('hiddendjatiKursi44').value='44'; }
            else if (mKPLogo ==  new String("djatiKursi45")) { document.getElementById('hiddendjatiKursi45').value='45'; }
            else if (mKPLogo ==  new String("djatiKursi46")) { document.getElementById('hiddendjatiKursi46').value='46'; }
            else if (mKPLogo ==  new String("djatiKursi47")) { document.getElementById('hiddendjatiKursi47').value='47'; }
            else if (mKPLogo ==  new String("djatiKursi48")) { document.getElementById('hiddendjatiKursi48').value='48'; }
            else if (mKPLogo ==  new String("djatiKursi49")) { document.getElementById('hiddendjatiKursi49').value='49'; }
            else if (mKPLogo ==  new String("djatiKursi50")) { document.getElementById('hiddendjatiKursi50').value='50'; }
            
        }else {
        	
        	
            $(this).parent().removeClass('wEnb');
            $('.airLines').find('#' + mKPLogo).hide();
            if (mKPLogo.match("airLine1")) { document.getElementById('hiddenAirasia').value=''; }
            else if (mKPLogo.match("airLine2")) { document.getElementById('hiddenBatavia').value=''; }
            else if (mKPLogo.match("airLine3")) { document.getElementById('hiddenCitilink').value=''; }
            else if (mKPLogo.match("airLine4")) { document.getElementById('hiddenGaruda').value=''; }
            else if (mKPLogo.match("airLine5")) { document.getElementById('hiddenGarudaExec').value=''; }
            else if (mKPLogo.match("airLine6")) { document.getElementById('hiddenKalstar').value=''; }
            else if (mKPLogo.match("airLine7")) { document.getElementById('hiddenLionair').value=''; }
            else if (mKPLogo.match("airLine8")) { document.getElementById('hiddenMerpati').value=''; }
            else if (mKPLogo.match("airLine9")) { document.getElementById('hiddenSriwijaya').value=''; }
            else if (mKPLogo.match("airLine10")) { document.getElementById('hiddenTiger').value=''; }
            
            $('.djatiKursi').find('#' + mKPLogo).hide();
            if (mKPLogo ==  new String("djatiKursi1")) { document.getElementById('hiddendjatiKursi1').value=''; }
            else if (mKPLogo ==  new String("djatiKursi2")) { document.getElementById('hiddendjatiKursi2').value=''; }
            else if (mKPLogo ==  new String("djatiKursi3")) { document.getElementById('hiddendjatiKursi3').value=''; }
            else if (mKPLogo ==  new String("djatiKursi4")) { document.getElementById('hiddendjatiKursi4').value=''; }
            else if (mKPLogo ==  new String("djatiKursi5")) { document.getElementById('hiddendjatiKursi5').value=''; }
            else if (mKPLogo ==  new String("djatiKursi6")) { document.getElementById('hiddendjatiKursi6').value=''; }
            else if (mKPLogo ==  new String("djatiKursi7")) { document.getElementById('hiddendjatiKursi7').value=''; }
            else if (mKPLogo ==  new String("djatiKursi8")) { document.getElementById('hiddendjatiKursi8').value=''; }
            else if (mKPLogo ==  new String("djatiKursi9")) { document.getElementById('hiddendjatiKursi9').value=''; }
            else if (mKPLogo ==  new String("djatiKursi10")) { document.getElementById('hiddendjatiKursi10').value=''; }
            
            else if (mKPLogo ==  new String("djatiKursi11")) { document.getElementById('hiddendjatiKursi11').value=''; }
            else if (mKPLogo ==  new String("djatiKursi12")) { document.getElementById('hiddendjatiKursi12').value=''; }
            else if (mKPLogo ==  new String("djatiKursi13")) { document.getElementById('hiddendjatiKursi13').value=''; }
            else if (mKPLogo ==  new String("djatiKursi14")) { document.getElementById('hiddendjatiKursi14').value=''; }
            else if (mKPLogo ==  new String("djatiKursi15")) { document.getElementById('hiddendjatiKursi15').value=''; }
            else if (mKPLogo ==  new String("djatiKursi16")) { document.getElementById('hiddendjatiKursi16').value=''; }
            else if (mKPLogo ==  new String("djatiKursi17")) { document.getElementById('hiddendjatiKursi17').value=''; }
            else if (mKPLogo ==  new String("djatiKursi18")) { document.getElementById('hiddendjatiKursi18').value=''; }
            else if (mKPLogo ==  new String("djatiKursi19")) { document.getElementById('hiddendjatiKursi19').value=''; }
            else if (mKPLogo ==  new String("djatiKursi20")) { document.getElementById('hiddendjatiKursi20').value=''; }
            
            else if (mKPLogo ==  new String("djatiKursi21")) { document.getElementById('hiddendjatiKursi21').value=''; }
            else if (mKPLogo ==  new String("djatiKursi22")) { document.getElementById('hiddendjatiKursi22').value=''; }
            else if (mKPLogo ==  new String("djatiKursi23")) { document.getElementById('hiddendjatiKursi23').value=''; }
            else if (mKPLogo ==  new String("djatiKursi24")) { document.getElementById('hiddendjatiKursi24').value=''; }
            else if (mKPLogo ==  new String("djatiKursi25")) { document.getElementById('hiddendjatiKursi25').value=''; }
            else if (mKPLogo ==  new String("djatiKursi26")) { document.getElementById('hiddendjatiKursi26').value=''; }
            else if (mKPLogo ==  new String("djatiKursi27")) { document.getElementById('hiddendjatiKursi27').value=''; }
            else if (mKPLogo ==  new String("djatiKursi28")) { document.getElementById('hiddendjatiKursi28').value=''; }
            else if (mKPLogo ==  new String("djatiKursi29")) { document.getElementById('hiddendjatiKursi29').value=''; }
            else if (mKPLogo ==  new String("djatiKursi30")) { document.getElementById('hiddendjatiKursi30').value=''; }
            
            else if (mKPLogo ==  new String("djatiKursi31")) { document.getElementById('hiddendjatiKursi31').value=''; }
            else if (mKPLogo ==  new String("djatiKursi32")) { document.getElementById('hiddendjatiKursi32').value=''; }
            else if (mKPLogo ==  new String("djatiKursi33")) { document.getElementById('hiddendjatiKursi33').value=''; }
            else if (mKPLogo ==  new String("djatiKursi34")) { document.getElementById('hiddendjatiKursi34').value=''; }
            else if (mKPLogo ==  new String("djatiKursi35")) { document.getElementById('hiddendjatiKursi35').value=''; }
            else if (mKPLogo ==  new String("djatiKursi36")) { document.getElementById('hiddendjatiKursi36').value=''; }
            else if (mKPLogo ==  new String("djatiKursi37")) { document.getElementById('hiddendjatiKursi37').value=''; }
            else if (mKPLogo ==  new String("djatiKursi38")) { document.getElementById('hiddendjatiKursi38').value=''; }
            else if (mKPLogo ==  new String("djatiKursi39")) { document.getElementById('hiddendjatiKursi39').value=''; }
            else if (mKPLogo ==  new String("djatiKursi40")) { document.getElementById('hiddendjatiKursi40').value=''; }
            
            else if (mKPLogo ==  new String("djatiKursi41")) { document.getElementById('hiddendjatiKursi41').value=''; }
            else if (mKPLogo ==  new String("djatiKursi42")) { document.getElementById('hiddendjatiKursi42').value=''; }
            else if (mKPLogo ==  new String("djatiKursi43")) { document.getElementById('hiddendjatiKursi43').value=''; }
            else if (mKPLogo ==  new String("djatiKursi44")) { document.getElementById('hiddendjatiKursi44').value=''; }
            else if (mKPLogo ==  new String("djatiKursi45")) { document.getElementById('hiddendjatiKursi45').value=''; }
            else if (mKPLogo ==  new String("djatiKursi46")) { document.getElementById('hiddendjatiKursi46').value=''; }
            else if (mKPLogo ==  new String("djatiKursi47")) { document.getElementById('hiddendjatiKursi47').value=''; }
            else if (mKPLogo ==  new String("djatiKursi48")) { document.getElementById('hiddendjatiKursi48').value=''; }
            else if (mKPLogo ==  new String("djatiKursi49")) { document.getElementById('hiddendjatiKursi49').value=''; }
            else if (mKPLogo ==  new String("djatiKursi50")) { document.getElementById('hiddendjatiKursi50').value=''; }
            
            
        }
        if (current >= 1) {
            $('.resetPBtnDsb, .submitPBtnDsb').hide();
            $('.resetPBtn, .submitPBtn').show();
            
            $('.resetPBtnDsb, .submitKBtnDsb').hide();
            $('.resetPBtn, .submitKursi').show();
            
        } else {
            $('.resetPBtn, .submitPBtn').hide();
            $('.resetPBtnDsb, .submitPBtnDsb').show();
            
            $('.resetPBtn, .submitKursi').hide();
            $('.resetPBtnDsb, .submitKBtnDsb').show();
        }
    });
    
    // fungsi untuk mereset pilihan maskapai
    $('.resetPBtn').click(function () {
        $('input[type="checkbox"].mKPL').removeAttr('checked');
        $('.aLList').removeClass('wDsb wEnb');
        $('.resetPBtn, .submitPBtn').hide();
        $('.resetPBtnDsb, .submitPBtnDsb').show();
        $('.resetPBtn, .submitKursi').hide();
        $('.resetPBtnDsb, .submitKBtnDsb').show();
        $('.aLList').removeClass('wDsb');
        $('.wEnb').find('.aLTrans').hide();
        $('input[type="checkbox"].mKPL').removeAttr('disabled');
        $('.airLines').find('.airLine').hide();
        document.getElementById('hiddenAirasia').value='';
        document.getElementById('hiddenBatavia').value='';
        document.getElementById('hiddenCitilink').value='';
        document.getElementById('hiddenGaruda').value='';
        document.getElementById('hiddenGarudaExec').value='';
        document.getElementById('hiddenKalstar').value='';
        document.getElementById('hiddenLionair').value='';
        document.getElementById('hiddenTiger').value='';
        document.getElementById('hiddenMerpati').value='';
        document.getElementById('hiddenSriwijaya').value='';
        
        $(this).parent().removeClass('wEnb');
        $('.djatiKursi').find('#' + mKPLogo).hide();
        if (mKPLogo ==  new String("djatiKursi1")) { document.getElementById('hiddendjatiKursi1').value=''; }
        else if (mKPLogo ==  new String("djatiKursi2")) { document.getElementById('hiddendjatiKursi2').value=''; }
        else if (mKPLogo ==  new String("djatiKursi3")) { document.getElementById('hiddendjatiKursi3').value=''; }
        else if (mKPLogo ==  new String("djatiKursi4")) { document.getElementById('hiddendjatiKursi4').value=''; }
        else if (mKPLogo ==  new String("djatiKursi5")) { document.getElementById('hiddendjatiKursi5').value=''; }
        else if (mKPLogo ==  new String("djatiKursi6")) { document.getElementById('hiddendjatiKursi6').value=''; }
        else if (mKPLogo ==  new String("djatiKursi7")) { document.getElementById('hiddendjatiKursi7').value=''; }
        else if (mKPLogo ==  new String("djatiKursi8")) { document.getElementById('hiddendjatiKursi8').value=''; }
        else if (mKPLogo ==  new String("djatiKursi9")) { document.getElementById('hiddendjatiKursi9').value=''; }
        else if (mKPLogo ==  new String("djatiKursi10")) { document.getElementById('hiddendjatiKursi10').value=''; }
        
        else if (mKPLogo ==  new String("djatiKursi11")) { document.getElementById('hiddendjatiKursi11').value=''; }
        else if (mKPLogo ==  new String("djatiKursi12")) { document.getElementById('hiddendjatiKursi12').value=''; }
        else if (mKPLogo ==  new String("djatiKursi13")) { document.getElementById('hiddendjatiKursi13').value=''; }
        else if (mKPLogo ==  new String("djatiKursi14")) { document.getElementById('hiddendjatiKursi14').value=''; }
        else if (mKPLogo ==  new String("djatiKursi15")) { document.getElementById('hiddendjatiKursi15').value=''; }
        else if (mKPLogo ==  new String("djatiKursi16")) { document.getElementById('hiddendjatiKursi16').value=''; }
        else if (mKPLogo ==  new String("djatiKursi17")) { document.getElementById('hiddendjatiKursi17').value=''; }
        else if (mKPLogo ==  new String("djatiKursi18")) { document.getElementById('hiddendjatiKursi18').value=''; }
        else if (mKPLogo ==  new String("djatiKursi19")) { document.getElementById('hiddendjatiKursi19').value=''; }
        else if (mKPLogo ==  new String("djatiKursi20")) { document.getElementById('hiddendjatiKursi20').value=''; }
        
        else if (mKPLogo ==  new String("djatiKursi21")) { document.getElementById('hiddendjatiKursi21').value=''; }
        else if (mKPLogo ==  new String("djatiKursi22")) { document.getElementById('hiddendjatiKursi22').value=''; }
        else if (mKPLogo ==  new String("djatiKursi23")) { document.getElementById('hiddendjatiKursi23').value=''; }
        else if (mKPLogo ==  new String("djatiKursi24")) { document.getElementById('hiddendjatiKursi24').value=''; }
        else if (mKPLogo ==  new String("djatiKursi25")) { document.getElementById('hiddendjatiKursi25').value=''; }
        else if (mKPLogo ==  new String("djatiKursi26")) { document.getElementById('hiddendjatiKursi26').value=''; }
        else if (mKPLogo ==  new String("djatiKursi27")) { document.getElementById('hiddendjatiKursi27').value=''; }
        else if (mKPLogo ==  new String("djatiKursi28")) { document.getElementById('hiddendjatiKursi28').value=''; }
        else if (mKPLogo ==  new String("djatiKursi29")) { document.getElementById('hiddendjatiKursi29').value=''; }
        else if (mKPLogo ==  new String("djatiKursi30")) { document.getElementById('hiddendjatiKursi30').value=''; }
        
        else if (mKPLogo ==  new String("djatiKursi31")) { document.getElementById('hiddendjatiKursi31').value=''; }
        else if (mKPLogo ==  new String("djatiKursi32")) { document.getElementById('hiddendjatiKursi32').value=''; }
        else if (mKPLogo ==  new String("djatiKursi33")) { document.getElementById('hiddendjatiKursi33').value=''; }
        else if (mKPLogo ==  new String("djatiKursi34")) { document.getElementById('hiddendjatiKursi34').value=''; }
        else if (mKPLogo ==  new String("djatiKursi35")) { document.getElementById('hiddendjatiKursi35').value=''; }
        else if (mKPLogo ==  new String("djatiKursi36")) { document.getElementById('hiddendjatiKursi36').value=''; }
        else if (mKPLogo ==  new String("djatiKursi37")) { document.getElementById('hiddendjatiKursi37').value=''; }
        else if (mKPLogo ==  new String("djatiKursi38")) { document.getElementById('hiddendjatiKursi38').value=''; }
        else if (mKPLogo ==  new String("djatiKursi39")) { document.getElementById('hiddendjatiKursi39').value=''; }
        else if (mKPLogo ==  new String("djatiKursi40")) { document.getElementById('hiddendjatiKursi40').value=''; }
        
        else if (mKPLogo ==  new String("djatiKursi41")) { document.getElementById('hiddendjatiKursi41').value=''; }
        else if (mKPLogo ==  new String("djatiKursi42")) { document.getElementById('hiddendjatiKursi42').value=''; }
        else if (mKPLogo ==  new String("djatiKursi43")) { document.getElementById('hiddendjatiKursi43').value=''; }
        else if (mKPLogo ==  new String("djatiKursi44")) { document.getElementById('hiddendjatiKursi44').value=''; }
        else if (mKPLogo ==  new String("djatiKursi45")) { document.getElementById('hiddendjatiKursi45').value=''; }
        else if (mKPLogo ==  new String("djatiKursi46")) { document.getElementById('hiddendjatiKursi46').value=''; }
        else if (mKPLogo ==  new String("djatiKursi47")) { document.getElementById('hiddendjatiKursi47').value=''; }
        else if (mKPLogo ==  new String("djatiKursi48")) { document.getElementById('hiddendjatiKursi48').value=''; }
        else if (mKPLogo ==  new String("djatiKursi49")) { document.getElementById('hiddendjatiKursi49').value=''; }
        else if (mKPLogo ==  new String("djatiKursi50")) { document.getElementById('hiddendjatiKursi50').value=''; }
        
       
        
    });
    
    // fungsi untuk membatalkan pilihan maskapai
    $('.cancelPBtn').click(function () {
        $('input[type="checkbox"].mKPL').removeAttr('checked');
        $('.aLList').removeClass('wDsb wEnb');
        $('.resetPBtn, .submitPBtn').hide();
        $('.resetPBtnDsb, .submitPBtnDsb').show();
        $('.resetPBtn, .submitKursi').hide();
        $('.resetPBtnDsb, .submitKBtnDsb').show();
        $('.aLList').removeClass('wDsb');
        $('.wEnb').find('.aLTrans').hide();
        $('input[type="checkbox"].mKPL').removeAttr('disabled');
        $('.aLOption').hide();
        $('.pMBText').removeClass('pMBTAct');
        $('.airLines').find('.airLine').hide();
        $('.aLOption').hide();
        
        document.getElementById('hiddendjatiKursi1').value='';
        document.getElementById('hiddendjatiKursi2').value=''; 
        document.getElementById('hiddendjatiKursi3').value=''; 
        document.getElementById('hiddendjatiKursi4').value='';
        document.getElementById('hiddendjatiKursi5').value=''; 
        document.getElementById('hiddendjatiKursi6').value=''; 
        document.getElementById('hiddendjatiKursi7').value=''; 
        document.getElementById('hiddendjatiKursi8').value=''; 
        document.getElementById('hiddendjatiKursi9').value=''; 
        document.getElementById('hiddendjatiKursi10').value=''; 
        
        document.getElementById('hiddendjatiKursi11').value=''; 
        document.getElementById('hiddendjatiKursi12').value=''; 
        document.getElementById('hiddendjatiKursi13').value=''; 
        document.getElementById('hiddendjatiKursi14').value=''; 
        document.getElementById('hiddendjatiKursi15').value=''; 
        document.getElementById('hiddendjatiKursi16').value=''; 
        document.getElementById('hiddendjatiKursi17').value=''; 
        document.getElementById('hiddendjatiKursi18').value=''; 
        document.getElementById('hiddendjatiKursi19').value=''; 
        document.getElementById('hiddendjatiKursi20').value=''; 
        
        document.getElementById('hiddendjatiKursi21').value=''; 
        document.getElementById('hiddendjatiKursi22').value=''; 
        document.getElementById('hiddendjatiKursi23').value=''; 
        document.getElementById('hiddendjatiKursi24').value=''; 
        document.getElementById('hiddendjatiKursi25').value=''; 
        document.getElementById('hiddendjatiKursi26').value=''; 
        document.getElementById('hiddendjatiKursi27').value=''; 
        document.getElementById('hiddendjatiKursi28').value=''; 
        document.getElementById('hiddendjatiKursi29').value=''; 
        document.getElementById('hiddendjatiKursi30').value=''; 
        
        document.getElementById('hiddendjatiKursi31').value=''; 
        document.getElementById('hiddendjatiKursi32').value=''; 
        document.getElementById('hiddendjatiKursi33').value=''; 
        document.getElementById('hiddendjatiKursi34').value=''; 
        document.getElementById('hiddendjatiKursi35').value=''; 
        document.getElementById('hiddendjatiKursi36').value=''; 
        document.getElementById('hiddendjatiKursi37').value=''; 
        document.getElementById('hiddendjatiKursi38').value=''; 
        document.getElementById('hiddendjatiKursi39').value=''; 
        document.getElementById('hiddendjatiKursi40').value=''; 
        
        document.getElementById('hiddendjatiKursi41').value=''; 
        document.getElementById('hiddendjatiKursi42').value=''; 
        document.getElementById('hiddendjatiKursi43').value=''; 
        document.getElementById('hiddendjatiKursi44').value=''; 
        document.getElementById('hiddendjatiKursi45').value=''; 
        document.getElementById('hiddendjatiKursi46').value=''; 
        document.getElementById('hiddendjatiKursi47').value=''; 
        document.getElementById('hiddendjatiKursi48').value=''; 
        document.getElementById('hiddendjatiKursi49').value=''; 
       document.getElementById('hiddendjatiKursi50').value=''; 
        
        
        
        return false;
    });
    
    // fungsi untuk memilih pilihan maskapai
    $('.submitPBtn').click(function () {
        $('.aLOption').hide();
        $('.pMBText').removeClass('pMBTAct');
        return false;
    });
    
    
 // fungsi untuk memilih pilihan Kursi
    $('.submitKursi').click(function () {
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
    $('.providerInput').select2({placeholder: 'Penyedia Jasa',
                data: { results: availableProvider, text: 'label' },
                formatSelection: format,
                formatResult: format});
});