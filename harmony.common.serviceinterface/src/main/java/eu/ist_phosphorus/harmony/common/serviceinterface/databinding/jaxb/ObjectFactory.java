
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EditLinkResponseType }
     * 
     */
    public EditLinkResponseType createEditLinkResponseType() {
        return new EditLinkResponseType();
    }

    /**
     * Create an instance of {@link CompleteJobType }
     * 
     */
    public CompleteJobType createCompleteJobType() {
        return new CompleteJobType();
    }

    /**
     * Create an instance of {@link AddOrEditDomainType }
     * 
     */
    public AddOrEditDomainType createAddOrEditDomainType() {
        return new AddOrEditDomainType();
    }

    /**
     * Create an instance of {@link UnsubscribeResponseType }
     * 
     */
    public UnsubscribeResponseType createUnsubscribeResponseType() {
        return new UnsubscribeResponseType();
    }

    /**
     * Create an instance of {@link ConnectionType }
     * 
     */
    public ConnectionType createConnectionType() {
        return new ConnectionType();
    }

    /**
     * Create an instance of {@link AddTopic }
     * 
     */
    public AddTopic createAddTopic() {
        return new AddTopic();
    }

    /**
     * Create an instance of {@link CancelReservationResponse }
     * 
     */
    public CancelReservationResponse createCancelReservationResponse() {
        return new CancelReservationResponse();
    }

    /**
     * Create an instance of {@link CancelReservation }
     * 
     */
    public CancelReservation createCancelReservation() {
        return new CancelReservation();
    }

    /**
     * Create an instance of {@link ConnectionConstraintType }
     * 
     */
    public ConnectionConstraintType createConnectionConstraintType() {
        return new ConnectionConstraintType();
    }

    /**
     * Create an instance of {@link GetEndpoints }
     * 
     */
    public GetEndpoints createGetEndpoints() {
        return new GetEndpoints();
    }

    /**
     * Create an instance of {@link DeleteLinkType }
     * 
     */
    public DeleteLinkType createDeleteLinkType() {
        return new DeleteLinkType();
    }

    /**
     * Create an instance of {@link CancelReservationType }
     * 
     */
    public CancelReservationType createCancelReservationType() {
        return new CancelReservationType();
    }

    /**
     * Create an instance of {@link DeferrableReservationConstraintType }
     * 
     */
    public DeferrableReservationConstraintType createDeferrableReservationConstraintType() {
        return new DeferrableReservationConstraintType();
    }

    /**
     * Create an instance of {@link CreateReservation }
     * 
     */
    public CreateReservation createCreateReservation() {
        return new CreateReservation();
    }

    /**
     * Create an instance of {@link SubscribeResponseType }
     * 
     */
    public SubscribeResponseType createSubscribeResponseType() {
        return new SubscribeResponseType();
    }

    /**
     * Create an instance of {@link NotificationResponseType }
     * 
     */
    public NotificationResponseType createNotificationResponseType() {
        return new NotificationResponseType();
    }

    /**
     * Create an instance of {@link DomainConnectionStatusType }
     * 
     */
    public DomainConnectionStatusType createDomainConnectionStatusType() {
        return new DomainConnectionStatusType();
    }

    /**
     * Create an instance of {@link GetDomainsType }
     * 
     */
    public GetDomainsType createGetDomainsType() {
        return new GetDomainsType();
    }

    /**
     * Create an instance of {@link AddOrEditDomain }
     * 
     */
    public AddOrEditDomain createAddOrEditDomain() {
        return new AddOrEditDomain();
    }

    /**
     * Create an instance of {@link IsAvailable }
     * 
     */
    public IsAvailable createIsAvailable() {
        return new IsAvailable();
    }

    /**
     * Create an instance of {@link Link }
     * 
     */
    public Link createLink() {
        return new Link();
    }

    /**
     * Create an instance of {@link OperationNotSupportedFault }
     * 
     */
    public OperationNotSupportedFault createOperationNotSupportedFault() {
        return new OperationNotSupportedFault();
    }

    /**
     * Create an instance of {@link EditDomainType }
     * 
     */
    public EditDomainType createEditDomainType() {
        return new EditDomainType();
    }

    /**
     * Create an instance of {@link InvalidReservationIDFault }
     * 
     */
    public InvalidReservationIDFault createInvalidReservationIDFault() {
        return new InvalidReservationIDFault();
    }

    /**
     * Create an instance of {@link ActivateResponseType }
     * 
     */
    public ActivateResponseType createActivateResponseType() {
        return new ActivateResponseType();
    }

    /**
     * Create an instance of {@link ProblemActionType }
     * 
     */
    public ProblemActionType createProblemActionType() {
        return new ProblemActionType();
    }

    /**
     * Create an instance of {@link DomainAlreadyExistsFault }
     * 
     */
    public DomainAlreadyExistsFault createDomainAlreadyExistsFault() {
        return new DomainAlreadyExistsFault();
    }

    /**
     * Create an instance of {@link CancelReservationResponseType }
     * 
     */
    public CancelReservationResponseType createCancelReservationResponseType() {
        return new CancelReservationResponseType();
    }

    /**
     * Create an instance of {@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType.ErrorCode }
     * 
     */
    public eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType.ErrorCode createBaseFaultTypeErrorCode() {
        return new eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType.ErrorCode();
    }

    /**
     * Create an instance of {@link GetStatus }
     * 
     */
    public GetStatus createGetStatus() {
        return new GetStatus();
    }

    /**
     * Create an instance of {@link DeleteLinkResponseType }
     * 
     */
    public DeleteLinkResponseType createDeleteLinkResponseType() {
        return new DeleteLinkResponseType();
    }

    /**
     * Create an instance of {@link DeleteLink }
     * 
     */
    public DeleteLink createDeleteLink() {
        return new DeleteLink();
    }

    /**
     * Create an instance of {@link CreateReservationResponse }
     * 
     */
    public CreateReservationResponse createCreateReservationResponse() {
        return new CreateReservationResponse();
    }

    /**
     * Create an instance of {@link AdditionalData }
     * 
     */
    public AdditionalData createAdditionalData() {
        return new AdditionalData();
    }

    /**
     * Create an instance of {@link EditDomainResponseType }
     * 
     */
    public EditDomainResponseType createEditDomainResponseType() {
        return new EditDomainResponseType();
    }

    /**
     * Create an instance of {@link EndpointNotFoundFault }
     * 
     */
    public EndpointNotFoundFault createEndpointNotFoundFault() {
        return new EndpointNotFoundFault();
    }

    /**
     * Create an instance of {@link GetTopicsType }
     * 
     */
    public GetTopicsType createGetTopicsType() {
        return new GetTopicsType();
    }

    /**
     * Create an instance of {@link EditEndpointResponseType }
     * 
     */
    public EditEndpointResponseType createEditEndpointResponseType() {
        return new EditEndpointResponseType();
    }

    /**
     * Create an instance of {@link GetReservations }
     * 
     */
    public GetReservations createGetReservations() {
        return new GetReservations();
    }

    /**
     * Create an instance of {@link ReservationIDNotFoundFault }
     * 
     */
    public ReservationIDNotFoundFault createReservationIDNotFoundFault() {
        return new ReservationIDNotFoundFault();
    }

    /**
     * Create an instance of {@link CancelJobResponseType }
     * 
     */
    public CancelJobResponseType createCancelJobResponseType() {
        return new CancelJobResponseType();
    }

    /**
     * Create an instance of {@link GetTopicsResponse }
     * 
     */
    public GetTopicsResponse createGetTopicsResponse() {
        return new GetTopicsResponse();
    }

    /**
     * Create an instance of {@link NotificationFault }
     * 
     */
    public NotificationFault createNotificationFault() {
        return new NotificationFault();
    }

    /**
     * Create an instance of {@link GetReservationResponse }
     * 
     */
    public GetReservationResponse createGetReservationResponse() {
        return new GetReservationResponse();
    }

    /**
     * Create an instance of {@link DomainNotFoundFault }
     * 
     */
    public DomainNotFoundFault createDomainNotFoundFault() {
        return new DomainNotFoundFault();
    }

    /**
     * Create an instance of {@link NotificationMessageType }
     * 
     */
    public NotificationMessageType createNotificationMessageType() {
        return new NotificationMessageType();
    }

    /**
     * Create an instance of {@link IsAvailableResponse }
     * 
     */
    public IsAvailableResponse createIsAvailableResponse() {
        return new IsAvailableResponse();
    }

    /**
     * Create an instance of {@link GetStatusResponseType }
     * 
     */
    public GetStatusResponseType createGetStatusResponseType() {
        return new GetStatusResponseType();
    }

    /**
     * Create an instance of {@link AddLinkType }
     * 
     */
    public AddLinkType createAddLinkType() {
        return new AddLinkType();
    }

    /**
     * Create an instance of {@link CompleteJobResponseType }
     * 
     */
    public CompleteJobResponseType createCompleteJobResponseType() {
        return new CompleteJobResponseType();
    }

    /**
     * Create an instance of {@link CancelJob }
     * 
     */
    public CancelJob createCancelJob() {
        return new CancelJob();
    }

    /**
     * Create an instance of {@link BindResponseType }
     * 
     */
    public BindResponseType createBindResponseType() {
        return new BindResponseType();
    }

    /**
     * Create an instance of {@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType }
     * 
     */
    public eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType createAttributedURIType() {
        return new eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType();
    }

    /**
     * Create an instance of {@link AddOrEditDomainResponseType }
     * 
     */
    public AddOrEditDomainResponseType createAddOrEditDomainResponseType() {
        return new AddOrEditDomainResponseType();
    }

    /**
     * Create an instance of {@link RemoveTopicResponseType }
     * 
     */
    public RemoveTopicResponseType createRemoveTopicResponseType() {
        return new RemoveTopicResponseType();
    }

    /**
     * Create an instance of {@link ActivateResponse }
     * 
     */
    public ActivateResponse createActivateResponse() {
        return new ActivateResponse();
    }

    /**
     * Create an instance of {@link AddTopicResponse }
     * 
     */
    public AddTopicResponse createAddTopicResponse() {
        return new AddTopicResponse();
    }

    /**
     * Create an instance of {@link CompleteJob }
     * 
     */
    public CompleteJob createCompleteJob() {
        return new CompleteJob();
    }

    /**
     * Create an instance of {@link DeleteEndpointResponse }
     * 
     */
    public DeleteEndpointResponse createDeleteEndpointResponse() {
        return new DeleteEndpointResponse();
    }

    /**
     * Create an instance of {@link AddEndpointType }
     * 
     */
    public AddEndpointType createAddEndpointType() {
        return new AddEndpointType();
    }

    /**
     * Create an instance of {@link GetReservationsResponse }
     * 
     */
    public GetReservationsResponse createGetReservationsResponse() {
        return new GetReservationsResponse();
    }

    /**
     * Create an instance of {@link EditDomainResponse }
     * 
     */
    public EditDomainResponse createEditDomainResponse() {
        return new EditDomainResponse();
    }

    /**
     * Create an instance of {@link GetEndpointsType }
     * 
     */
    public GetEndpointsType createGetEndpointsType() {
        return new GetEndpointsType();
    }

    /**
     * Create an instance of {@link AttributedAnyType }
     * 
     */
    public AttributedAnyType createAttributedAnyType() {
        return new AttributedAnyType();
    }

    /**
     * Create an instance of {@link GetReservation }
     * 
     */
    public GetReservation createGetReservation() {
        return new GetReservation();
    }

    /**
     * Create an instance of {@link AddDomainResponseType }
     * 
     */
    public AddDomainResponseType createAddDomainResponseType() {
        return new AddDomainResponseType();
    }

    /**
     * Create an instance of {@link DeleteDomainResponseType }
     * 
     */
    public DeleteDomainResponseType createDeleteDomainResponseType() {
        return new DeleteDomainResponseType();
    }

    /**
     * Create an instance of {@link EditLink }
     * 
     */
    public EditLink createEditLink() {
        return new EditLink();
    }

    /**
     * Create an instance of {@link GetReservationsComplexType }
     * 
     */
    public GetReservationsComplexType createGetReservationsComplexType() {
        return new GetReservationsComplexType();
    }

    /**
     * Create an instance of {@link PublishResponseType }
     * 
     */
    public PublishResponseType createPublishResponseType() {
        return new PublishResponseType();
    }

    /**
     * Create an instance of {@link EditLinkResponse }
     * 
     */
    public EditLinkResponse createEditLinkResponse() {
        return new EditLinkResponse();
    }

    /**
     * Create an instance of {@link GetLinksResponseType }
     * 
     */
    public GetLinksResponseType createGetLinksResponseType() {
        return new GetLinksResponseType();
    }

    /**
     * Create an instance of {@link CreateReservationType }
     * 
     */
    public CreateReservationType createCreateReservationType() {
        return new CreateReservationType();
    }

    /**
     * Create an instance of {@link OperationNotAllowedFault }
     * 
     */
    public OperationNotAllowedFault createOperationNotAllowedFault() {
        return new OperationNotAllowedFault();
    }

    /**
     * Create an instance of {@link ReservationFault }
     * 
     */
    public ReservationFault createReservationFault() {
        return new ReservationFault();
    }

    /**
     * Create an instance of {@link EditEndpointResponse }
     * 
     */
    public EditEndpointResponse createEditEndpointResponse() {
        return new EditEndpointResponse();
    }

    /**
     * Create an instance of {@link RemoveTopic }
     * 
     */
    public RemoveTopic createRemoveTopic() {
        return new RemoveTopic();
    }

    /**
     * Create an instance of {@link FixedReservationConstraintType }
     * 
     */
    public FixedReservationConstraintType createFixedReservationConstraintType() {
        return new FixedReservationConstraintType();
    }

    /**
     * Create an instance of {@link IsAvailableType }
     * 
     */
    public IsAvailableType createIsAvailableType() {
        return new IsAvailableType();
    }

    /**
     * Create an instance of {@link InvalidServiceIDFault }
     * 
     */
    public InvalidServiceIDFault createInvalidServiceIDFault() {
        return new InvalidServiceIDFault();
    }

    /**
     * Create an instance of {@link PathNotFoundFault }
     * 
     */
    public PathNotFoundFault createPathNotFoundFault() {
        return new PathNotFoundFault();
    }

    /**
     * Create an instance of {@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.MetadataType }
     * 
     */
    public eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.MetadataType createMetadataType() {
        return new eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.MetadataType();
    }

    /**
     * Create an instance of {@link GetReservationsType }
     * 
     */
    public GetReservationsType createGetReservationsType() {
        return new GetReservationsType();
    }

    /**
     * Create an instance of {@link CreateReservationResponseType }
     * 
     */
    public CreateReservationResponseType createCreateReservationResponseType() {
        return new CreateReservationResponseType();
    }

    /**
     * Create an instance of {@link DeleteDomainResponse }
     * 
     */
    public DeleteDomainResponse createDeleteDomainResponse() {
        return new DeleteDomainResponse();
    }

    /**
     * Create an instance of {@link AttributedUnsignedLongType }
     * 
     */
    public AttributedUnsignedLongType createAttributedUnsignedLongType() {
        return new AttributedUnsignedLongType();
    }

    /**
     * Create an instance of {@link AddDomainResponse }
     * 
     */
    public AddDomainResponse createAddDomainResponse() {
        return new AddDomainResponse();
    }

    /**
     * Create an instance of {@link GetReservationType }
     * 
     */
    public GetReservationType createGetReservationType() {
        return new GetReservationType();
    }

    /**
     * Create an instance of {@link ActivateType }
     * 
     */
    public ActivateType createActivateType() {
        return new ActivateType();
    }

    /**
     * Create an instance of {@link DeleteEndpointType }
     * 
     */
    public DeleteEndpointType createDeleteEndpointType() {
        return new DeleteEndpointType();
    }

    /**
     * Create an instance of {@link UnsubscribeType }
     * 
     */
    public UnsubscribeType createUnsubscribeType() {
        return new UnsubscribeType();
    }

    /**
     * Create an instance of {@link CompleteJobResponse }
     * 
     */
    public CompleteJobResponse createCompleteJobResponse() {
        return new CompleteJobResponse();
    }

    /**
     * Create an instance of {@link ConnectionStatusType }
     * 
     */
    public ConnectionStatusType createConnectionStatusType() {
        return new ConnectionStatusType();
    }

    /**
     * Create an instance of {@link Bind }
     * 
     */
    public Bind createBind() {
        return new Bind();
    }

    /**
     * Create an instance of {@link ServiceStatusType }
     * 
     */
    public ServiceStatusType createServiceStatusType() {
        return new ServiceStatusType();
    }

    /**
     * Create an instance of {@link EndpointAlreadyExistsFault }
     * 
     */
    public EndpointAlreadyExistsFault createEndpointAlreadyExistsFault() {
        return new EndpointAlreadyExistsFault();
    }

    /**
     * Create an instance of {@link Notification }
     * 
     */
    public Notification createNotification() {
        return new Notification();
    }

    /**
     * Create an instance of {@link InvalidRequestFault }
     * 
     */
    public InvalidRequestFault createInvalidRequestFault() {
        return new InvalidRequestFault();
    }

    /**
     * Create an instance of {@link Tuple }
     * 
     */
    public Tuple createTuple() {
        return new Tuple();
    }

    /**
     * Create an instance of {@link DeleteEndpointResponseType }
     * 
     */
    public DeleteEndpointResponseType createDeleteEndpointResponseType() {
        return new DeleteEndpointResponseType();
    }

    /**
     * Create an instance of {@link GetLinksResponse }
     * 
     */
    public GetLinksResponse createGetLinksResponse() {
        return new GetLinksResponse();
    }

    /**
     * Create an instance of {@link BindType }
     * 
     */
    public BindType createBindType() {
        return new BindType();
    }

    /**
     * Create an instance of {@link EndpointType }
     * 
     */
    public EndpointType createEndpointType() {
        return new EndpointType();
    }

    /**
     * Create an instance of {@link BindResponse }
     * 
     */
    public BindResponse createBindResponse() {
        return new BindResponse();
    }

    /**
     * Create an instance of {@link AddEndpoint }
     * 
     */
    public AddEndpoint createAddEndpoint() {
        return new AddEndpoint();
    }

    /**
     * Create an instance of {@link EditDomain }
     * 
     */
    public EditDomain createEditDomain() {
        return new EditDomain();
    }

    /**
     * Create an instance of {@link DomainInformationType }
     * 
     */
    public DomainInformationType createDomainInformationType() {
        return new DomainInformationType();
    }

    /**
     * Create an instance of {@link GetStatusResponseType.ServiceStatus }
     * 
     */
    public GetStatusResponseType.ServiceStatus createGetStatusResponseTypeServiceStatus() {
        return new GetStatusResponseType.ServiceStatus();
    }

    /**
     * Create an instance of {@link CancelJobType }
     * 
     */
    public CancelJobType createCancelJobType() {
        return new CancelJobType();
    }

    /**
     * Create an instance of {@link NotificationType }
     * 
     */
    public NotificationType createNotificationType() {
        return new NotificationType();
    }

    /**
     * Create an instance of {@link GetReservationResponseType }
     * 
     */
    public GetReservationResponseType createGetReservationResponseType() {
        return new GetReservationResponseType();
    }

    /**
     * Create an instance of {@link EditLinkType }
     * 
     */
    public EditLinkType createEditLinkType() {
        return new EditLinkType();
    }

    /**
     * Create an instance of {@link AddLinkResponseType }
     * 
     */
    public AddLinkResponseType createAddLinkResponseType() {
        return new AddLinkResponseType();
    }

    /**
     * Create an instance of {@link AddTopicResponseType }
     * 
     */
    public AddTopicResponseType createAddTopicResponseType() {
        return new AddTopicResponseType();
    }

    /**
     * Create an instance of {@link AddLinkResponse }
     * 
     */
    public AddLinkResponse createAddLinkResponse() {
        return new AddLinkResponse();
    }

    /**
     * Create an instance of {@link EditEndpointType }
     * 
     */
    public EditEndpointType createEditEndpointType() {
        return new EditEndpointType();
    }

    /**
     * Create an instance of {@link EndpointTechnologyType }
     * 
     */
    public EndpointTechnologyType createEndpointTechnologyType() {
        return new EndpointTechnologyType();
    }

    /**
     * Create an instance of {@link GetEndpointsResponse }
     * 
     */
    public GetEndpointsResponse createGetEndpointsResponse() {
        return new GetEndpointsResponse();
    }

    /**
     * Create an instance of {@link UnexpectedFault }
     * 
     */
    public UnexpectedFault createUnexpectedFault() {
        return new UnexpectedFault();
    }

    /**
     * Create an instance of {@link GetReservationsResponseType }
     * 
     */
    public GetReservationsResponseType createGetReservationsResponseType() {
        return new GetReservationsResponseType();
    }

    /**
     * Create an instance of {@link RelatesToType }
     * 
     */
    public RelatesToType createRelatesToType() {
        return new RelatesToType();
    }

    /**
     * Create an instance of {@link AddOrEditDomainResponse }
     * 
     */
    public AddOrEditDomainResponse createAddOrEditDomainResponse() {
        return new AddOrEditDomainResponse();
    }

    /**
     * Create an instance of {@link RemoveTopicType }
     * 
     */
    public RemoveTopicType createRemoveTopicType() {
        return new RemoveTopicType();
    }

    /**
     * Create an instance of {@link GetTopics }
     * 
     */
    public GetTopics createGetTopics() {
        return new GetTopics();
    }

    /**
     * Create an instance of {@link TopologyFault }
     * 
     */
    public TopologyFault createTopologyFault() {
        return new TopologyFault();
    }

    /**
     * Create an instance of {@link DomainTechnologyType }
     * 
     */
    public DomainTechnologyType createDomainTechnologyType() {
        return new DomainTechnologyType();
    }

    /**
     * Create an instance of {@link Publish }
     * 
     */
    public Publish createPublish() {
        return new Publish();
    }

    /**
     * Create an instance of {@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType }
     * 
     */
    public eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType createEndpointReferenceType() {
        return new eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType();
    }

    /**
     * Create an instance of {@link EditEndpoint }
     * 
     */
    public EditEndpoint createEditEndpoint() {
        return new EditEndpoint();
    }

    /**
     * Create an instance of {@link GetLinks }
     * 
     */
    public GetLinks createGetLinks() {
        return new GetLinks();
    }

    /**
     * Create an instance of {@link AddEndpointResponse }
     * 
     */
    public AddEndpointResponse createAddEndpointResponse() {
        return new AddEndpointResponse();
    }

    /**
     * Create an instance of {@link RemoveTopicResponse }
     * 
     */
    public RemoveTopicResponse createRemoveTopicResponse() {
        return new RemoveTopicResponse();
    }

    /**
     * Create an instance of {@link SubscribeType }
     * 
     */
    public SubscribeType createSubscribeType() {
        return new SubscribeType();
    }

    /**
     * Create an instance of {@link AddTopicType }
     * 
     */
    public AddTopicType createAddTopicType() {
        return new AddTopicType();
    }

    /**
     * Create an instance of {@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType.FaultCause }
     * 
     */
    public eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType.FaultCause createBaseFaultTypeFaultCause() {
        return new eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType.FaultCause();
    }

    /**
     * Create an instance of {@link GetDomains }
     * 
     */
    public GetDomains createGetDomains() {
        return new GetDomains();
    }

    /**
     * Create an instance of {@link NotificationResponse }
     * 
     */
    public NotificationResponse createNotificationResponse() {
        return new NotificationResponse();
    }

    /**
     * Create an instance of {@link GetLinksType }
     * 
     */
    public GetLinksType createGetLinksType() {
        return new GetLinksType();
    }

    /**
     * Create an instance of {@link CancelJobResponse }
     * 
     */
    public CancelJobResponse createCancelJobResponse() {
        return new CancelJobResponse();
    }

    /**
     * Create an instance of {@link PublishResponse }
     * 
     */
    public PublishResponse createPublishResponse() {
        return new PublishResponse();
    }

    /**
     * Create an instance of {@link LinkAlreadyExistsFault }
     * 
     */
    public LinkAlreadyExistsFault createLinkAlreadyExistsFault() {
        return new LinkAlreadyExistsFault();
    }

    /**
     * Create an instance of {@link UnsubscribeResponse }
     * 
     */
    public UnsubscribeResponse createUnsubscribeResponse() {
        return new UnsubscribeResponse();
    }

    /**
     * Create an instance of {@link GetDomainsResponse }
     * 
     */
    public GetDomainsResponse createGetDomainsResponse() {
        return new GetDomainsResponse();
    }

    /**
     * Create an instance of {@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType.Description }
     * 
     */
    public eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType.Description createBaseFaultTypeDescription() {
        return new eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType.Description();
    }

    /**
     * Create an instance of {@link MalleableReservationConstraintType }
     * 
     */
    public MalleableReservationConstraintType createMalleableReservationConstraintType() {
        return new MalleableReservationConstraintType();
    }

    /**
     * Create an instance of {@link AddDomainType }
     * 
     */
    public AddDomainType createAddDomainType() {
        return new AddDomainType();
    }

    /**
     * Create an instance of {@link SubscribeResponse }
     * 
     */
    public SubscribeResponse createSubscribeResponse() {
        return new SubscribeResponse();
    }

    /**
     * Create an instance of {@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ReferenceParametersType }
     * 
     */
    public eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ReferenceParametersType createReferenceParametersType() {
        return new eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ReferenceParametersType();
    }

    /**
     * Create an instance of {@link DeleteDomain }
     * 
     */
    public DeleteDomain createDeleteDomain() {
        return new DeleteDomain();
    }

    /**
     * Create an instance of {@link GetDomainsResponseType }
     * 
     */
    public GetDomainsResponseType createGetDomainsResponseType() {
        return new GetDomainsResponseType();
    }

    /**
     * Create an instance of {@link GetTopicsResponseType }
     * 
     */
    public GetTopicsResponseType createGetTopicsResponseType() {
        return new GetTopicsResponseType();
    }

    /**
     * Create an instance of {@link LinkIdentifierType }
     * 
     */
    public LinkIdentifierType createLinkIdentifierType() {
        return new LinkIdentifierType();
    }

    /**
     * Create an instance of {@link IsAvailableResponseType }
     * 
     */
    public IsAvailableResponseType createIsAvailableResponseType() {
        return new IsAvailableResponseType();
    }

    /**
     * Create an instance of {@link AddLink }
     * 
     */
    public AddLink createAddLink() {
        return new AddLink();
    }

    /**
     * Create an instance of {@link Subscribe }
     * 
     */
    public Subscribe createSubscribe() {
        return new Subscribe();
    }

    /**
     * Create an instance of {@link DeleteDomainType }
     * 
     */
    public DeleteDomainType createDeleteDomainType() {
        return new DeleteDomainType();
    }

    /**
     * Create an instance of {@link ConnectionAvailabilityType }
     * 
     */
    public ConnectionAvailabilityType createConnectionAvailabilityType() {
        return new ConnectionAvailabilityType();
    }

    /**
     * Create an instance of {@link TimeoutFault }
     * 
     */
    public TimeoutFault createTimeoutFault() {
        return new TimeoutFault();
    }

    /**
     * Create an instance of {@link TopicNotFoundFault }
     * 
     */
    public TopicNotFoundFault createTopicNotFoundFault() {
        return new TopicNotFoundFault();
    }

    /**
     * Create an instance of {@link InterdomainLinkType }
     * 
     */
    public InterdomainLinkType createInterdomainLinkType() {
        return new InterdomainLinkType();
    }

    /**
     * Create an instance of {@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType }
     * 
     */
    public eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType createBaseFaultType() {
        return new eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType();
    }

    /**
     * Create an instance of {@link AttributedQNameType }
     * 
     */
    public AttributedQNameType createAttributedQNameType() {
        return new AttributedQNameType();
    }

    /**
     * Create an instance of {@link DeleteLinkResponse }
     * 
     */
    public DeleteLinkResponse createDeleteLinkResponse() {
        return new DeleteLinkResponse();
    }

    /**
     * Create an instance of {@link PublishType }
     * 
     */
    public PublishType createPublishType() {
        return new PublishType();
    }

    /**
     * Create an instance of {@link GetStatusType }
     * 
     */
    public GetStatusType createGetStatusType() {
        return new GetStatusType();
    }

    /**
     * Create an instance of {@link GetStatusResponse }
     * 
     */
    public GetStatusResponse createGetStatusResponse() {
        return new GetStatusResponse();
    }

    /**
     * Create an instance of {@link DeleteEndpoint }
     * 
     */
    public DeleteEndpoint createDeleteEndpoint() {
        return new DeleteEndpoint();
    }

    /**
     * Create an instance of {@link GetEndpointsResponseType }
     * 
     */
    public GetEndpointsResponseType createGetEndpointsResponseType() {
        return new GetEndpointsResponseType();
    }

    /**
     * Create an instance of {@link Unsubscribe }
     * 
     */
    public Unsubscribe createUnsubscribe() {
        return new Unsubscribe();
    }

    /**
     * Create an instance of {@link ServiceConstraintType }
     * 
     */
    public ServiceConstraintType createServiceConstraintType() {
        return new ServiceConstraintType();
    }

    /**
     * Create an instance of {@link AddEndpointResponseType }
     * 
     */
    public AddEndpointResponseType createAddEndpointResponseType() {
        return new AddEndpointResponseType();
    }

    /**
     * Create an instance of {@link Activate }
     * 
     */
    public Activate createActivate() {
        return new Activate();
    }

    /**
     * Create an instance of {@link DomainStatusType }
     * 
     */
    public DomainStatusType createDomainStatusType() {
        return new DomainStatusType();
    }

    /**
     * Create an instance of {@link AddDomain }
     * 
     */
    public AddDomain createAddDomain() {
        return new AddDomain();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "ProblemIRI")
    public JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType> createProblemIRI(eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType value) {
        return new JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType>(_ProblemIRI_QNAME, eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "EndpointReference")
    public JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType> createEndpointReference(eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType value) {
        return new JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType>(_EndpointReference_QNAME, eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "To")
    public JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType> createTo(eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType value) {
        return new JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType>(_To_QNAME, eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "From")
    public JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType> createFrom(eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType value) {
        return new JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType>(_From_QNAME, eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "ReplyTo")
    public JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType> createReplyTo(eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType value) {
        return new JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType>(_ReplyTo_QNAME, eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.MetadataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "Metadata")
    public JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.MetadataType> createMetadata(eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.MetadataType value) {
        return new JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.MetadataType>(_Metadata_QNAME, eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.MetadataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "Action")
    public JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType> createAction(eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType value) {
        return new JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType>(_Action_QNAME, eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wsrf/bf-2", name = "BaseFault")
    public JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType> createBaseFault(eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType value) {
        return new JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType>(_BaseFault_QNAME, eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "MessageID")
    public JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType> createMessageID(eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType value) {
        return new JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType>(_MessageID_QNAME, eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AttributedURIType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2005/08/addressing", name = "FaultTo")
    public JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType> createFaultTo(eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType value) {
        return new JAXBElement<eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType>(_FaultTo_QNAME, eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointReferenceType.class, null, value);
    }

}
