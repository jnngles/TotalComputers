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

#ifndef TC_TEXT_H
#define TX_TEXT_H

#include "util.h"

/// STRUCT

namespace text {
    class CharacterIterator;
    class AttributedCharacterIterator;
}

/// CLASSES

namespace text {

class CharacterIterator : public NativeObjectInstance {
public:
    CharacterIterator(jobject instance);
    ~CharacterIterator();

public:
    const static unsigned short DONE = 65535;

public:
    lang::Object Clone();
    signed char Current();
    signed char First();
    int GetBeginIndex();
    int GetEndIndex();
    int GetIndex();
    signed char Last();
    signed char Next();
    signed char Previous();
    signed char SetIndex(int position);

public:
    jobject instance;

};

class AttributedCharacterIterator : public CharacterIterator {
public:
    class Attribute;

public:
    AttributedCharacterIterator(jobject instance);
    ~AttributedCharacterIterator();

public:
    util::Set GetAllAttributeKeys();
    lang::Object GetAttribute(AttributedCharacterIterator::Attribute attribute);
    util::Map GetAttributes();
    int GetRunLimit();
    int GetRunLimit(AttributedCharacterIterator::Attribute attribute);
    int GetRunLimit(util::Set attributes);
    int GetRunStart();
    int GetRunStart(AttributedCharacterIterator::Attribute attribute);
    int GetRunStart(util::Set attributes);

public:
    jobject instance;

};

class AttributedCharacterIterator::Attribute : public lang::Object {
public:
    Attribute(jobject instance);
    ~Attribute();

public:
    bool Equals(lang::Object obj);
    int HashCode();
    char* ToString();

public:
    __FIELD__ static AttributedCharacterIterator::Attribute INPUT_METHOD_SEGMENT();
    __FIELD__ static AttributedCharacterIterator::Attribute LANGUAGE();
    __FIELD__ static AttributedCharacterIterator::Attribute READING();

public:
    jobject instance;

};

}

#endif