function seleccionarTodos(nombreComponente)
{
  var seleccionados=document.getElementById(nombreComponente+"Der");
  var sel="";
  var i=0;
  for (i=0;i<seleccionados.options.length;i++)
  {
	seleccionados.options[i].selected=true;
    sel+=seleccionados.options[i].value+",";
  }
  sel=sel.substring(0,sel.length-1);
  var hidden= document.getElementById(nombreComponente);
  hidden.value=sel;
}
function leftToRight(nombreComponente)
{
  var desde=document.getElementById(nombreComponente+"Izq");
  var hasta=document.getElementById(nombreComponente+"Der");
  if(desde.value.length == 12 || desde.value.length == 13){
	  cargaElemento(desde,hasta);
  }else{
	  jAlert("El código del elemento debe ser de 12 o 13 dígitos.","Código Erróneo");
  }
}
function rightToLeft(nombreComponente)
{
  var desde=document.getElementById(nombreComponente+"Der");
  var hasta=document.getElementById(nombreComponente+"Izq");
  pasar(desde,hasta);
}
function cargaElemento(desde,hasta)
{
	if(desde.value != null && desde.value != "" && desde.value.replace(/^(\s|\&nbsp;)*|(\s|\&nbsp;)*$/g,"") != "" && parseInt(desde.value)!= 0){
      var opcionDesde=desde;
      var opcionHasta=new Option(opcionDesde.value,opcionDesde.value,"defaultSelected");
  /*agregamos la opcion en hasta*/
  hasta.options[hasta.options.length]=opcionHasta;
  /*eliminamos la opcion de desde*/
      desde.value = null;
	}
}
function pasar(desde,hasta)
{
  var i=0;
  for (i=0;i<desde.options.length;i++)
  {
    if (desde.options[i].selected==true)
    {
    /*eliminamos la opcion de desde*/
      desde.options[i]=null;
      i--;
    }
  }
}