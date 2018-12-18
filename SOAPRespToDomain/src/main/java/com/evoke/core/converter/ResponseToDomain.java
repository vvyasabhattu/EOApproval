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
import javax.xml.soap.SOAPException;
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
import com.evoke.model.EOAPPRVL;
import com.evoke.model.EOCOMENT;
import com.evoke.model.EOHEADER;
import com.evoke.model.EOPARTF;

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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		String resp = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" + "\r\n" +
		// " <s:Header />\r\n" +
				"\r\n" + "  <s:Body>\r\n" + "\r\n"
				+ "    <GetExcessObsoleteRequestResponse xmlns=\"http://tempuri.org/\">\r\n" + "\r\n"
				+ "      <GetExcessObsoleteRequestResult xmlns:a=\"http://schemas.datacontract.org/2004/07/EOApprovalProcess.Core.Server.Wcf.Models\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n"
				+ "\r\n" + "        <a:Comments />\r\n" + "\r\n" + "        <a:Header>\r\n" + "\r\n"
				+ "          <a:ControllingUser>Singla, Puneet</a:ControllingUser>\r\n" + "\r\n"
				+ "          <a:DateInitiated>8/8/13 6:07 PM</a:DateInitiated>\r\n" + "\r\n"
				+ "          <a:Description>TEST</a:Description>\r\n" + "\r\n" + "          <a:DocumentLink />\r\n"
				+ "\r\n" + "          <a:ECN />\r\n" + "\r\n"
				+ "          <a:EmailLink>cppsingl@nmhg.com</a:EmailLink>\r\n" + "\r\n"
				+ "          <a:HighestDollarValue>$ 817.360</a:HighestDollarValue>\r\n" + "\r\n"
				+ "          <a:Key>\r\n" + "\r\n" + "            <a:ControlNumber>GS 2-21-2013</a:ControlNumber>\r\n"
				+ "\r\n" + "          </a:Key>\r\n" + "\r\n"
				+ "          <a:Originator>Singla, Puneet</a:Originator>\r\n" + "\r\n"
				+ "          <a:PlantName>Berea</a:PlantName>\r\n" + "\r\n"
				+ "          <a:Status>Unknown</a:Status>\r\n" + "\r\n"
				+ "          <a:TotalValue>$ 817.360</a:TotalValue>\r\n" + "\r\n" + "        </a:Header>\r\n" + "\r\n"
				+ "        <a:Parts>\r\n" + "\r\n" + "          <a:Part>\r\n" + "\r\n"
				+ "            <a:Comment>Excess parts with extended value &lt;= $1000. No usage in past two years.</a:Comment>\r\n"
				+ "\r\n" + "            <a:Key>\r\n" + "\r\n"
				+ "              <a:ControlNumber>GS 2-21-2013</a:ControlNumber>\r\n" + "\r\n"
				+ "            </a:Key>\r\n" + "\r\n" + "            <a:MaterialPlanner>MP12</a:MaterialPlanner>\r\n"
				+ "\r\n" + "            <a:OnHandQuantity>1</a:OnHandQuantity>\r\n" + "\r\n"
				+ "            <a:PartDescription>OUTRIG ARM 4.5\"TK</a:PartDescription>\r\n" + "\r\n"
				+ "            <a:PartNumber>752594605</a:PartNumber>\r\n" + "\r\n"
				+ "            <a:ScrapQuantity>1</a:ScrapQuantity>\r\n" + "\r\n"
				+ "            <a:ThrowAwayQuantity>1</a:ThrowAwayQuantity>\r\n" + "\r\n"
				+ "            <a:TotalCost>1</a:TotalCost>\r\n" + "\r\n" + "            <a:UnitCost>1</a:UnitCost>\r\n"
				+ "\r\n" + "            <a:Vendor>501114</a:Vendor>\r\n" + "\r\n"
				+ "            <a:VendorName>RYERSON INC</a:VendorName>\r\n" + "\r\n" + "          </a:Part>\r\n"
				+ "\r\n" + "        </a:Parts>\r\n" + "\r\n" + "        <a:Routings>\r\n" + "\r\n"
				+ "          <a:Routing>\r\n" + "\r\n"
				+ "            <a:ApprovalDate>2013-08-08T18:09:00</a:ApprovalDate>\r\n" + "\r\n"
				+ "            <a:ApproverName>Singla, Puneet</a:ApproverName>\r\n" + "\r\n"
				+ "            <a:Authority>Materials Support Supervisor</a:Authority>\r\n" + "\r\n"
				+ "            <a:Key>\r\n" + "\r\n"
				+ "              <a:ControlNumber>GS 2-21-2013</a:ControlNumber>\r\n" + "\r\n"
				+ "            </a:Key>\r\n" + "\r\n" + "          </a:Routing>\r\n" + "\r\n"
				+ "          <a:Routing>\r\n" + "\r\n" + "            <a:ApprovalDate i:nil=\"true\" />\r\n" + "\r\n"
				+ "            <a:ApproverName>Singla, Puneet</a:ApproverName>\r\n" + "\r\n"
				+ "            <a:Authority>Division Materials Manager</a:Authority>\r\n" + "\r\n"
				+ "            <a:Key>\r\n" + "\r\n"
				+ "              <a:ControlNumber>GS 2-21-2013</a:ControlNumber>\r\n" + "\r\n"
				+ "            </a:Key>\r\n" + "\r\n" + "          </a:Routing>\r\n" + "\r\n"
				+ "          <a:Routing>\r\n" + "\r\n" + "            <a:ApprovalDate i:nil=\"true\" />\r\n" + "\r\n"
				+ "            <a:ApproverName>Singla, Puneet</a:ApproverName>\r\n" + "\r\n"
				+ "            <a:Authority>Controller</a:Authority>\r\n" + "\r\n" + "            <a:Key>\r\n" + "\r\n"
				+ "              <a:ControlNumber>GS 2-21-2013</a:ControlNumber>\r\n" + "\r\n"
				+ "            </a:Key>\r\n" + "\r\n" + "          </a:Routing>\r\n" + "\r\n"
				+ "        </a:Routings>\r\n" + "\r\n" + "      </GetExcessObsoleteRequestResult>\r\n" + "\r\n"
				+ "    </GetExcessObsoleteRequestResponse>\r\n" + "\r\n" + "  </s:Body>\r\n" + "\r\n" + "</s:Envelope>";
		ResponseToDomain respTo = new ResponseToDomain();
		respTo.responseToEOExess(resp);
		// System.out.println("size " + eoHEADER.getEOCNUM());
		// System.out.println("getEOLINK " + eoHEADER.getEOLINK());
	}

}
