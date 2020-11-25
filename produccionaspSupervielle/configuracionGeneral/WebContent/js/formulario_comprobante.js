$(document).ready(function(){
  $(".inputTextNumericPositiveIntegerOnly").numeric({ decimal: false, negative: false});
  $(".numerico").numeric(); 	
  
  $(window).unload(function(){
		parent.$.fancybox.close();
	});
	$("#cancelar").click(function(){
		parent.$.fancybox.close();
	});    
    
	$('#guardar').click(function(){
		$(window).unbind("unload");
		document.forms[0].submit();
	});
});





