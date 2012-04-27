
package org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RetryAfter_QNAME = new QName("http://www.w3.org/2005/08/addressing", "RetryAfter");
    private final static QName _ProblemIRI_QNAME = new QName("http://www.w3.org/2005/08/addressing", "ProblemIRI");
    private final static QName _EndpointReference_QNAME = new QName("http://www.w3.org/2005/08/addressing", "EndpointReference");
    private final static QName _ProblemHeaderQName_QNAME = new QName("http://www.w3.org/2005/08/addressing", "ProblemHeaderQName");
    private final static QName _To_QNAME = new QName("http://www.w3.org/2005/08/addressing", "To");
    private final static QName _ProblemHeader_QNAME = new QName("http://www.w3.org/2005/08/addressing", "ProblemHeader");
    private final static QName _ProblemAction_QNAME = new QName("http://www.w3.org/2005/08/addressing", "ProblemAction");
    private final static QName _RelatesTo_QNAME = new QName("http://www.w3.org/2005/08/addressing", "RelatesTo");
    private final static QName _From_QNAME = new QName("http://www.w3.org/2005/08/addressing", "From");
    private final static QName _ReplyTo_QNAME = new QName("http://www.w3.org/2005/08/addressing", "ReplyTo");
    private final static QName _Metadata_QNAME = new QName("http://www.w3.org/2005/08/addressing", "Metadata");
    private final static QName _Action_QNAME = new QName("http://www.w3.org/2005/08/addressing", "Action");
    private final static QName _BaseFault_QNAME = new QName("http://docs.oasis-open.org/wsrf/bf-2", "BaseFault");
    private final static QName _MessageID_QNAME = new QName("http://www.w3.org/2005/08/addressing", "MessageID");
    private final static QName _FaultTo_QNAME = new QName("http://www.w3.org/2005/08/addressing", "FaultTo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType }
     * 
     */
    public org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType createAttributedURIType() {
        return new org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType();
    }

    /**
     * Create an instance of {@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType.FaultCause }
     * 
     */
    public org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType.FaultCause createBaseFaultTypeFaultCause() {
        return new org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType.FaultCause();
    }

    /**
     * Create an instance of {@link EndpointType }
     * 
     */
    public EndpointType createEndpointType() {
        return new EndpointType();
    }

    /**
     * Create an instance of {@link GetPathDiscoveryResponseType }
     * 
     */
    public GetPathDiscoveryResponseType createGetPathDiscoveryResponseType() {
        return new GetPathDiscoveryResponseType();
    }

    /**
     * Create an instance of {@link GetPathDiscoveryResponse }
     * 
     */
    public GetPathDiscoveryResponse createGetPathDiscoveryResponse() {
        return new GetPathDiscoveryResponse();
    }

    /**
     * Create an instance of {@link SourceTNAFault }
     * 
     */
    public SourceTNAFault createSourceTNAFault() {
        return new SourceTNAFault();
    }

    /**
     * Create an instance of {@link GetPathDiscovery }
     * 
     */
    public GetPathDiscovery createGetPathDiscovery() {
        return new GetPathDiscovery();
    }

    /**
     * Create an instance of {@link GetPathStatusResponseType }
     * 
     */
    public GetPathStatusResponseType createGetPathStatusResponseType() {
        return new GetPathStatusResponseType();
    }

    /**
     * Create an instance of {@link AttributedAnyType }
     * 
     */
    public AttributedAnyType createAttributedAnyType() {
        return new AttributedAnyType();
    }

    /**
     * Create an instance of {@link GetPathDiscoveryType }
     * 
     */
    public GetPathDiscoveryType createGetPathDiscoveryType() {
        return new GetPathDiscoveryType();
    }

    /**
     * Create an instance of {@link PathType }
     * 
     */
    public PathType createPathType() {
        return new PathType();
    }

    /**
     * Create an instance of {@link TerminatePathType }
     * 
     */
    public TerminatePathType createTerminatePathType() {
        return new TerminatePathType();
    }

    /**
     * Create an instance of {@link GetPathStatusResponse }
     * 
     */
    public GetPathStatusResponse createGetPathStatusResponse() {
        return new GetPathStatusResponse();
    }

    /**
     * Create an instance of {@link CreatePathResponseType }
     * 
     */
    public CreatePathResponseType createCreatePathResponseType() {
        return new CreatePathResponseType();
    }

    /**
     * Create an instance of {@link GetEndpointDiscoveryResponse }
     * 
     */
    public GetEndpointDiscoveryResponse createGetEndpointDiscoveryResponse() {
        return new GetEndpointDiscoveryResponse();
    }

    /**
     * Create an instance of {@link GetEndpointDiscoveryType }
     * 
     */
    public GetEndpointDiscoveryType createGetEndpointDiscoveryType() {
        return new GetEndpointDiscoveryType();
    }

    /**
     * Create an instance of {@link CreatePathFault }
     * 
     */
    public CreatePathFault createCreatePathFault() {
        return new CreatePathFault();
    }

    /**
     * Create an instance of {@link GetPathStatus }
     * 
     */
    public GetPathStatus createGetPathStatus() {
        return new GetPathStatus();
    }

    /**
     * Create an instance of {@link BandwidthFault }
     * 
     */
    public BandwidthFault createBandwidthFault() {
        return new BandwidthFault();
    }

    /**
     * Create an instance of {@link CreatePath }
     * 
     */
    public CreatePath createCreatePath() {
        return new CreatePath();
    }

    /**
     * Create an instance of {@link PathNotFoundFault }
     * 
     */
    public PathNotFoundFault createPathNotFoundFault() {
        return new PathNotFoundFault();
    }

    /**
     * Create an instance of {@link RelatesToType }
     * 
     */
    public RelatesToType createRelatesToType() {
        return new RelatesToType();
    }

    /**
     * Create an instance of {@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.ReferenceParametersType }
     * 
     */
    public org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.ReferenceParametersType createReferenceParametersType() {
        return new org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.ReferenceParametersType();
    }

    /**
     * Create an instance of {@link DestinationTNAFault }
     * 
     */
    public DestinationTNAFault createDestinationTNAFault() {
        return new DestinationTNAFault();
    }

    /**
     * Create an instance of {@link GetEndpointDiscoveryResponseType }
     * 
     */
    public GetEndpointDiscoveryResponseType createGetEndpointDiscoveryResponseType() {
        return new GetEndpointDiscoveryResponseType();
    }

    /**
     * Create an instance of {@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType }
     * 
     */
    public org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType createEndpointReferenceType() {
        return new org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType();
    }

    /**
     * Create an instance of {@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.MetadataType }
     * 
     */
    public org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.MetadataType createMetadataType() {
        return new org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.MetadataType();
    }

    /**
     * Create an instance of {@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType.Description }
     * 
     */
    public org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType.Description createBaseFaultTypeDescription() {
        return new org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType.Description();
    }

    /**
     * Create an instance of {@link GetPathStatusType }
     * 
     */
    public GetPathStatusType createGetPathStatusType() {
        return new GetPathStatusType();
    }

    /**
     * Create an instance of {@link CreatePathResponse }
     * 
     */
    public CreatePathResponse createCreatePathResponse() {
        return new CreatePathResponse();
    }

    /**
     * Create an instance of {@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType }
     * 
     */
    public org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType createBaseFaultType() {
        return new org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType();
    }

    /**
     * Create an instance of {@link AttributedUnsignedLongType }
     * 
     */
    public AttributedUnsignedLongType createAttributedUnsignedLongType() {
        return new AttributedUnsignedLongType();
    }

    /**
     * Create an instance of {@link AttributedQNameType }
     * 
     */
    public AttributedQNameType createAttributedQNameType() {
        return new AttributedQNameType();
    }

    /**
     * Create an instance of {@link CreatePathType }
     * 
     */
    public CreatePathType createCreatePathType() {
        return new CreatePathType();
    }

    /**
     * Create an instance of {@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType.ErrorCode }
     * 
     */
    public org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType.ErrorCode createBaseFaultTypeErrorCode() {
        return new org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType.ErrorCode();
    }

    /**
     * Create an instance of {@link ProblemActionType }
     * 
     */
    public ProblemActionType createProblemActionType() {
        return new ProblemActionType();
    }

    /**
     * Create an instance of {@link GetEndpointDiscovery }
     * 
     */
    public GetEndpointDiscovery createGetEndpointDiscovery() {
        return new GetEndpointDiscovery();
    }

    /**
     * Create an instance of {@link TerminatePath }
     * 
     */
    public TerminatePath createTerminatePath() {
        return new TerminatePath();
    }

    /**
     * Create an instance of {@link TerminatePathResponseType }
     * 
     */
    public TerminatePathResponseType createTerminatePathResponseType() {
        return new TerminatePathResponseType();
    }

    /**
     * Create an instance of {@link PathIdentifierType }
     * 
     */
    public PathIdentifierType createPathIdentifierType() {
        return new PathIdentifierType();
    }

    /**
     * Create an instance of {@link UnexpectedFault }
     * 
     */
    public UnexpectedFault createUnexpectedFault() {
        return new UnexpectedFault();
    }

    /**
     * Create an instance of {@link TerminatePathResponse }
     * 
     */
    public TerminatePathResponse createTerminatePathResponse() {
        return new TerminatePathResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributedUnsignedLongType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "RetryAfter")
    public JAXBElement<AttributedUnsignedLongType> createRetryAfter(AttributedUnsignedLongType value) {
        return new JAXBElement<AttributedUnsignedLongType>(_RetryAfter_QNAME, AttributedUnsignedLongType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "ProblemIRI")
    public JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType> createProblemIRI(org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType value) {
        return new JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType>(_ProblemIRI_QNAME, org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "EndpointReference")
    public JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType> createEndpointReference(org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType value) {
        return new JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType>(_EndpointReference_QNAME, org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributedQNameType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "ProblemHeaderQName")
    public JAXBElement<AttributedQNameType> createProblemHeaderQName(AttributedQNameType value) {
        return new JAXBElement<AttributedQNameType>(_ProblemHeaderQName_QNAME, AttributedQNameType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "To")
    public JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType> createTo(org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType value) {
        return new JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType>(_To_QNAME, org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributedAnyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "ProblemHeader")
    public JAXBElement<AttributedAnyType> createProblemHeader(AttributedAnyType value) {
        return new JAXBElement<AttributedAnyType>(_ProblemHeader_QNAME, AttributedAnyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProblemActionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "ProblemAction")
    public JAXBElement<ProblemActionType> createProblemAction(ProblemActionType value) {
        return new JAXBElement<ProblemActionType>(_ProblemAction_QNAME, ProblemActionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RelatesToType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "RelatesTo")
    public JAXBElement<RelatesToType> createRelatesTo(RelatesToType value) {
        return new JAXBElement<RelatesToType>(_RelatesTo_QNAME, RelatesToType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "From")
    public JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType> createFrom(org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType value) {
        return new JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType>(_From_QNAME, org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "ReplyTo")
    public JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType> createReplyTo(org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType value) {
        return new JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType>(_ReplyTo_QNAME, org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.MetadataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "Metadata")
    public JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.MetadataType> createMetadata(org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.MetadataType value) {
        return new JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.MetadataType>(_Metadata_QNAME, org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.MetadataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "Action")
    public JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType> createAction(org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType value) {
        return new JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType>(_Action_QNAME, org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wsrf/bf-2", name = "BaseFault")
    public JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType> createBaseFault(org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType value) {
        return new JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType>(_BaseFault_QNAME, org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.BaseFaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "MessageID")
    public JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType> createMessageID(org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType value) {
        return new JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType>(_MessageID_QNAME, org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.AttributedURIType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "FaultTo")
    public JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType> createFaultTo(org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType value) {
        return new JAXBElement<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType>(_FaultTo_QNAME, org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointReferenceType.class, null, value);
    }

}
