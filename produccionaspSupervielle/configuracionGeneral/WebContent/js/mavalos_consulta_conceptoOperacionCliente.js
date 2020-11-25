var menuAbierto=false;

$(document).ready(function() {
	//tabla remito
	$("#conceptoOperacionCliente tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#conceptoOperacionCliente tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
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
	
	$('#imprimirRemitos').click(function(){
		
		 	var allVals = [];
		    $("input[class='checklote']:checked").each(function() {
		    	allVals.push($(this).val());
		    });
		    if(allVals.length == 0)
		    {
		    	jAlert('Debe seleccionar al menos un remito para imprimir.');
				return;
			}
		    else
			{
		    	window.open('imprimirRemito.html?&seleccionados='+allVals,'');
		    }
	});
	
	//Validaciones y botones
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

	$(".inputTextNumericPositiveIntegerOnly").numeric({ decimal: false, negative: false});
	
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	$('#busquedaDiv > table').addClass('seccion');
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
		
	//PopUp
      $('#conceptoOperacionCliente tbody tr').contextMenu('myMenu1', {
 
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
			$("#conceptoOperacionCliente tbody tr").removeClass('tr_mouseover');
		}
 
      }); 
    //Inicio menu
    	$("ul.topnav li").hover(
    		function() {
    			$(this).addClass("hover");
    			$(this).find("ul.subnav").slideDown('fast').show(); //Drop down the subnav on click  
    		}, 
    		function(){  
    			$(this).removeClass("hover");
    			$(this).find("ul.subnav").slideUp('fast'); //When the mouse hovers out of the subnav, move it back up  
    		}
        );
    	//Fin menu
      	//binding OnBlur() para los campos con popup	
    	$("#codigoCliente").blur(function(){
    		searchAjax('seleccionCliente.html','codigoCliente','codigoEmpresa',getClienteCallBack);
    	});
    	$("#buscaCliente").click(function(){
    		abrirPopupSeleccion("popUpSeleccionCliente.html","codigoEmpresa","clientesPopupMap", $("#mensajeSeleccioneEmpresa").val());
    	});
    	
    	$('.completarZeros').blur();


    	$("#imprimir").click(function(){
    		exportarPdf();
      	});
    	

});

function exportarPdf(){
	window.open('exportarConceptoOperacionClientePdf.html','popupConceptoOperacionCliente','width=900,height=700,scrollbars=1');
}

function agregar(){
	document.location="precargaFormularioConceptoOperacionCliente.html";
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioConceptoOperacionCliente.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="precargaFormularioConceptoOperacionCliente.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarConceptoOperacionCliente.html?id="+id;
		    }
		});
	}
}
function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosConceptoOperacionCliente.html";
}
function volver(){
	document.location="menu.html";
}

function abrirPopup(claseNom){
	$("#codigoSucursal").val("");
	$("#codigoDeposito").val("");
	var labelLista =  document.getElementById("codigoSucursalLabel");
	labelLista.textContent = "";
	var labelConcepto =  document.getElementById("codigoDepositoLabel");
	labelConcepto.textContent = "";
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

function getClienteCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoClienteLabel").html(sResponseText);
	else{
		$("#codigoClienteLabel").html("");
		$("#codigoCliente").val("");
	}
}

function updateTextArea() {         
   
 }
$(function() {
  $('#c_b input').click(updateTextArea);
  updateTextArea();
});
