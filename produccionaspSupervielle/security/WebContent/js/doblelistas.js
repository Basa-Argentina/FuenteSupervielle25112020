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
   pasar(desde,hasta);
 }
 function rightToLeft(nombreComponente)
 {
   var desde=document.getElementById(nombreComponente+"Der");
   var hasta=document.getElementById(nombreComponente+"Izq");
   pasar(desde,hasta);
 }
 function pasar(desde,hasta)
 {
   var i=0;
   for (i=0;i<desde.options.length;i++)
   {
     if (desde.options[i].selected==true)
     {
       var opcionDesde=desde.options[i];
       var opcionHasta=new Option(opcionDesde.text,opcionDesde.value,"defaultSelected");
   /*agregamos la opcion en hasta*/
   hasta.options[hasta.options.length]=opcionHasta;
   /*eliminamos la opcion de desde*/
       desde.options[i]=null;
       i--;
     }
   }
 }