package com.evoke.core.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Comments", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
@XmlAccessorType(XmlAccessType.FIELD)
public class EOCOMMENTAllBean {

	@XmlElement(name = "Comment", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private List<EOCOMMENTBean> eoCommentBean;
	
	public List<EOCOMMENTBean> getEoCommentBean() {
		return eoCommentBean;
	}

	public void setEoCommentBean(List<EOCOMMENTBean> eoCommentBean) {
		this.eoCommentBean = eoCommentBean;
	}

}
