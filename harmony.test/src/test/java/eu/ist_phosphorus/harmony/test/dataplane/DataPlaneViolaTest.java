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


package eu.ist_phosphorus.harmony.test.dataplane;

import org.junit.Test;

import eu.ist_phosphorus.harmony.common.utils.Config;

public class DataPlaneViolaTest extends AbstractDataPlaneTest {

	public DataPlaneViolaTest() {
		super(Config.getString("dataplane", "dataplane.epr.reservation"));
	}

	@Test
	public void testI2CAT_VIOLA_10003_1000173() {
		this.runDataPlaneTest("i2CAT - VIOLA", "10.3.1.9", "10.7.13.4",
				"10.0.0.173");
	}

	@Test
	public void testI2CAT_VIOLA_10003_100073() {
		this.runDataPlaneTest("i2CAT - VIOLA", "10.3.1.9", "10.7.13.5",
				"10.0.0.73");
	}
	
	//@Test
	//public void testI2CAT_VIOLA_10003_100072() {
	//	this.runDataPlaneTest("i2CAT - VIOLA", "10.3.1.9", "10.7.12.108","10.0.0.72");
	//}
	
	
}
