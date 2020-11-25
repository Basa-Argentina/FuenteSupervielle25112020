var menuAbierto=true;

$(document).ready(function(){
	
	parent.$.fancybox.hideActivity();	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	$(".inputTextNumericPositiveIntegerOnly").numeric({ decimal: false, negative: false});
	
	//Validaciones sobre formato de codigos
	$('.completarZeros').blur(function(e){
		valorSinZeros='';
		valorSinZeros=$(this).val();
		zerosIzq='';
		if(valorSinZeros!=null && valorSinZeros!=''){
			maximo=$(this).attr('maxlength');
			actual=valorSinZeros.length;
			for(i=0;i<(maximo-actual);i++){
				zerosIzq+='0';
			}
		}
		$(this).val(zerosIzq+valorSinZeros);		
	});
	
	
	
	$('#botonCancelar').click(function(){
	    var accion = parent.document.getElementById("accion").value;
	    
		window.parent.location.href = 'precargaFormularioLoteFacturacion.html?accion='+accion; 
		parent.$.fancybox.close();
		//parent.content.location = 'precargaFormularioFactura.html';
		//window.location='precargaFormularioFactura.html';
	});
	
	//Checkbox
	$("input[name=checktodos]").change(function(){
		$('input[type=checkbox]').each( function() {			
			if($("input[name=checktodos]:checked").length == 1){
				this.checked = true;
			} else {
				this.checked = false;
			}
		});
	});
	
	$('#agregar').click(function(){
		
		 	var allVals = [];
		    $("input[class='checklote']:checked").each(function() {
		    	allVals.push($(this).val());
		    });
		    if(allVals.length == 0)
		    {
		    	jAlert('Debe seleccionar al menos un concepto para asociar.');
				return;
			}
		    else
			{
		    	document.location='guardarActualizarLoteFacturacionDetalle.html?&seleccionados='+allVals
		    	+'&estado='+$('#estado').val()+'&descripcion='+$('#descripcion').val()+'&periodo='+$('#periodo').val()
		    	+'&fechaRegistro='+$('#fechaRegistro').val()+'&fechaFacturacion='+$('#fechaFacturacion').val();
		    }
	});
	
	
	
});

function popupOnDivInsideFancybox(divOpen){
	var left=($(window).width()-$(divOpen).width())/2;
	var top = ($(window).height()-$(divOpen).height())/2;
	$(divOpen).css("left",left);
	$(divOpen).css("top",top);
	$(divOpen).show("slow");
}

