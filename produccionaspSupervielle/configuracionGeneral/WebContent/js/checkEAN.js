function checkEan(eanCode) {
	// Check if only digits
	var ValidChars = "0123456789";
	for (i = 0; i < eanCode.length; i++) {
		digit = eanCode.charAt(i);
		if (ValidChars.indexOf(digit) == -1) {
			return false;
		}
		
	}
	// Add five 0 if the code has only 8 digits
	if (eanCode.length == 8 ) {
		eanCode = "00000" + eanCode;
	}
	// Check for 13 digits otherwise
	else if (eanCode.length != 13) {
		return false;
	}

	// Get the check number
	originalCheck = eanCode.substring(eanCode.length - 1);
	eanCode = eanCode.substring(0, eanCode.length - 1);

	// Add even numbers together
	even = Number(eanCode.charAt(1)) +
	       Number(eanCode.charAt(3)) +
	       Number(eanCode.charAt(5)) +
	       Number(eanCode.charAt(7)) +
	       Number(eanCode.charAt(9)) +
	       Number(eanCode.charAt(11));
	// Multiply this result by 3
	even *= 3;

	// Add odd numbers together
	odd = Number(eanCode.charAt(0)) +
	      Number(eanCode.charAt(2)) +
	      Number(eanCode.charAt(4)) +
	      Number(eanCode.charAt(6)) +
	      Number(eanCode.charAt(8)) +
	      Number(eanCode.charAt(10));

	// Add two totals together
	total = even + odd;

	// Calculate the checksum
    // Divide total by 10 and store the remainder
    checksum = total % 10;
    // If result is not 0 then take away 10
    if (checksum != 0) {
        checksum = 10 - checksum;
    }

	// Return the result
	if (checksum != originalCheck) {
		return false;
	}

    return true;
}