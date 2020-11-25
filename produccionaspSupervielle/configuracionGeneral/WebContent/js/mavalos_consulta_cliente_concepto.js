var menuAbierto=false;

$(document).ready(function() {
	//tabla user
	$("#clienteConcepto tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#clienteConcepto tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
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
	
		
	//PopUp
      $('#clienteConcepto tbody tr').contextMenu('myMenu1', {
 
        bindings: {
 
          'consultar': function(t) {
            consultar($(t).find('#hdn_id').val());
          },
 
          'modificar': function(t) {
            modificar($(t).find('#hdn_id').val());
 
          },
 
          'eliminar': function(t) {
 
        	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val());
 
          }
 
        },
		onShowMenu: function(e, menu) {
			menuAbierto=true;
			return menu;

		},
		onHide: function(){
			menuAbierto=false;
			$("#clienteConcepto tbody tr").removeClass('tr_mouseover');
		}
 
      }); 

  //binding OnBlur() para los campos con popup
	$("#clienteCodigo").blur(function(){
		getAjax('clientesServlet','codigo','clienteCodigo',getClienteCallBack);
	});
	$("#listaPrecioCodigo").blur(function(){
		getAjax('listasPreciosServlet','codigo','listaPrecioCodigo',getListaCallBack);
	});
	$("#conceptoCodigo").blur(function(){
		getAjax('conceptoFacturableServlet','codigo','conceptoCodigo',getConceptoCallBack);
	});
});

function agregarClienteConcepto(){
	document.location="precargaFormularioClienteConcepto.html";
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioClienteConcepto.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="precargaFormularioClienteConcepto.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarClienteConcepto.html?id="+id;
		    }
		});
	}
}
function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosClienteConcepto.html";
}

function volver(){
	document.location="menu.html";
}

function abrirPopup(claseNom){
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
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
function getClienteCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#clienteCodigoLabel").html(sResponseText);
	else
		$("#idCliente").val("");
}
//ajax
function getListaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#listaPrecioCodigoLabel").html(sResponseText);
	else
		$("#idCliente").val("");
}

//ajax
function getConceptoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#conceptoCodigoLabel").html(sResponseText);
	else
		$("#idCliente").val("");
}
