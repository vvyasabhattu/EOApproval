package com.evoke.core.utils;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetPartsResult", namespace = "http://tempuri.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class EOPARTFParentBean {
	
	@XmlElement(name = "Part", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private List<EOPARTFBean> eoPARTFBean;
	
	public List<EOPARTFBean> getEoPARTFBean() {
		return eoPARTFBean;
	}

	public void setEoPARTFBean(List<EOPARTFBean> eoPARTFBean) {
		this.eoPARTFBean = eoPARTFBean;
	}

}
