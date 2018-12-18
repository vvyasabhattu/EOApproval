package com.evoke.core.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Routings", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
@XmlAccessorType(XmlAccessType.FIELD)
public class EOAPPRVLAllBean {

	@XmlElement(name = "Routing", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private List<EOAPPRVLBean> eoAPPRVLBean;

	public List<EOAPPRVLBean> getEoAPPRVLBean() {
		return eoAPPRVLBean;
	}

	public void setEoAPPRVLBean(List<EOAPPRVLBean> eoAPPRVLBean) {
		this.eoAPPRVLBean = eoAPPRVLBean;
	}
	
	
}
