#ifndef __BROWSER_HPP_
#define __BROWSER_HPP_

#include <awt.h>
#include <utils.h>
#include "base.hpp"
#include <iostream>
#include <thread>

class NativeBrowser;

NativeBrowser* _browser;

void _NativeBrowser__initProc(void*);

class NativeBrowser {
public:	
	NativeBrowser() {}
	~NativeBrowser() {}

public:
	void Init(int width, int height, const char* path) {
        this->width = width;
        wndHeight = height;
        this->height = wndHeight;// -application->GetHeight() / 12;
        this->path = path;
        _browser = this;
        _beginthread(_NativeBrowser__initProc, 0, NULL);
        std::cout << browser << std::endl;
	}

    void ForceClose() {
        if (error) return;
        done = false;
        CefQuitMessageLoop();
        browser->GetHost()->CloseBrowser(true);
        CefShutdown();
        browser = nullptr;
        client = nullptr;
    }

    bool Update(int appWidth, int appHeight) {
        if (!done || error) return false;
        width = appWidth;
        height = appHeight;
        if (width != handler->width || height != handler->height) {
            handler->resize(width, height);
            browser->GetHost()->NotifyScreenInfoChanged();
        }
        return true;
    }

    void Render(awt::Graphics2D g) {
        if (!done || error || !g.instance || !handler || !handler->width || !handler->height || !tc::jEnv || !browser || !client) {
            return;
        }
        if (!handler->queue) return;

        if (!bootstrap || !renderArray) {
            bootstrap = tc::jEnv->FindClass("com/jnngl/system/WebBrowserBootstrap");
            renderArray = tc::jEnv->GetStaticMethodID(bootstrap, "renderArray", "(Ljava/awt/Graphics2D;[IIII)V");
        }
        jintArray canvas = ToJIntArray(handler->data, handler->width * handler->height);
        //if (!canvas || (handler->resized && width != handler->width && height != handler->height)) {
        //    std::cout << "Canvas creation attempt " << (size_t)handler->width << " " << (size_t)handler->height << " (" << (jsize)((size_t)handler->width * (size_t)handler->height) << ")" << std::endl;
        //    canvas = tc::jEnv->NewIntArray((jsize)handler->width * (jsize)handler->height);
        //    handler->resized = false;
        //}
        //if (canvas && handler->data) {
        //    tc::jEnv->SetIntArrayRegion(canvas, 0, (size_t)handler->width * (size_t)handler->height, (jint*)handler->data);
        if (bootstrap && renderArray)
            tc::jEnv->CallStaticVoidMethod(bootstrap, renderArray, g.instance, canvas, handler->height, handler->width, wndHeight);
        //}

        handler->queue = 0;
    }

    void ProcessInput(int x, int y, int type) {
        browser->GetHost()->SendMouseClickEvent(
            _cef_mouse_event_t{ x, y, (uint32)cef_event_flags_t::EVENTFLAG_NONE },
            (CefBrowserHost::MouseButtonType)(type * 2), false, 1);
        Sleep(2);
        browser->GetHost()->SendMouseClickEvent(
            _cef_mouse_event_t{ x, y, (uint32)cef_event_flags_t::EVENTFLAG_NONE },
            (CefBrowserHost::MouseButtonType)(type * 2), true, 1);
    }

public:
    CefRefPtr<CefBrowser> browser;
    CefRefPtr<BrowserClient> client;
    Handler* handler;
    jclass bootstrap;
    jmethodID renderArray;
    //jintArray canvas;
    bool error = false;
    bool done = false;

    int width;
    int height;
    int wndHeight;
    const char* path;

};

void _NativeBrowser__initProc(void*) {

    if (sizeof(int) == 4) {
        std::cout << "OK: sizeof(int) == 4" << std::endl;
    }
    else {
        std::cout << "ERROR: sizeof(int) != 4" << std::endl;
        std::cout << "aborting CEF initialization..." << std::endl;
        _browser->error = true;
        return;
    }

    CefMainArgs args(GetModuleHandle(NULL));

    {

        int result = CefExecuteProcess(args, nullptr, nullptr);

        if (result >= 0) {
            _browser->error = true;
            std::cout << "Failed to execute process" << std::endl;
        }

    }

    CefSettings settings;
    settings.multi_threaded_message_loop = false;
    settings.windowless_rendering_enabled = true;
    std::string pathStr = std::string(_browser->path) + "/cefclient.exe";
    std::wstring pathWstr = std::wstring(pathStr.begin(), pathStr.end());
    settings.browser_subprocess_path = { _wcsdup(pathWstr.c_str()), pathStr.size() };
    bool result = CefInitialize(args, settings, nullptr, nullptr);

    if (!result) {
        _browser->error = true;
        std::cout << "Failed to initialize CEF" << std::endl;
    }

    _browser->handler = new Handler(_browser->width, _browser->height);

    CefWindowInfo window_info;
    CefBrowserSettings browser_settings;
    browser_settings.windowless_frame_rate = 30;

    window_info.SetAsWindowless(nullptr);

    _browser->client = new BrowserClient(_browser->handler);

    if (_browser->client.get()->GetRenderHandler() != _browser->handler) {
        std::cout << "RenderHandler check failed." << std::endl;
        _browser->error = true;
    }

    _browser->browser = CefBrowserHost::CreateBrowserSync(window_info, _browser->client.get(), "http://www.google.com", browser_settings, nullptr, nullptr);

    _browser->done = true;

    CefRunMessageLoop();
    _endthread();
}

#endif