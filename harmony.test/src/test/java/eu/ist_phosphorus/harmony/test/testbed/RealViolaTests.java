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

package eu.ist_phosphorus.harmony.test.testbed;

import org.junit.Test;

public class RealViolaTests extends AbstractRealTestbedTests {
    // alex: does not work *sometimes* - very annoying @Test
    public void testVIOLA_10732_107135() {
        this
                .runFixedTest("VIOLA/GMPLS to VIOLA/MPLS", "10.7.3.2",
                        "10.7.13.5");
    }

    // alex: not possible anymore @Test
    public void testVIOLA_GMPLS_10731_10727() {
        this.runFixedTest("VIOLA/GMPLS", "10.7.3.1", "10.7.2.7");
    }

    @Test
    public void testVIOLA_GMPLS_10731_10737() {
        // for (int i = 0; i < 60;i++)
        this.runFixedTest("VIOLA/GMPLS", "10.7.3.1", "10.7.3.7");
    }

    // alex: not possible anymore @Test
    public void testVIOLA_GMPLS_10732_10726() {
        this.runFixedTest("VIOLA/GMPLS", "10.7.3.2", "10.7.2.6");
    }

    @Test
    public void testVIOLA_GMPLS_10732_10736() {
        // for (int i = 0; i < 6;i++)
        this.runFixedTest("VIOLA/GMPLS", "10.7.3.2", "10.7.3.6");
    }

    // alex: does not work *sometimes* - this is annoying @Test
    public void testVIOLA_MPLS_10712108_10712106() {
        this.runFixedTest("VIOLA/MPLS", "10.7.12.108", "10.7.12.106");
    }

    @Test
    public void testVIOLA_MPLS_107134_10713107() {
        this.runFixedTest("VIOLA/MPLS", "10.7.13.4", "10.7.13.107");
    }

    @Test
    public void testVIOLA_MPLS_107135_10713106() {
        this.runFixedTest("VIOLA/MPLS", "10.7.13.5", "10.7.13.106");
    }
}
