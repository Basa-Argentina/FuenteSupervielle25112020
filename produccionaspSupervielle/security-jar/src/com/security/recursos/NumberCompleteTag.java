package com.security.recursos;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 
 * @author Gonzalo Noriega
 *
 */
public class NumberCompleteTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private Integer length = 2;
	private Integer valorDefualt = 0;
	static{
	}
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getLength() {
		return length;
	}

	public  void setLength(Integer length) {
		this.length = length;
	}
	
	public Integer getValorDefualt() {
		return valorDefualt;
	}

	public void setValorDefualt(Integer valorDefualt) {
		this.valorDefualt = valorDefualt;
	}

	/**
     * ver doStartTag en TagSupport
     * @return int
     * @throws JspException
     */
    public int doStartTag() throws JspException {
        try {
        	if(value.isEmpty()){
        		value = valorDefualt.toString();
        	}
        	String formatedValue  = String.format("%0"+length.toString()+"d", new Integer(value));
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
