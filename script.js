var language = window.navigator.language;
var languageFirstTwo = language.substr(0, 2);

openLink = function() {
	var redirectLink;
	switch (languageFirstTwo) {
		case "es":
			redirectLink = "privacy-policy-es.html";
		break;
		case "fr":
			redirectLink = "privacy-policy-fr.html";
		break;
		case "pt":
			redirectLink = "privacy-policy-pt.html";
		break;
		default:
			redirectLink = "privacy-policy-en.html";
	}
	window.location.href = redirectLink;
}

openEmail = function() {
	if (/Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.userAgent) ||
	/Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.platform)) {
		window.location.href = "mailto:raumar95@gmail.com";
	} else {
		var confirmGmail;
		switch (languageFirstTwo) {
			case "es":
				confirmGmail = "¿Quieres enviar un correo electrónico desde tu cuenta de Gmail?";
			break;
			case "fr":
				confirmGmail = "Voulez-vous envoyer un e-mail à partir de votre compte Gmail?";
			break;
			case "pt":
				confirmGmail = "Você deseja enviar um e-mail da sua conta do Gmail?";
			break;
			default:
				confirmGmail = "Do you want to send an email from your Gmail account?";
		}
		if (window.confirm(confirmGmail)) {
			window.open("https://mail.google.com/mail/?view=cm&fs=1&tf=1&to=raumar95@gmail.com", "_blank");
		} else {
			window.location.href = "mailto:raumar95@gmail.com";
		}
	}
}
