var menuAbierto=false;

$(document).ready(function() {
	//tabla Estante
	$("#estante tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#estante tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	var urlParams = {};
	(function () {
	    var e,
	        a = /\+/g,  // Regex for replacing addition symbol with a space
	        r = /([^&=]+)=?([^&]*)/g,
	        d = function (s) { return decodeURIComponent(s.replace(a, " ")); },
	        q = window.location.search.substring(1);

	    while (e = r.exec(q))
	       urlParams[d(e[1])] = d(e[2]);
	})();

	//Tooltips
	$("img[title]").tooltip();
	
	//Slide 1
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	//Slide 1
	$('#grupoDiv').attr({'style':'display:block'});
	$('#grupoImg').click(function() {
		$('#grupoDiv').slideToggle('slideUp');
		$('#grupoImgSrcDown').slideToggle('slideUp');
		$('#grupoImgSrc').slideToggle('slideUp');
	});
	
	//PopUp
    $('#estante tbody tr').contextMenu('myMenu1', {

      bindings: {

        'consultar': function(t) {
          consultar($(t).find('#hdn_id').val());
        },

        'modificar': function(t) {
          modificar($(t).find('#hdn_id').val());

        },

        'eliminar': function(t) {

      	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val(),$('#id').val());

        }

      },
		onShowMenu: function(e, menu) {
			menuAbierto=true;
			return menu;

		},
		onHide: function(){
			menuAbierto=false;
			$("#estante tbody tr").removeClass('tr_mouseover');
		}

    });

    $("#codigoEmpresa").blur(function(){
    	getAjax('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
	});
	$("#codigoSucursal").blur(function(){
		getAjax('sucursalesServlet','codigo','codigoSucursal', getSucursalCallBack);
	});
	$("#codigoDeposito").blur(function(){
		getAjax('depositosServlet','codigo','codigoDeposito',getDepositoCallBack);
	});
  	$("#codigoSeccion").blur(function(){
		getAjax('seccionesServlet','codigo','codigoSeccion',getSeccionCallBack);
	});
	if($("#codigoDeposito").val() != null){
		getAjax('depositosServlet','codigo','codigoDeposito',getDepositoCallBack);
	}
	if($("#codigoSucursal").val() != null){
		getAjax('sucursalesServlet','codigo','codigoSucursal',getSucursalCallBack);
	}
	if($("#codigoEmpresa").val() != null){
		getAjax('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
	}
  		
});

//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function agregarEstante(){
	var accion = document.getElementById("accion");
	document.location="precargaFormularioEstante.html?accionGrupo="+accion.value;
}

function consultar(id){
	var accion = document.getElementById("accion");
	if(id!=null)
		document.location="precargaFormularioEstante.html?accionGrupo="+accion.value+"&accion=CONSULTA&id="+id;
}

function modificar(id){
	var accion = document.getElementById("accion");
	if(id!=null)
		document.location="precargaFormularioEstante.html?accionGrupo="+accion.value+"&accion=MODIFICACION&id="+id;
}

function eliminar(mensaje, idEstante, idGrupo){
	if(idEstante!=null && idEstante!=undefined && idGrupo!=null && idGrupo!=undefined && mensaje!=undefined){
		jConfirm(mensaje, $('#confirmarEliminarEstante').val(),function(r) {
		    if(r){
		    	var div = $("#pop");
				popupOnDiv(div,'darkLayer');
		    	document.location="eliminarEstanteria.html?idEstante="+idEstante+"&idGrupo="+idGrupo;
		    }
		});
	}
}

function volver(){
	document.location="mostrarGrupo.html";
}
function volverCancelar(){
	document.location="mostrarGrupo.html";
}
function guardarYSalir(){
 	document.forms[0].submit();
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

function abrirPopupSucursal(claseNom, mensaje, title){
	if($("#codigoEmpresa").val()==null || $("#codigoEmpresa").val()==""){
		jAlert(mensaje,title);
		return;
	}
	var url = "precargaFormularioGrupo.html?empresaCodigo="+$("#codigoEmpresa").val();
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

function abrirPopupDeposito(claseNom, mensaje, title){
	if($("#codigoSucursal").val()==null || $("#codigoSucursal").val()==""){
		jAlert(mensaje,title);
		return;
	}
	var url = "precargaFormularioGrupo.html?empresaCodigo="+$("#codigoEmpresa").val()+"&sucursalCodigo="+$("#codigoSucursal").val();
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

function abrirPopupSeccion(claseNom, mensaje, title){
	if($("#codigoDeposito").val()==null || $("#codigoDeposito").val()==""){
		jAlert(mensaje,title);
		return;
	}
	var url = "precargaFormularioGrupo.html?empresaCodigo="+$("#codigoEmpresa").val()+"&sucursalCodigo="+$("#codigoSucursal").val()+"&depositoCodigo="+$("#codigoDeposito").val();
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
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}

//ajax
function getSeccionCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSeccionLabel").html(sResponseText);	
	else{
		$("#idCliente").val("");
		$("#codigoSeccionLabel").html("");
	}
}
//ajax
function getDepositoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoDepositoLabel").html(sResponseText);	
	else{
		$("#idCliente").val("");
		$("#codigoDepositoLabel").html("");
		$("#codigoSeccionLabel").html("");
		$("#codigoSeccion").val("");
	}
}
//ajax
function getSucursalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSucursalLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoSucursalLabel").html("");
		$("#codigoDepositoLabel").html("");
		$("#codigoDeposito").val("");
	}
}

//ajax
function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoEmpresaLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoEmpresaLabel").html("");
		$("#codigoDepositoLabel").html("");
		$("#codigoSucursalLabel").html("");
		$("#codigoDeposito").val("");
		$("#codigoSucursal").val("");
	}
}
//Función para validar que si se ha ingresado un número de una cifra se complete con el 0 a la izquieda
function formato(caracteres)
{
	tam = document.getElementById("codigo").value.length;
	valor = document.getElementById("codigo").value;
	aux = caracteres - tam;
	if(tam == 1)
	{
		var i = 0;
		while(i<aux)
		{
			valor= '0'+valor;
			i++;
		}
		
	}
	document.getElementById("codigo").value = valor;
}

function borrar()
{
	document.getElementById("codigoSucursal").value = "";
	document.getElementById("codigoSucursalLabel").value = null;
	getAjax('sucursalesServlet','codigo','codigoSucursal', getSucursalCallBack);

}