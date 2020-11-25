 function seleccionarTodos(nombreComponente)
 {
   var seleccionados=document.getElementById(nombreComponente+"Der");
   var sel="";
   var i=0;
   for (i=0;i<seleccionados.options.length;i++)
   {
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
   var combo=document.getElementById(nombreComponente+"Def");
   pasar(desde,hasta,true,combo);
 }
 function rightToLeft(nombreComponente)
 {
   var desde=document.getElementById(nombreComponente+"Der");
   var hasta=document.getElementById(nombreComponente+"Izq");
   var combo=document.getElementById(nombreComponente+"Def");
   pasar(desde,hasta,false,combo);
 }
 function pasar(desde,hasta,agregar,combo)
 {
   var i=0;
   for (i=0;i<desde.options.length;i++)
   {
     if (desde.options[i].selected==true)
     {
       var opcionDesde=desde.options[i];
	   /*agregamos la opcion en hasta*/
	   hasta.options[hasta.options.length]=new Option(opcionDesde.text,opcionDesde.value,"defaultSelected");
	   if(agregar)
		   combo.options[combo.options.length]=new Option(opcionDesde.text,opcionDesde.value,"defaultSelected");
	   /*eliminamos la opcion de desde*/
       desde.options[i]=null;
       if(!agregar)
    	   combo.options[i]=null;
       i--;
     }
   }
 }