# com.modp.random #

This package contain some unusual generators based on normal numbers and cellular automata as well as some old classics. These are designed to be used for research, and not as a replacement to the stock `Random` (which is fine for what it is). All of these should not be used for cryptography (`SecureRandom` is fine as is).

## The Generators ##

| [LinearSunJDK  ](http://javarng.googlecode.com/svn/trunk/com/modp/random/LinearSunJDK.java)|	The same algorithm as in com.util.Random but without synchronization |
|:-------------------------------------------------------------------------------------------|:---------------------------------------------------------------------|
| [BlumBlumShub](http://javarng.googlecode.com/svn/trunk/com/modp/random/BlumBlumShub.java)	 |	a 'cryptographically-secure' generator in which predicting the next bit is computationally equivalent to integer factorization. |
| [Rule30, 192 Cells](http://javarng.googlecode.com/svn/trunk/com/modp/random/Rule30Cells192.java) | Rule 30 celluar automaton using 192 cells                            |
| [Mersenne Twister](http://javarng.googlecode.com/svn/trunk/com/modp/random/MersenneTwister.java) |	a high speed and high quality generator with period 219937-1         |
| [Mersenne Twiser 64](http://javarng.googlecode.com/svn/trunk/com/modp/random/MersenneTwister64.java) |	Similar to the original but implemented using 64-bit registers.      |
| [Bailey-Crandall](http://javarng.googlecode.com/svn/trunk/com/modp/random/BaileyCrandall.java) |  	this is unusual in that it uses high-percision floating point computations to generate random bits. |

Detailed notes and references in are in the source.   javadoc output is [here](http://modp.com/release/javarng/javarng/doc/overview-summary.html)

With the exception of `BlumBlumShub`, all come with unit tests to insure that the algorithm is implemented correctly.

TODO:

  * Unit test for BlumBlumShub
  * Micali-Schnorr
  * IEEE P1363 SHA-1

## Introducing `RandomGenerator` ##

Ok, so a Random Number Generator, should generate bits, and bits only. It's an interface. It has one method.

```
public interface RandomGenerator {
   public int next(int numBits);
} 

```

That's it. How the generator is constructed, seeded or re-seeded is up to the generator. This greatly simplifies class construction since you don't worry about calling `super()` or worrying about synchronization.

## Java and Random Number Generation ##

And so, why Java? A few positive reasons first

  * Self-contained. Core java contains everything needed -- no external libraries needed. In particular:
    * Arbitrary precision integer arithmetic
    * SHA-1 hash algorithm
  * known integer sizes and layout. An int is 32 bits (32 signed bits, but we can managed that). No endian issues to deal with.
  * Reasonable performance. If you don't add gobs and gobs of abstraction (which unfortunately seems to be the norm in java), it performs quite well.
  * Easy _Mathematica_ integration


On the other hand, I don't use java that much any more (whoops).  At some point I may write a C/C++ version and add it to [Boost](http://www.boost.org/) or the [GNU Scientific Library](http://www.gnu.org/software/gsl/).

## Problems with `java.util.Random` ##

That said, the stock `java.util.Random` and it's twin `java.security.SecureRandom` have some problems.

  * Munges bit generation with bit consumers. Meaning, random bit generation is very different than algorithms to make random distributions.
  * Random's constructor calls `setSeed`. This is a problem since if you subclass Random, `setSeed` will be called before your runs. This limits static initialization and makes the code harder to understand.
  * Forced to use a long value for a seed even if it makes no sense for a particular generator
  * Random's private or protected variables are of no use to the subclass since they are algorithm-specific.
  * Harder to benchmark since class hierarchy causes the JIT to not make some optimizations.
  * Synchronized. Ugh. Random is a leftover from JDK1.0 days when everything was synchronized for you. In later editions they fixed this for the container classes (note `Vector` and `ArrayList`), but not Random. This might make sense for `SecureRandom` since you don't want two threads potentionally corrupting the internal state, but for regular random it's a waste.
  * To properly implement `SecureRandom`, you need to make a 'provider' and all sorts of other crap that is secondary
  * `SecureRandom` is closed source