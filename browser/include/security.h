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