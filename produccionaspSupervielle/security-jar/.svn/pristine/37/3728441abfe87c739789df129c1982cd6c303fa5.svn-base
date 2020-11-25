package com.security.servicios;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;

import com.security.accesoDatos.interfaz.ParameterService;
import com.security.modelo.seguridad.Parameter;



/**
 * Esta clase centraliza el envío de mails.
 * Puede ser configurada como bean en spring.
 *  
 * @author Federico Muñoz
 *
 */
public class MailManager {
	private ParameterService service;
	/**
	 * setea el servicio para obtener los parámetros
	 * @param service
	 */
	@Autowired
	public void setServices(ParameterService service){
		this.service=service;
	}
	/**
	 * Envía el mensaje.
	 * 
	 * @param para
	 * @param subject
	 * @param cuerpo
	 * @throws MessagingException
	 */
    public void enviar (String para,String asunto,String cuerpo,String sistema) throws MessagingException{
    	if(service==null){
    		throw new IllegalStateException("no fue configurado el servicio para obtener los parámetros.");
    	}
    	Parameter param=service.obtenerParametros();
    	if(param==null || param.getHostSMTP()==null || param.getHostSMTP().trim().length()==0){
    		throw new IllegalStateException("no fueron configurados los parámetros de envío de mail.");
    	}
    	String email = param.geteMailUserSMTP();
        String nombreUsuario = param.getUserSMTP();
        String claveAcceso = param.getPasswordSMTP();
        String protocolo = "smtp";
        String host = param.getHostSMTP();
        int puerto = param.getPortSMTP();
        boolean ssl = param.getEnableSSLSMTP()==1;
        Properties props = null;
        Session session;
        try{
            props = System.getProperties();
            props.put("mail.smtp.starttls.enable","true");
            if(ssl)
            	props.put("mail.smtps.auth", "true");
        }catch(SecurityException se){
        	se.printStackTrace();
        }
        session = Session.getDefaultInstance(props, null);
        Transport transport = session.getTransport(protocolo);
        transport.connect(host,puerto, nombreUsuario,claveAcceso);
        
        InternetAddress from = null;;
		try {
			from = new InternetAddress(email,sistema);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	InternetAddress paraAddress = new InternetAddress(para);
    	
    	Message msg = new MimeMessage(session);
        msg.setSentDate(new Date());
        msg.setFrom(from);
        msg.setReplyTo(new InternetAddress[]{from});
        msg.setRecipients(Message.RecipientType.BCC, new InternetAddress[]{from});
        msg.setSubject(asunto);
        msg.setContent(cuerpo, "text/html");

        transport.sendMessage(msg,new InternetAddress[]{paraAddress});
        	
    	transport.close();
    }
    
    public void enviarConAdjunto (String para, String copia, String asunto,String cuerpo, String nombreArchivo, ByteArrayInputStream archivo, String sistema) throws MessagingException{
    	if(service==null){
    		throw new IllegalStateException("no fue configurado el servicio para obtener los parámetros.");
    	}
    	Parameter param=service.obtenerParametros();
    	if(param==null || param.getHostSMTP()==null || param.getHostSMTP().trim().length()==0){
    		throw new IllegalStateException("no fueron configurados los parámetros de envío de mail.");
    	}
    	String email = param.geteMailUserSMTP();
        String nombreUsuario = param.getUserSMTP();
        String claveAcceso = param.getPasswordSMTP();
        String protocolo = "smtp";
        String host = param.getHostSMTP();
        int puerto = param.getPortSMTP();
        boolean ssl = param.getEnableSSLSMTP()==1;
        Properties props = null;
        Session session;
        try{
            props = System.getProperties();
            props.put("mail.smtp.starttls.enable","true");
            if(ssl)
            	props.put("mail.smtps.auth", "true");
        }catch(SecurityException se){
        	se.printStackTrace();
        }
        session = Session.getDefaultInstance(props, null);
        
        if (archivo != null){
        	MimeMultipart multiParte = new MimeMultipart();
        	
        	//mensaje
            BodyPart texto = new MimeBodyPart();
            texto.setText(cuerpo);
            multiParte.addBodyPart(texto);
            
            //adjunto
            try {
            	BodyPart adjunto = new MimeBodyPart();
            	ByteArrayDataSource ds = new ByteArrayDataSource(archivo, "application/x-zip-compressed");
                adjunto.setDataHandler(new DataHandler(ds));          
                adjunto.setFileName(nombreArchivo);
                multiParte.addBodyPart(adjunto);
            } catch (IOException e) {
            	throw new MessagingException("imposible adjuntar el archivo", e);
            }
            
            Transport transport = session.getTransport(protocolo);
            transport.connect(host,puerto, nombreUsuario,claveAcceso);
            
            InternetAddress from = null;;
            InternetAddress bcc = null;;
            //TODO: Validar to
            
    		try {
    			from = new InternetAddress(email,sistema);
    			if(copia!=null)
    				bcc= new InternetAddress(copia);
    		} catch (UnsupportedEncodingException e) {
    			throw new MessagingException("imposible enviar al destinatario: "+email, e);
    		}
        	InternetAddress paraAddress = new InternetAddress(para);
        	
        	Message msg = new MimeMessage(session);
            msg.setSentDate(new Date());
            msg.setFrom(from);
            msg.setReplyTo(new InternetAddress[]{from});
            msg.setRecipients(Message.RecipientType.BCC, new InternetAddress[]{from,bcc});
            msg.setSubject(asunto);
            msg.setContent(multiParte);

            transport.sendMessage(msg,new InternetAddress[]{paraAddress});
            	
        	transport.close();
        }
       
    }
}
