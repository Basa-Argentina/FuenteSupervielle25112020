function guardarMatch(){
	var inTotal = document.getElementById("facturaTotal");
	var total = inTotal.value;
	var inTotal2 = document.getElementById("remcontTotal");
	var total2 = inTotal2.value;
	if(total == "0,00" && total2 == "0,00"){
		alert('Debe seleccionar al menos una Remisión y una Factura');
		return;
	}
	if(!verificarTotales()){
		alert('Los Totales de las Remisiones y las Facturas no coinciden');
		return;
	}
	document.forms[1].submit();
 }
 
function buscarFiltros(){
	if(document.getElementById("remcontTotal").value!="0,00"){
		var confirma=window.confirm('Si realiza una búsqueda en este momento perderá los cambios realizados.\n Desea Continuar?');
		if (!confirma){	 
			 return;
		}
	}
	document.forms[0].submit();
}
function borrarFiltros(){
	if(document.getElementById("remcontTotal").value!="0,00"){
		var confirma=window.confirm('Si realiza una búsqueda en este momento perderá los cambios realizados.\n Desea Continuar?');
		if (!confirma){	 
			 return;
		}
	}
	document.location="borrarFiltrosRemCont.html";
}
function volver(){
	document.location="menu.html";
}
function consultarCruise(sequence,company){
	//document.location="mostrarTTGIn.html?sequence="+sequence;
	window.open('iniciarTTGIn.html?sequence='+sequence+'&company='+company,'','left=20,top=20,width=900,height=600,scrollbars=1,resizable=0');
}

function sumarRemCont(opSuma,sumar){
	try{
		//javascript no maneja muy bien los decimales. por eso redondeamos todo a centavos.
		var inTotal = document.getElementById("remcontTotal");
		var total = parseFormatNumber(inTotal.value);
		total=parseFloat(total);
		total*=100;
		sumar=parseFloat(sumar);
		sumar*=100;
		sumar=Math.round(sumar);
		if(opSuma)
			total+=sumar;
		else
			total-=sumar;
		total/=100;
		total = Math.round(total*Math.pow(10,2))/Math.pow(10,2);
		inTotal.value=formatNumber(total);
		
		if(!verificarTotales()){
			inTotal.style.color = "RED";
		}
		else{
			inTotal.style.color = "BLACK";
		}
			
	}catch(e){
		alert (e);
	}
}

function mostrarRemCont(params){
	if(document.getElementById("remcontTotal").value!=0){
		var confirma=window.confirm('Si realiza una búsqueda en este momento perderá los cambios realizados.\n Desea Continuar?');
		if (!confirma)
			return;
	}
	window.location = params;		
}
function sumarFactura(opSuma,sumar){
	try{
		//javascript no maneja muy bien los decimales. por eso redondeamos todo a centavos.
		var inTotal = document.getElementById("facturaTotal");
		var total = parseFormatNumber(inTotal.value);
		total=parseFloat(total);
		total*=100;
		sumar=parseFloat(sumar);
		sumar*=100;
		sumar=Math.round(sumar);
		if(opSuma)
			total+=sumar;
		else
			total-=sumar;
		total/=100;
		total = Math.round(total*Math.pow(10,2))/Math.pow(10,2);
		inTotal.value=formatNumber(total);
		var inTotal2 = document.getElementById("remcontTotal");
		if(!verificarTotales()){
			inTotal2.style.color = "RED";
		}
		else{
			inTotal2.style.color = "BLACK";
		}
	}catch(e){
		alert (e);
	}
}
function mostrarFactura(params){
	if(document.getElementById("facturaTotal").value!=0){
		var confirma=window.confirm('Si realiza una búsqueda en este momento perderá los cambios realizados.\n Desea Continuar?');
		if (!confirma)
			return;
	}
	window.location = params;		
}

function consultarFactura(sequence,company){
	window.open('iniciarInsMatch.html?sequence='+sequence+'&company='+company,'','left=20,top=20,width=900,height=600,scrollbars=1,resizable=0');
}

function verificarTotales(){
	var inTotal = document.getElementById("facturaTotal");
	var total = inTotal.value;
	var inTotal2 = document.getElementById("remcontTotal");
	var total2 = inTotal2.value;
	if(total!=total2)
		return false;
	else
		return true;
}


function volverCancelar(){
	document.location="cancelarRemCont.html";
}
function parseFormatNumber(str){
	str = str.replace(/\./g,'');
	str = str.replace(',','.');
	return str;
}
function formatNumber(numero){
	var formater = new NumberFormat(numero);  
	formater.setSeparators(true, '.', ',');  
	return formater.toFormatted();  
} 

function validarNum(objeto,e)
{
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 8 || tecla == 13) return true;
    return /^[-+]?[0-9]+(\.[0-9]+)?$/.test(objeto.value);
}

function validarIntegerNum(e)
{
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 8 || tecla == 13) return true;
    patron = /\d/;
    te = String.fromCharCode(tecla);
    return patron.test(te);
}
