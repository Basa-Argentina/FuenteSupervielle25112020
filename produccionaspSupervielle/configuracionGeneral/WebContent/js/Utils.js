/**
 * Funcion para validar el CUIT - CUIL
 * 
 */

function validarCuit(S) {
	// Acepta el formato XX-XXXXXXXX-X
	if (!(S.match(/^\d{2}\-\d{8}\-\d{1}$/))) {
		jAlert("El Cuil/Cuit es incorrecto. Revisar","Información");
		return (false);
	}
	var vec = new Array(10);
	esCuit = false;
	cuit_rearmado = "";
	errors = '';
	for (i = 0; i < S.length; i++) {
		caracter = S.charAt(i);
		if (caracter.charCodeAt(0) >= 48 && caracter.charCodeAt(0) <= 57) {
			cuit_rearmado += caracter;
		}
	}
	S = cuit_rearmado;
	if (S.length != 11) { // si to estan todos los digitos
		esCuit = false;
		errors = 'Cuit <11 ';
		jAlert("CUIT Menor a 11 Caracteres","Información");
	} else {
		x = i = dv = 0;
		// Multiplico los dígitos.
		vec[0] = S.charAt(0) * 5;
		vec[1] = S.charAt(1) * 4;
		vec[2] = S.charAt(2) * 3;
		vec[3] = S.charAt(3) * 2;
		vec[4] = S.charAt(4) * 7;
		vec[5] = S.charAt(5) * 6;
		vec[6] = S.charAt(6) * 5;
		vec[7] = S.charAt(7) * 4;
		vec[8] = S.charAt(8) * 3;
		vec[9] = S.charAt(9) * 2;

		// Suma cada uno de los resultado.
		for (i = 0; i <= 9; i++) {
			x += vec[i];
		}
		dv = (11 - (x % 11)) % 11;
		if (dv == S.charAt(10)) {
			esCuit = true;
		}
	}
	if (!esCuit) {
		jAlert("CUIT Invalido","Información");
		errors = 'Cuit Invalido ';
	}
	return esCuit;
}

/**
 * Funcion para validar la hora HH:MM
 */
function CheckTime(str) {
	hora = str.value;
	if (hora == '') {
		return
	}
	if (hora.length > 5) {
		jAlert("Introdujo una cadena mayor a 5 caracteres","Información");
		return
	}
	if (hora.length != 5) {
		jAlert("Introducir HH:MM","Información");
		return
	}
	a = hora.charAt(0); // <=2
	b = hora.charAt(1); // <=4
	c = hora.charAt(2); // :
	d = hora.charAt(3); // <=5
	e = hora.charAt(4); // <=9
	//Verificamos que sean numeros
	if(isNaN(a) || isNaN(b) || isNaN(d) || isNaN(e)){
		jAlert("Introducir HH:MM","Información");
		return
	}
		
	if ((a == 2 && b > 3) || (a > 2)) {
		jAlert("El valor que introdujo en la Hora no corresponde, introduzca un digito entre 00 y 23","Información");
		return
	}
	
	if (d > 5) {
		jAlert("El valor que introdujo en los minutos no corresponde, introduzca un digito entre 00 y 59","Información");
		return
	}
	if (e > 9) {
		jAlert("El valor que introdujo en los minutos no corresponde, introduzca un digito entre 00 y 59","Información");
		return
	}
	
	if (c != ':') {
		jAlert("Introduzca el caracter ':' para separar la hora, los minutos y los segundos","Información");
		return
	}

}