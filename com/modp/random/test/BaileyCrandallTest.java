/*
 * Copyright 2005, Nick Galbreath -- nickg [at] modp [dot] com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *   Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *   Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 *   Neither the name of the modp.com nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This is the standard "new" BSD license:
 * http://www.opensource.org/licenses/bsd-license.php
 */

package com.modp.random.test;
import com.modp.random.BaileyCrandall;

import junit.framework.TestCase;

/**
 * Unit tests for the BCN random number generator
 * 
 * @author Nick Galbreath -- nickg [at] modp [dot] com
 * @version 1 -- 06-Jul-2005
 */
public class BaileyCrandallTest extends TestCase {
	/**
	 * Test to make sure implementation matches published results
	 *
	 */
	public void testCorrectness() {
		// sanity check
		assertEquals(100, results.length);

		BaileyCrandall r = new BaileyCrandall();
		r.setSeedRaw(5559060566555623L);
		for (int i = 0; i < 100; ++i) {
			// detla is 1e-15, or in other words
			// only the last digit may be off by 1 (rounding for printing)
			assertEquals(results[i], r.nextDoubleOpen(), 0.000000000000001);
		}
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(BaileyCrandallTest.class);
	}
	
	/**
	 * The first 100 results of the BCNRandom based from bcnrand.out
	 */
	public static final double[] results = { 0.766073574343168,
			0.384734052280235, 0.163140570236979, 0.021776022548249,
			0.164609939547147, 0.567863085411560, 0.766294758822025,
			0.916407477520993, 0.440060487373777, 0.354733639184163,
			0.652718285729268, 0.858948819461297, 0.291747767041481,
			0.795905866015334, 0.172854948057511, 0.750032527571501,
			0.656471438738546, 0.721742649594121, 0.013446393647782,
			0.841952766476458, 0.439880506768320, 0.281775205909329,
			0.964719703392539, 0.581326412961776, 0.018316903841526,
			0.352097183083918, 0.217712088728172, 0.110217742205424,
			0.581850027927220, 0.187551851158481, 0.693001489163858,
			0.090822376419092, 0.423854284368504, 0.367953407117543,
			0.021736614416170, 0.429830128099422, 0.381540296778468,
			0.494404778474202, 0.913772652909707, 0.695019673479899,
			0.733569970312044, 0.144075056822145, 0.484277583162355,
			0.531997618891108, 0.193018804213281, 0.208447872861195,
			0.404044643781900, 0.237438314329674, 0.833777999554756,
			0.363458282148855, 0.180227041342622, 0.579387970492220,
			0.216434183666006, 0.520005190316857, 0.047729260721411,
			0.409238497559179, 0.104170592296921, 0.705852435848167,
			0.110011011881767, 0.846807220231845, 0.924365288254287,
			0.079689706755419, 0.776539349492334, 0.674079950860739,
			0.694474180077189, 0.044885008100273, 0.197797001150380,
			0.692236194808811, 0.022343376748498, 0.973486165672710,
			0.415760042900002, 0.557328035924712, 0.908008239318928,
			0.289343752541326, 0.703314412291197, 0.942169107108058,
			0.042894997134771, 0.209156849557080, 0.674717099423677,
			0.269748035897328, 0.907308574802537, 0.362723606109944,
			0.905625348207726, 0.091395824851408, 0.415779499630617,
			0.398916091922109, 0.538087276948762, 0.288540009046710,
			0.473211168360288, 0.971866209420661, 0.506404988982169,
			0.232094943823875, 0.320607739363374, 0.597402503510628,
			0.315468529270727, 0.445496658198783, 0.238299802067477,
			0.493565234974979, 0.483013735265864, 0.356247141531708 };
}
