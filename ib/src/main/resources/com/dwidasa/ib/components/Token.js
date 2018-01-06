function updateChallenge(accountNumber) {
	var padding = "00000000" + accountNumber;
	var challenge = padding.substring(padding.length-8, padding.length);
	var display = challenge.substring(0, 3) + " " + challenge.substring(3, 6) + " " + challenge.substring(6);
	$('tokenChallenge').innerHTML = display;
}

