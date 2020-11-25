var menuAbierto=false;
//nombres de componentes dependientes del pais
var paisDep = new Array();
paisDep[0] = "provincia";
paisDep[1] = "localidad";
paisDep[2] = "barrio";

$(document).ready(function() {
	//tabla user
	$("#clienteDireccion tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#clienteDireccion tbody tr").mouseout(function(){
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
      $('#clienteDireccion tbody tr').contextMenu('myMenu1', {
 
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
			$("#clienteDireccion tbody tr").removeClass('tr_mouseover');
		}
 
      }); 

  //binding OnBlur() para los campos con popup
	$("#clienteCodigo").blur(function(){
		getAjaxCliente('clientesServlet','codigo','clienteCodigo',getClienteCallBack);
	});
	$("#pais").blur(function(){
		getAjax('paisServlet','nom','pais', null,getPaisCallBack);
	});
	$("#provincia").blur(function(){
		getAjax('provinciasPorPaisServlet','nom','provincia','pais',getProvinciaCallBack);
	});
	$("#localidad").blur(function(){
		getAjax('localidadesPorProvinciaServlet','nom','localidad','provincia',getLocalidadCallBack);
	});
	$("#barrio").blur(function(){
		getAjax('barriosPorLocalidadServlet','nom','barrio','localidad',getBarrioCallBack);
	});	
});

function agregarClienteDireccion(){
	document.location="precargaFormularioClienteDireccion.html";
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioClienteDireccion.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="precargaFormularioClienteDireccion.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarClienteDireccion.html?id="+id;
		    }
		});		
	}
}
function buscarFiltro(){
	$("#idBarrio").val($("#barrioLabel").html());
	$("#idLocalidadAux").val($("#localidadLabel").html());
	$("#idProvinciaAux").val($("#provinciaLabel").html());
	$("#idPaisAux").val($("#paisLabel").html());
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosClienteDireccion.html";
}

function volver(){
	document.location="menu.html";
}

function abrirPopupProvincia(claseNom){
	//uso clienteCodigo porque esta en duro y si se cambia dejan de funcionar otras pantallas
	var url = "precargaFormularioClienteDireccion.html?clienteCodigo="+$("#paisLabel").html();
	abrirPopupGeneral(claseNom, url);
}
function abrirPopupLocalidad(claseNom){
	//uso clienteCodigo porque esta en duro y si se cambia dejan de funcionar otras pantallas
	var url = "precargaFormularioClienteDireccion.html?clienteCodigo="+$("#provinciaLabel").html();
	abrirPopupGeneral(claseNom, url);
}
function abrirPopupBarrio(claseNom){
	//uso clienteCodigo porque esta en duro y si se cambia dejan de funcionar otras pantallas
	var url = "precargaFormularioClienteDireccion.html?clienteCodigo="+$("#localidadLabel").html();
	abrirPopupGeneral(claseNom, url);
}

function abrirPopup(claseNom){
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}

function abrirPopupGeneral(claseNom, url){
	jQuery.ajax({
        url: url,
        success: function(data){
           filteredResponse =  $(data).find(this.selector);
           if(filteredResponse.size() == 1){
                $(this).html(filteredResponse);                            
           }else{
                $(this).html(data);
           }
           $(this).displayTagAjax();
        } ,
        data: ({"time":new Date().getTime()}),
        context: $(".displayTagDiv."+ claseNom)
    });
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}

//ajax
function getAjaxCliente(url, varName, elementName, callBack){
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
		$("#clienteCodigo").val("");
}

//ajax 
function getAjax(url, varName, elementId, parentId, callBack){
	var input = document.getElementById(elementId);
	var parentValueId = "";
	if(parentId != null)
		parentValueId = '&val='+$("#"+parentId+"Label").html();
	if(input == null || input.value == undefined || input.value == ''){
		$("#"+elementId+"Label").html("");
		return;
	}
	var url = url+'?'+varName+'='+input.value+parentValueId;	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getPaisCallBack(sResponseText){	
	var componentId = "pais";
	setResponce(sResponseText, componentId);
	for(i=0;i<paisDep.length;i++){
		$("#"+paisDep[i]).val("");
		$("#"+paisDep[i]+"Label").html("");
	}	
}
//ajax
function getProvinciaCallBack(sResponseText){	
	var componentId = "provincia";
	setResponce(sResponseText, componentId);
	for(i=1;i<paisDep.length;i++){
		$("#"+paisDep[i]).val("");
		$("#"+paisDep[i]+"Label").html("");
	}	
}
//ajax
function getLocalidadCallBack(sResponseText){	
	var componentId = "localidad";
	setResponce(sResponseText, componentId);
	for(i=2;i<paisDep.length;i++){
		$("#"+paisDep[i]).val("");
		$("#"+paisDep[i]+"Label").html("");
	}	
}
//ajax
function getBarrioCallBack(sResponseText){	
	var componentId = "barrio";
	setResponce(sResponseText, componentId);	
}

//ajax
function setResponce(sResponseText, componentId){
	if (sResponseText != 'null' && sResponseText != ""){
		var array = sResponseText.split("-");
		$("#"+componentId).val(array[0]);		
		$("#"+componentId+"Label").html(array[1]);		
	}else{
		$("#"+componentId).val("");
		$("#"+componentId+"Label").html("");
	}	
}

function addOptionToSelect(selectComponent, option){
	if($.browser.msie)
		selectComponent.add(option); //IE
	else
		selectComponent.add(option,null); //Mozilla, Chrome
}	
