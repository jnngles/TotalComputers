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
#include "security.h"

using namespace security;

// java/security/PermissionCollection
// Name: PermissionCollection
// Base Class(es): [lang::Object]

NativeObject clsPermissionCollection;
jclass InitPermissionCollection() {
   return tcInitNativeObject(clsPermissionCollection, "java/security/PermissionCollection", { 
          { "add", "(Ljava/security/Permission;)V", false }, 
          { "elements", "()Ljava/util/Enumeration;", false }, 
          { "implies", "(Ljava/security/Permission;)Z", false }, 
          { "isReadOnly", "()Z", false }, 
          { "setReadOnly", "()V", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

PermissionCollection::PermissionCollection(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitPermissionCollection();
}

PermissionCollection::~PermissionCollection() {}

void PermissionCollection::Add(Permission permission) {
   tc::jEnv->CallVoidMethod(METHOD(clsPermissionCollection, 0), permission.instance);
}

util::Enumeration PermissionCollection::Elements() {
   return (util::Enumeration)(tc::jEnv->CallObjectMethod(METHOD(clsPermissionCollection, 1)));
}

bool PermissionCollection::Implies(Permission permission) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPermissionCollection, 2), permission.instance);
}

bool PermissionCollection::IsReadOnly() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPermissionCollection, 3));
}

void PermissionCollection::SetReadOnly() {
   tc::jEnv->CallVoidMethod(METHOD(clsPermissionCollection, 4));
}

char* PermissionCollection::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsPermissionCollection, 5)));
}


// java/security/Guard
// Name: Guard
// Base Class(es): [NativeObjectInstance]

NativeObject clsGuard;
jclass InitGuard() {
   return tcInitNativeObject(clsGuard, "java/security/Guard", { 
          { "checkGuard", "(Ljava/lang/Object;)V", false }
       }, { 
       });
}

Guard::Guard(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitGuard();
}

Guard::~Guard() {}

void Guard::CheckGuard(lang::Object object) {
   tc::jEnv->CallVoidMethod(METHOD(clsGuard, 0), object.instance);
}


// java/security/Permission
// Name: Permission
// Base Class(es): [Guard, lang::Object]

NativeObject clsPermission;
jclass InitPermission() {
   return tcInitNativeObject(clsPermission, "java/security/Permission", { 
          { "checkGuard", "(Ljava/lang/Object;)V", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "getActions", "()Ljava/lang/String;", false }, 
          { "getName", "()Ljava/lang/String;", false }, 
          { "hashCode", "()I", false }, 
          { "implies", "(Ljava/security/Permission;)Z", false }, 
          { "newPermissionCollection", "()Ljava/security/PermissionCollection;", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

Permission::Permission(jobject instance)
 : instance(instance), Guard(instance), lang::Object(instance) {
    InitPermission();
}

Permission::~Permission() {}

void Permission::CheckGuard(lang::Object object) {
   tc::jEnv->CallVoidMethod(METHOD(clsPermission, 0), object.instance);
}

bool Permission::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPermission, 1), obj.instance);
}

char* Permission::GetActions() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsPermission, 2)));
}

char* Permission::GetName() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsPermission, 3)));
}

int Permission::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsPermission, 4));
}

bool Permission::Implies(Permission permission) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPermission, 5), permission.instance);
}

PermissionCollection Permission::NewPermissionCollection() {
   return (PermissionCollection)(tc::jEnv->CallObjectMethod(METHOD(clsPermission, 6)));
}

char* Permission::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsPermission, 7)));
}


