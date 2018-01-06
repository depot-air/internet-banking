jQuery(document).ready(function ($) {
    'use strict';
var departQR = $('.departIDetail .fCCode').html(),
    returnQR = $('.returnIDetail .fCCode').html(),
    findLayout = $('.trainLayout').find('div').attr('class'),
	totalSeat = $('.trainLayout').find('div').attr('totalSeat');

var idSeat = ''; 
var seatNumber = '';
var rowNumber = '';
var colNumber = '';
var numberSeat = '';
var nomorSeat = '';
var nomor;
var nomorValid;
var status='0';
    
	//Drawing Layout 1 ( 5 kursi dengan format 3 : 2 posisi berhadapan )
	if(findLayout == 'layout1'){
		var seatRow = 24,
			seatPerRow = 5,
            i = 0,
            j = 0,
			drawLayout = '<ul>';
			for (i=1; i<=seatRow; i++){
				drawLayout += '<li class="colLayout col' + i + '"><ul>';
				for (j=1; j<=seatPerRow; j++){
					drawLayout += '<li class="seat' + j + '"><span></span></li>';
				}
				drawLayout += '</ul></li>';
			}
			drawLayout += '</ul>';
		$('.layout1').append(drawLayout);
		$('.layout1 li').find('ul li:eq( 2 )').css('margin-bottom', '20px');
		$('.layout1 li.colLayout:even').css('margin-right', '20px').find('li span').addClass('seatRight');
		$('.layout1 li.colLayout:odd').find('li span').addClass('seatLeft');
	};
	
	//Drawing Layout 2 ( 5 kursi dengan format 3 : 2 posisi sejajar )
	if(findLayout == 'layout2'){
		var seatRow = 24,
			seatPerRow = 5,
			drawLayout = '<ul>';
			for (i=1; i<=seatRow; i++){
				drawLayout += '<li class="colLayout col' + i + '"><ul>';
				for (j=1; j<=seatPerRow; j++){
					drawLayout += '<li class="seat' + j + '"><span></span></li>';
				}
				drawLayout += '</ul></li>';
			}
			drawLayout += '</ul>';
		$('.layout2').append(drawLayout);
		$('.layout2 li').find('ul li:eq( 2 )').css('margin-bottom', '20px');
		$('.layout2 li.colLayout').css('margin-left', '10px').find('li span').addClass('seatLeft');
	};
	
	//Drawing Layout 3 ( 4 kursi dengan format 2 : 2 posisi berhadapan )
	if(findLayout == 'layout3'){
		var seatRow = 24,
			seatPerRow = 4,
			drawLayout = '<ul>';
			for (i=1; i<=seatRow; i++){
				drawLayout += '<li class="colLayout col' + i + '"><ul>';
				for (j=1; j<=seatPerRow; j++){
					drawLayout += '<li class="seat' + j + '"><span></span></li>';
				}
				drawLayout += '</ul></li>';
			}
			drawLayout += '</ul>';
		$('.layout3').append(drawLayout);
		$('.layout3 li').find('ul li:eq( 2 )').css('margin-bottom', '49px');
		$('.layout3 li.colLayout:even').css('margin-right', '20px').find('li span').addClass('seatRight');
		$('.layout3 li.colLayout:odd').find('li span').addClass('seatLeft');
	}
	
	//Drawing Layout 3 ( 4 kursi dengan format 2 : 2 posisi sejajar )
	if(findLayout == 'layout4'){
		var seatRow = 4,
			seatPerRow = 3,
			drawLayout = '<ul>';
            drawLayout += '<li class="colLayout col1"><ul><li class="seat1"><span></span></li></ul></li>';
			for (i=2; i<=seatRow; i++){
				drawLayout += '<li class="colLayout col' + i + '"><ul>';
				for (j=1; j<=seatPerRow; j++){
					drawLayout += '<li class="seat' + j + '"><span></span></li>';
				}
				drawLayout += '</ul></li>';
			}
			drawLayout += '</ul>';
		$('.layout4').append(drawLayout);
        $('.layout4 li:first').css('width', '100%');
		$('.layout4 li').find('ul li:eq( 0 )').css('margin-bottom', '1px');
		$('.layout4 li.colLayout').css('margin-left', '12px').find('li span').addClass('seatLeft');
	}
	
	//Drawing Layout 5 ( 5 kursi dengan format 5 posisi berhadapan )
	if(findLayout == 'layout5'){
		var seatRow = 24,
			seatPerRow = 5,
			drawLayout = '<ul>';
			for (i=1; i<=seatRow; i++){
				drawLayout += '<li class="colLayout col' + i + '"><ul>';
				for (j=1; j<=seatPerRow; j++){
					drawLayout += '<li class="seat' + j + '"><span></span></li>';
				}
				drawLayout += '</ul></li>';
			}
			drawLayout += '</ul>';
		$('.layout5').append(drawLayout);
		$('.layout5 li.colLayout:even').css('margin-right', '20px').find('li span').addClass('seatRight');
		$('.layout5 li.colLayout:odd').find('li span').addClass('seatLeft');
	}
	
	//Drawing Layout 6 ( 5 kursi dengan format 5 posisi berhadapan )
	if(findLayout == 'layout6'){
		var seatRow = 24,
			seatPerRow = 5,
			drawLayout = '<ul>';
			for (i=1; i<=seatRow; i++){
				drawLayout += '<li class="colLayout col' + i + '"><ul>';
				for (j=1; j<=seatPerRow; j++){
					drawLayout += '<li class="seat' + j + '"><span></span></li>';
				}
				drawLayout += '</ul></li>';
			}
			drawLayout += '</ul>';
		$('.layout6').append(drawLayout);
		$('.layout6 li.colLayout').css('margin-left', '10px').find('li span').addClass('seatLeft');
	}
	
	//Drawing Layout 7 ( 4 kursi dengan format 4 posisi berhadapan )
	if(findLayout == 'layout7'){
		var seatRow = 24,
			seatPerRow = 4,
			drawLayout = '<ul>';
			for (i=1; i<=seatRow; i++){
				drawLayout += '<li class="colLayout col' + i + '"><ul>';
				for (j=1; j<=seatPerRow; j++){
					drawLayout += '<li class="seat' + j + '"><span></span></li>';
				}
				drawLayout += '</ul></li>';
			}
			drawLayout += '</ul>';
		$('.layout7').append(drawLayout);
		$('.layout7 li.colLayout:even').css('margin-right', '20px').find('li span').addClass('seatRight');
		$('.layout7 li.colLayout:odd').find('li span').addClass('seatLeft');
	}
	
	//Drawing Layout 8 ( 4 kursi dengan format 4 posisi sejajar)
	if(findLayout == 'layout8'){
		var seatRow = 24,
			seatPerRow = 4,
			drawLayout = '<ul>';
			for (i=1; i<=seatRow; i++){
				drawLayout += '<li class="colLayout col' + i + '"><ul>';
				for (j=1; j<=seatPerRow; j++){
					drawLayout += '<li class="seat' + j + '"></li>';
				}
				drawLayout += '</ul></li>';
			}
			drawLayout += '</ul>';
		$('.layout8').append(drawLayout);
		$('.layout8 li.colLayout').css('margin-left', '10px').find('li span').addClass('seatLeft');
	}
    
    
    //Drawing Layout 3 ( 4 kursi dengan format 2 : 2 posisi sejajar )
	if(findLayout == 'layout9'){
		var seatCol = 4,
			seatPerCol = totalSeat,
			drawLayout = '<ul>';
            drawLayout += '<li class="colLayout col1"><ul><li class="seat1"><span></span></li></ul></li>';
			for (i=2; i<=seatCol; i++){
				drawLayout += '<li class="colLayout col' + i + '"><ul>';
				for (j=1; j<=seatPerCol; j++){
					drawLayout += '<li class="seat' + j + '"><span></span></li>';
				}
				drawLayout += '</ul></li>';
			}
			drawLayout += '</ul>';
		$('.layout9').append(drawLayout);
        $('.layout9 li:first').css('width', '100%');
		$('.layout9 li').find('ul li:eq( 0 )').css('margin-bottom', '1px');
		$('.layout9 li.colLayout').css('margin-left', '12px').find('li span').addClass('seatLeft');
	}
    
    
	//$('.col1 .seat4, .col1 .seat5, .col2 .seat1, .col2 .seat2, .col2 .seat3, .col2 .seat7, .col2 .seat9').addClass('filled');
	//$('.col1 .seat1').addClass('active');
	
	
	$('.trainLayout .colLayout li').click(function(){
		var seatNumberClass = $(this).attr('class');
		var arrX = seatNumberClass.split(' ');
		var arr = arrX[0];
		var arrStatus = arrX[1];
		if(arr == 'seat1'){
			var alpSeat = 'A';
		} else if(arr == 'seat2'){
			var alpSeat = 'B';
		} else if(arr == 'seat3'){
			var alpSeat = 'C';
		} else if(arr == 'seat4'){
			var alpSeat = 'D';
		} else if(arr == 'seat5'){
			var alpSeat = 'E';
		}else if(arr == 'seat6'){
			var alpSeat = 'F';
		}else if(arr == 'seat7'){
			var alpSeat = 'G';
		}else if(arr == 'seat8'){
			var alpSeat = 'H';
		}
		var seatNumberCol = $(this).parent().parent().attr('class');
		var colArr = seatNumberCol.split(' ');
		var colArrNum = colArr[1].split('l');
		var colSeat = colArrNum[1];
		if(arrStatus == null){
			
			seatNumber = colSeat + alpSeat;
			rowNumber = colArr[1];
			colNumber = arr;	
			
			if (seatNumber ==  "1A") { numberSeat = '1'; nomorSeat+='01'+','; }
	    	else if (seatNumber ==  "2A") { numberSeat ='2'; nomorSeat+='02'+','; }
	    	else if (seatNumber ==  "2B") { numberSeat ='3'; nomorSeat+='03'+',';}
	    	else if (seatNumber ==  "2C") { numberSeat='4'; nomorSeat+='04'+','; }
			
	        else if (seatNumber ==  "3A") { numberSeat ='5'; nomorSeat+='05'+','; }
	        else if (seatNumber ==  "3B") { numberSeat ='6'; nomorSeat+='06'+','; }
	        else if (seatNumber ==  "3C") { numberSeat='7'; nomorSeat+='07'+',';  }
			
	        else if (seatNumber ==  "4A") { numberSeat ='8'; nomorSeat+='08'+','; }
	        else if (seatNumber ==  "4B") { numberSeat ='9'; nomorSeat+='09'+','; }
	        else if (seatNumber ==  "4C") { numberSeat='10'; nomorSeat+='10'+','; }
	        
			nomor = nomorSeat.substring(0, nomorSeat.length - 1);
			
			if(nomor.length > 5){
				alert('Maaf, Pemesanan Kursi Maksimal 2');
			}else{
				//$('.trainLayout').find('.active').removeClass('active');
				$('.checkSeatName').html(nomor);
				$(this).addClass('active');
			}
			
		} else if(arrStatus == 'active'){
			//$('.checkSeatName').html(alpSeat + ' ' + colSeat);
			seatNumber = colSeat + alpSeat;
			rowNumber = colArr[1];
			colNumber = arr;
			
			arrStatus = null
			seatNumber = colSeat + alpSeat;
		} else {
			alert('Maaf, kursi sudah ada yang pesan.')
		}
	})
	
	$('.col1 .active').click();
    
	$('.changeSeat').click(function(){
    	
    	idSeat = $(this).attr('id').substring('btnChangeSeat'.length);
		$(idSeat).addClass('filled');
    	
        $('.masking, .getTrainLayout').fadeIn();
    });
    
    $('.masking, .chooseSeat').click(function(){
    	
    	document.getElementById('selectNomorKursi').value=nomor.substring(0, 5);
        $('.masking, .getTrainLayout').fadeOut();
    });
    
    
    $('.masking, .cancelChooseSeat').click(function(){
    	document.getElementById('selectNomorKursi').value='';
    	$('.checkSeatName').html('');
    	nomorSeat = '';
    	//$('.'+rowNumber +' .'+colNumber).removeClass('active');
    	$('.trainLayout').find('.active').removeClass('active');
        $('.masking, .getTrainLayout').fadeOut();
    }); 
    
    
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