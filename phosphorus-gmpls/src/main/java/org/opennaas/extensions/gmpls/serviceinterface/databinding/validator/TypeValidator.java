package org.opennaas.extensions.gmpls.serviceinterface.databinding.validator;

import org.w3c.dom.Node;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;

/**
 * Nothing yet.
 *
 * @todo   Jan: describe function!
 * @author Jan Gassen (gassen@cs.uni-bonn.de)
 *
 */
public class TypeValidator {

    /**
     * Nothing yet.
     *
     * @todo   Jan: describe function!
     *
     */
    public TypeValidator() {
        //Nothing to do here
    }

    /**
     * Nothing yet.
     * @param element Element to be validated
     * @return TODO: describe
     *
     * @todo   Jan: describe function!
     *
     */
    public final boolean isValid(final Node element) {
        if (null == element) {
            return false;
        }
        
        String nodeName = element.getNodeName();

        if (nodeName.endsWith("Type")) {
            return false;
        }

        return true;
    }
    
    /**
     * Nothing yet.
     * @param element Element to be validated
     * @return TODO: describe
     * @throws InvalidRequestFaultException 
     *
     * @todo   Jan: describe function!
     *
     */
    public final void validate(final Node element)
            throws InvalidRequestFaultException {
        
        if(false == isValid(element)) {
            throw new InvalidRequestFaultException("Element has invalid type");
        }
    }
}
