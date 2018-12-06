package com.evoke.core.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evoke.core.utils.Key;

@XmlRootElement(name = "Routing", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
@XmlAccessorType(XmlAccessType.FIELD)
public class EOAPPRVLBean {

	@XmlElement(name = "Authority", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private String EOAUTH;

	@XmlElement(name = "ApproverName", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private String EONAME;

	@XmlElement(name = "ApprovalDate", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private String EODATE;

	@XmlElement(name = "Key", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private Key key;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getEODATE() {
		return EODATE;
	}

	public void setEODATE(String eODATE) {
		EODATE = eODATE;
	}

	public void setEOAUTH(String EOAUTH) {
		this.EOAUTH = EOAUTH;
	}

	public String getEOAUTH() {
		return EOAUTH;
	}

	public void setEONAME(String EONAME) {
		this.EONAME = EONAME;
	}

	public String getEONAME() {
		return EONAME;
	}

}
