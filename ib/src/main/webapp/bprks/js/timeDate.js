(function($) {
 
  $.fn.jclock = function(options) {
    var version = '2.3.2';
	
    var opts = $.extend({}, $.fn.jclock.defaults, options);
         
    return this.each(function() {
      $this = $(this);
      $this.timerID = null;
      $this.running = false;
 
      $this.increment = 0;
      $this.lastCalled = new Date().getTime();
 
      var o = $.meta ? $.extend({}, opts, $this.data()) : opts;
 
      $this.format = o.format;
      $this.utc = o.utc;
      $this.utcOffset = (o.utc_offset != null) ? o.utc_offset : o.utcOffset;
      $this.seedTime = o.seedTime;
      $this.timeout = o.timeout;
 
      $this.css({
        fontFamily: o.fontFamily,
        fontSize: o.fontSize,
        backgroundColor: o.background,
        color: o.foreground
      });
 
      // %A
      $this.daysFullNames = new Array(7);
      $this.daysFullNames[0] = "Minggu";
      $this.daysFullNames[1] = "Senin";
      $this.daysFullNames[2] = "Selasa";
      $this.daysFullNames[3] = "Rabu";
      $this.daysFullNames[4] = "Kamis";
      $this.daysFullNames[5] = "Jum'at";
      $this.daysFullNames[6] = "Sabtu";
 
      // %B
      $this.monthsFullNames = new Array(12);
      $this.monthsFullNames[0] = "Januari";
      $this.monthsFullNames[1] = "Februari";
      $this.monthsFullNames[2] = "Maret";
      $this.monthsFullNames[3] = "April";
      $this.monthsFullNames[4] = "Mei";
      $this.monthsFullNames[5] = "Juni";
      $this.monthsFullNames[6] = "Juli";
      $this.monthsFullNames[7] = "Augustus";
      $this.monthsFullNames[8] = "September";
      $this.monthsFullNames[9] = "October";
      $this.monthsFullNames[10] = "November";
      $this.monthsFullNames[11] = "December";
 
      $.fn.jclock.startClock($this);
 
    });
  };
       
  $.fn.jclock.startClock = function(el) {
    $.fn.jclock.stopClock(el);
    $.fn.jclock.displayTime(el);
  }
 
  $.fn.jclock.stopClock = function(el) {
    if(el.running) {
      clearTimeout(el.timerID);
    }
    el.running = false;
  }
 
  $.fn.jclock.displayTime = function(el) {
    var time = $.fn.jclock.currentTime(el);
    var formatted_time = $.fn.jclock.formatTime(time, el);
    el.attr('currentTime', time.getTime())
    el.html(formatted_time);
    el.timerID = setTimeout(function(){$.fn.jclock.displayTime(el)},el.timeout);
  }

  $.fn.jclock.currentTime = function(el) {
    if(typeof(el.seedTime) == 'undefined') {
      var now = new Date();
    } else {
      el.increment += new Date().getTime() - el.lastCalled;
      var now = new Date(el.seedTime + el.increment);
      el.lastCalled = new Date().getTime();
    }
    return now
  }
  $.fn.jclock.formatTime = function(time, el) {
    var timeNow = "";
    var i = 0;
    var index = 0;
    while ((index = el.format.indexOf("%", i)) != -1) {
      timeNow += el.format.substring(i, index);
      index++;
      var property = $.fn.jclock.getProperty(time, el, el.format.charAt(index));
      index++;
      timeNow += property;
      i = index
    }
    timeNow += el.format.substring(i);
    return timeNow;
  };
 
  $.fn.jclock.getProperty = function(dateObject, el, property) {
    switch (property) {
      case "A":
          return (el.daysFullNames[dateObject.getDay()]);
      case "B":
          return (el.monthsFullNames[dateObject.getMonth()]);
      case "D":
          return ((dateObject.getDate() < 10) ? "0" : "") + dateObject.getDate();
      case "H":
          return ((dateObject.getHours() < 10) ? "0" : "") + dateObject.getHours();
      case "M":
          return ((dateObject.getMinutes() < 10) ? "0" : "") + dateObject.getMinutes();
      case "S":
          return ((dateObject.getSeconds() < 10) ? "0" : "") + dateObject.getSeconds();
      case "Y":
          return (dateObject.getFullYear());
      case "%":
          return "%";
    }
 
  }
       
  $.fn.jclock.defaults = {
    format: '%H:%M:%S',
    utcOffset: 0,
    utc: false,
    fontFamily: '',
    fontSize: '',
    foreground: '',
    background: '',
    seedTime: undefined,
    timeout: 1000
  };
 
})(jQuery);
