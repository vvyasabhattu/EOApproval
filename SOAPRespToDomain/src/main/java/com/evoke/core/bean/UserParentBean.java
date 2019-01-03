package com.evoke.core.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetGroupsResult", namespace = "http://tempuri.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserParentBean {
	
	@XmlElement(name = "Group", namespace = "http://schemas.datacontract.org/2004/07/FauxActiveDirectory.Server.Wcf.Models")
	private List<UserGroupBean> group;
	
	public List<UserGroupBean> getGroup() {
		return group;
	}

	public void setGroup(List<UserGroupBean> group) {
		this.group = group;
	}


}
