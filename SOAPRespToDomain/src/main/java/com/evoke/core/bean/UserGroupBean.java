package com.evoke.core.bean;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Group", namespace = "http://schemas.datacontract.org/2004/07/FauxActiveDirectory.Server.Wcf.Models")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserGroupBean {
	
	@XmlElement(name = "Key", namespace = "http://schemas.datacontract.org/2004/07/FauxActiveDirectory.Server.Wcf.Models")
	private GroupKey key;

	@XmlElement(name = "Users", namespace = "http://schemas.datacontract.org/2004/07/FauxActiveDirectory.Server.Wcf.Models")
	private UsersBean usersBean;
	
	
	public UsersBean getUsersBean() {
		return usersBean;
	}

	public void setUsersBean(UsersBean usersBean) {
		this.usersBean = usersBean;
	}

	public GroupKey getKey() {
		return key;
	}

	public void setKey(GroupKey key) {
		this.key = key;
		
	}
	
	

	

}
