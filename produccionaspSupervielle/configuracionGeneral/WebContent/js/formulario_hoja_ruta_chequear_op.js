var menuAbierto=false;
$(document).ready(function() {
	
	//Tooltips
	$("img[title]").tooltip();
	
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	$('#busquedaDiv > table').addClass('seccion');
	
	//Slide 1
	$('#grupoDiv').attr({'style':'display:block'});
	$('#grupoImg').click(function() {
		$('#grupoDiv').slideToggle('slideUp');
		$('#grupoImgSrcDown').slideToggle('slideUp');
		$('#grupoImgSrc').slideToggle('slideUp');
	});
    
	//tabla operaciones
	$("#listaOperacion tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
	});
	$("#listaOperacion tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	$("#operacionPlanificadas tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#operacionPlanificadas tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	$("#operacionPlanificadas tbody tr").click(function(){
		$(this).siblings().removeClass('tr_selected');
		$(this).toggleClass('tr_selected');
		$('#listaElementos tbody tr').show();
		if($(this).hasClass('tr_selected')){
			var id = $(this).find('#hdn_id_operacion').val();
			$('.operacionId').each(function(){
				if($(this).val()!=id)
					$(this).closest('tr').hide();
			});
		}
	});
	
    //PopUp
    $('#operacionPlanificadas tbody tr').contextMenu('myMenu2', {
    	bindings: {
		   'eliminar': function(t) {
			   eliminar($(t).find('#hdn_id_operacion').val());
	       }
    	},
		onShowMenu: function(e, menu) {
			menuAbierto=true;
			return menu;

		},
		onHide: function(){
			menuAbierto=false;
			$("#operacionPlanificadas tbody tr").removeClass('tr_mouseover');
		}
 
    }); 
    
    //PopUp
    $('#listaOperacion tbody tr').contextMenu('myMenu1', {
    	bindings: {
		   'agregar': function(t) {
			   agregar($(t).find('#hdn_id').val());
		   }
    	},
		onShowMenu: function(e, menu) {
			menuAbierto=true;
			return menu;

		},
		onHide: function(){
			menuAbierto=false;
			$("#listaOperacion tbody tr").removeClass('tr_mouseover');
		}
 
    }); 
	
	

	
	
	$("#btnGuardar").click(function(){
		document.forms[0].submit();
	});
	$("#btnCancelar").click(function(){
		document.location="consultaHojaRuta.html";
	});
	$("#btnVolver").click(function(){
		document.location="consultaHojaRuta.html";
	});
	
	
});




//ajax
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}

//ajax
function getCodigoSerieCallBack(sResponseText) {
	
	if (sResponseText != 'null' && sResponseText != "") {
		var array = sResponseText.split('-');
		$("#codigoSerieLabel").html(array[0]);
		var numero = Number(array[1]);
		numero = numero + 1;
		$("#numeroStr").val(agregarCeros(numero, 8));
	} else {
		$("#codigoSerie").val("");
		$("#codigoSerieLabel").html("");
	}
}

function agregarCeros(numero, maximo) {
	valorSinZeros = '';
	valorSinZeros = numero + '';
	zerosIzq = '';
	actual = valorSinZeros.length;
	for (i = 0; i < (maximo - actual); i++) {
		zerosIzq += '0';
	}
	return zerosIzq + valorSinZeros + '';
}

function selectAll(box, classStyle) {
	var inTotal = document.getElementById("cantidadElementosSeleccionados");
	var total = inTotal.value;
	total=parseFloat(total);
	var chk = box.checked;
	total=0;
    $("#listaElementos").find('.'+classStyle).each( function(){
    	if(chk)
    		total+=1;
    	this.checked = chk;
    });
    inTotal.value=total;
    
    jQuery.ajax({
        url: 'marcarElemento.html?idElemento=selectAll&checkeado=false',
        success: function(data){
        	// alert(data);
        },
        data: ({"time":new Date().getTime()}),
        context: $(".selectorDiv")
    });
}

function marcarElemento(check, id){
	
	var inTotal = document.getElementById("cantidadElementosSeleccionados");
	var total = inTotal.value;
	total=parseFloat(total);
	if(check)
		total+=1;
	else
		total-=1;
	inTotal.value=total;
	
	jQuery.ajax({
        url: 'marcarElemento.html?idElemento='+id+'&checkeado='+check,
        success: function(data){
        	// alert(data);
        },
        data: ({"time":new Date().getTime()}),
        context: $(".selectorDiv")
    });
}

function scanearOperacion (event) {
    var keyCode = ('which' in event) ? event.which : event.keyCode;
    if(keyCode=='13'){
    	var txt = document.getElementById("txtScanear");
    	var search = "op_"+txt.value;
    	//alert(search);
    	$('#'+search).attr({'checked':'checked'});
    }
}    

