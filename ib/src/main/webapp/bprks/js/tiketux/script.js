jQuery(document).ready(function ($) {
    
    $('.passTab li').click(function () {
        $('.passTab li').removeClass('active');
        var activePass = $(this).attr('class');
        $('.passForm div').addClass('hide');
        $('.passForm').find('.' + activePass).removeClass('hide');
        $(this).addClass('active');
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
      
});