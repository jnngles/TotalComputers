#ifndef TC_NET_H
#define TC_NET_H

#include "util.h"
#include "io.h"
#include "security.h"

/// STRUCT

namespace net {
    class SocketAddress;
    class Proxy;
    class ContentHandler;
    class ContentHandlerFactory;
    class FileNameMap;
    class URLStreamHandler;
    class URLConnection;
    class URL;
    class URI;
}

/// CLASSES

namespace net {

class SocketAddress : public lang::Object {
public:
    SocketAddress(jobject instance);
    ~SocketAddress();

public:
    jobject instance;

};

class Proxy : public lang::Object {
public:
    class Type;

public:
    Proxy(jobject instance);
    ~Proxy();

public:
    Proxy(Proxy::Type type, SocketAddress sa);

public:
    __FIELD__ static Proxy NO_PROXY();

public:
    SocketAddress Address();
    bool Equals(lang::Object obj);
    int HashCode();
    char* ToString();
    Proxy::Type Type0();

public:
    jobject instance;

};

class Proxy::Type : public lang::Enum {
public:
    Type(jobject instance);
    ~Type();

public:
    __FIELD__ static Proxy::Type DIRECT();
    __FIELD__ static Proxy::Type HTTP();
    __FIELD__ static Proxy::Type SOCKS();

public:
    jobject instance;

};

class ContentHandler : public lang::Object {
public:
    ContentHandler(jobject instance);
    ~ContentHandler();

public:
    lang::Object GetContent(URLConnection urlc);

public:
    jobject instance;

};

class ContentHandlerFactory : public NativeObjectInstance {
public:
    ContentHandlerFactory(jobject instance);
    ~ContentHandlerFactory();

public:
    ContentHandler CreateContentHandler(const char* mimetype);

public:
    jobject instance;

};

class FileNameMap : public NativeObjectInstance {
public:
    FileNameMap(jobject instance);
    ~FileNameMap();

public:
    char* GetContentTypeFor(const char* fileName);

public:
    jobject instance;

};

class URLStreamHandler : public lang::Object {
public:
    URLStreamHandler(jobject instance);
    ~URLStreamHandler();

public:
    jobject instance;

};

class URLConnection : public lang::Object {
public:
    URLConnection(jobject instance);
    ~URLConnection();

public:
    void AddRequestProperty(const char* key, const char* value);
    void Connect();
    bool GetAllowUserInteraction();
    int GetConnectTimeout();
    lang::Object GetContent();
    char* GetContentEncoding();
    int GetContentLength();
    long long GetContentLengthLong();
    char* GetContentType();
    long long GetDate();
    static bool GetDefaultAllowUserInteraction();
    bool GetDefaultUseCaches();
    bool GetDoInput();
    bool GetDoOutput();
    long long GetExpiration();
    static FileNameMap GetFileNameMap();
    char* GetHeaderField(int n);
    char* GetHeaderField(const char* name);
    long long GetHeaderFieldDate(const char* name, long long Default);
    int GetHeaderFieldInt(const char* name, int Default);
    char* GetHeaderFieldKey(int n);
    long long GetHeaderFieldLong(const char* name, long long Default);
    util::Map GetHeaderFields();
    long long GetIfModifiedSince();
    io::InputStream GetInputStream();
    long long GetLastModified();
    io::OutputStream GetOutputStream();
    security::Permission GetPermission();
    int GetReadTimeout();
    util::Map GetRequestProperties();
    char* GetRequestProperty(const char* key);
    URL GetURL();
    bool GetUseCaches();
    static char* GuessContentTypeFromName(const char* fname);
    static char* GuessContentTypeFromStream(io::InputStream is);
    void SetAllowUserInteraction(bool allowuserinteraction);
    void SetConnectTimeout(int timeout);
    static void SetContentHandlerFactory(ContentHandlerFactory fac);
    static void SetDefaultAllowUserInteraction(bool defaultallowuserinteraction);
    void SetDefaultUseCaches(bool defaultusecaches);
    void SetDoInput(bool doinput);
    void SetDoOutput(bool dooutput);
    static void SetFileNameMap(FileNameMap map);
    void SetIfModifiedSince(long long ifmodifiedsince);
    void SetReadTimeout(int timeout);
    void SetRequestProperty(const char* key, const char* value);
    void SetUseCaches(bool usecaches);
    char* ToString();

public:
    jobject instance;

};

class URL : public lang::Object {
public:
    URL(jobject instance);
    ~URL();

public:
    URL(const char* spec);
    URL(const char* protocol, const char* host, int port, const char* file);
    URL(const char* protocol, const char* host, int port, const char* file, URLStreamHandler handler);
    URL(const char* protocol, const char* host, const char* file);
    URL(URL context, const char* spec);
    URL(URL context, const char* spec, URLStreamHandler handler);

public:
    bool Equals(lang::Object obj);
    char* GetAuthority();
    lang::Object GetContent();
    int GetDefaultPort();
    char* GetFile();
    char* GetHost();
    char* GetPath();
    int GetPort();
    char* GetProtocol();
    char* GetQuery();
    char* GetRef();
    char* GetUserInfo();
    int HashCode();
    URLConnection OpenConnection();
    URLConnection OpenConnection(Proxy proxy);
    io::InputStream OpenStream();
    bool SameFile(URL other);
    char* ToExternalForm();
    char* ToString();
    URI ToURI();

public:
    jobject instance;

};

class URI : public lang::Object {
public:
    URI(jobject instance);
    ~URI();

public:
    URI(const char* str);
    URI(const char* scheme, const char* ssp, const char* fragment);
    URI(const char* scheme, const char* userInfo, const char* host, int port, const char* path, const char* query, const char* fragment);
    URI(const char* scheme, const char* host, const char* path, const char* fragment);
    URI(const char* scheme, const char* authority, const char* path, const char* query, const char* fragment);

public:
    int CompareTo(URI that);
    static URI Create(const char* str);
    bool Equals(lang::Object ob);
    char* GetAuthority();
    char* GetFragment();
    char* GetHost();
    char* GetPath();
    int GetPort();
    char* GetQuery();
    char* GetRawAuthority();
    char* GetRawFragment();
    char* GetRawPath();
    char* GetRawQuery();
    char* GetRawSchemeSpecificPart();
    char* GetRawUserInfo();
    char* GetScheme();
    char* GetSchemeSpecificPart();
    char* GetUserInfo();
    int HashCode();
    bool IsAbsolute();
    bool IsOpaque();
    URI Normalize();
    URI ParseServerAuthority();
    URI Relativize(URI uri);
    URI Resolve(const char* str);
    URI Resolve(URI uri);
    char* ToASCIIString();
    char* ToString();
    URL ToURL();

public:
    jobject instance;

};

}

#endif