openLink = function() {
	var language = window.navigator.language;
	var languageFirstTwo = language.substr(0, 2);
	var redirectLink;
	switch (languageFirstTwo) {
		case "es":
			redirectLink = "privacy-policy-es.html";
		break;
		case "fr":
			redirectLink = "privacy-policy-fr.html";
		break;
		default:
			redirectLink = "privacy-policy-en.html";
	}
	window.location.href = redirectLink;
}

openEmail = function() {
	if (/Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.userAgent) ||
	/Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.platform)) {
		window.open("mailto:raumar95@gmail.com", "_blank");
	} else {
		window.open("https://mail.google.com/mail/?view=cm&fs=1&tf=1&to=raumar95@gmail.com", "_blank");
	}
}
