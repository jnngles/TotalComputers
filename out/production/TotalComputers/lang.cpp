#include "j_env.h"
#include "utils.h"
#include "lang.h"
#include "util.h"

using namespace lang;

// java/lang/Object
// Name: Object
// Base Class(es): [NativeObjectInstance]

NativeObject clsObject;
jclass InitObject() {
   return tcInitNativeObject(clsObject, "java/lang/Object", { 
          { "clone", "()Ljava/lang/Object;", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "finalize", "()V", false }, 
          { "hashCode", "()I", false }, 
          { "notify", "()V", false }, 
          { "notifyAll", "()V", false }, 
          { "toString", "()Ljava/lang/String;", false }, 
          { "wait", "()V", false }, 
          { "wait", "(J)V", false }, 
          { "wait", "(JI)V", false }
       }, { 
       });
}

Object::Object(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitObject();
}

Object::Object()
 : instance(tc::jEnv->NewObject(InitObject(), tc::jEnv->GetMethodID(InitObject(), "<init>", "()V"))), NativeObjectInstance(instance)
{}

Object::~Object() {}

Object Object::Clone() {
   return (Object)(tc::jEnv->CallObjectMethod(METHOD(clsObject, 0)));
}

bool Object::Equals(Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsObject, 1), obj.instance);
}

void Object::Finalize() {
   tc::jEnv->CallVoidMethod(METHOD(clsObject, 2));
}

int Object::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsObject, 3));
}

void Object::Notify() {
   tc::jEnv->CallVoidMethod(METHOD(clsObject, 4));
}

void Object::NotifyAll() {
   tc::jEnv->CallVoidMethod(METHOD(clsObject, 5));
}

char* Object::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsObject, 6)));
}

void Object::Wait() {
   tc::jEnv->CallVoidMethod(METHOD(clsObject, 7));
}

void Object::Wait(long timeout) {
   tc::jEnv->CallVoidMethod(METHOD(clsObject, 8), (jlong)timeout);
}

void Object::Wait(long timeout, int nanos) {
   tc::jEnv->CallVoidMethod(METHOD(clsObject, 9), (jlong)timeout, (jint)nanos);
}


// java/lang/Enum
// Name: Enum
// Base Class(es): [Object]

NativeObject clsEnum;
jclass InitEnum() {
   return tcInitNativeObject(clsEnum, "java/lang/Enum", { 
          { "compareTo", "(Ljava/lang/Object;)I", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "hashCode", "()I", false }, 
          { "name", "()Ljava/lang/String;", false }, 
          { "ordinal", "()I", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

Enum::Enum(jobject instance)
 : instance(instance), Object(instance) {
    InitEnum();
}

Enum::~Enum() {}

int Enum::CompareTo(lang::Object o) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsEnum, 0), o.instance);
}

bool Enum::Equals(lang::Object other) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsEnum, 1), other.instance);
}

int Enum::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsEnum, 2));
}

char* Enum::Name() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsEnum, 3)));
}

int Enum::Ordinal() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsEnum, 4));
}

char* Enum::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsEnum, 5)));
}


// java/lang/Number
// Name: Number
// Base Class(es): [Object]

NativeObject clsNumber;
jclass InitNumber() {
   return tcInitNativeObject(clsNumber, "java/lang/Number", { 
          { "signed charValue", "()B", false }, 
          { "doubleValue", "()D", false }, 
          { "floatValue", "()F", false }, 
          { "intValue", "()I", false }, 
          { "longValue", "()J", false }, 
          { "shortValue", "()S", false }
       }, { 
       });
}

Number::Number(jobject instance)
 : instance(instance), Object(instance) {
    InitNumber();
}

Number::~Number() {}

signed char Number::ByteValue() {
   return (signed char)tc::jEnv->CallByteMethod(METHOD(clsNumber, 0));
}

double Number::DoubleValue() {
   return (double)tc::jEnv->CallDoubleMethod(METHOD(clsNumber, 1));
}

float Number::FloatValue() {
   return (float)tc::jEnv->CallFloatMethod(METHOD(clsNumber, 2));
}

int Number::IntValue() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsNumber, 3));
}

long Number::LongValue() {
   return (long)tc::jEnv->CallLongMethod(METHOD(clsNumber, 4));
}

short Number::ShortValue() {
   return (short)tc::jEnv->CallShortMethod(METHOD(clsNumber, 5));
}


// java/lang/Iterable
// Name: Iterable
// Base Class(es): [NativeObjectInstance]

NativeObject clsIterable;
jclass InitIterable() {
   return tcInitNativeObject(clsIterable, "java/lang/Iterable", { 
          { "iterator", "()Ljava/util/Iterator;", false }
       }, { 
       });
}

Iterable::Iterable(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitIterable();
}

Iterable::~Iterable() {}

util::Iterator Iterable::Iterator0() {
   return (util::Iterator)(tc::jEnv->CallObjectMethod(METHOD(clsIterable, 0)));
}


// java/lang/AutoCloseable
// Name: AutoCloseable
// Base Class(es): [NativeObjectInstance]

NativeObject clsAutoCloseable;
jclass InitAutoCloseable() {
   return tcInitNativeObject(clsAutoCloseable, "java/lang/AutoCloseable", { 
          { "close", "()V", false }
       }, { 
       });
}

AutoCloseable::AutoCloseable(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitAutoCloseable();
}

AutoCloseable::~AutoCloseable() {}

void AutoCloseable::Close() {
   tc::jEnv->CallVoidMethod(METHOD(clsAutoCloseable, 0));
}


