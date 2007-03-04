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
package com.modp.random.misc;

import java.util.Random;
import com.modp.random.*;

/**
 * A simple benchmark program
 * 
 * @author nickg
 * @version 1
 */
public class SpeedTest {

	public static void main(String[] args) {
		final int iter = 10000000;
		long start, end;
		RandomGenerator r1 = new LinearSunJDK(1L);
		RandomGenerator r2 = new MersenneTwister(1);
		RandomGenerator r3 = new Rule30Cells192(1L << 32);
		RandomGenerator r4 = new BaileyCrandall(1L << 32);		
		RandomGenerator r5 = new BlumBlumShub(512);
		RandomGenerator r6 = new MersenneTwister64(1L);
		Random rand = new Random();

		for (int j = 0; j < 10; ++j) {
			System.out.println("\nROUND " + j + "\n==========");
			start = System.currentTimeMillis();
			for (int i = iter; i != 0; --i) {
				rand.nextInt();
			}
			end = System.currentTimeMillis();
			System.out.println("java.util.random   : " + (end - start) / 1000.0);

			
			start = System.currentTimeMillis();
			for (int i = iter; i != 0; --i) {
				r1.next(32);
			}
			end = System.currentTimeMillis();
			System.out.println("SunJDK             : " + (end - start) / 1000.0);

			
			start = System.currentTimeMillis();
			for (int i = iter; i != 0; --i) {
				r2.next(32);
			}
			end = System.currentTimeMillis();
			System.out.println("Mersenne Twister   : " + (end - start) / 1000.0);

			start = System.currentTimeMillis();
			for (int i = iter; i != 0; --i) {
				r6.next(32);
			}
			end = System.currentTimeMillis();
			System.out.println("Mersenne Twister 64: " + (end - start) / 1000.0);
			
			start = System.currentTimeMillis();
			for (int i = iter; i != 0; --i) {
				r3.next(32);
			}
			end = System.currentTimeMillis();
			System.out.println("Rule 30            : " + (end - start) / 1000.0);

			start = System.currentTimeMillis();
			for (int i = iter; i != 0; --i) {
				r4.next(32);
			}
			end = System.currentTimeMillis();
			System.out.println("Bailey-Crandall    : " + (end - start) / 1000.0);
			
			start = System.currentTimeMillis();
			for (int i = iter / 1000; i != 0; --i) {
				r5.next(32);
			}
			end = System.currentTimeMillis();
			System.out.println("BlumBlumShub 512  : " + (end - start) + " (estimated)");
		}
	}
}
