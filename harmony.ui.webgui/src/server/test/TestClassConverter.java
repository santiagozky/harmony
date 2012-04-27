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


/**
 * 
 */
package server.test;

import server.common.NspConverter;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;

/**
 * @author gassen
 */
public class TestClassConverter {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        // TODO Auto-generated method stub
        final DomainStatusType src = new DomainStatusType();

        src.setDomain("bla");
        src.setStatus(StatusType.PENDING);

        final NspConverter converter = new NspConverter();

        GetDomainsResponseType in = new GetDomainsResponseType();
        final DomainInformationType type = new DomainInformationType();
        type.setDescription("test domain information");

        in.getDomains().add(type);

        final client.classes.nsp.GetDomainsResponseType out =
                (client.classes.nsp.GetDomainsResponseType) converter
                        .convert(in);

        System.out.println("In... "
                + ((out.getDomains().get(0))).getDescription());

        in = null;
        in = (GetDomainsResponseType) converter.convert(out);

        System.out.println("and out: "
                + in.getDomains().get(0).getDescription());

        final String test = (String) converter.convert("test");
        System.out.println(test);
    }

}
