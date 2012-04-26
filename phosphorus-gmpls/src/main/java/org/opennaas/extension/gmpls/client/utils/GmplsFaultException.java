package org.opennaas.extension.gmpls.client.utils;

import org.opennaas.extension.gmpls.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.BaseFaultType;
import org.opennaas.extension.idb.serviceinterface.databinding.utils.AJaxbSerializer;
import org.opennaas.extension.idb.serviceinterface.databinding.utils.AbstractFaultException;

public class GmplsFaultException extends AbstractFaultException {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opennaas.extension.idb.serviceinterface.databinding.utils.
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
