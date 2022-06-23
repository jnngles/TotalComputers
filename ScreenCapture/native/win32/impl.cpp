#include "com_jnngl_misc_ScreenCapture.h"
#include <Windows.h>
#include <vector>

BOOL CALLBACK EnumWindows_Callback_(HWND hwnd, LPARAM lparam) {
	if (!IsWindowVisible(hwnd)) return TRUE;
	(*reinterpret_cast<std::vector<HWND>*>(lparam)).push_back(hwnd);
	return TRUE;
}

JNIEXPORT jobjectArray JNICALL Java_com_jnngl_misc_ScreenCapture_win32applications
(JNIEnv* env, jobject) {
	std::vector<HWND> hwnds;
	EnumDesktopWindows(NULL, EnumWindows_Callback_, reinterpret_cast<LPARAM>(&hwnds));
	jobjectArray dst = env->NewObjectArray(hwnds.size(), env->FindClass("java/nio/ByteBuffer"), 0);
	for (int i = 0; i < hwnds.size(); i++) {
		env->SetObjectArrayElement(dst, i, env->NewDirectByteBuffer(hwnds[i], sizeof(HWND)));
	}
	return dst;
}

JNIEXPORT jobject JNICALL Java_com_jnngl_misc_ScreenCapture_win32screenScreenshot
(JNIEnv*, jobject, jintArray, jintArray) {
	// TODO: Implement this
	return 0;
}

JNIEXPORT jobject JNICALL Java_com_jnngl_misc_ScreenCapture_win32appScreenshot
(JNIEnv* env, jobject, jobject hwnd_buf, jintArray jw, jintArray jh) {
	HWND hwnd = (HWND)env->GetDirectBufferAddress(hwnd_buf);
	RECT rc;
	GetClientRect(hwnd, &rc);
	jint width = rc.right - rc.left;
	jint height = rc.bottom - rc.top;
	HDC hdc = GetDC(hwnd);
	HDC compatibleDC = CreateCompatibleDC(hdc);
	HBITMAP hbmp = CreateCompatibleBitmap(hdc, width, height);
	SelectObject(compatibleDC, hbmp);
	DeleteDC(compatibleDC);
	ReleaseDC(NULL, hdc);
	PrintWindow(hwnd, compatibleDC, PW_CLIENTONLY);
	char* data = (char*)calloc((size_t)3 * width * height, 1);
	GetBitmapBits(hbmp, 3 * width * height, data);
	env->SetIntArrayRegion(jw, 0, 1, &width);
	env->SetIntArrayRegion(jh, 0, 1, &height);
	return env->NewDirectByteBuffer(data, (size_t)3 * width * height);
}

JNIEXPORT jstring JNICALL Java_com_jnngl_misc_ScreenCapture_win32appName
(JNIEnv* env, jobject, jobject hwnd_buf) {
	HWND hwnd = (HWND) env->GetDirectBufferAddress(hwnd_buf);
	char title[256];
	GetWindowText(hwnd, title, sizeof(title));
	return env->NewStringUTF(title);
}