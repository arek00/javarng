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
 * The Bailey-Crandall random number generator.
 * 
 * <p>
 * This generator is based on properties of the a<sub>2,3</sub>, a "2-normal" number.
 * Normal numbers have the interesting property that the binary expansion (infinite)
 * contains every binary string in exactly same frequency as a "truly random sequence."
 * </p>
 * <p>The algorithm is unusual in that it natively computes random floating-point numbers instead
 * of the usual random bits.</p>
 * <ol>
 * <li>Select seed <i>s</i> in the range [ 3<sup>33</sup>, 2<sup>53</sup>] </li>
 * <li>Compute <i>x</i> = 2<sup><i>s</i> - (3^33)</sup> * Floor(3<sup>33</sup>/2) mod 3<sup>33</sup></li>
 * <li>Compute a "random" 64-bit IEEE double value with
 * <ol>
 * <li>Compute <i>x</i> = 2<sup>53</sup><i>x</i> mod  3<sup>33</sup></li>
 * <li>Return <i>x</i>3<sup>-33</sup></li>
 * </ol>
 * </li>
 * </ol>
 * <p>
 * In order to do these computations, "double double" (128-bit) arithmetic is used.
 * </p>
 * 
 * <p>
 * The performance could be improved if Java allowed "fused multiply-add" available
 * on PowerPC and some other microprocessors.
 * </p>
 * 
 * <p>
 * References:
 * </p>
 * <ul>
 * <li>Bailey, David H., <a href="http://crd.lbl.gov/~dhbailey/dhbpapers/normal-random.pdf"><i>
 * A Pseudo-Random Number Generator Based on Normal Numbers</i></a>, 11 Dec 2004 
</li>
 * <li>Bailey, David H., and Crandall, Richard. "<i>Random Generators and Normal Numbers</i>," Experimental Mathematics, vol 11, no. 4 (2004) pg. 527-546.
 * <li><a href="http://mathworld.wolfram.com/NormalNumber.html">Mathworld -- Normal Number</a></li>
 * </ul>
 * 
 * @author Nick Galbreath -- nickg [at] modp [dot] com
 * @version 1 06-Jul-05
 */
public class BaileyCrandall implements RandomGenerator {
	/**
	 * Constant: 3<sup>33</sup>
	 */
	private static final double POW3_33 = 5559060566555523.0;

	/**
	 * Constant: Math.floor((3<sup>33</sup>)/2)
	 */
	private static final double POW3_33_DIV_2 = 2779530283277761.0;

	/**
	 * Constant: 2<sup>53</sup>
	 */
	private static final long POW2_53 = 9007199254740992L;

	// the iterates
	private double d1;

	// tmp variables
	private double[] dd1 = new double[2];

	private double[] dd2 = new double[2];

	private double[] dd3 = new double[2];

	/**
	 * Constructor. Seed set to current time.
	 */
	public BaileyCrandall() {
		setSeed(System.currentTimeMillis());
	}

	/**
	 * Constructor with seed.
	 * 
	 * @param seed
	 */
	public BaileyCrandall(final long seed) {
		setSeed(seed);
	}

	/**
	 * Resets internal state with new seed
	 */
	public void setSeed(long seed) {
		if (seed < POW3_33 + 100) {
			seed += POW3_33 + 100;
		}
		seed &= (POW2_53 - 1L);
		setSeedRaw(seed);
	}

	/**
	 * Set the raw seed or state to match original fortran code
	 * 
	 * @param seed
	 *            3<sup>33</sup>+100 <= seed < 2<sup>53</sup>
	 */
	public void setSeedRaw(final long seed) {
		// TBD: add check, throw exception
		ddmuldd(expm2((double) seed - POW3_33, POW3_33), POW3_33_DIV_2, dd1);
		dddivd(dd1, POW3_33, dd2);
		ddmuldd(Math.floor(dd2[0]), POW3_33, dd2);
		ddsub(dd1, dd2, dd3);
		d1 = dd3[0];
	}

	/**
	 * Compute the next iterate. This is mostly useful for
	 * unit tests.
	 *  
	 */
	 public void nextIterate() {
		dd1[0] = POW2_53 * d1;
		dd1[1] = 0.0;
		dddivd(dd1, POW3_33, dd2);
		ddmuldd(POW3_33, Math.floor(dd2[0]), dd2);
		ddsub(dd1, dd2, dd3);
		d1 = dd3[0];
		if (d1 < 0.0) {
			d1 += POW3_33;
		}
	}

	/**
	 * Get the internal iterate (or state) Used for debugging and validation.
	 * 
	 * @return double
	 */
	public double getIterate() {
		return d1;
	}

	/**
	 * Returns a random value in the half-open interval [0,1) as per java spec.
	 * 
	 * @return double result
	 */
	public double nextDouble() {
		double result = (d1 - 1.0) / (POW3_33 - 1.0);
		nextIterate();
		return result;
	}

	/**
	 * Returns a random value in (0,1) NOT [0,1) as commonly done in java.
	 * 
	 * @return double result
	 */
	public double nextDoubleOpen() {
		double result = d1 / POW3_33;
		nextIterate();
		return result;
	}

	/** 
	 * Get n random bits as an integer
	 * 
	 * @param numBits  number of bits
	 * @return an integer
	 */
	public int next(final int numBits) {
		
		// floating point method
		// return (int) Math.round(nextDouble() * (1 << numBits -1));
		
		// direct inspection of iterate bits 0-51 are the mantissa
		// and should be random
		return (int)((Double.doubleToRawLongBits(nextDouble()) & 0x000fffffffffffffL) >> (52 - numBits));
	}
	
	/**
	 * Computes 2^p mod am
	 * 
	 * @param p
	 *            exponent
	 * @param am
	 *            modulus
	 * @return result
	 */
	private double expm2(final double p, final double am) {
		double ptl = 1;
		while (ptl < p) {
			ptl *= 2;
		}
		ptl /= 2;

		double p1 = p;
		double r = 1.0;
		double[] ddm = { am, 0.0 };
		while (true) {
			if (p1 >= ptl) {
				// r = (2*r) mod am
				ddmuldd(2.0, r, dd1);
				if (dd1[0] > am) {
					// dd1 -= ddm
					ddsub(dd1, ddm, dd2);
					dd1[0] = dd2[0];
					dd1[1] = dd2[1];
				}
				r = dd1[0];
				p1 -= ptl;
			}
			ptl *= 0.5;
			if (ptl >= 1.0) {
				/*
				 * r*r mod am == r*r - floor(r*r / am) * am
				 */
				ddmuldd(r, r, dd1);
				dddivd(dd1, am, dd2);
				ddmuldd(am, Math.floor(dd2[0]), dd2);
				ddsub(dd1, dd2, dd3);
				r = dd3[0];
				if (r < 0.0)
					r += am;
			} else {
				return r;
			}
		}
	}

	/**
	 * Used to split doubles into hi and lo words
	 */
	private static final double SPLIT = 134217729.0;

	/**
	 * Double precision multiplication
	 * 
	 * @param a
	 *            in: double
	 * @param b
	 *            in: double
	 * @param c
	 *            out: double double
	 */
	private final static void ddmuldd(final double a, final double b, double[] c) {
		double cona = a * SPLIT;
		double conb = b * SPLIT;
		double a1 = cona - (cona - a);
		double b1 = conb - (conb - b);
		double a2 = a - a1;
		double b2 = b - b1;
		double s1 = a * b;
		c[0] = s1;
		c[1] = (((a1 * b1 - s1) + a1 * b2) + a2 * b1) + a2 * b2;
		return;
	}

	/**
	 * Double Precision division
	 * 
	 * Double-double / double = double double
	 * 
	 * @param a
	 *            In: double double
	 * @param b
	 *            In: double
	 * @param c
	 *            Out: double double
	 */
	private final static void dddivd(final double[] a, final double b, double[] c) {
		double t1 = a[0] / b;
		double cona = t1 * SPLIT;
		double conb = b * SPLIT;
		double a1 = cona - (cona - t1);
		double b1 = conb - (conb - b);
		double a2 = t1 - a1;
		double b2 = b - b1;
		double t12 = t1 * b;
		double t22 = (((a1 * b1 - t12) + a1 * b2) + a2 * b1) + a2 * b2;
		double t11 = a[0] - t12;
		double e = t11 - a[0];
		double t21 = ((-t12 - e) + (a[0] - (t11 - e))) + a[1] - t22;
		double t2 = (t11 + t21) / b;
		double s1 = t1 + t2;
		c[0] = s1;
		c[1] = t2 - (s1 - t1);
		return;
	}

	/**
	 * Double-Precision subtraction a-b = c
	 * 
	 * @param a
	 *            in: double-double
	 * @param b
	 *            in: double-double
	 * @param c
	 *            out: double-double result
	 */
	private final static void ddsub(final double[] a, final double[] b, double[] c) {
		double t1 = a[0] - b[0];
		double e = t1 - a[0];
		double t2 = ((-b[0] - e) + (a[0] - (t1 - e))) + a[1] - b[1];
		double s1 = t1 + t2;
		c[0] = s1;
		c[1] = t2 - (s1 - t1);
		return;
	}
}
