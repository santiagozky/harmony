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

package org.opennaas.extensions.idb.serviceinterface.databinding.validator;

import org.w3c.dom.Node;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;

/**
 * Nothing yet.
 * 
 * @todo Jan: describe function!
 * @author Jan Gassen (gassen@cs.uni-bonn.de)
 * 
 */
public class TypeValidator {

    /**
     * Nothing yet.
     * 
     * @todo Jan: describe function!
     * 
     */
    public TypeValidator() {
        // Nothing to do here
    }

    /**
     * Nothing yet.
     * 
     * @param element
     *            Element to be validated
     * @return TODO: describe
     * 
     * @todo Jan: describe function!
     * 
     */
    public final boolean isValid(final Node element) {
        if (null == element) {
            return false;
        }

        final String nodeName = element.getNodeName();

        if (nodeName.endsWith("Type")) {
            return false;
        }

        return true;
    }

    /**
     * Nothing yet.
     * 
     * @param element
     *            Element to be validated
     * @return TODO: describe
     * @throws InvalidRequestFaultException
     * 
     * @todo Jan: describe function!
     * 
     */
    public final void validate(final Node element)
            throws InvalidRequestFaultException {

        if (false == this.isValid(element)) {
            throw new InvalidRequestFaultException("Element has invalid type");
        }
    }
}
