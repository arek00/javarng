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
package com.modp.random;

import junit.framework.TestCase;
import java.util.Arrays;

/**
 * Unit tests for Rule30Cells192
 *
 * @author Nick Galbreath -- nickg [at] modp [dot] com
 * @version 1 -- 06-Jul-2005
 *
 */
public class Rule30Cells192Test extends TestCase {

    /**
     * Test case data.
     *
     * <p>
     * This is the first 500 steps in 192 cell CA, using the center column
     * iterated using rule 30.
     * </p>
     *
     * <p>
     * This can be generated in <i>Mathematica</i> with:
     * </p>
     * <pre>
     *
     *  Flatten[CellularAutomaton[30, ReplacePart[Table[0, {192}], 1, 192/2],
     *    10, {All, {192/2 - 1}}]]
     *
     * </pre>
     */
    private static final int[] results = { 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0,
					   0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1,
					   1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1,
					   1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0,
					   0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0,
					   0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0,
					   0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1,
					   0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1,
					   0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0,
					   0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1,
					   1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1,
					   0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1,
					   0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1,
					   0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1,
					   0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0,
					   0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0,
					   1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0,
					   1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1,
					   1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1,
					   1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1,
					   1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0,
					   1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1,
					   1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0,
					   0, 1, 0, 1, 1 };

    /**
     * Test implementation of bit slicing
     *
     */
    public void testBitSlice() {
	Rule30Cells192 r = new Rule30Cells192();

	// make sure nothing is set when it shouldn't be
	r.setSeed(0L, 0L, 0L);
	long[] result1 = { 0L, 0L, 0L };
	//hmm, odd, JUnit doesn't have assertEquals(int[], int[]) or any other
	// arrays types
	assertTrue(Arrays.equals(r.getState(), result1));

	// make sure there are no holes
	r.setSeed(0xffffffffffffffffL, 0xffffffffffffffffL,
		  0xffffffffffffffffL);
	long[] result2 = { 0xffffffffffffffffL, 0xffffffffffffffffL,
			   0xffffffffffffffffL };
	assertTrue(Arrays.equals(r.getState(), result2));

	//(binary) 10000....
	r.setSeed(1L, 0L, 0L);
	long[] result3 = { 1L, 0L, 0L };
	assertTrue(Arrays.equals(r.getState(), result3));

	// 0100000
	r.setSeed(2L, 0L, 0L);
	long[] result4 = { 0L, 1L, 0L };
	assertTrue(Arrays.equals(r.getState(), result4));

	// 0010000......
	r.setSeed(4L, 0L, 0L);
	long[] result5 = { 0L, 0L, 1L };
	assertTrue(Arrays.equals(r.getState(), result5));

	// 00010000......
	r.setSeed(8L, 0L, 0L);
	long[] result6 = { 2L, 0L, 0L };
	assertTrue(Arrays.equals(r.getState(), result6));
    }

    /**
     * Test to make sure generate is producing correct results
     */
    public void testCorrectness() {
	Rule30Cells192 r = new Rule30Cells192();
	r.setSeed(0L, 1L << 32, 0L);
	for (int i = 0; i < results.length; ++i) {
	    assertEquals("Step " + i, r.next(1), results[i]);
	}
    }

    public static void main(String[] args) {
	junit.textui.TestRunner.run(Rule30Cells192Test.class);
    }
}
