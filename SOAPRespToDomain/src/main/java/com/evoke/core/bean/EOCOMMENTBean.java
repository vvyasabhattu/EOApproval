
package com.evoke.core.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evoke.core.utils.Key;

@XmlRootElement(name = "Comment", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
@XmlAccessorType(XmlAccessType.FIELD)
public class EOCOMMENTBean {

	@XmlElement(name = "Text", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private String EOCOMM;

	@XmlElement(name = "Key", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private Key key;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public void setEOCOMM(String EOCOMM) {
		this.EOCOMM = EOCOMM;
	}

	public String getEOCOMM() {
		return EOCOMM;
	}

}
