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
        	parent.$.fancybox.close();
        	$("#"+idCompHtml).blur();
        });	
        $('.inputSearch.'+popupClaseNombre).keydown(function(event) {
    		if (event.keyCode == '13') {
    			$('.buttonSearch.'+popupClaseNombre).click();
    		}	
    	});
    	$(document).keydown(function(event){
    		if (event.keyCode == '27') {
    			//cancelarPopup();
    			parent.$.fancybox.close();
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



