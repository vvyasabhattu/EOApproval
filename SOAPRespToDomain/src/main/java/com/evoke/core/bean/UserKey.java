package com.evoke.core.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Key", namespace="http://schemas.datacontract.org/2004/07/FauxActiveDirectory.Server.Wcf.Models")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserKey {
	
	@XmlElement(name = "EmployeeNumber", namespace = "http://schemas.datacontract.org/2004/07/FauxActiveDirectory.Server.Wcf.Models")
	private String employeeNumber;

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}


}
