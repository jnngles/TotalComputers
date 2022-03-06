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

#include "j_env.h"
#include "jmath.h"
#include "utils.h"

using namespace math;

// java/math/BigInteger
// Name: BigInteger
// Base Class(es): [lang::Number]

NativeObject clsBigInteger;
jclass InitBigInteger() {
   return tcInitNativeObject(clsBigInteger, "java/math/BigInteger", { 
          { "abs", "()Ljava/math/BigInteger;", false }, 
          { "add", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }, 
          { "and", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }, 
          { "andNot", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }, 
          { "bitCount", "()I", false }, 
          { "bitLength", "()I", false }, 
          { "clearBit", "(I)Ljava/math/BigInteger;", false }, 
          { "compareTo", "(Ljava/math/BigInteger;)I", false }, 
          { "divide", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }, 
          { "divideAndRemainder", "(Ljava/math/BigInteger;)[Ljava/math/BigInteger;", false }, 
          { "doubleValue", "()D", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "flipBit", "(I)Ljava/math/BigInteger;", false }, 
          { "floatValue", "()F", false }, 
          { "gcd", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }, 
          { "getLowestSetBit", "()I", false }, 
          { "hashCode", "()I", false }, 
          { "intValue", "()I", false }, 
          { "isProbablePrime", "(I)Z", false }, 
          { "longValue", "()J", false }, 
          { "max", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }, 
          { "min", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }, 
          { "mod", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }, 
          { "modInverse", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }, 
          { "modPow", "(Ljava/math/BigInteger;Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }, 
          { "multiply", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }, 
          { "negate", "()Ljava/math/BigInteger;", false }, 
          { "nextProbablePrime", "()Ljava/math/BigInteger;", false }, 
          { "not", "()Ljava/math/BigInteger;", false }, 
          { "or", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }, 
          { "pow", "(I)Ljava/math/BigInteger;", false }, 
          { "probablePrime", "(ILjava/util/Random;)Ljava/math/BigInteger;", true }, 
          { "remainder", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }, 
          { "setBit", "(I)Ljava/math/BigInteger;", false }, 
          { "shiftLeft", "(I)Ljava/math/BigInteger;", false }, 
          { "shiftRight", "(I)Ljava/math/BigInteger;", false }, 
          { "signum", "()I", false }, 
          { "subtract", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }, 
          { "testBit", "(I)Z", false }, 
          { "toByteArray", "()[B", false }, 
          { "toString", "()Ljava/lang/String;", false }, 
          { "toString", "(I)Ljava/lang/String;", false }, 
          { "valueOf", "(J)Ljava/math/BigInteger;", true }, 
          { "xor", "(Ljava/math/BigInteger;)Ljava/math/BigInteger;", false }
       }, { 
       });
}

BigInteger::BigInteger(jobject instance)
 : instance(instance), lang::Number(instance) {
    InitBigInteger();
}

BigInteger::~BigInteger() {}

BigInteger::BigInteger(signed char* val, int valc)
 : instance(tc::jEnv->NewObject(InitBigInteger(), tc::jEnv->GetMethodID(InitBigInteger(), "<init>", "([B)V"), ToJByteArray(val, valc))), lang::Number(instance)
{}

BigInteger::BigInteger(int signum, signed char* magnitude, int magnitudec)
 : instance(tc::jEnv->NewObject(InitBigInteger(), tc::jEnv->GetMethodID(InitBigInteger(), "<init>", "(I[B)V"), (jint)signum, ToJByteArray(magnitude, magnitudec))), lang::Number(instance)
{}

BigInteger::BigInteger(int bitLength, int certainty, util::Random rnd)
 : instance(tc::jEnv->NewObject(InitBigInteger(), tc::jEnv->GetMethodID(InitBigInteger(), "<init>", "(IILjava/util/Random;)V"), (jint)bitLength, (jint)certainty, rnd.instance)), lang::Number(instance)
{}

BigInteger::BigInteger(int numBits, util::Random rnd)
 : instance(tc::jEnv->NewObject(InitBigInteger(), tc::jEnv->GetMethodID(InitBigInteger(), "<init>", "(ILjava/util/Random;)V"), (jint)numBits, rnd.instance)), lang::Number(instance)
{}

BigInteger::BigInteger(const char* val)
 : instance(tc::jEnv->NewObject(InitBigInteger(), tc::jEnv->GetMethodID(InitBigInteger(), "<init>", "(Ljava/lang/String;)V"), ToJString(val))), lang::Number(instance)
{}

BigInteger::BigInteger(const char* cal, int radix)
 : instance(tc::jEnv->NewObject(InitBigInteger(), tc::jEnv->GetMethodID(InitBigInteger(), "<init>", "(Ljava/lang/String;I)V"), ToJString(cal), (jint)radix)), lang::Number(instance)
{}

BigInteger BigInteger::Abs() {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 0)));
}

BigInteger BigInteger::Add(BigInteger val) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 1), val.instance));
}

BigInteger BigInteger::And(BigInteger val) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 2), val.instance));
}

BigInteger BigInteger::AndNot(BigInteger val) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 3), val.instance));
}

int BigInteger::BitCount() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBigInteger, 4));
}

int BigInteger::BitLength() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBigInteger, 5));
}

BigInteger BigInteger::ClearBit(int n) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 6), (jint)n));
}

int BigInteger::CompareTo(BigInteger val) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBigInteger, 7), val.instance);
}

BigInteger BigInteger::Divide(BigInteger val) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 8), val.instance));
}

BigInteger* BigInteger::DivideAndRemainder(int* size, BigInteger val) {
   return FromJObjectArray<BigInteger>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 9), val.instance));
}

double BigInteger::DoubleValue() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsBigInteger, 10));
}

bool BigInteger::Equals(lang::Object x) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsBigInteger, 11), x.instance);
}

BigInteger BigInteger::FlipBit(int n) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 12), (jint)n));
}

float BigInteger::FloatValue() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsBigInteger, 13));
}

BigInteger BigInteger::Gcd(BigInteger val) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 14), val.instance));
}

int BigInteger::GetLowestSetBit() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBigInteger, 15));
}

int BigInteger::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBigInteger, 16));
}

int BigInteger::IntValue() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBigInteger, 17));
}

bool BigInteger::IsProbablePrime(int certainty) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsBigInteger, 18), (jint)certainty);
}

long BigInteger::LongValue() {
   return (long)tc::jEnv->CallLongMethod(METHOD(clsBigInteger, 19));
}

BigInteger BigInteger::Max(BigInteger val) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 20), val.instance));
}

BigInteger BigInteger::Min(BigInteger val) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 21), val.instance));
}

BigInteger BigInteger::Mod(BigInteger m) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 22), m.instance));
}

BigInteger BigInteger::ModInverse(BigInteger m) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 23), m.instance));
}

BigInteger BigInteger::ModPow(BigInteger exponent, BigInteger m) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 24), exponent.instance, m.instance));
}

BigInteger BigInteger::Multiply(BigInteger val) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 25), val.instance));
}

BigInteger BigInteger::Negate() {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 26)));
}

BigInteger BigInteger::NextProbablePrime() {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 27)));
}

BigInteger BigInteger::Not() {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 28)));
}

BigInteger BigInteger::Or(BigInteger val) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 29), val.instance));
}

BigInteger BigInteger::Pow(int exponent) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 30), (jint)exponent));
}

BigInteger BigInteger::ProbablePrime(int bitLength, util::Random rnd) {
   InitBigInteger();
   return (BigInteger)(tc::jEnv->CallStaticObjectMethod(clsBigInteger.clazz, clsBigInteger.methods[31], (jint)bitLength, rnd.instance));
}

BigInteger BigInteger::Remainder(BigInteger val) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 32), val.instance));
}

BigInteger BigInteger::SetBit(int n) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 33), (jint)n));
}

BigInteger BigInteger::ShiftLeft(int n) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 34), (jint)n));
}

BigInteger BigInteger::ShiftRight(int n) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 35), (jint)n));
}

int BigInteger::Signum() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsBigInteger, 36));
}

BigInteger BigInteger::Subtract(BigInteger val) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 37), val.instance));
}

bool BigInteger::TestBit(int n) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsBigInteger, 38), (jint)n);
}

signed char* BigInteger::ToByteArray(int* size) {
   return FromJByteArray(size, (jbyteArray)tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 39)));
}

char* BigInteger::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 40)));
}

char* BigInteger::ToString(int radix) {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 41), (jint)radix));
}

BigInteger BigInteger::ValueOf(long val) {
   InitBigInteger();
   return (BigInteger)(tc::jEnv->CallStaticObjectMethod(clsBigInteger.clazz, clsBigInteger.methods[42], (jlong)val));
}

BigInteger BigInteger::Xor(BigInteger val) {
   return (BigInteger)(tc::jEnv->CallObjectMethod(METHOD(clsBigInteger, 43), val.instance));
}


