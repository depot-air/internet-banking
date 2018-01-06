$(document).ready(function(){
	var findLayout = $('.trainLayout').find('div').attr('class');
    
	//Drawing Layout 1 ( 5 kursi dengan format 3 : 2 posisi berhadapan )
	if(findLayout == 'layout1'){
		var seatRow = 32,
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
		$('.layout1').append(drawLayout);
		$('.layout1 li').find('ul li:eq( 2 )').css('margin-bottom', '20px');
		$('.layout1 li.colLayout:even').css('margin-right', '20px').find('li span').addClass('seatRight');
		$('.layout1 li.colLayout:odd').find('li span').addClass('seatLeft');
	};
	
	//Drawing Layout 2 ( 5 kursi dengan format 3 : 2 posisi sejajar )
	if(findLayout == 'layout2'){
		var seatRow = 8,
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
		$('.layout3 li').find('ul li:eq( 1 )').css('margin-bottom', '49px');
		$('.layout3 li.colLayout:even').css('margin-right', '20px').find('li span').addClass('seatRight');
		$('.layout3 li.colLayout:odd').find('li span').addClass('seatLeft');
	}
	
	//Drawing Layout 3 ( 4 kursi dengan format 2 : 2 posisi sejajar )
	if(findLayout == 'layout4'){
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
		$('.layout4').append(drawLayout);
		$('.layout4 li').find('ul li:eq( 1 )').css('margin-bottom', '49px');
		$('.layout4 li.colLayout').css('margin-left', '10px').find('li span').addClass('seatLeft');
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
    if(findLayout == 'layout9'){
		var seatRow = 8,
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
		$('.layout2').append(drawLayout);
		$('.layout2 li').find('ul li:eq( 2 )').css('margin-bottom', '20px');
		$('.layout2 li.colLayout').css('margin-left', '10px').find('li span').addClass('seatLeft');
	};
	
	
	$('.col1 .seat4, .col1 .seat5, .col2 .seat1, .col2 .seat2, .col2 .seat3, .col2 .seat4, .col2 .seat5').addClass('filled');
	$('.col1 .seat1').addClass('active');
	
	
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
		}
		var seatNumberCol = $(this).parent().parent().attr('class');
		var colArr = seatNumberCol.split(' ');
		var colArrNum = colArr[1].split('l');
		var colSeat = colArrNum[1];
		if(arrStatus == null){
			$('.checkSeatName').html(alpSeat + ' ' + colSeat);
			$('.trainLayout').find('.active').removeClass('active');
			$(this).addClass('active');
		} else if(arrStatus == 'active'){
			$('.checkSeatName').html(alpSeat + ' ' + colSeat);
		} else {
			alert('Sorry, this seat already taken.')
		}
	})
	
	$('.col1 .active').click();
});