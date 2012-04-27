package org.opennaas.extensions.gmpls.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.xml.bind.JAXBException;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import org.opennaas.extensions.gmpls.client.utils.GmplsWS;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.core.utils.Config;

/**
 * JUnit test cases for the gmpls web service.
 * 
 * @author Alexander Zimmermann (zimmerm2@cs.uni-bonn.de)
 * @version $Id$
 */
public class TestGmplsWebservice {
	/**
	 * General timeout for a test.
	 */

	private static final int TIMEOUT = 20 * 1000;

	/** */
	private static boolean serviceAvailable = true;

	/** */
	private static int createdPathId;

	private static boolean isNew = true;

	public static final void setUpBeforeClass() throws Exception {
		final String eprUri = Config.getString("gmpls", "epr.gmpls");
		final int portSeperatorPos = eprUri.indexOf(':', 7);
		final String host = eprUri.substring(7, portSeperatorPos);

		final int port = Integer.parseInt(eprUri.substring(
				portSeperatorPos + 1, eprUri.indexOf('/', portSeperatorPos)));

		try {
			System.out.println("open Socket on host:" + host + "and port:"
					+ port);
			boolean status = InetAddress.getByName(host).isReachable(TIMEOUT);
			System.out.println("Socket opened: " + status);
			if (!status) {
				System.err.println("*** Service not running ***");
				TestGmplsWebservice.serviceAvailable = false;
			}
		} catch (final UnknownHostException e) {
			System.err.println("*** Unknown Host ***");
			TestGmplsWebservice.serviceAvailable = false;
		} catch (final IOException e) {
			System.err.println("*** Service not running ***");
			TestGmplsWebservice.serviceAvailable = false;
		}

	}

	/**
	 * Tear down after test suite ran.
	 * 
	 * @throws java.lang.Exception
	 *             An exception.
	 */
	@AfterClass
	public static final void tearDownAfterClass() throws Exception {
		// Insert methods for tear down if necessary
	}

	/** */
	private final int createPathBandwidth = 1000;
	/** */
	private final String createPathSourceTNA = "10.7.2.8";
	/** */
	private final String createPathDestinationTNA = "10.7.1.8";

	/**
	 * Setup before a test.
	 * 
	 * @throws java.lang.Exception
	 *             An exception
	 */
	@Before
	public final void setUp() throws Exception {
		if (isNew) {
			setUpBeforeClass();
			isNew = false;
		}
	}

	/**
	 * Tear down after a test.
	 * 
	 * @throws java.lang.Exception
	 *             An exception.
	 */
	@After
	public void tearDown() throws Exception {
		// Insert methods for tear down if necessary
	}

	/**
	 * This test will create and delete 50 paths.
	 * 
	 * @throws SAXException
	 * @throws SoapFault
	 * @throws IOException
	 * @throws JAXBException
	 * @throws InvalidRequestFaultException
	 */
	@Test()
	public final void testCreateDeletePath()
			throws InvalidRequestFaultException, JAXBException, IOException,
			SoapFault, SAXException {
		if (TestGmplsWebservice.serviceAvailable) {
			for (int i = 0; i < 1; i++) {

				final org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathResponseType response = GmplsWS
						.createPath(this.createPathSourceTNA,
								this.createPathDestinationTNA,
								this.createPathBandwidth);
				TestGmplsWebservice.createdPathId = response
						.getPathIdentifier().getPathIdentifier();

				final org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePathResponseType response2 = GmplsWS
						.terminatePath(TestGmplsWebservice.createdPathId);
				Assert.assertNotNull(response2);
			}
		} else {
			Assert.fail("Service unavailable");
		}
	}

	/**
	 * Test Path creation service.
	 * 
	 * @throws SAXException
	 * @throws SoapFault
	 * @throws IOException
	 * @throws JAXBException
	 * @throws InvalidRequestFaultException
	 */
	@Test(timeout = TestGmplsWebservice.TIMEOUT)
	public final void testCreatePath() throws InvalidRequestFaultException,
			JAXBException, IOException, SoapFault, SAXException {
		if (TestGmplsWebservice.serviceAvailable) {

			final org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathResponseType response = GmplsWS
					.createPath(this.createPathSourceTNA,
							this.createPathDestinationTNA,
							this.createPathBandwidth);
			TestGmplsWebservice.createdPathId = response.getPathIdentifier()
					.getPathIdentifier();
		} else {
			Assert.fail("Service unavailable");
		}

	}

	/**
	 * Test Path creation service (This test should fail).
	 * 
	 * @throws SAXException
	 * @throws SoapFault
	 * @throws IOException
	 * @throws JAXBException
	 * @throws InvalidRequestFaultException
	 */
	@Test(expected = UnexpectedFaultException.class)
	public final void testFailCreatePath() throws InvalidRequestFaultException,
			JAXBException, IOException, SoapFault, SAXException {
		if (TestGmplsWebservice.serviceAvailable) {

			final org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathResponseType response = GmplsWS
					.createPath(this.createPathSourceTNA,
							this.createPathDestinationTNA,
							this.createPathBandwidth);
			TestGmplsWebservice.createdPathId = response.getPathIdentifier()
					.getPathIdentifier();
		} else {
			Assert.fail("Service unavailable");
		}
	}

	/**
	 * Test getEndpointInfo service.
	 */
	@Test(timeout = TestGmplsWebservice.TIMEOUT)
	public final void testGetEndpointInfo() {
		Assert.fail("not implemented");
	}

	/**
	 * Test getPathDiscovery service.
	 */
	@Test(timeout = TestGmplsWebservice.TIMEOUT)
	public final void testGetPathDiscovery() {
		Assert.fail("not implemented");
	}

	/**
	 * Test getPathStatus service.
	 */
	@Test(timeout = TestGmplsWebservice.TIMEOUT)
	public final void testGetPathStatus() {
		Assert.fail("not implemented");
	}

	/**
	 * Test Path termination service.
	 * 
	 * @throws SAXException
	 * @throws SoapFault
	 * @throws IOException
	 * @throws JAXBException
	 * @throws InvalidRequestFaultException
	 */
	@Test(timeout = TestGmplsWebservice.TIMEOUT)
	public final void testTerminatePath() throws InvalidRequestFaultException,
			JAXBException, IOException, SoapFault, SAXException {
		if (TestGmplsWebservice.serviceAvailable) {

			final org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePathResponseType response = GmplsWS
					.terminatePath(TestGmplsWebservice.createdPathId);
			Assert.assertNotNull(response);
		} else {
			Assert.fail("Service unavailable");
		}
	}

}
