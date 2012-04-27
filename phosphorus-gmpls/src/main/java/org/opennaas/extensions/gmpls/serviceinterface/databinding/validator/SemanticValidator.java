package org.opennaas.extensions.gmpls.serviceinterface.databinding.validator;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.core.utils.Helpers;

public class SemanticValidator {

    /**
     * @param input
     * @throws InvalidRequestFaultException
     */
    public static void validateContent(final Object input)
	    throws InvalidRequestFaultException {

    }

    /**
     * @param start
     * @param stop
     * @throws InvalidRequestFaultException
     */
    private static void checkReservationTime(final Date start, final Date stop)
	    throws InvalidRequestFaultException {
	Date now = new Date();

	if (!(start.before(stop) && stop.after(now))) {
	    throw new InvalidRequestFaultException("Invalid reservation time! "
		    + start + " -> " + stop + ". Prozessing time: " + now);
	}
    }

    /**
     * @param start
     * @param stop
     * @throws InvalidRequestFaultException
     */
    private static void checkReservationTime(final XMLGregorianCalendar start,
	    final XMLGregorianCalendar stop)
	    throws InvalidRequestFaultException {
	checkReservationTime(Helpers.xmlCalendarToDate(start), Helpers
		.xmlCalendarToDate(stop));
    }

    /**
     * @param start
     * @param duration
     * @throws InvalidRequestFaultException
     */
    private static void checkReservationTime(
	    final XMLGregorianCalendar startXML, final long duration)
	    throws InvalidRequestFaultException {
	Date start = Helpers.xmlCalendarToDate(startXML);
	Date stop = new Date(start.getTime() + duration * 1000);

	checkReservationTime(start, stop);
    }

}
