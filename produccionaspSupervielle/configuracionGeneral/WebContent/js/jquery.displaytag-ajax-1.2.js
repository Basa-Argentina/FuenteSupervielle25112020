/* 
 * Copyright (c) 2010 CompuCloud Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * Author: Jevon Gill
 */
(function($){

    $.fn.displayTagAjax = function() {
        var links = new Array();
        var container = this;
        addItemsToArray(this.find("table .sortable a"),links);
        addItemsToArray(this.find(".pagelinks a"),links);
        $.each(links,function()
            {
                    var url = $(this).attr("href");
                    addClickEvent(container, this,url);
                    $(this).removeAttr("href");
            }
        );
        //agrego el estilo
        this.find("table tbody tr").mouseover(function(){
    		$(this).addClass('tr_mouseover');			
    	});
        this.find("table tbody tr").mouseout(function(){
    		$(this).removeClass('tr_mouseover');
    	});	
        this.find("table tbody tr").dblclick(function(){
        	var valorSel = $(this).find('.hdn_reference').html();
        	var valorSel2 = $(this).find('.hdn_reference2').html();
        	var idCompHtml = $(this).find('#hdn_referenceHtml').val();
        	$("#"+idCompHtml).val(valorSel);      	
        	$("#"+idCompHtml+"Label").html(valorSel2);      	
        	popupOffDiv($('.'+popupClaseNombre),'darkLayer');
        	//fix for popUpSeleccionLoteRearchivoDigital.html
        	if(idCompHtml=="codigoLoteRearchivoSeleccionado"){
        		document.forms[0].submit();
        	}
        	//fix for formularioTransferenciaContenedor.jsp
        	if($("#triggerBlurAfterSelectItemInPopUp").val()=="true"){
        		$("#"+idCompHtml).trigger("blur");
        	}
        	$("#"+idCompHtml).focus();
        });	
        $('.inputSearch.'+popupClaseNombre).keydown(function(event) {
    		if (event.keyCode == '13') {
    			$('.buttonSearch.'+popupClaseNombre).click();
//    			buscarPopup($('.urlBusqueda.'+popupClaseNombre).val(), popupClaseNombre);
    		}	
    	});
    	$(document).keydown(function(event){
    		if (event.keyCode == '27') {
    			cancelarPopup();
    		}	  
    	});
    };

  function addClickEvent(ctx, element,url){
        $(element).click(
            function(){
                jQuery.ajax(
                {
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
                    context: ctx
                });
            }
        );
    }

   function addItemsToArray(items,array){
        $.each(items,function()
            {
            	array.push(this);
            }
        );        
    }
    
})(jQuery);



