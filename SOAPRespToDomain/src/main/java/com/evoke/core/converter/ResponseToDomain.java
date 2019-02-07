package com.evoke.core.converter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

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
		UserParentBean userParentBean = new UserParentBean();
		SOAPMessage message = null;
		try {
			message = MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(response.getBytes()));
			JAXBContext jaxbContext = JAXBContext.newInstance(UserParentBean.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			// converting soap response to EOPARTFParentBean objecteoPARTFParentBean
			userParentBean = (UserParentBean) unmarshaller
					.unmarshal(message.getSOAPBody().extractContentAsDocument().getFirstChild().getFirstChild());
			if(userParentBean != null)
				logger.info("Inside responseStringToEOAPPRVL eoAPPRVLParentBean.getEoAPPRVL().size() = " + userParentBean.getGroup().size());
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
					logger.info("inside responseToEOExess, eoPARTFList--" + eoPARTFList.size());
				}

				List<EOAPPRVLBean> eoAPPRVLBean = eoBean.getEoRouting().getEoAPPRVLBean();
				if (eoAPPRVLBean != null && eoAPPRVLBean.size() > 0) {
					eoAPPRVLBeanToDomain(eoAPPRVLList, eoAPPRVLBean);
					logger.info("inside responseToEOExess, eoAPPRVLList--" + eoAPPRVLList.size());
				}

				List<EOCOMMENTBean> eoCommentBean = eoBean.getEoComments().getEoCommentBean();
				if (eoCommentBean != null && eoCommentBean.size() > 0) {
					eoCOMMENTBeanToDomain(eoCOMMENTList, eoCommentBean);
					logger.info("inside responseToEOExess, eoCOMMENTList--" + eoCOMMENTList.size());
				}

				EOHEADERBean eoHEADERBean = eoBean.getEoHEADER();
				if (eoHEADER != null) {
					eoHEADERBeanToDomain(eoHEADER, eoHEADERBean);
					logger.info("inside responseToEOExess, eoHEADER--" + eoHEADER.getEOCNUM());
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
		SOAPMessage message = null;
		try {
			message = MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(response.getBytes()));
			JAXBContext jaxbContext = JAXBContext.newInstance(EOBean.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			eoBean = (EOBean) unmarshaller
					.unmarshal(message.getSOAPBody().extractContentAsDocument().getFirstChild().getFirstChild());
			if(eoBean != null)
				logger.info("Inside responseStringToEOAPPRVL eoAPPRVLParentBean.getEoAPPRVL().size() = " + eoBean.getEoComments().getEoCommentBean().size());
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
		SOAPMessage message = null;
		try {
			message = MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(response.getBytes()));
			JAXBContext jaxbContext = JAXBContext.newInstance(EOHEADERBean.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			// converting soap response to EOPARTFParentBean objecteoPARTFParentBean
			eoHEADERBean = (EOHEADERBean) unmarshaller
					.unmarshal(message.getSOAPBody().extractContentAsDocument().getFirstChild().getFirstChild());
			if(eoHEADERBean != null)
				logger.info("Inside responseStringToEOAPPRVL eoAPPRVLParentBean.getEoAPPRVL().size() = " + eoHEADERBean);
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

	/*public static void main(String[] args) {
		String resp = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><GetGroupsResponse xmlns=\"http://tempuri.org/\"><GetGroupsResult xmlns:a=\"http://schemas.datacontract.org/2004/07/FauxActiveDirectory.Server.Wcf.Models\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><a:Group><a:Key><a:Name>BPM_EO_ Plant_Mgr</a:Name></a:Key><a:Users><a:User><a:Domain>GLOBAL</a:Domain><a:Email>russell.asher@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Russell</a:FirstName><a:FullName>Russell Asher</a:FullName><a:Key><a:EmployeeNumber>211152</a:EmployeeNumber></a:Key><a:LastName>Asher</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>john.gardiner@hyster-yale.com</a:ManagerEmail><a:Presentation>Asher, Russell</a:Presentation><a:Title>Plant Manager</a:Title><a:UserId>abrasher</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>edmundo.morales@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Edmundo</a:FirstName><a:FullName>Edmundo Morales</a:FullName><a:Key><a:EmployeeNumber>190001</a:EmployeeNumber></a:Key><a:LastName>Morales</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>john.gardiner@hyster-yale.com</a:ManagerEmail><a:Presentation>Morales, Edmundo</a:Presentation><a:Title>Plant Manager</a:Title><a:UserId>amemoral</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>jon.riley@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Jon</a:FirstName><a:FullName>Jon Riley</a:FullName><a:Key><a:EmployeeNumber>205708</a:EmployeeNumber></a:Key><a:LastName>Riley</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>john.gardiner@hyster-yale.com</a:ManagerEmail><a:Presentation>Riley, Jon</a:Presentation><a:Title>President</a:Title><a:UserId>asjriley</a:UserId></a:User></a:Users></a:Group><a:Group><a:Key><a:Name>BPM_EO_Controller</a:Name></a:Key><a:Users><a:User><a:Domain>GLOBAL</a:Domain><a:Email>daniela.chapa@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Daniela</a:FirstName><a:FullName>Daniela Chapa</a:FullName><a:Key><a:EmployeeNumber>190015</a:EmployeeNumber></a:Key><a:LastName>Chapa</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>edmundo.morales@hyster-yale.com</a:ManagerEmail><a:Presentation>Chapa, Daniela</a:Presentation><a:Title>Plant Controller</a:Title><a:UserId>amdchapa</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>brandon.kim@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Brandon</a:FirstName><a:FullName>Brandon Kim</a:FullName><a:Key><a:EmployeeNumber>208844</a:EmployeeNumber></a:Key><a:LastName>Kim</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>jon.riley@hyster-yale.com</a:ManagerEmail><a:Presentation>Kim, Brandon</a:Presentation><a:Title>Plant Manager</a:Title><a:UserId>asbkim</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>casey.raynor@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Casey</a:FirstName><a:FullName>Casey Raynor</a:FullName><a:Key><a:EmployeeNumber>206498</a:EmployeeNumber></a:Key><a:LastName>Raynor</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>tim.plachecki@hyster-yale.com</a:ManagerEmail><a:Presentation>Raynor, Casey</a:Presentation><a:Title>Materials Planning Team Leader</a:Title><a:UserId>aacrayno</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>jon.riley@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Jon</a:FirstName><a:FullName>Jon Riley</a:FullName><a:Key><a:EmployeeNumber>205708</a:EmployeeNumber></a:Key><a:LastName>Riley</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>john.gardiner@hyster-yale.com</a:ManagerEmail><a:Presentation>Riley, Jon</a:Presentation><a:Title>President</a:Title><a:UserId>asjriley</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>timothy.white@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Timothy</a:FirstName><a:FullName>Timothy White</a:FullName><a:Key><a:EmployeeNumber>203910</a:EmployeeNumber></a:Key><a:LastName>White</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>russell.asher@hyster-yale.com</a:ManagerEmail><a:Presentation>White, Timothy</a:Presentation><a:Title>Plant Controller</a:Title><a:UserId>abtwhite</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>andrea.woolard@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Andrea</a:FirstName><a:FullName>Andrea Woolard</a:FullName><a:Key><a:EmployeeNumber>201473</a:EmployeeNumber></a:Key><a:LastName>Woolard</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>jodie.wrought@hyster-yale.com</a:ManagerEmail><a:Presentation>Woolard, Andrea</a:Presentation><a:Title>Cost Accounting Manager</a:Title><a:UserId>agawoola</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>jodie.wrought@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Jodie</a:FirstName><a:FullName>Jodie Wrought</a:FullName><a:Key><a:EmployeeNumber>201423</a:EmployeeNumber></a:Key><a:LastName>Wrought</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>john.gardiner@hyster-yale.com</a:ManagerEmail><a:Presentation>Wrought, Jodie</a:Presentation><a:Title>Plant Controller</a:Title><a:UserId>AGJWROUG</a:UserId></a:User></a:Users></a:Group><a:Group><a:Key><a:Name>BPM_EO_Div_Matl_Mgr</a:Name></a:Key><a:Users><a:User><a:Domain>GLOBAL</a:Domain><a:Email>james.anderson@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>James</a:FirstName><a:FullName>James Anderson</a:FullName><a:Key><a:EmployeeNumber>206233</a:EmployeeNumber></a:Key><a:LastName>Anderson</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>mark.champagne@hyster-yale.com</a:ManagerEmail><a:Presentation>Anderson, James</a:Presentation><a:Title>Supply Manager Americas</a:Title><a:UserId>aajander</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>g.clark@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Greg</a:FirstName><a:FullName>Greg Clark</a:FullName><a:Key><a:EmployeeNumber>206260</a:EmployeeNumber></a:Key><a:LastName>Clark</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>jon.riley@hyster-yale.com</a:ManagerEmail><a:Presentation>Clark, Greg</a:Presentation><a:Title>Materials Flow Manager</a:Title><a:UserId>asgclark</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>melba.gorham@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Melba</a:FirstName><a:FullName>Melba Gorham</a:FullName><a:Key><a:EmployeeNumber>212698</a:EmployeeNumber></a:Key><a:LastName>Gorham</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail i:nil=\"true\"/><a:Presentation>Gorham, Melba</a:Presentation><a:Title>Materials Flow Manager</a:Title><a:UserId>agmgorha</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>mike.gorham@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Mike</a:FirstName><a:FullName>Mike Gorham</a:FullName><a:Key><a:EmployeeNumber>190077</a:EmployeeNumber></a:Key><a:LastName>Gorham</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>edmundo.morales@hyster-yale.com</a:ManagerEmail><a:Presentation>Gorham, Mike</a:Presentation><a:Title>Materials Manager</a:Title><a:UserId>ammgorha</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>rick.martin@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Rick</a:FirstName><a:FullName>Rick Martin</a:FullName><a:Key><a:EmployeeNumber>206082</a:EmployeeNumber></a:Key><a:LastName>Martin</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>alejandro.oliva@hyster-yale.com</a:ManagerEmail><a:Presentation>Martin, Rick</a:Presentation><a:Title>Value Stream Manager IV</a:Title><a:UserId>agrmarti</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>casey.raynor@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Casey</a:FirstName><a:FullName>Casey Raynor</a:FullName><a:Key><a:EmployeeNumber>206498</a:EmployeeNumber></a:Key><a:LastName>Raynor</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>tim.plachecki@hyster-yale.com</a:ManagerEmail><a:Presentation>Raynor, Casey</a:Presentation><a:Title>Materials Planning Team Leader</a:Title><a:UserId>aacrayno</a:UserId></a:User></a:Users></a:Group><a:Group><a:Key><a:Name>BPM_EO_FinalReport</a:Name></a:Key><a:Users><a:User><a:Domain>GLOBAL</a:Domain><a:Email>wayne.chen@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Wayne</a:FirstName><a:FullName>Wayne Chen</a:FullName><a:Key><a:EmployeeNumber>202062</a:EmployeeNumber></a:Key><a:LastName>Chen</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>rusty.mellmer@hyster-yale.com</a:ManagerEmail><a:Presentation>Chen, Wayne</a:Presentation><a:Title>Senior Technical Specialist Lead</a:Title><a:UserId>cpwchen</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>gidu.sriram@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Gidu</a:FirstName><a:FullName>Gidu Sriram</a:FullName><a:Key><a:EmployeeNumber>205969</a:EmployeeNumber></a:Key><a:LastName>Sriram</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>John.Bartho@hyster-yale.com</a:ManagerEmail><a:Presentation>Sriram, Gidu</a:Presentation><a:Title>Director IT - Integration &amp; Product Development</a:Title><a:UserId>cpgsrira</a:UserId></a:User></a:Users></a:Group><a:Group><a:Key><a:Name>BPM_EO_Matl_Supp_Sprvsr</a:Name></a:Key><a:Users><a:User><a:Domain>GLOBAL</a:Domain><a:Email>j.brown@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Jason S.</a:FirstName><a:FullName>Jason S. Brown</a:FullName><a:Key><a:EmployeeNumber>201279</a:EmployeeNumber></a:Key><a:LastName>Brown</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>warren.nel@hyster-yale.com</a:ManagerEmail><a:Presentation>Brown, Jason S.</a:Presentation><a:Title>Strategic Sourcing Manager II</a:Title><a:UserId>agjbrown</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>laura.buck@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Laura</a:FirstName><a:FullName>Laura Buck</a:FullName><a:Key><a:EmployeeNumber>200403</a:EmployeeNumber></a:Key><a:LastName>Buck</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>tim.plachecki@hyster-yale.com</a:ManagerEmail><a:Presentation>Buck, Laura</a:Presentation><a:Title>Materials Planner Team Leader</a:Title><a:UserId>aglbuck</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>dale.davis@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Dale</a:FirstName><a:FullName>Dale Davis</a:FullName><a:Key><a:EmployeeNumber>201249</a:EmployeeNumber></a:Key><a:LastName>Davis</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>curt.tucker@hyster-yale.com</a:ManagerEmail><a:Presentation>Davis, Dale</a:Presentation><a:Title>ECN Analyst</a:Title><a:UserId>agddavis</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>brian.fleenor@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Brian</a:FirstName><a:FullName>Brian Fleenor</a:FullName><a:Key><a:EmployeeNumber>211607</a:EmployeeNumber></a:Key><a:LastName>Fleenor</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>tim.plachecki@hyster-yale.com</a:ManagerEmail><a:Presentation>Fleenor, Brian</a:Presentation><a:Title>Materials Planner Team Leader</a:Title><a:UserId>abbfleen</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>nicole.licursi@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Nicole</a:FirstName><a:FullName>Nicole Licursi</a:FullName><a:Key><a:EmployeeNumber>209350</a:EmployeeNumber></a:Key><a:LastName>Licursi</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>russell.asher@hyster-yale.com</a:ManagerEmail><a:Presentation>Licursi, Nicole</a:Presentation><a:Title>Materials Manager</a:Title><a:UserId>abnlicur</a:UserId></a:User><a:User><a:Domain>GLOBAL</a:Domain><a:Email>casey.raynor@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Casey</a:FirstName><a:FullName>Casey Raynor</a:FullName><a:Key><a:EmployeeNumber>206498</a:EmployeeNumber></a:Key><a:LastName>Raynor</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>tim.plachecki@hyster-yale.com</a:ManagerEmail><a:Presentation>Raynor, Casey</a:Presentation><a:Title>Materials Planning Team Leader</a:Title><a:UserId>aacrayno</a:UserId></a:User></a:Users></a:Group><a:Group><a:Key><a:Name>BPM_EO_VP_Fin</a:Name></a:Key><a:Users><a:User><a:Domain>GLOBAL</a:Domain><a:Email>ray.ulmer@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>Ray</a:FirstName><a:FullName>Ray Ulmer</a:FullName><a:Key><a:EmployeeNumber>202050</a:EmployeeNumber></a:Key><a:LastName>Ulmer</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>chuck.pascarelli@hyster-yale.com</a:ManagerEmail><a:Presentation>Ulmer, Ray</a:Presentation><a:Title>Vice President, Finance</a:Title><a:UserId>aarulmer</a:UserId></a:User></a:Users></a:Group><a:Group><a:Key><a:Name>BPM_EO_VP_Mfg</a:Name></a:Key><a:Users><a:User><a:Domain>GLOBAL</a:Domain><a:Email>john.gardiner@hyster-yale.com</a:Email><a:Enabled>true</a:Enabled><a:FirstName>John</a:FirstName><a:FullName>John Gardiner</a:FullName><a:Key><a:EmployeeNumber>204438</a:EmployeeNumber></a:Key><a:LastName>Gardiner</a:LastName><a:Manager i:nil=\"true\"/><a:ManagerEmail>chuck.pascarelli@hyster-yale.com</a:ManagerEmail><a:Presentation>Gardiner, John</a:Presentation><a:Title>Vice President, Manufacturing</a:Title><a:UserId>aajgardi</a:UserId></a:User></a:Users></a:Group></GetGroupsResult></GetGroupsResponse></s:Body></s:Envelope>";
		logger.info(resp);
		ResponseToDomain respTo = new ResponseToDomain();
		System.out.println(respTo.responseToUserBean(resp).size()+"");
	}*/

}
