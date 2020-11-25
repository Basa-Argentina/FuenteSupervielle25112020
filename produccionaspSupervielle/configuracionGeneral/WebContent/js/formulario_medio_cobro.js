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
	
	
	$("#idTipoMedioPago").change
    (
      function()
      {
        var selectedValue = $(this).val();
        if(selectedValue == "2" || selectedValue == "3")
        {
        	$("#fechaVencimiento").show();
            $("#titular").show();	
            $("#banco").hide();
          
        }
        if(selectedValue == "2")
        {
          $("#numero").show();
          $("#banco").show();
        }
        if(selectedValue == "-1" || selectedValue == "1")
        {
        	$("#banco").hide();
        	$("#numero").hide();
        	$("#titular").hide();
        	$("#fechaVencimiento").hide();
        }
      }   
    );
    $("#idTipoMedioPago").trigger('change');
	
});


