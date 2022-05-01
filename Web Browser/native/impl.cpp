#include "base.hpp"
#include "com_jnngl_system_BrowserImpl_Native.h"

class handle {
public:
	CefRefPtr<CefBrowser> browser;
	CefRefPtr<BrowserClient> client;
	Handler* handler;
};

/*
	* Class:     com_jnngl_system_BrowserImpl_Native
	* Method:    N_getURL
	* Signature: (Ljava/nio/ByteBuffer;)Ljava/lang/String;
	*/
JNIEXPORT jstring JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1getURL
(JNIEnv* env, jobject, jobject handleBuffer) {
	handle* data = reinterpret_cast<handle*>(env->GetDirectBufferAddress(handleBuffer));
	return env->NewStringUTF(((std::string)(data->browser->GetMainFrame()->GetURL())).c_str());
}

/*
	* Class:     com_jnngl_system_BrowserImpl_Native
	* Method:    N_getTitle
	* Signature: (Ljava/nio/ByteBuffer;)Ljava/lang/String;
	*/
JNIEXPORT jstring JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1getTitle
(JNIEnv* env, jobject, jobject handleBuffer) {
	handle* data = reinterpret_cast<handle*>(env->GetDirectBufferAddress(handleBuffer));
	return env->NewStringUTF(data->handler->title.c_str());
}

/*
	* Class:     com_jnngl_system_BrowserImpl_Native
	* Method:    N_createCef
	* Signature: (IILjava/lang/String;)Ljava/nio/ByteBuffer;
	*/
JNIEXPORT jobject JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1createCef
(JNIEnv* env, jobject, jint width, jint height, jstring path) {
	CefMainArgs args;

	CefRefPtr<CEF_noGPU> noGpu(new CEF_noGPU);
	{
		int result = CefExecuteProcess(args, noGpu.get(), nullptr);

		if (result >= 0) {
			printf("[impl.cpp](N_createCef) -> Failed to execute CEF process\n");
			return nullptr;
		}
	}

	CefSettings settings;
	settings.multi_threaded_message_loop = false;
	settings.no_sandbox = true;
	settings.windowless_rendering_enabled = true;
	const char* chars = env->GetStringUTFChars(path, 0);
	CefString(&settings.browser_subprocess_path).FromASCII(chars);
	bool result = CefInitialize(args, settings, noGpu, nullptr);

	if (!result) {
		printf("[impl.cpp](N_createCef) -> Failed to initialize CEF\n");
		return nullptr;
	}

	Handler* handler = new Handler(width, height);

	CefWindowInfo window_info;
	CefBrowserSettings browser_settings;
	browser_settings.windowless_frame_rate = 20;
	
	window_info.SetAsWindowless(0);

	CefRefPtr<BrowserClient> client = new BrowserClient(handler);
	CefRefPtr<CefBrowser> browser = CefBrowserHost::CreateBrowserSync(
		window_info, client.get(), "https://google.com", browser_settings, nullptr, nullptr);
	env->ReleaseStringUTFChars(path, chars);

	handle* native = new handle();
	native->browser = browser;
	native->client = client;
	native->handler = handler;
	return env->NewDirectByteBuffer(native, sizeof(handle*));
}

JNIEXPORT void JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1close
(JNIEnv* env, jobject, jobject handleBuffer) {
	handle* data = reinterpret_cast<handle*>(env->GetDirectBufferAddress(handleBuffer));
	data->browser->GetHost()->CloseBrowser(true);
	CefShutdown();
	data->browser = nullptr;
	data->client = nullptr;
	data->handler = nullptr;
}


JNIEXPORT void JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1render
(JNIEnv* env, jobject, jobject handleBuffer) {
	CefDoMessageLoopWork();
}

JNIEXPORT void JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1getSize
(JNIEnv* env, jobject, jobject handleBuffer, jintArray dstW, jintArray dstH) {
	handle* data = reinterpret_cast<handle*>(env->GetDirectBufferAddress(handleBuffer));
	env->SetIntArrayRegion(dstW, 0, 1, new jint[1]{ data->handler->width });
	env->SetIntArrayRegion(dstH, 0, 1, new jint[1]{ data->handler->height });
}

#ifndef min
#define min(a,b) (((a) < (b)) ? (a) : (b))
#endif
JNIEXPORT void JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1getPixels
(JNIEnv* env, jobject, jobject handleBuffer, jintArray dst) {
	handle* data = reinterpret_cast<handle*>(env->GetDirectBufferAddress(handleBuffer));
	size_t length = min((size_t)env->GetArrayLength(dst), data->handler->width*(size_t)data->handler->height);
	env->SetIntArrayRegion(dst, 0, length, (jint*)data->handler->data);
}

JNIEXPORT void JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1keyTyped
(JNIEnv* env, jobject, jobject handleBuffer, jchar jkey, jstring full) {
	handle* data = reinterpret_cast<handle*>(env->GetDirectBufferAddress(handleBuffer));

	char key = jkey;
	const char* text = env->GetStringUTFChars(full, 0);

	if (strcmp("BACKSPACE", text) == 0) key = 0x08;
	if (strcmp("SHIFT", text) == 0) key = 0x10;
	if (strcmp("CONTROL", text) == 0) key = 0x11;
	if (strcmp("TAB", text) == 0) key = 0x09;
	if (strcmp("ALT", text) == 0 || strcmp("FUNCTION", text) == 0 || strcmp("LANG", text) == 0 || strcmp("HOME", text) == 0) return;

	env->ReleaseStringUTFChars(full, text);

	{
		CefKeyEvent evt;
		evt.type = KEYEVENT_CHAR;
		evt.character = key;
		evt.unmodified_character = key;
		evt.native_key_code = key;
		evt.windows_key_code = key;
		evt.is_system_key = false;

		data->browser->GetHost()->SendKeyEvent(evt);
	}
}

JNIEXPORT void JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1setSize
(JNIEnv* env, jobject, jobject handleBuffer, jint width, jint height) {
	handle* data = reinterpret_cast<handle*>(env->GetDirectBufferAddress(handleBuffer));
	data->handler->resize(width, height);
	data->browser->GetHost()->NotifyScreenInfoChanged();
}

JNIEXPORT void JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1goBack
(JNIEnv* env, jobject, jobject handleBuffer) {
	handle* data = reinterpret_cast<handle*>(env->GetDirectBufferAddress(handleBuffer));
	data->browser->GoBack();
}

JNIEXPORT void JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1goForward
(JNIEnv* env, jobject, jobject handleBuffer) {
	handle* data = reinterpret_cast<handle*>(env->GetDirectBufferAddress(handleBuffer));
	data->browser->GoForward();
}

JNIEXPORT void JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1loadURL
(JNIEnv* env, jobject, jobject handleBuffer, jstring url) {
	handle* data = reinterpret_cast<handle*>(env->GetDirectBufferAddress(handleBuffer));
	const char* chars = env->GetStringUTFChars(url, 0);
	data->browser->GetMainFrame()->LoadURL({ chars });
	env->ReleaseStringUTFChars(url, chars);
}

JNIEXPORT jboolean JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1canGoBack
(JNIEnv* env, jobject, jobject handleBuffer) {
	handle* data = reinterpret_cast<handle*>(env->GetDirectBufferAddress(handleBuffer));
	return data->browser->CanGoBack();
}

JNIEXPORT jboolean JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1canGoForward
(JNIEnv* env, jobject, jobject handleBuffer) {
	handle* data = reinterpret_cast<handle*>(env->GetDirectBufferAddress(handleBuffer));
	return data->browser->CanGoForward();
}

JNIEXPORT void JNICALL Java_com_jnngl_system_BrowserImpl_1Native_N_1click
(JNIEnv* env, jobject, jobject handleBuffer, jint x, jint y) {
	handle* data = reinterpret_cast<handle*>(env->GetDirectBufferAddress(handleBuffer));
	data->browser->GetHost()->SendMouseClickEvent(
		_cef_mouse_event_t{ x, y, (uint32)cef_event_flags_t::EVENTFLAG_NONE },
		CefBrowserHost::MouseButtonType::MBT_LEFT, false, 1
	);
	data->browser->GetHost()->SendMouseClickEvent(
		_cef_mouse_event_t{ x, y, (uint32)cef_event_flags_t::EVENTFLAG_NONE },
		CefBrowserHost::MouseButtonType::MBT_LEFT, true, 1
	);
}