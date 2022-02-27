#ifndef TC_IO_H
#define TC_IO_H

#include "lang.h"
#include "nio.h"

/// STRUCT

namespace io {
    class Flushable;
    class Closeable;
    class InputStream;
    class OutputStream;
    class FilenameFilter;
    class FileFilter;
    class File;
}

/// CLASSES

namespace net { class URI; }

namespace io {

class Flushable : public NativeObjectInstance {
public:
    Flushable(jobject instance);
    ~Flushable();

public:
    void Flush();

public:
    jobject instance;

};

class Closeable : public lang::AutoCloseable {
public:
    Closeable(jobject instance);
    ~Closeable();

public:
    void Close();

public:
    jobject instance;

};

class InputStream : public lang::Object, public Closeable {
public:
    InputStream(jobject instance);
    ~InputStream();

public:
    int Available();
    void Close();
    void Mark(int readlimit);
    bool MarkSupported();
    int Read();
    int Read(signed char* b, int bc);
    int Read(signed char* b, int bc, int off, int len);
    void Reset();
    long long Skip(long long n);

public:
    jobject instance;

};

class OutputStream : public lang::Object, public Closeable, public Flushable {
public:
    OutputStream(jobject instance);
    ~OutputStream();

public:
    void Close();
    void Flush();
    void Write(signed char* b, int bc);
    void Write(signed char* b, int bc, int off, int len);
    void Write(int b);

public:
    jobject instance;

};

class FilenameFilter : public NativeObjectInstance {
public:
    FilenameFilter(jobject instance);
    ~FilenameFilter();

public:
    bool Accept(File dir, const char* name);

public:
    jobject instance;

};

class FileFilter : public NativeObjectInstance {
public:
    FileFilter(jobject instance);
    ~FileFilter();

public:
    bool Accept(File pathname);

public:
    jobject instance;

};

class File : public lang::Object {
public:
    File(jobject instance);
    ~File();

public:
    File(File parent, const char* child);
    File(const char* pathname);
    File(const char* parent, const char* child);
    File(net::URI uri);

public:
    bool CanExecute();
    bool CanRead();
    bool CanWrite();
    int CompareTo(File pathname);
    bool CreateNewFile();
    static File CreateTempFile(const char* prefix, const char* suffix);
    static File CreateTempFile(const char* prefix, const char* suffix, File directory);
    bool Delete();
    void DeleteOnExit();
    bool Equals(lang::Object obj);
    bool Exists();
    File GetAbsoluteFile();
    char* GetAbsolutePath();
    File GetCanonicalFile();
    char* GetCanonicalPath();
    long long GetFreeSpace();
    char* GetName();
    char* GetParent();
    File GetParentFile();
    char* GetPath();
    long long GetTotalSpace();
    long long GetUsableSpace();
    int HashCode();
    bool IsAbsolute();
    bool IsDirectory();
    bool IsFile();
    bool IsHidden();
    long long LastModified();
    long long Length();
    char** List(int* size);
    char** List(int* size, FilenameFilter filter);
    File* ListFiles(int* size);
    File* ListFiles(int* size, FileFilter filter);
    File* ListFiles(int* size, FilenameFilter filter);
    static File* ListRoots(int* size);
    bool Mkdir();
    bool Mkdirs();
    bool RenameTo(File dest);
    bool SetExecutable(bool executable);
    bool SetExecutable(bool executable, bool ownerOnly);
    bool SetLastModified(long long time);
    bool SetReadable(bool readable);
    bool SetReadable(bool readable, bool ownerOnly);
    bool SetReadOnly();
    bool SetWritable(bool writable);
    bool SetWritable(bool writable, bool ownerOnly);
    nio::file::Path ToPath();
    char* ToString();
    net::URI ToURI();

public:
    jobject instance;

};

}

#endif