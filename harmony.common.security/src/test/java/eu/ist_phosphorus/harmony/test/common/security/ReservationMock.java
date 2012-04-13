package eu.ist_phosphorus.harmony.test.common.security;

import org.mockito.Mockito;
import org.w3c.dom.Element;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.IReservationWS;

public class ReservationMock implements IReservationWS {

	public Element activate(Element activate) throws Exception {
		return Mockito.mock(org.w3c.dom.Element.class);

	}

	public Element bind(Element bind) throws Exception {
		return Mockito.mock(org.w3c.dom.Element.class);
	}

	public Element cancelJob(Element cancelJob) throws Exception {
		return Mockito.mock(org.w3c.dom.Element.class);
	}

	public Element cancelReservation(Element cancelReservation)
			throws Exception {
		return Mockito.mock(org.w3c.dom.Element.class);
	}

	public Element completeJob(Element completeJob) throws Exception {
		return Mockito.mock(org.w3c.dom.Element.class);
	}

	public Element createReservation(Element createReservation)
			throws Exception {
		return Mockito.mock(org.w3c.dom.Element.class);
	}

	public Element getReservation(Element getReservation) throws Exception {
		return Mockito.mock(org.w3c.dom.Element.class);
	}

	public Element getReservations(Element getReservations) throws Exception {
		return Mockito.mock(org.w3c.dom.Element.class);
	}

	public Element getStatus(Element getStatus) throws Exception {
		return Mockito.mock(org.w3c.dom.Element.class);
	}

	public Element isAvailable(Element isAvailable) throws Exception {
		return Mockito.mock(org.w3c.dom.Element.class);
	}

	public Element notification(Element notification) throws Exception {
		return Mockito.mock(org.w3c.dom.Element.class);
	}

}
