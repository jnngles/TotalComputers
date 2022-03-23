#include "com_jnngl_system_WebBrowserBootstrap.h"
#include "base.hpp"

struct browser {
	CefRefPtr<CefBrowser> browser;
	CefRefPtr<CefClient> client;
	Handler* handler;
};

/*
 * Class:     com_jnngl_system_WebBrowserBootstrap
 * Method:    InitCEF
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_jnngl_system_WebBrowserBootstrap_InitCEF
(JNIEnv* env, jclass, jstring path) {
	bool error = false;
	CefMainArgs args(GetModuleHandle(NULL));

	{
		int result = CefExecuteProcess(args, nullptr, nullptr);

		if (result >= 0) {
			error = true;
			std::cout << "Failed to execute process" << std::endl;
		}
	}

	CefSettings settings;
	settings.multi_threaded_message_loop = false;
	settings.windowless_rendering_enabled = true;
	const char* pathChr = env->GetStringUTFChars(path, nullptr);
	std::string pathStr = std::string(pathChr) + "/cefclient.exe";
	std::wstring pathWstr = std::wstring(pathStr.begin(), pathStr.end());
	env->ReleaseStringUTFChars(path, pathChr);
	settings.browser_subprocess_path = { _wcsdup(pathWstr.c_str()), pathStr.size() };
	bool result = CefInitialize(args, settings, nullptr, nullptr);

	if (!result) {
		error = true;
		std::cout << "Failed to initialize CEF" << std::endl;
	}

	return error;
}

/*
 * Class:     com_jnngl_system_WebBrowserBootstrap
 * Method:    CreateBrowser
 * Signature: (II)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_com_jnngl_system_WebBrowserBootstrap_CreateBrowser
(JNIEnv* env, jclass, jint width, jint height) {
	browser data;
	
	CefWindowInfo window_info;
	CefBrowserSettings browser_settings;
	browser_settings.windowless_frame_rate = 40;

	window_info.SetAsWindowless(nullptr);

	data.handler = new Handler(width, height);
	data.client = new BrowserClient(data.handler);
	data.browser = CefBrowserHost::CreateBrowserSync(window_info, data.client.get(), "http://www.google.com", browser_settings, nullptr, nullptr);

	return env->NewDirectByteBuffer(&data, sizeof(browser));
}

/*
 * Class:     com_jnngl_system_WebBrowserBootstrap
 * Method:    Update
 * Signature: (Ljava/nio/ByteBuffer;II)V
 */
JNIEXPORT void JNICALL Java_com_jnngl_system_WebBrowserBootstrap_Update
(JNIEnv* env, jclass, jobject i, jint width, jint height) {
	browser* data = reinterpret_cast<browser*>(env->GetDirectBufferAddress(i));
	if (width != data->handler->width || height != data->handler->width) {
		data->handler->resize(width, height);
		data->browser->GetHost()->NotifyScreenInfoChanged();
	}
}

/*
 * Class:     com_jnngl_system_WebBrowserBootstrap
 * Method:    GetPixels
 * Signature: (Ljava/nio/ByteBuffer;)[I
 */
JNIEXPORT jintArray JNICALL Java_com_jnngl_system_WebBrowserBootstrap_GetPixels
(JNIEnv* env, jclass, jobject i) {
	browser* data = reinterpret_cast<browser*>(env->GetDirectBufferAddress(i));
	jsize size = data->handler->width * data->handler->height;
	jintArray jarr = env->NewIntArray(size);
	env->SetIntArrayRegion(jarr, 0, size, (jint*)data->handler->data);
	data->handler->queue = 0;
	return jarr;
}

/*
 * Class:     com_jnngl_system_WebBrowserBootstrap
 * Method:    ProcessTouch
 * Signature: (Ljava/nio/ByteBuffer;IIZ)V
 */
JNIEXPORT void JNICALL Java_com_jnngl_system_WebBrowserBootstrap_ProcessTouch
(JNIEnv* env, jclass, jobject i, jint x, jint y, jboolean type) {
	browser* data = reinterpret_cast<browser*>(env->GetDirectBufferAddress(i));
	data->browser->GetHost()->SendMouseClickEvent(
		_cef_mouse_event_t{ x, y, (uint32)cef_event_flags_t::EVENTFLAG_NONE },
		(CefBrowserHost::MouseButtonType)((int)type * 2), false, 1);
	Sleep(5);
	data->browser->GetHost()->SendMouseClickEvent(
		_cef_mouse_event_t{ x, y, (uint32)cef_event_flags_t::EVENTFLAG_NONE },
		(CefBrowserHost::MouseButtonType)((int)type * 2), true, 1);
}

JNIEXPORT void JNICALL Java_com_jnngl_system_WebBrowserBootstrap_RunLoop
(JNIEnv*, jclass) {
	CefRunMessageLoop();
}