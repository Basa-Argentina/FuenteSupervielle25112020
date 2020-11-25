package com.security.recursos;

import java.text.NumberFormat;
import java.util.Locale;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 
 * @author Federico Muñoz
 *
 */
public class NumberFormatTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private static NumberFormat nf;
	static{
		nf = NumberFormat.getInstance(new Locale("es"));
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		nf.setGroupingUsed(true);
	}
    private String value;
    
    private String tipoValor = "d";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTipoValor() {
		return tipoValor;
	}

	public void setTipoValor(String tipoValor) {
		this.tipoValor = tipoValor;
	}

	/**
     * ver doStartTag en TagSupport
     * @return int
     * @throws JspException
     */
    public int doStartTag() throws JspException {
        try {
        	String formatedValue = null;
        	if("d".equals(tipoValor)){
        		formatedValue = nf.format(new Double(value));
        	}if("f".equals(tipoValor)){
        		formatedValue = nf.format(new Float(value));
        	}
            pageContext.getOut().print(formatedValue);
        } catch (Exception ex) {
        }
        return SKIP_BODY;
    }

    /**
     * ver release en TagSupport
     */
    public void release() {
    	value = null;
    }
}
