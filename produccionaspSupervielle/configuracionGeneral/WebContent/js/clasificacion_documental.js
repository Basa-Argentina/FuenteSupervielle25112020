$(document).ready(function(){
	ddtreemenu.createTree("treemenu", false);
	
	 $('#treemenu li').contextMenu('myMenu1', {
	        
		 	bindings: {
	          'consultar': function(t) {
	        	  actualizarCoordenadasScroll();
	        	  consultar(t);
	          },
	          'modificar': function(t) {
	        	  actualizarCoordenadasScroll();
	        	  modificar(t);
	          },
	          'eliminar': function(t) {
	        	  actualizarCoordenadasScroll();
	        	  eliminar(t);
	          },
	          'crearSubCarpeta': function(t) {
	        	  actualizarCoordenadasScroll();
	        	  crearSubCarpeta(t);
	          },
	          'crearIndice': function(t) {
	        	  actualizarCoordenadasScroll();
	        	  crearIndice(t);
	          },
	          'personal': function(t){
	        	  actualizarCoordenadasScroll();
	        	  personalClienteEmp(t);
	          }
	        },
	        
			onShowMenu: function(e, menu) {
				menuAbierto=true;
				var nodo = $(e.currentTarget).children('#hdn_nodo').val();
				if(nodo == 'I'){
					$(menu).find('#personal').hide();
				}else{
					$(menu).find('#personal').show();
				}
				return menu;
			},
			
			onHide: function(){
				menuAbierto=false;
				$("#treemenu li").removeClass('tr_mouseover');
			}
			
			
	 }); 
	 
	 $("#tab_header_ind").click(function(){
		 $("#tab_header_grp").removeClass("selected");
		 $("#tab_content_grp").hide();
		 $("#tab_header_ind").addClass("selected");
		 $("#tab_content_ind").show();
	 });
	 
	 $("#tab_header_grp").click(function(){
		 $("#tab_header_ind").removeClass("selected");
		 $("#tab_content_ind").hide();
		 $("#tab_header_grp").addClass("selected");
		 $("#tab_content_grp").show();
	 });
	 
	 setIndividualSeleccionadoChange('#individualFecha1Seleccionado','#individualFecha1Titulo','#individualFecha1Requerido');
	 setIndividualSeleccionadoChange('#individualFecha2Seleccionado','#individualFecha2Titulo','#individualFecha2Requerido');
	 setIndividualSeleccionadoChange('#individualNumero1Seleccionado','#individualNumero1Titulo','#individualNumero1Requerido');
	 setIndividualSeleccionadoChange('#individualNumero2Seleccionado','#individualNumero2Titulo','#individualNumero2Requerido');
	 setIndividualSeleccionadoChange('#individualTexto1Seleccionado','#individualTexto1Titulo','#individualTexto1Requerido');
	 setIndividualSeleccionadoChange('#individualTexto2Seleccionado','#individualTexto2Titulo','#individualTexto2Requerido');
	 
	 setGrupalSeleccionadoChange('#grupalFechaSeleccionado','#grupalFecha1Titulo','#grupalFecha2Titulo','#grupalFechaRequerido');
	 setGrupalSeleccionadoChange('#grupalNumeroSeleccionado','#grupalNumero1Titulo','#grupalNumero2Titulo','#grupalNumeroRequerido');
	 setGrupalSeleccionadoChange('#grupalTextoSeleccionado','#grupalTexto1Titulo','#grupalTexto2Titulo','#grupalTextoRequerido');
	 
	 $('#descripcionSeleccionado').change(function(){
		 if($('#descripcionSeleccionado').is(':checked')){
			 $('#descripcionTitulo').removeAttr('readonly').addClass('requerido');
		 }else{
			 $('#descripcionRequerido').removeAttr('checked');
			 $('#descripcionTitulo').val('').attr('readonly','readonly').removeClass('requerido');
		 }
	 });
	
	 $('#descripcionRequerido').change(function(){
		 if($('#descripcionRequerido').is(':checked')){
			 $('#descripcionSeleccionado').attr('checked','checked');
		 }
	 });
	 
	 $('#indiceIndividual').change(function (){
		 if(!$("#indiceIndividual").is(':checked')){
			 $("#individualFecha1Seleccionado").removeAttr('checked').trigger('change');
			 $("#individualFecha2Seleccionado").removeAttr('checked').trigger('change');
			 $("#individualNumero1Seleccionado").removeAttr('checked').trigger('change');
			 $("#individualNumero2Seleccionado").removeAttr('checked').trigger('change');
			 $("#individualTexto1Seleccionado").removeAttr('checked').trigger('change');
			 $("#individualTexto2Seleccionado").removeAttr('checked').trigger('change');
		 }
	 });
	 $('#indiceGrupal').change(function (){
		 if(!$("#indiceGrupal").is(':checked')){
			 $("#grupalFechaSeleccionado").removeAttr('checked').trigger('change');
			 $("#grupalNumeroSeleccionado").removeAttr('checked').trigger('change');
			 $("#grupalTextoSeleccionado").removeAttr('checked').trigger('change');
		 }
	 });
	 
	$('#btn_guardar').click(function(){
		var accion = $('#clasifDocForm').children('#accion').val();
		if(accion != null && accion == 'personal'){
			seleccionarTodos("empleadosSeleccionados");
		}
		$('#clasifDocForm').submit(); 
	});
	$('#btn_cancelar').click(function(){
		$('#cargar').submit();
	});

	$('#tdArbol li').mousedown(function(e){
		 if (e.which == 3) {
			 var divArbol = $('#tdArbol');
			 $('#auxScrollX').val(divArbol.scrollLeft());
			 $('#auxScrollY').val(divArbol.scrollTop());
		 }
	});
	
	$('#btn_actualizarCodigo').click(function(){
		getAjax('clasificacionDocumentalUltimoCodigoServlet', 'clienteEmpId', 'id_cliente_emp', getUltimoCodigoCallBack);
	});
	
	var table = $('#tdArbol');
	var alto = '380px';
	var ancho = '300px';
	table.css('height', alto);
	table.css('width', ancho);
		
	setCoordenadasScroolDefault();
	
	$('#bsqArbol').keyup(function(){
		$('.txtArbol').removeClass('nodoArbolSeleccionado');
		if($('#bsqArbol').val()!=''){
			ddtreemenu.flatten('treemenu', 'contact');//'expand');
			$('.txtArbol:contains("'+$('#bsqArbol').val()+'")').addClass('nodoArbolSeleccionado');
			$('.nodoArbolSeleccionado').closest('ul').not("#treemenu").each(function(ind,nodo){
				ddtreemenu.expandSubTree('treemenu', nodo );
			});
		}
	});
	$('#bsqArbol').trigger('keyup');
	
	$("#imprimir").click(function(){
		exportarPdf();
  	});
	
	chequearLeeCodigoBarra();
	
	$("#leeCodigoBarra").click(function(){
		chequearLeeCodigoBarra();
	});
	
});

function exportarPdf(){
	window.open('exportarClasificacionDocPdf.html','popupClasificacionDoc','width=900,height=700,scrollbars=1');
}

function setIndividualSeleccionadoChange(check,text,req){
	$(check).change(function(){
		 if($(check).is(':checked')){
			 $('#indiceIndividual').attr('checked','checked');
			 $(text).removeAttr('readonly').addClass('requerido').focus();
		 } else {
			 $(text).val('').attr('readonly','readonly').removeClass('requerido');
			 $(req).removeAttr('checked');
		 }
	});
	$(req).change(function(){
		 if($(req).is(':checked')){
			 $(check).attr('checked','checked').trigger('change');
		 }
	});
}
function setGrupalSeleccionadoChange(check,text1,text2,req){
	$(check).change(function(){
		 if($(check).is(':checked')){
			 $('#indiceGrupal').attr('checked','checked');
			 $(text1).removeAttr('readonly').addClass('requerido').focus();
			 $(text2).removeAttr('readonly').addClass('requerido');
		 } else {
			 $(text1).val('').attr('readonly','readonly').removeClass('requerido');
			 $(text2).val('').attr('readonly','readonly').removeClass('requerido');
			 $(req).removeAttr('checked');
		 }
	});
	$(req).change(function(){
		 if($(req).is(':checked')){
			 $(check).attr('checked','checked').trigger('change');
		 }
	});
}

function consultar (selected){
	var id = $(selected).children('#hdn_id').val();
	ejecutar('consultar',id);
}

function modificar (selected){
	var id = $(selected).children('#hdn_id').val();
	ejecutar('modificar',id);
}

function eliminar (selected){
	var id = $(selected).children('#hdn_id').val();
	jConfirm("Esta seguro de eliminar el elemento seleccionado?","Confirmación",function(res){
		if(res)
			ejecutar('eliminar',id);
	});
}
function confirmaEliminar(ok){
	
}
function crearSubCarpeta (selected){
	var id = obtenerIdCarpeta(selected);
	ejecutar('crearSubCarpeta',id);
}
function crearIndice (selected){
	var id = obtenerIdCarpeta(selected);
	ejecutar('crearIndice',id);
}

function personalClienteEmp(selected){
	var id = obtenerIdCarpeta(selected);
	ejecutar('personal',id);
}

function obtenerIdCarpeta(selected){
	while($(selected).children('#hdn_nodo').val()!='N'){
		selected = $(selected).parent();
	}
	var id = $(selected).children('#hdn_id').val();
	return id;
}
function ejecutar(accion,id_seleccionado){
	if(id_seleccionado || accion == 'crearCarpeta'){
		$('#cargar').children('#accion').val(accion);
		$('#cargar').children('#id_seleccionado').val(id_seleccionado);
		$('#cargar').submit();
	}else{
		jAlert("Debe guardar primero el nodo actual","Edicion Detenida");
	}
}

//ajax
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteAspId='+$("#clienteAspId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}

function getUltimoCodigoCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$('#codigo').val(sResponseText);
	else{			
		$('#codigo').val("");
	}
}

function volver(){
	document.location="iniciarCliente.html";
}

function actualizarCoordenadasScroll(){
	var x = $('#auxScrollX').val();
	var y = $('#auxScrollY').val();
	
	$('#gArbolScrollX').val(x);
	$('#gArbolScrollY').val(y);
	$('#cArbolScrollX').val(x);
	$('#cArbolScrollY').val(y);
}

function setCoordenadasScroolDefault(){
	var x = $('#auxScrollX').val();
	var y = $('#auxScrollY').val();
	var divArbol = $('#tdArbol');
	divArbol.scrollLeft(x);
	divArbol.scrollTop(y);	
}

function chequearLeeCodigoBarra(){
	if($('#leeCodigoBarra').is(':checked'))
		$('.trLeeCodigo').slideDown('slow').show();
	else
		$('.trLeeCodigo').slideUp('slow');
}