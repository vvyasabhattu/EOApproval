package com.evoke.core.bean;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetExcessObsoleteRequestResult", namespace = "http://tempuri.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class EOBean {
	
	@XmlElement(name = "Comments", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private EOCOMMENTBeans eoComments;
	
	@XmlElement(name = "Header", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private EOHEADERBean eoHEADER;
	
	
	@XmlElement(name = "Parts", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private EOPARTFBeans eoPARTF;
	
	@XmlElement(name = "Routings", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private EOAPPRVLBeans eoRouting;
	

	public EOPARTFBeans getEPARTF() {
		return eoPARTF;
	}

	public EOAPPRVLBeans getEoRouting() {
		return eoRouting;
	}

	public void setEoRouting(EOAPPRVLBeans eoRouting) {
		this.eoRouting = eoRouting;
	}

	public void setEPARTF(EOPARTFBeans eoPartList) {
		this.eoPARTF = eoPartList;
	}

	public EOHEADERBean getEoHEADER() {
		return eoHEADER;
	}

	public void setEoHEADER(EOHEADERBean eoHEADER) {
		this.eoHEADER = eoHEADER;
	}

	public EOCOMMENTBeans getEoComments() {
		return eoComments;
	}

	public void setEoComments(EOCOMMENTBeans eoComments) {
		this.eoComments = eoComments;
	}
	
	

}
