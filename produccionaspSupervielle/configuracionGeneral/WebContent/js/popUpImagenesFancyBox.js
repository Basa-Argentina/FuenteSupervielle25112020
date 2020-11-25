//var zoom = 70; //Por defecto arranca en 70%

$(document).ready(function() {
	
		
    	$("#nombreArchivoDigital").val($(this).find('#hdn_nombreArchivoDigital').val());
	     $("#pathArchivoDigital").val($(this).find('#hdn_pathArchivoDigital').val());
	    
	     if(($(this).find('#hdn_pathArchivoDigital').val().match(/.tif$/)) || ($(this).find('#hdn_pathArchivoDigital').val().match(/.TIF$/))){
		     $("#cantidadImagenes").val($(this).find('#hdn_cantidadImagenes').val());
		     cargarComboJPGDigital($(this).find('#hdn_cantidadImagenes').val());
		     if($(this).find('#hdn_cantidadImagenes').val()!=null && $(this).find('#hdn_cantidadImagenes').val()!= ''){
			     var total = parseInt($(this).find('#hdn_cantidadImagenes').val());
			     if(total > 0){
			    	 popupOnDiv($('#pop'),'darkLayer');
			    	 $("#jpgDigital").attr("src",'verImagenesRearchivo.html?fileName='+$(this).find('#hdn_pathArchivoDigital').val()+'&pos=1');
			    	 popupOffDiv($('#pop'),'darkLayer');
			     }
			 }
	     }
	     
	     if(($(this).find('#hdn_nombreArchivoDigital').val().match(/.pdf$/)) || ($(this).find('#hdn_nombreArchivoDigital').val().match(/.PDF$/))){
	    	 popupOnDiv($('#pop'),'darkLayer');
	    	 $("#jpgDigital").attr("src",'../FlexPaper/common/simple_document.jsp?doc='+$(this).find('#hdn_nombreArchivoDigital').val()+'&ruta='+$(this).find('#hdn_pathArchivoJPGDigital').val());
	    	 $('#imageDiv').slideDown();
	    	 popupOffDiv($('#pop'),'darkLayer');
	     }
     
	
	var focusEn = $("#hacerFocusEn").val();
	if(focusEn!="")
		$("#"+focusEn).focus();
	
	$("#selectCantidadJPG").change(function(){
		if($(this).val()!=null){
			popupOnDiv($('#pop'),'darkLayer');
			var fileName = $("#hdn_pathArchivoDigital").val() +'&pos='+ $(this).val();
			$("#jpgDigital").attr("src",'verImagenesRearchivo.html?fileName='+fileName); 
			popupOffDiv($('#pop'),'darkLayer');
		}
			
	});
	
	var zoom = parseInt($('#zoom').val());
	$('#jpgDigital').css({'height' : zoom +'%', 'width' : zoom +'%'});
	$('#zoomTxt').val(zoom + ' %');
	$('#zoom').val(zoom);
	
	var scrollY = $('#scrollY').val();
	var scrollX = $('#scrollX').val();
	$('#divImagen').animate({ scrollTop: scrollY }, 'slow');
	$('#divImagen').animate({ scrollLeft: scrollX }, 'slow');
	
	
	$('#divImagen').scroll(function() {
		  $('#scrollY').val($('#divImagen').scrollTop());
		  $('#scrollX').val($('#divImagen').scrollLeft());
	});
	
	
});

function refresh(){
	$("#rearchivoFormulario").attr('action',"refrescarFormRearchivo.html");
	popupOnDiv($('#pop'),'darkLayer');
	$("#rearchivoFormulario").submit();
}

function downloadFile(fileName){
	document.location="downloadFileLoteRearchivo.html?fileName="+fileName;
}

function view(fileName) {
	var iframe = parent.frames[1];
	iframe.window.location.href = 'viewFileJPGLoteRearchivo.html?fileName='+fileName;
}

function view2() {
	var iframe = parent.frames[1];
	//alert(iframe);
	jAlert(iframe);
	iframe.window.location.href = 'viewFileJPGLoteRearchivo.html?fileName=c://error.JPG';
}

function cargarComboJPGDigital(cantidad){
	if(cantidad==null || cantidad == undefined)
		return;
	if(($('#hdn_pathArchivoDigital').val().match(/.tif$/)) || ($('#hdn_pathArchivoDigital').val().match(/.TIF$/))){
		var total = parseInt(cantidad);
		var select = document.getElementById ('selectCantidadJPG');
		while (select.options.length) { 
			select.options.remove (0); 
	    } 
	
	    for (var i=1; i <= total; i++) { 
	        var option = new Option (i, i); 
	        select.options.add (option); 
	    } 
	}
    $('#imageDiv').slideDown();
}

function zoomIn(){
	var zoom = parseInt($('#zoom').val());
	if(zoom < 200){
		zoom = zoom + 10;
		$('#jpgDigital').css({'height' : zoom +'%', 'width' : zoom +'%'});
		$('#zoomTxt').val(zoom + ' %');
		$('#zoom').val(zoom);
	}
}

function zoomOut(){
	var zoom = parseInt($('#zoom').val());
	if(zoom > 10){
		zoom = zoom - 10;
		$('#jpgDigital').css({'height' : zoom +'%', 'width' : zoom +'%'});
		$('#zoomTxt').val(zoom + ' %');
		$('#zoom').val(zoom);
	}
}
