jQuery(document).ready(function ($) {
    'use strict';
var departQR = $('.departIDetail .fCCode').html(),
    returnQR = $('.returnIDetail .fCCode').html();
    $('.departIDetail .qRCode').qrcode({
        render: 'div',
        size: 50,
        text: departQR
    });
    
    $('.returnIDetail .qRCode').qrcode({
        render: 'div',
        size: 50,
        text: returnQR
    });
});