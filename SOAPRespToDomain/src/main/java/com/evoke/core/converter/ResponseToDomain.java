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
import org.w3c.dom.Document;
import org.w3c.dom.Node;

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
				logger.info("Inside responseToEOPARTF method eoPARTFBeanList = "+eoPARTFBeanList.size());
				// converting EOPARTFBean to EOPARTF object
				eoPARTFBeanToDomain(eoPARTFList, eoPARTFBeanList);
			}
			return eoPARTFList;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Inside responseToEOPARTF, Exception is = " + e);
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
		
		//List<UserBean> usersList = new ArrayList<UserBean>();

		try {
			// converting response string to EOPARTFParentBean
			UserParentBean userParentBean = responseStringToUserGroupList(response, xif);
			if (userParentBean != null) {
				List<UserGroupBean> eoPARTFBeanList = userParentBean.getGroup();
				logger.info("Inside responseToUserBean method eoPARTFBeanList = "+eoPARTFBeanList.size());
				
				if(eoPARTFBeanList != null && eoPARTFBeanList.size() > 0) {
					for (UserGroupBean userGroupBean : eoPARTFBeanList) {
						List<UserBean> userBeanList = userGroupBean.getUsersBean().getUserBeanList();
						usersMap.put(userGroupBean.getKey().getName(), userBeanList);						
						/*if(userBeanList != null && userBeanList.size() > 0) {
							for (UserBean userBean : userBeanList) {
								userBean.setGroupName(userGroupBean.getKey().getName());
								userBean.setEmployeeNumber(userBean.getKey().getEmployeeNumber());
								usersList.add(userBean);
							}
						}*/
					}
				}
			}
			//usersMap.put("users", usersList);
			return usersMap;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Inside responseToUserBean, Exception = " + e);
			return usersMap;
		}
	}
	
	private UserParentBean responseStringToUserGroupList(String response, XMLInputFactory xif)
			throws XMLStreamException, TransformerConfigurationException, TransformerFactoryConfigurationError,
			TransformerException, JAXBException {
		// StringReader sr = responseTransformation(response, xif);
		UserParentBean userParentBean = new UserParentBean();
		/*XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(response));
		xsr.nextTag();
		xsr.nextTag(); // Advance to Header tag
		xsr.nextTag(); // Advance to Body tag
		xsr.nextTag(); // Advance to
		if (response.contains("s:Header")) {
			xsr.nextTag(); // Advance to GetPartsResult tag
			xsr.nextTag();
		}
		
		//SOAPMessage message =  MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(response.getBytes()));
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		StringWriter stringWriter = new StringWriter();
		transformer.transform(new StreamSource(new StringReader(response)), new StreamResult(stringWriter));
		StringReader sr = new StringReader(stringWriter.toString());
		JAXBContext jaxbContext = JAXBContext.newInstance(UserParentBean.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		userParentBean = (UserParentBean) unmarshaller.unmarshal(sr);
		if(userParentBean != null)
			logger.info("Inside responseStringToUserGroupList userParentBean.getGroup().size() = "+userParentBean.getGroup().size());
		return userParentBean;*/
		
		
		
		SOAPMessage message = null;
		try {
			message = MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(response.getBytes()));
			JAXBContext jaxbContext = JAXBContext.newInstance(UserParentBean.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			// converting soap response to EOPARTFParentBean objecteoPARTFParentBean
			userParentBean = (UserParentBean) unmarshaller
					.unmarshal(message.getSOAPBody().extractContentAsDocument().getFirstChild().getFirstChild());
			if(userParentBean != null)
				System.out.println("Inside responseStringToEOAPPRVL eoAPPRVLParentBean.getEoAPPRVL().size() = " + userParentBean.getGroup().size());
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("Inside responseStringToEOAPPRVL Exception = " + e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Inside responseStringToEOAPPRVL Exception = " + e);
		}
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
					System.out.println("inside responseToEOExess, eoPARTFList--" + eoPARTFList.size());
				}

				List<EOAPPRVLBean> eoAPPRVLBean = eoBean.getEoRouting().getEoAPPRVLBean();
				if (eoAPPRVLBean != null && eoAPPRVLBean.size() > 0) {
					eoAPPRVLBeanToDomain(eoAPPRVLList, eoAPPRVLBean);
					System.out.println("inside responseToEOExess, eoAPPRVLList--" + eoAPPRVLList.size());
				}

				List<EOCOMMENTBean> eoCommentBean = eoBean.getEoComments().getEoCommentBean();
				if (eoCommentBean != null && eoCommentBean.size() > 0) {
					eoCOMMENTBeanToDomain(eoCOMMENTList, eoCommentBean);
					System.out.println("inside responseToEOExess, eoCOMMENTList--" + eoCOMMENTList.size());
				}

				EOHEADERBean eoHEADERBean = eoBean.getEoHEADER();
				if (eoHEADER != null) {
					eoHEADERBeanToDomain(eoHEADER, eoHEADERBean);
					System.out.println("inside responseToEOExess, eoHEADER--" + eoHEADER.getEOCNUM());
				}

				mapOfEOS.put("eoPARTFList", eoPARTFList);
				mapOfEOS.put("eoAPPRVLList", eoAPPRVLList);
				mapOfEOS.put("eoCOMMENTList", eoCOMMENTList);
				mapOfEOS.put("eoHEADER", eoHEADER);

				logger.info("inside responseToEOExess, eoCOMMENTList--" + eoCOMMENTList.size());
			}
			return mapOfEOS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("inside responseToEOExess, Exception = " + e);
			return null;
		}

	}

	private EOBean responseStringToEO(String response, XMLInputFactory xif) {
		EOBean eoBean = new EOBean();

	/*	try {
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
			if(eoBean != null)
				logger.info("inside responseStringToEO, eoBean.getEoHEADER().getKey().getControlNumber() = " + eoBean.getEoHEADER().getKey().getControlNumber());

		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		SOAPMessage message = null;
		try {
			message = MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(response.getBytes()));
			JAXBContext jaxbContext = JAXBContext.newInstance(EOBean.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			// converting soap response to EOPARTFParentBean objecteoPARTFParentBean
			// Document extractContentAsDocument = message.getSOAPBody().extractContentAsDocument();
			//Node firstChild = extractContentAsDocument.getFirstChild();
			///Node firstChild2 = firstChild.getFirstChild();
			eoBean = (EOBean) unmarshaller
					.unmarshal(message.getSOAPBody().extractContentAsDocument().getFirstChild().getFirstChild());
			if(eoBean != null)
				System.out.println("Inside responseStringToEOAPPRVL eoAPPRVLParentBean.getEoAPPRVL().size() = " + eoBean.getEoComments().getEoCommentBean().size());
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("Inside responseStringToEOAPPRVL Exception = " + e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Inside responseStringToEOAPPRVL Exception = " + e);
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
				logger.info("inside responseToEOCOMMENT, eoCOMMENTBeanList--" + eoCOMMENTBeanList.size());
				// converting EOPARTFBean to EOPARTF object
				eoCOMMENTBeanToDomain(eoCOMMENTList, eoCOMMENTBeanList);
			}
			return eoCOMMENTList;
		} catch (Exception e) {
			logger.info("Inside eoCOMMENTBeanList, Exception = " + e);
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
				logger.info("inside responseToEOAPPRVL, eoEOAPPRVLBeanList--" + eoEOAPPRVLBeanList.size());
				// converting EOPARTFBean to EOPARTF object
				eoAPPRVLBeanToDomain(eoAPPRVLList, eoEOAPPRVLBeanList);
			}
			return eoAPPRVLList;
		} catch (Exception e) {
			logger.info("Inside eoEOAPPRVLBeanList, Exception = " + e);
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
				logger.info("inside responseToEOHEADER, eoHEADERBean.getKey().getControlNumber()--" + eoHEADERBean.getKey().getControlNumber());
			}
			return eoHEADER;
		} catch (Exception e) {
			logger.info("Inside responseToEOHEADER, Exception = " + e);
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
			logger.info("inside eoAPPRVLBeanToDomain : " + eoAPPRVLParentBeanList.size());
			for (EOAPPRVLBean eoAPPRVLParentBean : eoAPPRVLParentBeanList) {
				EOAPPRVL eoAPPRVL = new EOAPPRVL();
				eoAPPRVL.setEOAUTH(eoAPPRVLParentBean.getEOAUTH());
				eoAPPRVL.setEODATE(eoAPPRVLParentBean.getEODATE());
				eoAPPRVL.setEONAME(eoAPPRVLParentBean.getEONAME());
				eoAPPRVL.setEONUM(eoAPPRVLParentBean.getKey().getControlNumber());
				eoAPPRVLList.add(eoAPPRVL);
			}

			logger.info("Inside eoAPPRVLBeanToDomain eoAPPRVLList.size() = " + eoAPPRVLList.size());
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
			if(eoAPPRVLParentBean != null)
				logger.info("Inside responseStringToEOAPPRVL eoAPPRVLParentBean.getEoAPPRVL().size() = " + eoAPPRVLParentBean.getEoAPPRVL().size());
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("Inside responseStringToEOAPPRVL Exception = " + e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Inside responseStringToEOAPPRVL Exception = " + e);
		}

		return eoAPPRVLParentBean;
	}

	private void eoCOMMENTBeanToDomain(List<EOCOMENT> eoCOMMENTList, List<EOCOMMENTBean> eoCOMMENTBeanList) {
		if (eoCOMMENTBeanList != null && eoCOMMENTBeanList.size() > 0) {
			logger.info("inside eoCOMMENTBeanToDomain = " + eoCOMMENTBeanList.size());
			for (EOCOMMENTBean eoPARTFBean : eoCOMMENTBeanList) {
				EOCOMENT eoCOMENT = new EOCOMENT();
				eoCOMENT.setEOCOMM(eoPARTFBean.getEOCOMM());
				eoCOMENT.setEONUM(eoPARTFBean.getKey().getControlNumber());
				eoCOMENT.setEOINDX(eoPARTFBean.getKey().getIndex());
				eoCOMMENTList.add(eoCOMENT);
			}

			logger.info("inside eoCOMMENTBeanToDomain = " + eoCOMMENTList.size());
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
			if(eoPARTFParentBean != null)
				logger.info("inside responseStringToEOPARTFBean, eoPARTFParentBean.getEoPARTFBean().size()= " + eoPARTFParentBean.getEoPARTFBean().size());
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("Inside responseStringToEOPARTFBean Exception = " + e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Inside responseStringToEOPARTFBean Exception = " + e);
		}

		return eoPARTFParentBean;
	}

	private EOHEADERBean responseStringToEOHEADERBean(String response, XMLInputFactory xif)
			throws XMLStreamException, TransformerConfigurationException, TransformerFactoryConfigurationError,
			TransformerException, JAXBException {
		EOHEADERBean eoHEADERBean = new EOHEADERBean();
	/*	XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(response));
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
		if(eoHEADERBean != null)
			logger.info("inside responseStringToEOHEADERBean, eoHEADERBean.getKey().getControlNumber() = " + eoHEADERBean.getKey().getControlNumber());*/
		SOAPMessage message = null;
		try {
			message = MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(response.getBytes()));
			JAXBContext jaxbContext = JAXBContext.newInstance(EOHEADERBean.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			// converting soap response to EOPARTFParentBean objecteoPARTFParentBean
			eoHEADERBean = (EOHEADERBean) unmarshaller
					.unmarshal(message.getSOAPBody().extractContentAsDocument().getFirstChild().getFirstChild());
			if(eoHEADERBean != null)
				System.out.println("Inside responseStringToEOAPPRVL eoAPPRVLParentBean.getEoAPPRVL().size() = " + eoHEADERBean);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("Inside responseStringToEOAPPRVL Exception = " + e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Inside responseStringToEOAPPRVL Exception = " + e);
		}
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
			if(eoCOMMENTParentBean != null)
				logger.info("inside responseStringToEOCOMENTBean, eoCOMMENTParentBean.getEOCOMMENTBean().size() = " + eoCOMMENTParentBean.getEOCOMMENTBean().size());
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("Inside responseStringToEOCOMENTBean Exception = " + e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Inside responseStringToEOCOMENTBean Exception = " + e);
		}

		return eoCOMMENTParentBean;
	}

	private void eoPARTFBeanToDomain(List<EOPARTF> eoPARTFList, List<EOPARTFBean> eoPARTFBeanList) {
		if (eoPARTFBeanList != null && eoPARTFBeanList.size() > 0) {
			logger.info("Inside eoPARTFBeanToDomain, EOPARTFBeanList : " + eoPARTFBeanList.size());
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

			logger.info("Insde eoPARTFBeanToDomain, EOPARTFList : " + eoPARTFList.size());
		}
	}

	public static void main(String[] args) {
		String resp = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><GetHeaderResponse xmlns=\"http://tempuri.org/\"><GetHeaderResult i:nil=\"true\" xmlns:a=\"http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"/></GetHeaderResponse></s:Body></s:Envelope>";
		System.out.println(resp);
		ResponseToDomain respTo = new ResponseToDomain();
		System.out.println(respTo.responseToEOHEADER(resp));
	}

}
