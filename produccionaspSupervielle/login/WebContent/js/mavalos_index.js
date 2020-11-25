var separador;

$(document).ready(function() {			
	//Tooltips
	$("img[title]").tooltip();
	$("a[title]").tooltip();
	//Input Labels
	$("label").inFieldLabels();
	$("button[title]").tooltip();
	
	//Seteo los eventos keydown de los inputs del login
	$("#j_empresa").keydown(function(event){
		if (event.keyCode == '13')
			$("#j_username").focus();
	});
	
	$("#j_username").keydown(function(event){
		if (event.keyCode == '13')
			$("#j_password").focus();
	});
	$("#j_password").keydown(function(event){
		if (event.keyCode == '13')
			login();
	});
});

function setSeparador(val){
	separador = val;
}

function login(){
	var empresa = $('#j_empresa').val();
	var usuario = $('#j_username').val();
	if(usuario.indexOf('@'+empresa) == -1)
		$('#j_username').val(usuario+separador+empresa);
	$('#f').submit();
}

function configurarEmpresaSucursal(){
	document.location="precargaFormularioUserPersonalData.html";
}

function noConfigurarEmpresaSucursal(){
	$('#continuar').submit();
}

function ingresarModulo(formname, submoduloSel){
	$.cookie("submoduloSel", submoduloSel, { path: '/' });
	$("#"+formname).submit();
}


function abrirPopup(claseNom){
	$("#codigoSucursal").val("");
	var labelLista =  document.getElementById("codigoSucursalLabel");
	labelLista.textContent = "";
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
	var url = "precargaFormularioDeposito.html?empresaCodigo="+$("#codigoEmpresa").val();
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
function getSucursalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSucursalLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoSucursalLabel").html("");
	}
}

//ajax
function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoEmpresaLabel").html(sResponseText);
	else{
		$("#idCliente").val("");
		$("#codigoEmpresaLabel").html("");
		$("#codigoSucursalLabel").html("");
		$("#codigoSucursal").val("");
	}
}

