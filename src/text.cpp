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
#include "utils.h"
#include "text.h"

using namespace text;

// java/text/CharacterIterator
// Name: CharacterIterator
// Base Class(es): [NativeObjectInstance]

NativeObject clsCharacterIterator;
jclass InitCharacterIterator() {
   return tcInitNativeObject(clsCharacterIterator, "java/text/CharacterIterator", { 
          { "clone", "()Ljava/lang/Object;", false }, 
          { "current", "()C", false }, 
          { "first", "()C", false }, 
          { "getBeginIndex", "()I", false }, 
          { "getEndIndex", "()I", false }, 
          { "getIndex", "()I", false }, 
          { "last", "()C", false }, 
          { "next", "()C", false }, 
          { "previous", "()C", false }, 
          { "setIndex", "(I)C", false }
       }, { 
       });
}

CharacterIterator::CharacterIterator(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitCharacterIterator();
}

CharacterIterator::~CharacterIterator() {}

lang::Object CharacterIterator::Clone() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsCharacterIterator, 0)));
}

signed char CharacterIterator::Current() {
   return (signed char)tc::jEnv->CallByteMethod(METHOD(clsCharacterIterator, 1));
}

signed char CharacterIterator::First() {
   return (signed char)tc::jEnv->CallByteMethod(METHOD(clsCharacterIterator, 2));
}

int CharacterIterator::GetBeginIndex() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsCharacterIterator, 3));
}

int CharacterIterator::GetEndIndex() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsCharacterIterator, 4));
}

int CharacterIterator::GetIndex() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsCharacterIterator, 5));
}

signed char CharacterIterator::Last() {
   return (signed char)tc::jEnv->CallByteMethod(METHOD(clsCharacterIterator, 6));
}

signed char CharacterIterator::Next() {
   return (signed char)tc::jEnv->CallByteMethod(METHOD(clsCharacterIterator, 7));
}

signed char CharacterIterator::Previous() {
   return (signed char)tc::jEnv->CallByteMethod(METHOD(clsCharacterIterator, 8));
}

signed char CharacterIterator::SetIndex(int position) {
   return (signed char)tc::jEnv->CallByteMethod(METHOD(clsCharacterIterator, 9), (jint)position);
}


// java/text/AttributedCharacterIterator
// Name: AttributedCharacterIterator
// Base Class(es): [CharacterIterator]

NativeObject clsAttributedCharacterIterator;
jclass InitAttributedCharacterIterator() {
   return tcInitNativeObject(clsAttributedCharacterIterator, "java/text/AttributedCharacterIterator", { 
          { "getAllAttributeKeys", "()Ljava/util/Set;", false }, 
          { "getAttribute", "(Ljava/text/AttributedCharacterIterator$Attribute;)Ljava/lang/Object;", false }, 
          { "getAttributes", "()Ljava/util/Map;", false }, 
          { "getRunLimit", "()I", false }, 
          { "getRunLimit", "(Ljava/text/AttributedCharacterIterator$Attribute;)I", false }, 
          { "getRunLimit", "(Ljava/util/Set;)I", false }, 
          { "getRunStart", "()I", false }, 
          { "getRunStart", "(Ljava/text/AttributedCharacterIterator$Attribute;)I", false }, 
          { "getRunStart", "(Ljava/util/Set;)I", false }
       }, { 
       });
}

AttributedCharacterIterator::AttributedCharacterIterator(jobject instance)
 : instance(instance), CharacterIterator(instance) {
    InitAttributedCharacterIterator();
}

AttributedCharacterIterator::~AttributedCharacterIterator() {}

util::Set AttributedCharacterIterator::GetAllAttributeKeys() {
   return (util::Set)(tc::jEnv->CallObjectMethod(METHOD(clsAttributedCharacterIterator, 0)));
}

lang::Object AttributedCharacterIterator::GetAttribute(AttributedCharacterIterator::Attribute attribute) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsAttributedCharacterIterator, 1), attribute.instance));
}

util::Map AttributedCharacterIterator::GetAttributes() {
   return (util::Map)(tc::jEnv->CallObjectMethod(METHOD(clsAttributedCharacterIterator, 2)));
}

int AttributedCharacterIterator::GetRunLimit() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsAttributedCharacterIterator, 3));
}

int AttributedCharacterIterator::GetRunLimit(AttributedCharacterIterator::Attribute attribute) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsAttributedCharacterIterator, 4), attribute.instance);
}

int AttributedCharacterIterator::GetRunLimit(util::Set attributes) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsAttributedCharacterIterator, 5), attributes.instance);
}

int AttributedCharacterIterator::GetRunStart() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsAttributedCharacterIterator, 6));
}

int AttributedCharacterIterator::GetRunStart(AttributedCharacterIterator::Attribute attribute) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsAttributedCharacterIterator, 7), attribute.instance);
}

int AttributedCharacterIterator::GetRunStart(util::Set attributes) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsAttributedCharacterIterator, 8), attributes.instance);
}


// java/text/AttributedCharacterIterator/Attribute
// Name: AttributedCharacterIterator::Attribute
// Base Class(es): [lang::Object]

NativeObject clsAttributedCharacterIterator_Attribute;
jclass InitAttributedCharacterIterator_Attribute() {
   return tcInitNativeObject(clsAttributedCharacterIterator_Attribute, "java/text/AttributedCharacterIterator/Attribute", { 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "hashCode", "()I", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
          { "INPUT_METHOD_SEGMENT", "Ljava/text/AttributedCharacterIterator$Attribute;", true }, 
          { "LANGUAGE", "Ljava/text/AttributedCharacterIterator$Attribute;", true }, 
          { "READING", "Ljava/text/AttributedCharacterIterator$Attribute;", true }
       });
}

AttributedCharacterIterator::Attribute::Attribute(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitAttributedCharacterIterator_Attribute();
}

AttributedCharacterIterator::Attribute::~Attribute() {}

bool AttributedCharacterIterator::Attribute::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsAttributedCharacterIterator_Attribute, 0), obj.instance);
}

int AttributedCharacterIterator::Attribute::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsAttributedCharacterIterator_Attribute, 1));
}

char* AttributedCharacterIterator::Attribute::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsAttributedCharacterIterator_Attribute, 2)));
}

AttributedCharacterIterator::Attribute AttributedCharacterIterator::Attribute::INPUT_METHOD_SEGMENT() {
   InitAttributedCharacterIterator_Attribute();
   return (AttributedCharacterIterator::Attribute)(tc::jEnv->GetStaticObjectField(clsAttributedCharacterIterator_Attribute.clazz, clsAttributedCharacterIterator_Attribute.fields[0]));
}

AttributedCharacterIterator::Attribute AttributedCharacterIterator::Attribute::LANGUAGE() {
   InitAttributedCharacterIterator_Attribute();
   return (AttributedCharacterIterator::Attribute)(tc::jEnv->GetStaticObjectField(clsAttributedCharacterIterator_Attribute.clazz, clsAttributedCharacterIterator_Attribute.fields[1]));
}

AttributedCharacterIterator::Attribute AttributedCharacterIterator::Attribute::READING() {
   InitAttributedCharacterIterator_Attribute();
   return (AttributedCharacterIterator::Attribute)(tc::jEnv->GetStaticObjectField(clsAttributedCharacterIterator_Attribute.clazz, clsAttributedCharacterIterator_Attribute.fields[2]));
}


