package com.dividato.security.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
public class CambiarIdioma {
	
	private String[] urlFrom = {"filtrarAuthority.html","filtrarGroup.html","filtrarUser.html"};
	private String[] urlTo = {"mostrarAuthority.html","mostrarGroup.html","mostrarUser.html"};
	
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
		String url = request.getHeader("Referer"); 
		String[] urlCortadas = url.split("/");
		String salida = urlCortadas[urlCortadas.length - 1];
		//return "redirect:index.html";
		if(salida.indexOf(".html")!=-1){
			String buscar = buscarString(salida);
			if(!"".equals(buscar))
				return "redirect:"+buscar;
			else
				return "redirect:"+salida;
		}
		else
			return "redirect:index.html";
	}
	
	private String buscarString(String from){
		int i= 0;
		for(String buscar:urlFrom){
			if(buscar.equals(from)){
				return urlTo[i];
			}
			i++;
		}
		return "";
	}
}
