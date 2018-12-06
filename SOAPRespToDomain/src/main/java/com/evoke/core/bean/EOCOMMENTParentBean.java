package com.evoke.core.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetCommentsResult", namespace = "http://tempuri.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class EOCOMMENTParentBean {

	

	@XmlElement(name = "Comment", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private List<EOCOMMENTBean> eoCOMMENTBean;
	
	public List<EOCOMMENTBean> getEOCOMMENTBean() {
		return eoCOMMENTBean;
	}

	public void setEOCOMMENTBean(List<EOCOMMENTBean> eoPARTFBean) {
		this.eoCOMMENTBean = eoPARTFBean;
	}

}
