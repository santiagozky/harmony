/**
 *
 */
package org.opennaas.extensions.gmpls.client.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.muse.ws.addressing.EndpointReference;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.xml.sax.SAXException;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePath;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathResponse;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetEndpointDiscovery;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetEndpointDiscoveryResponse;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetEndpointDiscoveryType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathDiscovery;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathDiscoveryResponse;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathDiscoveryType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatus;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatusResponse;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatusResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatusType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathIdentifierType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePath;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePathResponse;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePathResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.extensions.gmpls.webservice.GmplsClient;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.core.utils.Config;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de)
 */
public class GmplsWS {
    /** Proxy for the GmplsWS. */
    private static GmplsClient proxy = null;
    /** TNA List. */
    private static List<EndpointType> endpointList = new ArrayList<EndpointType>();
    /** Path List. */
    private static List<PathIdentifierType> pathList = new ArrayList<PathIdentifierType>();

    static {
	try {
	    GmplsWS.proxy = new GmplsClient(new EndpointReference(new URI(
		    Config.getString("gmplsClient", "epr.gmpls"))));
	} catch (final URISyntaxException e) {
	    e.printStackTrace();
	}
    }

    /**
     * @return List<EndpointType>
     * @throws SoapFault
     * @throws JAXBException
     * @throws SAXException
     * @throws IOException
     */
    public static final List<EndpointType> getTopologyInformation()
	    throws SoapFault, JAXBException, SAXException, IOException {
	final GetEndpointDiscoveryType gtt = new GetEndpointDiscoveryType();

	final GetEndpointDiscovery gt = new GetEndpointDiscovery();
	gt.setGetEndpointDiscovery(gtt);
	final JaxbSerializer jser = JaxbSerializer.getInstance();
	final GetEndpointDiscoveryResponse response = (GetEndpointDiscoveryResponse) jser
		.elementToObject(GmplsWS.proxy.getEndpointDiscovery(jser
			.objectToElement(gt)));
	GmplsWS.endpointList.clear();
	GmplsWS.endpointList.addAll(response.getGetEndpointDiscoveryResponse()
		.getEndpoint());
	return GmplsWS.endpointList;
    }

    /**
     * @return List<PathIdentifierType>
     * @throws JAXBException
     * @throws SAXException
     * @throws IOException
     * @throws SoapFault
     */
    public static final List<PathIdentifierType> getPathInformation()
	    throws JAXBException, SAXException, IOException, SoapFault {
	final GetPathDiscoveryType gpdt = new GetPathDiscoveryType();
	final GetPathDiscovery gpd = new GetPathDiscovery();
	gpd.setGetPathDiscovery(gpdt);
	final JaxbSerializer jser = JaxbSerializer.getInstance();
	final GetPathDiscoveryResponse response = (GetPathDiscoveryResponse) jser
		.elementToObject(GmplsWS.proxy.getPathDiscovery(jser
			.objectToElement(gpd)));
	GmplsWS.pathList = response.getGetPathDiscoveryResponse()
		.getPathIdentifierList();
	return GmplsWS.pathList;
    }

    /**
     * @param lspHandle
     * @return
     * @throws JAXBException
     * @throws SAXException
     * @throws IOException
     * @throws SoapFault
     */
    public static GetPathStatusResponseType getPathStatus(final int lspHandle)
	    throws JAXBException, SAXException, IOException, SoapFault {
	final GetPathStatusType gpst = new GetPathStatusType();
	final GetPathStatus gps = new GetPathStatus();
	final PathIdentifierType pathId = new PathIdentifierType();
	pathId.setPathIdentifier(lspHandle);
	gpst.setPathIdentifier(pathId);
	gps.setGetPathStatus(gpst);
	final JaxbSerializer jser = JaxbSerializer.getInstance();
	final GetPathStatusResponse response = (GetPathStatusResponse) jser
		.elementToObject(GmplsWS.proxy.getPathStatus((jser
			.objectToElement(gps))));
	return response.getGetPathStatusResponse();
    }

    /**
     * @param lspHandle
     * @return
     * @throws JAXBException
     * @throws SAXException
     * @throws IOException
     * @throws SoapFault
     */
    public static TerminatePathResponseType terminatePath(final int lspHandle)
	    throws JAXBException, SAXException, IOException, SoapFault {
	final TerminatePathType tpt = new TerminatePathType();
	final TerminatePath tp = new TerminatePath();
	final PathIdentifierType pathId = new PathIdentifierType();
	pathId.setPathIdentifier(lspHandle);
	tpt.setPathIdentifier(pathId);
	tpt.setStatus(StatusType.CANCELLED_BY_USER);
	tp.setTerminatePath(tpt);

	final JaxbSerializer jser = JaxbSerializer.getInstance();
	final TerminatePathResponse response = (TerminatePathResponse) jser
		.elementToObject(GmplsWS.proxy.terminatePath((jser
			.objectToElement(tp))));
	return response.getTerminatePathResponse();
    }

    /**
     * @param sourceTNA
     * @param destTNA
     * @param bandwidth
     * @return
     * @throws InvalidRequestFaultException
     * @throws SoapFault
     * @throws IOException
     * @throws JAXBException
     * @throws SAXException
     */
    public static CreatePathResponseType createPath(final String sourceTNA,
	    final String destTNA, final int bandwidth)
	    throws InvalidRequestFaultException, SoapFault, IOException,
	    JAXBException, SAXException {
	final CreatePath createPath = new CreatePath();
	final CreatePathType createPathType = new CreatePathType();
	final PathType pt = new PathType();
	pt.setBandwidth(bandwidth);
	pt.setDestinationTNA(destTNA);
	pt.setSourceTNA(sourceTNA);
	createPathType.setPath(pt);
	createPath.setCreatePath(createPathType);
	final JaxbSerializer jser = JaxbSerializer.getInstance();
	return ((CreatePathResponse) jser.elementToObject(proxy
		.createPath((jser.objectToElement(createPath)))))
		.getCreatePathResponse();
    }

}
