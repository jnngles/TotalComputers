
#include <main.h>
#include "base.hpp"
#include <iostream>
#include <thread>
#include <map>

TCMP_DEF_WND;

std::map<long long, CefRefPtr<CefBrowser>> browser;
CefRefPtr<BrowserClient> client;
Handler* handler;
jclass bootstrap;
jmethodID renderArray;
bool error = false;
std::map<long long, bool> done;

std::map<long long, int> width;
std::map<long long, int> height;
std::map<long long, int> wndHeight;
std::map<long long, const char*> path;

static bool initialized = false;

void Init(void* uid_p) {
    long long uid = reinterpret_cast<long long*>(uid_p)[0];

    if (!initialized) {

        std::cout << "Initializing CEF..." << std::endl;

        if (sizeof(int) != 4) {
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
        std::string pathStr = std::string(path[uid]) + "/cefclient.exe";
        std::wstring pathWstr = std::wstring(pathStr.begin(), pathStr.end());
        settings.browser_subprocess_path = { _wcsdup(pathWstr.c_str()), pathStr.size() };
        bool result = CefInitialize(args, settings, nullptr, nullptr);

        if (!result) {
            error = true;
            std::cout << "Failed to initialize CEF" << std::endl;
        }


    }

    if(!handler) handler = new Handler(width[uid], height[uid]);

    CefWindowInfo window_info;
    CefBrowserSettings browser_settings;
    browser_settings.windowless_frame_rate = 40;

    window_info.SetAsWindowless(nullptr);

    if(!client) client = new BrowserClient(handler);

    if (client.get()->GetRenderHandler() != handler) {
        std::cout << "RenderHandler check failed." << std::endl;
        error = true;
    }

    browser[uid] = CefBrowserHost::CreateBrowserSync(window_info, client.get(), "http://www.google.com", browser_settings, nullptr, nullptr);

    done[uid] = true;

    if (!initialized) {
        initialized = true;
        CefRunMessageLoop();
    }
    _endthread();

}

void tc::app::OnStart(long long uid, char* abspath, int argc, char* argv[]) {

    application->SetWidth(4 * 128);
    application->SetHeight(3 * 128 - 32);
    application->SetX(0);
    application->SetY(32);
    application->SetName("Web Browser");

    done[uid] = false;

    width[uid] = application->GetWidth();
    wndHeight[uid] = application->GetHeight();
    height[uid] = wndHeight[uid];// -application->GetHeight() / 12;
    path[uid] = abspath;

    _beginthread(Init, 0, new long long[] { uid });

}

bool tc::app::OnClose(long long uid) {
    if (error) return true;
    done[uid] = false;
    //browser[uid]->GetHost()->CloseBrowser(true);
    browser[uid] = nullptr;
    return true;
}

void tc::app::Update(long long uid) {
    if (!done[uid] || error) return;
    width[uid] = application->GetWidth();
    wndHeight[uid] = application->GetHeight();
    height[uid] = wndHeight[uid];// -application->GetHeight() / 12;
    if (width[uid] != handler->width || height[uid] != handler->height) {
        handler->resize(width[uid], height[uid]);
        browser[uid]->GetHost()->NotifyScreenInfoChanged();
    }
    application->RenderCanvas();
}

void tc::app::Render(long long uid, awt::Graphics2D g) {
    if (!done[uid] || error || !g.instance || !handler || !handler->width || !handler->height || !tc::jEnv) {
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

void tc::app::ProcessInput(long long uid, int x, int y, tc::InteractType type) {
    browser[uid]->GetHost()->SendMouseClickEvent(
        _cef_mouse_event_t{ x, y, (uint32)cef_event_flags_t::EVENTFLAG_NONE },
        (CefBrowserHost::MouseButtonType)((int)type * 2), false, 1);
    Sleep(5);
    browser[uid]->GetHost()->SendMouseClickEvent(
        _cef_mouse_event_t{ x, y, (uint32)cef_event_flags_t::EVENTFLAG_NONE },
        (CefBrowserHost::MouseButtonType)((int)type * 2), true, 1);
}