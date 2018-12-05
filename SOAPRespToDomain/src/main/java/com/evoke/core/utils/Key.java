package com.evoke.core.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Key", namespace="http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
@XmlAccessorType(XmlAccessType.FIELD)
public class Key {

	
	@XmlElement(name = "ControlNumber", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private String ControlNumber;

	public String getControlNumber() {
		return ControlNumber;
	}

	public void setControlNumber(String controlNumber) {
		ControlNumber = controlNumber;
	}
	
}
