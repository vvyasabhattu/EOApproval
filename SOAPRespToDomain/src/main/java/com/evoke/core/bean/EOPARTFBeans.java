package com.evoke.core.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Parts", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
@XmlAccessorType(XmlAccessType.FIELD)
public class EOPARTFAllBean {
	
	@XmlElement(name = "Part", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private List<EOPARTFBean> eoPARTFBean;
	
	public List<EOPARTFBean> getEOPARTFBean() {
		return eoPARTFBean;
	}

	public void setEOPARTFBean(List<EOPARTFBean> eoPARTFBean) {
		this.eoPARTFBean = eoPARTFBean;
	}

}
