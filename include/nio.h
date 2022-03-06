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