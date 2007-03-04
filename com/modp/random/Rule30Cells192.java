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

/**
 * A random number generator based on Celluar Automaton Rule 30.
 * 
 * <p>
 * This implementation uses an circular array of 192 cells. 
 * Each cell is updated, in parallel, based on the three values, the left neighbor, itself, and the right neighbor.
 * according to "rule 30" defined by:
 * </p>
 * <table border="1" style="text-align: center">
 * <tr><td>111</td><td>110</td><td>101</td><td>100</td><td>011</td><td>010</td><td>001</td><td>000</td></tr>
 * <tr><td> 0 </td><td> 0 </td><td> 0 </td><td> 1 </td><td> 1 </td><td> 1 </td><td> 1 </td><td> 0 </td></tr>
 * </table>
 * <p>
 * The central or middle cell contains the "random" value.
 * </p>
 * 
 * <p>
 * Using a seed of <code>1L << 32</code> and subsequent calls to
 * <code>next(1)</code> will produce a sequence of bits equivalent to
 * (in <i>Mathematica</i>):
 * </p>
 * <pre>
 * CellularAutomaton[30,
 *   ReplacePart[Table[0, {192}], 1, 192/2],
 *   <i>n</i>,
 *   {All, {192/2 - 1}}]
 * </pre>
 * 
 * <p>WARNING: Rule 30 has some obvious practical problems.  Some initial conditions
 * will result in loops or terminate (e.g. all 0s).
 * These problems are <b>NOT</b> corrected here.
 * This is a "pure" implementation designed for research (especially since this
 * is quite slow in java)
 * </p>
 * <p>
 * You may find the following references useful:
 * </p>
 * <ul>
 * <li><a href="http://atlas.wolfram.com/01/01/30/">http://atlas.wolfram.com/01/01/30/</a></li>
 * <li><a href="http://en.wikipedia.org/wiki/Cellular_automata">http://en.wikipedia.org/wiki/Cellular_automata</a></li>
 * <li><a href="http://www.wolframscience.com/nksonline/page-974b-text">http://www.wolframscience.com/nksonline/page-974b-text</a></li>
 * <li><a href="http://www.google.com/search?q=US+Patent+4,691,291">US Patent 4,691,291</a>
 *   (Granted, September 1, 1987, so it should expire in 2005)</li>
 * </ul>
 * 
 * @author Nick Galbreath -- nickg [at] modp [dot] com
 * @version 1 -- 06-Jul-2005
 */
public class Rule30Cells192 implements RandomGenerator {
	
	// the internal state
	private long w0;
	private long w1;
	private long w2;


	/**
	 *  Default constructor.
	 * Probably uses current time to seed generator
	 */
	public Rule30Cells192() {
		setSeed(System.currentTimeMillis());
	}

	/**
	 * Seeded constructor.
	 * 
	 * Input seed will be used as bits 64-128 in the generator
	 * 
	 * @param arg0
	 */
	public Rule30Cells192(final long arg0) {
		setSeed(arg0);
	}
	
	/**
	 * Constructor with full seed
	 * 
	 * @param w0
	 * @param w1
	 * @param w2
	 */
	public Rule30Cells192(final long w0, final long w1, final long w2) {
		setSeed(w0,w1,w2);
	}
	
	/**
	 * Set the seed using a single long value.
	 * 
	 * This will be used for bits 64-128.
	 */
	public void setSeed(final long seed) {
		setSeed(0L,seed, 0L);
	}

	/**
	 * Get the internal state of the generator.
	 * 
	 * This is used for unit-testing.  The data
	 * is bitsliced so it won't make pretty pictures
	 * if you try and plot it (or maybe it will!)
	 * 
	 * @return a long array with 3 values.
	 */
	public long[] getState() {
		long[] result = new long[3];
		result[0] = w0;
		result[1] = w1;
		result[2] = w2;
		return result;
	}
	
	/**
	 * Set the seed using 3 long values.
	 * 
	 * The three 64-bit long value define the initial starting conditions
	 * and the bit values are layed out as a bit-string from left to right
	 * <pre>
	 * w0-0 w0-1 .... w0-63 w1-0 w1-1 .... w1-63 w2-0 w2-1 ... w2-63
	 * </pre>
	 * 
	 * To get the clasical Rule 30 with "black dot" in the middle
	 * Use <code>(0L, 1L << 32, 0L)</code>
	 * 
	 * TODO: need to explain bitsliced layout
	 * 
	 * @param w0 bits 0-63
	 * @param w1 bits 64-127
	 * @param w2 bits 128-191
	 */
	public void setSeed(final long w0, final long w1, final long w2) {
		final int BLOCKS = 3;
		final int BITS_PER_BLOCK = 64;

		// this loop can certainly be unrolled, and the use of array eliminated
		// however this isn't critical and this shows how to extend
		// the algorithm for more blocks
		long input[] = {w0, w1, w2};      // pack into array to simply algorithm below
		long output[] = new long[BLOCKS]; // tmp variable for holding state

		for (int j = 0; j < BLOCKS * BITS_PER_BLOCK; ++j) {
			int inputBlock = j / BITS_PER_BLOCK;
			int inputPos = j % BITS_PER_BLOCK;
			int outputBlock = j % BLOCKS;
			int outputPos = j / BLOCKS;
			
			// get the bit we are working on
			// if it's 0, nothing to do
			// if it's 1, set the appropriate bit
			// MAYBE: use table instead of shifting.
			if ((input[inputBlock] & (1L << inputPos)) != 0L) {
				output[outputBlock] |= (1L << outputPos);
			}
		}
		this.w0 = output[0];
		this.w1 = output[1];
		this.w2 = output[2];
	}
	
	/*
	 * Generate up to 32 random bits
	 * 
	 * @see java.util.Random#next(int)
	 */
	public int next(final int bits) {
		int result = 0;
		long t0, t1, t2;
		
		// ROTATE LEFT foo = (foo << 1) | (foo >>> 63);
		// ROTATE RIGHT foo = (foo >> 1) | (foo << 63);
		for (int j = bits; j != 0; --j) {
			result = (result << 1) | (int) ((w0 >>> 32) & 1L);
			t0 = ((w2 >>> 1) | (w2 << 63)) ^ (w0 | w1);
			t2 = w1 ^ (w2 | ((w0 << 1) | (w0 >>> 63)));
			t1 = w0 ^ (w1 | w2);
			w0 = t0; w1 = t1; w2 = t2;
		}
		return result;
	}
}
