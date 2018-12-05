package com.evoke.core.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;

import com.company.model.EOPARTF;

public class ResponseToEOPARTFDomain {
	
	private static final Logger logger = Logger.getLogger(ResponseToEOPARTFDomain.class);
	
	/**
	 * Get Parts Service
	 * @param response
	 * @return
	 */
	public List<EOPARTF> responseToEOPARTF(String response) {

		List<EOPARTF> eoPARTFList = new ArrayList<EOPARTF>();
		XMLInputFactory xif = XMLInputFactory.newFactory();
		
		try {
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
			JAXBContext jaxbContext = JAXBContext.newInstance(EOPARTFParentBean.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			//converting soap response to EOPARTFParentBean object 
			EOPARTFParentBean eoPARTFParentBean = (EOPARTFParentBean) unmarshaller.unmarshal(sr);
			if (eoPARTFParentBean != null) {
				List<EOPARTFBean> eoPARTFBeanList = eoPARTFParentBean.getEoPARTFBean();
				if (eoPARTFBeanList != null && eoPARTFBeanList.size() > 0) {
					logger.info("eoPARTFBeanList : " + eoPARTFBeanList.size());
					for (EOPARTFBean eoPARTFBean : eoPARTFBeanList) {
						EOPARTF eoPARTF = new EOPARTF();
						//converting  EOPARTFBean to EOPARTF object 
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

					logger.info("eoPARTFList : " + eoPARTFBeanList.size());
				}
			}
			return eoPARTFList;
		} catch (Exception e) {
			e.printStackTrace();
			return eoPARTFList;
		}
	}

	public static void main(String[] args) {
	}

}
