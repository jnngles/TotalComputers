#include "j_env.h"
#include "utils.h"
#include "io.h"
#include "net.h"

using namespace io;

// java/io/Flushable
// Name: Flushable
// Base Class(es): [NativeObjectInstance]

NativeObject clsFlushable;
jclass InitFlushable() {
   return tcInitNativeObject(clsFlushable, "java/io/Flushable", { 
          { "flush", "()V", false }
       }, { 
       });
}

Flushable::Flushable(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitFlushable();
}

Flushable::~Flushable() {}

void Flushable::Flush() {
   tc::jEnv->CallVoidMethod(METHOD(clsFlushable, 0));
}


// java/io/Closeable
// Name: Closeable
// Base Class(es): [lang::AutoCloseable]

NativeObject clsCloseable;
jclass InitCloseable() {
   return tcInitNativeObject(clsCloseable, "java/io/Closeable", { 
          { "close", "()V", false }
       }, { 
       });
}

Closeable::Closeable(jobject instance)
 : instance(instance), lang::AutoCloseable(instance) {
    InitCloseable();
}

Closeable::~Closeable() {}

void Closeable::Close() {
   tc::jEnv->CallVoidMethod(METHOD(clsCloseable, 0));
}


// java/io/InputStream
// Name: InputStream
// Base Class(es): [Closeable, lang::Object]

NativeObject clsInputStream;
jclass InitInputStream() {
   return tcInitNativeObject(clsInputStream, "java/io/InputStream", { 
          { "available", "()I", false }, 
          { "close", "()V", false }, 
          { "mark", "(I)V", false }, 
          { "markSupported", "()Z", false }, 
          { "read", "()I", false }, 
          { "read", "([B)I", false }, 
          { "read", "([BII)I", false }, 
          { "reset", "()V", false }, 
          { "skip", "(J)J", false }
       }, { 
       });
}

InputStream::InputStream(jobject instance)
 : instance(instance), Closeable(instance), lang::Object(instance) {
    InitInputStream();
}

InputStream::~InputStream() {}

int InputStream::Available() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsInputStream, 0));
}

void InputStream::Close() {
   tc::jEnv->CallVoidMethod(METHOD(clsInputStream, 1));
}

void InputStream::Mark(int readlimit) {
   tc::jEnv->CallVoidMethod(METHOD(clsInputStream, 2), (jint)readlimit);
}

bool InputStream::MarkSupported() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsInputStream, 3));
}

int InputStream::Read() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsInputStream, 4));
}

int InputStream::Read(signed char* b, int bc) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsInputStream, 5), ToJByteArray(b, bc));
}

int InputStream::Read(signed char* b, int bc, int off, int len) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsInputStream, 6), ToJByteArray(b, bc), (jint)off, (jint)len);
}

void InputStream::Reset() {
   tc::jEnv->CallVoidMethod(METHOD(clsInputStream, 7));
}

long long InputStream::Skip(long long n) {
   return (long long)(tc::jEnv->CallObjectMethod(METHOD(clsInputStream, 8), (jlong)n));
}


// java/io/OutputStream
// Name: OutputStream
// Base Class(es): [Flushable, Closeable, lang::Object]

NativeObject clsOutputStream;
jclass InitOutputStream() {
   return tcInitNativeObject(clsOutputStream, "java/io/OutputStream", { 
          { "close", "()V", false }, 
          { "flush", "()V", false }, 
          { "write", "([B)V", false }, 
          { "write", "([BII)V", false }, 
          { "write", "(I)V", false }
       }, { 
       });
}

OutputStream::OutputStream(jobject instance)
 : instance(instance), Flushable(instance), Closeable(instance), lang::Object(instance) {
    InitOutputStream();
}

OutputStream::~OutputStream() {}

void OutputStream::Close() {
   tc::jEnv->CallVoidMethod(METHOD(clsOutputStream, 0));
}

void OutputStream::Flush() {
   tc::jEnv->CallVoidMethod(METHOD(clsOutputStream, 1));
}

void OutputStream::Write(signed char* b, int bc) {
   tc::jEnv->CallVoidMethod(METHOD(clsOutputStream, 2), ToJByteArray(b, bc));
}

void OutputStream::Write(signed char* b, int bc, int off, int len) {
   tc::jEnv->CallVoidMethod(METHOD(clsOutputStream, 3), ToJByteArray(b, bc), (jint)off, (jint)len);
}

void OutputStream::Write(int b) {
   tc::jEnv->CallVoidMethod(METHOD(clsOutputStream, 4), (jint)b);
}


// java/io/FilenameFilter
// Name: FilenameFilter
// Base Class(es): [NativeObjectInstance]

NativeObject clsFilenameFilter;
jclass InitFilenameFilter() {
   return tcInitNativeObject(clsFilenameFilter, "java/io/FilenameFilter", { 
          { "accept", "(Ljava/io/File;Ljava/lang/String;)Z", false }
       }, { 
       });
}

FilenameFilter::FilenameFilter(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitFilenameFilter();
}

FilenameFilter::~FilenameFilter() {}

bool FilenameFilter::Accept(File dir, const char* name) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFilenameFilter, 0), dir.instance, ToJString(name));
}


// java/io/FileFilter
// Name: FileFilter
// Base Class(es): [NativeObjectInstance]

NativeObject clsFileFilter;
jclass InitFileFilter() {
   return tcInitNativeObject(clsFileFilter, "java/io/FileFilter", { 
          { "accept", "(Ljava/io/File;)Z", false }
       }, { 
       });
}

FileFilter::FileFilter(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitFileFilter();
}

FileFilter::~FileFilter() {}

bool FileFilter::Accept(File pathname) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFileFilter, 0), pathname.instance);
}


// java/io/File
// Name: File
// Base Class(es): [lang::Object]

NativeObject clsFile;
jclass InitFile() {
   return tcInitNativeObject(clsFile, "java/io/File", { 
          { "canExecute", "()Z", false }, 
          { "canRead", "()Z", false }, 
          { "canWrite", "()Z", false }, 
          { "compareTo", "(Ljava/io/File;)I", false }, 
          { "createNewFile", "()Z", false }, 
          { "createTempFile", "(Ljava/lang/String;Ljava/lang/String;)Ljava/io/File;", true }, 
          { "createTempFile", "(Ljava/lang/String;Ljava/lang/String;Ljava/io/File;)Ljava/io/File;", true }, 
          { "delete", "()Z", false }, 
          { "deleteOnExit", "()V", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "exists", "()Z", false }, 
          { "getAbsoluteFile", "()Ljava/io/File;", false }, 
          { "getAbsolutePath", "()Ljava/lang/String;", false }, 
          { "getCanonicalFile", "()Ljava/io/File;", false }, 
          { "getCanonicalPath", "()Ljava/lang/String;", false }, 
          { "getFreeSpace", "()J", false }, 
          { "getName", "()Ljava/lang/String;", false }, 
          { "getParent", "()Ljava/lang/String;", false }, 
          { "getParentFile", "()Ljava/io/File;", false }, 
          { "getPath", "()Ljava/lang/String;", false }, 
          { "getTotalSpace", "()J", false }, 
          { "getUsableSpace", "()J", false }, 
          { "hashCode", "()I", false }, 
          { "isAbsolute", "()Z", false }, 
          { "isDirectory", "()Z", false }, 
          { "isFile", "()Z", false }, 
          { "isHidden", "()Z", false }, 
          { "lastModified", "()J", false }, 
          { "length", "()J", false }, 
          { "list", "()[Ljava/lang/String;", false }, 
          { "list", "(Ljava/io/FilenameFilter;)[Ljava/lang/String;", false }, 
          { "listFiles", "()[Ljava/io/File;", false }, 
          { "listFiles", "(Ljava/io/FileFilter;)[Ljava/io/File;", false }, 
          { "listFiles", "(Ljava/io/FilenameFilter;)[Ljava/io/File;", false }, 
          { "listRoots", "()[Ljava/io/File;", true }, 
          { "mkdir", "()Z", false }, 
          { "mkdirs", "()Z", false }, 
          { "renameTo", "(Ljava/io/File;)Z", false }, 
          { "setExecutable", "(Z)Z", false }, 
          { "setExecutable", "(ZZ)Z", false }, 
          { "setLastModified", "(J)Z", false }, 
          { "setReadable", "(Z)Z", false }, 
          { "setReadable", "(ZZ)Z", false }, 
          { "setReadOnly", "()Z", false }, 
          { "setWritable", "(Z)Z", false }, 
          { "setWritable", "(ZZ)Z", false }, 
          { "toPath", "()Ljava/nio/file/Path;", false }, 
          { "toString", "()Ljava/lang/String;", false }, 
          { "toURI", "()Ljava/net/URI;", false }
       }, { 
       });
}

File::File(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitFile();
}

File::~File() {}

File::File(File parent, const char* child)
 : instance(tc::jEnv->NewObject(InitFile(), tc::jEnv->GetMethodID(InitFile(), "<init>", "(Ljava/io/File;Ljava/lang/String;)V"), parent.instance, ToJString(child))), lang::Object(instance)
{}

File::File(const char* pathname)
 : instance(tc::jEnv->NewObject(InitFile(), tc::jEnv->GetMethodID(InitFile(), "<init>", "(Ljava/lang/String;)V"), ToJString(pathname))), lang::Object(instance)
{}

File::File(const char* parent, const char* child)
 : instance(tc::jEnv->NewObject(InitFile(), tc::jEnv->GetMethodID(InitFile(), "<init>", "(Ljava/lang/String;Ljava/lang/String;)V"), ToJString(parent), ToJString(child))), lang::Object(instance)
{}

File::File(net::URI uri)
 : instance(tc::jEnv->NewObject(InitFile(), tc::jEnv->GetMethodID(InitFile(), "<init>", "(Ljava/net/URI;)V"), uri.instance)), lang::Object(instance)
{}

bool File::CanExecute() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 0));
}

bool File::CanRead() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 1));
}

bool File::CanWrite() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 2));
}

int File::CompareTo(File pathname) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFile, 3), pathname.instance);
}

bool File::CreateNewFile() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 4));
}

File File::CreateTempFile(const char* prefix, const char* suffix) {
   InitFile();
   return (File)(tc::jEnv->CallStaticObjectMethod(clsFile.clazz, clsFile.methods[5], ToJString(prefix), ToJString(suffix)));
}

File File::CreateTempFile(const char* prefix, const char* suffix, File directory) {
   InitFile();
   return (File)(tc::jEnv->CallStaticObjectMethod(clsFile.clazz, clsFile.methods[6], ToJString(prefix), ToJString(suffix), directory.instance));
}

bool File::Delete() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 7));
}

void File::DeleteOnExit() {
   tc::jEnv->CallVoidMethod(METHOD(clsFile, 8));
}

bool File::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 9), obj.instance);
}

bool File::Exists() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 10));
}

File File::GetAbsoluteFile() {
   return (File)(tc::jEnv->CallObjectMethod(METHOD(clsFile, 11)));
}

char* File::GetAbsolutePath() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsFile, 12)));
}

File File::GetCanonicalFile() {
   return (File)(tc::jEnv->CallObjectMethod(METHOD(clsFile, 13)));
}

char* File::GetCanonicalPath() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsFile, 14)));
}

long long File::GetFreeSpace() {
   return (long long)(tc::jEnv->CallObjectMethod(METHOD(clsFile, 15)));
}

char* File::GetName() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsFile, 16)));
}

char* File::GetParent() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsFile, 17)));
}

File File::GetParentFile() {
   return (File)(tc::jEnv->CallObjectMethod(METHOD(clsFile, 18)));
}

char* File::GetPath() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsFile, 19)));
}

long long File::GetTotalSpace() {
   return (long long)(tc::jEnv->CallObjectMethod(METHOD(clsFile, 20)));
}

long long File::GetUsableSpace() {
   return (long long)(tc::jEnv->CallObjectMethod(METHOD(clsFile, 21)));
}

int File::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsFile, 22));
}

bool File::IsAbsolute() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 23));
}

bool File::IsDirectory() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 24));
}

bool File::IsFile() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 25));
}

bool File::IsHidden() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 26));
}

long long File::LastModified() {
   return (long long)(tc::jEnv->CallObjectMethod(METHOD(clsFile, 27)));
}

long long File::Length() {
   return (long long)(tc::jEnv->CallObjectMethod(METHOD(clsFile, 28)));
}

char** File::List(int* size) {
   return FromJStringArray(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsFile, 29)));
}

char** File::List(int* size, FilenameFilter filter) {
   return FromJStringArray(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsFile, 30), filter.instance));
}

File* File::ListFiles(int* size) {
   return FromJObjectArray<File>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsFile, 31)));
}

File* File::ListFiles(int* size, FileFilter filter) {
   return FromJObjectArray<File>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsFile, 32), filter.instance));
}

File* File::ListFiles(int* size, FilenameFilter filter) {
   return FromJObjectArray<File>(size, (jobjectArray)tc::jEnv->CallObjectMethod(METHOD(clsFile, 33), filter.instance));
}

File* File::ListRoots(int* size) {
   InitFile();
   return FromJObjectArray<File>(size, (jobjectArray)tc::jEnv->CallStaticObjectMethod(clsFile.clazz, clsFile.methods[34]));
}

bool File::Mkdir() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 35));
}

bool File::Mkdirs() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 36));
}

bool File::RenameTo(File dest) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 37), dest.instance);
}

bool File::SetExecutable(bool executable) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 38), (jboolean)executable);
}

bool File::SetExecutable(bool executable, bool ownerOnly) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 39), (jboolean)executable, (jboolean)ownerOnly);
}

bool File::SetLastModified(long long time) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 40), (jlong)time);
}

bool File::SetReadable(bool readable) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 41), (jboolean)readable);
}

bool File::SetReadable(bool readable, bool ownerOnly) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 42), (jboolean)readable, (jboolean)ownerOnly);
}

bool File::SetReadOnly() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 43));
}

bool File::SetWritable(bool writable) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 44), (jboolean)writable);
}

bool File::SetWritable(bool writable, bool ownerOnly) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsFile, 45), (jboolean)writable, (jboolean)ownerOnly);
}

nio::file::Path File::ToPath() {
   return (nio::file::Path)(tc::jEnv->CallObjectMethod(METHOD(clsFile, 46)));
}

char* File::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsFile, 47)));
}

net::URI File::ToURI() {
   return (net::URI)(tc::jEnv->CallObjectMethod(METHOD(clsFile, 48)));
}


