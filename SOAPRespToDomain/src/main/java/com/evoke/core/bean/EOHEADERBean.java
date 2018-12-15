package com.evoke.core.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evoke.core.utils.Key;

@XmlRootElement(name = "GetHeaderResult", namespace = "http://tempuri.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class EOHEADERBean
{
	@XmlElement(name = "DateInitiated", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
    private String EOINDT;
	
	@XmlElement(name = "PlantName", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
    private String EOPLT;
	
	@XmlElement(name = "Description", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
    private String EOPDSC;

	@XmlElement(name = "HighestDollarValue", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
    private String EOHDV;

	@XmlElement(name = "TotalValue", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
    private String EOTOTV;

	@XmlElement(name = "Originator", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
    private String EOORG;

	@XmlElement(name = "DocumentLink", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
    private String EODLCK;

	@XmlElement(name = "ECN", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
    private String EOECN;

	@XmlElement(name = "Status", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
    private String EOSTS;

	@XmlElement(name = "ControllingUser", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
    private String EOCUSR;

	@XmlElement(name = "EmailLink", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
    private String EOLINK;
    
    @XmlElement(name = "Key", namespace = "http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models")
	private Key key;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

    public void setEOINDT(String EOINDT) {
        this.EOINDT = EOINDT;
    }

    public String getEOINDT() {
        return EOINDT;
    }

    public void setEOPLT(String EOPLT) {
        this.EOPLT = EOPLT;
    }

    public String getEOPLT() {
        return EOPLT;
    }

    public void setEOPDSC(String EOPDSC) {
        this.EOPDSC = EOPDSC;
    }

    public String getEOPDSC() {
        return EOPDSC;
    }

    public void setEOHDV(String EOHDV) {
        this.EOHDV = EOHDV;
    }

    public String getEOHDV() {
        return EOHDV;
    }

    public void setEOTOTV(String EOTOTV) {
        this.EOTOTV = EOTOTV;
    }

    public String getEOTOTV() {
        return EOTOTV;
    }

    public void setEOORG(String EOORG) {
        this.EOORG = EOORG;
    }

    public String getEOORG() {
        return EOORG;
    }

    public void setEODLCK(String EODLCK) {
        this.EODLCK = EODLCK;
    }

    public String getEODLCK() {
        return EODLCK;
    }

    public void setEOECN(String EOECN) {
        this.EOECN = EOECN;
    }

    public String getEOECN() {
        return EOECN;
    }

    public void setEOSTS(String EOSTS) {
        this.EOSTS = EOSTS;
    }

    public String getEOSTS() {
        return EOSTS;
    }

    public void setEOCUSR(String EOCUSR) {
        this.EOCUSR = EOCUSR;
    }

    public String getEOCUSR() {
        return EOCUSR;
    }

    public void setEOLINK(String EOLINK) {
        this.EOLINK = EOLINK;
    }

    public String getEOLINK() {
        return EOLINK;
    }

}