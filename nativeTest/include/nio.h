#ifndef TC_NIO_H
#define TC_NIO_H

#include "lang.h"

/// STRUCT

namespace nio {
    namespace file {
        class Path;
    }
}

/// CLASSES

namespace io { class File; }
namespace net { class URI; }

namespace nio {

namespace file {

class Path : public lang::Iterable {
public:
    Path(jobject instance);
    ~Path();

public:
    int CompareTo(Path other);
    bool EndsWith(Path other);
    bool EndsWith(const char* other);
    bool Equals(lang::Object other);
    Path GetFileName();
    Path GetName(int index);
    int GetNameCount();
    Path GetParent();
    Path GetRoot();
    int HashCode();
    bool IsAbsolute();
    util::Iterator Iterator0();
    Path Normalize();
    Path Relativize(Path other);
    Path Resolve(Path other);
    Path Resolve(const char* other);
    Path ResolveSibling(Path other);
    Path ResolveSibling(const char* other);
    bool StartsWith(Path other);
    bool StartsWith(const char* other);
    Path Subpath(int beginIndex, int endIndex);
    Path ToAbsolutePath();
    io::File ToFile();
    char* ToString();
    net::URI ToUri();

public:
    jobject instance;

};

}

}

#endif