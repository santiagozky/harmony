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

public final class RealWorkflowTests extends AbstractRealTestbedTests {

    @Test
    public void testWorkflow_10319_107135() {
        this.runFixedTest("CRC to VIOLA/MPLS", "10.3.1.9", "10.7.13.5");
    }

    @Test
    public void testWorkflow_10815_10319() {
        this.runFixedTest("CRC to I2cat", "10.3.1.10", "10.3.1.9");
    }

    @Test
    public void testWorkflow_10815_10612() {
        this.runFixedTest("CRC to UESSEX", "10.8.1.5", "10.6.1.2");
    }

    @Test
    public void testWorkflow_10815_107134() {
        this.runFixedTest("CRC to VIOLA/MPLS", "10.8.1.5", "10.7.13.4");
    }

    // @Test
    public void testWorkflow_SC08_IDC_10319_10923() {
        this.runFixedTest("", "10.3.1.9", "10.9.2.3");
    }

    // @Test
    public void testWorkflow_SC08_IDC_107134_10923() {
        this.runFixedTest("", "10.7.13.4", "10.9.2.3");
    }

    // @Test
    public void testWorkflow_SC08_WP1_103110_107134() {
        this.runFixedTest("", "10.3.1.10", "10.7.13.4");
    }

}
