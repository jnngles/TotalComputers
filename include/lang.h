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

#ifndef TC_LANG_H
#define TC_LANG_H

#include "object.h"

/// STRUCT

namespace lang {
    class Object;
    class Enum;
    class Number;
    class Iterable;
    class AutoCloseable;
}


/// CLASSES

namespace util { class Iterator; }

namespace lang {

class Object : public NativeObjectInstance {
public:
    Object(jobject instance);
    Object();
    ~Object();

public:
    Object Clone();
    bool Equals(Object obj);
    void Finalize();
    int HashCode();
    void Notify();
    void NotifyAll();
    char* ToString();
    void Wait();
    void Wait(long timeout);
    void Wait(long timeout, int nanos);

public:
    jobject instance;

};

class Enum : public Object {
public:
    Enum(jobject instance);
    ~Enum();

public:
    int CompareTo(lang::Object o);
    bool Equals(lang::Object other);
    int HashCode();
    char* Name();
    int Ordinal();
    char* ToString();

public:
    jobject instance;

};

class Number : public Object {
public:
    Number(jobject instance);
    ~Number();

public:
    signed char ByteValue();
    double DoubleValue();
    float FloatValue();
    int IntValue();
    long LongValue();
    short ShortValue();

public:
    jobject instance;

};

class Iterable : public NativeObjectInstance {
public:
    Iterable(jobject instance);
    ~Iterable();

public:
    util::Iterator Iterator0();

public:
    jobject instance;

};

class AutoCloseable : public NativeObjectInstance {
public:
    AutoCloseable(jobject instance);
    ~AutoCloseable();

public:
    void Close();

public:
    jobject instance;

};

}

#endif