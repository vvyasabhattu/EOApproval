package com.evoke.core.bean;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetExcessObsoleteRequestResult", namespace = "http://tempuri.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class EOBean {
	
	@XmlElement(name = "Comments", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private EOCOMMENTAllBean eoComments;
	
	@XmlElement(name = "Header", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private EOHEADERBean eoHEADER;
	
	
	@XmlElement(name = "Parts", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private EOPARTFAllBean eoPARTF;
	
	@XmlElement(name = "Routings", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private EOAPPRVLAllBean eoRouting;
	

	public EOPARTFAllBean getEPARTF() {
		return eoPARTF;
	}

	public EOAPPRVLAllBean getEoRouting() {
		return eoRouting;
	}

	public void setEoRouting(EOAPPRVLAllBean eoRouting) {
		this.eoRouting = eoRouting;
	}

	public void setEPARTF(EOPARTFAllBean eoPartList) {
		this.eoPARTF = eoPartList;
	}

	public EOHEADERBean getEoHEADER() {
		return eoHEADER;
	}

	public void setEoHEADER(EOHEADERBean eoHEADER) {
		this.eoHEADER = eoHEADER;
	}

	public EOCOMMENTAllBean getEoComments() {
		return eoComments;
	}

	public void setEoComments(EOCOMMENTAllBean eoComments) {
		this.eoComments = eoComments;
	}
	
	

}
