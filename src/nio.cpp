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
#include "nio.h"
#include "net.h"

using namespace nio;
using namespace nio::file;

// java/nio/file/Path
// Name: Path
// Base Class(es): [lang::Iterable]

NativeObject clsPath;
jclass InitPath() {
   return tcInitNativeObject(clsPath, "java/nio/file/Path", { 
          { "compareTo", "(Ljava/nio/file/Path;)I", false }, 
          { "endsWith", "(Ljava/nio/file/Path;)Z", false }, 
          { "endsWith", "(Ljava/lang/String;)Z", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "getFileName", "()Ljava/nio/file/Path;", false }, 
          { "getName", "(I)Ljava/nio/file/Path;", false }, 
          { "getNameCount", "()I", false }, 
          { "getParent", "()Ljava/nio/file/Path;", false }, 
          { "getRoot", "()Ljava/nio/file/Path;", false }, 
          { "hashCode", "()I", false }, 
          { "isAbsolute", "()Z", false }, 
          { "iterator", "()Ljava/util/Iterator;", false }, 
          { "normalize", "()Ljava/nio/file/Path;", false }, 
          { "relativize", "(Ljava/nio/file/Path;)Ljava/nio/file/Path;", false }, 
          { "resolve", "(Ljava/nio/file/Path;)Ljava/nio/file/Path;", false }, 
          { "resolve", "(Ljava/lang/String;)Ljava/nio/file/Path;", false }, 
          { "resolveSibling", "(Ljava/nio/file/Path;)Ljava/nio/file/Path;", false }, 
          { "resolveSibling", "(Ljava/lang/String;)Ljava/nio/file/Path;", false }, 
          { "startsWith", "(Ljava/nio/file/Path;)Z", false }, 
          { "startsWith", "(Ljava/lang/String;)Z", false }, 
          { "subpath", "(II)Ljava/nio/file/Path;", false }, 
          { "toAbsolutePath", "()Ljava/nio/file/Path;", false }, 
          { "toFile", "()Ljava/io/File;", false }, 
          { "toString", "()Ljava/lang/String;", false }, 
          { "toUri", "()Ljava/net/URI;", false }
       }, { 
       });
}

Path::Path(jobject instance)
 : instance(instance), lang::Iterable(instance) {
    InitPath();
}

Path::~Path() {}

int Path::CompareTo(Path other) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsPath, 0), other.instance);
}

bool Path::EndsWith(Path other) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPath, 1), other.instance);
}

bool Path::EndsWith(const char* other) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPath, 2), ToJString(other));
}

bool Path::Equals(lang::Object other) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPath, 3), other.instance);
}

Path Path::GetFileName() {
   return (Path)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 4)));
}

Path Path::GetName(int index) {
   return (Path)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 5), (jint)index));
}

int Path::GetNameCount() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsPath, 6));
}

Path Path::GetParent() {
   return (Path)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 7)));
}

Path Path::GetRoot() {
   return (Path)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 8)));
}

int Path::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsPath, 9));
}

bool Path::IsAbsolute() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPath, 10));
}

util::Iterator Path::Iterator0() {
   return (util::Iterator)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 11)));
}

Path Path::Normalize() {
   return (Path)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 12)));
}

Path Path::Relativize(Path other) {
   return (Path)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 13), other.instance));
}

Path Path::Resolve(Path other) {
   return (Path)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 14), other.instance));
}

Path Path::Resolve(const char* other) {
   return (Path)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 15), ToJString(other)));
}

Path Path::ResolveSibling(Path other) {
   return (Path)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 16), other.instance));
}

Path Path::ResolveSibling(const char* other) {
   return (Path)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 17), ToJString(other)));
}

bool Path::StartsWith(Path other) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPath, 18), other.instance);
}

bool Path::StartsWith(const char* other) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsPath, 19), ToJString(other));
}

Path Path::Subpath(int beginIndex, int endIndex) {
   return (Path)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 20), (jint)beginIndex, (jint)endIndex));
}

Path Path::ToAbsolutePath() {
   return (Path)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 21)));
}

io::File Path::ToFile() {
   return (io::File)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 22)));
}

char* Path::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsPath, 23)));
}

net::URI Path::ToUri() {
   return (net::URI)(tc::jEnv->CallObjectMethod(METHOD(clsPath, 24)));
}


