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
			seatPerRow = totalSeat,
			drawLayout = '<ul>';
			for (i=1; i<=seatRow; i++){
				drawLayout += '<li class="colLayout col' + i + '"><ul>';
				for (j=1; j<=seatPerRow; j++){
					drawLayout += '<li class="seat' + j + '"><span></span></li>';
				}
				drawLayout += '</ul></li>';
			}
			drawLayout += '</ul>';
		$('.layout4').append(drawLayout);
		$('.layout4 li').find('ul li:eq( 0 )').css('margin-bottom', '1px');
		$('.layout4 li.colLayout').css('margin-left', '2px').find('li span').addClass('seatLeft');
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
	
	//$('.col1 .seat1').removeClass('active');
	
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
		}else if(arr == 'seat9'){
			var alpSeat = 'I';
		}else if(arr == 'seat10'){
			var alpSeat = 'J';
		}else if(arr == 'seat11'){
			var alpSeat = 'K';
		}else if(arr == 'seat12'){
			var alpSeat = 'L';
		}else if(arr == 'seat13'){
			var alpSeat = 'M';
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
	        else if (seatNumber ==  "3A") { numberSeat ='3'; nomorSeat+='03'+','; }
	        else if (seatNumber ==  "4A") { numberSeat ='4'; nomorSeat+='04'+','; }
	        else if (seatNumber ==  "1B") { numberSeat ='5'; nomorSeat+='05'+','; }
	        else if (seatNumber ==  "2B") { numberSeat ='6'; nomorSeat+='06'+',';}
	        else if (seatNumber ==  "3B") { numberSeat ='7'; nomorSeat+='07'+','; }
	        else if (seatNumber ==  "4B") { numberSeat='8'; nomorSeat+='08'+','; }
	        else if (seatNumber ==  "1C") { numberSeat='9'; nomorSeat+='09'+',';  }
	        else if (seatNumber ==  "2C") { numberSeat='10'; nomorSeat+='10'+','; }
	        
	        else if (seatNumber ==  "3C") { numberSeat='11'; nomorSeat+='11'+',';}
	        else if (seatNumber ==  "4C") { numberSeat='12'; nomorSeat+='12'+',';}
	        else if (seatNumber ==  "1D") { numberSeat='13'; nomorSeat+='13'+',';}
	        else if (seatNumber ==  "2D") { numberSeat='14'; nomorSeat+='14'+','; }
	        else if (seatNumber ==  "3D") { numberSeat='15'; nomorSeat+='15'+','; }
	        else if (seatNumber ==  "4D") { numberSeat+='16'; nomorSeat+='16'+',';}
	        else if (seatNumber ==  "1E") { numberSeat+='17'; nomorSeat+='17'+',';}
	        else if (seatNumber ==  "2E") { numberSeat='18'; nomorSeat+='18'+',';}
	        else if (seatNumber ==  "3E") { numberSeat='19'; nomorSeat+='19'+',';  }
	        else if (seatNumber ==  "4E") { numberSeat='20'; nomorSeat+='20'+',';}
	        
	        else if (seatNumber ==  "1F") { numberSeat='21'; nomorSeat+='21'+',';}
	        else if (seatNumber ==  "2F") { numberSeat='22'; nomorSeat+='22'+','; }
	        else if (seatNumber ==  "3F") { numberSeat='23'; nomorSeat+='23'+','; }
	        else if (seatNumber ==  "4F") { numberSeat='24'; nomorSeat+='24'+','; }
	        else if (seatNumber ==  "1G") { numberSeat='25'; nomorSeat+='25'+','; }
	        else if (seatNumber ==  "2G") { numberSeat='26'; nomorSeat+='26'+',';}
	        else if (seatNumber ==  "3G") { numberSeat='27'; nomorSeat+='27'+',';}
	        else if (seatNumber ==  "4G") { numberSeat='28'; nomorSeat+='28'+',';}
	        else if (seatNumber ==  "1H") { numberSeat='29'; nomorSeat+='29'+','; }
	        else if (seatNumber ==  "2H") { numberSeat='30'; nomorSeat+='30'+',';}
	        
	        else if (seatNumber ==  "3H") { numberSeat='31'; nomorSeat+='31'+','; }
	        else if (seatNumber ==  "4H") { numberSeat='32'; nomorSeat+='32'+',';}
			
	        else if (seatNumber ==  "1I") { numberSeat='33'; nomorSeat+='33'+','; }
	        else if (seatNumber ==  "2I") { numberSeat='34'; nomorSeat+='34'+',';}
	        else if (seatNumber ==  "3I") { numberSeat='35'; nomorSeat+='35'+','; }
	        else if (seatNumber ==  "4I") { numberSeat='36'; nomorSeat+='36'+','; }
			
	        else if (seatNumber ==  "1J") { numberSeat='37'; nomorSeat+='37'+',';}
	        else if (seatNumber ==  "2J") { numberSeat='38'; nomorSeat+='38'+','; }
	        else if (seatNumber ==  "3J") { numberSeat='39'; nomorSeat+='39'+','; }
	        else if (seatNumber ==  "4J") { numberSeat='40'; nomorSeat+='40'+',';}
			
	        else if (seatNumber ==  "1K") { numberSeat='41'; nomorSeat+='41'+',';}
	        else if (seatNumber ==  "2K") { numberSeat='42'; nomorSeat+='42'+',';}
	        else if (seatNumber ==  "3K") { numberSeat='43'; nomorSeat+='43'+','; }
	        else if (seatNumber ==  "4K") { numberSeat='44'; nomorSeat+='44'+',';}
			
	        else if (seatNumber ==  "1L") { numberSeat='45'; nomorSeat+='45'+',';}
	        else if (seatNumber ==  "2L") { numberSeat='46'; nomorSeat+='46'+','; }
	        else if (seatNumber ==  "3L") { numberSeat='47'; nomorSeat+='47'+',';}
	        else if (seatNumber ==  "4L") { numberSeat='48'; nomorSeat+='48'+','; }
			
	        else if (seatNumber ==  "1M") { numberSeat='49'; nomorSeat+='49'+',';}
	        else if (seatNumber ==  "2M") { numberSeat='50'; nomorSeat+='50'+','; }
	        else if (seatNumber ==  "3M") { numberSeat='51'; nomorSeat+='51'+','; }
	        else if (seatNumber ==  "4M") { numberSeat='52'; nomorSeat+='52'+','; }
			
			nomor = nomorSeat.substring(0, nomorSeat.length - 1);
			
			if(nomor.length > 5){
				alert('Maaf, Pemesanan Kursi Maksimal 2');
			}else{
				//$('.trainLayout').find('.active').removeClass('active');
				$('.checkSeatName').html(nomor);
				$(this).addClass('active');
			}
			   
		} else if(arrStatus == 'active'){
			
			seatNumber = colSeat + alpSeat;
			rowNumber = colArr[1];
			colNumber = arr;
//			$('.'+rowNumber +' .'+colNumber).removeClass('active');
//			
//			if (seatNumber ==  "1A") { numberSeat = '1'; nomorSeat=''+','; }
//	    	else if (seatNumber ==  "2A") { numberSeat ='2'; nomorSeat=''+','; }
//	        else if (seatNumber ==  "3A") { numberSeat ='3'; nomorSeat=''+','; }
//	        else if (seatNumber ==  "4A") { numberSeat ='4'; nomorSeat=''+','; }
//	        else if (seatNumber ==  "1B") { numberSeat ='5'; nomorSeat-='05'+','; }
//	        else if (seatNumber ==  "2B") { numberSeat ='6'; nomorSeat-='06'+',';}
//	        else if (seatNumber ==  "3B") { numberSeat ='7'; nomorSeat-='07'+','; }
//	        else if (seatNumber ==  "4B") { numberSeat='8'; nomorSeat-='08'+','; }
//	        else if (seatNumber ==  "1C") { numberSeat='9'; nomorSeat-='09'+',';  }
//	        else if (seatNumber ==  "2C") { numberSeat='10'; nomorSeat-='10'+','; }
//	        
//	        else if (seatNumber ==  "3C") { numberSeat='11'; nomorSeat-='11'+',';}
//	        else if (seatNumber ==  "4C") { numberSeat='12'; nomorSeat-='12'+',';}
//	        else if (seatNumber ==  "1D") { numberSeat='13'; nomorSeat-='13'+',';}
//	        else if (seatNumber ==  "2D") { numberSeat='14'; nomorSeat-='14'+','; }
//	        else if (seatNumber ==  "3D") { numberSeat='15'; nomorSeat-='15'+','; }
//	        else if (seatNumber ==  "4D") { numberSeat+='16'; nomorSeat-='16'+',';}
//	        else if (seatNumber ==  "1E") { numberSeat+='17'; nomorSeat-='17'+',';}
//	        else if (seatNumber ==  "2E") { numberSeat='18'; nomorSeat-='18'+',';}
//	        else if (seatNumber ==  "3E") { numberSeat='19'; nomorSeat-='19'+',';  }
//	        else if (seatNumber ==  "4E") { numberSeat='20'; nomorSeat-='20'+',';}
//	        
//	        else if (seatNumber ==  "1F") { numberSeat='21'; nomorSeat-='21'+',';}
//	        else if (seatNumber ==  "2F") { numberSeat='22'; nomorSeat-='22'+','; }
//	        else if (seatNumber ==  "3F") { numberSeat='23'; nomorSeat-='23'+','; }
//	        else if (seatNumber ==  "4F") { numberSeat='24'; nomorSeat-='24'+','; }
//	        else if (seatNumber ==  "1G") { numberSeat='25'; nomorSeat-='25'+','; }
//	        else if (seatNumber ==  "2G") { numberSeat='26'; nomorSeat-='26'+',';}
//	        else if (seatNumber ==  "3G") { numberSeat='27'; nomorSeat-='27'+',';}
//	        else if (seatNumber ==  "4G") { numberSeat='28'; nomorSeat-='28'+',';}
//	        else if (seatNumber ==  "1H") { numberSeat='29'; nomorSeat-='29'+','; }
//	        else if (seatNumber ==  "2H") { numberSeat='30'; nomorSeat-='30'+',';}
//	        
//	        else if (seatNumber ==  "3H") { numberSeat='31'; nomorSeat-='31'+','; }
//	        else if (seatNumber ==  "4H") { numberSeat='32'; nomorSeat-='32'+',';}
//			
//	        else if (seatNumber ==  "1I") { numberSeat='33'; nomorSeat-='33'+','; }
//	        else if (seatNumber ==  "2I") { numberSeat='34'; nomorSeat-='34'+',';}
//	        else if (seatNumber ==  "3I") { numberSeat='35'; nomorSeat-='35'+','; }
//	        else if (seatNumber ==  "4I") { numberSeat='36'; nomorSeat-='36'+','; }
//		
//	        else if (seatNumber ==  "1J") { numberSeat='37'; nomorSeat-='37'+',';}
//	        else if (seatNumber ==  "2J") { numberSeat='38'; nomorSeat-='38'+','; }
//	        else if (seatNumber ==  "3J") { numberSeat='39'; nomorSeat-='39'+','; }
//	        else if (seatNumber ==  "4J") { numberSeat='40'; nomorSeat-='40'+',';}
//			
//	        else if (seatNumber ==  "1K") { numberSeat='41'; nomorSeat-='41'+',';}
//	        else if (seatNumber ==  "2K") { numberSeat='42'; nomorSeat-='42'+',';}
//	        else if (seatNumber ==  "3K") { numberSeat='43'; nomorSeat-='43'+','; }
//	        else if (seatNumber ==  "4K") { numberSeat='44'; nomorSeat-='44'+',';}
//			
//	        else if (seatNumber ==  "1L") { numberSeat='45'; nomorSeat-='45'+',';}
//	        else if (seatNumber ==  "2L") { numberSeat='46'; nomorSeat-='46'+','; }
//	        else if (seatNumber ==  "3L") { numberSeat='47'; nomorSeat-='47'+',';}
//	        else if (seatNumber ==  "4L") { numberSeat='48'; nomorSeat-='48'+','; }
//			
//	        else if (seatNumber ==  "1M") { numberSeat='49'; nomorSeat-='49'+',';}
//	        else if (seatNumber ==  "2M") { numberSeat='50'; nomorSeat-='50'+','; }
//	        else if (seatNumber ==  "3M") { numberSeat='51'; nomorSeat-='51'+','; }
//	        else if (seatNumber ==  "4M") { numberSeat='52'; nomorSeat-='52'+','; }
//			
			//var nomor = nomorSeat.substring(0, nomorSeat.length - 1);
			//$('.checkSeatName').html(nomor);
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