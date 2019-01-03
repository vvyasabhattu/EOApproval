package com.evoke.core.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Key", namespace="http://schemas.datacontract.org/2004/07/FauxActiveDirectory.Server.Wcf.Models")
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupKey {
	
	@XmlElement(name = "Name", namespace = "http://schemas.datacontract.org/2004/07/FauxActiveDirectory.Server.Wcf.Models")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
 