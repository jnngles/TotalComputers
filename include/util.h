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

#ifndef TC_UTIL_H
#define TC_UTIL_H

#include "object.h"
#include "lang.h"

/// STRUCT

namespace util {
    class Enumeration;
    class Iterator;
    class ListIterator;
    class Collection;
    class Set;
    class Map;
    class AbstractCollection;
    class List;
    class AbstractList;
    class Vector;
    class Dictionary;
    class Random;
}

/// CLASSES

namespace util {

class Enumeration : public NativeObjectInstance {
public:
    Enumeration(jobject instance);
    ~Enumeration();

public:
    bool HasMoreElements();
    lang::Object NextElement();

public:
    jobject instance;

};

class Iterator : public NativeObjectInstance {
public:
    Iterator(jobject instance);
    ~Iterator();

public:
    bool HasNext();
    lang::Object Next();
    void Remove();

public:
    jobject instance;

};

class ListIterator : public Iterator {
public:
    ListIterator(jobject instance);
    ~ListIterator();

public:
    void Add(lang::Object e);
    bool HasNext();
    bool HasPrevious();
    lang::Object Next();
    int NextIndex();
    lang::Object Previous();
    int PreviousIndex();
    void Remove();
    void Set(lang::Object e);

public:
    jobject instance;

};

class Collection : public lang::Iterable {
public:
    Collection(jobject instance);
    ~Collection();
    
public:
    bool Add(lang::Object e);
    bool AddAll(Collection c);
    void Clear();
    bool Contains(lang::Object o);
    bool ContainsAll(Collection c);
    bool Equals(lang::Object o);
    int HashCode();
    bool IsEmpty();
    Iterator Iterator0();
    bool Remove(lang::Object o);
    bool RemoveAll(Collection c);
    bool RetainAll(Collection c);
    int Size();
    lang::Object* ToArray(int* size);
    lang::Object* ToArray(int* size, lang::Object* a, int ac);

public:
    jobject instance;

};

class Set : public Collection {
public:
    Set(jobject instance);
    ~Set();

public:
    bool Add(lang::Object e);
    bool AddAll(Collection c);
    void Clear();
    bool Contains(lang::Object o);
    bool ContainsAll(Collection c);
    bool Equals(lang::Object o);
    int HashCode();
    bool IsEmpty();
    Iterator Iterator0();
    bool Remove(lang::Object o);
    bool RemoveAll(Collection c);
    bool RetainAll(Collection c);
    int Size();
    lang::Object* ToArray(int* size);
    lang::Object* ToArray(int* size, lang::Object* a, int ac);

public:
    jobject instance;

};

class Map : public NativeObjectInstance {
public:
    class Entry;

public:
    Map(jobject instance);
    ~Map();

public:
    void Clear();
    bool ContainsKey(lang::Object key);
    bool ContainsValue(lang::Object value);
    Set EntrySet();
    bool Equals(lang::Object o);
    lang::Object Get(lang::Object key);
    int HashCode();
    bool IsEmpty();
    Set KeySet();
    lang::Object Put(lang::Object key, lang::Object value);
    void PutAll(Map m);
    lang::Object Remove(lang::Object key);
    int Size();
    Collection Values();

public:
    jobject instance;

};

class Map::Entry : public NativeObjectInstance {
public:
    Entry(jobject instance);
    ~Entry();

public:
    bool Equals(lang::Object o);
    lang::Object GetKey();
    lang::Object GetValue();
    int HashCode();
    lang::Object SetValue(lang::Object value);

public:
    jobject instance;

};

class AbstractCollection : public lang::Object, public Collection {
public:
    AbstractCollection(jobject instance);
    ~AbstractCollection();

public:
    bool Add(lang::Object e);
    bool AddAll(Collection c);
    void Clear();
    bool Contains(lang::Object o);
    bool ContainsAll(Collection c);
    bool IsEmpty();
    Iterator Iterator0();
    bool Remove(lang::Object o);
    bool RemoveAll(Collection c);
    bool RetainAll(Collection c);
    int Size();
    lang::Object* ToArray(int* size);
    lang::Object* ToArray(int* size, lang::Object* a, int ac);
    char* ToString();

public:
    jobject instance;

};

class List : public Collection {
public:
    List(jobject instance);
    ~List();

public:
    bool Add(lang::Object e);
    void Add(int index, lang::Object element);
    bool AddAll(Collection c);
    bool AddAll(int index, Collection c);
    void Clear();
    bool Contains(lang::Object o);
    bool ContainsAll(Collection c);
    bool Equals(lang::Object o);
    lang::Object Get(int index);
    int HashCode();
    int IndexOf(lang::Object o);
    bool IsEmpty();
    Iterator Iterator0();
    int LastIndexOf(lang::Object o);
    ListIterator ListIterator0();
    ListIterator ListIterator0(int index);
    lang::Object Remove(int index);
    bool Remove(lang::Object o);
    bool RemoveAll(Collection c);
    bool RetainAll(Collection c);
    lang::Object Set(int index, lang::Object element);
    int Size();
    List SubList(int fromIndex, int toIndex);
    lang::Object* ToArray(int* size);
    lang::Object* ToArray(int* size, lang::Object* a, int ac);

public:
    jobject instance;

};

class AbstractList : public AbstractCollection, public List {
public:
    AbstractList(jobject instance);
    ~AbstractList();

public:
    bool Add(lang::Object e);
    void Add(int index, lang::Object element);
    bool AddAll(int index, Collection c);
    void Clear();
    bool Equals(lang::Object o);
    lang::Object Get(int index);
    int HashCode();
    int IndexOf(lang::Object o);
    Iterator Iterator0();
    int LastIndexOf(lang::Object o);
    ListIterator ListIterator0();
    ListIterator ListIterator0(int index);
    lang::Object Remove(int index);
    void RemoveRange(int fromIndex, int toIndex);
    lang::Object Set(int index, lang::Object element);
    List SubList(int fromIndex, int toIndex);

public:
    jobject instance;

};

class Vector : public AbstractList {
public:
    Vector(jobject instance);
    ~Vector();

public:
    Vector();
    Vector(Collection c);
    Vector(int initialCapacity);
    Vector(int initialCapacity, int capacityIncrement);

public:
    bool Add(lang::Object e);
    void Add(int index, lang::Object e);
    bool AddAll(Collection c);
    bool AddAll(int index, Collection c);
    void AddElement(lang::Object obj);
    int Capacity();
    void Clear();
    lang::Object Clone();
    bool Contains(lang::Object o);
    bool ContainsAll(Collection c);
    void CopyInto(lang::Object* anArray, int anArrayc);
    lang::Object ElementAt(int index);
    Enumeration Elements();
    void EnsureCapacity(int minCapacity);
    bool Equals(lang::Object o);
    lang::Object FirstElement();
    int HashCode();
    int IndexOf(lang::Object o);
    int IndexOf(lang::Object o, int index);
    void InsertElementAt(lang::Object obj, int index);
    bool IsEmpty();
    Iterator Iterator0();
    lang::Object LastElement();
    int LastIndexOf(lang::Object o);
    int LastIndexOf(lang::Object o, int index);
    ListIterator ListIterator0();
    ListIterator ListIterator0(int index);
    lang::Object Remove(int index);
    bool Remove(lang::Object o);
    bool RemoveAll(Collection c);
    void RemoveAllElements();
    bool RemoveElement(lang::Object obj);
    void RemoveElementAt(int index);
    void RemoveRange(int fromIndex, int toIndex);
    bool RetainAll(Collection c);
    lang::Object Set(int index, lang::Object element);
    void SetElementAt(lang::Object obj, int index);
    void SetSize(int newSize);
    int Size();
    List SubList(int fromIndex, int toIndex);
    lang::Object* ToArray(int* size);
    lang::Object* ToArray(int* size, lang::Object* a, int ac);
    char* ToString();
    void TrimToSize();

public:
    jobject instance;

};

class Dictionary : public lang::Object {
public:
    Dictionary(jobject instance);
    ~Dictionary();

public:
    Enumeration Elements();
    lang::Object Get(lang::Object key);
    bool IsEmpty();
    Enumeration Keys();
    lang::Object Put(lang::Object key, lang::Object value);
    lang::Object Remove(lang::Object key);
    int Size();

public:
    jobject instance;

};

class Hashtable : public Dictionary, public Map {
public:
    Hashtable(jobject instance);
    ~Hashtable();

public:
    Hashtable();
    Hashtable(int initialCapacity);
    Hashtable(int initialCapacity, float loadFactor);
    Hashtable(Map t);

public:
    void Clear();
    lang::Object Clone();
    bool Contains(lang::Object value);
    bool ContainsKey(lang::Object key);
    bool ContainsValue(lang::Object value);
    Enumeration Elements();
    Set EntrySet();
    bool Equals(lang::Object o);
    lang::Object Get(lang::Object key);
    int HashCode();
    bool IsEmpty();
    Enumeration Keys();
    Set KeySet();
    lang::Object Put(lang::Object key, lang::Object value);
    void PutAll(Map t);
    void Rehash();
    lang::Object Remove(lang::Object key);
    int Size();
    char* ToString();
    Collection Values();

public:
    jobject instance;

};

class Random : public lang::Object {
public:
    Random(jobject instance);
    ~Random();

public:
    Random();
    Random(long seed);

public:
    bool NextBoolean();
    void NextBytes(signed char* chars, int signed charsc);
    double NextDouble();
    float NextFloat();
    double NextGaussian();
    int NextInt();
    int NextInt(int n);
    long NextLong();
    void SetSeed(long seed);

public:
    jobject instance;

};

}

#endif