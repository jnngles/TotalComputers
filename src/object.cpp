#include "object.h"

NativeObjectInstance::NativeObjectInstance(jobject instance)
    : instance(instance)
{}

NativeObjectInstance::~NativeObjectInstance() {}

NativeObjectInstance::NativeObjectInstance() {}