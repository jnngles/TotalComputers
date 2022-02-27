#include "j_env.h"
#include "utils.h"
#include "net.h"

using namespace net;

// java/net/SocketAddress
// Name: SocketAddress
// Base Class(es): [lang::Object]

NativeObject clsSocketAddress;
jclass InitSocketAddress() {
   return tcInitNativeObject(clsSocketAddress, "java/net/SocketAddress", { 
       }, { 
       });
}

SocketAddress::SocketAddress(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitSocketAddress();
}

SocketAddress::~SocketAddress() {}


// java/net/Proxy
// Name: Proxy
// Base Class(es): [lang::Object]

NativeObject clsProxy;
jclass InitProxy() {
   return tcInitNativeObject(clsProxy, "java/net/Proxy", { 
          { "address", "()Ljava/net/SocketAddress;", false }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "hashCode", "()I", false }, 
          { "toString", "()Ljava/lang/String;", false }, 
          { "type", "()Ljava/net/Proxy$Type;", false }
       }, { 
          { "NO_PROXY", "Ljava/net/Proxy;", true }
       });
}

Proxy::Proxy(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitProxy();
}

Proxy::~Proxy() {}

Proxy::Proxy(Proxy::Type type, SocketAddress sa)
 : instance(tc::jEnv->NewObject(InitProxy(), tc::jEnv->GetMethodID(InitProxy(), "<init>", "(Ljava/net/Proxy$Type;Ljava/net/SocketAddress;)V"), type.instance, sa.instance)), lang::Object(instance)
{}

Proxy Proxy::NO_PROXY() {
   InitProxy();
   return (Proxy)(tc::jEnv->GetStaticObjectField(clsProxy.clazz, clsProxy.fields[0]));
}

SocketAddress Proxy::Address() {
   return (SocketAddress)(tc::jEnv->CallObjectMethod(METHOD(clsProxy, 0)));
}

bool Proxy::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsProxy, 1), obj.instance);
}

int Proxy::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsProxy, 2));
}

char* Proxy::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsProxy, 3)));
}

Proxy::Type Proxy::Type0() {
   return (Proxy::Type)(tc::jEnv->CallObjectMethod(METHOD(clsProxy, 4)));
}


// java/net/Proxy/Type
// Name: Proxy::Type
// Base Class(es): [lang::Enum]

NativeObject clsProxy_Type;
jclass InitProxy_Type() {
   return tcInitNativeObject(clsProxy_Type, "java/net/Proxy/Type", { 
       }, { 
          { "DIRECT", "Ljava/net/Proxy$Type;", true }, 
          { "HTTP", "Ljava/net/Proxy$Type;", true }, 
          { "SOCKS", "Ljava/net/Proxy$Type;", true }
       });
}

Proxy::Type::Type(jobject instance)
 : instance(instance), lang::Enum(instance) {
    InitProxy_Type();
}

Proxy::Type::~Type() {}

Proxy::Type Proxy::Type::DIRECT() {
   InitProxy_Type();
   return (Proxy::Type)(tc::jEnv->GetStaticObjectField(clsProxy_Type.clazz, clsProxy_Type.fields[0]));
}

Proxy::Type Proxy::Type::HTTP() {
   InitProxy_Type();
   return (Proxy::Type)(tc::jEnv->GetStaticObjectField(clsProxy_Type.clazz, clsProxy_Type.fields[1]));
}

Proxy::Type Proxy::Type::SOCKS() {
   InitProxy_Type();
   return (Proxy::Type)(tc::jEnv->GetStaticObjectField(clsProxy_Type.clazz, clsProxy_Type.fields[2]));
}


// java/net/ContentHandler
// Name: ContentHandler
// Base Class(es): [lang::Object]

NativeObject clsContentHandler;
jclass InitContentHandler() {
   return tcInitNativeObject(clsContentHandler, "java/net/ContentHandler", { 
          { "getContent", "(Ljava/net/URLConnection;)Ljava/lang/Object;", false }
       }, { 
       });
}

ContentHandler::ContentHandler(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitContentHandler();
}

ContentHandler::~ContentHandler() {}

lang::Object ContentHandler::GetContent(URLConnection urlc) {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsContentHandler, 0), urlc.instance));
}


// java/net/ContentHandlerFactory
// Name: ContentHandlerFactory
// Base Class(es): [NativeObjectInstance]

NativeObject clsContentHandlerFactory;
jclass InitContentHandlerFactory() {
   return tcInitNativeObject(clsContentHandlerFactory, "java/net/ContentHandlerFactory", { 
          { "createContentHandler", "(Ljava/lang/String;)Ljava/net/ContentHandler;", false }
       }, { 
       });
}

ContentHandlerFactory::ContentHandlerFactory(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitContentHandlerFactory();
}

ContentHandlerFactory::~ContentHandlerFactory() {}

ContentHandler ContentHandlerFactory::CreateContentHandler(const char* mimetype) {
   return (ContentHandler)(tc::jEnv->CallObjectMethod(METHOD(clsContentHandlerFactory, 0), ToJString(mimetype)));
}


// java/net/FileNameMap
// Name: FileNameMap
// Base Class(es): [NativeObjectInstance]

NativeObject clsFileNameMap;
jclass InitFileNameMap() {
   return tcInitNativeObject(clsFileNameMap, "java/net/FileNameMap", { 
          { "getContentTypeFor", "(Ljava/lang/String;)Ljava/lang/String;", false }
       }, { 
       });
}

FileNameMap::FileNameMap(jobject instance)
 : instance(instance), NativeObjectInstance(instance) {
    InitFileNameMap();
}

FileNameMap::~FileNameMap() {}

char* FileNameMap::GetContentTypeFor(const char* fileName) {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsFileNameMap, 0), ToJString(fileName)));
}


// java/net/URLStreamHandler
// Name: URLStreamHandler
// Base Class(es): [lang::Object]

NativeObject clsURLStreamHandler;
jclass InitURLStreamHandler() {
   return tcInitNativeObject(clsURLStreamHandler, "java/net/URLStreamHandler", { 
       }, { 
       });
}

URLStreamHandler::URLStreamHandler(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitURLStreamHandler();
}

URLStreamHandler::~URLStreamHandler() {}


// java/net/URLConnection
// Name: URLConnection
// Base Class(es): [lang::Object]

NativeObject clsURLConnection;
jclass InitURLConnection() {
   return tcInitNativeObject(clsURLConnection, "java/net/URLConnection", { 
          { "addRequestProperty", "(Ljava/lang/String;Ljava/lang/String;)V", false }, 
          { "connect", "()V", false }, 
          { "getAllowUserInteraction", "()Z", false }, 
          { "getConnectTimeout", "()I", false }, 
          { "getContent", "()Ljava/lang/Object;", false }, 
          { "getContentEncoding", "()Ljava/lang/String;", false }, 
          { "getContentLength", "()I", false }, 
          { "getContentLengthLong", "()J", false }, 
          { "getContentType", "()Ljava/lang/String;", false }, 
          { "getDate", "()J", false }, 
          { "getDefaultAllowUserInteraction", "()Z", true }, 
          { "getDefaultUseCaches", "()Z", false }, 
          { "getDoInput", "()Z", false }, 
          { "getDoOutput", "()Z", false }, 
          { "getExpiration", "()J", false }, 
          { "getFileNameMap", "()Ljava/net/FileNameMap;", true }, 
          { "getHeaderField", "(I)Ljava/lang/String;", false }, 
          { "getHeaderField", "(Ljava/lang/String;)Ljava/lang/String;", false }, 
          { "getHeaderFieldDate", "(Ljava/lang/String;J)J", false }, 
          { "getHeaderFieldInt", "(Ljava/lang/String;I)I", false }, 
          { "getHeaderFieldKey", "(I)Ljava/lang/String;", false }, 
          { "getHeaderFieldLong", "(Ljava/lang/String;J)J", false }, 
          { "getHeaderFields", "()Ljava/util/Map;", false }, 
          { "getIfModifiedSince", "()J", false }, 
          { "getInputStream", "()Ljava/io/InputStream;", false }, 
          { "getLastModified", "()J", false }, 
          { "getOutputStream", "()Ljava/io/OutputStream;", false }, 
          { "getPermission", "()Ljava/security/Permission;", false }, 
          { "getReadTimeout", "()I", false }, 
          { "getRequestProperties", "()Ljava/util/Map;", false }, 
          { "getRequestProperty", "(Ljava/lang/String;)Ljava/lang/String;", false }, 
          { "getURL", "()Ljava/net/URL;", false }, 
          { "getUseCaches", "()Z", false }, 
          { "guessContentTypeFromName", "(Ljava/lang/String;)Ljava/lang/String;", true }, 
          { "guessContentTypeFromStream", "(Ljava/io/InputStream;)Ljava/lang/String;", true }, 
          { "setAllowUserInteraction", "(Z)V", false }, 
          { "setConnectTimeout", "(I)V", false }, 
          { "setContentHandlerFactory", "(Ljava/net/ContentHandlerFactory;)V", true }, 
          { "setDefaultAllowUserInteraction", "(Z)V", true }, 
          { "setDefaultUseCaches", "(Z)V", false }, 
          { "setDoInput", "(Z)V", false }, 
          { "setDoOutput", "(Z)V", false }, 
          { "setFileNameMap", "(Ljava/net/FileNameMap;)V", true }, 
          { "setIfModifiedSince", "(J)V", false }, 
          { "setReadTimeout", "(I)V", false }, 
          { "setRequestProperty", "(Ljava/lang/String;Ljava/lang/String;)V", false }, 
          { "setUseCaches", "(Z)V", false }, 
          { "toString", "()Ljava/lang/String;", false }
       }, { 
       });
}

URLConnection::URLConnection(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitURLConnection();
}

URLConnection::~URLConnection() {}

void URLConnection::AddRequestProperty(const char* key, const char* value) {
   tc::jEnv->CallVoidMethod(METHOD(clsURLConnection, 0), ToJString(key), ToJString(value));
}

void URLConnection::Connect() {
   tc::jEnv->CallVoidMethod(METHOD(clsURLConnection, 1));
}

bool URLConnection::GetAllowUserInteraction() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsURLConnection, 2));
}

int URLConnection::GetConnectTimeout() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsURLConnection, 3));
}

lang::Object URLConnection::GetContent() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 4)));
}

char* URLConnection::GetContentEncoding() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 5)));
}

int URLConnection::GetContentLength() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsURLConnection, 6));
}

long long URLConnection::GetContentLengthLong() {
   return (long long)(tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 7)));
}

char* URLConnection::GetContentType() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 8)));
}

long long URLConnection::GetDate() {
   return (long long)(tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 9)));
}

bool URLConnection::GetDefaultAllowUserInteraction() {
   InitURLConnection();
   return (bool)tc::jEnv->CallStaticBooleanMethod(clsURLConnection.clazz, clsURLConnection.methods[10]);
}

bool URLConnection::GetDefaultUseCaches() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsURLConnection, 11));
}

bool URLConnection::GetDoInput() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsURLConnection, 12));
}

bool URLConnection::GetDoOutput() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsURLConnection, 13));
}

long long URLConnection::GetExpiration() {
   return (long long)(tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 14)));
}

FileNameMap URLConnection::GetFileNameMap() {
   InitURLConnection();
   return (FileNameMap)(tc::jEnv->CallStaticObjectMethod(clsURLConnection.clazz, clsURLConnection.methods[15]));
}

char* URLConnection::GetHeaderField(int n) {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 16), (jint)n));
}

char* URLConnection::GetHeaderField(const char* name) {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 17), ToJString(name)));
}

long long URLConnection::GetHeaderFieldDate(const char* name, long long Default) {
   return (long long)(tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 18), ToJString(name), (jlong)Default));
}

int URLConnection::GetHeaderFieldInt(const char* name, int Default) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsURLConnection, 19), ToJString(name), (jint)Default);
}

char* URLConnection::GetHeaderFieldKey(int n) {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 20), (jint)n));
}

long long URLConnection::GetHeaderFieldLong(const char* name, long long Default) {
   return (long long)(tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 21), ToJString(name), (jlong)Default));
}

util::Map URLConnection::GetHeaderFields() {
   return (util::Map)(tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 22)));
}

long long URLConnection::GetIfModifiedSince() {
   return (long long)(tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 23)));
}

io::InputStream URLConnection::GetInputStream() {
   return (io::InputStream)(tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 24)));
}

long long URLConnection::GetLastModified() {
   return (long long)(tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 25)));
}

io::OutputStream URLConnection::GetOutputStream() {
   return (io::OutputStream)(tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 26)));
}

security::Permission URLConnection::GetPermission() {
   return (security::Permission)(tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 27)));
}

int URLConnection::GetReadTimeout() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsURLConnection, 28));
}

util::Map URLConnection::GetRequestProperties() {
   return (util::Map)(tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 29)));
}

char* URLConnection::GetRequestProperty(const char* key) {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 30), ToJString(key)));
}

URL URLConnection::GetURL() {
   return (URL)(tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 31)));
}

bool URLConnection::GetUseCaches() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsURLConnection, 32));
}

char* URLConnection::GuessContentTypeFromName(const char* fname) {
   InitURLConnection();
   return FromJString((jstring)tc::jEnv->CallStaticObjectMethod(clsURLConnection.clazz, clsURLConnection.methods[33], ToJString(fname)));
}

char* URLConnection::GuessContentTypeFromStream(io::InputStream is) {
   InitURLConnection();
   return FromJString((jstring)tc::jEnv->CallStaticObjectMethod(clsURLConnection.clazz, clsURLConnection.methods[34], is.instance));
}

void URLConnection::SetAllowUserInteraction(bool allowuserinteraction) {
   tc::jEnv->CallVoidMethod(METHOD(clsURLConnection, 35), (jboolean)allowuserinteraction);
}

void URLConnection::SetConnectTimeout(int timeout) {
   tc::jEnv->CallVoidMethod(METHOD(clsURLConnection, 36), (jint)timeout);
}

void URLConnection::SetContentHandlerFactory(ContentHandlerFactory fac) {
   InitURLConnection();
   tc::jEnv->CallStaticVoidMethod(clsURLConnection.clazz, clsURLConnection.methods[37], fac.instance);
}

void URLConnection::SetDefaultAllowUserInteraction(bool defaultallowuserinteraction) {
   InitURLConnection();
   tc::jEnv->CallStaticVoidMethod(clsURLConnection.clazz, clsURLConnection.methods[38], (jboolean)defaultallowuserinteraction);
}

void URLConnection::SetDefaultUseCaches(bool defaultusecaches) {
   tc::jEnv->CallVoidMethod(METHOD(clsURLConnection, 39), (jboolean)defaultusecaches);
}

void URLConnection::SetDoInput(bool doinput) {
   tc::jEnv->CallVoidMethod(METHOD(clsURLConnection, 40), (jboolean)doinput);
}

void URLConnection::SetDoOutput(bool dooutput) {
   tc::jEnv->CallVoidMethod(METHOD(clsURLConnection, 41), (jboolean)dooutput);
}

void URLConnection::SetFileNameMap(FileNameMap map) {
   InitURLConnection();
   tc::jEnv->CallStaticVoidMethod(clsURLConnection.clazz, clsURLConnection.methods[42], map.instance);
}

void URLConnection::SetIfModifiedSince(long long ifmodifiedsince) {
   tc::jEnv->CallVoidMethod(METHOD(clsURLConnection, 43), (jlong)ifmodifiedsince);
}

void URLConnection::SetReadTimeout(int timeout) {
   tc::jEnv->CallVoidMethod(METHOD(clsURLConnection, 44), (jint)timeout);
}

void URLConnection::SetRequestProperty(const char* key, const char* value) {
   tc::jEnv->CallVoidMethod(METHOD(clsURLConnection, 45), ToJString(key), ToJString(value));
}

void URLConnection::SetUseCaches(bool usecaches) {
   tc::jEnv->CallVoidMethod(METHOD(clsURLConnection, 46), (jboolean)usecaches);
}

char* URLConnection::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURLConnection, 47)));
}


// java/net/URL
// Name: URL
// Base Class(es): [lang::Object]

NativeObject clsURL;
jclass InitURL() {
   return tcInitNativeObject(clsURL, "java/net/URL", { 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "getAuthority", "()Ljava/lang/String;", false }, 
          { "getContent", "()Ljava/lang/Object;", false }, 
          { "getDefaultPort", "()I", false }, 
          { "getFile", "()Ljava/lang/String;", false }, 
          { "getHost", "()Ljava/lang/String;", false }, 
          { "getPath", "()Ljava/lang/String;", false }, 
          { "getPort", "()I", false }, 
          { "getProtocol", "()Ljava/lang/String;", false }, 
          { "getQuery", "()Ljava/lang/String;", false }, 
          { "getRef", "()Ljava/lang/String;", false }, 
          { "getUserInfo", "()Ljava/lang/String;", false }, 
          { "hashCode", "()I", false }, 
          { "openConnection", "()Ljava/net/URLConnection;", false }, 
          { "openConnection", "(Ljava/net/Proxy;)Ljava/net/URLConnection;", false }, 
          { "openStream", "()Ljava/io/InputStream;", false }, 
          { "sameFile", "(Ljava/net/URL;)Z", false }, 
          { "toExternalForm", "()Ljava/lang/String;", false }, 
          { "toString", "()Ljava/lang/String;", false }, 
          { "toURI", "()Ljava/net/URI;", false }
       }, { 
       });
}

URL::URL(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitURL();
}

URL::~URL() {}

URL::URL(const char* spec)
 : instance(tc::jEnv->NewObject(InitURL(), tc::jEnv->GetMethodID(InitURL(), "<init>", "(Ljava/lang/String;)V"), ToJString(spec))), lang::Object(instance)
{}

URL::URL(const char* protocol, const char* host, int port, const char* file)
 : instance(tc::jEnv->NewObject(InitURL(), tc::jEnv->GetMethodID(InitURL(), "<init>", "(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;)V"), ToJString(protocol), ToJString(host), (jint)port, ToJString(file))), lang::Object(instance)
{}

URL::URL(const char* protocol, const char* host, int port, const char* file, URLStreamHandler handler)
 : instance(tc::jEnv->NewObject(InitURL(), tc::jEnv->GetMethodID(InitURL(), "<init>", "(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/net/URLStreamHandler;)V"), ToJString(protocol), ToJString(host), (jint)port, ToJString(file), handler.instance)), lang::Object(instance)
{}

URL::URL(const char* protocol, const char* host, const char* file)
 : instance(tc::jEnv->NewObject(InitURL(), tc::jEnv->GetMethodID(InitURL(), "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"), ToJString(protocol), ToJString(host), ToJString(file))), lang::Object(instance)
{}

URL::URL(URL context, const char* spec)
 : instance(tc::jEnv->NewObject(InitURL(), tc::jEnv->GetMethodID(InitURL(), "<init>", "(Ljava/net/URL;Ljava/lang/String;)V"), context.instance, ToJString(spec))), lang::Object(instance)
{}

URL::URL(URL context, const char* spec, URLStreamHandler handler)
 : instance(tc::jEnv->NewObject(InitURL(), tc::jEnv->GetMethodID(InitURL(), "<init>", "(Ljava/net/URL;Ljava/lang/String;Ljava/net/URLStreamHandler;)V"), context.instance, ToJString(spec), handler.instance)), lang::Object(instance)
{}

bool URL::Equals(lang::Object obj) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsURL, 0), obj.instance);
}

char* URL::GetAuthority() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURL, 1)));
}

lang::Object URL::GetContent() {
   return (lang::Object)(tc::jEnv->CallObjectMethod(METHOD(clsURL, 2)));
}

int URL::GetDefaultPort() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsURL, 3));
}

char* URL::GetFile() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURL, 4)));
}

char* URL::GetHost() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURL, 5)));
}

char* URL::GetPath() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURL, 6)));
}

int URL::GetPort() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsURL, 7));
}

char* URL::GetProtocol() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURL, 8)));
}

char* URL::GetQuery() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURL, 9)));
}

char* URL::GetRef() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURL, 10)));
}

char* URL::GetUserInfo() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURL, 11)));
}

int URL::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsURL, 12));
}

URLConnection URL::OpenConnection() {
   return (URLConnection)(tc::jEnv->CallObjectMethod(METHOD(clsURL, 13)));
}

URLConnection URL::OpenConnection(Proxy proxy) {
   return (URLConnection)(tc::jEnv->CallObjectMethod(METHOD(clsURL, 14), proxy.instance));
}

io::InputStream URL::OpenStream() {
   return (io::InputStream)(tc::jEnv->CallObjectMethod(METHOD(clsURL, 15)));
}

bool URL::SameFile(URL other) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsURL, 16), other.instance);
}

char* URL::ToExternalForm() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURL, 17)));
}

char* URL::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURL, 18)));
}

URI URL::ToURI() {
   return (URI)(tc::jEnv->CallObjectMethod(METHOD(clsURL, 19)));
}


// java/net/URI
// Name: URI
// Base Class(es): [lang::Object]

NativeObject clsURI;
jclass InitURI() {
   return tcInitNativeObject(clsURI, "java/net/URI", { 
          { "compareTo", "(Ljava/net/URI;)I", false }, 
          { "create", "(Ljava/lang/String;)Ljava/net/URI;", true }, 
          { "equals", "(Ljava/lang/Object;)Z", false }, 
          { "getAuthority", "()Ljava/lang/String;", false }, 
          { "getFragment", "()Ljava/lang/String;", false }, 
          { "getHost", "()Ljava/lang/String;", false }, 
          { "getPath", "()Ljava/lang/String;", false }, 
          { "getPort", "()I", false }, 
          { "getQuery", "()Ljava/lang/String;", false }, 
          { "getRawAuthority", "()Ljava/lang/String;", false }, 
          { "getRawFragment", "()Ljava/lang/String;", false }, 
          { "getRawPath", "()Ljava/lang/String;", false }, 
          { "getRawQuery", "()Ljava/lang/String;", false }, 
          { "getRawSchemeSpecificPart", "()Ljava/lang/String;", false }, 
          { "getRawUserInfo", "()Ljava/lang/String;", false }, 
          { "getScheme", "()Ljava/lang/String;", false }, 
          { "getSchemeSpecificPart", "()Ljava/lang/String;", false }, 
          { "getUserInfo", "()Ljava/lang/String;", false }, 
          { "hashCode", "()I", false }, 
          { "isAbsolute", "()Z", false }, 
          { "isOpaque", "()Z", false }, 
          { "normalize", "()Ljava/net/URI;", false }, 
          { "parseServerAuthority", "()Ljava/net/URI;", false }, 
          { "relativize", "(Ljava/net/URI;)Ljava/net/URI;", false }, 
          { "resolve", "(Ljava/lang/String;)Ljava/net/URI;", false }, 
          { "resolve", "(Ljava/net/URI;)Ljava/net/URI;", false }, 
          { "toASCIIString", "()Ljava/lang/String;", false }, 
          { "toString", "()Ljava/lang/String;", false }, 
          { "toURL", "()Ljava/net/URL;", false }
       }, { 
       });
}

URI::URI(jobject instance)
 : instance(instance), lang::Object(instance) {
    InitURI();
}

URI::~URI() {}

URI::URI(const char* str)
 : instance(tc::jEnv->NewObject(InitURI(), tc::jEnv->GetMethodID(InitURI(), "<init>", "(Ljava/lang/String;)V"), ToJString(str))), lang::Object(instance)
{}

URI::URI(const char* scheme, const char* ssp, const char* fragment)
 : instance(tc::jEnv->NewObject(InitURI(), tc::jEnv->GetMethodID(InitURI(), "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"), ToJString(scheme), ToJString(ssp), ToJString(fragment))), lang::Object(instance)
{}

URI::URI(const char* scheme, const char* userInfo, const char* host, int port, const char* path, const char* query, const char* fragment)
 : instance(tc::jEnv->NewObject(InitURI(), tc::jEnv->GetMethodID(InitURI(), "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"), ToJString(scheme), ToJString(userInfo), ToJString(host), (jint)port, ToJString(path), ToJString(query), ToJString(fragment))), lang::Object(instance)
{}

URI::URI(const char* scheme, const char* host, const char* path, const char* fragment)
 : instance(tc::jEnv->NewObject(InitURI(), tc::jEnv->GetMethodID(InitURI(), "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"), ToJString(scheme), ToJString(host), ToJString(path), ToJString(fragment))), lang::Object(instance)
{}

URI::URI(const char* scheme, const char* authority, const char* path, const char* query, const char* fragment)
 : instance(tc::jEnv->NewObject(InitURI(), tc::jEnv->GetMethodID(InitURI(), "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"), ToJString(scheme), ToJString(authority), ToJString(path), ToJString(query), ToJString(fragment))), lang::Object(instance)
{}

int URI::CompareTo(URI that) {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsURI, 0), that.instance);
}

URI URI::Create(const char* str) {
   InitURI();
   return (URI)(tc::jEnv->CallStaticObjectMethod(clsURI.clazz, clsURI.methods[1], ToJString(str)));
}

bool URI::Equals(lang::Object ob) {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsURI, 2), ob.instance);
}

char* URI::GetAuthority() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 3)));
}

char* URI::GetFragment() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 4)));
}

char* URI::GetHost() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 5)));
}

char* URI::GetPath() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 6)));
}

int URI::GetPort() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsURI, 7));
}

char* URI::GetQuery() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 8)));
}

char* URI::GetRawAuthority() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 9)));
}

char* URI::GetRawFragment() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 10)));
}

char* URI::GetRawPath() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 11)));
}

char* URI::GetRawQuery() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 12)));
}

char* URI::GetRawSchemeSpecificPart() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 13)));
}

char* URI::GetRawUserInfo() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 14)));
}

char* URI::GetScheme() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 15)));
}

char* URI::GetSchemeSpecificPart() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 16)));
}

char* URI::GetUserInfo() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 17)));
}

int URI::HashCode() {
   return (int)tc::jEnv->CallIntMethod(METHOD(clsURI, 18));
}

bool URI::IsAbsolute() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsURI, 19));
}

bool URI::IsOpaque() {
   return (bool)tc::jEnv->CallBooleanMethod(METHOD(clsURI, 20));
}

URI URI::Normalize() {
   return (URI)(tc::jEnv->CallObjectMethod(METHOD(clsURI, 21)));
}

URI URI::ParseServerAuthority() {
   return (URI)(tc::jEnv->CallObjectMethod(METHOD(clsURI, 22)));
}

URI URI::Relativize(URI uri) {
   return (URI)(tc::jEnv->CallObjectMethod(METHOD(clsURI, 23), uri.instance));
}

URI URI::Resolve(const char* str) {
   return (URI)(tc::jEnv->CallObjectMethod(METHOD(clsURI, 24), ToJString(str)));
}

URI URI::Resolve(URI uri) {
   return (URI)(tc::jEnv->CallObjectMethod(METHOD(clsURI, 25), uri.instance));
}

char* URI::ToASCIIString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 26)));
}

char* URI::ToString() {
   return FromJString((jstring)tc::jEnv->CallObjectMethod(METHOD(clsURI, 27)));
}

URL URI::ToURL() {
   return (URL)(tc::jEnv->CallObjectMethod(METHOD(clsURI, 28)));
}


