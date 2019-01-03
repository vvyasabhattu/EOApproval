package com.evoke.core.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Users", namespace = "http://schemas.datacontract.org/2004/07/FauxActiveDirectory.Server.Wcf.Models")
@XmlAccessorType(XmlAccessType.FIELD)
public class UsersBean {
	
	@XmlElement(name = "User", namespace = "http://schemas.datacontract.org/2004/07/FauxActiveDirectory.Server.Wcf.Models")
	private List<UserBean> userBeanList;

	public List<UserBean> getUserBeanList() {
		return userBeanList;
	}

	public void setUserBeanList(List<UserBean> userBeanList) {
		this.userBeanList = userBeanList;
	}
	

}
