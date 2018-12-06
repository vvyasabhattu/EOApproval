package com.evoke.core.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetRoutingsResult", namespace = "http://tempuri.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class EOAPPRVLParentBean {

	@XmlElement(name = "Routing", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private List<EOAPPRVLBean> eoAPPRVL;

	public List<EOAPPRVLBean> getEoAPPRVL() {
		return eoAPPRVL;
	}

	
	public void setEoAPPRVL(List<EOAPPRVLBean> eoAPPRVL) {
		this.eoAPPRVL = eoAPPRVL;
	}

}
