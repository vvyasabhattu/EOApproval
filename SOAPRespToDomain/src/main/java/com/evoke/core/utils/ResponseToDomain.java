package com.evoke.core.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;

import com.evoke.model.EOCOMENT;
import com.evoke.model.EOPARTF;
import com.evoke.core.bean.EOAPPRVLBean;
import com.evoke.core.bean.EOAPPRVLParentBean;
import com.evoke.core.bean.EOCOMMENTBean;
import com.evoke.core.bean.EOCOMMENTParentBean;
import com.evoke.core.bean.EOPARTFBean;
import com.evoke.core.bean.EOPARTFParentBean;
import com.evoke.model.EOAPPRVL;

public class ResponseToDomain {

	private static final Logger logger = Logger.getLogger(ResponseToDomain.class);

	public static String response = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" + "\r\n"
			+ "  <s:Header />\r\n" + "\r\n" + "  <s:Body>\r\n" + "\r\n"
			+ "    <GetRoutingsResponse xmlns=\"http://tempuri.org/\">\r\n" + "\r\n"
			+ "      <GetRoutingsResult xmlns:a=\"http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n"
			+ "\r\n" + "        <a:Routing>\r\n" + "\r\n"
			+ "          <a:ApprovalDate>2013-07-01T15:04:00</a:ApprovalDate>\r\n" + "\r\n"
			+ "          <a:ApproverName>Singla, Puneet</a:ApproverName>\r\n" + "\r\n"
			+ "          <a:Authority>Materials Support Supervisor</a:Authority>\r\n" + "\r\n" + "          <a:Key>\r\n"
			+ "\r\n" + "            <a:ControlNumber>TEST127</a:ControlNumber>\r\n" + "\r\n" + "          </a:Key>\r\n"
			+ "\r\n" + "        </a:Routing>\r\n" + "\r\n" + "        <a:Routing>\r\n" + "\r\n"
			+ "          <a:ApprovalDate>2013-07-01T15:04:00</a:ApprovalDate>\r\n" + "\r\n"
			+ "          <a:ApproverName>Singla, Puneet</a:ApproverName>\r\n" + "\r\n"
			+ "          <a:Authority>Division Materials Manager</a:Authority>\r\n" + "\r\n" + "          <a:Key>\r\n"
			+ "\r\n" + "            <a:ControlNumber>TEST127</a:ControlNumber>\r\n" + "\r\n" + "          </a:Key>\r\n"
			+ "\r\n" + "        </a:Routing>\r\n" + "\r\n" + "        <a:Routing>\r\n" + "\r\n"
			+ "          <a:ApprovalDate>2013-07-01T15:05:00</a:ApprovalDate>\r\n" + "\r\n"
			+ "          <a:ApproverName>Singla, Puneet</a:ApproverName>\r\n" + "\r\n"
			+ "          <a:Authority>Controller</a:Authority>\r\n" + "\r\n" + "          <a:Key>\r\n" + "\r\n"
			+ "            <a:ControlNumber>TEST127</a:ControlNumber>\r\n" + "\r\n" + "          </a:Key>\r\n" + "\r\n"
			+ "        </a:Routing>\r\n" + "\r\n" + "        <a:Routing>\r\n" + "\r\n"
			+ "          <a:ApprovalDate>2013-07-01T15:05:00</a:ApprovalDate>\r\n" + "\r\n"
			+ "          <a:ApproverName>Singla, Puneet</a:ApproverName>\r\n" + "\r\n"
			+ "          <a:Authority>Plant/Facility Manager</a:Authority>\r\n" + "\r\n" + "          <a:Key>\r\n"
			+ "\r\n" + "            <a:ControlNumber>TEST127</a:ControlNumber>\r\n" + "\r\n" + "          </a:Key>\r\n"
			+ "\r\n" + "        </a:Routing>\r\n" + "\r\n" + "        <a:Routing>\r\n" + "\r\n"
			+ "          <a:ApprovalDate>2013-07-15T15:22:00</a:ApprovalDate>\r\n" + "\r\n"
			+ "          <a:ApproverName>Singla, Puneet</a:ApproverName>\r\n" + "\r\n"
			+ "          <a:Authority>V.P. Manufacturing</a:Authority>\r\n" + "\r\n" + "          <a:Key>\r\n" + "\r\n"
			+ "            <a:ControlNumber>TEST127</a:ControlNumber>\r\n" + "\r\n" + "          </a:Key>\r\n" + "\r\n"
			+ "        </a:Routing>\r\n" + "\r\n" + "        <a:Routing>\r\n" + "\r\n"
			+ "          <a:ApprovalDate>2013-07-15T15:22:00</a:ApprovalDate>\r\n" + "\r\n"
			+ "          <a:ApproverName>Singla, Puneet</a:ApproverName>\r\n" + "\r\n"
			+ "          <a:Authority>V.P. Finance</a:Authority>\r\n" + "\r\n" + "          <a:Key>\r\n" + "\r\n"
			+ "            <a:ControlNumber>TEST127</a:ControlNumber>\r\n" + "\r\n" + "          </a:Key>\r\n" + "\r\n"
			+ "        </a:Routing>\r\n" + "\r\n" + "      </GetRoutingsResult>\r\n" + "\r\n"
			+ "    </GetRoutingsResponse>\r\n" + "\r\n" + "  </s:Body>\r\n" + "\r\n" + "</s:Envelope>";

	/**
	 * Get Parts Service
	 * 
	 * @param response
	 * @return
	 */
	public List<EOPARTF> responseToEOPARTF(String response) {

		List<EOPARTF> eoPARTFList = new ArrayList<EOPARTF>();
		XMLInputFactory xif = XMLInputFactory.newFactory();

		try {
			// converting response string to EOPARTFParentBean
			EOPARTFParentBean eoPARTFParentBean = responseStringToEOPARTFBean(response, xif);
			if (eoPARTFParentBean != null) {
				List<EOPARTFBean> eoPARTFBeanList = eoPARTFParentBean.getEoPARTFBean();
				// converting EOPARTFBean to EOPARTF object
				eoPARTFBeanToDomain(eoPARTFList, eoPARTFBeanList);
			}
			return eoPARTFList;
		} catch (Exception e) {
			logger.info("Exception at responseToEOPARTF method : " + e);
			return eoPARTFList;
		}
	}

	/**
	 * Get Comments
	 * 
	 * @param response
	 * @return
	 */
	public List<EOCOMENT> responseToEOCOMMENT(String response) {

		List<EOCOMENT> eoCOMMENTList = new ArrayList<EOCOMENT>();
		XMLInputFactory xif = XMLInputFactory.newFactory();

		try {
			// converting response string to EOCOMMENTBean
			EOCOMMENTParentBean eoCOMMENTParentBean = responseStringToEOCOMENTBean(response, xif);
			if (eoCOMMENTParentBean != null) {
				List<EOCOMMENTBean> eoCOMMENTBeanList = eoCOMMENTParentBean.getEOCOMMENTBean();
				// converting EOPARTFBean to EOPARTF object
				eoCOMMENTBeanToDomain(eoCOMMENTList, eoCOMMENTBeanList);
			}
			return eoCOMMENTList;
		} catch (Exception e) {
			logger.info("Exception at responseToEOCOMMENT method : " + e);
			return eoCOMMENTList;
		}
	}

	/**
	 * Get Approvals
	 * 
	 * @param response
	 * @return
	 */
	public List<EOAPPRVL> responseToEOAPPRVL(String response) {

		List<EOAPPRVL> eoAPPRVLList = new ArrayList<EOAPPRVL>();
		XMLInputFactory xif = XMLInputFactory.newFactory();

		try {
			// converting response string to EOCOMMENTBean
			EOAPPRVLParentBean eoAPPRVLParentBean = responseStringToEOAPPRVL(response, xif);
			if (eoAPPRVLParentBean != null) {
				List<EOAPPRVLBean> eoEOAPPRVLBeanList = eoAPPRVLParentBean.getEoAPPRVL();
				// converting EOPARTFBean to EOPARTF object
				eoAPPRVLBeanToDomain(eoAPPRVLList, eoEOAPPRVLBeanList);
			}
			return eoAPPRVLList;
		} catch (Exception e) {
			logger.info("Exception at responseToEOAPPRVL method : " + e);
			return eoAPPRVLList;
		}
	}

	private void eoAPPRVLBeanToDomain(List<EOAPPRVL> eoAPPRVLList,  List<EOAPPRVLBean> eoAPPRVLParentBeanList) {
		
		if (eoAPPRVLParentBeanList != null && eoAPPRVLParentBeanList.size() > 0) {
			logger.info("EOAPPRVLBeanList : " + eoAPPRVLParentBeanList.size());
			for (EOAPPRVLBean eoAPPRVLParentBean : eoAPPRVLParentBeanList) {
				EOAPPRVL eoAPPRVL = new EOAPPRVL();
				eoAPPRVL.setEOAUTH(eoAPPRVLParentBean.getEOAUTH());
				eoAPPRVL.setEODATE(eoAPPRVLParentBean.getEODATE());
				eoAPPRVL.setEONAME(eoAPPRVLParentBean.getEONAME());
				eoAPPRVL.setEONUM(eoAPPRVLParentBean.getKey().getControlNumber());
				eoAPPRVLList.add(eoAPPRVL);
			}

			logger.info("EOAPPRVLList : " + eoAPPRVLList.size());
		}
	}

	private EOAPPRVLParentBean responseStringToEOAPPRVL(String response, XMLInputFactory xif)
			throws XMLStreamException, TransformerConfigurationException, TransformerFactoryConfigurationError,
			TransformerException, JAXBException {
		StringReader sr = responseTransformation(response, xif);
		JAXBContext jaxbContext = JAXBContext.newInstance(EOAPPRVLParentBean.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		// converting soap response to EOPARTFParentBean objecteoPARTFParentBean
		EOAPPRVLParentBean eoAPPRVLParentBean = (EOAPPRVLParentBean) unmarshaller.unmarshal(sr);

		return eoAPPRVLParentBean;
	}

	private void eoCOMMENTBeanToDomain(List<EOCOMENT> eoCOMMENTList, List<EOCOMMENTBean> eoCOMMENTBeanList) {
		if (eoCOMMENTBeanList != null && eoCOMMENTBeanList.size() > 0) {
			logger.info("EOCOMMENTBeanList : " + eoCOMMENTBeanList.size());
			for (EOCOMMENTBean eoPARTFBean : eoCOMMENTBeanList) {
				EOCOMENT eoCOMENT = new EOCOMENT();
				eoCOMENT.setEOCOMM(eoPARTFBean.getEOCOMM());
				eoCOMENT.setEONUM(eoPARTFBean.getKey().getControlNumber());
				eoCOMENT.setEOINDX(eoPARTFBean.getKey().getIndex());
				eoCOMMENTList.add(eoCOMENT);
			}

			logger.info("EOCOMMENTList : " + eoCOMMENTList.size());
		}
	}

	private EOPARTFParentBean responseStringToEOPARTFBean(String response, XMLInputFactory xif)
			throws XMLStreamException, TransformerConfigurationException, TransformerFactoryConfigurationError,
			TransformerException, JAXBException {
		StringReader sr = responseTransformation(response, xif);
		JAXBContext jaxbContext = JAXBContext.newInstance(EOPARTFParentBean.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		// converting soap response to EOPARTFParentBean objecteoPARTFParentBean
		EOPARTFParentBean eoPARTFParentBean = (EOPARTFParentBean) unmarshaller.unmarshal(sr);

		return eoPARTFParentBean;
	}

	private EOCOMMENTParentBean responseStringToEOCOMENTBean(String response, XMLInputFactory xif)
			throws XMLStreamException, TransformerConfigurationException, TransformerFactoryConfigurationError,
			TransformerException, JAXBException {
		StringReader sr = responseTransformation(response, xif);
		JAXBContext jaxbContext = JAXBContext.newInstance(EOCOMMENTParentBean.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		// converting soap response to EOPARTFParentBean objecteoPARTFParentBean
		EOCOMMENTParentBean eoCOMMENTParentBean = (EOCOMMENTParentBean) unmarshaller.unmarshal(sr);

		return eoCOMMENTParentBean;
	}

	private StringReader responseTransformation(String response, XMLInputFactory xif) throws XMLStreamException,
			TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(response));
		xsr.nextTag(); // Advance to Envelope tag
		xsr.nextTag(); // Advance to Header tag
		xsr.nextTag(); // Advance to Body tag
		xsr.nextTag(); // Advance to GetPartsResponse tag
		xsr.nextTag(); // Advance to GetPartsResult tag
		xsr.nextTag();

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		StringWriter stringWriter = new StringWriter();
		transformer.transform(new StAXSource(xsr), new StreamResult(stringWriter));
		StringReader sr = new StringReader(stringWriter.toString());

		return sr;
	}

	private void eoPARTFBeanToDomain(List<EOPARTF> eoPARTFList, List<EOPARTFBean> eoPARTFBeanList) {
		if (eoPARTFBeanList != null && eoPARTFBeanList.size() > 0) {
			logger.info("EOPARTFBeanList : " + eoPARTFBeanList.size());
			for (EOPARTFBean eoPARTFBean : eoPARTFBeanList) {
				EOPARTF eoPARTF = new EOPARTF();
				eoPARTF.setEOCNUM(eoPARTFBean.getKey().getControlNumber());
				eoPARTF.setEODESC(eoPARTFBean.getEODESC());
				eoPARTF.setEOMPNR(eoPARTFBean.getEOMPNR());
				eoPARTF.setEOOHQT(eoPARTFBean.getEOOHQT());
				eoPARTF.setEOPART(eoPARTFBean.getEOPART());
				eoPARTF.setEOSCQT(eoPARTFBean.getEOSCQT());
				eoPARTF.setEOTAQT(eoPARTFBean.getEOTAQT());
				eoPARTF.setEOTCST(eoPARTFBean.getEOTCST());
				eoPARTF.setEOUCST(eoPARTFBean.getEOUCST());
				eoPARTF.setEOVCOM(eoPARTFBean.getEOVCOM());
				eoPARTF.setEOVEND(eoPARTFBean.getEOVEND());
				eoPARTF.setEOVNAM(eoPARTFBean.getEOVNAM());
				eoPARTFList.add(eoPARTF);
			}

			logger.info("EOPARTFList : " + eoPARTFList.size());
		}
	}

	public static void main(String[] args) {
		ResponseToDomain responseToDomain = new ResponseToDomain();
		List<EOAPPRVL> responseToEOCOMMENT = responseToDomain.responseToEOAPPRVL(response);
		for (EOAPPRVL eocoment : responseToEOCOMMENT) {
			logger.info("getEOAUTH : " + eocoment.getEOAUTH());
			logger.info("getEODATE : " + eocoment.getEODATE());
			logger.info("getEONAME : " + eocoment.getEONAME());
			logger.info("getEONUM : " + eocoment.getEONUM());
		}
	}

}
