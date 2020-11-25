$(document).ready(function() {			
	//Tooltips
	$("img[title]").tooltip();
	
	//Inicio menu
	$("ul.topnav li").hover(
		function() {
			$(this).addClass("hover");
			$(this).find("ul.subnav").slideDown('fast').show(); //Drop down the subnav on click  
		}, 
		function(){  
			$(this).removeClass("hover");
			$(this).find("ul.subnav").slideUp('fast'); //When the mouse hovers out of the subnav, move it back up  
		}
    );  
	//Fin menu
});

function abrirPopup(claseNom){
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}
