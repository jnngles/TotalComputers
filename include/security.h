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

#ifndef TC_SECURITY_H
#define TC_SECURITY_H

#include "util.h"

/// STRUCT

namespace security {
    class PermissionCollection;
    class Guard;
    class Permission;
}

/// CLASSES

namespace security {

class PermissionCollection : public lang::Object {
public:
    PermissionCollection(jobject instance);
    ~PermissionCollection();

public:
    void Add(Permission permission);
    util::Enumeration Elements();
    bool Implies(Permission permission);
    bool IsReadOnly();
    void SetReadOnly();
    char* ToString();

public:
    jobject instance;

};

class Guard : public NativeObjectInstance {
public:
    Guard(jobject instance);
    ~Guard();

public:
    void CheckGuard(lang::Object object);

public:
    jobject instance;

};

class Permission : public lang::Object, public Guard {
public:
    Permission(jobject instance);
    ~Permission();

public:
    void CheckGuard(lang::Object object);
    bool Equals(lang::Object obj);
    char* GetActions();
    char* GetName();
    int HashCode();
    bool Implies(Permission permission);
    PermissionCollection NewPermissionCollection();
    char* ToString();

public:
    jobject instance;

};

}

#endif