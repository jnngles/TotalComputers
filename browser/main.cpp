
#include <main.h>
#include "base.hpp"
#include <iostream>
#include <thread>

TCMP_DEF_WND;

CefRefPtr<CefBrowser> browser;
CefRefPtr<BrowserClient> client;
Handler* handler;
jclass bootstrap;
jmethodID renderArray;
jintArray canvas;
bool error = false;
bool done = false;

int width;
int height; 
int wndHeight;
const char* path;

void Init(void*) {
    if (sizeof(int) == 4) {
        std::cout << "OK: sizeof(int) == 4" << std::endl;
    }
    else {
        std::cout << "ERROR: sizeof(int) != 4" << std::endl;
        std::cout << "aborting CEF initialization..." << std::endl;
        error = true;
        return;
    }

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
    std::string pathStr = std::string(path) + "/cefclient.exe";
    std::wstring pathWstr = std::wstring(pathStr.begin(), pathStr.end());
    settings.browser_subprocess_path = { _wcsdup(pathWstr.c_str()), pathStr.size() };
    bool result = CefInitialize(args, settings, nullptr, nullptr);

    if (!result) {
        error = true;
        std::cout << "Failed to initialize CEF" << std::endl;
    }

    handler = new Handler(width, height);

    CefWindowInfo window_info;
    CefBrowserSettings browser_settings;
    browser_settings.windowless_frame_rate = 60;

    window_info.SetAsWindowless(nullptr);

    client = new BrowserClient(handler);

    if (client.get()->GetRenderHandler() != handler) {
        std::cout << "RenderHandler check failed." << std::endl;
        error = true;
    }

    browser = CefBrowserHost::CreateBrowserSync(window_info, client.get(), "http://www.google.com", browser_settings, nullptr, nullptr);

    done = true;
    
    CefRunMessageLoop();
    _endthread();

}

void tc::app::OnStart(char* abspath, int argc, char* argv[]) {

    application->SetWidth(4 * 128);
    application->SetHeight(3 * 128 - 32);
    application->SetX(0);
    application->SetY(32);
    application->SetName("Web Browser");

    width = application->GetWidth();
    wndHeight = application->GetHeight();
    height = wndHeight;// -application->GetHeight() / 12;
    path = abspath;

    _beginthread(Init, 0, NULL);
    
}

bool tc::app::OnClose() {
    if (!error) {
        browser->GetHost()->CloseBrowser(true);
        browser = nullptr;
        client = nullptr;
    }
    return true;
}

void tc::app::Update() {
    if (!done || error) return;
    width = application->GetWidth();
    wndHeight = application->GetHeight();
    height = wndHeight;// -application->GetHeight() / 12;
    if (width != handler->width || height != handler->height) {
        handler->resize(width, height);
        browser->GetHost()->NotifyScreenInfoChanged();
    }
    application->RenderCanvas();
}

void tc::app::Render(awt::Graphics2D g) {
    if (!done || error || !g.instance || !handler || !handler->width || !handler->height || !tc::jEnv) {
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

void tc::app::ProcessInput(int x, int y, tc::InteractType type) {
    browser->GetHost()->SendMouseClickEvent(
        _cef_mouse_event_t{ x, y, (uint32)cef_event_flags_t::EVENTFLAG_NONE },
        (CefBrowserHost::MouseButtonType)((int)type * 2), false, 1);
    Sleep(10);
    browser->GetHost()->SendMouseClickEvent(
        _cef_mouse_event_t{ x, y, (uint32)cef_event_flags_t::EVENTFLAG_NONE },
        (CefBrowserHost::MouseButtonType)((int)type * 2), true, 1);
}