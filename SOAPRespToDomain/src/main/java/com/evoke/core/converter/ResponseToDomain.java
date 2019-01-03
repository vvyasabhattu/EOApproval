package com.evoke.core.converter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.evoke.core.bean.EOAPPRVLBean;
import com.evoke.core.bean.EOAPPRVLParentBean;
import com.evoke.core.bean.EOBean;
import com.evoke.core.bean.EOCOMMENTBean;
import com.evoke.core.bean.EOCOMMENTParentBean;
import com.evoke.core.bean.EOHEADERBean;
import com.evoke.core.bean.EOPARTFBean;
import com.evoke.core.bean.EOPARTFParentBean;
import com.evoke.core.bean.UserBean;
import com.evoke.core.bean.UserGroupBean;
import com.evoke.core.bean.UserParentBean;

import com.hysteryale.model.EOAPPRVL;
import com.hysteryale.model.EOCOMENT;
import com.hysteryale.model.EOHEADER;
import com.hysteryale.model.EOPARTF;

public class ResponseToDomain {

	private static final Logger logger = LoggerFactory.getLogger(ResponseToDomain.class);

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
	 * Get Parts Service
	 * 
	 * @param response
	 * @return
	 */
	public Map<String, List<UserBean>> responseToUserBean(String response) {
		
		XMLInputFactory xif = XMLInputFactory.newFactory();
		Map<String, List<UserBean>> usersMap = new HashMap<String, List<UserBean>>();

		try {
			// converting response string to EOPARTFParentBean
			UserParentBean userParentBean = responseStringToUserGroupList(response, xif);			
			if (userParentBean != null) {
				List<UserGroupBean> eoPARTFBeanList = userParentBean.getGroup();
				
				if(eoPARTFBeanList != null && eoPARTFBeanList.size() > 0) {
					for (UserGroupBean userGroupBean : eoPARTFBeanList) {						
						List<UserBean> userBeanList = userGroupBean.getUsersBean().getUserBeanList();
						usersMap.put(userGroupBean.getKey().getName(), userBeanList);
						/*if(userBeanList != null && userBeanList.size() > 0) {
							for (UserBean userBean : userBeanList) {								
								System.out.println("userGroupBean -getDomain--->"+userBean.getDomain());
								System.out.println("userGroupBean -employeeNumber--->"+userBean.getKey().getEmployeeNumber());
							}
						}*/
					}
				}
			}
			return usersMap;
		} catch (Exception e) {
			logger.info("Exception at responseToEOPARTF method : " + e);
			return usersMap;
		}
	}
	
	private UserParentBean responseStringToUserGroupList(String response, XMLInputFactory xif)
			throws XMLStreamException, TransformerConfigurationException, TransformerFactoryConfigurationError,
			TransformerException, JAXBException {
		// StringReader sr = responseTransformation(response, xif);		
		UserParentBean userParentBean = new UserParentBean();
		XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(response));
		xsr.nextTag();
		xsr.nextTag(); // Advance to Header tag
		xsr.nextTag(); // Advance to Body tag
		xsr.nextTag(); // Advance to
		if (response.contains("s:Header")) {
			xsr.nextTag(); // Advance to GetPartsResult tag
			xsr.nextTag();
		}
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		StringWriter stringWriter = new StringWriter();
		transformer.transform(new StAXSource(xsr), new StreamResult(stringWriter));
		StringReader sr = new StringReader(stringWriter.toString());
		JAXBContext jaxbContext = JAXBContext.newInstance(UserParentBean.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		userParentBean = (UserParentBean) unmarshaller.unmarshal(sr);

		return userParentBean;
	}

	
	
	
	/**
	 * Get EO Service
	 * 
	 * @param response
	 * @returnoi
	 */
	public Map<String, Object> responseToEOExess(String response) {
		List<EOCOMENT> eoCOMMENTList = new ArrayList<EOCOMENT>();
		List<EOPARTF> eoPARTFList = new ArrayList<EOPARTF>();
		List<EOAPPRVL> eoAPPRVLList = new ArrayList<>();
		EOHEADER eoHEADER = new EOHEADER();
		Map<String, Object> mapOfEOS = new HashMap<String, Object>();
		XMLInputFactory xif = XMLInputFactory.newFactory();

		try {
			// converting response string to EOPARTFParentBean
			EOBean eoBean = responseStringToEO(response, xif);
			if (eoBean != null) {
				List<EOPARTFBean> eoPartFBean = eoBean.getEPARTF().getEOPARTFBean();
				if (eoPartFBean != null && eoPartFBean.size() > 0) {
					eoPARTFBeanToDomain(eoPARTFList, eoPartFBean);
					System.out.println("eoPARTFList--" + eoPARTFList.size());
				}

				List<EOAPPRVLBean> eoAPPRVLBean = eoBean.getEoRouting().getEoAPPRVLBean();
				if (eoAPPRVLBean != null && eoAPPRVLBean.size() > 0) {
					eoAPPRVLBeanToDomain(eoAPPRVLList, eoAPPRVLBean);
					System.out.println("eoAPPRVLList--" + eoAPPRVLList.size());
				}

				List<EOCOMMENTBean> eoCommentBean = eoBean.getEoComments().getEoCommentBean();
				if (eoCommentBean != null && eoCommentBean.size() > 0) {
					eoCOMMENTBeanToDomain(eoCOMMENTList, eoCommentBean);
					System.out.println("eoCOMMENTList--" + eoCOMMENTList.size());
				}

				EOHEADERBean eoHEADERBean = eoBean.getEoHEADER();
				if (eoHEADER != null) {
					eoHEADERBeanToDomain(eoHEADER, eoHEADERBean);
					System.out.println("eoHEADER--" + eoHEADER.getEOCNUM());
				}

				mapOfEOS.put("eoPARTFList", eoPARTFList);
				mapOfEOS.put("eoAPPRVLList", eoAPPRVLList);
				mapOfEOS.put("eoCOMMENTList", eoCOMMENTList);
				mapOfEOS.put("eoHEADER", eoHEADER);

				logger.info("eoCOMMENTList--" + eoCOMMENTList.size());
			}
			return mapOfEOS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception at responseToEOPARTF method : " + e);
			return null;
		}

	}

	private EOBean responseStringToEO(String response, XMLInputFactory xif) {
		EOBean eoBean = new EOBean();

		try {
			XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(response));
			xsr.nextTag();
			xsr.nextTag(); // Advance to Header tag
			xsr.nextTag(); // Advance to Body tag
			xsr.nextTag(); // Advance to
			if (response.contains("s:Header")) {
				xsr.nextTag(); // Advance to GetPartsResult tag
				xsr.nextTag();
			}

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			StringWriter stringWriter = new StringWriter();
			transformer.transform(new StAXSource(xsr), new StreamResult(stringWriter));
			StringReader sr = new StringReader(stringWriter.toString());

			JAXBContext jaxbContext = JAXBContext.newInstance(EOBean.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			eoBean = (EOBean) unmarshaller.unmarshal(sr);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return eoBean;
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

	/**
	 * Get HEADER
	 * 
	 * @param response
	 * @return
	 */
	public EOHEADER responseToEOHEADER(String response) {

		EOHEADER eoHEADER = new EOHEADER();
		XMLInputFactory xif = XMLInputFactory.newFactory();

		try {
			// converting response string to EOCOMMENTBean
			EOHEADERBean eoHEADERBean = responseStringToEOHEADERBean(response, xif);
			if (eoHEADERBean != null) {

				eoHEADERBeanToDomain(eoHEADER, eoHEADERBean);
			}
			return eoHEADER;
		} catch (Exception e) {
			logger.info("Exception at responseToEOAPPRVL method : " + e);
			return eoHEADER;
		}
	}

	private void eoHEADERBeanToDomain(EOHEADER eoHEADER, EOHEADERBean eoHEADERBean) {

		eoHEADER.setEOCNUM(eoHEADERBean.getKey().getControlNumber());
		eoHEADER.setEOCUSR(eoHEADERBean.getEOCUSR());
		eoHEADER.setEODLCK(eoHEADERBean.getEODLCK());
		eoHEADER.setEOECN(eoHEADERBean.getEOECN());
		eoHEADER.setEOHDV(eoHEADERBean.getEOHDV());
		eoHEADER.setEOINDT(eoHEADERBean.getEOINDT());
		eoHEADER.setEOLINK(eoHEADERBean.getEOLINK());
		eoHEADER.setEOORG(eoHEADERBean.getEOORG());
		eoHEADER.setEOPDSC(eoHEADERBean.getEOPDSC());
		eoHEADER.setEOPLT(eoHEADERBean.getEOPLT());
		eoHEADER.setEOSTS(eoHEADERBean.getEOSTS());
		eoHEADER.setEOTOTV(eoHEADERBean.getEOTOTV());

	}

	private void eoAPPRVLBeanToDomain(List<EOAPPRVL> eoAPPRVLList, List<EOAPPRVLBean> eoAPPRVLParentBeanList) {

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

		SOAPMessage message = null;
		EOAPPRVLParentBean eoAPPRVLParentBean = new EOAPPRVLParentBean();
		try {
			message = MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(response.getBytes()));

			JAXBContext jaxbContext = JAXBContext.newInstance(EOAPPRVLParentBean.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			// converting soap response to EOPARTFParentBean objecteoPARTFParentBean
			eoAPPRVLParentBean = (EOAPPRVLParentBean) unmarshaller
					.unmarshal(message.getSOAPBody().extractContentAsDocument().getFirstChild().getFirstChild());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

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
		// StringReader sr = responseTransformation(response, xif);
		SOAPMessage message = null;
		EOPARTFParentBean eoPARTFParentBean = new EOPARTFParentBean();
		try {
			message = MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(response.getBytes()));

			JAXBContext jaxbContext = JAXBContext.newInstance(EOPARTFParentBean.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			// converting soap response to EOPARTFParentBean objecteoPARTFParentBean
			eoPARTFParentBean = (EOPARTFParentBean) unmarshaller
					.unmarshal(message.getSOAPBody().extractContentAsDocument().getFirstChild().getFirstChild());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return eoPARTFParentBean;
	}

	private EOHEADERBean responseStringToEOHEADERBean(String response, XMLInputFactory xif)
			throws XMLStreamException, TransformerConfigurationException, TransformerFactoryConfigurationError,
			TransformerException, JAXBException {
		EOHEADERBean eoHEADERBean = new EOHEADERBean();
		XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(response));
		xsr.nextTag();
		xsr.nextTag(); // Advance to Header tag
		xsr.nextTag(); // Advance to Body tag
		xsr.nextTag(); // Advance to
		if (response.contains("s:Header")) {
			xsr.nextTag(); // Advance to GetPartsResult tag
			xsr.nextTag();
		}
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		StringWriter stringWriter = new StringWriter();
		transformer.transform(new StAXSource(xsr), new StreamResult(stringWriter));
		StringReader sr = new StringReader(stringWriter.toString());
		JAXBContext jaxbContext = JAXBContext.newInstance(EOHEADERBean.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		eoHEADERBean = (EOHEADERBean) unmarshaller.unmarshal(sr);

		return eoHEADERBean;
	}

	private EOCOMMENTParentBean responseStringToEOCOMENTBean(String response, XMLInputFactory xif)
			throws XMLStreamException, TransformerConfigurationException, TransformerFactoryConfigurationError,
			TransformerException, JAXBException {
		SOAPMessage message = null;
		EOCOMMENTParentBean eoCOMMENTParentBean = new EOCOMMENTParentBean();
		try {
			message = MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(response.getBytes()));

			JAXBContext jaxbContext = JAXBContext.newInstance(EOCOMMENTParentBean.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			// converting soap response to EOPARTFParentBean objecteoPARTFParentBean
			eoCOMMENTParentBean = (EOCOMMENTParentBean) unmarshaller
					.unmarshal(message.getSOAPBody().extractContentAsDocument().getFirstChild().getFirstChild());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return eoCOMMENTParentBean;
	}

	/*
	 * private StringReader responseTransformation(String response, XMLInputFactory
	 * xif) throws XMLStreamException, TransformerConfigurationException,
	 * TransformerFactoryConfigurationError, TransformerException { XMLStreamReader
	 * xsr = xif.createXMLStreamReader(new StringReader(response)); xsr.nextTag();
	 * // Advance to Envelope tag xsr.nextTag(); // Advance to Header tag
	 * xsr.nextTag(); // Advance to Body tag xsr.nextTag(); // Advance to
	 * GetPartsResponse tag //xsr.nextTag(); // Advance to GetPartsResult tag
	 * //xsr.nextTag();
	 * 
	 * try { SOAPMessage message = MessageFactory.newInstance().createMessage(null,
	 * new ByteArrayInputStream(response.getBytes())); } catch (IOException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); } catch (SOAPException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); } Transformer
	 * transformer = TransformerFactory.newInstance().newTransformer(); StringWriter
	 * stringWriter = new StringWriter(); transformer.transform(new StAXSource(xsr),
	 * new StreamResult(stringWriter)); StringReader sr = new
	 * StringReader(stringWriter.toString());
	 * 
	 * return sr; }
	 */

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
		String resp = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" + 
				"\r\n" + 
				"  <s:Header />\r\n" + 
				"\r\n" + 
				"  <s:Body>\r\n" + 
				"\r\n" + 
				"    <GetGroupsResponse xmlns=\"http://tempuri.org/\">\r\n" + 
				"\r\n" + 
				"      <GetGroupsResult xmlns:a=\"http://schemas.datacontract.org/2004/07/FauxActiveDirectory.Server.Wcf.Models\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
				"\r\n" + 
				"        <a:Group>\r\n" + 
				"\r\n" + 
				"          <a:Key>\r\n" + 
				"\r\n" + 
				"            <a:Name>BPM_PLM_Product_Line_Leader</a:Name>\r\n" + 
				"\r\n" + 
				"          </a:Key>\r\n" + 
				"\r\n" + 
				"          <a:Users>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>jason.anthony@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Jason</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Jason Anthony</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>210929</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Anthony</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>wayne.seaman@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Anthony, Jason</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Sr. Current Product Leader</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>AGJANTHO</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>mark.arnold@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Mark</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Mark Arnold</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>205675</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Arnold</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>gopi.somayajula@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Arnold, Mark</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Director, Global Functional Engineering - Power Sys</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpmarnol</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>eric.arterberry@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Eric</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Eric Arterberry</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>206522</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Arterberry</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>james.krueger@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Arterberry, Eric</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Global Platform Leader I</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpearter</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>karl.bennett@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Karl</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Karl Bennett</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>206013</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Bennett</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>david.blackburn@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Bennett, Karl</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Senior Project Technician</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpkbenne</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>nico.berns@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Nico</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Nico Berns</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>150056</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Berns</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>olaf.van.straaten@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Berns, Nico</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Project Leader/System Group leader</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>ennberns</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>david.blackburn@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"             <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>David</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>David Blackburn</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>205847</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Blackburn</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>terry.snapp@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Blackburn, David</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Engineering Manager I</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpdblack</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>mike.curry@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Mike</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Mike Curry</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>200295</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Curry</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>jason.hartnell@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Curry, Mike</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Project Engineer</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agmcurry</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>john.dalton@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>John</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>John Dalton</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>207694</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Dalton</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>mike.curry@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Dalton, John</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Design Engineer</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agjdalto</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>NUVERA</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>TDoherty@nuvera.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Timothy</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Timothy Doherty</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>209317</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Doherty</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>sblanchet@nuvera.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Doherty, Timothy</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Senior Manager, System Development</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>tdoherty</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>keith.edgar@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Keith</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Keith Edgar</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>130051</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Edgar</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>ivor.wilkinson@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Edgar, Keith</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Engineering Supervisor - Electric Product</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>eckedgar</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>bouwe.everts@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Bouwe</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Bouwe Everts</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>150291</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Everts</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Everts, Bouwe</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Platform Leader</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>ENBEVERT</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>craig.foglesong@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Craig</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Craig Foglesong</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>206668</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Foglesong</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>john.rowley@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Foglesong, Craig</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Global Automation Program Leader</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agcfogle</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>paolo.garofano@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Paolo</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Paolo Garofano</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>314802</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Garofano</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>john.gribben@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Garofano, Paolo</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Special Truck Engineering Coordinator WH Product</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>eopgarof</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>rodrigo.goncalves@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Rodrigo</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Rodrigo Goncalves</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>100226</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Goncalves</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>edson.nascimento@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Goncalves, Rodrigo</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Supervisor de Engenharia</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>aergonca</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>chris.goodwin@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Chris</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Chris Goodwin</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>200218</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Goodwin</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>ian.zook@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Goodwin, Chris</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Functional System Leader I</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agcgoodw</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>nick.greenwood@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Nick</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Nick Greenwood</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>120191</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Greenwood</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>mark.stent@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Greenwood, Nick</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Functional Systems Leader - Automation</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>efnngree</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>john.gribben@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>John</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>John Gribben</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>130163</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Gribben</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>gary.paris@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Gribben, John</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Special Truck Engineering Manager EMEA</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>ecjgribb</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>katherine.halm@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Katherine</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Katherine Halm</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>209565</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Halm</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>matt.arteaga@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Halm, Katherine</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Supervisor, Aftermarket Engineering</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>apkhalm</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>jason.hartnell@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Jason</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Jason Hartnell</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>205606</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Hartnell</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>gary.paris@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Hartnell, Jason</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Special Truck Engineering Manager</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agjhartn</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>NUVERA</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>ghickey@nuvera.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Gregory</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Gregory Hickey</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>209377</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Hickey</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>TKonopka@nuvera.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Hickey, Gregory</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Senior Product Engineer</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>ghickey</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>williamadam.hinkel@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>William (Adam)</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>William (Adam) Hinkel</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>210479</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Hinkel</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>david.blackburn@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Hinkel, William (Adam)</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Engineer Tech Records Support</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpwhinke</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>NUVERA</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>KHoyt@nuvera.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Kevin</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Kevin Hoyt</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>209318</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Hoyt</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>TKonopka@nuvera.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Hoyt, Kevin</a:Presentation>\r\n" + 
				"\r\n" + 
				"             <a:Title>Senior Product Engineer</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>khoyt</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>vaughn.hultsman@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Vaughn</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Vaughn Hultsman</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>206028</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Hultsman</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>jeffrey.janes@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Hultsman, Vaughn</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Program Leader II</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpvhults</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>jeffrey.janes@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Jeffrey</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Jeffrey Janes</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>205971</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Janes</a:LastName>\r\n" + 
				"\r\n" + 
				"             <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>mark.arnold@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Janes, Jeffrey</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Functional System Leader I</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpjjanes</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>joe.joyce@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Joe</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Joe Joyce</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>202208</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Joyce</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>olaf.van.straaten@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Joyce, Joe</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Platform Leader 8-18t</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>abjjoyce</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>NUVERA</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>TKonopka@nuvera.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Tomasz</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Tomasz Konopka</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>209397</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Konopka</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>WVanDam@nuvera.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Konopka, Tomasz</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Director, Engineering</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>tkonopka</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>james.krueger@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>James</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>James Krueger</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>205618</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Krueger</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>gopi.somayajula@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Krueger, James</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Platform Director - 2020 Products</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpjkrueg</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>anton.lenssen@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Anton</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Anton Lenssen</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>316155</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Lenssen</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>peter.vander.aalst@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Lenssen, Anton</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Global Build Development Engineer</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>enalenss</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>vivek.luktuke@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Vivek</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Vivek Luktuke</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>175003</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Luktuke</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>rajesh.wazarkar@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Luktuke, Vivek</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Asst. Gen. Manager</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>civluktu</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>thomas.lyles@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Thomas</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Thomas Lyles</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>206197</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Lyles</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>chris.goodwin@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Lyles, Thomas</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Engineering Manager I</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agdlyles</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>michael.maben@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Michael</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Michael Maben</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>210129</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Maben</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>greg.moffat@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Maben, Michael</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Records Technician</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpmmaben</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>kevin.mcgoldrick@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Kevin</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Kevin McGoldrick</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>200166</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>McGoldrick</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>john.rowley@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>McGoldrick, Kevin</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Functional System Leader Automation Development</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agkmcgol</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>tom.mcneal@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Tom</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Tom McNeal</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>203879</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>McNeal</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>gary.paris@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>McNeal, Tom</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Special Truck Engineering Manager Berea</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>abtmcnea</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>jeff.monico@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Jeff</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Jeff Monico</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>200014</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Monico</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>fred.forstner@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Monico, Jeff</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Analytical Prediction Leader</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agjmonic</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>wim.monteban@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Wim</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Wim Monteban</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>150162</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Monteban</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>peter.faber@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Monteban, Wim</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Team Leader Aftermarket Engineering</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>ENWMONTE</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>mark.nelson@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Mark</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Mark Nelson</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>203590</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Nelson</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>wayne.seaman@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Nelson, Mark</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Senior Program Leader</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agmnelso</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>ben.newey@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Ben</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Ben Newey</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>165261</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Newey</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>anthony.fagg@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Newey, Ben</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Vice President Sales, Asia Pacific</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>fsneweb1</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>willem.nieuwland@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Willem</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Willem Nieuwland</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>315966</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Nieuwland</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>olaf.van.straaten@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Nieuwland, Willem</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Project Leader/System Group leader</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>enwnieuw</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>kelly.nowell@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Kelly</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Kelly Nowell</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>200421</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Nowell</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>tim.cherry@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Nowell, Kelly</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Program Leader II</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agknowel</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>abdel.otmani@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Abdel</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Abdel Otmani</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>206899</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Otmani</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>john.dudley@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Otmani, Abdel</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Aftermarket Product Support Manager</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>apaotman</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>binu.philip@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Binu</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Binu Philip</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>202342</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Philip</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>john.dudley@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Philip, Binu</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Aftermarket Project Engineer II - Quality</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>apbphili</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>greg.pittman@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Greg</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Greg Pittman</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>209829</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Pittman</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>kristian.ressler@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Pittman, Greg</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Engineering Manager II</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpgpittm</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>chad.plaisted@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Chad</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Chad Plaisted</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>205737</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Plaisted</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>tim.cherry@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Plaisted, Chad</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Emerging Markets Global Platform Leader</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpcplais</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>fabio.radaelli@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Fabio</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Fabio Radaelli</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>160051</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Radaelli</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>ken.schreiber@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Radaelli, Fabio</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Warehouse Development Center Leader - Masate</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>eofradae</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>corey.reaves@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Corey</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Corey Reaves</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>208189</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Reaves</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>don.silvers@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Reaves, Corey</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Design Engineer</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agcreave</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>kristian.ressler@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Kristian</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Kristian Ressler</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>200301</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Ressler</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>gopi.somayajula@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Ressler, Kristian</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Director, Global Functional Engineering - Electrical &amp; Control Systems</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpkressl</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>igor.rossi@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Igor</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Igor Rossi</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>160032</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Rossi</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>fabio.radaelli@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Rossi, Igor</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Platform Leader Class III</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>eoirossi</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>karen.roth@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Karen</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Karen Roth</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>203342</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Roth</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>anthony.hornbeck@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Roth, Karen</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Supervisor, Aftermarket Engineering</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>apkroth</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>john.santiagoii@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>John</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>John Santiago II</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>206085</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"             <a:LastName>Santiago II</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>james.krueger@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Santiago II, John</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Global Platform Leader I</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpjsanti</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>bernardo.saucedo@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Bernardo</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Bernardo Saucedo</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>206171</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Saucedo</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>tim.cherry@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Saucedo, Bernardo</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Senior Program Leader</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agbsauce</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>ken.schreiber@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Ken</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Ken Schreiber</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>202896</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Schreiber</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>gopi.somayajula@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Schreiber, Ken</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Director - Americas Warehouse Product Development</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agkschre</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>yuanfu.shi@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Yuanfu</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Yuanfu Shi</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>180497</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Shi</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>tim.cherry@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Shi, Yuanfu</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Assistant Director EMDC</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>fcyshi</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>don.silvers@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Don</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Don Silvers</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>205633</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Silvers</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>jason.hartnell@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Silvers, Don</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Product Engineering Supervisor</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agdsilve</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>barry.simo@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Barry</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Barry Simo</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>201324</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Simo</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>chris.goodwin@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Simo, Barry</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Sr. Project Engineer</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agbsimo</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>mark.stent@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Mark</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Mark Stent</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>120190</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Stent</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>rajiv.prasad@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Stent, Mark</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>ECC Operations Director</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>efnstent</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>edmund.stilwell@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Edmund</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Edmund Stilwell</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>205700</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Stilwell</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>gopi.somayajula@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Stilwell, Edmund</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Functional System Leader, Innovation</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpestilw</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>cassie.strojny@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Cassie</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Cassie Strojny</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>209703</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Strojny</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>kenny.williams@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Strojny, Cassie</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Program Leader II</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agcstroj</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>NMHGFO2</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>fo07a745@snlift.co.jp</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Eriya</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Eriya Sugawara</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>07a745</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Sugawara</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Sugawara, Eriya</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:UserId>fo07a745</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>jim.taylor@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Jim</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Jim Taylor</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>202927</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Taylor</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>gopi.somayajula@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Taylor, Jim</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Program Director - 2020 Products</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpjtaylr</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>julie.toll@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Julie</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Julie Toll</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>203586</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Toll</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>jim.taylor@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Toll, Julie</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>PMP</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpjtoll</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>scott.turner@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Scott</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Scott Turner</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>201126</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Turner</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>brian.crowell@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Turner, Scott</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Prod Support Solutions Engineer II</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agsturne</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>peter.vander.aalst@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Peter</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Peter van der Aalst</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>150002</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>van der Aalst</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:Presentation>van der Aalst, Peter</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Platformleader 36-52 &amp; Reachstacker</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>ENPAAPST</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>twan.van.kuil@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Twan</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Twan van Kuil</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>150146</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>van Kuil</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>wim.monteban@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>van Kuil, Twan</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Senior Aftermarket Engineer</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>entvkuil</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>ivor.wilkinson@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Ivor</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Ivor Wilkinson</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>130162</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Wilkinson</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>michele.corini@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Wilkinson, Ivor</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Product Engineering Manager II</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>eciwilki</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>kenny.williams@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Kenny</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Kenny Williams</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>201985</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Williams</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>ken.schreiber@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Williams, Kenny</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Platform Leader</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>agkewill</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>pillar.yu@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Pillar</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Pillar Yu</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>180357</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Yu</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>tim.cherry@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Yu, Pillar</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Engineering Manager</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>fcpyu</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>riccardo.zignani@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Riccardo</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Riccardo Zignani</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>314628</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Zignani</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>fabio.radaelli@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Zignani, Riccardo</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Vehicle Integration Leader II</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>eorzigna</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>ian.zook@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Ian</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Ian Zook</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>205746</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Zook</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>gopi.somayajula@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Zook, Ian</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Director, Global Functional Engineering - Truck Systems</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpizook</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"          </a:Users>\r\n" + 
				"\r\n" + 
				"        </a:Group>\r\n" + 
				"\r\n" + 
				"        <a:Group>\r\n" + 
				"\r\n" + 
				"          <a:Key>\r\n" + 
				"\r\n" + 
				"            <a:Name>BPM_PLM_Test_Group</a:Name>\r\n" + 
				"\r\n" + 
				"          </a:Key>\r\n" + 
				"\r\n" + 
				"          <a:Users>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>arick.abney@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Arick</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Arick Abney</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>203658</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Abney</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>tom.mcneal@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"             <a:Presentation>Abney, Arick</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Design Engineer III</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>abaabney</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>matthew.bennett@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Matthew</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Matthew Bennett</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>208624</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Bennett</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>duane.richards@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Bennett, Matthew</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Engineer II</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpmbenne</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>david.blackburn@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>David</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>David Blackburn</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>205847</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Blackburn</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>terry.snapp@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Blackburn, David</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Engineering Manager I</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpdblack</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>wayne.chen@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Wayne</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Wayne Chen</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>202062</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Chen</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>rusty.mellmer@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Chen, Wayne</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Senior Technical Specialist Lead</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpwchen</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>michael.cormack@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Michael</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Michael Cormack</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>205724</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Cormack</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>david.blackburn@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Cormack, Michael</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Engineer IV</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpmcorma</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>sjoerd.gerritsen@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Sjoerd</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Sjoerd Gerritsen</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>150097</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Gerritsen</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>roy.opten.berg@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Gerritsen, Sjoerd</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Systems / Process  Support Engineer</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>ensgerri</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>john.gribben@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>John</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>John Gribben</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>130163</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Gribben</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>gary.paris@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Gribben, John</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Special Truck Engineering Manager EMEA</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>ecjgribb</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>williamadam.hinkel@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>William (Adam)</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>William (Adam) Hinkel</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>210479</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Hinkel</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>david.blackburn@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Hinkel, William (Adam)</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Engineer Tech Records Support</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpwhinke</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>jonathan.keeble@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Jonathan</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Jonathan Keeble</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>205972</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Keeble</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>robert.jones@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Keeble, Jonathan</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Project Engineer</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpjkeebl</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>alex.krewson@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Alex</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Alex Krewson</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>206838</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Krewson</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>chikka.rao@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Krewson, Alex</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Engineer III</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpakrews</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>cerilio.maduro@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Cerilio</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Cerilio Maduro</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>150158</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Maduro</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>roy.opten.berg@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Maduro, Cerilio</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>CAD Application Engineer</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>enmaduro</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>gary.matteson@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Gary</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Gary Matteson</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>206054</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Matteson</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>chris.phimister@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Matteson, Gary</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Senior Designer</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpgmatte</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>NUVERA</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>CMcMahon@nuvera.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Catherine</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Catherine McMahon</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>209276</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"             <a:LastName>McMahon</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>bnoble@nuvera.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>McMahon, Catherine</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Configuration Management Specialist</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cmcmahon</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>tom.mcneal@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Tom</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Tom McNeal</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>203879</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>McNeal</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>gary.paris@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>McNeal, Tom</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Special Truck Engineering Manager Berea</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>abtmcnea</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>becky.mcwrightman@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Becky</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Becky McWrightman</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>210130</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>McWrightman</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>david.blackburn@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>McWrightman, Becky</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Records Technician</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpbmcwri</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>rusty.mellmer@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Rusty</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Rusty Mellmer</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>205851</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Mellmer</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>gidu.sriram@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Mellmer, Rusty</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Senior Business Analyst Manager</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cprmellm</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>david.morrow@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>David</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>David Morrow</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>315105</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Morrow</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>john.gribben@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Morrow, David</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Senior Design Engineer</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>ecdmorro</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>valentine.mungyeh@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Valentine</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Valentine Mungyeh</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>209830</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Mungyeh</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>rusty.mellmer@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Mungyeh, Valentine</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Business Analyst Lead</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpvmungy</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>NMHGFO2</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>fo08r731@snlift.co.jp</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Tasuku</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Tasuku Nakashima</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>08r731</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Nakashima</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Nakashima, Tasuku</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:UserId>fo08r731</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>ambar.rasal@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Ambar</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Ambar Rasal</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>211441</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Rasal</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>rusty.mellmer@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Rasal, Ambar</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>IT Consultant</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>scmarasa</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>gidu.sriram@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Gidu</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Gidu Sriram</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>205969</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Sriram</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>John.Bartho@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Sriram, Gidu</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Director IT - Integration &amp; Product Development</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpgsrira</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>isaac.steffen@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Isaac</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Isaac Steffen</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>209162</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Steffen</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>todd.morgan@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Steffen, Isaac</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Engineer II</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpisteff</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>hans.vander.lugt@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Hans</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Hans van der Lugt</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>150155</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>van der Lugt</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>roy.opten.berg@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>van der Lugt, Hans</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>BTDC Manufacturing Engineer</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>ENVDLUGT</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"            <a:User>\r\n" + 
				"\r\n" + 
				"              <a:Domain>GLOBAL</a:Domain>\r\n" + 
				"\r\n" + 
				"              <a:Email>ben.vorobets@hyster-yale.com</a:Email>\r\n" + 
				"\r\n" + 
				"              <a:Enabled>true</a:Enabled>\r\n" + 
				"\r\n" + 
				"              <a:FirstName>Ben</a:FirstName>\r\n" + 
				"\r\n" + 
				"              <a:FullName>Ben Vorobets</a:FullName>\r\n" + 
				"\r\n" + 
				"              <a:Key>\r\n" + 
				"\r\n" + 
				"                <a:EmployeeNumber>211054</a:EmployeeNumber>\r\n" + 
				"\r\n" + 
				"              </a:Key>\r\n" + 
				"\r\n" + 
				"              <a:LastName>Vorobets</a:LastName>\r\n" + 
				"\r\n" + 
				"              <a:Manager i:nil=\"true\" />\r\n" + 
				"\r\n" + 
				"              <a:ManagerEmail>david.blackburn@hyster-yale.com</a:ManagerEmail>\r\n" + 
				"\r\n" + 
				"              <a:Presentation>Vorobets, Ben</a:Presentation>\r\n" + 
				"\r\n" + 
				"              <a:Title>Designer I</a:Title>\r\n" + 
				"\r\n" + 
				"              <a:UserId>cpbvorob</a:UserId>\r\n" + 
				"\r\n" + 
				"            </a:User>\r\n" + 
				"\r\n" + 
				"          </a:Users>\r\n" + 
				"\r\n" + 
				"        </a:Group>\r\n" + 
				"\r\n" + 
				"      </GetGroupsResult>\r\n" + 
				"\r\n" + 
				"    </GetGroupsResponse>\r\n" + 
				"\r\n" + 
				"  </s:Body>\r\n" + 
				"\r\n" + 
				"</s:Envelope>\r\n" + 
				"\r\n" + 
				" ";
		ResponseToDomain respTo = new ResponseToDomain();
		respTo.responseToUserBean(resp);
		// System.out.println("size " + eoHEADER.getEOCNUM());
		// System.out.println("getEOLINK " + eoHEADER.getEOLINK());
	}

}
