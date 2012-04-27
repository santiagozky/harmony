/**
 *
 */
package org.opennaas.extensions.gmpls.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de)
 */
public class Serializer {

    /**
     *
     */
    public Serializer() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param reservations
     */
    public static void saveToFile(
	    final List<CreateReservationResponseType> reservations) {
	try {
	    final FileOutputStream fout = new FileOutputStream(
		    "reservations.dat");
	    final ObjectOutputStream oos = new ObjectOutputStream(fout);
	    oos.writeObject(reservations);
	    oos.close();
	} catch (final Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<CreateReservationResponseType> loadFromFile() {
	List<CreateReservationResponseType> result = null;
	try {
	    final FileInputStream fin = new FileInputStream("reservations.dat");
	    final ObjectInputStream ois = new ObjectInputStream(fin);
	    result = (List<CreateReservationResponseType>) ois.readObject();
	    ois.close();
	} catch (final Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<EndpointType> loadUserEndpointFromFile() {
	List<EndpointType> result = new ArrayList<EndpointType>();
	try {
	    final File f = new File("userEndpoints.dat");
	    if (f.exists()) {
		final FileInputStream fin = new FileInputStream(f);
		final ObjectInputStream ois = new ObjectInputStream(fin);
		result = (List<EndpointType>) ois.readObject();
		ois.close();
	    }
	} catch (final Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    /**
     * @param endpoint
     */
    public static void saveUserEndpointToFile(final List<EndpointType> endpoint) {
	final List<EndpointType> toSave = new ArrayList<EndpointType>();
	for (final EndpointType endpointType : endpoint) {
	    if (endpointType.getInterface() == EndpointInterfaceType.USER) {
		toSave.add(endpointType);
	    }
	}
	try {
	    final FileOutputStream fout = new FileOutputStream(
		    "userEndpoints.dat");
	    final ObjectOutputStream oos = new ObjectOutputStream(fout);
	    oos.writeObject(toSave);
	    oos.close();
	} catch (final Exception e) {
	    e.printStackTrace();
	}
    }

}
