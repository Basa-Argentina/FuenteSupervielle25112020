var menuAbierto=false;

$(document).ready(function() {
	
	$("#observaciones").val($.trim($('#observaciones').val()));
	
	$('#observaciones').keydown(function() {
	    var longitud = $(this).val().length;
	    var resto = 255 - longitud;
	    if(resto <= 0){
	        $('#observaciones').attr("maxlength", 255);
	    }
	});
	
	//fancybox
	$('#fancyboxImagenes').fancybox({
		'padding'			: 10,
		'titleShow'			: false,
		'showNavArrows'		: true,
		'autoDimensions'	: false,
		'width'         	: 730,
		'scrolling'			: 'auto',
		'height'        	: 575,
		'overlayColor'		: '#CCCCCC',
		'overlayOpacity'	: 0.7,
		'showCloseButton'	: true,
		'autoScale'			: false,
		'transitionIn'		: 'elastic',
		'transitionOut'		: 'elastic',
		'type'				: 'iframe'
		//'onClosed'			: function(){ $('#botonAgregar').removeAttr('disabled');}
	});
	
	//tabla items 
	$("#operacionReferencia tbody tr").each(function(){
		if($(this).find('#hdn_path').val()!=null && $(this).find('#hdn_path').val()!='')
				$(this).addClass('tr_rearchivo');
	});
	
	//tabla items 
	$("#operacionReferencia tbody tr").mouseover(function(){
		if(!menuAbierto){
				$(this).addClass('tr_mouseover');
			if($(this).find('#hdn_path').val()!=null && $(this).find('#hdn_path').val()!=''){
				$(this).removeClass('tr_rearchivo');
				$(this).addClass('tr_mouseover');
			}
		}
	});
	$("#operacionReferencia tbody tr").mouseout(function(){
		if(!menuAbierto){
			$(this).removeClass('tr_mouseover');
			if($(this).find('#hdn_path').val()!=null && $(this).find('#hdn_path').val()!=''){
				$(this).removeClass('tr_mouseover');
				$(this).addClass('tr_rearchivo');
			}
		}
	});
		
	//PopUp
    $('#operacionReferencia tbody tr').contextMenu('myMenu1', {

      bindings: {


        'eliminar': function(t) {

      	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val(),$(t).find('#hdn_proviene_lectura').val(),$(t).find('#hdn_traspasado').val()
      			,$(t).find('#hdn_proviene_lectura_mensaje').val(),$(t).find('#hdn_traspasado_mensaje').val());

        },
        
        'verImagenes': function(t) {
        	verImagenes($(t).find('#hdn_path').val(),t);
        }

      },
		onShowMenu: function(e, menu) {
			var cell = e.currentTarget.children[17].children[0].value;
			if(cell == null || cell == "" || cell == undefined){
				menuAbierto=true;
				$('#verImagenes', menu).remove();
				return menu;
			}else{
				menuAbierto=true;
				return menu;
			}
		},
		onHide: function(){
			menuAbierto=false;
			$("#requerimiento tbody tr").removeClass('tr_mouseover');
		}

    }); 
	
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
	$(".integer").numeric(false, function() {
		this.value = "";
		this.focus();
	});
	
	 $('#codigoElemento').keypress(function(e) {
		  if (e.keyCode == 13) {
			  
			  buscarCodigoElemento();
		  }
	  });
     
	 $("#buscaLectura").click(function(){
			abrirPopupSeleccion("popUpSeleccionLectura.html","codigoEmpresa",null, null);
		  	});
	 $("#cargaLectura").click(function(){
		 getAjax('lecturasServlet','codigo','codigoLectura',getLecturaCallBack);
	 });
	 $("#codigoLectura").blur(function(){
			getAjax('lecturasServlet','codigo','codigoLectura',getLecturaCallBack2);
	 });
	
	 $('#pop').hide();
});

function verImagenes(path,t){
	if(path!=null){
		$(t).removeClass('tr_mouseover');
		$(t).addClass('tr_rearchivo');
		$("#fancyboxImagenes").attr("href",'abrirFancyBoxImagenesRearchivo.html?fileName='+path);
		$('#fancyboxImagenes').click();
	}
	
}

function buscarFiltro(){
	$("#buscarElemento").val(true);
	$("#buscarElementoReferencia").val(false);
	popupOnDiv($('#pop'),'darkLayer');
	document.forms[0].submit();
}
function buscarElementos(){
	$("#buscarElemento").val(false);
	$("#buscarElementoReferencia").val(true);
	popupOnDiv($('#pop'),'darkLayer');
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosOperacionReferencia.html";
}

function volverCancelar(){
	popupOnDiv($('#pop'),'darkLayer');
	document.location="mostrarOperacion.html";
}

function guardarYSalir() {
	
	
	if($("#rearchivosPendientes").val() != null && $("#rearchivosPendientes").val() == 'true'){
		jAlert($("#mensajeRearchivosPendientes").val(),"Error");
		return;
	}
	$("#observacionesHdn").val($("#observaciones").val());
	$("#buscarElemento").val(false);
	$("#buscarElementoReferencia").val(false);
	if($("#procesando").val())
		$("#accionGuardar").val('procesando');
	var finOK = $("#finalizarOK").val();
	var traspE = $("#traspasar").val();
	var finError = $("#finalizarError").val();
	var procE = $("#procesando").val();
	
	if(finOK == 'true' && traspE=='false'){
		jConfirm($("#preguntaFinalizarOK").val(), 'Confirmar Guardar',function(r) {
		    if(r){
		    	$("#accionGuardar").val('finalizarOK');
		    }
		    else{
		    	$("#accionGuardar").val('');
		    }
		    verificarTipoCalculo();
		});
	}
	if(finOK=='true' && traspE=='true'){
		jConfirm($("#preguntaFinalizarOKConTraspaso").val(), 'Confirmar Guardar',function(r) {
		    if(r){
		    	$("#accionGuardar").val('finalizarOKConTraspaso');
		    }
		    else{
		    	$("#accionGuardar").val('');
		    }
		    verificarTipoCalculo();
		});
	}
	if(finOK == 'false' && traspE == 'true'){
		jConfirm($("#preguntaTraspaso").val(), 'Confirmar Guardar',function(r) {
		    if(r){
		    	$("#accionGuardar").val('traspaso');
		    }
		    else{
		    	$("#accionGuardar").val('');
		    }
		    verificarTipoCalculo();
		});
	}
	if(finOK=='false' && finError=='true'){
		jConfirm($("#preguntaFinalizarError").val(), 'Confirmar Guardar',function(r) {
		    if(r){
		    	$("#accionGuardar").val('finalizarError');
		    }
		    else{
		    	$("#accionGuardar").val('');
		    }
		    verificarTipoCalculo();
		});
	}
	if(finOK=='false' && finError=='true' && traspE == 'true'){
		jConfirm($("#preguntaFinalizarErrorConTraspaso").val(), 'Confirmar Guardar',function(r) {
		    if(r){
		    	$("#accionGuardar").val('finalizarErrorConTraspaso');
		    }
		    else{
		    	$("#accionGuardar").val('');
		    }
		    verificarTipoCalculo();
		});
	}
	if(procE=='true'){
		verificarTipoCalculo();
	}
	
	
}

function selectAll(box, classStyle) {
	var inTotal = document.getElementById("cantidadElementosSeleccionados");
	var total = inTotal.value;
	total=parseFloat(total);
	var chk = box.checked;
	total=0;
    $("#operacionReferencia").find('.'+classStyle).each( function(){
    	if(chk)
    		total+=1;
    	this.checked = chk;
    });
    inTotal.value=total;
}

function buscarCodigoElemento(){
	var mensaje = $("#mensajeErrorEAN").val();
	if(checkEan($("#codigoElemento").val())){
		$("#seleccionBusqueda").val('porElemento');
		popupOnDiv($('#pop'),'darkLayer');
		document.forms[1].submit();
	}
	else{
		jAlert(mensaje,"Error");
		$("#codigoElemento").focus();
		return;
	}
}

function setFocusInicial(){
	$("#codigoElemento").focus();
}

function sumarElemento(opSuma){
	var inTotal = document.getElementById("cantidadElementosSeleccionados");
	var total = inTotal.value;
	total=parseFloat(total);
	if(opSuma)
		total+=1;
	else
		total-=1;
	inTotal.value=total;
}

function cambiarEstadoSeleccion(){
	var mensaje = $("#mensajeErrorSeleccion").val();
	var inTotal = document.getElementById("cantidadElementosSeleccionados");
	var total = inTotal.value;
	total=parseFloat(total);
	if(total == 0){
		jAlert(mensaje,"Error");
		return;
	}
	$("#seleccionBusqueda").val('porSeleccion');
	popupOnDiv($('#pop'),'darkLayer');
	document.forms[1].submit();
}

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
function getLecturaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoLecturaLabel").html(sResponseText);
		$("#seleccionBusqueda").val('porLectura');
		popupOnDiv($('#pop'),'darkLayer');
		document.forms[1].submit();
	}
	else{
		$("#codigoLecturaLabel").html("");
		$("#codigoLectura").val("");
	}
}

//ajax
function getLecturaCallBack2(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoLecturaLabel").html(sResponseText);
		$("#seleccionBusqueda").val('porLectura');
	}
	else{
		$("#codigoLecturaLabel").html("");
		$("#codigoLectura").val("");
	}
}

function verificarTipoCalculo(){
	var tipoCalculo = $("#tipoCalculo").val();
	if(tipoCalculo == 'Manual'){
		jPrompt('Cantidad', '1', 'Ingrese cantidad a facturar', function(r) {
		    if( r ){
		    	if(validarInt(r)){
		    		$("#cantidadTipoCalculo").val(r);
		    		popupOnDiv($('#pop'),'darkLayer');
		    		document.forms[0].submit();
		    	}
		    	else
		    		verificarTipoCalculo();
		    }
		    else{
		    	verificarTipoCalculo();
		    }
		});
	}
	else{
		popupOnDiv($('#pop'),'darkLayer');
		document.forms[0].submit();
	}
}

function validarInt(valor) {  
	var error = '';
	var re = /^(-)?[0-9]*$/;
	if (!re.test(valor)) {  
		return false;  
	}     
	return true; 
}

function eliminar(mensaje, id, proviene_lectura, traspasado, mensajeNoProvieneLectura, mensajeTraspasado){
	if(proviene_lectura!=null && proviene_lectura == 'false'){
		jAlert(mensajeNoProvieneLectura);
		return;
	}
	if(traspasado!=null && traspasado == 'true'){
		jAlert(mensajeTraspasado);
		return;
	}
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	popupOnDiv($('#pop'),'darkLayer');
		    	document.location="eliminarOperacionReferencia.html?id="+id;
		    }
		});
	}
}