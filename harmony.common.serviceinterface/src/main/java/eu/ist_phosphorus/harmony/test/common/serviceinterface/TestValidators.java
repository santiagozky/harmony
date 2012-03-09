/**
 *  This code is part of the Harmony System implemented in Work Package 1 
 *  of the Phosphorus project. This work is supported by the European 
 *  Comission under the Sixth Framework Programme with contract number 
 *  IST-034115.
 *
 *  Copyright (C) 2006-2009 Phosphorus WP1 partners. Phosphorus Consortium.
 *  http://ist-phosphorus.eu/
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package eu.ist_phosphorus.harmony.test.common.serviceinterface;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservation;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.MalleableReservationConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ServiceConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.JaxbSerializer;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.validator.SemanticValidator;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.validator.SyntaxValidator;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.Helpers;

public class TestValidators {
    protected CreateReservationType resv1 = null;
    protected CreateReservationType resv2 = null;
    protected ServiceConstraintType service1 = null;
    protected ServiceConstraintType service2 = null;
    protected ConnectionConstraintType connection1 = null;
    protected EndpointType endpoint1 = null;
    protected EndpointType endpoint2 = null;

    protected long reservationId = 0;

    public TestValidators() {
        this.endpoint1 = new EndpointType();
        this.endpoint2 = new EndpointType();
        this.resv1 = new CreateReservationType();
        this.service1 = new ServiceConstraintType();

        final FixedReservationConstraintType reservationConstraintType = new FixedReservationConstraintType();
        reservationConstraintType.setDuration(5 * 60);
        reservationConstraintType.setStartTime(Helpers.generateXMLCalendar(-1,
                0));

        this.endpoint1.setEndpointId("1.2.3.4");
        this.endpoint2.setEndpointId("6.7.8.9");

        this.resv1 = new CreateReservationType();
        this.resv1.setJobID(Long.valueOf(0));

        this.service1 = new ServiceConstraintType();
        this.service1.setAutomaticActivation(false);
        this.service1.setServiceID((short) 1);
        this.service1.setFixedReservationConstraints(reservationConstraintType);
        this.service1.setTypeOfReservation(ReservationType.FIXED);

        this.connection1 = new ConnectionConstraintType();

        this.connection1.setSource(this.endpoint1);
        this.connection1.unsetTarget();
        this.connection1.getTarget().add(this.endpoint2);
        this.service1.unsetConnections();
        this.service1.getConnections().add(this.connection1);
        this.resv1.unsetService();
        this.resv1.getService().add(this.service1);

        final MalleableReservationConstraintType malleable = new MalleableReservationConstraintType();

        malleable.setStartTime(Helpers.generateXMLCalendar());
        malleable.setDeadline(Helpers.generateXMLCalendar(1, 0));

        this.resv2 = new CreateReservationType();

        this.service2 = new ServiceConstraintType();
        this.service2.setAutomaticActivation(false);
        this.service2.setServiceID((short) 1);
        this.service2.setMalleableReservationConstraints(malleable);
        this.service2.setTypeOfReservation(ReservationType.MALLEABLE);
        this.service2.unsetConnections();
        this.service2.getConnections().add(this.connection1);

        this.resv2.unsetService();
        this.resv2.getService().add(this.service2);
    }

    @Test
    public final void testMalleableReservationValidation() {
        try {
            SemanticValidator.validateContent(this.resv2);

            Assert.assertTrue(false);
        } catch (final InvalidRequestFaultException ex) {
            Assert.assertTrue(true);
        }

        this.resv2.getService().get(0).getConnections().get(0).setDataAmount(
                Long.valueOf(2));
    }

    @Test
    public final void testSemanticValidator()
            throws InvalidRequestFaultException {
        SemanticValidator.validateContent(this.resv1);
    }

    @Test
    public final void testSyntaxValidator()
            throws InvalidRequestFaultException, FileNotFoundException,
            SAXException, JAXBException, UnexpectedFaultException {
        final SyntaxValidator validator = new SyntaxValidator(Config.getURL(
                "databinding", "wsdl.validator"));

        final CreateReservation env = new CreateReservation();
        env.setCreateReservation(this.resv1);

        final String xml = JaxbSerializer.getInstance().objectToXml(env);

        validator.validate(xml);
    }

    /**
     * Need to be last test, because reservation is modified!!
     * 
     * @throws FileNotFoundException
     * @throws SAXException
     * @throws JAXBException
     * @throws UnexpectedFaultException
     */
    @Test
    public final void testSyntaxValidatorFail() throws FileNotFoundException,
            SAXException, JAXBException, UnexpectedFaultException {
        final SyntaxValidator validator = new SyntaxValidator(Config.getURL(
                "databinding", "wsdl.validator"));

        this.resv1.getService().clear();

        final CreateReservation env = new CreateReservation();
        env.setCreateReservation(this.resv1);

        try {
            final String xml = JaxbSerializer.getInstance().objectToXml(env);

            validator.validate(xml);

            Assert.assertTrue(false);
        } catch (final InvalidRequestFaultException e) {
            Assert.assertTrue(true);
        }
    }
}
