package org.opennaas.extensions.gmpls.client.utils;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.BaseFaultType;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.AJaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.AbstractFaultException;

public class GmplsFaultException extends AbstractFaultException {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opennaas.extensions.idb.serviceinterface.databinding.utils.
	 * AbstractFaultException#getSerializer()
	 */
	@Override
	protected AJaxbSerializer getSerializer() {
		// TODO Auto-generated method stub
		return JaxbSerializer.getInstance();
	}

	public GmplsFaultException(BaseFaultType fault, String message,
			Throwable cause) {
		super(fault, message, cause);
		// TODO Auto-generated constructor stub
	}

	public GmplsFaultException(BaseFaultType fault, String message) {
		super(fault, message);
		// TODO Auto-generated constructor stub
	}

	public GmplsFaultException(BaseFaultType fault, Throwable cause) {
		super(fault, cause);
		// TODO Auto-generated constructor stub
	}

	public GmplsFaultException(BaseFaultType fault) {
		super(fault);
		// TODO Auto-generated constructor stub
	}

}
