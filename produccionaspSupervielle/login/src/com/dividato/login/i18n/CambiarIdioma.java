package com.dividato.login.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
public class CambiarIdioma {
	@RequestMapping("/cambiarIdioma.html")
	public String cambiarIdioma(
			@RequestParam("siteLanguage")String siteLanguage,
			HttpServletRequest request,
			HttpServletResponse response) {
		RequestContextUtils.getLocaleResolver(request).
			setLocale(
					request, 
					response, 
					new Locale(siteLanguage));
		return "redirect:index.html";
	}
}
