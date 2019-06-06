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
 *
 * Portions may also be
 * Copyright (C) 1997 - 2002, Makoto Matsumoto and Takuji Nishimura,
 * All rights reserved.
 * (and covered under the BSD license)
 * See http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c
 */

package com.modp.random;

/**
 * Implementation of the Mersenne Twister random number generator.
 *
 * <p>
 * This mostly a straight-port of the
 * <a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c">
 * C code (mt19937ar.c)</a>, but have been
 * cleaned up a bit to make it more "java-like".
 * </p>
 *
 * <p>
 * References:
 * </p>
 * <ul>
 * <li>
 * <a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/emt.html">
 * The Mersenne Twister Home Page </a>
 * </li>
 * <li>  Makato Matsumoto and Takuji Nishimura,
 * <a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/ARTICLES/mt.pdf">"Mersenne Twister:A 623-Dimensionally Equidistributed Uniform Pseudo-Random Number Generator"</a>,
 * <i>ACM Transactions on Modeling and Computer Simulation, </i> Vol. 8, No. 1,
 * January 1998, pp 3--30.</li>
 * </ul>
 *
 * @author Nick Galbreath -- nickg [at] modp [dot] com
 * @version 1 -- 06-Jul-2005
 *
 */
public class MersenneTwister implements RandomGenerator {
    /**
     * N, Internal array size
     */
    private static final int N = 624;

    /**
     * M
     */
    private static final int M = 397;

    /**
     * State vector
     */
    private final int[] mt = new int[N];

    /**
     * Internal counter of position in state.
     * mti == N+1 means we haven't been initialized yet,.
     */
    private int mti = N+1;

    /**
     * Constants
     */
    private static final int mag01[] = {0x0, 0x9908b0df};

    /**
     * Constructor LSB of the current ime
     */
    public MersenneTwister() {
	setSeed((int) System.currentTimeMillis());
    }

    /**
     * Constructor using a given seed.
     */
    public MersenneTwister(final int seed) {
	setSeed(seed);
    }

    /**
     * Constructor using an array.
     */
    public MersenneTwister(final int[] array) {
	setSeed(array);
    }

    /**
     * Initalize the pseudo random number generator with 32-bits.
     */
    public void setSeed(final int seed) {
	mt[0] = seed;
	for (mti = 1; mti < N; mti++) {
	    mt[mti] = (1812433253 * (mt[mti - 1] ^ (mt[mti - 1] >>> 30)) + mti);
	}
    }

    /**
     * Direct seeding using an array.
     *
     */
    public void setSeed(final int[] array) {
	setSeed(19650218);
	int i = 1;
	int j = 0;
	int k = (N > array.length ? N : array.length);

	for (; k != 0; k--) {
	    mt[i] = (mt[i] ^ ((mt[i - 1] ^ (mt[i - 1] >>> 30)) * 1664525))
		+ array[j] + j;
	    i++;
	    j++;
	    if (i >= N) {
		mt[0] = mt[N - 1];
		i = 1;
	    }
	    if (j >= array.length)
		j = 0;
	}
	for (k = N - 1; k != 0; k--) {
	    mt[i] = (mt[i] ^ ((mt[i - 1] ^ (mt[i - 1] >>> 30)) * 1566083941))
		- i;
	    i++;
	    if (i >= N) {
		mt[0] = mt[N - 1];
		i = 1;
	    }
	}
	mt[0] = 0x80000000; // MSB is 1; assuring non-zero initial array
    }

    public long next(final int bits) {
	int y;
	if (mti >= N) {
	    int kk;
	    for (kk = 0; kk < N - M; kk++) {
		y = (mt[kk] & 0x80000000) | (mt[kk + 1] & 0x7fffffff);
		mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
	    }
	    for (; kk < N - 1; kk++) {
		y = (mt[kk] & 0x80000000) | (mt[kk + 1] & 0x7fffffff);
		mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
	    }
	    y = (mt[N - 1] & 0x80000000) | (mt[0] & 0x7fffffff);
	    mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];

	    mti = 0;
	}

	y = mt[mti++];
	y ^= y >>> 11;
	y ^= (y << 7) & 0x9d2c5680;
	y ^= (y << 15) & 0xefc60000;
	y ^= (y >>> 18);

	return y >>> (32 - bits);
    }
}
