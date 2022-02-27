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