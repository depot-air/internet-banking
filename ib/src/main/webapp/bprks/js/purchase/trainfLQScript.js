jQuery(document).ready(function ($) {
    'use strict';
var departQR = $('.departIDetail .fCCode').html(),
    returnQR = $('.returnIDetail .fCCode').html(),
    findLayout = $('.trainLayout').find('div').attr('class');
	var idSeat = ''; 
	var seatNumber = '';
	var rowNumber = '';
	var colNumber = '';
	var wagon = '';

	function initLayout(seatRow, seatPerRow) {
		var i = 1,
		j = 1,
		drawLayout = '<ul>';
		
		drawLayout += '<li class="colLayout row0"><ul>';
		for (j=1; j<=seatPerRow; j++){
			drawLayout += '<li class="col' + j + '"><span class="noseat">'+String.fromCharCode(64+j)+'</span></li>';
		}
		drawLayout += '</ul></li>';		
		
		for (i=1; i<=seatRow; i++){
			drawLayout += '<li class="colLayout row' + i + '"><ul>';
			for (j=1; j<=seatPerRow; j++){
				drawLayout += '<li class="col' + j + '"><span></span></li>';
			}
			drawLayout += '<li class="col' + seatPerRow + '"><span class="noseat">' + i + '</span></li>';
			drawLayout += '</ul></li>';
		}
		drawLayout += '</ul>';
		return drawLayout;
	}
    	
    $('.changeSeat').click(function(){
    	idSeat = 'seat' + $(this).attr('id').substring('btnChangeSeat'.length);
    	wagon = $("#wagon option:selected").text();
    	var seatRow = parseInt($('#' + wagon + ' #rowCount').html());
    	var seatCol = parseInt($('#' + wagon + ' #colCount').html());
    	$('#seatLayout').html(initLayout(seatRow,seatCol));
    	$('#seatLayout li').find('ul li:eq( 1 )').css('margin-bottom', '20px');		
    	var seatElement = $('#seatLayout li.colLayout').css('margin-left', '10px').find('li span');
    	$( seatElement ).each(function( ) {			
    		if (!$(this).hasClass('noseat')) {
    			$(this).addClass('seatLeft');
    		}
    	});		
    	$('#seatLayout').css('margin', '0 auto').css('width', (seatRow+1) * 33);
    	$($('#' + wagon + ' #noSeat').html()).addClass('noseat');
    	$($('#' + wagon + ' #filledSeat').html()).addClass('filled');
//    	$($('#' + wagon + ' #activeSeat').html()).addClass('active');
    	var activeSeatName = $('#' + idSeat).html();
    	var activeSeatRow = activeSeatName.substring(0, activeSeatName.length-1);
    	var activeSeatCol = activeSeatName.substring(activeSeatName.length-1).charCodeAt(0) - 64;
    	var activeElement = '.row' + activeSeatRow + ' .col' + activeSeatCol;
    	$(activeElement).removeClass('filled');
    	$(activeElement).addClass('active');
    	        
    	$('.trainLayout .colLayout li').click(function(){
    		if ($(this).children('span').hasClass('noseat')) {
    			return;
    		}
    		var seatNumberClass = $(this).attr('class');
    		var arrX = seatNumberClass.split(' ');
    		var arr = arrX[0];
    		var arrStatus = arrX[1];
    		if(arr == 'col1'){
    			var alpSeat = 'A';
    		} else if(arr == 'col2'){
    			var alpSeat = 'B';
    		} else if(arr == 'col3'){
    			var alpSeat = 'C';
    		} else if(arr == 'col4'){
    			var alpSeat = 'D';
    		} else if(arr == 'col5'){
    			var alpSeat = 'E';
    		}
    		var seatNumberCol = $(this).parent().parent().attr('class');
    		var colArr = seatNumberCol.split(' ');
    		var colArrNum = colArr[1].split('w');
    		var colSeat = colArrNum[1];
    		if(arrStatus == null){
    			$('.checkSeatName').html(colSeat + ' ' + alpSeat);
    			$('.trainLayout').find('.active').removeClass('active');
    			$(this).addClass('active');
    			seatNumber = colSeat + alpSeat;
    			rowNumber = colArr[1];
    			colNumber = arr;	    			
    		} else if(arrStatus == 'active'){
    			$('.checkSeatName').html(colSeat + ' ' + alpSeat);
    			seatNumber = colSeat + alpSeat;
    		} else if(arrStatus == 'filled'){
    			alert('Sorry, this seat already taken.')
    		}
    		
    	});    	
    	
//    	$($('#' + wagon + ' #activeSeat').html()).click();
    	$(activeElement).click();
    	$('.masking, .getTrainLayout').fadeIn();
    });
    
    $('.chooseSeat').click(function(){
    	$('#' + idSeat).html(seatNumber);
    	$('#' + wagon + ' #activeSeat').html('.' + rowNumber + ' .' + colNumber);
    	var strSeat = '';
    	$('span[id^="seat"]').each(function() {
    		strSeat = strSeat + ',' + $(this).html();
    	});
    	strSeat = strSeat.substring(1);
    	$('input[name="selectedSeat"]').val(strSeat);
        $('.masking, .getTrainLayout').fadeOut();
    });      
    
    $('.masking, .cancelChooseSeat').click(function(){
        $('.masking, .getTrainLayout').fadeOut();
    });      
    
    $('#wagon').change(function(){
    	$('#wagonName').html($("#wagon option:selected").text());
    });
});