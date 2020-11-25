$(document).ready(function(){
  $('.tooltip-wrapper').tooltip();
});

(function($){
  var opts;

  $.fn.tooltip = function(options){
    opts = $.extend({}, $.fn.tooltip.defaults, options);

    $(this).css('position', 'relative');
    $('.tooltip', this).css({'position': 'absolute'});

    return $(this).each(function(){
      var hideDelayTimer = null;
      var visible = false;
      var animating = false;

      var trigger = $('.tooltip-trigger', this);
      var tooltip = $('.tooltip', this).css({'opacity': 0});

      $([trigger.get(0), tooltip.get(0)]).mouseover(function(){
        if (hideDelayTimer) clearTimeout(hideDelayTimer);

        if(visible || animating){
          return;
        }else{
          animating = true;

          tooltip.css({
            top:-50,
            display: 'block'
          }).animate({
            top: '-=' + opts.distance + 'px',
            opacity: 0.90
          }, opts.time, 'swing', function(){
            animating = false;
            visible = true;
          });
        }
      }).mouseout(function(){
        if (hideDelayTimer) clearTimeout(hideDelayTimer);

        hideDelayTimer = setTimeout(function(){
          hideDelayTimer = null;
          tooltip.animate({
            top: '-=' + opts.distance + 'px',
            opacity: 0
          }, opts.time, 'swing', function(){
            visible = false;
            tooltip.css('display', 'none');
          });
        }, opts.hideDelay);
      });

    })

  };

  // Defaults
  $.fn.tooltip.defaults = {
    distance: 10, // Distancia que recorre el tooltip hasta aparecer o desaparecer
    time: 250, // Tiempo que tarda el tooltip en aparecer o desaparecer
    hideDelay: 250 // Tiempo que tarda el tooltip en comenzar a desaparecer una vez hemos
  };
})(jQuery);