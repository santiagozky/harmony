package org.opennaas.extensions.gmpls.client.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType;

/**
 * @author Daniel Beer (daniel.beer@iais.fraunhofer.de)
 */
public final class EndpointListSorter {

    /**
     * private constructor.
     */
    private EndpointListSorter() {
	// private
    }

    /**
     * Sorts a list of EndpointTypes.
     * 
     * @param list
     *            the list to be sorted
     */
    public static void sort(
	    final List<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType> list) {
	EndpointType[] array = new EndpointType[list.size()];
	array = list.toArray(array);
	Arrays.sort(array, new EndpointComparator());

	list.clear();
	list.addAll(Arrays.asList(array));
    }

    public static void sortHarmonyEndpoints(
	    final List<org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType> list) {
	org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType[] array = new org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType[list
		.size()];
	array = list.toArray(array);
	Arrays.sort(array, new HarmonyEndpointComparator());

	list.clear();
	list.addAll(Arrays.asList(array));
    }

    /**
     * @author Daniel Beer (daniel.beer@iais.fraunhofer.de)
     */
    public static class EndpointComparator implements Comparator<EndpointType> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public final int compare(final EndpointType e1, final EndpointType e2) {
	    return e1.getEndpointId().compareTo(e2.getEndpointId());
	}

    }

    /**
     * @author Daniel Beer (daniel.beer@iais.fraunhofer.de)
     */
    public static class HarmonyEndpointComparator
	    implements
	    Comparator<org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public final int compare(
		final org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType e1,
		final org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType e2) {
	    return e1.getEndpointId().compareTo(e2.getEndpointId());
	}

    }
}
