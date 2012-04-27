package org.opennaas.extensions.gmpls.serviceinterface.databinding.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;

import org.apache.muse.util.xml.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BandwidthFault;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePath;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathFault;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathResponse;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.DestinationTNAFault;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetEndpointDiscovery;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetEndpointDiscoveryResponse;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetEndpointDiscoveryResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetEndpointDiscoveryType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathDiscovery;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathDiscoveryResponse;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathDiscoveryResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatus;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatusResponse;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatusResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatusType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathIdentifierType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathNotFoundFault;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.SourceTNAFault;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePath;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePathResponse;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePathResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.UnexpectedFault;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.validator.SyntaxValidator;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.validator.TypeValidator;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.AJaxbSerializer;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;

/**
 * Class to convert Element to Classes (and vice versa).
 * 
 * @author Jan Gassen (gassen@cs.uni-bonn.de)
 * @author Richard Figura (figura@cs.uni-bonn.de)
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * 
 * @version $Id$
 */
public final class JaxbSerializer extends AJaxbSerializer {
	private static org.apache.log4j.Logger logger = PhLogger.getLogger();
	/** Singleton Instance. */
	private static JaxbSerializer selfInstance = null;

	/** JAXB serializer instance. */
	private final JAXBContext serializer;

	/** JAXB marshaller instance. */
	private final Marshaller marshaller;

	/** Jaxb Unmarshaller. */
	private final Unmarshaller unmarshaller;

	/** Validator instance. */
	private final SyntaxValidator syntaxValidator;

	/** Type Validator Instance. */
	private final TypeValidator typeValidator;

	/**
	 * Method to create the JaxbContext.
	 * 
	 * @return new JaxbContext
	 */
	private final JAXBContext createContext() {
		try {
			System.out.println("\n\n\ntest1\n\n\n\n");
			return JAXBContext.newInstance(BandwidthFault.class,

			CreatePath.class, CreatePathResponseType.class,
					CreatePathResponse.class, UnexpectedFault.class,
					CreatePathFault.class, CreatePathType.class,
					TerminatePath.class, TerminatePathResponse.class,
					TerminatePathType.class, TerminatePathResponseType.class,
					PathNotFoundFault.class, GetPathStatus.class,
					GetPathStatusResponse.class, GetPathDiscovery.class,
					GetPathDiscoveryResponse.class,
					GetPathDiscoveryResponseType.class,
					GetEndpointDiscovery.class, GetEndpointDiscoveryType.class,
					GetEndpointDiscoveryResponse.class,
					GetEndpointDiscoveryResponseType.class,
					GetEndpointDiscoveryType.class,
					GetEndpointDiscoveryResponse.class,
					GetEndpointDiscoveryResponseType.class,
					GetPathStatusResponseType.class, PathType.class,
					PathIdentifierType.class, StatusType.class,
					EndpointType.class, SourceTNAFault.class,
					DestinationTNAFault.class, GetPathStatusType.class);
			// return JAXBContext.newInstance(Config.getString("gmpls",
			// "jaxb.path"));
		} catch (JAXBException e) {
			throw new RuntimeException("Cannot create JAXB context", e);
		}
	}

	/**
	 * Method to create the Marshaller.
	 * 
	 * @return new Marshaller
	 */
	private final Marshaller createMarshaller() {
		try {
			return this.serializer.createMarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException("Cannot create Marshaller", e);
		}
	}

	/**
	 * Method to create the Unmarshaller.
	 * 
	 * @return new Unmarshaller
	 */
	private final Unmarshaller createUnmarshaller() {
		try {
			return this.serializer.createUnmarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException("Cannot create Unmarshaller", e);
		}
	}

	/**
	 * Method to create the Syntax Validator.
	 * 
	 * @return new SyntaxValidator
	 */
	private synchronized final SyntaxValidator createSyntaxValidator() {
		try {
			System.out.println("\n\n\ntest2\n\n\n\n");
			logger.debug(Config.getURL("gmpls", "wsdl.validator").toString());
			System.out.println(Config.getURL("gmpls", "wsdl.validator")
					.toString());
			System.out.println("\n\n\ntest3\n\n\n\n");
			return new SyntaxValidator(
					Config.getURL("gmpls", "wsdl.validator"), 0);
		} catch (Exception e) {
			throw new RuntimeException("Cannot create Validator: "
					+ e.toString(), e);
		}
	}

	/**
	 * Constructor.
	 */
	private JaxbSerializer() {

		this.serializer = this.createContext();

		this.marshaller = this.createMarshaller();

		this.unmarshaller = this.createUnmarshaller();

		this.syntaxValidator = this.createSyntaxValidator();

		this.typeValidator = new TypeValidator();
	}

	/**
	 * Singleton instance getter.
	 * 
	 * @return Singleton Instance
	 */
	public static synchronized JaxbSerializer getInstance() {
		if (null == selfInstance) {
			selfInstance = new JaxbSerializer();
		}

		return selfInstance;
	}

	/**
	 * Very important bugfix. See
	 * http://ws.apache.org/muse/docs/2.2.0/manual/troubleshooting/
	 * default-namespaces-xerces.html
	 * 
	 * @param xml
	 *            xml string
	 * @return xml string with corrected namespace
	 */
	private static String adjustNamespace(final String xml) {
		return (xml.replace("xmlns:=", "xmlns="));
	}

	/**
	 * Convert XML String to Element.
	 * 
	 * @param xml
	 *            XML-String to be converted to an Element
	 * @return Dom Element Element derived out of XML-String
	 * @throws SAXException
	 *             A SEXException
	 * @throws IOException
	 *             A IOException
	 * @author Alexander Willner (willner@cs.uni-bonn.de)
	 */
	public static Element xmlToElement(final String xml) throws SAXException,
			IOException {
		String result = xml;

		result = adjustNamespace(result);
		final Element el = XmlUtils.createDocument(result).getDocumentElement();
		return el;
	}

	/**
	 * Convert an Element to XML string.
	 * 
	 * @param element
	 *            Dom Element
	 * @return XML String
	 */
	public static String elementToXml(final Node element) {
		String xml = XmlUtils.toString(element);

		xml = adjustNamespace(xml);

		return xml;
	}

	/**
	 * Convert XML to Object.
	 * 
	 * @param xml
	 *            XML-String to be converted to an Object
	 * @return Object Object derived out of XML-String
	 * @throws JAXBException
	 *             A JAXBException
	 */
	@Override
	public Object xmlToObject(final String xml) throws JAXBException {
		final StringReader is = new StringReader(xml);

		return this.unmarshaller.unmarshal(is);
	}

	/**
	 * Convert Object to XML.
	 * 
	 * @param obj
	 *            Object to be converted to a XML-String
	 * @return XML String XML-String derived out of Object
	 * @throws JAXBException
	 *             A JAXBException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String objectToXml(final Object obj) throws JAXBException {

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final StreamResult result = new StreamResult(outputStream);

		try {
			if (obj.getClass().isAnnotationPresent(XmlRootElement.class)) {
				this.marshaller.marshal(obj, result);
			} else {
				final Class<?> objClass = obj.getClass();

				this.marshaller.marshal(
						new JAXBElement(new QName(objClass.getName()),
								objClass, obj), result);
			}
		} catch (NullPointerException npe) {
			throw new JAXBException(npe.getMessage()
					+ "\n\nMay be caused by an invalid XMLGregorianCalendar.\n"
					+ "Please make shure to use the generateXMLCalendar"
					+ " method from Helpers to create new Calendars to"
					+ " avoid illegal calendar states", npe);
		}

		return (outputStream.toString());
	}

	/**
	 * Convert Element to Object.
	 * 
	 * @param element
	 *            Element to be converted to an Object
	 * @param useValidator
	 *            Use validator true/false
	 * @return Object Object derived out of Element
	 * @throws InvalidRequestFaultException
	 * @throws UnexpectedFaultException
	 */
	@Override
	public synchronized Object elementToObject(final Node element,
			final boolean useValidator) throws InvalidRequestFaultException,
			UnexpectedFaultException {

		if (useValidator) {
			this.typeValidator.validate(element);
		}

		final String xml = elementToXml(element);

		if (useValidator) {
			this.syntaxValidator.validate(xml);
		}

		Object obj;
		try {
			obj = this.xmlToObject(xml);
		} catch (JAXBException e) {
			logger.debug(e.getMessage(), e);
			UnexpectedFaultException e2 = new UnexpectedFaultException(
					"Error during element conversion: " + e.getMessage());
			e2.setStackTrace(e.getStackTrace());
			throw e2;
		}
		return obj;
	}

	/**
	 * Convert Element to Object.
	 * 
	 * @param element
	 *            Element to be converted to an Object
	 * @return Object Object derived out of Element
	 * @throws InvalidRequestFaultException
	 * @throws UnexpectedFaultException
	 */
	@Override
	public Object elementToObject(final Node element)
			throws InvalidRequestFaultException, UnexpectedFaultException {
		return elementToObject(element, true);
	}

	/**
	 * Convert Object to Element.
	 * 
	 * @param obj
	 *            Object to be converted to an Element
	 * @param useValidator
	 *            Use validator true/false
	 * @return DOM Element Element derived out of Object
	 * @throws InvalidRequestFaultException
	 * @throws UnexpectedFaultException
	 */
	@Override
	public synchronized Element objectToElement(final Object obj,
			final boolean useValidator) throws InvalidRequestFaultException,
			UnexpectedFaultException {

		String xml;
		try {
			xml = this.objectToXml(obj);
		} catch (JAXBException e) {
			logger.debug(e.getMessage(), e);
			UnexpectedFaultException e2 = new UnexpectedFaultException(
					"Error during object conversion: " + e.getMessage());
			e2.setStackTrace(e.getStackTrace());
			throw e2;
		}

		if (useValidator) {
			this.syntaxValidator.validate(xml);
		}

		Element element;
		try {
			element = xmlToElement(xml);
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
			UnexpectedFaultException e2 = new UnexpectedFaultException(
					"Error during object conversion: " + e.getMessage());
			e2.setStackTrace(e.getStackTrace());
			throw e2;
		} catch (SAXException e) {
			logger.debug(e.getMessage(), e);
			UnexpectedFaultException e2 = new UnexpectedFaultException(
					"Error during object conversion: " + e.getMessage());
			e2.setStackTrace(e.getStackTrace());
			throw e2;
		}

		if (useValidator) {
			this.typeValidator.validate(element);
		}

		return (element);
	}

	/**
	 * Convert Object to Element.
	 * 
	 * @param obj
	 *            Object to be converted to an Element
	 * @return DOM Element Element derived out of Object
	 * @throws InvalidRequestFaultException
	 * @throws UnexpectedFaultException
	 */
	@Override
	public Element objectToElement(final Object obj)
			throws InvalidRequestFaultException, UnexpectedFaultException {
		return objectToElement(obj, false);
	}

	/**
	 * Cloning _not_ supported! Should use the factory class instead
	 * 
	 * @return Nothing. Since cloning is _not_ supported!
	 * @throws CloneNotSupportedException
	 *             A CloneNotSupportedException
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
