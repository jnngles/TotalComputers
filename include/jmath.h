/*
    Plugin for computers in vanilla minecraft!
    Copyright (C) 2022  JNNGL

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

#ifndef TC_MATH_H
#define TC_MATH_H

#include "object.h"
#include "util.h"

/// STRUCT

namespace math {
    class BigInteger;
}

/// CLASSES

namespace math {

class BigInteger : public lang::Number {
public:
    BigInteger(jobject instance);
    ~BigInteger();

public:
    BigInteger(signed char* val, int valc);
    BigInteger(int signum, signed char* magnitude, int magnitudec);
    BigInteger(int bitLength, int certainty, util::Random rnd);
    BigInteger(int numBits, util::Random rnd);
    BigInteger(const char* val);
    BigInteger(const char* cal, int radix);

public:
    BigInteger Abs();
    BigInteger Add(BigInteger val);
    BigInteger And(BigInteger val);
    BigInteger AndNot(BigInteger val);
    int BitCount();
    int BitLength();
    BigInteger ClearBit(int n);
    int CompareTo(BigInteger val);
    BigInteger Divide(BigInteger val);
    BigInteger* DivideAndRemainder(int* size, BigInteger val);
    double DoubleValue();
    bool Equals(lang::Object x);
    BigInteger FlipBit(int n);
    float FloatValue();
    BigInteger Gcd(BigInteger val);
    int GetLowestSetBit();
    int HashCode();
    int IntValue();
    bool IsProbablePrime(int certainty);
    long LongValue();
    BigInteger Max(BigInteger val);
    BigInteger Min(BigInteger val);
    BigInteger Mod(BigInteger m);
    BigInteger ModInverse(BigInteger m);
    BigInteger ModPow(BigInteger exponent, BigInteger m);
    BigInteger Multiply(BigInteger val);
    BigInteger Negate();
    BigInteger NextProbablePrime();
    BigInteger Not();
    BigInteger Or(BigInteger val);
    BigInteger Pow(int exponent);
    static BigInteger ProbablePrime(int bitLength, util::Random rnd);
    BigInteger Remainder(BigInteger val);
    BigInteger SetBit(int n);
    BigInteger ShiftLeft(int n);
    BigInteger ShiftRight(int n);
    int Signum();
    BigInteger Subtract(BigInteger val);
    bool TestBit(int n);
    signed char* ToByteArray(int* size);
    char* ToString();
    char* ToString(int radix);
    static BigInteger ValueOf(long val);
    BigInteger Xor(BigInteger val);

public:
    jobject instance;

};

}

#endif