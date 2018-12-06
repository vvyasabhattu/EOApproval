
package com.evoke.core.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evoke.core.utils.Key;

@XmlRootElement(name = "Part", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
@XmlAccessorType(XmlAccessType.FIELD)
public class EOPARTFBean {

	private Long persistenceId;
	private Long persistenceVersion;

	// EOCNUM
	private String EOCNUM;

	@XmlElement(name = "Key", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private Key key;

	@XmlElement(name = "PartNumber", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private String EOPART;

	@XmlElement(name = "PartDescription", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private String EODESC;

	@XmlElement(name = "MaterialPlanner", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private String EOMPNR;

	@XmlElement(name = "OnHandQuantity", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private Float EOOHQT;

	@XmlElement(name = "ScrapQuantity", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private Float EOSCQT;

	@XmlElement(name = "UnitCost", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private Float EOUCST;

	@XmlElement(name = "TotalCost", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private Float EOTCST;

	@XmlElement(name = "ThrowAwayQuantity", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private Float EOTAQT;

	@XmlElement(name = "Vendor", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private String EOVEND;

	@XmlElement(name = "VendorName", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private String EOVNAM;

	@XmlElement(name = "Comment", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private String EOVCOM;

	public EOPARTFBean() {
	}

	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}

	public Long getPersistenceId() {
		return persistenceId;
	}

	public void setPersistenceVersion(Long persistenceVersion) {
		this.persistenceVersion = persistenceVersion;
	}

	public Long getPersistenceVersion() {
		return persistenceVersion;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

	public void setEOPART(String EOPART) {
		this.EOPART = EOPART;
	}

	public String getEOPART() {
		return EOPART;
	}

	public void setEODESC(String EODESC) {
		this.EODESC = EODESC;
	}

	public String getEODESC() {
		return EODESC;
	}

	public void setEOMPNR(String EOMPNR) {
		this.EOMPNR = EOMPNR;
	}

	public String getEOMPNR() {
		return EOMPNR;
	}

	public void setEOOHQT(Float EOOHQT) {
		this.EOOHQT = EOOHQT;
	}

	public Float getEOOHQT() {
		return EOOHQT;
	}

	public void setEOSCQT(Float EOSCQT) {
		this.EOSCQT = EOSCQT;
	}

	public Float getEOSCQT() {
		return EOSCQT;
	}

	public void setEOUCST(Float EOUCST) {
		this.EOUCST = EOUCST;
	}

	public Float getEOUCST() {
		return EOUCST;
	}

	public void setEOTCST(Float EOTCST) {
		this.EOTCST = EOTCST;
	}

	public Float getEOTCST() {
		return EOTCST;
	}

	public void setEOTAQT(Float EOTAQT) {
		this.EOTAQT = EOTAQT;
	}

	public Float getEOTAQT() {
		return EOTAQT;
	}

	public void setEOVEND(String EOVEND) {
		this.EOVEND = EOVEND;
	}

	public String getEOVEND() {
		return EOVEND;
	}

	public void setEOVNAM(String EOVNAM) {
		this.EOVNAM = EOVNAM;
	}

	public String getEOVNAM() {
		return EOVNAM;
	}

	public void setEOVCOM(String EOVCOM) {
		this.EOVCOM = EOVCOM;
	}

	public String getEOVCOM() {
		return EOVCOM;
	}

	public String getEOCNUM() {
		return EOCNUM;
	}

	public void setEOCNUM(String eOCNUM) {
		EOCNUM = eOCNUM;
	}

}
