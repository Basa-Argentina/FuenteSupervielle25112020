var menuAbierto=true;

$(document).ready(function(){
	
	parent.$.fancybox.showActivity();
	var codigoEmpresa = parent.document.getElementById("codigoEmpresa").value;
	$('#codigoEmpresa').val(codigoEmpresa);
	var codigoSucursal = parent.document.getElementById("codigoSucursal").value;
	$('#codigoSucursal').val(codigoSucursal);
	var periodo = parent.document.getElementById('periodo').value;
	$('#periodo').val(periodo);
	var fechaRegistro = parent.document.getElementById("fechaRegistro").value;
	var fechaFacturacion = parent.document.getElementById("fechaFacturacion").value;
	var descripcion = parent.document.getElementById("descripcion").value;
	var select = parent.document.getElementById('estado');
	if(select!= null){
	    var options = select.getElementsByTagName('option');
	    var option = select[select.selectedIndex];
	    var estado = option.value;
	}
    var accion = parent.document.getElementById("accion").value;
	
	window.location='mostrarLoteFacturacionDetalle.html?periodo='+periodo+"&codigoEmpresa="+codigoEmpresa
	+"&codigoSucursal="+codigoSucursal+'&fechaRegistro='+fechaRegistro+'&fechaFacturacion='+fechaFacturacion
	+'&descripcion='+descripcion+'&estado='+estado+'&accion='+accion;
	
});