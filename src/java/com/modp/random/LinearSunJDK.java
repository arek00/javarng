/*
 * Copyright 2005, 2007 Nick Galbreath -- nickg [at] modp [dot] com
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
 * Re-implemtation of the PRNG from java.util.Random.
 *
 * <p>This is a re-implementation of the random number generation
 * that is used in Sun's <code>java.util.Random</code>.  It is unsynchronized
 * and so it is about 2x faster.
 * </p>
 *
 * @author Nick Galbreath nickg [at] modp [dot] com
 * @version 1 -- 06-Jul-2005
 */
public class LinearSunJDK implements RandomGenerator {

    private long seed;
    private final static long multiplier = 0x5DEECE66DL;
    private final static long addend = 0xBL;
    private final static long mask = (1L << 48) - 1;

    /* Constructor, initializes seed with current time
     * 
     */
    public LinearSunJDK() {
	setSeed(System.currentTimeMillis());
    }

    public LinearSunJDK(final long seed) {
	setSeed(seed);
    }

    public void setSeed(long seed) {
        this.seed = (seed ^ multiplier) & mask;
    }

    /* (non-Javadoc)
     * @see com.modp.random.RandomGenerator#next(int)
     */
    public long next(int numBits) {
	seed = (seed * multiplier + addend) & mask;
	return (int)(seed >>> (48 - numBits));
    }
}
